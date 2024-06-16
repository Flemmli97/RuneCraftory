package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.runecraftory.common.entities.SwimWalkMoveController;
import io.github.flemmli97.runecraftory.common.entities.ai.ChargeAttackGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.pathing.AmphibiousNavigator;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class EntityTortas extends ChargingMonster {

    public static final AnimatedAction BITE = new AnimatedAction(11, 6, "bite");
    public static final AnimatedAction SPIN = new AnimatedAction(51, 0, "spin");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(BITE, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(1, "sleep").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{BITE, SPIN, INTERACT, SLEEP};

    public final ChargeAttackGoal<EntityTortas> ai = new ChargeAttackGoal<>(this);
    protected final WaterBoundPathNavigation waterNavigator;
    protected final GroundPathNavigation groundNavigator;
    private final AnimationHandler<EntityTortas> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityTortas(EntityType<? extends EntityTortas> type, Level world) {
        super(type, world);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.goalSelector.addGoal(2, this.ai);
        this.moveControl = new SwimWalkMoveController(this);
        this.goalSelector.removeGoal(this.swimGoal);
        this.waterNavigator = new AmphibiousNavigator(this, world);
        this.groundNavigator = new GroundPathNavigation(this, world);
        this.wander.setInterval(2);
        this.maxUpStep = 1;
    }

    @Override
    protected void applyAttributes() {
        super.applyAttributes();
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.16);
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.CHARGE) {
            return anim.is(SPIN);
        }
        return type == AnimationType.MELEE && anim.is(BITE);
    }

    @Override
    public int animationCooldown(AnimatedAction anim) {
        if (anim != null && anim.is(SPIN))
            return super.animationCooldown(anim) * 2;
        return super.animationCooldown(anim);
    }

    @Override
    public void setDoJumping(boolean jump) {
        if (this.isInWater())
            super.setDoJumping(jump);
    }

    @Override
    public void travel(Vec3 vec) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.handleNoGravTravel(vec);
        } else {
            super.travel(vec);
        }
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 1;
    }

    @Override
    public AABB calculateAttackAABB(AnimatedAction anim, Vec3 target, double grow) {
        if (anim != null && anim.is(SPIN))
            return this.attackAABB(anim).move(this.getX(), this.getY(), this.getZ());
        return super.calculateAttackAABB(anim, target, grow);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                return;
            if (command == 1)
                this.getAnimationHandler().setAnimation(SPIN);
            else
                this.getAnimationHandler().setAnimation(BITE);
        }
    }

    @Override
    public double ridingSpeedModifier() {
        return 0.4;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.TURTLE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.TURTLE_DEATH;
    }

    @Override
    public float getVoicePitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 0.75f;
    }

    @Override
    public float attackChance(AnimationType type) {
        if (type == AnimationType.MELEE)
            return 0.85f;
        return 1;
    }

    @Override
    public AnimationHandler<EntityTortas> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean adjustRotFromRider(LivingEntity rider) {
        return true;
    }

    @Override
    public float chargingYaw() {
        return this.getYRot();
    }

    @Override
    public boolean handleChargeMovement() {
        Vec3 prevMotion = this.getDeltaMovement();
        if (this.getTarget() != null) {
            Vec3 pos = this.position();
            Vec3 target = this.getTarget().position();
            Vec3 mot = target.subtract(pos.x, this.isInWater() ? pos.y : target.y, pos.z).normalize().scale(0.27);
            this.setDeltaMovement(mot.x, mot.y, mot.z);
            if (!this.isOnGround() && !this.isInWater())
                this.setDeltaMovement(this.getDeltaMovement().add(0, prevMotion.y, 0));
        } else {
            Vec3 look = this.calculateViewVector(this.isInWater() ? this.getXRot() : 0, this.getYRot()).scale(0.27);
            this.setDeltaMovement(look.x, look.y, look.z);
            if (!this.isOnGround() && !this.isInWater())
                this.setDeltaMovement(this.getDeltaMovement().add(0, prevMotion.y, 0));
        }
        return true;
    }

    @Override
    public void doWhileCharge() {
        if (this.tickCount % 4 == 0)
            this.playSound(ModSounds.PLAYER_ATTACK_SWOOSH_LIGHT.get(), 1, (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2f + 1.0f);
        if (this.tickCount % 8 == 0) {
            this.hitEntity.clear();
        }
    }

    //==========Water stuff

    @Override
    public float chargingLength() {
        return 10;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    @Override
    public void updateSwimming() {
        if (!this.level.isClientSide) {
            if (this.isInWater()) {
                this.navigation = this.waterNavigator;
                this.wander.setInterval(2);
                this.setSwimming(true);
            } else {
                this.navigation = this.groundNavigator;
                this.wander.setInterval(100);
                this.setSwimming(false);
            }
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        return this.getBbHeight() * 0.7D;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public MobType getMobType() {
        return MobType.WATER;
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public AnimatedAction getSleepAnimation() {
        return SLEEP;
    }

    @Override
    public Vec3 passengerOffset(Entity passenger) {
        return new Vec3(0, 11 / 16d, -4 / 16d);
    }
}
