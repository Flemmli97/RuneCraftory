package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.StrafeTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.navigation.SmoothWallClimberNavigation;

public class Spider extends BaseMonster {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String MELEE = BUILDER.add("attack", AnimationsBuilder.definition(0.6).marker("attack", 0.48));
    public static final String INTERACT = BUILDER.add("interact", MELEE);
    public static final String WEBSHOT = BUILDER.add("webshot", AnimationsBuilder.definition(0.68).marker("attack", 0.36));
    public static final String STILL = BUILDER.add("still", AnimationsBuilder.definition(0).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();
    public static final int CLIMB_MAX = 9;
    private static final EntityDataAccessor<Boolean> CLIMBING_SYNC = SynchedEntityData.defineId(Spider.class, EntityDataSerializers.BOOLEAN);
    private final AnimationHandler<Spider> animationHandler = new AnimationHandler<>(this, ANIMS);
    public int climbingTicker = -1;

    public Spider(EntityType<? extends Spider> type, Level level) {
        super(type, level);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new SmoothWallClimberNavigation(this, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CLIMBING_SYNC, false);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.27);
        super.applyAttributes();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<BaseMonster>create()
                .start(MELEE).play(MonsterBehaviourUtils.requireInRangePlay())
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(MonsterBehaviourUtils.moveAttack())
                .end(4)
                .start(WEBSHOT).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new StrafeTarget<BaseMonster>().strafeDistance(8))
                .end(7)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseMonster>builder()
                .add(5, new SetWalkTargetToAttackTarget<>(), MonsterBehaviourUtils.moveAttack())
                .add(3, new SetRandomWalkTarget<>(), MonsterBehaviourUtils.moveAttack())
                .add(3, new Idle<>()).build();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            this.setClimbing(this.horizontalCollision);
        }
        if (this.isClimbing() && this.isAlive() && !this.playDeath()) {
            this.climbingTicker = Math.min(this.climbingTicker + 1, CLIMB_MAX);
        } else
            this.climbingTicker = Math.max(this.climbingTicker - 1, -1);
    }

    public boolean isClimbing() {
        return this.entityData.get(CLIMBING_SYNC);
    }

    public void setClimbing(boolean climbing) {
        this.entityData.set(CLIMBING_SYNC, climbing);
    }

    @Override
    public boolean onClimbable() {
        return this.isClimbing();
    }

    @Override
    public void makeStuckInBlock(BlockState state, Vec3 motionMultiplier) {
        if (!state.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(state, motionMultiplier);
        }
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double width = this.getBbWidth() * 1.5;
        double length = this.getBbWidth() * 1.7;
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(WEBSHOT)) {
            this.getNavigation().stop();
            if (anim.isAt("attack")) {
                if (this.getTarget() != null && this.getSensing().hasLineOfSight(this.getTarget()) || this.getFirstPassenger() instanceof Player) {
                    RuneCraftorySpells.WEB_SHOT.get().use(this);
                }
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    public AnimationHandler<Spider> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 1 ? RuneCraftorySpells.WEB_SHOT.get() : null))
                return;
            if (command == 1)
                this.getAnimationHandler().setAnimation(WEBSHOT);
            else
                this.getAnimationHandler().setAnimation(MELEE);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SPIDER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.SPIDER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SPIDER_DEATH;
    }

    @Override
    protected int calculateFallDamage(float distance, float damageMultiplier) {
        return (int) ((super.calculateFallDamage(distance, damageMultiplier) - 3) * 0.5);
    }

    @Override
    public float getVoicePitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.3f;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.SPIDER_STEP, 0.15f, 1.0f);
    }

    @Override
    public String getInteractAnimation() {
        return INTERACT;
    }

    @Override
    public String getSleepAnimation() {
        return STILL;
    }
}
