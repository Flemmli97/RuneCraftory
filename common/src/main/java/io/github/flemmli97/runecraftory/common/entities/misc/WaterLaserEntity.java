package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.registry.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.runecraftory.common.utils.MathsHelper;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.UUID;
import java.util.function.Predicate;

public class WaterLaserEntity extends BaseBeam {

    private static final EntityDataAccessor<Float> RANGE = SynchedEntityData.defineId(WaterLaserEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> MAX_LIVING_TICK = SynchedEntityData.defineId(WaterLaserEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Float> YAW_OFFSET = SynchedEntityData.defineId(WaterLaserEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Vector3f> OFFSET = SynchedEntityData.defineId(WaterLaserEntity.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Boolean> ROTATION_FROM_OFFSET = SynchedEntityData.defineId(WaterLaserEntity.class, EntityDataSerializers.BOOLEAN);

    private final Object2IntMap<Entity> hitEntities = new Object2IntOpenHashMap<>();
    private float accumulatedRot;
    private UUID other;

    public WaterLaserEntity(EntityType<? extends WaterLaserEntity> type, Level level) {
        super(type, level);
    }

    public WaterLaserEntity(Level level, LivingEntity shooter) {
        super(RuneCraftoryEntities.WATER_LASER.get(), level, shooter);
    }

    public WaterLaserEntity(Level level, LivingEntity shooter, Vector3f offset) {
        super(RuneCraftoryEntities.WATER_LASER.get(), level, shooter);
        this.entityData.set(OFFSET, offset);
        this.updateYawPitch();
    }

    public WaterLaserEntity setMaxTicks(int ticks) {
        this.entityData.set(MAX_LIVING_TICK, ticks);
        return this;
    }

    public void setYawOffset(float offset) {
        this.entityData.set(YAW_OFFSET, offset);
        this.updateYawPitch();
    }

    public void setTwinId(UUID other) {
        this.other = other;
    }

    public void setRange(float range) {
        this.entityData.set(RANGE, range);
    }

    public void setRotationFromOffset() {
        this.entityData.set(ROTATION_FROM_OFFSET, true);
        this.updateYawPitch();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(MAX_LIVING_TICK, 20);
        builder.define(YAW_OFFSET, 0f);
        builder.define(OFFSET, new Vector3f());
        builder.define(RANGE, 10f);
        builder.define(ROTATION_FROM_OFFSET, false);
    }

    @Override
    public float getRange() {
        return this.entityData.get(RANGE);
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
    public void updateYawPitch() {
        if (this.getHitVecFromShooter() && this.getOwner() != null) {
            this.setupRotationAndPosition(this.getOwner(), this.getOwner());
        }
    }

    public void setupRotationAndPosition(Entity positionEntity, Entity from) {
        float yRot = this.getYRot();
        float xRot = this.getXRot();
        float[] yxRot;
        Vector3f offset = this.entityData.get(OFFSET)
                .rotateY(-(from.getYRot() + this.entityData.get(YAW_OFFSET)) * Mth.DEG_TO_RAD, new Vector3f());
        if (this.entityData.get(ROTATION_FROM_OFFSET)) {
            float pitch = from.getXRot();
            Vec3 look = Vec3.directionFromRotation(pitch, from.getYRot()).scale(offset.length());
            Vec3 up = Vec3.directionFromRotation(pitch - 90, from.getYRot());
            offset = new Vector3f((float) look.x(), (float) look.y(), (float) look.z())
                    .rotateAxis(this.entityData.get(YAW_OFFSET) * Mth.DEG_TO_RAD, (float) up.x(), (float) up.y(), (float) up.z(), new Vector3f());
            yxRot = MathsHelper.YXRotFrom(offset.x(), offset.y(), offset.z());
        } else {
            yxRot = new float[]{from.getYRot() + this.entityData.get(YAW_OFFSET), from.getXRot()};
        }
        this.setXRot(yxRot[1]);
        this.setYRot(yxRot[0]);
        this.setPos(positionEntity.getX() + offset.x(), positionEntity.getY() + positionEntity.getEyeHeight() - 0.1 + offset.y(), positionEntity.getZ() + offset.z());
        this.accumulatedRot += Math.abs(this.getYRot() - yRot) + Math.abs(this.getXRot() - xRot);
    }

    @Override
    public boolean getHitVecFromShooter() {
        return this.getOwner() instanceof Player;
    }

    @Override
    public void tick() {
        if (this.accumulatedRot > 15) {
            this.hitEntities.clear();
            this.accumulatedRot = 0;
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
        int invul_time = 5;
        if (e instanceof LivingEntity living) {
            if (living.getLastDamageSource() != null && living.getLastDamageSource().getDirectEntity() != null &&
                    living.getLastDamageSource().getDirectEntity().getUUID().equals(this.other)) {
                invul_time = 0;
            }
        }
        CombatUtils.damageWithFaintAndCrit(this.getOwner(), e, new DynamicDamage.Builder(this, this.getOwner()).hurtResistant(invul_time).magic().noKnockback().element(ItemElement.WATER), CombatUtils.getAttributeValue(this.getOwner(), RuneCraftoryAttributes.MAGIC_ATTACK.asHolder()) * this.damageMultiplier, null);
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
        this.entityData.set(RANGE, compound.getFloat("Range"));
        this.entityData.set(MAX_LIVING_TICK, compound.getInt("MaxTicks"));
        this.entityData.set(YAW_OFFSET, compound.getFloat("YawOffset"));
        this.entityData.set(OFFSET, new Vector3f(compound.getFloat("OffsetX"),
                compound.getFloat("OffsetY"),
                compound.getFloat("OffsetZ")));
        this.entityData.set(ROTATION_FROM_OFFSET, compound.getBoolean("RotationFromOffset"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("Range", this.entityData.get(RANGE));
        compound.putInt("MaxTicks", this.entityData.get(MAX_LIVING_TICK));
        compound.putFloat("YawOffset", this.entityData.get(YAW_OFFSET));
        Vector3f offset = this.entityData.get(OFFSET);
        compound.putFloat("OffsetX", offset.x());
        compound.putFloat("OffsetY", offset.y());
        compound.putFloat("OffsetZ", offset.z());
        compound.putBoolean("RotationFromOffset", this.entityData.get(ROTATION_FROM_OFFSET));
    }
}
