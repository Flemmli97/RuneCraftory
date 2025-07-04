package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerCrafting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class S2CCraftingRecipes implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CCraftingRecipes> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_crafting_recipes_container"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CCraftingRecipes> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CCraftingRecipes decode(RegistryFriendlyByteBuf buf) {
            long lastChange = buf.readLong();
            List<ItemStack> list = new ArrayList<>();
            int size = buf.readInt();
            for (int i = 0; i < size; i++)
                list.add(ItemStack.STREAM_CODEC.decode(buf));
            return new S2CCraftingRecipes(new ContainerCrafting.ClientRecipeResult(lastChange, list), buf.readInt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CCraftingRecipes pkt) {
            buf.writeLong(pkt.data.lastChange());
            buf.writeInt(pkt.data.result().size());
            pkt.data.result().forEach(stack -> ItemStack.STREAM_CODEC.encode(buf, stack));
            buf.writeInt(pkt.clientRecipeIndex);
        }
    };

    private final ContainerCrafting.ClientRecipeResult data;
    private final int clientRecipeIndex;

    public S2CCraftingRecipes(ContainerCrafting.ClientRecipeResult data, int clientRecipeIndex) {
        this.data = data;
        this.clientRecipeIndex = clientRecipeIndex;
    }

    public static void handle(S2CCraftingRecipes pkt, Player player) {
        if (player.containerMenu instanceof ContainerCrafting crafting)
            crafting.setMatchingRecipesClient(pkt.data);
        ClientHandlers.updateCurrentRecipeIndex(pkt.clientRecipeIndex);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
