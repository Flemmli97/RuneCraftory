package io.github.flemmli97.runecraftory.common.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
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

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class CodecHelper {

    public static Codec<EntityPredicate> ENTITY_PREDICATE_CODEC = new Codec<>() {

        @Override
        public <T> DataResult<T> encode(EntityPredicate input, DynamicOps<T> ops, T prefix) {
            JsonElement e = input.serializeToJson();
            if (e instanceof JsonObject) {
                DataResult<Stream<Pair<JsonPrimitive, JsonElement>>> mapLike = DataResult.success(e.getAsJsonObject().entrySet().stream()
                        .filter(entry -> !(entry.getValue() instanceof JsonNull))
                        .map(entry -> Pair.of(new JsonPrimitive(entry.getKey()), entry.getValue())));
                return ops.mergeToPrimitive(prefix, ops.createMap(mapLike.result().orElse(Stream.empty()).map(entry ->
                        Pair.of(JsonOps.INSTANCE.convertTo(ops, entry.getFirst()), JsonOps.INSTANCE.convertTo(ops, entry.getSecond()))
                )));
            }
            return ops.mergeToPrimitive(prefix, JsonOps.INSTANCE.convertTo(ops, input.serializeToJson()));
        }

        @Override
        public <T> DataResult<Pair<EntityPredicate, T>> decode(DynamicOps<T> ops, T input) {
            return DataResult.success(Pair.of(EntityPredicate.fromJson(ops.convertTo(JsonOps.INSTANCE, input)), input));
        }

        @Override
        public String toString() {
            return "EntityPredicate";
        }
    };

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
}
