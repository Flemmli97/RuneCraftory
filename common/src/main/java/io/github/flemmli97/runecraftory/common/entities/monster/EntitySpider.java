package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.DoNothingRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.RandomMoveAroundRunner;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntitySpider extends BaseMonster {

    private static final EntityDataAccessor<Boolean> CLIMBING_SYNC = SynchedEntityData.defineId(EntitySpider.class, EntityDataSerializers.BOOLEAN);

    public static final AnimatedAction MELEE = new AnimatedAction(13, 9, "attack");
    public static final AnimatedAction WEBSHOT = new AnimatedAction(14, 6, "webshot");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(MELEE, "interact");
    public static final AnimatedAction STILL = AnimatedAction.builder(1, "still").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{MELEE, WEBSHOT, INTERACT, STILL};

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntitySpider>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(MELEE, e -> 0.7f), 1),
            WeightedEntry.wrap(MonsterActionUtils.simpleRangedStrafingAction(WEBSHOT, 7, 1, e -> 1), 2)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntitySpider>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 1)), 5),
            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(9, 3)), 3),
            WeightedEntry.wrap(new IdleAction<>(DoNothingRunner::new), 1)
    );

    public final AnimatedAttackGoal<EntitySpider> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntitySpider> animationHandler = new AnimationHandler<>(this, ANIMS);

    public int climbingTicker = -1;
    public static final int CLIMB_MAX = 9;

    public EntitySpider(EntityType<? extends EntitySpider> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    protected void applyAttributes() {
        super.applyAttributes();
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.27);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WallClimberNavigation(this, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CLIMBING_SYNC, false);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            this.setClimbing(this.horizontalCollision);
        }
        if (this.isClimbing() && this.isAlive() && !this.playDeath()) {
            this.climbingTicker = Math.min(this.climbingTicker + 1, CLIMB_MAX);
        } else
            this.climbingTicker = Math.max(this.climbingTicker - 1, -1);
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

    public boolean isClimbing() {
        return this.entityData.get(CLIMBING_SYNC);
    }

    public void setClimbing(boolean climbing) {
        this.entityData.set(CLIMBING_SYNC, climbing);
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
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.SPIDER_STEP, 0.15f, 1.0f);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SPIDER_DEATH;
    }

    @Override
    public float getVoicePitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.3f;
    }

    @Override
    public AnimationHandler<EntitySpider> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 1;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(WEBSHOT)) {
            this.getNavigation().stop();
            if (anim.getTick() == 1 && this.getTarget() != null) {
                this.lookAtNow(this.getTarget(), 360, 90);
                this.targetPosition = this.getTarget().position();
            }
            if (anim.canAttack()) {
                if (this.getTarget() != null && this.getSensing().hasLineOfSight(this.getTarget()) || this.getFirstPassenger() instanceof Player) {
                    ModSpells.WEB_SHOT.get().use(this);
                }
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 1 ? ModSpells.WEB_SHOT.get() : null))
                return;
            if (command == 1)
                this.getAnimationHandler().setAnimation(WEBSHOT);
            else
                this.getAnimationHandler().setAnimation(MELEE);
        }
    }

    @Override
    protected int calculateFallDamage(float distance, float damageMultiplier) {
        return (int) ((super.calculateFallDamage(distance, damageMultiplier) - 3) * 0.5);
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public AnimatedAction getSleepAnimation() {
        return STILL;
    }

    @Override
    public Vec3 passengerOffset(Entity passenger) {
        return new Vec3(0, 10 / 16d, 3.5 / 16d);
    }
}
