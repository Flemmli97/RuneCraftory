package io.github.flemmli97.runecraftory.common.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListItemStackHolder implements Iterable<ItemStack> {

    public static final ListItemStackHolder DEFAULT = new ListItemStackHolder(List.of());
    public static final Codec<ListItemStackHolder> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(ItemStack.CODEC.listOf().fieldOf("stacks").forGetter(d -> d.stacks)
            ).apply(instance, ListItemStackHolder::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ListItemStackHolder> STREAM_CODEC = ByteBufCodecs.<RegistryFriendlyByteBuf, ItemStack>list()
            .apply(ItemStack.STREAM_CODEC).map(ListItemStackHolder::new, holder -> holder.stacks);

    private final List<ItemStack> stacks;

    public ListItemStackHolder(List<ItemStack> stacks) {
        this.stacks = stacks.stream().map(ItemStack::copy).toList();
    }

    public ListItemStackHolder add(ItemStack add) {
        List<ItemStack> stacks = new ArrayList<>(this.stacks);
        stacks.add(add.copy());
        return new ListItemStackHolder(stacks);
    }

    public int matchesItem(ItemStack other) {
        int matches = 0;
        for (ItemStack stack : this.stacks)
            if (ItemStack.isSameItem(stack, other))
                matches++;
        return matches;
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

    @Override
    public Iterator<ItemStack> iterator() {
        return this.stacks.stream().map(ItemStack::copy).iterator();
    }
}
