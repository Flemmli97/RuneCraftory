package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.entities.ai.control.FreeMoveControl;
import io.github.flemmli97.runecraftory.common.entities.ai.pathing.FloatingFlyNavigator;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetAwayFromTarget;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.entity.data.SyncedDataContainer;
import io.github.flemmli97.tenshilib.common.registry.TenshilibSyncableEntityDatas;
import io.github.flemmli97.tenshilib.common.utils.TypedResource;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.StrafeTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomFlyingTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomHoverTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Weagle extends BaseMonster {

    public static final TypedResource<Vec3> SWOOP_MOTION = new TypedResource<>(RuneCraftory.modRes("swoop_motion"));

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String GALE = BUILDER.add("gale", AnimationsBuilder.definition(0.96).marker("attack", 0.28));
    public static final String PECK = BUILDER.add("peck", AnimationsBuilder.definition(0.56).marker("attack", 0.2));
    public static final String INTERACT = BUILDER.add("interact", PECK);
    public static final String SWOOP = BUILDER.add("swoop", AnimationsBuilder.definition(0.6)
            .marker("swoop_start", 0.2).marker("swoop_end", 0.48));
    public static final String DEFEAT = BUILDER.add("defeat", AnimationsBuilder.definition(2).infinite());
    public static final String SLEEP = BUILDER.add("sleep", AnimationsBuilder.definition(0).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    protected List<LivingEntity> hitEntity;
    private final AnimationHandler<Weagle> animationHandler = new AnimationHandler<>(this, ANIMS)
            .withChangeListener(anim -> {
                this.hitEntity = null;
                this.setSwoopMotion(null);
                return false;
            });

    public Weagle(EntityType<? extends BaseMonster> type, Level level) {
        super(type, level);
        this.moveControl = new FreeMoveControl(this, 90, 50, FreeMoveControl.TRUE);
        this.setNoGravity(true);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new FloatingFlyNavigator(this, level);
    }

    @Override
    protected void definedAdditinoalSyncedData(SyncedDataContainer.Builder<BaseMonster> builder) {
        super.definedAdditinoalSyncedData(builder);
        builder.define(SWOOP_MOTION, TenshilibSyncableEntityDatas.VEC_3.get(), null);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.FLYING_SPEED).setBaseValue(0.33);
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(32);
        super.applyAttributes();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<BaseMonster>create()
                .start(PECK).play(MonsterBehaviourUtils.requireInRangePlay())
                .condition(MonsterBehaviourUtils.ifCloserThan(3))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(MonsterBehaviourUtils.moveAttack())
                .end(3)
                .start(SWOOP).play(MonsterBehaviourUtils.requireInRangePlay())
                .condition(MonsterBehaviourUtils.ifCloserThan(3))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(MonsterBehaviourUtils.moveAttack())
                .end(2)
                .start(GALE).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetAwayFromTarget<BaseMonster>().minDist(2).radius(4)).prepareOptional(MonsterBehaviourUtils.moveAttack())
                .end(5)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseMonster>builder()
                .add(2, new StrafeTarget<BaseMonster>().strafeDistance(7))
                .add(5, new SetRandomFlyingTarget<BaseMonster>()
                        .flightTargetPredicate((entity, pos) -> {
                            LivingEntity target = BrainUtils.hasMemory(entity, MemoryModuleType.ATTACK_TARGET) ? BrainUtils.getTargetOfEntity(entity) : null;
                            if (target == null) {
                                target = entity.getTarget();
                            }
                            return target != null && target.distanceToSqr(pos) <= 11 * 11 && Math.abs(target.getY() - pos.y()) < 6;
                        }), MonsterBehaviourUtils.moveTo()).build();
    }

    @Override
    protected ExtendedBehaviour<? extends BaseMonster> getWanderBehaviour() {
        return new SetRandomHoverTarget<>();
    }

    @Override
    protected Vec3 directionToLookAt() {
        if (this.getAnimationHandler().isCurrent(SWOOP)) {
            return this.getSwoopMotion();
        }
        return super.directionToLookAt();
    }

    @Override
    public void travel(Vec3 vec) {
        super.handleFreeTravel(vec);
    }

    @Override
    public int animationCooldown(@Nullable String anim) {
        if (anim != null && anim.equals(GALE)) {
            int diffAdd = this.difficultyCooldown();
            return this.getRandom().nextInt(40) + 50 + diffAdd;
        }
        return super.animationCooldown(anim);
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimationState anim, Vec3 target, double grow) {
        if (anim.is(SWOOP))
            return new OrientedBoundingBox(OrientedBoundingBox.originAABB(this)
                    .inflate(0.2)
                    .inflate(grow), this.getYRot(), 0, this.position().add(this.getDeltaMovement()));
        return super.calculateAttackAABB(anim, target, grow);
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double width = this.getBbWidth() * 1.5;
        double length = this.getBbWidth() * 1.7;
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(GALE)) {
            if (anim.isAt("attack")) {
                RuneCraftorySpells.GUST_SPELL.get().use(this);
            }
        } else if (anim.is(SWOOP)) {
            if (this.hitEntity == null)
                this.hitEntity = new ArrayList<>();
            if (this.getSwoopMotion() == null) {
                this.setSwoopMotion(EntityUtils.getTargetDirection(this, EntityAnchorArgument.Anchor.FEET, true)
                        .scale(0.2)
                        .add(0, -0.3, 0));
            }
            if (anim.isPast("swoop_start") && !anim.isPast("swoop_end")) {
                this.setDeltaMovement(this.getSwoopMotion());
                this.mobAttack(anim, null, e -> {
                    if (!this.hitEntity.contains(e)) {
                        this.hitEntity.add(e);
                        this.doHurtTarget(e);
                    }
                });
            } else {
                this.setDeltaMovement(this.getSwoopMotion().multiply(-1, -0.7, -1));
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    public AnimationHandler<Weagle> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 2 ? RuneCraftorySpells.GUST_SPELL.get() : null))
                return;
            switch (command) {
                case 2 -> this.getAnimationHandler().setAnimation(GALE);
                case 1 -> this.getAnimationHandler().setAnimation(SWOOP);
                default -> this.getAnimationHandler().setAnimation(PECK);
            }
        }
    }

    @Override
    protected void checkFallDamage(double dist, boolean groundLogic, BlockState state, BlockPos pos) {
    }

    public Vec3 getSwoopMotion() {
        return this.getDataContainer().get(SWOOP_MOTION);
    }

    public void setSwoopMotion(Vec3 swoopMotion) {
        this.getDataContainer().set(SWOOP_MOTION, swoopMotion);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    @Override
    protected void onFlap() {
        this.playSound(RuneCraftorySounds.ENTITY_WEAGLE_FLAP.get(), this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
    }

    @Override
    public String getInteractAnimation() {
        return INTERACT;
    }

    @Override
    public String getDeathAnimation() {
        return DEFEAT;
    }

    @Override
    public String getSleepAnimation() {
        return SLEEP;
    }
}
