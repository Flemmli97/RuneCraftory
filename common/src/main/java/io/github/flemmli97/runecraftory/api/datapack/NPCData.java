package io.github.flemmli97.runecraftory.api.datapack;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumSeason;
import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureHolder;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureType;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCSchedule;
import io.github.flemmli97.runecraftory.common.entities.npc.job.NPCJob;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModNPCJobs;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public record NPCData(@Nullable String name, @Nullable String surname,
                      Gender gender, List<NPCJob> profession, @Nullable List<NPCLookId> look,
                      @Nullable Pair<EnumSeason, Integer> birthday,
                      int weight, String neutralGiftResponse,
                      Map<ConversationContext, ResourceLocation> interactions,
                      QuestHandler questHandler,
                      Map<String, Gift> giftItems, @Nullable NPCSchedule.Schedule schedule,
                      @Nullable Map<Attribute, Double> baseStats, @Nullable Map<Attribute, Double> statIncrease,
                      int baseLevel, @Nullable List<ResourceLocation> combatActions, int unique,
                      RelationShipState relationShipState, List<ResourceLocation> possibleChildren) {

    public static final Map<Attribute, Double> DEFAULT_GAIN = Map.of(Attributes.MAX_HEALTH, 3d, Attributes.ATTACK_DAMAGE, 1d,
            ModAttributes.DEFENCE.get(), 0.5d, ModAttributes.MAGIC.get(), 1d, ModAttributes.MAGIC_DEFENCE.get(), 0.5d);
    public static final NPCData DEFAULT_DATA = new NPCData(null, null, Gender.UNDEFINED, List.of(), null, null, 1, "npc.default.gift.neutral",
            Map.of(), new QuestHandler(Map.of(), Set.of()), Map.of(), null, null, null, 1, null, 0, RelationShipState.DEFAULT, List.of());

    public static <T> Codec<Map<ConversationContext, T>> filledMap(Codec<Map<ConversationContext, T>> codec) {
        Function<Map<ConversationContext, T>, DataResult<Map<ConversationContext, T>>> check = map -> {
            List<ResourceLocation> missing = new ArrayList<>();
            for (ConversationContext e : ConversationContext.getRegistered()) {
                if (!map.containsKey(e)) {
                    missing.add(e.key());
                }
            }
            if (!missing.isEmpty())
                return DataResult.error("Conversation map is missing conversation for contexts: " + missing, map);
            return DataResult.success(map);
        };
        return codec.flatXmap(check, DataResult::success);
    }

    public static final Codec<NPCData> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    filledMap(Codec.unboundedMap(ResourceLocation.CODEC.flatComapMap(ConversationContext::get, ctx -> DataResult.success(ctx.key())), ResourceLocation.CODEC))
                            .fieldOf("interactions").forGetter(d -> d.interactions),
                    QuestHandler.CODEC.fieldOf("questHandler").forGetter(d -> d.questHandler),
                    NPCSchedule.Schedule.CODEC.optionalFieldOf("schedule").forGetter(d -> Optional.ofNullable(d.schedule)),
                    NPCCombat.CODEC.optionalFieldOf("combat").forGetter(d -> {
                        NPCCombat combat = new NPCCombat(d.baseStats, d.statIncrease, d.baseLevel, d.combatActions);
                        if (combat.isNone())
                            return Optional.empty();
                        return Optional.of(combat);
                    }),

                    RelationStruct.CODEC.fieldOf("relation").forGetter(d -> new RelationStruct(d.relationShipState, d.possibleChildren)),
                    Codec.STRING.fieldOf("neutralGiftResponse").forGetter(d -> d.neutralGiftResponse),
                    Codec.unboundedMap(Codec.STRING, Gift.CODEC).fieldOf("giftItems").forGetter(d -> d.giftItems),

                    NPCLookId.CODEC.listOf().optionalFieldOf("look").forGetter(d -> Optional.ofNullable(d.look == null || d.look.isEmpty() ? null : d.look)),
                    WorldUtils.DATE.optionalFieldOf("birthday").forGetter(d -> Optional.ofNullable(d.birthday)),
                    ExtraCodecs.POSITIVE_INT.fieldOf("weight").forGetter(d -> d.weight),
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("unique").forGetter(d -> d.unique == 0 ? Optional.empty() : Optional.of(d.unique)),

                    Codec.STRING.optionalFieldOf("name").forGetter(d -> Optional.ofNullable(d.name)),
                    Codec.STRING.optionalFieldOf("surname").forGetter(d -> Optional.ofNullable(d.surname)),
                    CodecUtils.stringEnumCodec(Gender.class, Gender.UNDEFINED).fieldOf("gender").forGetter(d -> d.gender),
                    ModNPCJobs.CODEC.listOf().optionalFieldOf("profession").forGetter(d -> d.profession.isEmpty() ? Optional.empty() : Optional.of(d.profession))
            ).apply(inst, (interactions, questHandler, schedule, combat, relation, neutralGift, giftItems, look, birthday, weight, unique, name, surname, gender, profession) ->
                    new NPCData(name.orElse(null), surname.orElse(null), gender, profession.orElse(List.of()), look.orElse(null), birthday.orElse(null),
                            weight, neutralGift, interactions, questHandler, giftItems, schedule.orElse(null), combat.map(d -> d.baseStats).orElse(null),
                            combat.map(d -> d.statIncrease).orElse(null), combat.map(d -> d.baseLevel).orElse(1), combat.map(d -> d.npcAction).orElse(null),
                            unique.orElse(0), relation.relationShipState, relation.possibleChildren)));

    public ConversationSet getConversation(ConversationContext convCtx) {
        ResourceLocation conversationId = this.interactions().get(convCtx);
        if (conversationId == null)
            return ConversationSet.DEFAULT;
        return DataPackHandler.INSTANCE.npcConversationManager().get(conversationId, ConversationSet.DEFAULT);
    }

    public ConversationSet getFromQuest(ResourceLocation quest, int state) {
        QuestResponses responses = this.questHandler().responses().get(quest);
        ConversationSet fallback = new ConversationSet("npc.default.quest.response.default", Map.of());
        if (responses == null)
            return fallback;
        ResourceLocation conversationId = switch (state) {
            case -2 -> responses.endID;
            case -1 -> responses.startID;
            default -> {
                if (state == 0 || !responses.hasSequence)
                    yield responses.activeID;
                else
                    yield new ResourceLocation(responses.activeID.getNamespace(), responses.activeID.getPath() + "_" + state);
            }
        };
        return DataPackHandler.INSTANCE.npcConversationManager().get(conversationId, fallback);
    }

    public enum Gender {
        UNDEFINED,
        MALE,
        FEMALE
    }

    public enum RelationShipState {
        DEFAULT,
        NON_ROMANCEABLE,
        NO_ROMANCE_NPC,
        NO_ROMANCE
    }

    record RelationStruct(RelationShipState relationShipState, List<ResourceLocation> possibleChildren) {

        public static final Codec<RelationStruct> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(
                        CodecUtils.stringEnumCodec(RelationShipState.class, RelationShipState.DEFAULT).fieldOf("relationShipState").forGetter(d -> d.relationShipState),
                        ResourceLocation.CODEC.listOf().optionalFieldOf("possibleChildren").forGetter(d -> d.possibleChildren.isEmpty() ? Optional.empty() : Optional.of(d.possibleChildren))
                ).apply(inst, (state, childs) -> new RelationStruct(state, childs.orElse(List.of()))));

    }

    record NPCCombat(@Nullable Map<Attribute, Double> baseStats, @Nullable Map<Attribute, Double> statIncrease,
                     int baseLevel, @Nullable List<ResourceLocation> npcAction) {

        public static final Codec<NPCCombat> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(
                        ExtraCodecs.POSITIVE_INT.optionalFieldOf("baseLevel").forGetter(d -> d.baseLevel != 1 ? Optional.of(d.baseLevel) : Optional.empty()),
                        ResourceLocation.CODEC.listOf().optionalFieldOf("combatActions").forGetter(d -> Optional.ofNullable(d.npcAction == null || d.npcAction.isEmpty() ? null : d.npcAction)),
                        Codec.unboundedMap(Registry.ATTRIBUTE.byNameCodec(), Codec.DOUBLE).optionalFieldOf("baseStats").forGetter(d -> Optional.ofNullable(d.baseStats)),
                        Codec.unboundedMap(Registry.ATTRIBUTE.byNameCodec(), Codec.DOUBLE).optionalFieldOf("statIncrease").forGetter(d -> Optional.ofNullable(d.statIncrease))
                ).apply(inst, (lvl, action, stats, inc) -> new NPCCombat(stats.orElse(null), inc.orElse(null), lvl.orElse(1), action.orElse(null))));

        public boolean isNone() {
            return this.baseLevel == 1 && this.baseStats == null && this.statIncrease == null && this.npcAction == null;
        }
    }

    public record QuestHandler(Map<ResourceLocation, QuestResponses> responses, Set<ResourceLocation> requiredQuests) {
        public static final Codec<QuestHandler> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(
                        Codec.unboundedMap(ResourceLocation.CODEC, QuestResponses.CODEC).fieldOf("responses").forGetter(d -> d.responses),
                        ResourceLocation.CODEC.listOf().fieldOf("requiredQuests").forGetter(d -> List.copyOf(d.requiredQuests))
                ).apply(inst, (responses, required) -> new QuestHandler(responses, Set.copyOf(required))));
    }

    public record QuestResponses(ResourceLocation startID, ResourceLocation activeID, boolean hasSequence,
                                 ResourceLocation endID) {
        public static final Codec<QuestResponses> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(
                        ResourceLocation.CODEC.fieldOf("startID").forGetter(d -> d.startID),
                        ResourceLocation.CODEC.fieldOf("activeID").forGetter(d -> d.activeID),
                        Codec.BOOL.fieldOf("hasSequence").forGetter(d -> d.hasSequence),
                        ResourceLocation.CODEC.fieldOf("endID").forGetter(d -> d.endID)
                ).apply(inst, QuestResponses::new));
    }

    public static class Builder {

        private final String name;
        private final String surname;
        private final Gender gender;
        private final int weight;
        private String neutralGiftResponse;
        private final List<NPCJob> professions = new ArrayList<>();
        private final Map<ConversationContext, ResourceLocation> interactions = new LinkedHashMap<>();
        private final Map<String, Gift> giftItems = new LinkedHashMap<>();
        private Pair<EnumSeason, Integer> birthday;
        private NPCSchedule.Schedule schedule;
        private List<NPCLookId> look;
        private List<ResourceLocation> combatAction;

        private final Map<Attribute, Double> baseStats = new TreeMap<>(ModAttributes.SORTED);
        private final Map<Attribute, Double> statIncrease = new TreeMap<>(ModAttributes.SORTED);
        private int baseLevel = 1;
        private int unique;
        private RelationShipState relationShipState = RelationShipState.DEFAULT;
        private List<ResourceLocation> possibleChildIds = new ArrayList<>();

        private final Map<ResourceLocation, QuestResponses> responses = new LinkedHashMap<>();
        private final Set<ResourceLocation> requiredQuests = new LinkedHashSet<>();

        private final Map<String, String> translations = new LinkedHashMap<>();

        public Builder(int weight) {
            this(weight, null, null, Gender.UNDEFINED);
        }

        public Builder(int weight, String name, Gender gender) {
            this(weight, name, null, gender);
        }

        public Builder(int weight, String name, String surname, Gender gender) {
            this.weight = weight;
            this.name = name;
            this.surname = surname;
            this.gender = gender;
            if (this.name != null) {
                this.neutralGiftResponse = "npc." + name.toLowerCase(Locale.ROOT) + ".default.gift";
            }
        }

        public Builder setNeutralGiftResponse(String neutralGiftResponse, String translation) {
            this.neutralGiftResponse = neutralGiftResponse;
            this.translations.put(this.neutralGiftResponse, translation);
            return this;
        }

        public Builder withLook(NPCLookId... looks) {
            this.look = List.of(looks);
            return this;
        }

        public Builder withBirthday(Pair<EnumSeason, Integer> birthday) {
            this.birthday = birthday;
            return this;
        }

        public Builder withProfession(NPCJob... professions) {
            this.professions.addAll(List.of(professions));
            return this;
        }

        public Builder addInteraction(ConversationContext convCtx, ResourceLocation conversationId) {
            this.interactions.put(convCtx, conversationId);
            return this;
        }

        public Builder addInteractionIfAbsent(ConversationContext convCtx, ResourceLocation conversationId) {
            this.interactions.putIfAbsent(convCtx, conversationId);
            return this;
        }

        public Builder addGiftResponse(String id, Gift gift, String translation) {
            this.giftItems.put(id, gift);
            this.translations.put(gift.responseKey, translation);
            return this;
        }

        public Builder addTranslation(String key, String translation) {
            this.translations.put(key, translation);
            return this;
        }

        public Builder withSchedule(NPCSchedule.Schedule schedule) {
            this.schedule = schedule;
            return this;
        }

        public Builder setBaseStat(Attribute attribute, double val) {
            this.baseStats.put(attribute, val);
            return this;
        }

        public Builder setStatIncrease(Attribute attribute, double val) {
            this.statIncrease.put(attribute, val);
            return this;
        }

        public Builder setBaseLevel(int level) {
            this.baseLevel = Math.max(1, level);
            return this;
        }

        public Builder setUnique(int amount) {
            this.unique = amount;
            return this;
        }

        public Builder relationState(RelationShipState state) {
            this.relationShipState = state;
            return this;
        }

        public Builder addChild(ResourceLocation child) {
            this.possibleChildIds.add(child);
            return this;
        }

        public Builder withCombatActions(ResourceLocation... actions) {
            this.combatAction = List.of(actions);
            return this;
        }

        public Builder requiresQuest(ResourceLocation quest) {
            this.requiredQuests.add(quest);
            return this;
        }

        public Builder addQuestResponse(ResourceLocation quest, ResourceLocation startingID, ResourceLocation activeID, boolean hasSequence, ResourceLocation endID) {
            this.responses.put(quest, new QuestResponses(startingID, activeID, hasSequence, endID));
            return this;
        }

        public Map<String, String> getTranslations() {
            return this.translations;
        }

        public NPCData build() {
            if (this.neutralGiftResponse == null)
                throw new IllegalStateException("Neutral gift response not set.");
            for (ConversationContext convCtx : ConversationContext.getRegistered()) {
                if (!this.interactions.containsKey(convCtx))
                    throw new IllegalStateException("Missing interactions for " + convCtx);
            }
            return new NPCData(this.name, this.surname, this.gender, this.professions, this.look, this.birthday, this.weight, this.neutralGiftResponse, this.interactions,
                    new QuestHandler(this.responses, this.requiredQuests), this.giftItems, this.schedule,
                    this.baseStats.isEmpty() ? null : this.baseStats, this.statIncrease.isEmpty() ? null : this.statIncrease,
                    this.baseLevel, this.combatAction, this.unique, this.relationShipState, this.possibleChildIds);
        }
    }

    public record NPCLookId(ResourceLocation id, Gender gender) {

        public static final Codec<NPCLookId> CODEC = Codec.either(ResourceLocation.CODEC, RecordCodecBuilder.<NPCLookId>create(inst ->
                inst.group(
                        ResourceLocation.CODEC.fieldOf("look").forGetter(NPCLookId::id),
                        CodecUtils.stringEnumCodec(Gender.class, Gender.UNDEFINED).fieldOf("gender").forGetter(NPCLookId::gender)
                ).apply(inst, NPCLookId::new))).flatXmap(d -> {
            if (d.left().isPresent())
                return DataResult.success(new NPCLookId(d.left().get()));
            return d.right().map(DataResult::success).orElse(DataResult.error("Failed to parse npc look id"));
        }, i -> DataResult.success(i.gender == Gender.UNDEFINED ? Either.left(i.id()) : Either.right(i)));

        public NPCLookId(String namespace, String path) {
            this(new ResourceLocation(namespace, path));
        }

        public NPCLookId(ResourceLocation id) {
            this(id, Gender.UNDEFINED);
        }
    }

    public record ConversationSet(String fallbackKey, Map<String, Conversation> conversations) {

        public static final Codec<ConversationSet> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(
                        Codec.STRING.optionalFieldOf("fallbackKey").forGetter(d -> Optional.of(d.fallbackKey)),
                        Codec.unboundedMap(Codec.STRING, Conversation.CODEC).fieldOf("conversations").forGetter(d -> d.conversations)
                ).apply(inst, (fallback, convs) -> new ConversationSet(fallback.orElse(""), convs)));

        public static final ConversationSet DEFAULT = new ConversationSet("npc.conversation.missing", Map.of());

        public static class Builder {

            private final String fallback;
            private final Map<String, Conversation> greetings = new LinkedHashMap<>();

            private final Map<String, String> translations = new LinkedHashMap<>();

            public Builder() {
                this.fallback = "";
            }

            public Builder(String fallback, String enTranslation) {
                this.fallback = fallback;
                this.translations.put(this.fallback, enTranslation);
            }

            public Builder addConversation(Conversation.Builder conversation, String enTranslation) {
                return this.addConversation(conversation.translationKey, conversation, enTranslation);
            }

            public Builder addConversation(String key, Conversation.Builder conversation, String enTranslation) {
                this.greetings.put(key, conversation.build());
                if (this.translations.containsKey(conversation.translationKey))
                    throw new IllegalStateException("Duplicate translation key " + conversation.translationKey);
                this.translations.put(conversation.translationKey, enTranslation);
                this.translations.putAll(conversation.actionTranslation);
                return this;
            }

            public Map<String, String> getTranslations() {
                return this.translations;
            }

            public ConversationSet build() {
                return new ConversationSet(this.fallback, this.greetings);
            }
        }
    }

    public record Conversation(String translationKey, @Nullable NumberProvider minHearts,
                               @Nullable NumberProvider maxHearts, boolean startingConversation,
                               List<ConversationActionHolder> actions, LootItemCondition... conditions) {

        private static final Gson GSON = Deserializers.createConditionSerializer().create();
        private static final JsonDeserializationContext CTX_DESERIALIZER = GSON::fromJson;
        private static final JsonSerializationContext CTX_SERIALIZER = new JsonSerializationContext() {
            @Override
            public JsonElement serialize(Object src) {
                return GSON.toJsonTree(src);
            }

            @Override
            public JsonElement serialize(Object src, Type typeOfSrc) {
                return GSON.toJsonTree(src, typeOfSrc);
            }
        };

        @SuppressWarnings("unchecked")
        public static final Codec<LootItemCondition> LOOT_ITEM_CONDITION_CODEC = Codec.PASSTHROUGH.comapFlatMap(dynamic -> {
            JsonElement json = dynamic.convert(JsonOps.INSTANCE).getValue();
            if (json instanceof JsonObject obj) {
                String type = GsonHelper.getAsString(obj, "type", "");
                if (type.isEmpty()) {
                    throw new JsonSyntaxException("Missing LootConditionType");
                }
                LootItemConditionType conditionType = Registry.LOOT_CONDITION_TYPE.get(new ResourceLocation(type));
                if (conditionType == null) {
                    throw new JsonSyntaxException("Unknown type '" + type + "'");
                }
                return DataResult.success(conditionType.getSerializer().deserialize(obj, CTX_DESERIALIZER));
            }
            return DataResult.error("Not a json object: " + json);
        }, conditon -> {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", Registry.LOOT_CONDITION_TYPE.getKey(conditon.getType()).toString());
            ((Serializer<LootItemCondition>) conditon.getType().getSerializer()).serialize(obj, conditon, CTX_SERIALIZER);
            return new Dynamic<>(JsonOps.INSTANCE, obj);
        });
        public static final Codec<Conversation> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(
                        Codec.BOOL.optionalFieldOf("startingConversation").forGetter(d -> d.startingConversation ? Optional.empty() : Optional.of(false)),
                        ConversationActionHolder.CODEC.listOf().optionalFieldOf("actions").forGetter(d -> d.actions.isEmpty() ? Optional.empty() : Optional.of(d.actions)),
                        LOOT_ITEM_CONDITION_CODEC.listOf().fieldOf("conditions").forGetter(d -> Arrays.stream(d.conditions).toList()),

                        Codec.STRING.fieldOf("translationKey").forGetter(d -> d.translationKey),
                        CodecUtils.jsonCodecBuilder(GSON, NumberProvider.class, "NumberProvider").optionalFieldOf("minHearts").forGetter(d -> Optional.ofNullable(d.minHearts)),
                        CodecUtils.jsonCodecBuilder(GSON, NumberProvider.class, "NumberProvider").optionalFieldOf("maxHearts").forGetter(d -> Optional.ofNullable(d.maxHearts))
                ).apply(inst, (start, action, cond, key, min, max) -> new Conversation(key, min.orElse(null), max.orElse(null), start.orElse(true), action.orElse(List.of()), cond.toArray(new LootItemCondition[0]))));

        public boolean test(int hearts, LootContext ctx) {
            if (this.minHearts != null && this.minHearts.getInt(ctx) > hearts)
                return false;
            if (this.maxHearts != null && this.maxHearts.getInt(ctx) < hearts)
                return false;
            for (LootItemCondition condition : this.conditions)
                if (!condition.test(ctx))
                    return false;
            return true;
        }

        public static class Builder {

            private final String translationKey;
            private NumberProvider minHearts, maxHearts;
            private boolean startingConversation = true;
            private final List<ConversationActionHolder> action = new ArrayList<>();
            private final Map<String, String> actionTranslation = new LinkedHashMap<>();
            private final List<LootItemCondition> conditions = new ArrayList<>();

            public Builder(String translationKey) {
                this.translationKey = translationKey;
            }

            public Builder min(@Nullable NumberProvider minHearts) {
                this.minHearts = minHearts;
                return this;
            }

            public Builder max(@Nullable NumberProvider maxHearts) {
                this.maxHearts = maxHearts;
                return this;
            }

            public Builder setAnswer() {
                this.startingConversation = false;
                return this;
            }

            public Builder addAction(ConversationActionHolder action, String enTranslation) {
                this.action.add(action);
                this.actionTranslation.put(action.translationKey, enTranslation);
                return this;
            }

            public Builder addCondition(LootItemCondition condition) {
                this.conditions.add(condition);
                return this;
            }

            public Conversation build() {
                return new Conversation(this.translationKey, this.minHearts, this.maxHearts, this.startingConversation, this.action, this.conditions.toArray(new LootItemCondition[0]));
            }
        }
    }

    public record ConversationActionHolder(String translationKey, ConversationAction action, String actionValue,
                                           int friendXP) {

        public static final Codec<ConversationActionHolder> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(
                        Codec.STRING.fieldOf("translationKey").forGetter(d -> d.translationKey),
                        CodecUtils.stringEnumCodec(ConversationAction.class, null).fieldOf("actions").forGetter(d -> d.action),
                        Codec.STRING.fieldOf("value").forGetter(d -> d.actionValue),
                        Codec.INT.optionalFieldOf("friendXP").forGetter(d -> d.friendXP != 0 ? Optional.of(d.friendXP) : Optional.empty())
                ).apply(inst, (key, action, value, xp) -> new ConversationActionHolder(key, action, value, xp.orElse(0))));
    }

    public enum ConversationAction {
        ANSWER,
        QUEST
    }

    public record Gift(TagKey<Item> item, String responseKey, int xp) {

        public static final Codec<Gift> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(
                        ResourceLocation.CODEC.optionalFieldOf("items").forGetter(d -> Optional.ofNullable(d.item).map(TagKey::location)),
                        Codec.STRING.fieldOf("responseKey").forGetter(d -> d.responseKey),
                        Codec.INT.fieldOf("xp").forGetter(d -> d.xp)
                ).apply(inst, (items, respone, xp) -> new Gift(items.map(PlatformUtils.INSTANCE::itemTag).orElse(null), respone, xp)));
    }

    public enum GiftType {
        HATE,
        DISLIKE,
        NEUTRAL,
        LIKE,
        LOVE;

        public static GiftType ofXP(int xp) {
            if (xp < -20)
                return HATE;
            if (xp < 0)
                return DISLIKE;
            if (xp < 20)
                return NEUTRAL;
            if (xp < 40)
                return LIKE;
            return LOVE;
        }
    }

    public record NPCLook(Gender gender, @Nullable String playerSkin, int weight,
                          Map<NPCFeatureType<?>, NPCFeatureHolder<?>> additionalFeatures) {

        public static final ResourceLocation DEFAULT_LOOK_ID = new ResourceLocation(RuneCraftory.MODID, "default_look");
        public static final NPCLook DEFAULT_LOOK = new NPCLook(Gender.MALE, null, 0, Map.of());

        public static final Codec<NPCLook> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(Codec.STRING.optionalFieldOf("player_skin").forGetter(d -> Optional.ofNullable(d.playerSkin)),
                        CodecUtils.stringEnumCodec(Gender.class, Gender.UNDEFINED).fieldOf("gender").forGetter(d -> d.gender),
                        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("weight").forGetter(d -> d.weight),
                        NPCFeature.CODEC.listOf().fieldOf("additionalFeatures").forGetter(d -> List.copyOf(d.additionalFeatures.values()))
                ).apply(inst, (skin, gender, weight, features) -> new NPCLook(gender, skin.orElse(null), weight, features
                        .stream().collect(Collectors.toMap(
                                NPCFeatureHolder::getType,
                                h -> h,
                                (e1, e2) -> e1,
                                HashMap::new
                        )))));

        public static NPCLook fromBuffer(FriendlyByteBuf buf) {
            String skin = null;
            if (buf.readBoolean())
                skin = buf.readUtf();
            return new NPCLook(buf.readEnum(Gender.class), skin, buf.readInt(), Map.of());
        }

        public void writeToBuffer(FriendlyByteBuf buf) {
            buf.writeBoolean(this.playerSkin != null);
            if (this.playerSkin != null)
                buf.writeUtf(this.playerSkin);
            buf.writeEnum(this.gender());
            buf.writeInt(this.weight());
        }
    }
}
