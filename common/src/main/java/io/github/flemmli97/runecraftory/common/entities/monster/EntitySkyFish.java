package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.SwimWalkMoveController;
import io.github.flemmli97.runecraftory.common.entities.ai.AirWanderGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.NearestTargetHorizontal;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.entities.ai.pathing.FloatingFlyNavigator;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWaterLaser;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.DoNothingRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.RandomMoveAroundRunner;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntitySkyFish extends BaseMonster {

    public static final AnimatedAction SLAP = new AnimatedAction(11, 6, "slap");
    public static final AnimatedAction BEAM = new AnimatedAction(14, 7, "beam");
    public static final AnimatedAction SWIPE = new AnimatedAction(16, 4, "swipe");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(SLAP, "interact");
    public static final AnimatedAction STILL = AnimatedAction.builder(1, "still").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{SLAP, BEAM, SWIPE, INTERACT, STILL};

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntitySkyFish>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(SLAP, e -> 0.6f), 1),
            WeightedEntry.wrap(MonsterActionUtils.simpleRangedEvadingAction(BEAM, 8, 5, 1, e -> 1), 2),
            WeightedEntry.wrap(MonsterActionUtils.simpleRangedEvadingAction(SWIPE, 8, 5, 1, e -> 1), 1)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntitySkyFish>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(10, 5)), 2),
            WeightedEntry.wrap(new IdleAction<>(DoNothingRunner::new), 1)
    );

    public final AnimatedAttackGoal<EntitySkyFish> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntitySkyFish> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntitySkyFish(EntityType<? extends BaseMonster> type, Level world) {
        super(type, world);
        this.goalSelector.removeGoal(this.wander);
        this.goalSelector.addGoal(6, this.wander = new AirWanderGoal(this));
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        //this.setPathPriority(BlockPathTypes.OPEN, 0.5f);
        this.goalSelector.addGoal(2, this.attack);
        this.moveControl = new FlySwimMoveController(this);
        this.goalSelector.removeGoal(this.swimGoal);
        this.wander.setInterval(50);
        this.setNoGravity(true);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new FloatingFlyNavigator(this, level);
    }

    @Override
    protected void applyAttributes() {
        super.applyAttributes();
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.13);
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(32);
    }

    @Override
    protected NearestAttackableTargetGoal<Player> createTargetGoalPlayer() {
        return new NearestTargetHorizontal<>(this, Player.class, 5, true, true, player -> !this.isTamed());
    }

    @Override
    protected NearestAttackableTargetGoal<Mob> createTargetGoalMobs() {
        return new NearestTargetHorizontal<>(this, Mob.class, 5, true, true, this.targetPred);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide && this.getTarget() == null && !this.isInWater() && this.belowSoldid()) {
            Vec3 mot = this.getDeltaMovement();
            double newY = Math.max(0, mot.y);
            newY += 0.03;
            this.setDeltaMovement(mot.x, Math.min(0.3, newY), mot.z);
        }
    }

    @Override
    public void travel(Vec3 vec) {
        this.handleNoGravTravel(vec);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(BEAM)) {
            this.getNavigation().stop();
            if (anim.canAttack()) {
                ModSpells.WATER_LASER.get().use(this);
            }
        } else if (anim.is(SWIPE)) {
            if (anim.canAttack()) {
                if (EntityUtils.sealed(this))
                    return;
                EntityWaterLaser laser = new EntityWaterLaser(this.level, this, -4).setMaxTicks(10);
                Vec3 dir;
                if (this.getTarget() != null) {
                    dir = this.getTarget().getEyePosition(1).subtract(this.position());
                } else {
                    dir = this.getLookAngle();
                }
                laser.setRotationToDirWithOffset(dir.x(), dir.y(), dir.z(), 0, 20);
                this.level.addFreshEntity(laser);
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 1) {
                if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), ModSpells.WATER_LASER.get()))
                    return;
                this.getAnimationHandler().setAnimation(BEAM);
            } else {
                if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                    return;
                if (command == 2) {
                    this.getAnimationHandler().setAnimation(SWIPE);
                } else
                    this.getAnimationHandler().setAnimation(SLAP);
            }
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.COD_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.COD_DEATH;
    }

    @Override
    public float getVoicePitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 0.8f;
    }

    @Override
    public AnimationHandler<EntitySkyFish> getAnimationHandler() {
        return this.animationHandler;
    }

    private boolean belowSoldid() {
        BlockPos pos = this.blockPosition().below();
        return this.level.getBlockState(pos).entityCanStandOn(this.level, pos, this);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    //==========Water stuff

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean rideableUnderWater() {
        return false;
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
        return STILL;
    }

    @Override
    public Vec3 passengerOffset(Entity passenger) {
        return new Vec3(0, 12 / 16d, -2 / 16d);
    }

    static class FlySwimMoveController extends SwimWalkMoveController {

        public FlySwimMoveController(Mob entity) {
            super(entity);
        }

        @Override
        protected void moveSpeed() {
        }

        @Override
        public void tick() {
            this.handleWaterMovement();
        }

    }
}
