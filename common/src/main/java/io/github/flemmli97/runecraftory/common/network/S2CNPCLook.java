package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.NPCData;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.entities.npc.features.NPCFeatureContainer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class S2CNPCLook implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_npc_look_update");

    private final int id;
    private final NPCData.NPCLook look;
    private final NPCFeatureContainer features;

    public S2CNPCLook(int id, NPCData.NPCLook look, NPCFeatureContainer features) {
        this.id = id;
        this.look = look;
        this.features = features;
    }

    public static S2CNPCLook read(FriendlyByteBuf buf) {
        return new S2CNPCLook(buf.readInt(), NPCData.NPCLook.fromBuffer(buf), new NPCFeatureContainer().fromBuffer(buf));
    }

    public static void handle(S2CNPCLook pkt) {
        Player player = ClientHandlers.getPlayer();
        if (player == null)
            return;
        Entity e = player.getLevel().getEntity(pkt.id);
        if (e instanceof EntityNPCBase npc) {
            npc.lookFeatures.with(pkt.features);
            npc.setClientLook(pkt.look);
        }
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.id);
        this.look.writeToBuffer(buf);
        this.features.toBuffer(buf);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
