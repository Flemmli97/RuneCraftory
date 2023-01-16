package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.entities.npc.job.ShopState;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;

public class S2COpenNPCGui implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_npc_gui");

    private final int entityID;
    private final ShopState isShopOpen;
    private final int followState;
    private final Map<String, Component> actions;

    private S2COpenNPCGui(int id, ShopState isShopOpen, int followState, Map<String, Component> actions) {
        this.entityID = id;
        this.isShopOpen = isShopOpen;
        this.followState = followState;
        this.actions = actions;
    }

    public S2COpenNPCGui(EntityNPCBase entity, ServerPlayer player) {
        this.entityID = entity.getId();
        this.isShopOpen = entity.canTrade();
        this.actions = entity.getShop().actions(entity, player);
        if (entity.getEntityToFollowUUID() == null)
            this.followState = Platform.INSTANCE.getPlayerData(player).map(d -> d.party.isPartyFull()).orElse(true) ? 2 : 0;
        else
            this.followState = entity.getEntityToFollowUUID().equals(player.getUUID()) ? 1 : 2;
    }

    public static S2COpenNPCGui read(FriendlyByteBuf buf) {
        return new S2COpenNPCGui(buf.readInt(), buf.readEnum(ShopState.class), buf.readInt(), buf.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readComponent));
    }

    public static void handle(S2COpenNPCGui pkt) {
        ClientHandlers.openNPCChat(pkt.entityID, pkt.isShopOpen, pkt.followState, pkt.actions);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityID);
        buf.writeEnum(this.isShopOpen);
        buf.writeInt(this.followState);
        buf.writeMap(this.actions, FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeComponent);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
