package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.entities.data.MobUpdateHandler;
import io.github.flemmli97.runecraftory.common.entities.data.SyncableEntityData;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class S2CMobUpdate implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_mob_update_data");

    private final int entity;
    private final SyncableEntityData.SyncedContainer<?> value;

    private S2CMobUpdate(int entity, SyncableEntityData.SyncedContainer<?> value) {
        this.entity = entity;
        this.value = value;
    }

    public static <T extends Entity & MobUpdateHandler, D> void send(T entity, SyncableEntityData.SyncedEntityData<D> key, D value) {
        if (!entity.level.isClientSide)
            Platform.INSTANCE.sendToTrackingAndSelf(new S2CMobUpdate(entity.getId(), new SyncableEntityData.SyncedContainer<>(key, value)), entity);
    }

    public static S2CMobUpdate read(FriendlyByteBuf buf) {
        return new S2CMobUpdate(buf.readInt(), SyncableEntityData.SyncedContainer.from(buf));
    }

    public static void handle(S2CMobUpdate pkt) {
        Player player = ClientHandlers.getPlayer();
        if (player == null)
            return;
        Entity entity = player.level.getEntity(pkt.entity);
        if (entity instanceof MobUpdateHandler handler) {
            handler.onUpdate(pkt.value);
        }
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entity);
        this.value.write(buf);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
