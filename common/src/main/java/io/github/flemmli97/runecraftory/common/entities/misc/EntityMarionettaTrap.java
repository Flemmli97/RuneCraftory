package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.api.entity.IAnimated;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EntityMarionettaTrap extends Entity implements OwnableEntity, IAnimated {

    private static final EntityDataAccessor<CompoundTag> CAUGHT_ENTITIES = SynchedEntityData.defineId(EntityMarionettaTrap.class, EntityDataSerializers.COMPOUND_TAG);

    private static final AnimatedAction[] ANIMS = new AnimatedAction[0];

    private final List<LivingEntity> caughtEntities = new ArrayList<>();
    private boolean dirty = true;
    private final AnimationHandler<EntityMarionettaTrap> animationHandler = new AnimationHandler<>(this, ANIMS);
    private int tickLeft = 100;
    private LivingEntity shooter;
    private UUID shooterUUID;
    private float damageMultiplier = 0.7f;

    public EntityMarionettaTrap(EntityType<? extends EntityMarionettaTrap> entityType, Level level) {
        super(entityType, level);
        this.noCulling = true;
    }

    public EntityMarionettaTrap(Level world, LivingEntity shooter) {
        this(ModEntities.TRAP_CHEST.get(), world);
        this.shooter = shooter;
        this.shooterUUID = shooter.getUUID();
        this.setPos(shooter.getX(), shooter.getEyeY(), shooter.getZ());
    }

    public static double horizontalMag(Vec3 vec) {
        return vec.x * vec.x + vec.z * vec.z;
    }

    public void addCaughtEntity(LivingEntity entity) {
        this.caughtEntities.add(entity);
        this.dirty = true;
    }

    public void setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(CAUGHT_ENTITIES, new CompoundTag());
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (key == CAUGHT_ENTITIES) {
            CompoundTag tag = this.entityData.get(CAUGHT_ENTITIES);
            this.readCaughtEntities(tag);
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.onGround) {
            Vec3 motion = this.getDeltaMovement();
            double f = Math.sqrt(horizontalMag(motion));
            this.setYRot(this.updateRotation(this.yRotO, (float) (Mth.atan2(motion.x, motion.z) * 57.29577951308232D)));
            this.setXRot(this.updateRotation(this.xRotO, (float) (Mth.atan2(motion.y, f) * 57.2957763671875D)));
            boolean water = this.isInWater();
            float friction = water ? 0.8F : 0.85F;
            this.setDeltaMovement(motion.scale(friction).subtract(0.0D, 0.05f, 0.0D));
            this.move(MoverType.SELF, this.getDeltaMovement());
        }

        --this.tickLeft;
        this.caughtEntities.forEach(e -> {
            if (e.isAlive()) {
                e.setPos(this.getX(), this.getY() + this.getBbHeight() + 0.05, this.getZ());
                e.hurtMarked = true;
                Platform.INSTANCE.getEntityData(e).ifPresent(data -> {
                    if (!data.isOrthoView())
                        data.setOrthoView(e, true);
                });
            }
        });
        if (!this.level.isClientSide) {
            if (this.dirty) {
                this.entityData.set(CAUGHT_ENTITIES, this.writeCaughtEntities());
                this.dirty = false;
            }
            if (this.tickLeft <= 21 && this.tickLeft >= 9) {
                if (this.getOwner() != null && this.tickLeft % 3 == 0)
                    this.caughtEntities.forEach(e -> CombatUtils.mobAttack(this.getOwner(), e, new CustomDamage.Builder(this, this.getOwner()).hurtResistant(this.tickLeft == 7 ? 10 : 0), CombatUtils.getAttributeValue(this.getOwner(), Attributes.ATTACK_DAMAGE) * this.damageMultiplier));
            }
            if (this.tickLeft <= 0) {
                this.caughtEntities.forEach(e -> Platform.INSTANCE.getEntityData(e).ifPresent(data -> data.setOrthoView(e, false)));
                this.discard();
            }
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.damageMultiplier = compound.getFloat("DamageMultiplier");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putFloat("DamageMultiplier", this.damageMultiplier);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    public int getTickLeft() {
        return this.tickLeft;
    }

    private float updateRotation(float prev, float current) {
        while (current - prev < -180.0F) {
            prev -= 360.0F;
        }

        while (current - prev >= 180.0F) {
            prev += 360.0F;
        }

        return Mth.lerp(0.2F, prev, current);
    }

    @Override
    public UUID getOwnerUUID() {
        return this.shooterUUID;
    }

    @Override
    @Nullable
    public LivingEntity getOwner() {
        if (this.shooter == null || this.shooter.isRemoved()) {
            UUID uuid = this.getOwnerUUID();
            if (uuid != null)
                this.shooter = EntityUtil.findFromUUID(LivingEntity.class, this.level, uuid);
        }
        return this.shooter;
    }

    private CompoundTag writeCaughtEntities() {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        this.caughtEntities.forEach(e -> list.add(IntTag.valueOf(e.getId())));
        tag.put("Caught", list);
        return tag;
    }

    private void readCaughtEntities(CompoundTag tag) {
        ListTag list = tag.getList("Caught", Tag.TAG_INT);
        this.caughtEntities.clear();
        list.forEach(t -> {
            Entity e = this.level.getEntity(((IntTag) t).getAsInt());
            if (e instanceof LivingEntity entity)
                this.caughtEntities.add(entity);
        });
    }

    @Override
    public AnimationHandler<?> getAnimationHandler() {
        return this.animationHandler;
    }
}
