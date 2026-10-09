package io.github.flemmli97.runecraftory.api.datapack.npc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.datapack.ReloadableHolder;
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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
                                                   Predicate<Map.Entry<String, ConversationSet.Conversation>> filter,
                                                   Random random) {
        List<Map.Entry<String, ConversationSet.Conversation>> filtered = conversations.value().conversations().entrySet().stream()
                .filter(filter).collect(Collectors.toList());
        Collections.shuffle(filtered, random);
        int size = Math.min(filtered.size(), 2 + random.nextInt(2)); //Select 2-3 random lines
        if (size > 0) {
            Map.Entry<String, ConversationSet.Conversation> randomLine = filtered.get(random.nextInt(size));
            Optional<NetworkActions> actions;
            if (randomLine.getValue().actions().isEmpty()) {
                actions = Optional.empty();
            } else {
                actions = Optional.of(new NetworkActions(conversations.id(), randomLine.getKey(), randomLine.getValue().actions().stream().map(e -> (Component) Component.translatable(e.translationKey())).toList()));
            }
            return new NetworkConversation(Component.translatable(randomLine.getValue().translationKey()), actions);
        } else {
            Component dialog = conversations.value().missing() != null
                    ? Component.translatable(conversations.value().fallbackKey(), conversations.value().missing()) : Component.translatable(conversations.value().fallbackKey());
            return new NetworkConversation(dialog, Optional.empty());
        }
    }

    public static Optional<NetworkConversation> encode(ReloadableHolder<ConversationSet> conversations, String conversationID) {
        Conversation conversation = conversations.value().conversations().get(conversationID);
        if (conversation == null)
            return Optional.empty();
        Optional<NetworkActions> actions;
        if (conversation.actions().isEmpty()) {
            actions = Optional.empty();
        } else {
            actions = Optional.of(new NetworkActions(conversations.id(), conversationID, conversation.actions().stream().map(e -> (Component) Component.translatable(e.translationKey())).toList()));
        }
        return Optional.of(new NetworkConversation(Component.translatable(conversation.translationKey()), actions));
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
            return this.addConversation(new Conversation.Builder(conversation), enTranslation);
        }

        public Builder addConversation(Conversation.Builder conversation, String enTranslation) {
            return this.addConversation(conversation.translationKey, conversation, enTranslation);
        }

        public Builder addConversation(String key, Conversation.Builder conversation, String enTranslation) {
            this.conversations.put(key, conversation.build());
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
            return new ConversationSet(this.fallback, this.conversations);
        }
    }

    public record Conversation(String translationKey, @Nullable NumberProvider minHearts,
                               @Nullable NumberProvider maxHearts, boolean startingConversation,
                               List<ConversationActionHolder> actions, LootItemCondition... conditions) {

        public static final Codec<Conversation> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(
                        Codec.BOOL.optionalFieldOf("starting_conversation").forGetter(d -> d.startingConversation ? Optional.empty() : Optional.of(false)),
                        ConversationActionHolder.CODEC.listOf().optionalFieldOf("actions").forGetter(d -> d.actions.isEmpty() ? Optional.empty() : Optional.of(d.actions)),
                        LootItemCondition.DIRECT_CODEC.listOf().fieldOf("conditions").forGetter(d -> Arrays.stream(d.conditions).toList()),

                        Codec.STRING.fieldOf("translation_key").forGetter(d -> d.translationKey),
                        NumberProviders.CODEC.optionalFieldOf("min_hearts").forGetter(d -> Optional.ofNullable(d.minHearts)),
                        NumberProviders.CODEC.optionalFieldOf("max_hearts").forGetter(d -> Optional.ofNullable(d.maxHearts))
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

    public record NetworkConversation(Component text, Optional<NetworkActions> action) {

        public static final StreamCodec<RegistryFriendlyByteBuf, NetworkConversation> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public NetworkConversation decode(RegistryFriendlyByteBuf buf) {
                return new NetworkConversation(ComponentSerialization.STREAM_CODEC.decode(buf),
                        ByteBufCodecs.optional(NetworkActions.STREAM_CODEC).decode(buf));
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buf, NetworkConversation pkt) {
                ComponentSerialization.STREAM_CODEC.encode(buf, pkt.text());
                ByteBufCodecs.optional(NetworkActions.STREAM_CODEC).encode(buf, pkt.action());
            }
        };

        public NetworkConversation(Component text) {
            this(text, Optional.empty());
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
