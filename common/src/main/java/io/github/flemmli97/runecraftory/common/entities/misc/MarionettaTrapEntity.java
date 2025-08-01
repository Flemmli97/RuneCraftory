package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.entity.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimatedEntity;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MarionettaTrapEntity extends Entity implements OwnableEntity, AnimatedEntity {

    public static int SWORDS = 7;
    public static int DURATION = 120;
    public static int SPIN_START = 80;
    public static int SPIN_STOP = 40;
    public static int[] ATTACK_TIMES = calculateAttackTimes();

    private static final EntityDataAccessor<CompoundTag> CAUGHT_ENTITIES = SynchedEntityData.defineId(MarionettaTrapEntity.class, EntityDataSerializers.COMPOUND_TAG);

    private static final AnimationDefinitionContainer ANIMS = new AnimationDefinitionContainer(Map.of());

    private final List<LivingEntity> caughtEntities = new ArrayList<>();
    private boolean dirty = true;
    private final AnimationHandler<MarionettaTrapEntity> animationHandler = new AnimationHandler<>(this, ANIMS);
    private int tickLeft = DURATION;
    private LivingEntity shooter;
    private UUID shooterUUID;
    private float damageMultiplier = 0.7f;

    private final boolean[] playSpawnSound = new boolean[SWORDS];
    private int shakeTicks;

    public MarionettaTrapEntity(EntityType<? extends MarionettaTrapEntity> entityType, Level level) {
        super(entityType, level);
        this.noCulling = true;
    }

    public MarionettaTrapEntity(Level level, LivingEntity shooter) {
        this(RuneCraftoryEntities.TRAP_CHEST.get(), level);
        this.shooter = shooter;
        this.shooterUUID = shooter.getUUID();
        this.setPos(shooter.getX(), shooter.getEyeY(), shooter.getZ());
    }

    public static double horizontalMag(Vec3 vec) {
        return vec.x * vec.x + vec.z * vec.z;
    }

    private static int[] calculateAttackTimes() {
        int[] times = new int[SWORDS];
        int time = 27;
        for (int i = 0; i < SWORDS; i++) {
            times[i] = time;
            time -= 3;
        }
        return times;
    }

    public void addCaughtEntity(LivingEntity entity) {
        this.caughtEntities.add(entity);
        this.dirty = true;
    }

    public void setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(CAUGHT_ENTITIES, new CompoundTag());
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
    public boolean ignoreExplosion(Explosion explosion) {
        return true;
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.onGround()) {
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
        this.caughtEntities.forEach(entity -> {
            if (entity.isAlive()) {
                Platform.INSTANCE.getEntityData(entity).setInvis(10);
                entity.setPos(this.getX(), this.getY() + this.getBbHeight() + 0.05, this.getZ());
                EntityData data = Platform.INSTANCE.getEntityData(entity);
                if (!data.thirdPersonView())
                    data.setThirdPersonView(true);
            }
        });
        if (!this.level().isClientSide) {
            if (this.dirty) {
                this.entityData.set(CAUGHT_ENTITIES, this.writeCaughtEntities());
                this.dirty = false;
            }
            if (this.getOwner() != null && this.canAttack()) {
                boolean[] success = new boolean[]{false};
                this.caughtEntities.forEach(e -> {
                    if (CombatUtils.mobAttack(this.getOwner(), e, new DynamicDamage.Builder(this, this.getOwner())
                                    .hurtResistant(this.tickLeft == 9 ? 10 : 0),
                            CombatUtils.getAttributeValue(this.getOwner(), Attributes.ATTACK_DAMAGE) * this.damageMultiplier) && !success[0]) {
                        success[0] = true;
                    }
                });
                if (success[0]) {
                    this.playSound(SoundEvents.PLAYER_ATTACK_CRIT, 1, 1);
                }
            }
            if (this.tickLeft <= 0) {
                this.caughtEntities.forEach(entity -> {
                    Platform.INSTANCE.getEntityData(entity).setInvis(0);
                    Platform.INSTANCE.getEntityData(entity).setThirdPersonView(false);
                });
                this.discard();
            }
        } else {
            --this.shakeTicks;
            if (this.canAttack() && !this.caughtEntities.isEmpty()) {
                this.shakeTicks = 2;
            }
        }
    }

    protected boolean canAttack() {
        for (int i : ATTACK_TIMES) {
            if (this.tickLeft == i)
                return true;
        }
        return false;
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

    public int getTickLeft() {
        return this.tickLeft;
    }

    public float getSpinProgress(float partialTicks) {
        int duration = SPIN_START - SPIN_STOP;
        return 1 - Mth.clamp(((this.getTickLeft() - SPIN_STOP) - partialTicks) / duration, 0, 1);
    }

    public float getAttackProgress(int idx, float partialTicks) {
        int time = ATTACK_TIMES[idx] + 2;
        return 1 - Mth.clamp(((this.getTickLeft() - time) - partialTicks) / 6, 0, 1);
    }

    public float shake(float partialTicks) {
        if (this.shakeTicks <= 0)
            return 0;
        return this.shakeTicks - partialTicks;
    }

    public void playSpawnSound(int idx) {
        if (idx < 0 || idx >= this.playSpawnSound.length)
            return;
        if (!this.playSpawnSound[idx]) {
            this.playSpawnSound[idx] = true;
            if (this.level().isClientSide) {
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ARROW_SHOOT, this.getSoundSource(), 1, 1, false);
            } else {
                this.playSound(SoundEvents.ARROW_SHOOT, 1, 1);
            }
        }
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
                this.shooter = EntityUtils.findFromUUID(LivingEntity.class, this.level(), uuid);
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
            Entity e = this.level().getEntity(((IntTag) t).getAsInt());
            if (e instanceof LivingEntity entity)
                this.caughtEntities.add(entity);
        });
    }

    @Override
    public AnimationHandler<?> getAnimationHandler() {
        return this.animationHandler;
    }
}
