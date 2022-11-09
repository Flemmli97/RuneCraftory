package io.github.flemmli97.runecraftory.common.network;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerCrafting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class S2CCraftingRecipes implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_crafting_recipes_container");

    private final List<Pair<Integer, ItemStack>> data;
    private final int clientRecipeIndex;

    public S2CCraftingRecipes(List<Pair<Integer, ItemStack>> data, int clientRecipeIndex) {
        this.data = data;
        this.clientRecipeIndex = clientRecipeIndex;
    }

    public static S2CCraftingRecipes read(FriendlyByteBuf buf) {
        List<Pair<Integer, ItemStack>> list = new ArrayList<>();
        int size = buf.readInt();
        for (int i = 0; i < size; i++)
            list.add(Pair.of(buf.readInt(), buf.readItem()));
        return new S2CCraftingRecipes(list, buf.readInt());
    }

    public static void handle(S2CCraftingRecipes pkt) {
        Player player = ClientHandlers.getPlayer();
        if (player == null)
            return;
        if (player.containerMenu instanceof ContainerCrafting crafting)
            crafting.setMatchingRecipesClient(pkt.data);
        ClientHandlers.updateCurrentRecipeIndex(pkt.clientRecipeIndex);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.data.size());
        this.data.forEach(p -> {
            buf.writeInt(p.getFirst());
            buf.writeItem(p.getSecond());
        });
        buf.writeInt(this.clientRecipeIndex);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
