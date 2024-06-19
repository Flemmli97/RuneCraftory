package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.AirWanderGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.NearestTargetHorizontal;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.entities.ai.pathing.FloatingFlyNavigator;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.ActionUtils;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.DoNothingRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.RandomMoveAroundRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.StrafingRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityVeggieGhost extends BaseMonster {

    public static final AnimatedAction ATTACK = new AnimatedAction(23, 12, "head_attack");
    public static final AnimatedAction CAST = new AnimatedAction(14, 7, "cast");
    public static final AnimatedAction SPIN = new AnimatedAction(14, 6, "spin");
    public static final AnimatedAction VANISH = new AnimatedAction(100, 50, "vanish");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(CAST, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(1, "sleep").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{ATTACK, CAST, SPIN, VANISH, INTERACT, SLEEP};

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityVeggieGhost>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeActionCondition(ATTACK, e -> 1, ActionUtils.ranged(8)), 1),
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeActionCondition(SPIN, e -> 1, ActionUtils.ranged(8)), 1),
            WeightedEntry.wrap(MonsterActionUtils.simpleRangedEvadingAction(CAST, 10, 5, 1, e -> 1), 2),
            WeightedEntry.wrap(new GoalAttackAction<EntityVeggieGhost>(VANISH)
                    .withCondition(((goal, target, previous) -> goal.attacker.shouldVanishNext(previous)))
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 4)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityVeggieGhost>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(16, 5)), 2),
            WeightedEntry.wrap(new IdleAction<>(() -> new StrafingRunner<>(16, 5)), 1)
    );

    public final AnimatedAttackGoal<EntityVeggieGhost> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityVeggieGhost> animationHandler = new AnimationHandler<>(this, ANIMS).setAnimationChangeCons(anim -> {
        if (anim != null && anim.is(VANISH))
            this.vanishNext = this.getRandom().nextFloat() < 0.6;
    });

    private boolean vanishNext;

    public EntityVeggieGhost(EntityType<? extends EntityVeggieGhost> type, Level world) {
        super(type, world);
        this.goalSelector.removeGoal(this.wander);
        this.goalSelector.addGoal(6, this.wander = new AirWanderGoal(this));
        this.goalSelector.addGoal(2, this.attack);
        this.moveControl = new FlyingMoveControl(this, 50, true);
        this.setNoGravity(true);
    }

    @Override
    protected void applyAttributes() {
        super.applyAttributes();
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(32);
        this.getAttribute(Attributes.FLYING_SPEED).setBaseValue(0.5f);
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
    public void travel(Vec3 vec) {
        if (this.isEffectiveAi() && this.isVehicle() && this.canBeControlledByRider() && this.getControllingPassenger() instanceof LivingEntity entity) {
            this.noPhysics = entity.noPhysics;
            this.handleNoGravTravel(vec);
        } else {
            this.noPhysics = !this.playDeath();
            if (this.getY() < this.level.getMinBuildHeight() + 1)
                vec = new Vec3(vec.x, 0.006, vec.z);
            super.travel(vec);
        }
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 1.5;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(CAST)) {
            this.getNavigation().stop();
            if (anim.getTick() == 1 && this.getTarget() != null)
                this.lookAt(this.getTarget(), 360, 90);
            if (anim.canAttack()) {
                ModSpells.TRIPLE_FIRE_BALL.get().use(this);
            }
        } else if (anim.is(VANISH)) {
            this.getNavigation().stop();
            if (anim.canAttack()) {
                LivingEntity target = this.getTarget();
                if (target == null) {
                    double rX = this.getX() + (this.random.nextDouble() - 0.5) * 16;
                    double rY = this.getY() + (this.random.nextDouble() - 0.5) * 4;
                    double rZ = this.getZ() + (this.random.nextDouble() - 0.5) * 16;
                    this.teleport(rX, rY, rZ);
                } else {
                    this.teleportTowards(target);
                }
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.getAnimationHandler().isCurrent(VANISH))
            return false;
        boolean ret = super.hurt(source, amount);
        if (ret)
            this.vanishNext = this.getRandom().nextFloat() < 0.4;
        return ret;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 1 ? ModSpells.TRIPLE_FIRE_BALL.get() : null))
                return;
            if (command == 1)
                this.getAnimationHandler().setAnimation(CAST);
            else
                this.getAnimationHandler().setAnimation(ATTACK);
        }
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new FloatingFlyNavigator(this, level);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.ENTITY_GHOST_AMBIENT.get();
    }

    private void teleportTowards(Entity entity) {
        Vec3 look = new Vec3(entity.getLookAngle().x, 0, entity.getLookAngle().z).normalize().scale(-1.5);
        Vec3 behindEntity = entity.position().add(look);
        Vec3 dir = new Vec3(behindEntity.x - this.getX(), behindEntity.y - this.getY(), behindEntity.z - this.getZ());
        if (dir.lengthSqr() < 100)
            this.teleport(behindEntity.x, behindEntity.y, behindEntity.z);
        else {
            dir = dir.normalize();
            double e = this.getX() + this.random.nextDouble() * 9 * dir.x;
            double g = this.getZ() + this.random.nextDouble() * 9 * dir.z;
            this.teleport(e, entity.getY(), g);
        }
    }

    private void teleport(double x, double y, double z) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(x, y, z);
        while (mutableBlockPos.getY() > this.level.getMinBuildHeight() && !this.level.getBlockState(mutableBlockPos).getMaterial().blocksMotion()) {
            mutableBlockPos.move(Direction.DOWN);
        }
        BlockState blockState = this.level.getBlockState(mutableBlockPos);
        if (!blockState.getMaterial().blocksMotion()) {
            y = this.getY();
        }
        this.teleportTo(x, y + 1, z);
    }

    public boolean shouldVanishNext(String prev) {
        LivingEntity target = this.getTarget();
        if (target != null && target.distanceToSqr(this) > 140)
            return true;
        return this.random.nextFloat() < 0.2f || !prev.equals(VANISH.getID()) && this.vanishNext;
    }

    @Override
    public AnimationHandler<EntityVeggieGhost> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    protected void checkFallDamage(double dist, boolean groundLogic, BlockState state, BlockPos pos) {
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
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
        return new Vec3(0, 12 / 16d, -4 / 16d);
    }
}
