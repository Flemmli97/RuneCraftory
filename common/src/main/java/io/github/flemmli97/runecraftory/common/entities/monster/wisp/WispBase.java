package io.github.flemmli97.runecraftory.common.entities.monster.wisp;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MoveToWalkTillClose;
import io.github.flemmli97.runecraftory.common.entities.ai.control.FreeMoveControl;
import io.github.flemmli97.runecraftory.common.entities.ai.pathing.FloatingFlyNavigator;
import io.github.flemmli97.runecraftory.common.entities.ai.pathing.NoClipFlyEvaluator;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.MoveToAttackTarget;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetSetClampedFloatingMoveTarget;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetAwayFromTarget;
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
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomHoverTarget;
import org.jetbrains.annotations.Nullable;

public abstract class WispBase extends BaseMonster {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String ATTACK_FAR = BUILDER.add("attack", AnimationsBuilder.definition(0.48).marker("attack", 0.36));
    public static final String ATTACK_CLOSE = BUILDER.add("attack_close", ATTACK_FAR);
    public static final String INTERACT = BUILDER.add("interact", ATTACK_FAR);
    public static final String VANISH = BUILDER.add("vanish", AnimationsBuilder.definition(5)
            .marker("teleport", 2.5).marker("teleport_done", 2.6));
    public static final String STILL = BUILDER.add("still", AnimationsBuilder.definition(0).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private boolean vanishNext;

    private final AnimationHandler<WispBase> animationHandler = new AnimationHandler<>(this, ANIMS)
            .withChangeListener(anim -> {
                if (anim != null && anim.is(VANISH))
                    this.vanishNext = this.getRandom().nextFloat() < 0.6;
                return false;
            });

    public WispBase(EntityType<? extends WispBase> type, Level level) {
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
        this.getAttribute(Attributes.FLYING_SPEED).setBaseValue(0.2);
        this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
        super.applyAttributes();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<BaseMonster>create()
                .start(ATTACK_FAR).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetAwayFromTarget<BaseMonster>().minDist(2)).prepareOptional(new MoveToAttackTarget<>())
                .end(5)
                .start(ATTACK_CLOSE).play(MonsterBehaviourUtils.cooldownedPlay())
                .end(3)
                .start(MonsterBehaviourUtils.checkedAttack(VANISH)).play(MonsterBehaviourUtils.cooldownedPlay())
                .end(8)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseMonster>builder().add(4, new SetSetClampedFloatingMoveTarget<>(2.), new MoveToWalkTillClose<>())
                .add(6, new Idle<>()).build();
    }

    @Override
    protected ExtendedBehaviour<? extends BaseMonster> getWanderBehaviour() {
        return new SetRandomHoverTarget<>();
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
    public int animationCooldown(@Nullable String anim) {
        int diffAdd = this.difficultyCooldown();
        if (anim == null)
            return this.getRandom().nextInt(20) + 30 + diffAdd;
        return this.getRandom().nextInt(40) + 25 + diffAdd;
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
    public AnimationHandler<WispBase> getAnimationHandler() {
        return this.animationHandler;
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
    public boolean allowAnimation(@Nullable String prev, String other) {
        if (other.equals(VANISH))
            return this.shouldVanishNext(prev);
        return super.allowAnimation(prev, other);
    }

    public boolean shouldVanishNext(String prev) {
        LivingEntity target = this.getTarget();
        if (target != null && target.distanceToSqr(this) > 140)
            return true;
        return !prev.equals(VANISH) && this.vanishNext;
    }

    public abstract void attackFar(LivingEntity target);

    public abstract void attackClose(LivingEntity target);

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
        Vec3 look = EntityUtils.horizontalLookAngle(entity).scale(-2.5);
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

    @Override
    protected SoundEvent getAmbientSound() {
        return RuneCraftorySounds.ENTITY_WISP_AMBIENT.get();
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