package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public record S2CEntityDataSync(int entityID,
                                S2CEntityDataSync.Type type,
                                boolean flag) implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_entity_data_sync");

    public static S2CEntityDataSync read(FriendlyByteBuf buf) {
        return new S2CEntityDataSync(buf.readInt(), buf.readEnum(Type.class), buf.readBoolean());
    }

    public static void handle(S2CEntityDataSync pkt) {
        Player player = ClientHandlers.getPlayer();
        if (player == null)
            return;
        Entity e = player.level.getEntity(pkt.entityID);
        if (e instanceof LivingEntity living) {
            Platform.INSTANCE.getEntityData(living).ifPresent(data -> {
                switch (pkt.type) {
                    case POISON -> data.setPoison(living, pkt.flag);
                    case SLEEP -> data.setSleeping(living, pkt.flag);
                    case PARALYSIS -> data.setParalysis(living, pkt.flag);
                    case COLD -> data.setCold(living, pkt.flag);
                    case INVIS -> data.setInvis(living, pkt.flag);
                    case ORTHOVIEW -> data.setOrthoView(living, pkt.flag);
                }
            });
        }
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityID);
        buf.writeEnum(this.type);
        buf.writeBoolean(this.flag);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public enum Type {

        POISON,
        SLEEP,
        PARALYSIS,
        COLD,
        FATIGUE,
        SEAL,
        INVIS,
        ORTHOVIEW
    }
}
