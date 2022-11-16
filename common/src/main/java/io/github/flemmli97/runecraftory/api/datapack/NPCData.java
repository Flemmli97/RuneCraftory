package io.github.flemmli97.runecraftory.api.datapack;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
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
import java.util.Map;
import java.util.Optional;

public record NPCData(@Nullable String name, @Nullable String surname,
                      Gender gender, EnumShop profession, @Nullable ResourceLocation look,
                      int weight, String neutralGiftResponse,
                      List<Conversation> greetings,
                      List<Conversation> conversations,
                      Map<String, Gift> giftItems) {

    public static final NPCData DEFAULT_DATA = new NPCData(null, null, Gender.UNDEFINED, null, null, 1, "npc.default.gift.neutral",
            List.of(new Conversation("npc.default.greeting", 0, 10)),
            List.of(new Conversation("npc.default.talk", 0, 10)), Map.of());

    public static final Codec<NPCData> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    Codec.STRING.fieldOf("neutralGiftResponse").forGetter(d -> d.neutralGiftResponse),
                    Codec.unboundedMap(Codec.STRING, Gift.CODEC).fieldOf("giftItems").forGetter(d -> d.giftItems),

                    ResourceLocation.CODEC.optionalFieldOf("look").forGetter(d -> Optional.ofNullable(d.look)),
                    ExtraCodecs.nonEmptyList(Conversation.CODEC.listOf()).fieldOf("conversations").forGetter(d -> d.conversations),
                    ExtraCodecs.nonEmptyList(Conversation.CODEC.listOf()).fieldOf("greetings").forGetter(d -> d.greetings),

                    CodecHelper.enumCodec(EnumShop.class, null).optionalFieldOf("profession").forGetter(d -> Optional.ofNullable(d.profession)),
                    ExtraCodecs.POSITIVE_INT.fieldOf("weight").forGetter(d -> d.weight),

                    Codec.STRING.optionalFieldOf("name").forGetter(d -> Optional.ofNullable(d.name)),
                    Codec.STRING.optionalFieldOf("surname").forGetter(d -> Optional.ofNullable(d.surname)),
                    CodecHelper.enumCodec(Gender.class, Gender.UNDEFINED).fieldOf("gender").forGetter(d -> d.gender)
            ).apply(inst, ((neutralGift, giftItems, look, conversations, greetings, profession, weight, name, surname, gender) -> new NPCData(name.orElse(null), surname.orElse(null),
                    gender, profession.orElse(null), look.orElse(null), weight, neutralGift, greetings, conversations, giftItems))));

    public enum Gender {
        UNDEFINED,
        MALE,
        FEMALE;
    }

    public static class Builder {

        private final String name;
        private final String surname;
        private final Gender gender;
        private final int weight;
        private String neutralGiftResponse;
        private EnumShop profession;
        private final List<Conversation> greetings = new ArrayList<>();
        private final List<Conversation> conversations = new ArrayList<>();
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
                this.neutralGiftResponse = "npc." + name + ".default.gift";
            }
        }

        public Builder withDefaultConvosOfName(String name) {
            this.neutralGiftResponse = "npc." + name + ".default.gift";
            return this;
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

        public Builder addGreeting(Conversation greeting) {
            this.greetings.add(greeting);
            return this;
        }

        public Builder addConversation(Conversation conversation) {
            this.conversations.add(conversation);
            return this;
        }

        public Builder addGiftResponse(String id, Gift gift) {
            this.giftItems.put(id, gift);
            return this;
        }

        public NPCData build() {
            if (this.greetings.isEmpty() || this.conversations.isEmpty() || this.neutralGiftResponse == null)
                throw new IllegalStateException("Not all required fields are filled.");
            return new NPCData(this.name, this.surname, this.gender, this.profession, this.look, this.weight, this.neutralGiftResponse, this.greetings, this.conversations, this.giftItems);
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
