package io.github.flemmli97.runecraftory.api.datapack.npc;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.datapack.ReloadableHolder;
import io.github.flemmli97.runecraftory.common.entities.ai.memory.ConversationGroupMemory;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.entities.npc.social.TopicHolder;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryMemoryTypes;
import io.github.flemmli97.runecraftory.common.utils.CodecHelper;
import io.github.flemmli97.runecraftory.common.utils.Utils;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
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
import java.util.function.BiFunction;
import java.util.function.Function;

public record ConversationSet(String fallbackKey, @Nullable ResourceLocation missing,
                              Map<String, Conversation> conversations) {

    public static final Codec<ConversationSet> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(Codec.STRING.optionalFieldOf("fallback_key").forGetter(d -> Optional.of(d.fallbackKey())),
                    Codec.unboundedMap(Codec.STRING, Conversation.CODEC).fieldOf("conversations").forGetter(d -> d.conversations)
            ).apply(inst, (fallback, convs) -> new ConversationSet(fallback.orElse(""), convs)));

    public ConversationSet(String fallbackKey, Map<String, Conversation> conversations) {
        this(fallbackKey, null, conversations);
    }

    public static NetworkConversation createRandom(ReloadableHolder<ConversationSet> conversations,
                                                   Function<Map.Entry<String, Conversation>, Integer> filter,
                                                   Random random) {
        List<Map.Entry<String, Conversation>> filtered = conversations.value().filteredConversations(filter);
        Collections.shuffle(filtered, random);
        int size = Math.min(filtered.size(), 2 + random.nextInt(2)); //Select 2-3 random lines
        if (size > 0) {
            Map.Entry<String, Conversation> randomLine = filtered.get(random.nextInt(size));
            Conversation conversation = randomLine.getValue();
            Optional<NetworkActions> actions;
            if (conversation.actions().isEmpty()) {
                actions = Optional.empty();
            } else {
                actions = Optional.of(new NetworkActions(conversations.id(), randomLine.getKey(), conversation.actions().stream().map(e -> (Component) Component.translatable(e.translationKey())).toList()));
            }
            return new NetworkConversation(conversation.dialogues().stream().map(NetworkDialog::new).toList(), Map.of(), actions);
        } else {
            Component dialog = conversations.value().missing() != null
                    ? Component.translatable(conversations.value().fallbackKey(), conversations.value().missing()) : Component.translatable(conversations.value().fallbackKey());
            return new NetworkConversation(dialog);
        }
    }

    public List<Map.Entry<String, Conversation>> filteredConversations(Function<Map.Entry<String, Conversation>, Integer> filter) {
        int currentPriority = 0;
        List<Map.Entry<String, Conversation>> filtered = new ArrayList<>();
        for (Map.Entry<String, ConversationSet.Conversation> entry : this.conversations().entrySet()) {
            int priority = filter.apply(entry);
            if (priority < currentPriority) {
                continue;
            }
            if (priority > currentPriority) {
                currentPriority = priority;
                filtered.clear();
            }
            if (!entry.getValue().participants().isEmpty())
                continue;
            filtered.add(entry);
        }
        return filtered;
    }

    public static Optional<NetworkConversation> fromTopic(ConversationGroupMemory group, TopicHolder topic) {
        if (topic == null)
            return Optional.empty();
        Conversation conversation = topic.holder().value().conversations().get(topic.topic().key());
        if (conversation == null || !group.isGroupComplete())
            return Optional.empty();
        Map<String, Component> participants = group.encodeParticipantsForDialogue();
        Optional<NetworkActions> actions;
        if (conversation.actions().isEmpty()) {
            actions = Optional.empty();
        } else {
            actions = Optional.of(new NetworkActions(topic.holder().id(), topic.topic().key(), conversation.actions().stream().map(e -> (Component) Component.translatable(e.translationKey())).toList()));
        }
        return Optional.of(new NetworkConversation(conversation.dialogues().stream().map(NetworkDialog::new).toList(), participants, actions));
    }

    public static Optional<NetworkConversation> encode(NPCEntity npc, ReloadableHolder<ConversationSet> conversations, String conversationID) {
        Conversation conversation = conversations.value().conversations().get(conversationID);
        if (conversation == null)
            return Optional.empty();
        ConversationGroupMemory group = BrainUtils.getMemory(npc, RuneCraftoryMemoryTypes.CONVERSATION_GROUP.get());
        Map<String, Component> participants = group == null ? Map.of() : group.encodeParticipantsForDialogue();
        if (!participants.keySet().containsAll(conversation.participants().keySet()))
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

        private final String prefix;
        private final Map<String, Conversation> conversations = new LinkedHashMap<>();

        private final Map<String, String> translations = new LinkedHashMap<>();

        public Builder() {
            this("");
        }

        public Builder(String prefix) {
            this(prefix, "");
        }

        public Builder(String prefix, String enTranslation) {
            this.prefix = prefix;
            if (!enTranslation.isEmpty()) {
                this.translations.put(this.prefix, enTranslation);
            }
        }

        public Builder addConversation(String conversation, String enTranslation) {
            return this.addConversation(conversation, (p, i) -> new Conversation.Builder(p, enTranslation));
        }

        public Builder addConversation(String key, BiFunction<String, Integer, Conversation.Builder> conversationFact) {
            int amount = this.conversations.size() + 1;
            if (!this.prefix.isEmpty()) {
                key = String.format("%s.%s.%s", this.prefix, key, amount);
            }
            Conversation.Builder conversation = conversationFact.apply(key, amount);
            this.conversations.put(key, conversation.build());
            Utils.mergeMapAndThrow(conversation.translations, this.translations);
            return this;
        }

        public Map<String, String> getTranslations() {
            return this.translations;
        }

        public ConversationSet build() {
            return new ConversationSet(this.translations.containsKey(this.prefix) ? this.prefix : "", this.conversations);
        }
    }

    public record Conversation(List<Dialogue> dialogues,
                               @Nullable NumberProvider minHearts, @Nullable NumberProvider maxHearts,
                               boolean startingConversation,
                               List<ConversationActionHolder> actions,
                               Map<String, Participant> participants, LootItemCondition... conditions) {

        public static final Codec<Conversation> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(CodecHelper.listOrSingle(Dialogue.CODEC).fieldOf("dialogues").forGetter(Conversation::dialogues),
                        NumberProviders.CODEC.optionalFieldOf("min_hearts").forGetter(d -> Optional.ofNullable(d.minHearts)),
                        NumberProviders.CODEC.optionalFieldOf("max_hearts").forGetter(d -> Optional.ofNullable(d.maxHearts)),
                        Codec.BOOL.optionalFieldOf("starting_conversation").forGetter(d -> d.startingConversation ? Optional.empty() : Optional.of(false)),
                        ConversationActionHolder.CODEC.listOf().optionalFieldOf("actions").forGetter(d -> d.actions.isEmpty() ? Optional.empty() : Optional.of(d.actions)),
                        Codec.unboundedMap(Codec.STRING, Participant.CODEC).fieldOf("speakers").forGetter(Conversation::participants),
                        LootItemCondition.DIRECT_CODEC.listOf().fieldOf("conditions").forGetter(d -> Arrays.stream(d.conditions).toList())
                ).apply(inst, (dialogues, min, max, start, actions, speakers, conditions) ->
                        new Conversation(dialogues, min.orElse(null), max.orElse(null), start.orElse(true), actions.orElse(List.of()), speakers, conditions.toArray(new LootItemCondition[0]))));

        public Conversation(List<Dialogue> dialogues, @Nullable NumberProvider minHearts, @Nullable NumberProvider maxHearts, boolean startingConversation, List<ConversationActionHolder> actions, Map<String, Participant> participants, LootItemCondition... conditions) {
            this.dialogues = dialogues;
            if (this.dialogues.isEmpty()) {
                throw new IllegalArgumentException("Dialogues must not be empty");
            }
            this.minHearts = minHearts;
            this.maxHearts = maxHearts;
            this.startingConversation = startingConversation;
            this.actions = actions;
            this.participants = participants;
            this.conditions = conditions;
            this.dialogues.forEach(d -> {
                if (d.speaker().map(s -> !this.participants.containsKey(s)).orElse(false)) {
                    throw new IllegalStateException("Speaker " + d.speaker().get() + " not found");
                }
            });
        }

        /**
         * Calculates a priority for this conversation
         * This way conversations with some specific condition are not gated behind the majority without conditions
         *
         * @return -1 if conditions are not met. Otherwise the conversations priority
         */
        public int priority(int hearts, LootContext ctx) {
            int priority = 0;
            if (this.minHearts != null || this.maxHearts != null) {
                priority = 1;
            }
            if (this.conditions.length > 0)
                priority = 2;
            if (this.minHearts != null && this.minHearts.getInt(ctx) > hearts)
                return -1;
            if (this.maxHearts != null && this.maxHearts.getInt(ctx) < hearts)
                return -1;
            for (LootItemCondition condition : this.conditions)
                if (!condition.test(ctx))
                    return -1;
            return priority;
        }

        public static class Builder {

            private final String prefix;
            private final List<Dialogue> dialogues = new ArrayList<>();
            private NumberProvider minHearts, maxHearts;
            private boolean startingConversation = true;
            private final List<ConversationActionHolder> action = new ArrayList<>();
            private final Map<String, Participant> speakers = new LinkedHashMap<>();
            private final List<LootItemCondition> conditions = new ArrayList<>();

            private final Map<String, String> translations = new LinkedHashMap<>();

            public Builder(String prefix, String enTranslation) {
                this(prefix, null, enTranslation);
            }

            public Builder(String prefix, @Nullable String speaker, String enTranslation) {
                this.prefix = prefix;
                this.withDialogue(speaker, enTranslation);
            }

            public Builder min(@Nullable NumberProvider minHearts) {
                this.minHearts = minHearts;
                return this;
            }

            public Builder max(@Nullable NumberProvider maxHearts) {
                this.maxHearts = maxHearts;
                return this;
            }

            public Builder withDialogue(@Nullable String speaker, String enTranslation) {
                int num = this.dialogues.size();
                Dialogue dialogue = new Dialogue(String.format("%s%s", this.prefix, num == 0 ? "" : "." + num), Optional.ofNullable(speaker));
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

    public record Participant(ResourceLocation npcID, LootItemCondition... conditions) {

        public static final Codec<Participant> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(ResourceLocation.CODEC.fieldOf("npc_id").forGetter(Participant::npcID),
                        LootItemCondition.DIRECT_CODEC.listOf().fieldOf("conditions").forGetter(d -> Arrays.stream(d.conditions).toList())
                ).apply(inst, (npc, conditions) -> new Participant(npc, conditions.toArray(new LootItemCondition[0]))));


        public boolean test(LootContext ctx) {
            for (LootItemCondition condition : this.conditions)
                if (!condition.test(ctx))
                    return false;
            return true;
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

        public static ConversationActionHolder answer(String translationKey, String answer, int xp) {
            return new ConversationActionHolder(translationKey, ConversationAction.ANSWER, answer, xp);
        }
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

        public NetworkActions updatedComponents(Function<Component, Component> update) {
            return new NetworkActions(this.setID(), this.conversationID(), this.actions().stream().map(update).toList());
        }
    }
}
