package io.github.flemmli97.runecraftory.api.datapack;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumSeason;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCSchedule;
import io.github.flemmli97.runecraftory.common.entities.npc.job.NPCJob;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModNPCJobs;
import io.github.flemmli97.runecraftory.common.utils.CodecHelper;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;

public record NPCData(@Nullable String name, @Nullable String surname,
                      Gender gender, NPCJob profession, @Nullable ResourceLocation look,
                      @Nullable Pair<EnumSeason, Integer> birthday,
                      int weight, String neutralGiftResponse,
                      Map<ConversationType, ResourceLocation> interactions,
                      Map<String, Gift> giftItems, @Nullable NPCSchedule.Schedule schedule,
                      @Nullable Map<Attribute, Double> baseStats, @Nullable Map<Attribute, Double> statIncrease,
                      int baseLevel, @Nullable ResourceLocation combatActions, int unique, boolean romanceable) {

    public static final NPCData DEFAULT_DATA = new NPCData(null, null, Gender.UNDEFINED, null, null, null, 1, "npc.default.gift.neutral",
            Map.of(), Map.of(), null, null, null, 1, null, 0, false);

    private static Map<ConversationType, ConversationSet> buildDefaultInteractionMap() {
        ImmutableMap.Builder<ConversationType, ConversationSet> builder = new ImmutableMap.Builder<>();
        for (ConversationType type : ConversationType.values())
            builder.put(type, new ConversationSet("npc.default." + type.key + ".default", Map.of()));
        return builder.build();
    }

    public static <E extends Enum<?>, T> Codec<Map<E, T>> filledMap(Class<E> enumClss, Codec<Map<E, T>> codec) {
        return codec.flatXmap(filledMapCheck(enumClss), filledMapCheck(enumClss));
    }

    public static <E extends Enum<?>, T> Function<Map<E, T>, DataResult<Map<E, T>>> filledMapCheck(Class<E> enumClss) {
        return map -> {
            E[] enums = enumClss.getEnumConstants();
            for (E e : enums)
                if (!map.containsKey(e)) {
                    return DataResult.error("Map is missing value for " + e);
                }
            return DataResult.success(map);
        };
    }

    public static final Codec<NPCData> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    filledMap(ConversationType.class, Codec.unboundedMap(CodecHelper.enumCodec(ConversationType.class, null), ResourceLocation.CODEC))
                            .fieldOf("interactions").forGetter(d -> d.interactions),
                    NPCSchedule.Schedule.CODEC.optionalFieldOf("schedule").forGetter(d -> Optional.ofNullable(d.schedule)),
                    NPCCombat.CODEC.optionalFieldOf("combat").forGetter(d -> {
                        NPCCombat combat = new NPCCombat(d.baseStats, d.statIncrease, d.baseLevel, d.combatActions);
                        if (combat.isNone())
                            return Optional.empty();
                        return Optional.of(combat);
                    }),

                    Codec.BOOL.optionalFieldOf("romanceable").forGetter(d -> Optional.of(d.romanceable)),
                    Codec.STRING.fieldOf("neutralGiftResponse").forGetter(d -> d.neutralGiftResponse),
                    Codec.unboundedMap(Codec.STRING, Gift.CODEC).fieldOf("giftItems").forGetter(d -> d.giftItems),

                    ResourceLocation.CODEC.optionalFieldOf("look").forGetter(d -> Optional.ofNullable(d.look)),
                    WorldUtils.DATE.optionalFieldOf("birthday").forGetter(d -> Optional.ofNullable(d.birthday)),
                    ExtraCodecs.POSITIVE_INT.fieldOf("weight").forGetter(d -> d.weight),
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("unique").forGetter(d -> d.unique == 0 ? Optional.empty() : Optional.of(d.unique)),

                    Codec.STRING.optionalFieldOf("name").forGetter(d -> Optional.ofNullable(d.name)),
                    Codec.STRING.optionalFieldOf("surname").forGetter(d -> Optional.ofNullable(d.surname)),
                    CodecHelper.enumCodec(Gender.class, Gender.UNDEFINED).fieldOf("gender").forGetter(d -> d.gender),
                    ModNPCJobs.CODEC.optionalFieldOf("profession").forGetter(d -> Optional.ofNullable(d.profession))
            ).apply(inst, (interactions, schedule, combat, romanceable, neutralGift, giftItems, look, birthday, weight, unique, name, surname, gender, profession) -> new NPCData(name.orElse(null), surname.orElse(null),
                    gender, profession.orElse(null), look.orElse(null), birthday.orElse(null), weight, neutralGift, interactions, giftItems, schedule.orElse(null), combat.map(d -> d.baseStats).orElse(null), combat.map(d -> d.statIncrease).orElse(null), combat.map(d -> d.baseLevel).orElse(1), combat.map(d -> d.npcAction).orElse(null), unique.orElse(0), romanceable.orElse(false))));

    public ConversationSet getConversation(ConversationType type) {
        ResourceLocation conversationId = this.interactions().get(type);
        return DataPackHandler.SERVER_PACK.npcConversationManager().get(conversationId, ConversationSet.DEFAULT.get(type));
    }

    public enum Gender {
        UNDEFINED,
        MALE,
        FEMALE
    }

    public enum ConversationType {

        GREETING("greeting"),
        TALK("talk"),
        FOLLOWYES("follow.yes"),
        FOLLOWNO("follow.no"),
        FOLLOWSTOP("follow.stop");

        public final String key;

        ConversationType(String key) {
            this.key = key;
        }
    }

    record NPCCombat(@Nullable Map<Attribute, Double> baseStats, @Nullable Map<Attribute, Double> statIncrease,
                     int baseLevel, @Nullable ResourceLocation npcAction) {

        public static final Codec<NPCCombat> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(
                        ExtraCodecs.POSITIVE_INT.optionalFieldOf("baseLevel").forGetter(d -> d.baseLevel != 1 ? Optional.of(d.baseLevel) : Optional.empty()),
                        ResourceLocation.CODEC.optionalFieldOf("combatActions").forGetter(d -> Optional.ofNullable(d.npcAction)),
                        Codec.unboundedMap(Registry.ATTRIBUTE.byNameCodec(), Codec.DOUBLE).optionalFieldOf("baseStats").forGetter(d -> Optional.ofNullable(d.baseStats)),
                        Codec.unboundedMap(Registry.ATTRIBUTE.byNameCodec(), Codec.DOUBLE).optionalFieldOf("statIncrease").forGetter(d -> Optional.ofNullable(d.statIncrease))
                ).apply(inst, (lvl, action, stats, inc) -> new NPCCombat(stats.orElse(null), inc.orElse(null), lvl.orElse(1), action.orElse(null))));

        public boolean isNone() {
            return this.baseLevel == 1 && this.baseStats == null && this.statIncrease == null && this.npcAction == null;
        }
    }

    public static class Builder {

        private final String name;
        private final String surname;
        private final Gender gender;
        private final int weight;
        private String neutralGiftResponse;
        private NPCJob profession;
        private final Map<ConversationType, ResourceLocation> interactions = new TreeMap<>();
        private final Map<String, Gift> giftItems = new LinkedHashMap<>();
        private Pair<EnumSeason, Integer> birthday;
        private NPCSchedule.Schedule schedule;
        private ResourceLocation look, combatAction;

        private final Map<Attribute, Double> baseStats = new TreeMap<>(ModAttributes.SORTED);
        private final Map<Attribute, Double> statIncrease = new TreeMap<>(ModAttributes.SORTED);
        private int baseLevel = 1;
        private int unique;
        private boolean romanceable;

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

        public Builder withLook(ResourceLocation id) {
            this.look = id;
            return this;
        }

        public Builder withBirthday(Pair<EnumSeason, Integer> birthday) {
            this.birthday = birthday;
            return this;
        }

        public Builder withProfession(NPCJob profession) {
            this.profession = profession;
            return this;
        }

        public Builder addInteraction(ConversationType type, ResourceLocation conversationId) {
            this.interactions.put(type, conversationId);
            return this;
        }

        public Builder addInteractionIfAbsent(ConversationType type, ResourceLocation conversationId) {
            this.interactions.putIfAbsent(type, conversationId);
            return this;
        }

        public Builder addGiftResponse(String id, Gift gift, String translation) {
            this.giftItems.put(id, gift);
            this.translations.put(gift.responseKey, translation);
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

        public Builder romanceable() {
            this.romanceable = true;
            return this;
        }

        public Builder withCombatAction(ResourceLocation action) {
            this.combatAction = action;
            return this;
        }

        public Map<String, String> getTranslations() {
            return this.translations;
        }

        public NPCData build() {
            if (this.neutralGiftResponse == null)
                throw new IllegalStateException("Neutral gift response not set.");
            for (ConversationType type : ConversationType.values()) {
                if (!this.interactions.containsKey(type))
                    throw new IllegalStateException("Missing interactions for " + type);
            }
            return new NPCData(this.name, this.surname, this.gender, this.profession, this.look, this.birthday, this.weight, this.neutralGiftResponse, this.interactions, this.giftItems, this.schedule,
                    this.baseStats.isEmpty() ? null : this.baseStats, this.statIncrease.isEmpty() ? null : this.statIncrease, this.baseLevel, this.combatAction, this.unique, this.romanceable);
        }
    }

    public static class ConversationSet {

        public static final Codec<ConversationSet> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(
                        Codec.STRING.fieldOf("fallbackKey").forGetter(d -> d.fallbackKey),
                        Codec.unboundedMap(Codec.STRING, Conversation.CODEC).fieldOf("conversations").forGetter(d -> d.conversations)
                ).apply(inst, ConversationSet::new));

        public static final Map<ConversationType, ConversationSet> DEFAULT = buildDefaultInteractionMap();

        private final String fallbackKey;
        private final Map<String, Conversation> conversations;

        public ConversationSet(String fallbackKey, Map<String, Conversation> conversations) {
            this.fallbackKey = fallbackKey;
            this.conversations = conversations;
        }

        public String getFallbackKey() {
            return this.fallbackKey;
        }

        public Map<String, Conversation> getConversations() {
            return this.conversations;
        }

        public static class Builder {

            private final String fallback;
            private final Map<String, Conversation> greetings = new LinkedHashMap<>();

            private final Map<String, String> translations = new LinkedHashMap<>();

            public Builder(String fallback, String enTranslation) {
                this.fallback = fallback;
                this.translations.put(this.fallback, enTranslation);
            }

            public Builder addConversation(Conversation.Builder conversation, String enTranslation) {
                return this.addConversation(conversation.translationKey, conversation, enTranslation);
            }

            public Builder addConversation(String key, Conversation.Builder conversation, String enTranslation) {
                this.greetings.put(key, conversation.build());
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

    public record Conversation(String translationKey, int minHearts, int maxHearts, boolean startingConversation,
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
                        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("minHearts").forGetter(d -> d.minHearts),
                        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("maxHearts").forGetter(d -> d.maxHearts)
                ).apply(inst, (start, action, cond, key, min, max) -> new Conversation(key, min, max, start.orElse(true), action.orElse(List.of()), cond.toArray(new LootItemCondition[0]))));

        public boolean test(int hearts, LootContext ctx) {
            if (this.minHearts > hearts || this.maxHearts < hearts)
                return false;
            for (LootItemCondition condition : this.conditions)
                if (!condition.test(ctx))
                    return false;
            return true;
        }

        public static class Builder {

            private final String translationKey;
            private final int minHearts, maxHearts;
            private boolean startingConversation = true;
            private final List<ConversationActionHolder> action = new ArrayList<>();
            private final Map<String, String> actionTranslation = new LinkedHashMap<>();
            private final List<LootItemCondition> conditions = new ArrayList<>();

            public Builder(String translationKey, int minHearts, int maxHearts) {
                this.translationKey = translationKey;
                this.minHearts = minHearts;
                this.maxHearts = maxHearts;
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
                        CodecHelper.enumCodec(ConversationAction.class, null).fieldOf("actions").forGetter(d -> d.action),
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

    public record NPCLook(Gender gender, @Nullable ResourceLocation texture, @Nullable String playerSkin, int weight,
                          List<Pair<ResourceLocation, ResourceLocation>> additionalFeatures) {

        public static final ResourceLocation DEFAULT_LOOK_ID = new ResourceLocation(RuneCraftory.MODID, "default_look");
        public static final NPCLook DEFAULT_LOOK = new NPCLook(Gender.MALE, new ResourceLocation("textures/entity/steve.png"), null, 0, List.of());

        public static final Codec<NPCLook> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(
                        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("weight").forGetter(d -> d.weight),
                        Codec.pair(ResourceLocation.CODEC, ResourceLocation.CODEC).listOf().fieldOf("additionalFeatures").forGetter(d -> d.additionalFeatures),

                        ResourceLocation.CODEC.optionalFieldOf("texture").forGetter(d -> Optional.ofNullable(d.texture)),
                        Codec.STRING.optionalFieldOf("player_skin").forGetter(d -> Optional.ofNullable(d.playerSkin)),
                        CodecHelper.enumCodec(Gender.class, Gender.UNDEFINED).fieldOf("gender").forGetter(d -> d.gender)
                ).apply(inst, (weight, features, text, skin, gender) -> {
                    if (text.isEmpty() && skin.isEmpty())
                        throw new IllegalArgumentException("Both texture or skin cant be null");
                    return new NPCLook(gender, text.orElse(null), skin.orElse(null), weight, features);
                }));

        public static NPCLook fromBuffer(FriendlyByteBuf buf) {
            ResourceLocation text = null;
            if (buf.readBoolean())
                text = buf.readResourceLocation();
            String skin = null;
            if (buf.readBoolean())
                skin = buf.readUtf();
            int size = buf.readInt();
            List<Pair<ResourceLocation, ResourceLocation>> additional = new ArrayList<>();
            for (int i = 0; i < size; i++)
                additional.add(Pair.of(buf.readResourceLocation(), buf.readResourceLocation()));
            return new NPCLook(buf.readEnum(Gender.class), text, skin, buf.readInt(), additional);
        }

        public void writeToBuffer(FriendlyByteBuf buf) {
            buf.writeBoolean(this.texture != null);
            if (this.texture != null)
                buf.writeResourceLocation(this.texture);
            buf.writeBoolean(this.playerSkin != null);
            if (this.playerSkin != null)
                buf.writeUtf(this.playerSkin);
            buf.writeInt(this.additionalFeatures.size());
            this.additionalFeatures.forEach(p -> {
                buf.writeResourceLocation(p.getFirst());
                buf.writeResourceLocation(p.getSecond());
            });
            buf.writeEnum(this.gender());
            buf.writeInt(this.weight());
        }
    }
}
