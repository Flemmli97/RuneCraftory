package io.github.flemmli97.runecraftory.common.entities.monster.wisp;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.NearestTargetNoLoS;
import io.github.flemmli97.runecraftory.common.entities.ai.control.FreeMoveControl;
import io.github.flemmli97.runecraftory.common.entities.ai.pathing.FloatingFlyNavigator;
import io.github.flemmli97.runecraftory.common.entities.ai.pathing.NoClipFlyEvaluator;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public abstract class EntityWispBase extends BaseMonster {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String ATTACK_FAR = BUILDER.add("attack", AnimationsBuilder.definition(0.48).marker("attack", 0.36));
    public static final String ATTACK_CLOSE = BUILDER.add("attack_close", ATTACK_FAR);
    public static final String INTERACT = BUILDER.add("interact", ATTACK_FAR);
    public static final String VANISH = BUILDER.add("vanish", AnimationsBuilder.definition(5)
            .marker("teleport", 2.5).marker("teleport_done", 2.6));
    public static final String STILL = BUILDER.add("still", AnimationsBuilder.definition(0).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    //    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityWispBase>>> ATTACKS = List.of(
//            WeightedEntry.wrap(MonsterActionUtils.simpleRangedEvadingAction(ATTACK_FAR, 7, 3, 1, e -> 1), 2),
//            WeightedEntry.wrap(new GoalAttackAction<EntityWispBase>(ATTACK_CLOSE)
//                    .cooldown(e -> e.animationCooldown(ATTACK_CLOSE))
//                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 1),
//            WeightedEntry.wrap(new GoalAttackAction<EntityWispBase>(VANISH)
//                    .withCondition(((goal, target, previous) -> goal.attacker.shouldVanishNext(previous)))
//                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 6)
//    );
//    private static final List<WeightedEntry.Wrapper<IdleAction<EntityWispBase>>> IDLE_ACTIONS = List.of(
//            WeightedEntry.wrap(new IdleAction<>(() -> new StayWithinHeightAction<>(2.0, new RandomMoveAroundRunner<>(7, 4))), 1),
//            WeightedEntry.wrap(new IdleAction<>(() -> new StayWithinHeightAction<>(2.0, new DoNothingRunner<>())), 3)
//    );
//
//    public final AnimatedAttackGoal<EntityWispBase> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private boolean vanishNext;

    private final AnimationHandler<EntityWispBase> animationHandler = new AnimationHandler<>(this, ANIMS)
            .withChangeListener(anim -> {
                if (anim != null && anim.is(VANISH))
                    this.vanishNext = this.getRandom().nextFloat() < 0.6;
                return false;
            });

    public EntityWispBase(EntityType<? extends EntityWispBase> type, Level world) {
        super(type, world);
        this.setNoGravity(true);
        this.noPhysics = true;
        this.moveControl = new FreeMoveControl(this);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new FloatingFlyNavigator(this, level) {
            @Override
            protected PathFinder createPathFinder(int maxDist) {
                this.nodeEvaluator = new NoClipFlyEvaluator();
                return new PathFinder(this.nodeEvaluator, maxDist);
            }
        };
    }

    @Override
    protected void applyAttributes() {
        super.applyAttributes();
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(32);
        this.getAttribute(Attributes.FLYING_SPEED).setBaseValue(0.2);
        this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
    }

    @Override
    protected NearestAttackableTargetGoal<Player> createTargetGoalPlayer() {
        return new NearestTargetNoLoS<>(this, Player.class, 5, false, player -> !this.isTamed());
    }

    @Override
    protected NearestAttackableTargetGoal<Mob> createTargetGoalMobs() {
        return new NearestTargetNoLoS<>(this, Mob.class, 5, false, this.targetPred);
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
    public void handleAttack(AnimationState anim) {
        LivingEntity target = this.getTarget();
        if (anim.is(ATTACK_FAR)) {
            this.getNavigation().stop();
            if (target != null)
                this.getLookControl().setLookAt(target, 360, 90);
            if (anim.isAt("attack")) {
                this.attackFar(target);
            }
        } else if (anim.is(ATTACK_CLOSE)) {
            this.getNavigation().stop();
            if (target != null)
                this.getLookControl().setLookAt(target, 360, 90);
            if (anim.isAt("attack")) {
                this.attackClose(target);
            }
        } else if (anim.is(VANISH)) {
            this.getNavigation().stop();
            if (anim.isAt("teleport")) {
                if (target == null) {
                    double rX = this.getX() + (this.random.nextDouble() - 0.5) * 16;
                    double rY = this.getY() + (this.random.nextDouble() - 0.5) * 4;
                    double rZ = this.getZ() + (this.random.nextDouble() - 0.5) * 16;
                    this.teleport(rX, rY, rZ);
                } else {
                    this.teleportTowards(target);
                }
            }
        }
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), this.getSpellFor(command)))
                return;
            if (command == 1)
                this.getAnimationHandler().setAnimation(ATTACK_CLOSE);
            else
                this.getAnimationHandler().setAnimation(ATTACK_FAR);
        }
    }

    protected abstract Spell getSpellFor(int command);

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.ENTITY_WISP_AMBIENT.get();
    }

    @Override
    public int animationCooldown(@Nullable String anim) {
        int diffAdd = this.difficultyCooldown();
        if (anim == null)
            return this.getRandom().nextInt(20) + 30 + diffAdd;
        return this.getRandom().nextInt(40) + 25 + diffAdd;
    }

    @Override
    public AnimationHandler<EntityWispBase> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void travel(Vec3 vec) {
        if (this.getFirstPassenger() instanceof LivingEntity entity) {
            this.noPhysics = entity.noPhysics;
        } else {
            this.noPhysics = !this.playDeath();
            if (this.getY() < this.level().getMinBuildHeight() + 1)
                vec = new Vec3(vec.x, 0.006, vec.z);
        }
        this.handleFreeTravel(vec);
    }

    private void teleportTowards(Entity entity) {
        Vec3 look = new Vec3(entity.getLookAngle().x, 0, entity.getLookAngle().z).normalize().scale(-2.5);
        Vec3 behindEntity = entity.position().add(look);
        Vec3 dir = new Vec3(behindEntity.x - this.getX(), behindEntity.y - this.getY(), behindEntity.z - this.getZ());
        if (dir.lengthSqr() < 100)
            this.teleport(behindEntity.x, entity.getY(), behindEntity.z);
        else {
            dir = dir.normalize();
            double e = this.getX() + this.random.nextDouble() * 9 * dir.x;
            double g = this.getZ() + this.random.nextDouble() * 9 * dir.z;
            this.teleport(e, entity.getY(), g);
        }
    }

    private void teleport(double x, double y, double z) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(x, y, z);
        while (mutableBlockPos.getY() > this.level().getMinBuildHeight() && !this.level().getBlockState(mutableBlockPos).blocksMotion()) {
            mutableBlockPos.move(Direction.DOWN);
        }
        BlockState blockState = this.level().getBlockState(mutableBlockPos);
        if (!blockState.blocksMotion()) {
            y = this.getY();
        }
        this.teleportTo(x, y + 1, z);
    }

    public abstract void attackFar(LivingEntity target);

    public abstract void attackClose(LivingEntity target);

    public boolean shouldVanishNext(String prev) {
        LivingEntity target = this.getTarget();
        if (target != null && target.distanceToSqr(this) > 140)
            return true;
        return !prev.equals(VANISH) && this.vanishNext;
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public String getSleepAnimation() {
        return STILL;
    }
//
//    @Override
//    public Vec3 passengerOffset(Entity passenger) {
//        return new Vec3(0, 10 / 16d, -3 / 16d);
//    }
}