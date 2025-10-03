package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.SetChargeTarget;
import io.github.flemmli97.runecraftory.common.entities.ai.control.FreeMoveControl;
import io.github.flemmli97.runecraftory.common.entities.ai.pathing.FloatingFlyNavigator;
import io.github.flemmli97.runecraftory.common.entities.ai.pathing.NoClipFlyEvaluator;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetSetClampedFloatingMoveTarget;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetAwayFromTarget;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinition;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomHoverTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class Ghost extends ChargingMonster {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String DARKBALL = BUILDER.add("darkball", AnimationsBuilder.definition(0.64).marker("attack", 0.28));
    public static final String CHARGE = BUILDER.add("charge", AnimationsBuilder.definition(1.2).marker("attack_start", 0.36));
    public static final String SWING = BUILDER.add("swing", AnimationsBuilder.definition(0.52).marker("attack", 0.24));
    public static final String INTERACT = BUILDER.add("interact", SWING);
    public static final String VANISH = BUILDER.add("vanish", AnimationsBuilder.definition(5).marker("teleport", 2.5));
    public static final String STILL = BUILDER.add("still", AnimationsBuilder.definition(0).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private final AnimationHandler<Ghost> animationHandler = new AnimationHandler<>(this, ANIMS);

    private boolean vanishNext;

    public Ghost(EntityType<? extends Ghost> type, Level level) {
        super(type, level);
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
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(32);
        this.getAttribute(Attributes.FLYING_SPEED).setBaseValue(0.31);
        this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
        super.applyAttributes();
    }

    @Override
    protected Consumer<AnimationDefinition> animatedActionConsumer() {
        return anim -> {
            super.animatedActionConsumer().accept(anim);
            if (anim != null && anim.is(VANISH))
                this.vanishNext = this.getRandom().nextFloat() < 0.6;
        };
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<ChargingMonster>create()
                .start(DARKBALL).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetAwayFromTarget<ChargingMonster>().minDist(3)).prepareOptional(MonsterBehaviourUtils.moveAttack())
                .end(5)
                .start(SWING).play(MonsterBehaviourUtils.requireInRangePlay())
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(MonsterBehaviourUtils.moveAttack())
                .end(5)
                .start(CHARGE).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepareOptional(new SetWalkTargetToAttackTarget<ChargingMonster>()
                        .closeEnoughDist(MonsterBehaviourUtils.closeEnough(8)), MonsterBehaviourUtils.moveTo(), new SetChargeTarget<>())
                .end(4)
                .start(MonsterBehaviourUtils.checkedAttack(VANISH)).play(MonsterBehaviourUtils.cooldownedPlay())
                .end(9)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseMonster>builder()
                .add(2, new SetWalkTargetToAttackTarget<BaseMonster>()
                        .closeEnoughDist(MonsterBehaviourUtils.closeEnough(5)), MonsterBehaviourUtils.moveTo())
                .add(4, MonsterBehaviourUtils.ifCloserThan(10), new SetSetClampedFloatingMoveTarget<>(2.), MonsterBehaviourUtils.moveTo())
                .add(6, MonsterBehaviourUtils.withCondition(MonsterBehaviourUtils.ifCloserThan(9)), new Idle<>()).build();
    }

    @Override
    protected ExtendedBehaviour<? extends BaseMonster> getWanderBehaviour() {
        return new SetRandomHoverTarget<>();
    }

    @Override
    protected boolean canFloatInWater() {
        return false;
    }

    @Override
    public boolean hasLineOfSight(Entity entity) {
        return true;
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
    public AABB attackBB(AnimationState anim) {
        double width = this.getBbWidth() * 2.9;
        double length = this.getBbWidth() * 2.5;
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public DynamicDamage.Builder damageSourceAttack() {
        DynamicDamage.Builder source = super.damageSourceAttack();
        if (this.getAnimationHandler().isCurrent(CHARGE))
            source.knock(DynamicDamage.KnockBackType.BACK, 1);
        return source;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 2 ? RuneCraftorySpells.DARK_BALL.get() : null))
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
    public void handleAttack(AnimationState anim) {
        if (anim.is(DARKBALL)) {
            this.getNavigation().stop();
            if (anim.isAt("attack")) {
                RuneCraftorySpells.DARK_BALL.get().use(this);
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
    public AnimationHandler<Ghost> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    protected boolean isChargingAnim(String anim) {
        return anim.equals(CHARGE);
    }

    @Override
    public boolean handleChargeMovement(AnimationState anim) {
        if (this.getChargeMotion() != null) {
            this.setDeltaMovement(this.getChargeMotion().x * 0.98f, this.getDeltaMovement().y, this.getChargeMotion().z * 0.98f);
            return true;
        }
        return false;
    }

    @Override
    public Vec3 getChargeTo(String animation) {
        return EntityUtils.getTargetDirection(this, EntityAnchorArgument.Anchor.FEET)
                .scale(this.chargingSpeed());
    }

    @Override
    public double chargingSpeed() {
        return 0.5;
    }

    @Override
    public boolean allowAnimation(@Nullable String prev, String other) {
        if (other.equals(VANISH))
            return this.shouldVanishNext(prev);
        return super.allowAnimation(prev, other);
    }

    public boolean shouldVanishNext(String prev) {
        LivingEntity target = this.getTarget();
        if (target != null && target.distanceToSqr(this) > 140)
            return true;
        return this.random.nextFloat() < 0.2f || !VANISH.equals(prev) && this.vanishNext;
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

    private void teleportTowards(Entity entity) {
        Vec3 look = EntityUtils.horizontalLookAngle(entity).scale(-1.5);
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

    @Override
    protected SoundEvent getAmbientSound() {
        return RuneCraftorySounds.ENTITY_GHOST_AMBIENT.get();
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
