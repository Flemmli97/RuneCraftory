package io.github.flemmli97.runecraftory.common.entities;

import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.tenshilib.common.entity.EntityUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityInLevelCallback;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class MultiPartEntity extends Entity implements OwnableEntity {

    private static final EntityDataAccessor<Optional<UUID>> PARENT_UUID = SynchedEntityData.defineId(MultiPartEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Float> SIZE_X = SynchedEntityData.defineId(MultiPartEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> SIZE_Y = SynchedEntityData.defineId(MultiPartEntity.class, EntityDataSerializers.FLOAT);

    private LivingEntity parent;
    private boolean addedToLevel, isHead;

    private MultipartPosition relativePosition = MultipartPosition.DEFAULT;

    private int lerpSteps;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    private double lerpYRot;
    private double lerpXRot;

    public MultiPartEntity(EntityType<MultiPartEntity> multipartType, Level level) {
        super(multipartType, level);
    }

    public MultiPartEntity(LivingEntity parent, float width, float height) {
        super(RuneCraftoryEntities.MULTIPART.get(), parent.level());
        this.setSize(width, height);
        this.setParent(parent);
    }

    public void setParent(LivingEntity parent) {
        this.entityData.set(PARENT_UUID, Optional.of(parent.getUUID()));
        this.parent = parent;
    }

    public MultiPartEntity setHeadPart() {
        this.isHead = true;
        return this;
    }

    public MultiPartEntity setSizeX(float x) {
        this.setSize(x, this.entityData.get(SIZE_Y));
        return this;
    }

    public MultiPartEntity setSizeY(float y) {
        this.setSize(this.entityData.get(SIZE_X), y);
        return this;
    }

    public MultiPartEntity setSize(float x, float y) {
        if (!this.level().isClientSide) {
            this.entityData.set(SIZE_X, x);
            this.entityData.set(SIZE_Y, y);
        }
        this.refreshDimensions();
        return this;
    }

    public MultiPartEntity updatePosition(Vec3 relativePosition) {
        return this.updatePosition(new MultipartPosition(relativePosition));
    }

    public MultiPartEntity updatePosition(MultipartPosition relativePosition) {
        this.relativePosition = relativePosition;
        return this;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(PARENT_UUID, Optional.empty());
        builder.define(SIZE_X, 0f);
        builder.define(SIZE_Y, 0f);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (SIZE_Y.equals(key)) {
            this.setSize(this.entityData.get(SIZE_X), this.entityData.get(SIZE_Y));
        }
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return this.getDefaultDimensions().scale(this.getOwner() != null ? this.getOwner().getScale() : 1);
    }

    protected EntityDimensions getDefaultDimensions() {
        float ageScale = this.getOwner() != null ? this.getOwner().getAgeScale() : 1;
        return EntityDimensions.scalable(ageScale * this.entityData.get(SIZE_X), ageScale * this.entityData.get(SIZE_Y));
    }

    @Override
    public Component getName() {
        return this.parent.getName();
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide) {
            if (this.getOwner() == null || !this.getOwner().isAlive()) {
                this.remove(RemovalReason.KILLED);
                return;
            }
        }
        super.tick();
    }

    @Override
    public void baseTick() {
        this.level().getProfiler().push("entityBaseTick");
        this.ejectPassengers();
        this.xRotO = this.getXRot();
        this.yRotO = this.getYRot();
        this.clearFire();
        if (this.isInLava()) {
            this.lavaHurt();
            this.fallDistance *= 0.5F;
        }
        this.checkBelowWorld();
        if (!this.level().isClientSide) {
            Vec3 newPos = this.getOwner().position().add(this.relativePosition.getPosition(this.getOwner()));
            this.moveTo(newPos.x(), newPos.y(), newPos.z(), this.relativePosition.noPhysics());
        }
        if (this.lerpSteps > 0) {
            this.lerpPositionAndRotationStep(this.lerpSteps, this.lerpX, this.lerpY, this.lerpZ, this.lerpYRot, this.lerpXRot);
            this.lerpSteps--;
        }
        this.level().getProfiler().pop();
    }

    @Override
    public void lerpTo(double x, double y, double z, float yRot, float xRot, int steps) {
        this.lerpX = x;
        this.lerpY = y;
        this.lerpZ = z;
        this.lerpYRot = yRot;
        this.lerpXRot = xRot;
        this.lerpSteps = steps;
    }

    @Override
    public double lerpTargetX() {
        return this.lerpSteps > 0 ? this.lerpX : this.getX();
    }

    @Override
    public double lerpTargetY() {
        return this.lerpSteps > 0 ? this.lerpY : this.getY();
    }

    @Override
    public double lerpTargetZ() {
        return this.lerpSteps > 0 ? this.lerpZ : this.getZ();
    }

    @Override
    public float lerpTargetXRot() {
        return this.lerpSteps > 0 ? (float) this.lerpXRot : this.getXRot();
    }

    @Override
    public float lerpTargetYRot() {
        return this.lerpSteps > 0 ? (float) this.lerpYRot : this.getYRot();
    }

    private void moveTo(double x, double y, double z, boolean simple) {
        if (this.getOwner() != null && !this.isEntityAddedToLevel()) {
            this.setPos(x, y, z);
            this.level().addFreshEntity(this);
        }
        Vec3 old = this.position();
        this.setOldPosAndRot();
        if (simple) {
            this.setPos(x, y, z);
        }
        else {
            this.setOnGround(true);
            double vy = y - old.y;
            if (vy >= 0 && vy < 1.5) {
                if (vy <= 1)
                    vy = -0.08;
                else
                    vy = 0;
            }
            this.move(MoverType.SELF, new Vec3(x - old.x, vy, z - old.z));
        }
    }

    /**
     * Spawns this part entity if not spawned. Call this in parent tick
     */
    public void parentTick() {
        if (this.getOwner() != null && !this.getOwner().level().isClientSide && !this.isEntityAddedToLevel()) {
            this.setPos(this.getOwner().position());
            this.level().addFreshEntity(this);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return this.getOwner() != null && this.getOwner().hurt(source, amount);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        return this.getOwner() != null ? this.getOwner().interact(player, hand) : InteractionResult.PASS;
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if (this.getOwner() != null && this.getOwner().isInvulnerableTo(source))
            return true;
        return source.is(DamageTypeTags.IS_FALL) || source.is(DamageTypeTags.IS_DROWNING) || (!this.isHead && source.is(DamageTypes.IN_WALL)) || super.isInvulnerableTo(source);
    }

    @Override
    public boolean isOnFire() {
        return this.getOwner() != null && this.getOwner().isOnFire();
    }

    @Override
    public int getTicksFrozen() {
        return this.getOwner() != null ? this.getOwner().getTicksFrozen() : 0;
    }

    @Override
    public boolean isPickable() {
        return this.getOwner() != null;
    }

    @Override
    public ItemStack getPickResult() {
        return this.getOwner() != null ? this.getOwner().getPickResult() : null;
    }

    @Override
    public void setLevelCallback(EntityInLevelCallback entityInLevelCallback) {
        super.setLevelCallback(entityInLevelCallback);
        this.addedToLevel = true;
    }

    public boolean isEntityAddedToLevel() {
        return this.addedToLevel;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Nullable
    @Override
    public UUID getOwnerUUID() {
        return this.entityData.get(PARENT_UUID).orElse(null);
    }

    @Override
    public LivingEntity getOwner() {
        if (this.parent != null && this.parent.isAlive())
            return this.parent;
        this.entityData.get(PARENT_UUID).ifPresent(uuid -> this.parent = EntityUtils.findFromUUID(LivingEntity.class, this.level(), uuid));
        return this.parent;
    }

    interface PositionUpdater {

        PositionUpdater FROM_YROT = (relative, parent) -> relative.yRot(-parent.yBodyRot * Mth.DEG_TO_RAD);

        Vec3 from(Vec3 relative, LivingEntity parent);
    }

    public record MultipartPosition(Vec3 relative, PositionUpdater updater, boolean noPhysics) {

        public static final MultipartPosition DEFAULT = new MultipartPosition(Vec3.ZERO);

        public MultipartPosition(Vec3 relative) {
            this(relative, PositionUpdater.FROM_YROT, true);
        }

        public MultipartPosition(Vec3 relative, boolean noPhysics) {
            this(relative, PositionUpdater.FROM_YROT, noPhysics);
        }

        public Vec3 getPosition(LivingEntity parent) {
            return this.updater().from(this.relative(), parent);
        }
    }
}
