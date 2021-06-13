package io.github.flemmli97.runecraftory.common.capability;

import io.github.flemmli97.runecraftory.common.network.PacketHandler;
import io.github.flemmli97.runecraftory.common.network.S2CEntityDataSync;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityCapImpl implements IEntityCap, ICapabilityProvider {

    private final LazyOptional<IEntityCap> holder = LazyOptional.of(() -> this);

    private boolean sleeping, paralysis, cold, poison;

    @Override
    public void setSleeping(LivingEntity entity, boolean flag) {
        this.sleeping = flag;
        if (!entity.world.isRemote) {
            PacketHandler.sendToTrackingAndSelf(new S2CEntityDataSync(entity.getEntityId(), S2CEntityDataSync.Type.SLEEP, this.sleeping), entity);
        }
    }

    @Override
    public boolean isSleeping() {
        return this.sleeping;
    }

    @Override
    public void setPoison(LivingEntity entity, boolean flag) {
        this.poison = flag;
        if (!entity.world.isRemote) {
            PacketHandler.sendToTrackingAndSelf(new S2CEntityDataSync(entity.getEntityId(), S2CEntityDataSync.Type.POISON, this.poison), entity);
        }
    }

    @Override
    public boolean isPoisoned() {
        return this.poison;
    }

    @Override
    public void setCold(LivingEntity entity, boolean flag) {
        this.cold = flag;
        if (!entity.world.isRemote) {
            PacketHandler.sendToTrackingAndSelf(new S2CEntityDataSync(entity.getEntityId(), S2CEntityDataSync.Type.COLD, this.cold), entity);
        }
    }

    @Override
    public boolean hasCold() {
        return this.cold;
    }

    @Override
    public void setParalysis(LivingEntity entity, boolean flag) {
        this.paralysis = flag;
        if (!entity.world.isRemote) {
            PacketHandler.sendToTrackingAndSelf(new S2CEntityDataSync(entity.getEntityId(), S2CEntityDataSync.Type.PARALYSIS, this.paralysis), entity);
        }
    }

    @Override
    public boolean isParalysed() {
        return this.paralysis;
    }

    @Override
    public void writeAllToPkt(PacketBuffer buffer) {
        buffer.writeBoolean(this.sleeping);
        buffer.writeBoolean(this.cold);
        buffer.writeBoolean(this.paralysis);
        buffer.writeBoolean(this.poison);
    }

    @Override
    public void readFromPacket(PacketBuffer buffer) {
        this.sleeping = buffer.readBoolean();
        this.cold = buffer.readBoolean();
        this.paralysis = buffer.readBoolean();
        this.poison = buffer.readBoolean();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CapabilityInsts.EntityCap.orEmpty(cap, this.holder);
    }
}
