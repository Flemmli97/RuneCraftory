package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.attachment.player.LevelExpPair;
import io.github.flemmli97.runecraftory.common.entities.IBaseMob;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class S2CEntityLevelPkt implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_entity_level");

    private final int entityID;
    private final LevelExpPair level;

    private S2CEntityLevelPkt(int id, LevelExpPair level) {
        this.entityID = id;
        this.level = level;
    }

    public static <T extends Entity & IBaseMob> S2CEntityLevelPkt create(T entity) {
        return new S2CEntityLevelPkt(entity.getId(), entity.level());
    }

    public static S2CEntityLevelPkt read(FriendlyByteBuf buf) {
        return new S2CEntityLevelPkt(buf.readInt(), new LevelExpPair(buf));
    }

    public static void handle(S2CEntityLevelPkt pkt) {
        Player player = ClientHandlers.getPlayer();
        if (player == null)
            return;
        Entity e = player.getLevel().getEntity(pkt.entityID);
        if (e instanceof IBaseMob mob) {
            mob.level().from(pkt.level);
        }
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityID);
        this.level.toPacket(buf);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
