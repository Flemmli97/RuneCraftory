package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.registry.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import io.github.flemmli97.tenshilib.loader.registry.AttachmentRegister;
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

public class SwipingWaterLaserEntity extends BaseBeam {

    private static final EntityDataAccessor<Integer> MAX_LIVING_TICK = SynchedEntityData.defineId(SwipingWaterLaserEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> TOTAL_ROTATION = SynchedEntityData.defineId(SwipingWaterLaserEntity.class, EntityDataSerializers.FLOAT);

    private final Object2IntMap<Entity> hitEntities = new Object2IntOpenHashMap<>();

    public SwipingWaterLaserEntity(EntityType<? extends SwipingWaterLaserEntity> type, Level level) {
        super(type, level);
    }

    public SwipingWaterLaserEntity(Level level, LivingEntity shooter, float rotationAngle) {
        super(RuneCraftoryEntities.SWIPING_WATER_LASER.get(), level, shooter);
        this.entityData.set(TOTAL_ROTATION, rotationAngle);
    }

    public SwipingWaterLaserEntity setMaxTicks(int ticks) {
        this.entityData.set(MAX_LIVING_TICK, ticks);
        return this;
    }

    public void setRotationToDirWithOffset(double dirX, double dirY, double dirZ, float acc, float yawOffset) {
        super.setRotationToDir(dirX, dirY, dirZ, acc);
        this.setYRot(this.getYRot() + yawOffset);
        this.updateYawPitch();
        this.xRotO = this.getXRot();
        this.yRotO = this.getYRot();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(MAX_LIVING_TICK, 20);
        builder.define(TOTAL_ROTATION, 0f);
    }

    @Override
    public float getRange() {
        return 12;
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
    public void updateHitDetectBox() {
        double dist = this.hitVec != null ? this.hitVec.subtract(this.position()).length() : 0;
        double diff = Mth.sin((this.getYRot() - this.yRotO) * Mth.DEG_TO_RAD) * this.getRange();
        double width = this.radius() * 2;
        double height = 1;
        this.hitObb = new OrientedBoundingBox(new AABB(-width * 0.5 + diff, -width * 0.5, 0, width * 0.5, height * 0.5, dist + 0.5),
                this.getYRot(), -this.getXRot(), this.position());
    }

    @Override
    public void tick() {
        float amount = this.entityData.get(TOTAL_ROTATION) / this.livingTickMax();
        if (amount != 0) {
            this.hit = null;
        }
        super.tick();
        if (amount != 0) {
            this.setYRot(Mth.wrapDegrees(this.getYRot() + amount));
        }
        if (this.getOwner() instanceof ServerPlayer player) {
            PlayerData data = RunecraftoryAttachments.PLAYER_DATA.get().get(player);
            AnimationState action = data.getWeaponHandler().getAnimation();
            boolean keep = action != null && action.is(PlayerModelAnimations.WATER_LASER_ONE, PlayerModelAnimations.WATER_LASER_TWO, PlayerModelAnimations.WATER_LASER_THREE);
            if (!keep && this.tickCount < this.livingTickMax() - 5) {
                this.entityData.set(MAX_LIVING_TICK, this.tickCount + 5);
            }
        }
    }

    // Disable rotation update from packet as client already rotates it
    @Override
    public void lerpTo(double x, double y, double z, float yRot, float xRot, int steps) {
    }

    @Override
    public void onImpact(EntityHitResult res) {
        Entity e = res.getEntity();
        CombatUtils.damageWithFaintAndCrit(this.getOwner(), e, new DynamicDamage.Builder(this, this.getOwner()).hurtResistant(5).magic().noKnockback().element(ItemElement.WATER), CombatUtils.getAttributeValue(this.getOwner(), RuneCraftoryAttributes.MAGIC_ATTACK.asHolder()) * this.damageMultiplier, null);
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
