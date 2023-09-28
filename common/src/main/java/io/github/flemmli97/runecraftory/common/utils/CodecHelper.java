package io.github.flemmli97.runecraftory.common.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.tenshilib.platform.registry.SimpleRegistryWrapper;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class CodecHelper {

    private static final Gson GSON = Deserializers.createConditionSerializer().create();

    public static Codec<EntityPredicate> ENTITY_PREDICATE_CODEC = jsonCodecBuilder(EntityPredicate::serializeToJson, EntityPredicate::fromJson, "EntityPredicate");
    public static Codec<NumberProvider> NUMER_PROVIDER_CODEC = jsonCodecBuilder(GSON::toJsonTree, e -> GSON.fromJson(e, NumberProvider.class), "NumberProvider");

    public static <T extends Enum<T>> Codec<T> enumCodec(Class<T> clss, T fallback) {
        return Codec.STRING.flatXmap(s -> {
            try {
                return DataResult.success(Enum.valueOf(clss, s));
            } catch (IllegalArgumentException e) {
                if (fallback != null)
                    return DataResult.success(fallback);
                return DataResult.error("No such enum constant " + s + " for class " + clss);
            }
        }, e -> DataResult.success(e.name()));
    }

    public static <T> Codec<T> ofCustomRegistry(Supplier<SimpleRegistryWrapper<T>> registry, ResourceKey<? extends Registry<T>> key) {
        return ResourceLocation.CODEC.flatXmap(res -> {
            T entry = registry.get().getFromId(res);
            if (!registry.get().getIDFrom(entry).equals(res))
                return DataResult.error("Unknown registry key in " + key + ": " + res);
            return DataResult.success(entry);
        }, val -> Optional.ofNullable(registry.get().getIDFrom(val))
                .map(DataResult::success).orElseGet(() -> DataResult.error("Unknown registry element in " + key + ":" + val)));
    }

    public static <E> Codec<E> jsonCodecBuilder(Function<E, JsonElement> encode, Function<JsonElement, E> decode, String name) {
        return new Codec<>() {
            @Override
            public <T> DataResult<T> encode(E input, DynamicOps<T> ops, T prefix) {
                try {
                    JsonElement e = encode.apply(input);
                    return DataResult.success(NullableJsonOps.INSTANCE.convertTo(ops, e));
                } catch (JsonParseException err) {
                    return DataResult.error("Couldn't encode value " + input + " error: " + err);
                }
            }

            @Override
            public <T> DataResult<Pair<E, T>> decode(DynamicOps<T> ops, T input) {
                JsonElement element = ops.convertTo(JsonOps.INSTANCE, input);
                try {
                    E result = decode.apply(element);
                    return DataResult.success(Pair.of(result, input));
                } catch (JsonParseException err) {
                    return DataResult.error("Couldn't decode value " + err);
                }
            }

            @Override
            public String toString() {
                return name;
            }
        };
    }


    public static class NullableJsonOps extends JsonOps {

        public static final JsonOps INSTANCE = new NullableJsonOps(false);

        protected NullableJsonOps(boolean compressed) {
            super(compressed);
        }

        @Override
        public <U> U convertMap(final DynamicOps<U> ops, JsonElement e) {
            DataResult<Stream<Pair<JsonPrimitive, JsonElement>>> mapLike = DataResult.success(e.getAsJsonObject().entrySet().stream()
                    .filter(entry -> !(entry.getValue() instanceof JsonNull))
                    .map(entry -> Pair.of(new JsonPrimitive(entry.getKey()), entry.getValue())));
            return ops.createMap(mapLike.result().orElse(Stream.empty()).map(entry ->
                    Pair.of(this.convertTo(ops, entry.getFirst()),
                            this.convertTo(ops, entry.getSecond()))
            ));
        }
    }
}
