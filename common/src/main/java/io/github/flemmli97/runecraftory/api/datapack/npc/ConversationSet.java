package io.github.flemmli97.runecraftory.api.datapack.npc;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.datapack.ReloadableHolder;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryMemoryTypes;
import io.github.flemmli97.runecraftory.common.utils.CodecHelper;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;

public record ConversationSet(String fallbackKey, @Nullable ResourceLocation missing,
                              Map<String, Conversation> conversations) {

    public static final Codec<ConversationSet> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(Codec.STRING.optionalFieldOf("fallback_key").forGetter(d -> Optional.of(d.fallbackKey())),
                    Codec.unboundedMap(Codec.STRING, Conversation.CODEC).fieldOf("conversations").forGetter(d -> d.conversations)
            ).apply(inst, (fallback, convs) -> new ConversationSet(fallback.orElse(""), convs)));

    public ConversationSet(String fallbackKey, Map<String, Conversation> conversations) {
        this(fallbackKey, null, conversations);
    }

    public static NetworkConversation createRandom(NPCEntity npc, ReloadableHolder<ConversationSet> conversations,
                                                   Predicate<Map.Entry<String, ConversationSet.Conversation>> filter,
                                                   Random random) {
        List<Pair<Map.Entry<String, Conversation>, Map<String, Component>>> filtered = new ArrayList<>();
        Map<ResourceLocation, List<NPCEntity>> nearby = BrainUtils.getMemory(npc, RuneCraftoryMemoryTypes.NEARBY_NPCS.get());
        for (Map.Entry<String, ConversationSet.Conversation> entry : conversations.value().conversations().entrySet()) {
            if (!filter.test(entry))
                continue;
            Map<String, Component> participants = entry.getValue().resolveParticipants(npc, nearby, random);
            if (participants == null)
                continue;
            filtered.add(Pair.of(entry, participants));
        }
        Collections.shuffle(filtered, random);
        int size = Math.min(filtered.size(), 2 + random.nextInt(2)); //Select 2-3 random lines
        if (size > 0) {
            Pair<Map.Entry<String, Conversation>, Map<String, Component>> randomLine = filtered.get(random.nextInt(size));
            Conversation conversation = randomLine.getFirst().getValue();
            Optional<NetworkActions> actions;
            if (conversation.actions().isEmpty()) {
                actions = Optional.empty();
            } else {
                actions = Optional.of(new NetworkActions(conversations.id(), randomLine.getFirst().getKey(), conversation.actions().stream().map(e -> (Component) Component.translatable(e.translationKey())).toList()));
            }
            return new NetworkConversation(conversation.dialogues().stream().map(NetworkDialog::new).toList(), randomLine.getSecond(), actions);
        } else {
            Component dialog = conversations.value().missing() != null
                    ? Component.translatable(conversations.value().fallbackKey(), conversations.value().missing()) : Component.translatable(conversations.value().fallbackKey());
            return new NetworkConversation(dialog);
        }
    }

    public static Optional<NetworkConversation> encode(NPCEntity npc, ReloadableHolder<ConversationSet> conversations, String conversationID,
                                                       Random random) {
        Conversation conversation = conversations.value().conversations().get(conversationID);
        if (conversation == null)
            return Optional.empty();
        Map<ResourceLocation, List<NPCEntity>> nearby = BrainUtils.getMemory(npc, RuneCraftoryMemoryTypes.NEARBY_NPCS.get());
        Map<String, Component> participants = conversation.resolveParticipants(npc, nearby, random);
        if (participants == null)
            return Optional.empty();
        Optional<NetworkActions> actions;
        if (conversation.actions().isEmpty()) {
            actions = Optional.empty();
        } else {
            actions = Optional.of(new NetworkActions(conversations.id(), conversationID, conversation.actions().stream().map(e -> (Component) Component.translatable(e.translationKey())).toList()));
        }
        return Optional.of(new NetworkConversation(conversation.dialogues().stream().map(NetworkDialog::new).toList(), participants, actions));
    }

    public static class Builder {

        private final String fallback;
        private final Map<String, Conversation> conversations = new LinkedHashMap<>();

        private final Map<String, String> translations = new LinkedHashMap<>();

        public Builder() {
            this.fallback = "";
        }

        public Builder(String fallback, String enTranslation) {
            this.fallback = fallback;
            this.translations.put(this.fallback, enTranslation);
        }

        public Builder addConversation(String conversation, String enTranslation) {
            return this.addConversation(new Conversation.Builder(conversation, enTranslation));
        }

        public Builder addConversation(Conversation.Builder conversation) {
            return this.addConversation(conversation.key, conversation);
        }

        public Builder addConversation(String key, Conversation.Builder conversation) {
            this.conversations.put(key, conversation.build());
            conversation.translations.forEach((k, t) -> {
                if (t.equals(this.translations.getOrDefault(k, t))) {
                    this.translations.put(k, t);
                } else {
                    throw new IllegalStateException("Map contains translation already " + k);
                }
            });
            return this;
        }

        public Map<String, String> getTranslations() {
            return this.translations;
        }

        public ConversationSet build() {
            return new ConversationSet(this.fallback, this.conversations);
        }
    }

    public record Conversation(List<Dialogue> dialogues,
                               @Nullable NumberProvider minHearts, @Nullable NumberProvider maxHearts,
                               boolean startingConversation,
                               List<ConversationActionHolder> actions,
                               Map<String, Participant> speakers, LootItemCondition... conditions) {

        public static final Codec<Conversation> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(CodecHelper.listOrSingle(Dialogue.CODEC).fieldOf("dialogues").forGetter(d -> d.dialogues),
                        NumberProviders.CODEC.optionalFieldOf("min_hearts").forGetter(d -> Optional.ofNullable(d.minHearts)),
                        NumberProviders.CODEC.optionalFieldOf("max_hearts").forGetter(d -> Optional.ofNullable(d.maxHearts)),
                        Codec.BOOL.optionalFieldOf("starting_conversation").forGetter(d -> d.startingConversation ? Optional.empty() : Optional.of(false)),
                        ConversationActionHolder.CODEC.listOf().optionalFieldOf("actions").forGetter(d -> d.actions.isEmpty() ? Optional.empty() : Optional.of(d.actions)),
                        Codec.unboundedMap(Codec.STRING, Participant.CODEC).fieldOf("speakers").forGetter(d -> d.speakers),
                        LootItemCondition.DIRECT_CODEC.listOf().fieldOf("conditions").forGetter(d -> Arrays.stream(d.conditions).toList())
                ).apply(inst, (dialogues, min, max, start, actions, speakers, conditions) ->
                        new Conversation(dialogues, min.orElse(null), max.orElse(null), start.orElse(true), actions.orElse(List.of()), speakers, conditions.toArray(new LootItemCondition[0]))));

        public Conversation(List<Dialogue> dialogues, @Nullable NumberProvider minHearts, @Nullable NumberProvider maxHearts, boolean startingConversation, List<ConversationActionHolder> actions, Map<String, Participant> speakers, LootItemCondition... conditions) {
            this.dialogues = dialogues;
            if (this.dialogues.isEmpty()) {
                throw new IllegalArgumentException("Dialogues must not be empty");
            }
            this.minHearts = minHearts;
            this.maxHearts = maxHearts;
            this.startingConversation = startingConversation;
            this.actions = actions;
            this.speakers = speakers;
            this.conditions = conditions;
            this.dialogues.forEach(d -> {
                if (d.speaker().map(s -> !this.speakers.containsKey(s)).orElse(false)) {
                    throw new IllegalStateException("Speaker " + d.speaker().get() + " not found");
                }
            });
        }

        public Map<String, Component> resolveParticipants(NPCEntity npc, Map<ResourceLocation, List<NPCEntity>> nearby, Random random) {
            Map<String, Component> names = new HashMap<>();
            for (Map.Entry<String, Participant> entry : this.speakers().entrySet()) {
                Participant participant = entry.getValue();
                List<NPCEntity> npcs = nearby.get(participant.npcID());
                if (npcs == null || npcs.isEmpty()) {
                    return null;
                }
                NPCEntity target = null;
                if (participant.heuristic() == SpeakerHeuristic.UNIQUE) {
                    if (npcs.size() == 1) {
                        target = npcs.getFirst();
                    }
                } else {
                    EntityPredicate predicate = participant.predicate().orElse(null);
                    npcs = npcs.stream().filter(n -> predicate == null || predicate.matches((ServerLevel) npc.level(), npc.position(), npc)).toList();
                    target = switch (participant.heuristic()) {
                        case RANDOM -> npcs.get(random.nextInt(npcs.size()));
                        case NEAREST -> npcs.getFirst();
                        case FURTHERS -> npcs.getLast();
                        default -> null;
                    };
                }
                if (target == null) {
                    return null;
                }
                names.put(entry.getKey(), target.getDisplayName());
            }
            return names;
        }

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

            private final String key;
            private final List<Dialogue> dialogues = new ArrayList<>();
            private NumberProvider minHearts, maxHearts;
            private boolean startingConversation = true;
            private final List<ConversationActionHolder> action = new ArrayList<>();
            private final Map<String, Participant> speakers = new LinkedHashMap<>();
            private final List<LootItemCondition> conditions = new ArrayList<>();

            private final Map<String, String> translations = new LinkedHashMap<>();

            public Builder(String translationKey, String enTranslation) {
                this(new Dialogue(translationKey), enTranslation);
            }

            public Builder(Dialogue dialogue, String enTranslation) {
                this.withDialogue(dialogue, enTranslation);
                this.key = dialogue.translationKey;
            }

            public Builder min(@Nullable NumberProvider minHearts) {
                this.minHearts = minHearts;
                return this;
            }

            public Builder max(@Nullable NumberProvider maxHearts) {
                this.maxHearts = maxHearts;
                return this;
            }

            public Builder withDialogue(Dialogue dialogue, String enTranslation) {
                this.dialogues.add(dialogue);
                if (enTranslation.equals(this.translations.getOrDefault(dialogue.translationKey(), enTranslation))) {
                    this.translations.put(dialogue.translationKey(), enTranslation);
                } else {
                    throw new IllegalStateException("Map contains translation already " + dialogue.translationKey());
                }
                return this;
            }

            public Builder withParticipant(String key, Participant participant) {
                this.speakers.put(key, participant);
                return this;
            }

            public Builder setAnswer() {
                this.startingConversation = false;
                return this;
            }

            public Builder addAction(ConversationActionHolder action, String enTranslation) {
                this.action.add(action);
                this.translations.put(action.translationKey, enTranslation);
                return this;
            }

            public Builder addCondition(LootItemCondition condition) {
                this.conditions.add(condition);
                return this;
            }

            public Conversation build() {
                return new Conversation(this.dialogues, this.minHearts, this.maxHearts, this.startingConversation, this.action, this.speakers, this.conditions.toArray(new LootItemCondition[0]));
            }
        }
    }

    public record Dialogue(String translationKey, Optional<String> speaker) {

        public static final Codec<Dialogue> FULL_CODEC = RecordCodecBuilder.create(inst ->
                inst.group(Codec.STRING.fieldOf("translation_key").forGetter(Dialogue::translationKey),
                        Codec.STRING.optionalFieldOf("speaker").forGetter(Dialogue::speaker)
                ).apply(inst, Dialogue::new));
        public static final Codec<Dialogue> CODEC = Codec.either(Codec.STRING, FULL_CODEC)
                .xmap(e -> e.map(Dialogue::new, Function.identity()),
                        d -> d.speaker.isEmpty() ? Either.left(d.translationKey) : Either.right(d));

        public Dialogue(String translationKey) {
            this(translationKey, Optional.empty());
        }

        public Dialogue(String translationKey, String speaker) {
            this(translationKey, Optional.of(speaker));
        }
    }

    public record Participant(ResourceLocation npcID, Optional<EntityPredicate> predicate, SpeakerHeuristic heuristic) {

        public static final Codec<Participant> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(ResourceLocation.CODEC.fieldOf("npc_id").forGetter(Participant::npcID),
                        EntityPredicate.CODEC.optionalFieldOf("predicate").forGetter(Participant::predicate),
                        CodecUtils.stringEnumCodec(SpeakerHeuristic.class, null).fieldOf("heuristic").forGetter(Participant::heuristic)
                ).apply(inst, Participant::new));

        public Participant(ResourceLocation npcID) {
            this(npcID, Optional.empty(), SpeakerHeuristic.UNIQUE);
        }
    }

    public enum SpeakerHeuristic {
        NEAREST,
        FURTHERS,
        RANDOM,
        UNIQUE
    }

    public enum ConversationAction {
        ANSWER,
        QUEST
    }

    public record ConversationActionHolder(String translationKey, ConversationAction action, String actionValue,
                                           int friendXP) {

        public static final Codec<ConversationActionHolder> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(
                        Codec.STRING.fieldOf("translation_key").forGetter(d -> d.translationKey),
                        CodecUtils.stringEnumCodec(ConversationAction.class, null).fieldOf("actions").forGetter(d -> d.action),
                        Codec.STRING.fieldOf("value").forGetter(d -> d.actionValue),
                        Codec.INT.optionalFieldOf("friend_xp").forGetter(d -> d.friendXP != 0 ? Optional.of(d.friendXP) : Optional.empty())
                ).apply(inst, (key, action, value, xp) -> new ConversationActionHolder(key, action, value, xp.orElse(0))));
    }

    public record NetworkConversation(List<NetworkDialog> dialogues, Map<String, Component> participants,
                                      Optional<NetworkActions> action) {

        public static final StreamCodec<RegistryFriendlyByteBuf, NetworkConversation> STREAM_CODEC = new StreamCodec<>() {

            private static final StreamCodec<RegistryFriendlyByteBuf, List<NetworkDialog>> DIALOGUES = ByteBufCodecs.collection(ArrayList::new, NetworkDialog.STREAM_CODEC);
            private static final StreamCodec<RegistryFriendlyByteBuf, Map<String, Component>> PARTICIPANTS = ByteBufCodecs.map(HashMap::new, ByteBufCodecs.STRING_UTF8, ComponentSerialization.STREAM_CODEC);

            @Override
            public NetworkConversation decode(RegistryFriendlyByteBuf buf) {
                return new NetworkConversation(DIALOGUES.decode(buf),
                        PARTICIPANTS.decode(buf),
                        ByteBufCodecs.optional(NetworkActions.STREAM_CODEC).decode(buf));
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buf, NetworkConversation pkt) {
                DIALOGUES.encode(buf, pkt.dialogues());
                PARTICIPANTS.encode(buf, pkt.participants());
                ByteBufCodecs.optional(NetworkActions.STREAM_CODEC).encode(buf, pkt.action());
            }
        };

        public NetworkConversation(Component text) {
            this(List.of(new NetworkDialog(text)), Map.of(), Optional.empty());
        }
    }

    public record NetworkDialog(Component text, Optional<String> speaker) {

        public static final StreamCodec<RegistryFriendlyByteBuf, NetworkDialog> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public NetworkDialog decode(RegistryFriendlyByteBuf buf) {
                return new NetworkDialog(ComponentSerialization.STREAM_CODEC.decode(buf),
                        ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8).decode(buf));
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buf, NetworkDialog pkt) {
                ComponentSerialization.STREAM_CODEC.encode(buf, pkt.text());
                ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8).encode(buf, pkt.speaker());
            }
        };

        public NetworkDialog(Component text) {
            this(text, Optional.empty());
        }

        public NetworkDialog(Dialogue dialogue) {
            this(Component.translatable(dialogue.translationKey()), dialogue.speaker());
        }
    }

    public record NetworkActions(ResourceLocation setID, String conversationID, List<Component> actions) {
        public static final StreamCodec<RegistryFriendlyByteBuf, NetworkActions> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public NetworkActions decode(RegistryFriendlyByteBuf buf) {
                return new NetworkActions(buf.readResourceLocation(), buf.readUtf(), buf.readList(b -> ComponentSerialization.STREAM_CODEC.decode(buf)));
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buf, NetworkActions pkt) {
                buf.writeResourceLocation(pkt.setID());
                buf.writeUtf(pkt.conversationID());
                buf.writeCollection(pkt.actions(), (b, c) -> ComponentSerialization.STREAM_CODEC.encode(buf, c));
            }
        };
    }
}
