package io.github.flemmli97.runecraftory.common.entities.monster.boss.rafflesia;

import io.github.flemmli97.runecraftory.common.entities.ai.boss.RafflesiaPartAttackGoal;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.IAnimated;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public abstract class EntityRafflesiaPart extends Mob implements IAnimated, OwnableEntity {

    private static final EntityDataAccessor<Optional<UUID>> PARENT = SynchedEntityData.defineId(EntityRafflesiaPart.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Direction> SPAWN_DIRECTION = SynchedEntityData.defineId(EntityRafflesia.class, EntityDataSerializers.DIRECTION);

    public static final AnimatedAction HORSE_TAIL_ACTION = new AnimatedAction(1.28, 0.56, "horse_tail_action");
    public static final AnimatedAction FLOWER_ACTION = new AnimatedAction(1, 0.44, "flower_action");
    public static final AnimatedAction PITCHER_ACTION = new AnimatedAction(0.96, 0.4, "pitcher_action");

    public final RafflesiaPartAttackGoal attack = new RafflesiaPartAttackGoal(this);

    private EntityRafflesia parent;

    public EntityRafflesiaPart(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
        if (!level.isClientSide)
            this.goalSelector.addGoal(1, this.attack);
    }

    public EntityRafflesiaPart(EntityType<? extends Mob> entityType, Level level, EntityRafflesia parent) {
        this(entityType, level);
        this.parent = parent;
        this.entityData.set(PARENT, Optional.of(this.parent.getUUID()));
        this.getAttributes().load(parent.getAttributes().save());
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(parent.getAttributeValue(Attributes.MAX_HEALTH));
    }

    public static AttributeSupplier.Builder createAttributes(Collection<? extends RegistryEntrySupplier<Attribute>> atts) {
        AttributeSupplier.Builder map = Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0)
                .add(Attributes.FOLLOW_RANGE, 32);
        if (atts != null)
            for (RegistryEntrySupplier<Attribute> att : atts)
                map.add(att.get());
        return map;
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new GroundPathNavigation(this, level) {
            @Nullable
            @Override
            protected Path createPath(Set<BlockPos> targets, int regionOffset, boolean offsetUpward, int accuracy, float followRange) {
                return null;
            }
        };
    }

    @Override
    public void baseTick() {
        if (!this.level.isClientSide && !this.firstTick) {
            if (this.getOwner() != null) {
                if (this.getOwner().isDeadOrDying())
                    this.discard();
                else {
                    if (this.getTarget() != this.getOwner().getTarget()) {
                        this.setTarget(this.getOwner().getTarget());
                    }
                    this.setPos(this.getOwner().position().add(EntityRafflesia.rotateVec(this.entityData.get(SPAWN_DIRECTION), this.offset())));
                }
            } else if (this.getOwnerUUID() == null) {
                this.discard();
            }
        }
        super.baseTick();
        this.getAnimationHandler().tick();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(PARENT, Optional.empty());
        this.entityData.define(SPAWN_DIRECTION, Direction.NORTH);
    }


    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.entityData.get(PARENT).ifPresent(uuid -> compound.putUUID("Parent", uuid));
        compound.putInt("SpawnDirection", this.entityData.get(SPAWN_DIRECTION).ordinal());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.hasUUID("Parent"))
            this.entityData.set(PARENT, Optional.of(compound.getUUID("Parent")));
        try {
            this.entityData.set(SPAWN_DIRECTION, Direction.values()[compound.getInt("SpawnDirection")]);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    public void setSpawnDirection(Direction direction) {
        this.entityData.set(SPAWN_DIRECTION, direction);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Nullable
    @Override
    public UUID getOwnerUUID() {
        return this.entityData.get(PARENT).orElse(null);
    }

    @Nullable
    @Override
    public EntityRafflesia getOwner() {
        UUID uuid = this.getOwnerUUID();
        if (uuid != null) {
            if (this.parent == null || !this.parent.isAlive()) {
                this.parent = EntityUtil.findFromUUID(EntityRafflesia.class, this.level, uuid);
            }
        } else
            this.parent = null;
        return this.parent;
    }

    public abstract Vec3 offset();

    public abstract AnimatedAction attackAnim();
}
