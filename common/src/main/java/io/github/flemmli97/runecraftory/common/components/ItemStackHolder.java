package io.github.flemmli97.runecraftory.common.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record ItemStackHolder(ItemStack stack) {

    public static final ItemStackHolder DEFAULT = new ItemStackHolder(ItemStack.EMPTY);
    public static final Codec<ItemStackHolder> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(ItemStack.CODEC.fieldOf("stack").forGetter(d -> d.stack)
            ).apply(instance, ItemStackHolder::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemStackHolder> STREAM_CODEC = StreamCodec.composite(ItemStack.STREAM_CODEC,
            d -> d.stack, ItemStackHolder::new);

    public ItemStackHolder(ItemStack stack) {
        this.stack = stack.copy();
    }

    @Override
    public ItemStack stack() {
        return this.stack.copy();
    }

    public boolean isEmpty() {
        return this.stack.isEmpty();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof ItemStackHolder other) {
            return this.stack.equals(other.stack);
        }
        return false;
    }
}
