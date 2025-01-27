package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.AttackAABBRender;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.utils.OrientedBoundingBox;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class S2CAttackDebug implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_attack_debug");

    public static void sendDebugPacket(OrientedBoundingBox aabb, EnumAABBType type, Entity entity) {
        if (GeneralConfig.debugAttack) {
            if (entity instanceof ServerPlayer player) {
                Platform.INSTANCE.sendToClient(new S2CAttackDebug(aabb, EnumAABBType.PLAYER), player);
            } else {
                Platform.INSTANCE.sendToTrackingAndSelf(new S2CAttackDebug(aabb, type), entity);
            }
        }
    }

    private final OrientedBoundingBox obb;
    private final int duration;
    private final EnumAABBType type;

    public S2CAttackDebug(OrientedBoundingBox aabb) {
        this(aabb, 200, EnumAABBType.ATTACK);
    }

    public S2CAttackDebug(OrientedBoundingBox aabb, EnumAABBType type) {
        this(aabb, 200, type);
    }

    public S2CAttackDebug(OrientedBoundingBox aabb, int duration, EnumAABBType type) {
        this.obb = aabb;
        this.duration = duration;
        this.type = type;
    }

    public static S2CAttackDebug read(FriendlyByteBuf buf) {
        return new S2CAttackDebug(OrientedBoundingBox.fromBuffer(buf), buf.readInt(), buf.readEnum(EnumAABBType.class));
    }

    public static void handle(S2CAttackDebug pkt) {
        Player player = ClientHandlers.getPlayer();
        if (player == null || !GeneralConfig.debugAttack)
            return;
        AttackAABBRender.INST.addNewAABB(pkt.obb, pkt.duration, pkt.type);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        this.obb.toBuffer(buf);
        buf.writeInt(this.duration);
        buf.writeEnum(this.type);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public enum EnumAABBType {

        ATTEMPT,
        ATTACK,
        PLAYER
    }
}
