package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class S2CUpdateNPCData implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_npc_data");

    private final int entityID;
    private final CompoundTag hearts;
    private final CompoundTag schedule;

    private S2CUpdateNPCData(int id, CompoundTag tag, CompoundTag schedule) {
        this.entityID = id;
        this.hearts = tag;
        this.schedule = schedule;
    }

    public S2CUpdateNPCData(EntityNPCBase entity, CompoundTag hearts) {
        this.entityID = entity.getId();
        this.hearts = hearts;
        this.schedule = entity.getSchedule().save();
    }

    public static S2CUpdateNPCData read(FriendlyByteBuf buf) {
        return new S2CUpdateNPCData(buf.readInt(), buf.readNbt(), buf.readNbt());
    }

    public static void handle(S2CUpdateNPCData pkt) {
        Player player = ClientHandlers.getPlayer();
        if (player == null)
            return;
        Entity e = player.getLevel().getEntity(pkt.entityID);
        if (e instanceof EntityNPCBase npc) {
            npc.updateFriendPointsFrom(player, pkt.hearts);
            npc.syncActivity(pkt.schedule);
        }
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityID);
        buf.writeNbt(this.hearts);
        buf.writeNbt(this.schedule);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
