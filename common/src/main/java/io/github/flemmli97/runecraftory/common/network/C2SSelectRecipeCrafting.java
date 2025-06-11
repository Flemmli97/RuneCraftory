package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerCrafting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public record C2SSelectRecipeCrafting(int id) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<C2SSelectRecipeCrafting> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("c2s_select_recipe_crafting"));

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SSelectRecipeCrafting> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public C2SSelectRecipeCrafting decode(RegistryFriendlyByteBuf buf) {
            return new C2SSelectRecipeCrafting(buf.readInt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, C2SSelectRecipeCrafting pkt) {
            buf.writeInt(pkt.id);
        }
    };

    public static C2SSelectRecipeCrafting read(RegistryFriendlyByteBuf buf) {
        return new C2SSelectRecipeCrafting(buf.readInt());
    }

    public static void handle(C2SSelectRecipeCrafting pkt, ServerPlayer sender) {
        if (sender.containerMenu instanceof ContainerCrafting crafting)
            crafting.updateCurrentRecipeIndex(pkt.id);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
