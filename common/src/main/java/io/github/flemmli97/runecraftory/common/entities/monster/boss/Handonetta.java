package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.SetWalkTargetWithinDist;
import io.github.flemmli97.runecraftory.common.entities.ai.control.FreeMoveControl;
import io.github.flemmli97.runecraftory.common.entities.ai.pathing.FloatingFlyNavigator;
import io.github.flemmli97.runecraftory.common.entities.data.SyncableDatas;
import io.github.flemmli97.runecraftory.common.entities.data.SyncableEntityData;
import io.github.flemmli97.runecraftory.common.entities.utils.RunecraftoryBossbar;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.network.S2CMobUpdate;
import io.github.flemmli97.runecraftory.common.network.S2CScreenShake;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.MathsHelper;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.MoveToAttackTarget;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.StrafeTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class Handonetta extends BossMonster {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String SWIPE = BUILDER.add("swipe", AnimationsBuilder.definition(1.28).marker("attack", 0.64));
    public static final String INTERACT = BUILDER.add("interact", SWIPE);
    public static final String FLICK = BUILDER.add("flick", AnimationsBuilder.definition(1.32).marker("attack", 0.64));
    public static final String SHOOT = BUILDER.add("shoot", AnimationsBuilder.definition(1.52).marker("attack", 0.44));
    public static final String LASER = BUILDER.add("laser", AnimationsBuilder.definition(1.24)
            .marker("aim", 0.3).marker("attack", 0.4));
    public static final String PLATE = BUILDER.add("plate", AnimationsBuilder.definition(0.88).marker("attack", 0.56));
    public static final String GRAB = BUILDER.add("grab", AnimationsBuilder.definition(1.2)
            .marker("attack", 0.56).marker("invis_start", 0.72).marker("grab_done", 1.04));
    public static final String GRAB_CAUGHT = BUILDER.add("grab_caught", AnimationsBuilder.definition(1.96)
            .marker("attack", 0.12, 0.6, 1.04, 1.48).marker("attack_end", 1.8));
    public static final String PUNCH = BUILDER.add("punch", AnimationsBuilder.definition(1.2)
            .marker("attack_start", 0.28).marker("attack_end", 1.04));
    public static final String DEFEAT = BUILDER.add("defeat", AnimationsBuilder.definition(10).infinite());
    public static final String ANGRY = BUILDER.add("angry", AnimationsBuilder.definition(1.56));
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private static final ImmutableMap<String, BiConsumer<AnimationState, Handonetta>> ATTACK_HANDLER = createAnimationHandler(b -> {
        b.put(SWIPE, (anim, entity) -> {
            LivingEntity target = entity.getTarget();
            entity.getNavigation().stop();
            if (anim.isAt("attack")) {
                entity.mobAttack(anim, target, entity::doHurtTarget);
            }
        });
        b.put(FLICK, (anim, entity) -> {
            LivingEntity target = entity.getTarget();
            entity.getNavigation().stop();
            if (anim.isAt("attack")) {
                entity.mobAttack(anim, target, entity::doHurtTarget);
            }
        });
        b.put(PUNCH, (anim, entity) -> {
            entity.getNavigation().stop();
            if (entity.moveDirection == null) {
                entity.setMoveDirection(EntityUtils.getTargetDirection(entity, EntityAnchorArgument.Anchor.FEET)
                        .scale(0.75));
                entity.caughtEntities.clear();
            }
            entity.setDeltaMovement(entity.moveDirection);
            if (anim.isPast("attack_start") && !anim.isPast("attack_end")) {
                entity.mobAttack(anim, null, e -> {
                    if (!entity.caughtEntities.contains(e) && CombatUtils.mobAttack(entity, e, new DynamicDamage.Builder(entity).hurtResistant(8))) {
                        entity.caughtEntities.add(e);
                        S2CScreenShake.sendAround(entity, 32, 4, 2);
                    }
                });
            }
        });
        b.put(SHOOT, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack"))
                RuneCraftorySpells.DARK_BULLETS.get().use(entity);
        });
        b.put(LASER, (anim, entity) -> {
            entity.getNavigation().stop();
            if (entity.getTarget() != null && !anim.isPast("aim"))
                entity.setTargetPosition(entity.getTarget());
            if (anim.isAt("attack"))
                RuneCraftorySpells.DARK_BEAM.get().use(entity);
        });
        b.put(PLATE, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack"))
                RuneCraftorySpells.PLATE.get().use(entity);
        });
        b.put(GRAB, (anim, entity) -> {
            entity.getNavigation().stop();
            if (entity.moveDirection == null) {
                entity.setMoveDirection(EntityUtils.getTargetDirection(entity, EntityAnchorArgument.Anchor.FEET)
                        .scale(0.45));
            }
            entity.setDeltaMovement(entity.moveDirection);
            if (anim.isPast("grab_done")) {
                if (!entity.caughtEntities.isEmpty())
                    entity.getAnimationHandler().setAnimation(GRAB_CAUGHT);
            } else if (anim.isPast("attack") && !anim.isPast("grab_done")) {
                entity.mobAttack(anim, null, e -> {
                    if (!entity.getType().is(RunecraftoryTags.EntityTypes.HANDONETTA_GRAP_IGNORE) && (entity.getBbWidth() < 5 || entity.getBbHeight() < 8) && !entity.caughtEntities.contains(e)) {
                        entity.catchEntity(e);
                    }
                });
            }
        });
        b.put(GRAB_CAUGHT, (anim, entity) -> {
            entity.getNavigation().stop();
            entity.setDeltaMovement(entity.getDeltaMovement().scale(0.1));
            if (anim.isAt("attack")) {
                entity.caughtEntities.forEach(entity::doHurtTarget);
                S2CScreenShake.sendAround(entity, 24, 4, 1);
            }
            if (anim.isPast("attack_end") && !entity.caughtEntities.isEmpty()) {
                Platform.INSTANCE.getEntityData(entity).setInvis(0);
                entity.caughtEntities.clear();
            }
        });
    });

    private final AnimationHandler<Handonetta> animationHandler = new AnimationHandler<>(this, ANIMS)
            .withChangeListener(anim -> {
                this.moveDirection = null;
                if (anim != null) {
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.1));
                }
                return false;
            });
    private final List<LivingEntity> caughtEntities = new ArrayList<>();
    private Vec3 moveDirection;

    public Handonetta(EntityType<? extends Handonetta> type, Level level) {
        super(type, level);
        this.setNoGravity(true);
        this.moveControl = new HandonettaMoveController(this);
    }

    @Override
    public RunecraftoryBossbar createBossBar() {
        return new RunecraftoryBossbar(RuneCraftoryEntities.HANDONETTA.getID(), this.getDisplayName(), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS)
                .setMusic(RuneCraftorySounds.HANDONETTA_FIGHT.get());
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new FloatingFlyNavigator(this, level);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.26);
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(32);
        this.getAttribute(Attributes.FLYING_SPEED).setBaseValue(0.32);
        super.applyAttributes();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<Handonetta>create()
                .start(MonsterBehaviourUtils.checkedAttack(SWIPE)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(MonsterBehaviourUtils.ifCloserThan(5))
                .prepare(new SetWalkTargetToAttackTarget<Handonetta>().speedMod((e, t) -> 1.1f)
                        .closeEnoughDist(MonsterBehaviourUtils.closeEnough(4)))
                .prepareOptional(MonsterBehaviourUtils.fastMovement())
                .end(11)
                .start(MonsterBehaviourUtils.checkedAttack(FLICK)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(MonsterBehaviourUtils.ifCloserThan(5))
                .prepare(new SetWalkTargetToAttackTarget<Handonetta>().speedMod((e, t) -> 1.1f)
                        .closeEnoughDist(MonsterBehaviourUtils.closeEnough(4)))
                .prepareOptional(MonsterBehaviourUtils.fastMovement())
                .end(11)
                .start(MonsterBehaviourUtils.checkedAttack(PUNCH)).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepareOptional(new MoveToAttackTarget<>())
                .end(10)
                .start(MonsterBehaviourUtils.checkedAttack(LASER)).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetWithinDist<Handonetta>().min(4).max(12))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(10)
                .start(MonsterBehaviourUtils.checkedAttack(PLATE)).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetWithinDist<Handonetta>().min(3).max(14))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(9)
                .start(MonsterBehaviourUtils.checkedAttack(GRAB)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(BossMonster::isEnraged)
                .prepare(new SetWalkTargetToAttackTarget<Handonetta>().closeEnoughDist(MonsterBehaviourUtils.closeEnough(7)))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(8)
                .start(MonsterBehaviourUtils.checkedAttack(SHOOT)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(BossMonster::isEnraged)
                .prepare(new SetWalkTargetWithinDist<Handonetta>().min(3).max(11))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(8)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseMonster>builder()
                .add(1, new StrafeTarget<BaseMonster>().strafeDistance(12)).build();
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.getAnimationHandler().isCurrent(ANGRY, DEFEAT);
    }

    @Override
    public void travel(Vec3 vec) {
        this.handleFreeTravel(vec);
    }

    @Override
    public void push(Entity entityIn) {
        if (this.getAnimationHandler().isCurrent(PUNCH, GRAB))
            return;
        super.push(entityIn);
    }

    @Override
    public void push(double x, double y, double z) {
        if (this.getAnimationHandler().isCurrent(ANGRY, DEFEAT))
            return;
        super.push(x, y, z);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.getAnimationHandler().isCurrent(GRAB, GRAB_CAUGHT)) {
            boolean invis = this.getAnimationHandler().isCurrent(GRAB) ? this.getAnimationHandler().getAnimation().isPast("invis_start") : this.getAnimationHandler().isCurrent(GRAB_CAUGHT);
            this.caughtEntities.forEach(entity -> {
                if (entity.isAlive()) {
                    if (entity instanceof ServerPlayer player) {
                        Vec3 dir = this.position().subtract(player.position());
                        player.setDeltaMovement(dir);
                        player.moveTo(this.getX(), this.getY(), this.getZ());
                    } else {
                        entity.setDeltaMovement(Vec3.ZERO);
                        entity.setPos(this.getX(), this.getY(), this.getZ());
                    }
                    if (invis)
                        Platform.INSTANCE.getEntityData(entity).setInvis(10);
                    entity.hurtMarked = true;
                }
            });
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if ((this.getAnimationHandler().isCurrent(GRAB, GRAB_CAUGHT)) && this.caughtEntities.contains(source.getEntity()))
            return false;
        return (!this.getAnimationHandler().hasAnimation() || !(this.getAnimationHandler().isCurrent(ANGRY))) && super.hurt(source, amount);
    }

    @Override
    protected Vec3 directionToLookAt() {
        if (this.getAnimationHandler().isCurrent(PUNCH, GRAB)) {
            return this.moveDirection;
        }
        return super.directionToLookAt();
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimationState anim, Vec3 target, double grow) {
        Vec3 dir = this.moveDirection;
        if (dir == null) {
            if (target != null)
                dir = target.subtract(this.position());
            else
                dir = this.getLookAngle();
        }
        if (anim.is(PUNCH)) {
            float[] rots = MathsHelper.YXRotFrom(dir);
            return new OrientedBoundingBox(OrientedBoundingBox.originAABB(this)
                    .inflate(grow + 0.2, grow, grow + 1.2), rots[0], rots[1], this.position());
        }
        if (anim.is(GRAB)) {
            float[] rots = MathsHelper.YXRotFrom(dir);
            return new OrientedBoundingBox(OrientedBoundingBox.originAABB(this)
                    .inflate(grow + 0.5, 0, grow + 1), rots[0], rots[1], this.position());
        }
        return super.calculateAttackAABB(anim, target, grow).inflate(0, grow * 2, 0);
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double width = this.getBbWidth() * 2.2;
        double length = this.getBbWidth() * 1.7;
        if (anim.is(FLICK)) {
            width = this.getBbWidth() * 1.6;
            length = this.getBbWidth() * 1.8;
        }
        return new AABB(-width * 0.5, -0.5, 0, width * 0.5, this.getBbHeight() + 0.5, length);
    }

    @Override
    public void handleAttack(AnimationState anim) {
        BiConsumer<AnimationState, Handonetta> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    @Override
    public DynamicDamage.Builder damageSourceAttack() {
        DynamicDamage.Builder builder = super.damageSourceAttack();
        if (this.getAnimationHandler().isCurrent(SWIPE)) {
            builder.knock(DynamicDamage.KnockBackType.BACK, 1.2f);
        }
        if (this.getAnimationHandler().isCurrent(FLICK)) {
            builder.knock(DynamicDamage.KnockBackType.UP, 0.9f);
        }
        return builder;
    }

    @Override
    public AnimationHandler<Handonetta> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 2 ? RuneCraftorySpells.DARK_BULLETS.get() : null))
                return;
            if (command == 2)
                this.getAnimationHandler().setAnimation(SHOOT);
            else if (command == 1)
                this.getAnimationHandler().setAnimation(FLICK);
            else
                this.getAnimationHandler().setAnimation(SWIPE);
        }
    }

    @Override
    public void setEnraged(boolean flag, boolean load) {
        super.setEnraged(flag, load);
        if (flag && !load)
            this.getAnimationHandler().setAnimation(ANGRY);
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        super.checkFallDamage(y, false, state, pos);
    }

    private void catchEntity(LivingEntity entity) {
        this.caughtEntities.add(entity);
    }

    protected void setMoveDirection(Vec3 moveDirection) {
        this.moveDirection = moveDirection;
        S2CMobUpdate.send(this, SyncableDatas.VEC_3, this.moveDirection);
    }

    @Override
    public void onUpdate(SyncableEntityData.SyncedContainer<?> data) {
        super.onUpdate(data);
        data.runIf(SyncableDatas.VEC_3, motion -> this.moveDirection = motion);
    }

    @Override
    public String getInteractAnimation() {
        return INTERACT;
    }

    @Override
    public String getDeathAnimation() {
        return DEFEAT;
    }

    static class HandonettaMoveController extends FreeMoveControl {

        public HandonettaMoveController(Mob mob) {
            super(mob);
        }

        @Override
        public void tick() {
            Operation current = this.operation;
            super.tick();
            if (current == Operation.STRAFE && this.mob.getTarget() != null) {
                Vec3 target = this.mob.getTarget().position().add(0, this.mob.getTarget().getBbHeight(), 0);
                Vec3 dist = this.mob.position().subtract(target);
                if (dist.y() < 3) {
                    this.mob.setYya(1);
                } else {
                    this.mob.setYya(-1);
                }
                if (dist.horizontalDistanceSqr() < 16) {
                    this.mob.setSpeed(this.mob.getSpeed() * 2);
                    this.mob.setZza(-1);
                    this.mob.setXxa(this.mob.xxa * 0.5f);
                }
            }
        }
    }
}
