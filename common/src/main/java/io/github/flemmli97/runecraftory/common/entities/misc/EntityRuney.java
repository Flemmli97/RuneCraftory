package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.items.tools.ItemStatIncrease;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class EntityRuney extends Entity {

    private static final EntityDataAccessor<Byte> TYPE = SynchedEntityData.defineId(EntityRuney.class, EntityDataSerializers.BYTE);

    private int ticksExisted;

    public EntityRuney(EntityType<?> entityType, Level level) {
        super(entityType, level);
        if (!level.isClientSide)
            this.setType((byte) level.random.nextInt(4));
    }

    @Override
    public void tick() {
        super.tick();
        this.ticksExisted++;
        if (!this.level.isClientSide) {
            if (this.ticksExisted > 6000)
                this.discard();
        }
    }

    @Override
    public void playerTouch(Player player) {
        if (this.level.isClientSide) {
            return;
        }
        this.discard();
        ItemStatIncrease.Stat stat = switch (this.getEntityData().get(TYPE)) {
            case 0 -> ItemStatIncrease.Stat.STR;
            case 2 -> ItemStatIncrease.Stat.INT;
            case 3 -> ItemStatIncrease.Stat.VIT;
            default -> ItemStatIncrease.Stat.HP;
        };
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
            data.increaseStatBonus(player, stat);
            data.refreshRunePoints(player, 150);
        });
        player.level.playSound(null, player.blockPosition(), SoundEvents.GLASS_BREAK, this.getSoundSource(), 1, 0.5f);
    }

    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(TYPE, (byte) 0);
    }

    public void setType(byte b) {
        this.getEntityData().set(TYPE, b);
    }

    public byte type() {
        return this.getEntityData().get(TYPE);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.ticksExisted = compound.getInt("TicksExisted");
        this.entityData.set(TYPE, compound.getByte("Type"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("TicksExisted", this.ticksExisted);
        compound.putByte("Type", this.entityData.get(TYPE));
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
}
