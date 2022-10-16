package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.entities.npc.ShopState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class S2COpenNPCGui implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_npc_gui");

    private final int entityID;
    private final ShopState isShopOpen;
    private final int followState;

    private S2COpenNPCGui(int id, ShopState isShopOpen, int followState) {
        this.entityID = id;
        this.isShopOpen = isShopOpen;
        this.followState = followState;
    }

    public S2COpenNPCGui(EntityNPCBase entity, ServerPlayer player) {
        this.entityID = entity.getId();
        this.isShopOpen = entity.canTrade();
        if (entity.getEntityToFollowUUID() == null)
            this.followState = 0;
        else
            this.followState = entity.getEntityToFollowUUID().equals(player.getUUID()) ? 1 : 2;
    }

    public static S2COpenNPCGui read(FriendlyByteBuf buf) {
        return new S2COpenNPCGui(buf.readInt(), buf.readEnum(ShopState.class), buf.readInt());
    }

    public static void handle(S2COpenNPCGui pkt) {
        ClientHandlers.openNPCChat(pkt.entityID, pkt.isShopOpen, pkt.followState);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityID);
        buf.writeEnum(this.isShopOpen);
        buf.writeInt(this.followState);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
