package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.SetWalkTargetWithinDist;
import io.github.flemmli97.runecraftory.common.entities.ai.control.FreeMoveControl;
import io.github.flemmli97.runecraftory.common.entities.ai.pathing.FloatingFlyNavigator;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.MoveToAttackTarget;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetSetClampedFloatingMoveTarget;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.StrafeTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomHoverTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import org.jetbrains.annotations.Nullable;

public class VeggieGhost extends BaseMonster {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String ATTACK = BUILDER.add("head_attack", AnimationsBuilder.definition(1.16).marker("attack", 0.68));
    public static final String CAST = BUILDER.add("cast", AnimationsBuilder.definition(0.68).marker("attack", 0.36));
    public static final String INTERACT = BUILDER.add("interact", CAST);
    public static final String SPIN = BUILDER.add("spin", AnimationsBuilder.definition(0.68).marker("attack", 0.36));
    public static final String VANISH = BUILDER.add("vanish", AnimationsBuilder.definition(5).marker("attack", 2.5));
    public static final String SLEEP = BUILDER.add("sleep", AnimationsBuilder.definition(0).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();
    private boolean vanishNext;

    private final AnimationHandler<VeggieGhost> animationHandler = new AnimationHandler<>(this, ANIMS).withChangeListener(anim -> {
        if (anim != null && anim.is(VANISH))
            this.vanishNext = this.getRandom().nextFloat() < 0.6;
        return false;
    });

    public VeggieGhost(EntityType<? extends VeggieGhost> type, Level level) {
        super(type, level);
        this.noPhysics = true;
        this.moveControl = new FreeMoveControl(this);
        this.setNoGravity(true);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new FloatingFlyNavigator(this, level);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(32);
        this.getAttribute(Attributes.FLYING_SPEED).setBaseValue(0.35);
        super.applyAttributes();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<BaseMonster>create()
                .start(ATTACK).play(MonsterBehaviourUtils.requireInRangePlay())
                .condition(MonsterBehaviourUtils.ifCloserThan(3))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(new MoveToAttackTarget<>())
                .end(5)
                .start(SPIN).play(MonsterBehaviourUtils.requireInRangePlay())
                .condition(MonsterBehaviourUtils.ifCloserThan(3))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(new MoveToAttackTarget<>())
                .end(5)
                .start(CAST).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepareOptional(new SetWalkTargetWithinDist<BaseMonster>().min(3).max(9), new MoveToWalkTarget<>())
                .end(7)
                .start(MonsterBehaviourUtils.checkedAttack(VANISH)).play(MonsterBehaviourUtils.cooldownedPlay())
                .end(9)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseMonster>builder()
                .add(4, new SetSetClampedFloatingMoveTarget<>(2.), new MoveToWalkTarget<>())
                .add(5, new StrafeTarget<>()).build();
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
    public OrientedBoundingBox calculateAttackAABB(AnimationState anim, Vec3 target, double grow) {
        if (anim.is(SPIN)) {
            return new OrientedBoundingBox(this.attackBB(anim), this.getYRot(), 0, this.position());
        }
        return super.calculateAttackAABB(anim, target, grow);
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        if (anim.is(SPIN)) {
            double attackSize = this.getBbWidth() * 1.4;
            return new AABB(-attackSize, -0.2, -attackSize, attackSize, this.getBbHeight() + 0.2, attackSize);
        }
        double width = this.getBbWidth() * 1.6;
        double length = this.getBbWidth() * 2;
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(CAST)) {
            this.getNavigation().stop();
            if (anim.isAt("attack")) {
                RuneCraftorySpells.TRIPLE_FIRE_BALL.get().use(this);
            }
        } else if (anim.is(VANISH)) {
            this.getNavigation().stop();
            if (anim.isAt("attack")) {
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
    public AnimationHandler<VeggieGhost> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 1 ? RuneCraftorySpells.TRIPLE_FIRE_BALL.get() : null))
                return;
            if (command == 1)
                this.getAnimationHandler().setAnimation(CAST);
            else
                this.getAnimationHandler().setAnimation(ATTACK);
        }
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
        return this.random.nextFloat() < 0.2f || !prev.equals(VANISH) && this.vanishNext;
    }

    @Override
    protected void checkFallDamage(double dist, boolean groundLogic, BlockState state, BlockPos pos) {
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

    @Override
    protected SoundEvent getAmbientSound() {
        return RuneCraftorySounds.ENTITY_GHOST_AMBIENT.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
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
