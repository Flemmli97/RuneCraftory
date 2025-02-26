package io.github.flemmli97.runecraftory.common.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.Registry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.List;
import java.util.function.Function;

public class CodecHelper {

    private static final Gson GSON = Deserializers.createConditionSerializer()
            .create();

    public static Codec<ItemPredicate> ITEM_PREDICATE_CODEC = CodecUtils.jsonCodecBuilder(nullToObj(ItemPredicate::serializeToJson), ItemPredicate::fromJson, "ItemPredicate");
    public static final Codec<EntityPredicate> ENTITY_PREDICATE_CODEC = CodecUtils.jsonCodecBuilder(nullToObj(EntityPredicate::serializeToJson), EntityPredicate::fromJson, "EntityPredicate");
    public static final Codec<NumberProvider> NUMER_PROVIDER_CODEC = CodecUtils.jsonCodecBuilder(GSON::toJsonTree, e -> GSON.fromJson(e, NumberProvider.class), "NumberProvider");

    public static final Codec<ItemStack> ITEM_OR_STACK = Codec.either(Registry.ITEM.byNameCodec(), ItemStack.CODEC)
            .xmap(e -> e.map(ItemStack::new, s -> s), l -> l.hasTag() ? Either.right(l) : Either.left(l.getItem()));

    private static <E> Function<E, JsonElement> nullToObj(Function<E, JsonElement> encode) {
        return v -> {
            JsonElement e = encode.apply(v);
            if (e.isJsonNull())
                return new JsonObject();
            return e;
        };
    }

    public static <E> Codec<List<E>> nonEmptyList(Codec<E> codec, String error) {
        Function<List<E>, DataResult<List<E>>> function = list -> {
            if (list.isEmpty())
                return DataResult.error(error);
            return DataResult.success(list);
        };
        return codec.listOf().flatXmap(function, function);
    }

    public static <T> Codec<List<T>> listOrSingle(Codec<T> codec) {
        return Codec.either(codec, codec.listOf())
                .xmap(e -> e.map(List::of, l -> l), l -> l.size() == 1 ? Either.left(l.get(0)) : Either.right(l));
    }

    public static <T> Codec<List<T>> itemOrStack(Codec<T> codec) {
        return Codec.either(codec, codec.listOf())
                .xmap(e -> e.map(List::of, l -> l), l -> l.size() == 1 ? Either.left(l.get(0)) : Either.right(l));
    }
}
