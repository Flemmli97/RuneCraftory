package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class C2SProcreationRequest implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "c2s_procreation_request");

    private final int id;

    public C2SProcreationRequest(int entityID) {
        this.id = entityID;
    }

    public static C2SProcreationRequest read(FriendlyByteBuf buf) {
        return new C2SProcreationRequest(buf.readInt());
    }

    public static void handle(C2SProcreationRequest pkt, ServerPlayer sender) {
        if (sender != null) {
            Entity entity = sender.level.getEntity(pkt.id);
            if (entity instanceof EntityNPCBase npc) {
                npc.procreateWith(sender);
            }
        }
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.id);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
