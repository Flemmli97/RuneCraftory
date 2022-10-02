package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.ChargeAttackGoal;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.FollowOwnerGoalMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.RiderAttackTargetGoal;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.StayGoal;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EntityMimic extends ChargingMonster {

    private static final EntityDataAccessor<Boolean> awake = SynchedEntityData.defineId(EntityMimic.class, EntityDataSerializers.BOOLEAN);
    private static final AnimatedAction melee = new AnimatedAction(12, 9, "attack");
    private static final AnimatedAction leap = new AnimatedAction(12, 3, "leap");
    private static final AnimatedAction close = new AnimatedAction(6, 6, "close");
    private static final AnimatedAction[] anims = new AnimatedAction[]{melee, leap, close};
    public ChargeAttackGoal<EntityMimic> attack = new ChargeAttackGoal<>(this);
    protected List<LivingEntity> hitEntity;
    private final AnimationHandler<EntityMimic> animationHandler = new AnimationHandler<>(this, anims)
            .setAnimationChangeCons(a -> {
                if (!leap.checkID(a))
                    this.hitEntity = null;
            });
    private int sleepTick = -1;

    public EntityMimic(EntityType<? extends EntityMimic> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
        this.moveControl = new JumpingMover(this);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.5);
        super.applyAttributes();
    }

    @Override
    public void addGoal() {
        this.targetSelector.addGoal(1, this.targetPlayer);
        this.targetSelector.addGoal(2, this.targetMobs);
        this.targetSelector.addGoal(0, this.hurt);
        this.targetSelector.addGoal(3, new RiderAttackTargetGoal(this, 15));

        this.goalSelector.addGoal(0, this.swimGoal);
        this.goalSelector.addGoal(0, new StayGoal(this));
        this.goalSelector.addGoal(3, new FollowOwnerGoalMonster(this, 1.1, 10, 3));
        this.goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 1.0));
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.MELEE)
            return anim.getID().equals(melee.getID());
        if (type == AnimationType.CHARGE)
            return anim.getID().equals(leap.getID());
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean ret = super.hurt(source, amount);
        if (ret)
            this.setAwake();
        return ret;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 1;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 1)
                this.getAnimationHandler().setAnimation(leap);
            else
                this.getAnimationHandler().setAnimation(melee);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(awake, false);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.getID().equals(leap.getID())) {
            if (anim.canAttack()) {
                Vec3 target = this.getTarget() != null ? this.getTarget().position() : this.getLookAngle();
                Vec3 vec32 = new Vec3(target.x - this.getX(), 0.0, target.z - this.getZ())
                        .normalize().scale(1.15);
                this.setDeltaMovement(vec32.x, 0.25f, vec32.z);
            }
            if (anim.getTick() >= anim.getAttackTime()) {
                if (this.hitEntity == null)
                    this.hitEntity = new ArrayList<>();
                this.mobAttack(anim, null, e -> {
                    if (!this.hitEntity.contains(e)) {
                        this.hitEntity.add(e);
                        this.doHurtTarget(e);
                    }
                });
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    public void setTarget(@Nullable LivingEntity livingEntity) {
        super.setTarget(livingEntity);
        if (livingEntity != null) {
            this.setAwake();
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.level.isClientSide) {
            if (this.getTarget() == null) {
                this.sleepTick--;
            }
            if (this.sleepTick == 0) {
                this.entityData.set(awake, false);
                this.getAnimationHandler().setAnimation(close);
                this.getNavigation().stop();
            }
        }
    }

    public void setAwake() {
        this.entityData.set(awake, true);
        this.sleepTick = 200;
    }

    @Override
    protected float getJumpPower() {
        return 0.48f * this.getBlockJumpFactor();
    }

    @Override
    protected void jumpFromGround() {
        Vec3 vec3 = this.getDeltaMovement();
        this.setDeltaMovement(vec3.x, this.getJumpPower(), vec3.z);
        this.hasImpulse = true;
    }

    public boolean isAwake() {
        return this.entityData.get(awake);
    }

    @Override
    public float attackChance(AnimationType type) {
        return 1;
    }

    @Override
    public AnimationHandler<? extends EntityMimic> getAnimationHandler() {
        return this.animationHandler;
    }

    private int getJumpDelay() {
        return this.random.nextInt(6) + 7;
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.isAwake();
    }

    protected static class JumpingMover extends MoveControl {

        private final EntityMimic mimic;
        private float yRot;
        private int jumpDelay;

        public JumpingMover(EntityMimic mimic) {
            super(mimic);
            this.mimic = mimic;
            this.yRot = 180.0f * mimic.getYRot() / (float) Math.PI;
        }

        @Override
        public void tick() {
            this.mob.yHeadRot = this.mob.getYRot();
            this.mob.yBodyRot = this.mob.getYRot();
            if (this.operation != MoveControl.Operation.MOVE_TO) {
                this.mob.setZza(0.0f);
                return;
            }
            this.operation = MoveControl.Operation.WAIT;
            LivingEntity target = this.mob.getTarget();
            double x = target == null ? this.wantedX : target.getX();
            double z = target == null ? this.wantedZ : target.getZ();
            double d = x - this.mob.getX();
            double e = z - this.mob.getZ();
            float n = (float) (Mth.atan2(e, d) * 57.2957763671875) - 90.0f;
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), n, 90.0f));
            if (this.mob.isOnGround()) {
                this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                if (this.jumpDelay-- <= 0) {
                    this.jumpDelay = this.mimic.getJumpDelay();
                    this.mimic.getJumpControl().jump();
                    this.mimic.playSound(SoundEvents.CHEST_OPEN, this.mimic.getSoundVolume() * 0.5f, 1);
                } else {
                    this.mimic.xxa = 0.0f;
                    this.mimic.zza = 0.0f;
                    this.mob.setSpeed(0.0f);
                }
            } else {
                this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
            }
        }
    }
}
