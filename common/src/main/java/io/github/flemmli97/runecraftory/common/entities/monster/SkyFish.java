package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.entities.ai.control.FreeMoveControl;
import io.github.flemmli97.runecraftory.common.entities.ai.pathing.FloatingFlyNavigator;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetAwayFromTarget;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomFlyingTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomHoverTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

public class SkyFish extends BaseMonster {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String SLAP = BUILDER.add("slap", AnimationsBuilder.definition(0.56).marker("attack", 0.28));
    public static final String INTERACT = BUILDER.add("interact", SLAP);
    public static final String BEAM = BUILDER.add("beam", AnimationsBuilder.definition(0.68).marker("attack", 0.4));
    public static final String SWIPE = BUILDER.add("swipe", AnimationsBuilder.definition(0.76).marker("attack", 0.28));
    public static final String STILL = BUILDER.add("still", AnimationsBuilder.definition(0).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private final AnimationHandler<SkyFish> animationHandler = new AnimationHandler<>(this, ANIMS);

    public SkyFish(EntityType<? extends BaseMonster> type, Level level) {
        super(type, level);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.moveControl = new FreeMoveControl(this, () -> false);
        this.setNoGravity(true);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new FloatingFlyNavigator(this, level);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25);
        super.applyAttributes();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<BaseMonster>create()
                .start(SLAP).play(MonsterBehaviourUtils.requireInRangePlay())
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(MonsterBehaviourUtils.moveAttack())
                .end(2)
                .start(BEAM).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetAwayFromTarget<BaseMonster>().minDist(4).radius(4)).prepareOptional(MonsterBehaviourUtils.moveAttack())
                .end(4)
                .start(SWIPE).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetAwayFromTarget<BaseMonster>().minDist(4).radius(4)).prepareOptional(MonsterBehaviourUtils.moveAttack())
                .end(4)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseMonster>builder()
                .add(1, new SetWalkTargetToAttackTarget<BaseMonster>()
                        .closeEnoughDist(MonsterBehaviourUtils.closeEnough(7)), MonsterBehaviourUtils.moveTo())
                .add(2, MonsterBehaviourUtils.ifCloserThan(10), new SetRandomFlyingTarget<BaseMonster>()
                        .flightTargetPredicate((entity, pos) -> {
                            LivingEntity target = BrainUtils.hasMemory(entity, MemoryModuleType.ATTACK_TARGET) ? BrainUtils.getTargetOfEntity(entity) : null;
                            if (target == null) {
                                target = entity.getTarget();
                            }
                            return target != null && target.distanceToSqr(pos) <= 11 * 11 && Math.abs(target.getY() - pos.y()) < 6;
                        }), MonsterBehaviourUtils.moveTo())
                .add(4, MonsterBehaviourUtils.ifFurtherThan(7), new Idle<>()).build();
    }

    @Override
    protected ExtendedBehaviour<? extends BaseMonster> getWanderBehaviour() {
        return new SetRandomHoverTarget<>();
    }

    @Override
    protected int wanderChance() {
        return 50;
    }

    @Override
    protected boolean canFloatInWater() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && this.getTarget() == null && !this.isInWater() && this.belowSoldid()) {
            Vec3 mot = this.getDeltaMovement();
            double newY = Math.max(0, mot.y);
            newY += 0.03;
            this.setDeltaMovement(mot.x, Math.min(0.3, newY), mot.z);
        }
    }

    private boolean belowSoldid() {
        BlockPos pos = this.blockPosition().below();
        return this.level().getBlockState(pos).entityCanStandOn(this.level(), pos, this);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    public void travel(Vec3 vec) {
        this.handleFreeTravel(vec);
    }

    @Override
    public int animationCooldown(@Nullable String anim) {
        int diffAdd = this.difficultyCooldown();
        return this.getRandom().nextInt(30) + 20 + diffAdd;
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double width = this.getBbWidth() * 1.5;
        double length = this.getBbWidth() * 1.6;
        return new AABB(-width * 0.2, -0.02, 0, width * 0.8, this.getBbHeight() + 0.02, length);
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(BEAM)) {
            this.getNavigation().stop();
            if (anim.isAt("attack")) {
                RuneCraftorySpells.WATER_LASER.get().use(this);
            }
        } else if (anim.is(SWIPE)) {
            if (anim.isAt("attack")) {
                RuneCraftorySpells.WATER_SWIPE.get().use(this);
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    public AnimationHandler<SkyFish> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 1) {
                if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), RuneCraftorySpells.WATER_LASER.get()))
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
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
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
        return STILL;
    }
}
