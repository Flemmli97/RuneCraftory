package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class EntityFireball extends BaseProjectile {

    private static final EntityDataAccessor<Boolean> BIG = SynchedEntityData.defineId(EntityFireball.class, EntityDataSerializers.BOOLEAN);

    public EntityFireball(EntityType<? extends EntityFireball> type, Level level) {
        super(type, level);
    }

    public EntityFireball(Level level, LivingEntity shooter, boolean big) {
        super(ModEntities.FIRE_BALL.get(), level, shooter);
        this.entityData.set(BIG, big);
    }

    public boolean big() {
        return this.entityData.get(BIG);
    }

    @Override
    public int livingTickMax() {
        return 200;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(BIG, false);
    }

    @Override
    protected float getGravityVelocity() {
        return 0.0025f;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        boolean att = CombatUtils.damageWithFaintAndCrit(this.getOwner(), result.getEntity(), new CustomDamage.Builder(this, this.getOwner()).magic().noKnockback().element(EnumElement.FIRE).hurtResistant(5), CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.MAGIC.get()) * this.damageMultiplier, null);
        this.level.playSound(null, result.getEntity().blockPosition(), SoundEvents.GENERIC_EXPLODE, this.getSoundSource(), 1.0f, 1.0f);
        this.discard();
        return att;
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {
        this.level.playSound(null, result.getLocation().x, result.getLocation().y, result.getLocation().z, SoundEvents.GENERIC_EXPLODE, this.getSoundSource(), 1.0f, 1.0f);
        this.discard();
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(BIG, compound.getBoolean("Big"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Big", this.big());
    }
}
