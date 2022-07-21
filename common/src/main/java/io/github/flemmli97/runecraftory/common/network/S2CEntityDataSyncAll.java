package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;


public class S2CEntityDataSyncAll implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_entity_data_all");

    private boolean sleeping, paralysis, cold, poison;
    private int entityID;

    public S2CEntityDataSyncAll(LivingEntity entity) {
        this.entityID = entity.getId();
        Platform.INSTANCE.getEntityData(entity)
                .ifPresent(data -> {
                    this.sleeping = data.isSleeping();
                    this.paralysis = data.isParalysed();
                    this.cold = data.hasCold();
                    this.poison = data.isPoisoned();
                });
    }

    private S2CEntityDataSyncAll(int entityID, boolean sleeping, boolean paralysis, boolean cold, boolean poison) {
        this.entityID = entityID;
        this.sleeping = sleeping;
        this.paralysis = paralysis;
        this.cold = cold;
        this.poison = poison;
    }

    public static S2CEntityDataSyncAll read(FriendlyByteBuf buf) {
        return new S2CEntityDataSyncAll(buf.readInt(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
    }

    public static void handle(S2CEntityDataSyncAll pkt) {
        Player player = ClientHandlers.getPlayer();
        if (player == null)
            return;
        Entity e = player.level.getEntity(pkt.entityID);
        if (e instanceof LivingEntity living) {
            Platform.INSTANCE.getEntityData(living).ifPresent(data -> {
                data.setSleeping(living, pkt.sleeping);
                data.setParalysis(living, pkt.paralysis);
                data.setCold(living, pkt.cold);
                data.setPoison(living, pkt.poison);
            });
        }
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityID);
        buf.writeBoolean(this.sleeping);
        buf.writeBoolean(this.paralysis);
        buf.writeBoolean(this.cold);
        buf.writeBoolean(this.poison);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
