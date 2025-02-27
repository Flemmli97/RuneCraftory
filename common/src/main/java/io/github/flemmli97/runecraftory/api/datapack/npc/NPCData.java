package io.github.flemmli97.runecraftory.api.datapack.npc;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.datapack.ConversationContext;
import io.github.flemmli97.runecraftory.api.enums.EnumSeason;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCSchedule;
import io.github.flemmli97.runecraftory.common.entities.npc.QuestConversationContext;
import io.github.flemmli97.runecraftory.common.entities.npc.job.NPCJob;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModNPCJobs;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;

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
                    QuestHandler.CODEC.fieldOf("quest_handler").forGetter(d -> d.questHandler),

                    NPCSchedule.Schedule.CODEC.optionalFieldOf("schedule").forGetter(d -> Optional.ofNullable(d.schedule)),
                    NPCCombat.CODEC.optionalFieldOf("combat").forGetter(d -> {
                        NPCCombat combat = new NPCCombat(d.baseStats, d.statIncrease, d.baseLevel, d.combatActions);
                        if (combat.isNone())
                            return Optional.empty();
                        return Optional.of(combat);
                    }),

                    RelationStruct.CODEC.fieldOf("relation").forGetter(d -> new RelationStruct(d.relationShipState, d.possibleChildren)),
                    Codec.STRING.fieldOf("neutral_gift_response").forGetter(d -> d.neutralGiftResponse),
                    Codec.unboundedMap(Codec.STRING, Gift.CODEC).fieldOf("gift_items").forGetter(d -> d.giftItems),

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
        ConversationSet fallback = new ConversationSet("npc.conversation.context.missing", convCtx.key(), Map.of());
        if (conversationId == null)
            return fallback;
        fallback = new ConversationSet("npc.conversation.missing", convCtx.key(), Map.of());
        return DataPackHandler.INSTANCE.npcConversationManager().get(conversationId, fallback);
    }

    public ConversationSet getFromQuest(ResourceLocation quest, QuestConversationContext ctx, int state) {
        QuestResponses responses = this.questHandler().responses().get(quest);
        ConversationSet fallback = new ConversationSet("npc.default.quest.response.missing", quest, Map.of());
        if (responses == null)
            return fallback;
        ResourceLocation conversationId = switch (ctx) {
            case NOT_STARTED -> {
                if (state <= 0)
                    yield responses.startID;
                else
                    yield new ResourceLocation(responses.startID.getNamespace(), responses.startID.getPath() + "_" + state);
            }
            case IN_PROGRESS -> {
                if (state == 0)
                    yield responses.activeID;
                else
                    yield new ResourceLocation(responses.activeID.getNamespace(), responses.activeID.getPath() + "_" + state);
            }
            case COMPLETED -> responses.endID;
        };
        fallback = new ConversationSet("npc.conversation.missing", conversationId, Map.of());
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
                        CodecUtils.stringEnumCodec(RelationShipState.class, RelationShipState.DEFAULT).fieldOf("relation_ship_state").forGetter(d -> d.relationShipState),
                        ResourceLocation.CODEC.listOf().optionalFieldOf("possible_children").forGetter(d -> d.possibleChildren.isEmpty() ? Optional.empty() : Optional.of(d.possibleChildren))
                ).apply(inst, (state, childs) -> new RelationStruct(state, childs.orElse(List.of()))));

    }

    record NPCCombat(@Nullable Map<Attribute, Double> baseStats, @Nullable Map<Attribute, Double> statIncrease,
                     int baseLevel, @Nullable List<ResourceLocation> npcAction) {

        public static final Codec<NPCCombat> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(
                        ExtraCodecs.POSITIVE_INT.optionalFieldOf("base_level").forGetter(d -> d.baseLevel != 1 ? Optional.of(d.baseLevel) : Optional.empty()),
                        ResourceLocation.CODEC.listOf().optionalFieldOf("combat_actions").forGetter(d -> Optional.ofNullable(d.npcAction == null || d.npcAction.isEmpty() ? null : d.npcAction)),
                        Codec.unboundedMap(Registry.ATTRIBUTE.byNameCodec(), Codec.DOUBLE).optionalFieldOf("base_stats").forGetter(d -> Optional.ofNullable(d.baseStats)),
                        Codec.unboundedMap(Registry.ATTRIBUTE.byNameCodec(), Codec.DOUBLE).optionalFieldOf("stat_increase").forGetter(d -> Optional.ofNullable(d.statIncrease))
                ).apply(inst, (lvl, action, stats, inc) -> new NPCCombat(stats.orElse(null), inc.orElse(null), lvl.orElse(1), action.orElse(null))));

        public boolean isNone() {
            return this.baseLevel == 1 && this.baseStats == null && this.statIncrease == null && this.npcAction == null;
        }
    }

    public record QuestHandler(Map<ResourceLocation, QuestResponses> responses, Set<ResourceLocation> requiredQuests) {
        public static final Codec<QuestHandler> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(
                        Codec.unboundedMap(ResourceLocation.CODEC, QuestResponses.CODEC).fieldOf("responses").forGetter(d -> d.responses),
                        ResourceLocation.CODEC.listOf().fieldOf("required_quests").forGetter(d -> List.copyOf(d.requiredQuests))
                ).apply(inst, (responses, required) -> new QuestHandler(responses, Set.copyOf(required))));
    }

    public record QuestResponses(ResourceLocation startID, ResourceLocation activeID,
                                 ResourceLocation endID) {
        public static final Codec<QuestResponses> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(
                        ResourceLocation.CODEC.fieldOf("start_id").forGetter(d -> d.startID),
                        ResourceLocation.CODEC.fieldOf("active_id").forGetter(d -> d.activeID),
                        ResourceLocation.CODEC.fieldOf("end_id").forGetter(d -> d.endID)
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
        private final List<ResourceLocation> possibleChildIds = new ArrayList<>();

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

        public Builder addQuestResponse(ResourceLocation quest, ResourceLocation startingID, ResourceLocation activeID, ResourceLocation endID) {
            this.responses.put(quest, new QuestResponses(startingID, activeID, endID));
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

    public record Gift(@Nullable ResourceLocation giftID, String responseKey, int xp) {

        public static final Codec<Gift> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(
                        ResourceLocation.CODEC.optionalFieldOf("gift_id").forGetter(d -> Optional.ofNullable(d.giftID)),
                        Codec.STRING.fieldOf("response_key").forGetter(d -> d.responseKey),
                        Codec.INT.fieldOf("xp").forGetter(d -> d.xp)
                ).apply(inst, (items, respone, xp) -> new Gift(items.orElse(null), respone, xp)));
    }
}
