package io.github.flemmli97.runecraftory.api.datapack;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.entities.npc.EnumShop;
import io.github.flemmli97.runecraftory.common.utils.CodecHelper;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;

public record NPCData(@Nullable String name, @Nullable String surname,
                      Gender gender, EnumShop profession, @Nullable ResourceLocation look,
                      int weight, String neutralGiftResponse,
                      Map<ConversationType, ConversationSet> interactions,
                      Map<String, Gift> giftItems) {

    public static final NPCData DEFAULT_DATA = new NPCData(null, null, Gender.UNDEFINED, null, null, 1, "npc.default.gift.neutral",
            buildDefaultInteractionMap(), Map.of());

    private static Map<ConversationType, ConversationSet> buildDefaultInteractionMap() {
        ImmutableMap.Builder<ConversationType, ConversationSet> builder = new ImmutableMap.Builder<>();
        for (ConversationType type : ConversationType.values())
            builder.put(type, new ConversationSet("npc.default" + type.key + ".default", List.of()));
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
                    ResourceLocation.CODEC.optionalFieldOf("look").forGetter(d -> Optional.ofNullable(d.look)),
                    Codec.STRING.fieldOf("neutralGiftResponse").forGetter(d -> d.neutralGiftResponse),
                    Codec.unboundedMap(Codec.STRING, Gift.CODEC).fieldOf("giftItems").forGetter(d -> d.giftItems),
                    filledMap(ConversationType.class, Codec.unboundedMap(CodecHelper.enumCodec(ConversationType.class, null), ConversationSet.CODEC))
                            .fieldOf("interactions").forGetter(d -> d.interactions),

                    CodecHelper.enumCodec(EnumShop.class, null).optionalFieldOf("profession").forGetter(d -> Optional.ofNullable(d.profession)),
                    ExtraCodecs.POSITIVE_INT.fieldOf("weight").forGetter(d -> d.weight),

                    Codec.STRING.optionalFieldOf("name").forGetter(d -> Optional.ofNullable(d.name)),
                    Codec.STRING.optionalFieldOf("surname").forGetter(d -> Optional.ofNullable(d.surname)),
                    CodecHelper.enumCodec(Gender.class, Gender.UNDEFINED).fieldOf("gender").forGetter(d -> d.gender)
            ).apply(inst, ((look, neutralGift, giftItems, interactions, profession, weight, name, surname, gender) -> new NPCData(name.orElse(null), surname.orElse(null),
                    gender, profession.orElse(null), look.orElse(null), weight, neutralGift, interactions, giftItems))));

    public enum Gender {
        UNDEFINED,
        MALE,
        FEMALE
    }

    public enum ConversationType {

        GREETING("greeting"),
        TALK("talk"),
        FOLLOWYES("follow.yes"),
        FOLLOWNO("follow.no");

        public final String key;

        ConversationType(String key) {
            this.key = key;
        }
    }

    public static class Builder {

        private final String name;
        private final String surname;
        private final Gender gender;
        private final int weight;
        private String neutralGiftResponse;
        private EnumShop profession;
        private final Map<ConversationType, ConversationSet> interactions = new TreeMap<>();
        private final Map<String, Gift> giftItems = new LinkedHashMap<>();
        private ResourceLocation look;

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

        public Builder setNeutralGiftResponse(String neutralGiftResponse) {
            this.neutralGiftResponse = neutralGiftResponse;
            return this;
        }

        public Builder withLook(ResourceLocation id) {
            this.look = id;
            return this;
        }

        public Builder withProfession(EnumShop profession) {
            this.profession = profession;
            return this;
        }

        public Builder addInteraction(ConversationType type, ConversationSet.Builder set) {
            this.interactions.put(type, set.build());
            return this;
        }

        public Builder addGiftResponse(String id, Gift gift) {
            this.giftItems.put(id, gift);
            return this;
        }

        public NPCData build() {
            if (this.neutralGiftResponse == null)
                throw new IllegalStateException("Neutral gift response not set.");
            for (ConversationType type : ConversationType.values()) {
                if (!this.interactions.containsKey(type))
                    throw new IllegalStateException("Missing interactions for " + type);
            }
            return new NPCData(this.name, this.surname, this.gender, this.profession, this.look, this.weight, this.neutralGiftResponse, this.interactions, this.giftItems);
        }
    }

    public record ConversationSet(String fallbackKey, List<Conversation> conversations) {

        public static final Codec<ConversationSet> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(
                        Codec.STRING.fieldOf("fallbackKey").forGetter(d -> d.fallbackKey),
                        Conversation.CODEC.listOf().fieldOf("conversations").forGetter(d -> d.conversations)
                ).apply(inst, ConversationSet::new));


        public static class Builder {

            private final String fallback;
            private final List<Conversation> greetings = new ArrayList<>();

            public Builder(String fallback) {
                this.fallback = fallback;
            }

            public Builder addConversation(Conversation conversation) {
                this.greetings.add(conversation);
                return this;
            }

            public ConversationSet build() {
                return new ConversationSet(this.fallback, this.greetings);
            }
        }
    }

    public record Conversation(String translationKey, int minHearts, int maxHearts) {

        public static final Codec<Conversation> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(
                        Codec.STRING.fieldOf("translationKey").forGetter(d -> d.translationKey),
                        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("minHearts").forGetter(d -> d.minHearts),
                        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("maxHearts").forGetter(d -> d.maxHearts)
                ).apply(inst, Conversation::new));

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
            if (xp < 50)
                return LIKE;
            return LOVE;
        }
    }

    public record NPCLook(Gender gender, ResourceLocation texture, int weight,
                          List<Pair<ResourceLocation, ResourceLocation>> additionalFeatures) {

        public static final NPCLook DEFAULT_LOOK = new NPCLook(Gender.MALE, new ResourceLocation("textures/entity/steve.png"), 0, List.of());

        public static final Codec<NPCLook> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(
                        CodecHelper.enumCodec(Gender.class, Gender.UNDEFINED).fieldOf("gender").forGetter(d -> d.gender),
                        ResourceLocation.CODEC.fieldOf("texture").forGetter(d -> d.texture),
                        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("weight").forGetter(d -> d.weight),
                        Codec.pair(ResourceLocation.CODEC, ResourceLocation.CODEC).listOf().fieldOf("additionalFeatures").forGetter(d -> d.additionalFeatures)
                ).apply(inst, NPCLook::new));

        public static NPCLook fromBuffer(FriendlyByteBuf buf) {
            int size = buf.readInt();
            List<Pair<ResourceLocation, ResourceLocation>> additional = new ArrayList<>();
            for (int i = 0; i < size; i++)
                additional.add(Pair.of(buf.readResourceLocation(), buf.readResourceLocation()));
            return new NPCLook(buf.readEnum(Gender.class), buf.readResourceLocation(), buf.readInt(), additional);
        }

        public void writeToBuffer(FriendlyByteBuf buf) {
            buf.writeInt(this.additionalFeatures.size());
            this.additionalFeatures.forEach(p -> {
                buf.writeResourceLocation(p.getFirst());
                buf.writeResourceLocation(p.getSecond());
            });
            buf.writeEnum(this.gender());
            buf.writeResourceLocation(new ResourceLocation(this.texture().getNamespace(), this.texture().getPath()));
            buf.writeInt(this.weight());
        }
    }
}
