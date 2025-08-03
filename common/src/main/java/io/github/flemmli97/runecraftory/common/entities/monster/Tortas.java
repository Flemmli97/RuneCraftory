package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MoveToWalkTillClose;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.SetChargeTarget;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.SetWaterPrioritizingWalkTarget;
import io.github.flemmli97.runecraftory.common.entities.ai.control.SwimWalkMoveController;
import io.github.flemmli97.runecraftory.common.entities.ai.pathing.AmphibiousNavigator;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.MoveToAttackTarget;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;

public class Tortas extends ChargingMonster {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String BITE = BUILDER.add("bite", AnimationsBuilder.definition(0.56).marker("attack", 0.32));
    public static final String INTERACT = BUILDER.add("interact", BITE);
    public static final String SPIN = BUILDER.add("spin", AnimationsBuilder.definition(2.5).marker("attack_start", 0));
    public static final String SLEEP = BUILDER.add("sleep", AnimationsBuilder.definition(0).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();
    protected final PathNavigation waterNavigator;
    protected final PathNavigation groundNavigator;
    private final AnimationHandler<Tortas> animationHandler = new AnimationHandler<>(this, ANIMS);

    public Tortas(EntityType<? extends Tortas> type, Level level) {
        super(type, level);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.moveControl = new SwimWalkMoveController(this, 1.2);
        this.waterNavigator = new AmphibiousNavigator(this, level);
        this.groundNavigator = this.navigation;
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.18);
        this.getAttribute(Attributes.STEP_HEIGHT).setBaseValue(Attributes.STEP_HEIGHT.value().getDefaultValue() + 1);
        super.applyAttributes();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<ChargingMonster>create()
                .start(BITE).play(MonsterBehaviourUtils.requireInRangePlay())
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(new MoveToAttackTarget<>())
                .end(2)
                .start(BITE).play(MonsterBehaviourUtils.requireInRangePlay())
                .condition(MonsterBehaviourUtils.ifCloserThan(3))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(new MoveToAttackTarget<>())
                .end(5)
                .start(SPIN).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetChargeTarget<>())
                .end(3)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseMonster>builder()
                .add(2, new SetWalkTargetToAttackTarget<>(), new MoveToWalkTillClose<>())
                .add(1, new SetRandomWalkTarget<>(), new MoveToWalkTillClose<>())
                .add(4, new Idle<>()).build();
    }

    @Override
    protected ExtendedBehaviour<? extends BaseMonster> getWanderBehaviour() {
        return new SetWaterPrioritizingWalkTarget<>();
    }

    @Override
    protected int wanderChance() {
        return this.isSwimming() ? 5 : 100;
    }

    @Override
    protected boolean canFloatInWater() {
        return false;
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double width = this.getBbWidth() * 1.2;
        double length = this.getBbWidth() * 1.4;
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public int animationCooldown(String anim) {
        if (anim != null && anim.equals(SPIN)) {
            int diffAdd = this.difficultyCooldown();
            return this.getRandom().nextInt(50) + 30 + diffAdd;
        }
        return super.animationCooldown(anim);
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimationState anim, Vec3 target, double grow) {
        if (anim != null && anim.is(SPIN)) {
            return new OrientedBoundingBox(OrientedBoundingBox.originAABB(this).inflate(0.2), this.getYRot(), 0, this.position());
        }
        return super.calculateAttackAABB(anim, target, grow);
    }

    @Override
    public AnimationHandler<Tortas> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    protected boolean isChargingAnim(String anim) {
        return anim.equals(SPIN);
    }

    @Override
    public double chargingSpeed() {
        return 0.3f;
    }

    @Override
    public boolean handleChargeMovement(AnimationState anim) {
        Vec3 prevMotion = this.getDeltaMovement();
        if (this.getTarget() != null) {
            Vec3 pos = this.position();
            Vec3 target = this.getTarget().position();
            Vec3 mot = target.subtract(pos.x, this.isInWater() ? pos.y : target.y, pos.z).normalize().scale(0.27);
            this.setDeltaMovement(mot.x, mot.y, mot.z);
            if (!this.onGround() && !this.isInWater())
                this.setDeltaMovement(this.getDeltaMovement().add(0, prevMotion.y, 0));
        } else {
            Vec3 look = this.calculateViewVector(this.isInWater() ? this.getXRot() : 0, this.getYRot()).scale(0.27);
            this.setDeltaMovement(look.x, look.y, look.z);
            if (!this.onGround() && !this.isInWater())
                this.setDeltaMovement(this.getDeltaMovement().add(0, prevMotion.y, 0));
        }
        return true;
    }

    @Override
    public void doWhileCharge() {
        if (this.tickCount % 4 == 0)
            this.playSound(RuneCraftorySounds.PLAYER_ATTACK_SWOOSH_LIGHT.get(), 1, (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2f + 1.0f);
        if (this.tickCount % 8 == 0) {
            this.hitEntity.clear();
        }
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
    public void setDoJumping(boolean jump) {
        if (this.isInWater())
            super.setDoJumping(jump);
    }

    @Override
    public void travel(Vec3 vec) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.handleFreeTravel(vec);
        } else {
            super.travel(vec);
        }
    }

    @Override
    public double ridingSpeedModifier() {
        return 0.8;
    }

    @Override
    protected Vec3 directionToLookAt() {
        if (this.getAnimationHandler().isCurrent(SPIN)) {
            return this.getDeltaMovement();
        }
        return super.directionToLookAt();
    }

    @Override
    public boolean adjustRotFromRider(LivingEntity rider) {
        return true;
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
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    @Override
    public void updateSwimming() {
        if (!this.level().isClientSide) {
            if (this.isInWater()) {
                this.navigation = this.waterNavigator;
                this.setSwimming(true);
            } else {
                this.navigation = this.groundNavigator;
                this.setSwimming(false);
            }
        }
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public String getInteractAnimation() {
        return INTERACT;
    }

    @Override
    public String getSleepAnimation() {
        return SLEEP;
    }
}
