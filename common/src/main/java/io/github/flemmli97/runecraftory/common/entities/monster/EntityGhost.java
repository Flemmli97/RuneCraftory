package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.AirWanderGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.NearestTargetNoLoS;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.ChargeAction;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.entities.ai.control.FreeMoveControl;
import io.github.flemmli97.runecraftory.common.entities.ai.pathing.FloatingFlyNavigator;
import io.github.flemmli97.runecraftory.common.entities.ai.pathing.NoClipFlyEvaluator;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.common.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.DoNothingRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.RandomMoveAroundRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Consumer;

public class EntityGhost extends ChargingMonster {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String DARKBALL = BUILDER.add("darkball", AnimationsBuilder.definition(0.64).marker("attack", 0.28));
    public static final String CHARGE = BUILDER.add("charge", AnimationsBuilder.definition(1.2).marker("attack_start", 0.36));
    public static final String SWING = BUILDER.add("swing", AnimationsBuilder.definition(0.52).marker("attack", 0.24));
    public static final String VANISH = BUILDER.add("vanish", AnimationsBuilder.definition(5).marker("teleport", 2.5));
    public static final String INTERACT = BUILDER.add("interact", SWING);
    public static final String STILL = BUILDER.add("still", AnimationsBuilder.definition(0).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityGhost>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.simpleRangedEvadingAction(DARKBALL, 9, 1, 1, e -> 1), 1),
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(SWING, e -> 1), 1),
            WeightedEntry.wrap(new GoalAttackAction<EntityGhost>(CHARGE)
                    .cooldown(e -> e.animationCooldown(CHARGE))
                    .withCondition(MonsterActionUtils.chargeCondition())
                    .prepare(ChargeAction::new), 2),
            WeightedEntry.wrap(new GoalAttackAction<EntityGhost>(VANISH)
                    .withCondition(((goal, target, previous) -> goal.attacker.shouldVanishNext(previous)))
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 4)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityGhost>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(16, 5)), 3),
            WeightedEntry.wrap(new IdleAction<>(DoNothingRunner::new), 1)
    );

    public final AnimatedAttackGoal<EntityGhost> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityGhost> animationHandler = new AnimationHandler<>(this, ANIMS);

    private boolean vanishNext;

    public EntityGhost(EntityType<? extends EntityGhost> type, Level world) {
        super(type, world);
        this.goalSelector.removeGoal(this.wander);
        this.goalSelector.addGoal(6, this.wander = new AirWanderGoal(this));
        this.goalSelector.removeGoal(this.swimGoal);
        this.goalSelector.addGoal(2, this.attack);
        this.setNoGravity(true);
        this.noPhysics = true;
        this.moveControl = new FreeMoveControl(this);
    }

    @Override
    protected Consumer<AnimatedAction> animatedActionConsumer() {
        return anim -> {
            super.animatedActionConsumer().accept(anim);
            if (anim != null && anim.is(VANISH))
                this.vanishNext = this.getRandom().nextFloat() < 0.6;
        };
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
        this.getAttribute(Attributes.FLYING_SPEED).setBaseValue(0.31);
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
    public CustomDamage.Builder damageSourceAttack() {
        CustomDamage.Builder source = super.damageSourceAttack();
        if (this.getAnimationHandler().isCurrent(CHARGE))
            source.knock(CustomDamage.KnockBackType.BACK).knockAmount(1);
        return source;
    }

    @Override
    public AABB attackBB(AnimatedAction anim) {
        double width = this.getBbWidth() * 2.9;
        double length = this.getBbWidth() * 2.5;
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 2 ? ModSpells.DARK_BALL.get() : null))
                return;
            if (command == 2)
                this.getAnimationHandler().setAnimation(DARKBALL);
            else if (command == 1)
                this.getAnimationHandler().setAnimation(CHARGE);
            else
                this.getAnimationHandler().setAnimation(SWING);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.ENTITY_GHOST_AMBIENT.get();
    }

    @Override
    public AnimationHandler<EntityGhost> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean handleChargeMovement(AnimatedAction anim) {
        if (this.getChargeMotion() != null) {
            this.setDeltaMovement(this.getChargeMotion().x * 0.98f, this.getDeltaMovement().y, this.getChargeMotion().z * 0.98f);
            return true;
        }
        return false;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(DARKBALL)) {
            this.getNavigation().stop();
            if (anim.isAt("attack")) {
                ModSpells.DARK_BALL.get().use(this);
            }
        } else if (anim.is(VANISH)) {
            this.getNavigation().stop();
            if (anim.isAt("teleport")) {
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
    protected boolean isChargingAnim(AnimatedAction anim) {
        return anim.is(CHARGE);
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

    @Override
    public Vec3 getChargeTo(AnimatedAction anim) {
        return EntityUtils.getTargetDirection(this, EntityAnchorArgument.Anchor.FEET)
                .scale(this.chargingSpeed());
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
        while (mutableBlockPos.getY() > this.level().getMinBuildHeight() && !this.level().getBlockState(mutableBlockPos).getMaterial().blocksMotion()) {
            mutableBlockPos.move(Direction.DOWN);
        }
        BlockState blockState = this.level().getBlockState(mutableBlockPos);
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
    public MobType getMobType() {
        return MobType.UNDEAD;
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
        return new Vec3(0, 11.5 / 16d, -10 / 16d);
    }

    @Override
    public boolean hasLineOfSight(Entity entity) {
        return true;
    }
}
