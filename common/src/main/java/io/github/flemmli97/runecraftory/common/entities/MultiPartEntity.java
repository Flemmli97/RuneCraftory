package io.github.flemmli97.runecraftory.common.entities;

import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
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

    private Entity parent;
    private EntityDimensions dimensions = EntityDimensions.fixed(1, 1);
    private boolean addedToLevel, isHead;

    public MultiPartEntity(EntityType<MultiPartEntity> multipartType, Level level) {
        super(multipartType, level);
    }

    public MultiPartEntity(Entity parent, float width, float height) {
        super(ModEntities.MULTIPART.get(), parent.level);
        this.setSize(width, height);
        this.setParent(parent);
    }

    public void setParent(Entity parent) {
        this.entityData.set(PARENT_UUID, Optional.of(parent.getUUID()));
        this.parent = parent;
    }

    public MultiPartEntity setHeadPart() {
        this.isHead = true;
        return this;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(PARENT_UUID, Optional.empty());
        this.entityData.define(SIZE_X, 0f);
        this.entityData.define(SIZE_Y, 0f);
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return this.dimensions;
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
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    @Override
    public void tick() {
        if (!this.level.isClientSide)
            if (this.getOwner() == null || !this.getOwner().isAlive())
                this.remove(RemovalReason.KILLED);
        super.tick();
    }

    @Override
    public void baseTick() {
        super.baseTick();
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
        return source == DamageSource.FALL || source == DamageSource.DROWN || (!this.isHead && source == DamageSource.IN_WALL) || super.isInvulnerableTo(source);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (SIZE_Y.equals(key)) {
            this.setSize(this.entityData.get(SIZE_X), this.entityData.get(SIZE_Y));
        }
    }

    public MultiPartEntity setSizeX(float x) {
        this.setSize(x, this.dimensions.height);
        return this;
    }

    public MultiPartEntity setSizeY(float y) {
        this.setSize(this.dimensions.width, y);
        return this;
    }

    public MultiPartEntity setSize(float x, float y) {
        if (!this.level.isClientSide) {
            this.entityData.set(SIZE_X, x);
            this.entityData.set(SIZE_Y, y);
        }
        this.dimensions = EntityDimensions.fixed(x, y);
        this.refreshDimensions();
        return this;
    }

    @Override
    public boolean isPickable() {
        return this.getOwner() != null;
    }

    @Override
    public void setLevelCallback(EntityInLevelCallback entityInLevelCallback) {
        super.setLevelCallback(entityInLevelCallback);
        this.addedToLevel = true;
    }

    public boolean isAddedToLevel() {
        return this.addedToLevel;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    public void updatePositionTo(double x, double y, double z, boolean simple) {
        if (this.getOwner() != null && !this.isAddedToLevel()) {
            this.setPos(x, y, z);
            this.level.addFreshEntity(this);
        }
        Vec3 old = this.position();
        this.setOldPosAndRot();
        if (simple)
            this.setPos(x, y, z);
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

    @Nullable
    @Override
    public UUID getOwnerUUID() {
        return this.entityData.get(PARENT_UUID).orElse(null);
    }

    @Nullable
    @Override
    public Entity getOwner() {
        if (this.parent != null && this.parent.isAlive())
            return this.parent;
        this.entityData.get(PARENT_UUID).ifPresent(uuid -> this.parent = EntityUtil.findFromUUID(Entity.class, this.level, uuid));
        return this.parent;
    }
}
