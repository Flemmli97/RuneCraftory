package io.github.flemmli97.runecraftory.common.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ListItemStackHolder {

    public static final ListItemStackHolder DEFAULT = new ListItemStackHolder(List.of());
    public static final Codec<ListItemStackHolder> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(ItemStack.CODEC.listOf().fieldOf("stacks").forGetter(d -> d.stacks)
            ).apply(instance, ListItemStackHolder::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ListItemStackHolder> STREAM_CODEC = ByteBufCodecs.<RegistryFriendlyByteBuf, ItemStack>list()
            .apply(ItemStack.STREAM_CODEC).map(ListItemStackHolder::new, holder -> holder.stacks);

    private final List<ItemStack> stacks;

    public ListItemStackHolder(List<ItemStack> stacks) {
        this.stacks = stacks;
    }


    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof ListItemStackHolder other) {
            return this.stacks.equals(other.stacks);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.stacks.hashCode();
    }
}
