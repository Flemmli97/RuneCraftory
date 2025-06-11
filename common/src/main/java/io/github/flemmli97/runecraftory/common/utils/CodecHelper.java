package io.github.flemmli97.runecraftory.common.utils;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class CodecHelper {

    public static final Codec<ItemStack> ITEM_OR_STACK = Codec.either(Registry.ITEM.byNameCodec(), ItemStack.CODEC)
            .xmap(e -> e.map(ItemStack::new, s -> s), l -> l.hasTag() ? Either.right(l) : Either.left(l.getItem()));

    public static <T> Codec<List<T>> listOrSingle(Codec<T> codec) {
        return Codec.either(codec, codec.listOf())
                .xmap(e -> e.map(List::of, l -> l), l -> l.size() == 1 ? Either.left(l.get(0)) : Either.right(l));
    }
}
