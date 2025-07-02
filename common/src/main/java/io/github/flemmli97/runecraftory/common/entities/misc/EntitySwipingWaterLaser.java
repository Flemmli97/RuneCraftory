package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;

import java.util.function.Predicate;

public class EntitySwipingWaterLaser extends BaseBeam {

    private static final EntityDataAccessor<Integer> MAX_LIVING_TICK = SynchedEntityData.defineId(EntitySwipingWaterLaser.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> TOTAL_ROTATION = SynchedEntityData.defineId(EntitySwipingWaterLaser.class, EntityDataSerializers.FLOAT);

    private final Object2IntMap<Entity> hitEntities = new Object2IntOpenHashMap<>();

    public EntitySwipingWaterLaser(EntityType<? extends EntitySwipingWaterLaser> type, Level level) {
        super(type, level);
    }

    public EntitySwipingWaterLaser(Level level, LivingEntity shooter, float rotationAngle) {
        super(ModEntities.SWIPING_WATER_LASER.get(), level, shooter);
        this.entityData.set(TOTAL_ROTATION, rotationAngle);
    }

    public EntitySwipingWaterLaser setMaxTicks(int ticks) {
        this.entityData.set(MAX_LIVING_TICK, ticks);
        return this;
    }

    public void setRotationToDirWithOffset(double dirX, double dirY, double dirZ, float acc, float yawOffset) {
        super.setRotationToDir(dirX, dirY, dirZ, acc);
        this.setYRot(this.getYRot() + yawOffset);
        this.updateYawPitch();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(MAX_LIVING_TICK, 20);
        builder.define(TOTAL_ROTATION, 0f);
    }

    @Override
    public float getRange() {
        return 9;
    }

    @Override
    public float radius() {
        return 0.4f;
    }

    @Override
    public boolean piercing() {
        return true;
    }

    @Override
    public int livingTickMax() {
        return this.entityData.get(MAX_LIVING_TICK);
    }

    @Override
    public void tick() {
        this.yRotO = this.getYRot();
        if (this.entityData.get(TOTAL_ROTATION) != 0) {
            float amount = this.entityData.get(TOTAL_ROTATION) / this.livingTickMax();
            this.setYRot(Mth.wrapDegrees(this.getYRot() + amount));
            this.hit = null;
        }
        super.tick();
        if (this.getOwner() instanceof ServerPlayer player) {
            PlayerData data = Platform.INSTANCE.getPlayerData(player);
            AnimationState action = data.getWeaponHandler().getAnimation();
            boolean keep = action != null && action.is(PlayerModelAnimations.WATER_LASER_ONE, PlayerModelAnimations.WATER_LASER_TWO, PlayerModelAnimations.WATER_LASER_THREE);
            if (!keep && this.tickCount < this.livingTickMax() - 5) {
                this.entityData.set(MAX_LIVING_TICK, this.tickCount + 5);
            }
        }
    }

    @Override
    public void onImpact(EntityHitResult res) {
        Entity e = res.getEntity();
        CombatUtils.damageWithFaintAndCrit(this.getOwner(), e, new DynamicDamage.Builder(this, this.getOwner()).hurtResistant(5).magic().noKnockback().element(EnumElement.WATER), CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.MAGIC_ATTACK.asHolder()) * this.damageMultiplier, null);
        this.hitEntities.put(e, this.tickCount);
    }

    @Override
    protected boolean check(Entity e, Predicate<AABB> intersects) {
        if (this.tickCount - this.hitEntities.getOrDefault(e, this.tickCount - 20) <= 19)
            return false;
        return super.check(e, intersects);
    }

    @Override
    public boolean canStartDamage() {
        return true;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(MAX_LIVING_TICK, compound.getInt("MaxTicks"));
        this.entityData.set(TOTAL_ROTATION, compound.getFloat("TotalRotation"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("MaxTicks", this.entityData.get(MAX_LIVING_TICK));
        compound.putFloat("TotalRotation", this.entityData.get(TOTAL_ROTATION));
    }
}
