package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.MobAttackExt;
import io.github.flemmli97.runecraftory.common.entities.RunecraftoryBossbar;
import io.github.flemmli97.runecraftory.common.entities.ai.pathing.StaticNavigator;
import io.github.flemmli97.runecraftory.common.entities.misc.SarcophagusTeleporter;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class EntitySanoUno extends BossMonster implements MobAttackExt {

    private static final EntityDataAccessor<Boolean> CAN_BE_REMOVED = SynchedEntityData.defineId(EntitySanoUno.class, EntityDataSerializers.BOOLEAN);

    private UUID linkedID;

    protected boolean reversedSwipe;
    protected Vec3 targetPos;

    public EntitySanoUno(EntityType<? extends EntitySanoUno> type, Level world) {
        super(type, world);
        this.lookControl = new NonLookControl(this);
    }

    @Override
    public RunecraftoryBossbar createBossBar() {
        return new RunecraftoryBossbar(null, this.getDisplayName(), BossEvent.BossBarColor.YELLOW, BossEvent.BossBarOverlay.PROGRESS)
                .setMusic(ModSounds.SANO_UNO_FIGHT.get());
    }

    public void linkUsing(UUID uuid) {
        this.linkedID = uuid;
        this.bossInfo.setMusicID(this.linkedID);
    }

    @Nullable
    public UUID getLinkedID() {
        return this.linkedID;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CAN_BE_REMOVED, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.linkedID != null)
            compound.putUUID("LinkedID", this.linkedID);
        compound.putBoolean("CanBeRemoved", this.entityData.get(CAN_BE_REMOVED));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.hasUUID("LinkedID"))
            this.linkedID = compound.getUUID("LinkedID");
        this.entityData.set(CAN_BE_REMOVED, compound.getBoolean("CanBeRemoved"));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new StaticNavigator(this, level);
    }

    @Override
    public void push(double x, double y, double z) {
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public void travel(Vec3 vec) {
        this.xxa = 0;
        this.yya = 0;
        this.zza = 0;
        this.setDeltaMovement(0, this.getDeltaMovement().y - 0.08, 0);
        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    @Override
    protected boolean checkRage() {
        return false;
    }

    @Override
    public void playInteractionAnimation() {
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source == DamageSource.IN_WALL)
            return false;
        if (source == DamageSource.OUT_OF_WORLD && this.deathTime == this.maxDeathTime() - 1) {
            this.remove(RemovalReason.KILLED);
            return true;
        }
        return super.hurt(source, amount);
    }

    @Override
    protected void tickDeath() {
        if (!this.level.isClientSide && this.deathTime == 0) {
            EntitySanoUno other = this.getLinked();
            // Notify the other that their death should be treated as normal
            if (other != null && !this.entityData.get(CAN_BE_REMOVED))
                other.entityData.set(CAN_BE_REMOVED, true);
        }
        if (this.deathTime == this.maxDeathTime() - 1) {
            if (this.level.isClientSide)
                return;
            EntitySanoUno other = this.getLinked();
            // Keep this one in the world while the other is still alive
            if (!this.entityData.get(CAN_BE_REMOVED) && other != null && !other.isRemoved())
                return;
        }
        super.tickDeath();
    }

    @Override
    public void remove(RemovalReason reason) {
        if (!this.level.isClientSide && reason == RemovalReason.KILLED && this.linkedID != null) {
            Vec3 dir = Vec3.directionFromRotation(0, this.getYRot());
            SarcophagusTeleporter teleporter = new SarcophagusTeleporter(ModEntities.SARCOPHAGUS_TELEPORTER.get(), this.level);
            teleporter.setPos(this.position().add(dir.scale(-2)));
            this.level.addFreshEntity(teleporter);
        }
        super.remove(reason);
    }

    @Override
    public Vec3 passengerOffset(Entity passenger) {
        return new Vec3(0, 30.5 / 16d, 6 / 16d).scale(2);
    }

    @Override
    public Vec3 targetPosition(Vec3 from) {
        return this.targetPos;
    }

    @Override
    public boolean reversed() {
        return this.reversedSwipe;
    }

    public abstract EntitySanoUno getLinked();

    static class NonLookControl extends LookControl {

        public NonLookControl(Mob mob) {
            super(mob);
        }

        @Override
        public void tick() {
        }
    }
}
