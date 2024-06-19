package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.LeapingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.RiderAttackTargetGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.StayGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveAwayRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.RandomMoveAroundRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.random.WeightedEntry;
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

public class EntityMimic extends LeapingMonster {

    private static final EntityDataAccessor<Boolean> AWAKE = SynchedEntityData.defineId(EntityMimic.class, EntityDataSerializers.BOOLEAN);

    private static final AnimatedAction MELEE = new AnimatedAction(12, 9, "attack");
    private static final AnimatedAction LEAP = new AnimatedAction(12, 3, "leap");
    private static final AnimatedAction CLOSE = new AnimatedAction(6, 6, "close");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(MELEE, "interact");
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{MELEE, LEAP, CLOSE, INTERACT};

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityMimic>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(MELEE, e -> 0.8f), 2),
            WeightedEntry.wrap(new GoalAttackAction<EntityMimic>(LEAP)
                    .cooldown(e -> e.animationCooldown(LEAP))
                    .prepare(() -> new WrappedRunner<>(new MoveAwayRunner<>(3, 1, 3))), 2)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityMimic>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 1.5)), 1),
            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(16, 5)), 2)
    );

    public final AnimatedAttackGoal<EntityMimic> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityMimic> animationHandler = new AnimationHandler<>(this, ANIMS);
    private int sleepTick = -1;
    private boolean sleeping;

    public EntityMimic(EntityType<? extends EntityMimic> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
        this.moveControl = new JumpingMover(this);
    }

    @Override
    protected void applyAttributes() {
        super.applyAttributes();
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.5);
    }

    @Override
    public void addGoal() {
        this.targetSelector.addGoal(1, this.targetPlayer);
        this.targetSelector.addGoal(2, this.targetMobs);
        this.targetSelector.addGoal(0, this.hurt);
        this.targetSelector.addGoal(3, new RiderAttackTargetGoal(this, 15));

        this.goalSelector.addGoal(0, this.swimGoal);
        this.goalSelector.addGoal(0, new StayGoal<>(this, StayGoal.CANSTAYMONSTER));
        this.goalSelector.addGoal(3, this.followOwnerGoal);
        this.goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 1.0));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean ret = super.hurt(source, amount);
        if (ret && !this.sleeping)
            this.setAwake();
        return ret;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 1.2;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                return;
            if (command == 1)
                this.getAnimationHandler().setAnimation(LEAP);
            else
                this.getAnimationHandler().setAnimation(MELEE);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(AWAKE, false);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(LEAP)) {
            if (anim.getTick() == 1 && this.getTarget() != null) {
                this.targetPosition = this.getTarget().position();
            }
            if (anim.canAttack()) {
                Vec3 vec32 = this.getLeapVec(this.getTarget() == null ? this.targetPosition : this.getTarget().position());
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
    protected boolean isLeapingAnim(AnimatedAction anim) {
        return anim.is(LEAP);
    }

    @Override
    public Vec3 getLeapVec(@Nullable Vec3 target) {
        return super.getLeapVec(target).scale(1.25);
    }

    @Override
    public double leapHeightMotion() {
        return 0.3;
    }

    @Override
    public void setTarget(@Nullable LivingEntity livingEntity) {
        super.setTarget(livingEntity);
        if (livingEntity != null && !this.sleeping) {
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
                this.entityData.set(AWAKE, false);
                this.getAnimationHandler().setAnimation(CLOSE);
                this.getNavigation().stop();
            }
        }
    }

    public void setAwake() {
        this.entityData.set(AWAKE, true);
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
        return this.entityData.get(AWAKE);
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

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public void onSleeping(boolean sleeping) {
        if (sleeping) {
            this.sleeping = true;
            if (this.isAwake()) {
                this.entityData.set(AWAKE, false);
                this.getAnimationHandler().setAnimation(CLOSE);
                this.getNavigation().stop();
            }
        } else
            this.sleeping = false;
    }

    @Override
    public boolean hasSleepingAnimation() {
        return true;
    }

    protected static class JumpingMover extends MoveControl {

        private final EntityMimic mimic;
        private final float yRot;
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
            this.mimic.setAwake();
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
