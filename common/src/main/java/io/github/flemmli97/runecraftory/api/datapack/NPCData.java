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
import io.github.flemmli97.runecraftory.common.entities.npc.NPCSchedule;
import io.github.flemmli97.runecraftory.common.entities.npc.job.NPCJob;
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
                      Map<ConversationType, ConversationSet> interactions,
                      Map<String, Gift> giftItems, @Nullable NPCSchedule.Schedule schedule) {

    public static final NPCData DEFAULT_DATA = new NPCData(null, null, Gender.UNDEFINED, null, null, null, 1, "npc.default.gift.neutral",
            buildDefaultInteractionMap(), Map.of(), null);

    private static Map<ConversationType, ConversationSet> buildDefaultInteractionMap() {
        ImmutableMap.Builder<ConversationType, ConversationSet> builder = new ImmutableMap.Builder<>();
        for (ConversationType type : ConversationType.values())
            builder.put(type, new ConversationSet("npc.default." + type.key + ".default", List.of()));
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
                    filledMap(ConversationType.class, Codec.unboundedMap(CodecHelper.enumCodec(ConversationType.class, null), ConversationSet.CODEC))
                            .fieldOf("interactions").forGetter(d -> d.interactions),
                    NPCSchedule.Schedule.CODEC.optionalFieldOf("schedule").forGetter(d -> Optional.ofNullable(d.schedule)),

                    ExtraCodecs.POSITIVE_INT.fieldOf("weight").forGetter(d -> d.weight),
                    Codec.STRING.fieldOf("neutralGiftResponse").forGetter(d -> d.neutralGiftResponse),
                    Codec.unboundedMap(Codec.STRING, Gift.CODEC).fieldOf("giftItems").forGetter(d -> d.giftItems),

                    ModNPCJobs.CODEC.optionalFieldOf("profession").forGetter(d -> Optional.ofNullable(d.profession)),
                    ResourceLocation.CODEC.optionalFieldOf("look").forGetter(d -> Optional.ofNullable(d.look)),
                    WorldUtils.DATE.optionalFieldOf("birthday").forGetter(d -> Optional.ofNullable(d.birthday)),

                    Codec.STRING.optionalFieldOf("name").forGetter(d -> Optional.ofNullable(d.name)),
                    Codec.STRING.optionalFieldOf("surname").forGetter(d -> Optional.ofNullable(d.surname)),
                    CodecHelper.enumCodec(Gender.class, Gender.UNDEFINED).fieldOf("gender").forGetter(d -> d.gender)
            ).apply(inst, ((interactions, schedule, weight, neutralGift, giftItems, profession, look, birthday, name, surname, gender) -> new NPCData(name.orElse(null), surname.orElse(null),
                    gender, profession.orElse(null), look.orElse(null), birthday.orElse(null), weight, neutralGift, interactions, giftItems, schedule.orElse(null)))));


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

    public static class Builder {

        private final String name;
        private final String surname;
        private final Gender gender;
        private final int weight;
        private String neutralGiftResponse;
        private NPCJob profession;
        private final Map<ConversationType, ConversationSet> interactions = new TreeMap<>();
        private final Map<String, Gift> giftItems = new LinkedHashMap<>();
        private Pair<EnumSeason, Integer> birthday;
        private NPCSchedule.Schedule schedule;
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

        public Builder withBirthday(Pair<EnumSeason, Integer> birthday) {
            this.birthday = birthday;
            return this;
        }

        public Builder withProfession(NPCJob profession) {
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

        public Builder withSchedule(NPCSchedule.Schedule schedule) {
            this.schedule = schedule;
            return this;
        }

        public NPCData build() {
            if (this.neutralGiftResponse == null)
                throw new IllegalStateException("Neutral gift response not set.");
            for (ConversationType type : ConversationType.values()) {
                if (!this.interactions.containsKey(type))
                    throw new IllegalStateException("Missing interactions for " + type);
            }
            return new NPCData(this.name, this.surname, this.gender, this.profession, this.look, this.birthday, this.weight, this.neutralGiftResponse, this.interactions, this.giftItems, this.schedule);
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

    public record Conversation(String translationKey, int minHearts, int maxHearts, LootItemCondition... conditions) {

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
                        Codec.STRING.fieldOf("translationKey").forGetter(d -> d.translationKey),
                        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("minHearts").forGetter(d -> d.minHearts),
                        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("maxHearts").forGetter(d -> d.maxHearts),
                        LOOT_ITEM_CONDITION_CODEC.listOf().fieldOf("conditions").forGetter(d -> Arrays.stream(d.conditions).toList())
                ).apply(inst, (key, min, max, cond) -> new Conversation(key, min, max, cond.toArray(new LootItemCondition[0]))));

        public boolean test(int hearts, LootContext ctx) {
            if (this.minHearts > hearts || this.maxHearts < hearts)
                return false;
            for (LootItemCondition condition : this.conditions)
                if (!condition.test(ctx))
                    return false;
            return true;
        }
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
                        CodecHelper.enumCodec(Gender.class, Gender.UNDEFINED).fieldOf("gender").forGetter(d -> d.gender),
                        ResourceLocation.CODEC.optionalFieldOf("texture").forGetter(d -> Optional.ofNullable(d.texture)),
                        Codec.STRING.optionalFieldOf("player_skin").forGetter(d -> Optional.ofNullable(d.playerSkin)),
                        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("weight").forGetter(d -> d.weight),
                        Codec.pair(ResourceLocation.CODEC, ResourceLocation.CODEC).listOf().fieldOf("additionalFeatures").forGetter(d -> d.additionalFeatures)
                ).apply(inst, (gender, text, skin, weight, features) -> {
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
