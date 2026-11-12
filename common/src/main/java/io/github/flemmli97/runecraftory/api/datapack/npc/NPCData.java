package io.github.flemmli97.runecraftory.api.datapack.npc;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.calendar.Season;
import io.github.flemmli97.runecraftory.api.datapack.ConversationContext;
import io.github.flemmli97.runecraftory.api.registry.NPCProfession;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.datapack.ReloadableHolder;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCSchedule;
import io.github.flemmli97.runecraftory.common.lib.LibConstants;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCProfessions;
import io.github.flemmli97.runecraftory.common.utils.Utils;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;

public record NPCData(@Nullable String name, @Nullable String surname,
                      Gender gender, List<NPCProfession> profession, @Nullable List<NPCLookId> look,
                      @Nullable Pair<Season, Integer> birthday,
                      int weight,
                      Map<ConversationContext, ResourceLocation> interactions,
                      Set<ResourceLocation> requiredQuests,
                      Map<String, Gift> giftItems, @Nullable NPCSchedule.Schedule schedule,
                      @Nullable Map<Holder<Attribute>, Double> baseStats,
                      @Nullable Map<Holder<Attribute>, Double> statIncrease,
                      int baseLevel, @Nullable List<ResourceLocation> combatActions, int unique,
                      RelationShipState relationShipState, List<ResourceLocation> possibleChildren) {

    public static final Map<Holder<Attribute>, Double> DEFAULT_GAIN = Map.of(Attributes.MAX_HEALTH, 5d, Attributes.ATTACK_DAMAGE, 0.5,
            RuneCraftoryAttributes.DEFENCE.asHolder(), 0.2, RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 0.5, RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 0.2);
    public static final ReloadableHolder<NPCData> DEFAULT = new ReloadableHolder<>(RuneCraftory.modRes("default_npc"),
            new NPCData(null, null, Gender.UNDEFINED, List.of(), null, null, 1,
                    Map.of(), Set.of(), Map.of(), null, null, null, 1, null, 0, RelationShipState.DEFAULT, List.of()));

    public static final Codec<NPCData> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    Codec.unboundedMap(ResourceLocation.CODEC.flatComapMap(ConversationContext::get, ctx -> DataResult.success(ctx.key())), ResourceLocation.CODEC)
                            .fieldOf("interactions").forGetter(d -> d.interactions),
                    ResourceLocation.CODEC.listOf().fieldOf("required_quests").forGetter(d -> List.copyOf(d.requiredQuests)),

                    NPCSchedule.Schedule.CODEC.optionalFieldOf("schedule").forGetter(d -> Optional.ofNullable(d.schedule)),
                    NPCCombat.CODEC.optionalFieldOf("combat").forGetter(d -> {
                        NPCCombat combat = new NPCCombat(d.baseStats, d.statIncrease, d.baseLevel, d.combatActions);
                        if (combat.isNone())
                            return Optional.empty();
                        return Optional.of(combat);
                    }),

                    RelationStruct.CODEC.fieldOf("relation").forGetter(d -> new RelationStruct(d.relationShipState, d.possibleChildren)),
                    Codec.unboundedMap(Codec.STRING, Gift.CODEC).fieldOf("gift_items").forGetter(NPCData::giftItems),

                    NPCLookId.CODEC.listOf().optionalFieldOf("look").forGetter(d -> Optional.ofNullable(d.look == null || d.look.isEmpty() ? null : d.look)),
                    WorldUtils.DATE.optionalFieldOf("birthday").forGetter(d -> Optional.ofNullable(d.birthday)),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("weight").forGetter(NPCData::weight),
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("unique").forGetter(d -> d.unique == 0 ? Optional.empty() : Optional.of(d.unique)),

                    Codec.STRING.optionalFieldOf("name").forGetter(d -> Optional.ofNullable(d.name)),
                    Codec.STRING.optionalFieldOf("surname").forGetter(d -> Optional.ofNullable(d.surname)),
                    CodecUtils.stringEnumCodec(Gender.class, Gender.UNDEFINED).fieldOf("gender").forGetter(NPCData::gender),
                    RuneCraftoryNPCProfessions.PROFESSIONS.registry().byNameCodec().listOf().optionalFieldOf("profession").forGetter(d -> d.profession.isEmpty() ? Optional.empty() : Optional.of(d.profession))
            ).apply(inst, (interactions, questHandler, schedule, combat, relation, giftItems, look, birthday, weight, unique, name, surname, gender, profession) ->
                    new NPCData(name.orElse(null), surname.orElse(null), gender, profession.orElse(List.of()), look.orElse(null), birthday.orElse(null),
                            weight, interactions, Set.copyOf(questHandler), giftItems, schedule.orElse(null), combat.map(d -> d.baseStats).orElse(null),
                            combat.map(d -> d.statIncrease).orElse(null), combat.map(d -> d.baseLevel).orElse(1), combat.map(d -> d.npcAction).orElse(null),
                            unique.orElse(0), relation.relationShipState, relation.possibleChildren)));

    public ReloadableHolder<ConversationSet> getConversation(ConversationContext convCtx) {
        ResourceLocation conversationId = this.interactions().get(convCtx);
        ConversationSet fallback = new ConversationSet("runecraftory.npc.conversation.context.missing", convCtx.key(), Map.of());
        if (conversationId == null)
            return new ReloadableHolder<>(LibConstants.EMPTY, fallback);
        fallback = new ConversationSet("runecraftory.npc.conversation.missing", convCtx.key(), Map.of());
        return DataPackHandler.INSTANCE.npcConversationManager().get(conversationId, fallback);
    }

    public List<ResourceLocation> missing() {
        List<ResourceLocation> missing = new ArrayList<>();
        for (ConversationContext e : ConversationContext.getRegistered()) {
            if (!this.interactions().containsKey(e)) {
                missing.add(e.key());
            }
        }
        return missing;
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

    record NPCCombat(@Nullable Map<Holder<Attribute>, Double> baseStats,
                     @Nullable Map<Holder<Attribute>, Double> statIncrease,
                     int baseLevel, @Nullable List<ResourceLocation> npcAction) {

        public static final Codec<NPCCombat> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(
                        ExtraCodecs.POSITIVE_INT.optionalFieldOf("base_level").forGetter(d -> d.baseLevel != 1 ? Optional.of(d.baseLevel) : Optional.empty()),
                        ResourceLocation.CODEC.listOf().optionalFieldOf("combat_actions").forGetter(d -> Optional.ofNullable(d.npcAction == null || d.npcAction.isEmpty() ? null : d.npcAction)),
                        Codec.unboundedMap(BuiltInRegistries.ATTRIBUTE.holderByNameCodec(), Codec.DOUBLE).optionalFieldOf("base_stats").forGetter(d -> Optional.ofNullable(d.baseStats)),
                        Codec.unboundedMap(BuiltInRegistries.ATTRIBUTE.holderByNameCodec(), Codec.DOUBLE).optionalFieldOf("stat_increase").forGetter(d -> Optional.ofNullable(d.statIncrease))
                ).apply(inst, (lvl, action, stats, inc) -> new NPCCombat(stats.orElse(null), inc.orElse(null), lvl.orElse(1), action.orElse(null))));

        public boolean isNone() {
            return this.baseLevel == 1 && this.baseStats == null && this.statIncrease == null && this.npcAction == null;
        }
    }

    public static class Builder {

        private final ResourceLocation id;

        private final String name;
        private final String surname;
        private final Gender gender;
        private final int weight;
        private final List<NPCProfession> professions = new ArrayList<>();
        private final Map<ConversationContext, ResourceLocation> interactions = new HashMap<>();
        private final Map<ResourceLocation, ConversationSet.Builder> conversations = new HashMap<>();
        private final Map<String, Gift> giftItems = new HashMap<>();
        private Pair<Season, Integer> birthday;
        private NPCSchedule.Schedule schedule;
        private List<NPCLookId> look;
        private List<ResourceLocation> combatAction;

        private final Map<Holder<Attribute>, Double> baseStats = new TreeMap<>(RuneCraftoryAttributes.SORTED);
        private final Map<Holder<Attribute>, Double> statIncrease = new TreeMap<>(RuneCraftoryAttributes.SORTED);
        private int baseLevel = 1;
        private int unique;
        private RelationShipState relationShipState = RelationShipState.DEFAULT;
        private final List<ResourceLocation> possibleChildIds = new ArrayList<>();

        private final Set<ResourceLocation> requiredQuests = new HashSet<>();

        private final Map<String, String> translations = new HashMap<>();

        public Builder(ResourceLocation id, int weight) {
            this(id, weight, null, null, Gender.UNDEFINED);
        }

        public Builder(ResourceLocation id, int weight, String name, Gender gender) {
            this(id, weight, name, null, gender);
        }

        public Builder(ResourceLocation id, int weight, String name, String surname, Gender gender) {
            this.id = id;
            this.weight = weight;
            this.name = name;
            this.surname = surname;
            this.gender = gender;
        }

        public Builder withLook(NPCLookId... looks) {
            this.look = List.of(looks);
            return this;
        }

        public Builder withBirthday(Pair<Season, Integer> birthday) {
            this.birthday = birthday;
            return this;
        }

        public Builder withProfession(NPCProfession... professions) {
            this.professions.addAll(List.of(professions));
            return this;
        }

        public Builder addInteractionIfAbsent(ConversationContext convCtx, ResourceLocation conversationId) {
            this.interactions.putIfAbsent(convCtx, conversationId);
            return this;
        }

        public GiftBuilder gifts() {
            return new GiftBuilder(this.id);
        }

        public ContextBuilder conversations() {
            return new ContextBuilder(this.id);
        }

        public Builder addTranslation(String key, String translation) {
            this.translations.put(key, translation);
            return this;
        }

        public Builder withSchedule(NPCSchedule.Schedule schedule) {
            this.schedule = schedule;
            return this;
        }

        public Builder setBaseStat(Holder<Attribute> attribute, double val) {
            this.baseStats.put(attribute, val);
            return this;
        }

        public Builder setStatIncrease(Holder<Attribute> attribute, double val) {
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

        public Map<String, String> getTranslations() {
            return this.translations;
        }

        public Map<ResourceLocation, ConversationSet.Builder> getConversations() {
            return this.conversations;
        }

        public NPCData build() {
            for (ConversationContext convCtx : ConversationContext.getRegistered()) {
                if (!this.interactions.containsKey(convCtx))
                    throw new IllegalStateException("Missing interactions for " + convCtx);
            }
            return new NPCData(this.name, this.surname, this.gender, this.professions, this.look, this.birthday, this.weight,
                    this.interactions,
                    this.requiredQuests, this.giftItems, this.schedule,
                    this.baseStats.isEmpty() ? null : this.baseStats, this.statIncrease.isEmpty() ? null : this.statIncrease,
                    this.baseLevel, this.combatAction, this.unique, this.relationShipState, this.possibleChildIds);
        }

        public class ContextBuilder {

            private final ResourceLocation id;
            private final String prefix;
            private ConversationContext context;
            private final Map<ConversationContext, ConversationSet.Builder> conversations = new LinkedHashMap<>();

            public ContextBuilder(ResourceLocation id) {
                this(id, true);
            }

            public ContextBuilder(ResourceLocation id, boolean defaulted) {
                this.id = id;
                this.prefix = String.format("%s%s.%s", defaulted ? "npc." : "", id.getNamespace(), id.getPath().replace("/", "."));
            }

            private ResourceLocation idFromContext(ConversationContext context) {
                return this.id.withPath(p -> p + "/" + context.key().getPath());
            }

            public ContextBuilder push(ConversationContext context) {
                this.context = context;
                this.conversations.computeIfAbsent(this.context, ctx -> new ConversationSet.Builder(this.prefix));
                return this;
            }

            public ContextBuilder addConversation(String translation) {
                if (this.context == null) {
                    throw new IllegalStateException("Context is null");
                }
                ConversationSet.Builder builder = this.conversations.get(this.context);
                builder.addConversation(this.context.key().getPath(), translation);
                Utils.mergeMapAndThrow(builder.getTranslations(), Builder.this.translations);
                return this;
            }

            public ContextBuilder addConversation(Function<String, ConversationSet.Conversation.Builder> conversation) {
                if (this.context == null) {
                    throw new IllegalStateException("Context is null");
                }
                ConversationSet.Builder builder = this.conversations.get(this.context);
                builder.addConversation(this.context.key().getPath(), conversation);
                Utils.mergeMapAndThrow(builder.getTranslations(), Builder.this.translations);
                return this;
            }

            public Builder end() {
                this.conversations.forEach((ctx, builder) -> {
                    ResourceLocation id = this.idFromContext(ctx);
                    Builder.this.interactions.put(ctx, id);
                    Builder.this.conversations.put(id, builder);
                });
                return Builder.this;
            }
        }

        public class GiftBuilder {

            private final ResourceLocation baseId;
            private final String prefix;

            private final Map<ResourceLocation, ConversationSet.Builder> conversations = new LinkedHashMap<>();

            public GiftBuilder(ResourceLocation baseId) {
                this(baseId, true);
            }

            public GiftBuilder(ResourceLocation baseId, boolean defaulted) {
                this.baseId = baseId;
                this.prefix = String.format("%s%s.%s", defaulted ? "npc." : "", baseId.getNamespace(), baseId.getPath().replace("/", "."));
            }

            public GiftBuilder addGift(String id, @Nullable ResourceLocation giftID, int xp, String translation) {
                ConversationSet.Builder builder = new ConversationSet.Builder(this.prefix);
                builder.addConversation(id, translation);
                ResourceLocation conversationID = this.baseId.withPath(p -> p + "/" + id);
                this.conversations.put(conversationID, builder);
                Utils.mergeMapAndThrow(builder.getTranslations(), this.end().getTranslations());
                Builder.this.giftItems.put(id, new Gift(giftID, conversationID, xp));
                return this;
            }

            public GiftBuilder addGift(String id, @Nullable ResourceLocation giftID, int xp, Function<String, ConversationSet.Conversation.Builder> conversation) {
                ConversationSet.Builder builder = new ConversationSet.Builder(this.prefix);
                builder.addConversation(id, conversation);
                ResourceLocation conversationID = this.baseId.withPath(p -> p + "/" + id);
                this.conversations.put(conversationID, builder);
                Utils.mergeMapAndThrow(builder.getTranslations(), this.end().getTranslations());
                Builder.this.giftItems.put(id, new Gift(giftID, conversationID, xp));
                return this;
            }

            public NPCData.Builder end() {
                Builder.this.conversations.putAll(this.conversations);
                return Builder.this;
            }
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
            return d.right().map(DataResult::success).orElse(DataResult.error(() -> "Failed to parse npc look id"));
        }, i -> DataResult.success(i.gender == Gender.UNDEFINED ? Either.left(i.id()) : Either.right(i)));

        public NPCLookId(String namespace, String path) {
            this(ResourceLocation.fromNamespaceAndPath(namespace, path));
        }

        public NPCLookId(ResourceLocation id) {
            this(id, Gender.UNDEFINED);
        }
    }

    public record Gift(@Nullable ResourceLocation giftID, ResourceLocation responseKey, int xp) {

        public static final Codec<Gift> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(
                        ResourceLocation.CODEC.optionalFieldOf("gift_id").forGetter(d -> Optional.ofNullable(d.giftID)),
                        ResourceLocation.CODEC.fieldOf("response").forGetter(d -> d.responseKey),
                        Codec.INT.fieldOf("xp").forGetter(d -> d.xp)
                ).apply(inst, (items, respone, xp) -> new Gift(items.orElse(null), respone, xp)));
    }
}
