package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.items.tools.ItemStatIncrease;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import io.github.flemmli97.tenshilib.loader.registry.AttachmentRegister;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class RuneyEntity extends Entity {

    private static final EntityDataAccessor<Byte> TYPE = SynchedEntityData.defineId(RuneyEntity.class, EntityDataSerializers.BYTE);

    private int ticksExisted;

    public RuneyEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        if (!level.isClientSide)
            this.setType((byte) level.random.nextInt(4));
    }

    @Override
    public void tick() {
        super.tick();
        this.ticksExisted++;
        if (!this.level().isClientSide) {
            if (this.ticksExisted > 6000)
                this.discard();
        }
    }

    @Override
    public void playerTouch(Player player) {
        if (this.level().isClientSide) {
            return;
        }
        this.discard();
        ItemStatIncrease.Stat stat = switch (this.getEntityData().get(TYPE)) {
            case 0 -> ItemStatIncrease.Stat.STR;
            case 2 -> ItemStatIncrease.Stat.INT;
            case 3 -> ItemStatIncrease.Stat.VIT;
            default -> ItemStatIncrease.Stat.HP;
        };
        PlayerData data = RunecraftoryAttachments.PLAYER_DATA.get().get(player);
        data.increaseStatBonus(stat);
        data.regenRunePoints(150);
        player.level().playSound(null, player.blockPosition(), RuneCraftorySounds.ENTITY_RUNEY_COLLECT.get(), this.getSoundSource(), 1, 0.5f);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(TYPE, (byte) 0);
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
}
