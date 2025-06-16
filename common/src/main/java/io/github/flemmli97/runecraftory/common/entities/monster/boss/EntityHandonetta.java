package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.entities.ai.control.FreeMoveControl;
import io.github.flemmli97.runecraftory.common.entities.ai.pathing.FloatingFlyNavigator;
import io.github.flemmli97.runecraftory.common.entities.data.SyncableDatas;
import io.github.flemmli97.runecraftory.common.entities.data.SyncableEntityData;
import io.github.flemmli97.runecraftory.common.entities.utils.RunecraftoryBossbar;
import io.github.flemmli97.runecraftory.common.network.S2CMobUpdate;
import io.github.flemmli97.runecraftory.common.network.S2CScreenShake;
import io.github.flemmli97.runecraftory.common.registry.ModEffects;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.MathsHelper;
import io.github.flemmli97.tenshilib.common.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.common.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.KeepDistanceRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetAttackRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.StrafingRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.TimedWrappedRunner;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class EntityHandonetta extends BossMonster {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String SWIPE = BUILDER.add("swipe", AnimationsBuilder.definition(1.28).marker("attack", 0.64));
    public static final String FLICK = BUILDER.add("flick", AnimationsBuilder.definition(1.32).marker("attack", 0.64));
    public static final String SHOOT = BUILDER.add("shoot", AnimationsBuilder.definition(1.44).marker("attack", 0.36));
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
    public static final String INTERACT = BUILDER.add("interact", SWIPE);
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private static final ImmutableMap<String, BiConsumer<AnimatedAction, EntityHandonetta>> ATTACK_HANDLER = createAnimationHandler(b -> {
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
                    if (!entity.caughtEntities.contains(e) && CombatUtils.mobAttack(entity, e, new CustomDamage.Builder(entity).hurtResistant(8))) {
                        entity.caughtEntities.add(e);
                        S2CScreenShake.sendAround(entity, 32, 4, 2);
                    }
                });
            }
        });
        b.put(SHOOT, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack"))
                ModSpells.DARK_BULLETS.get().use(entity);
        });
        b.put(LASER, (anim, entity) -> {
            entity.getNavigation().stop();
            if (entity.getTarget() != null && !anim.isPast("aim"))
                entity.setTargetPosition(entity.getTarget());
            if (anim.isAt("attack"))
                ModSpells.DARK_BEAM.get().use(entity);
        });
        b.put(PLATE, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack"))
                ModSpells.PLATE.get().use(entity);
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
                    if (!entity.caughtEntities.contains(e)) {
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
                entity.caughtEntities.forEach(e -> e.removeEffect(ModEffects.TRUE_INVIS.get()));
                entity.caughtEntities.clear();
            }
        });
    });
    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityHandonetta>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.<EntityHandonetta>nonRepeatableAttack(SWIPE)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetAttackRunner<>(1.1), e -> 70 + e.getRandom().nextInt(50)))
                    .withCondition((goal, target, previous) -> goal.attacker.allowAnimation(previous, SWIPE)
                            && (goal.distanceToTargetSq < 16 || goal.attacker.random.nextFloat() < 0.5)), 11),
            WeightedEntry.wrap(MonsterActionUtils.<EntityHandonetta>nonRepeatableAttack(FLICK)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetAttackRunner<>(1.1), e -> 70 + e.getRandom().nextInt(50)))
                    .withCondition((goal, target, previous) -> goal.attacker.allowAnimation(previous, FLICK)
                            && (goal.distanceToTargetSq < 16 || goal.attacker.random.nextFloat() < 0.5)), 11),
            WeightedEntry.wrap(MonsterActionUtils.<EntityHandonetta>nonRepeatableAttack(PUNCH)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetRunner<>(1, 8, true, true, true), e -> 70 + e.getRandom().nextInt(50))), 10),
            WeightedEntry.wrap(MonsterActionUtils.<EntityHandonetta>nonRepeatableAttack(LASER)
                    .prepare(() -> new TimedWrappedRunner<>(new KeepDistanceRunner<>(7, 9, 1), e -> 70 + e.getRandom().nextInt(50))), 10),
            WeightedEntry.wrap(MonsterActionUtils.<EntityHandonetta>nonRepeatableAttack(PLATE)
                    .prepare(() -> new TimedWrappedRunner<>(new KeepDistanceRunner<>(7, 11, 1), e -> 70 + e.getRandom().nextInt(50))), 9),
            WeightedEntry.wrap(MonsterActionUtils.<EntityHandonetta>enragedBossAttack(GRAB)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetRunner<>(1, 5, true, true, true), e -> 70 + e.getRandom().nextInt(50))), 8),
            WeightedEntry.wrap(MonsterActionUtils.<EntityHandonetta>enragedBossAttack(SHOOT)
                    .prepare(() -> new TimedWrappedRunner<>(new KeepDistanceRunner<>(7, 10, 1), e -> 70 + e.getRandom().nextInt(50))), 9)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityHandonetta>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new StrafingRunner<>(10, 8, 0.8f, 0.1f)), 2)
    );

    public final AnimatedAttackGoal<EntityHandonetta> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityHandonetta> animationHandler = new AnimationHandler<>(this, ANIMS)
            .withChangeListener(anim -> {
                this.moveDirection = null;
                if (anim != null) {
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.1));
                }
                return false;
            });

    private final List<LivingEntity> caughtEntities = new ArrayList<>();
    private Vec3 moveDirection;

    public EntityHandonetta(EntityType<? extends EntityHandonetta> type, Level world) {
        super(type, world);
        if (!world.isClientSide)
            this.goalSelector.addGoal(1, this.attack);
        this.setNoGravity(true);
        this.moveControl = new HandonettaMoveController(this);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new FloatingFlyNavigator(this, level);
    }

    @Override
    public RunecraftoryBossbar createBossBar() {
        return new RunecraftoryBossbar(null, this.getDisplayName(), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS)
                .setMusic(ModSounds.HANDONETTA_FIGHT.get());
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        super.checkFallDamage(y, false, state, pos);
    }

    @Override
    public void setEnraged(boolean flag, boolean load) {
        super.setEnraged(flag, load);
        if (flag && !load)
            this.getAnimationHandler().setAnimation(ANGRY);
    }

    @Override
    protected void applyAttributes() {
        super.applyAttributes();
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.26);
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(32);
        this.getAttribute(Attributes.FLYING_SPEED).setBaseValue(0.32);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if ((this.getAnimationHandler().isCurrent(GRAB, GRAB_CAUGHT)) && this.caughtEntities.contains(source.getEntity()))
            return false;
        return (!this.getAnimationHandler().hasAnimation() || !(this.getAnimationHandler().isCurrent(ANGRY))) && super.hurt(source, amount);
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.getAnimationHandler().isCurrent(ANGRY, DEFEAT);
    }

    @Override
    protected Vec3 directionToLookAt() {
        if (this.getAnimationHandler().isCurrent(PUNCH, GRAB)) {
            return this.moveDirection;
        }
        return super.directionToLookAt();
    }

    @Override
    public void push(double x, double y, double z) {
        if (this.getAnimationHandler().isCurrent(ANGRY, DEFEAT))
            return;
        super.push(x, y, z);
    }

    @Override
    public AnimatedAction getDeathAnimation() {
        return DEFEAT;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        BiConsumer<AnimatedAction, EntityHandonetta> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }


    @Override
    public CustomDamage.Builder damageSourceAttack() {
        CustomDamage.Builder builder = super.damageSourceAttack();
        if (this.getAnimationHandler().isCurrent(SWIPE)) {
            builder.knock(CustomDamage.KnockBackType.BACK);
            builder.knockAmount(1.2f);
        }
        if (this.getAnimationHandler().isCurrent(FLICK)) {
            builder.knock(CustomDamage.KnockBackType.UP);
            builder.knockAmount(0.9f);
        }
        return builder;
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimatedAction anim, Vec3 target, double grow) {
        if (anim.is(PUNCH)) {
            float[] rots = MathsHelper.YXRotFrom(this.moveDirection);
            return new OrientedBoundingBox(OrientedBoundingBox.originAABB(this)
                    .inflate(grow + 0.2, grow, grow + 0.2), rots[0], rots[1], this.position());
        }
        if (anim.is(GRAB)) {
            float[] rots = MathsHelper.YXRotFrom(this.moveDirection);
            return new OrientedBoundingBox(OrientedBoundingBox.originAABB(this)
                    .inflate(grow, 0, grow), rots[0], rots[1], this.position());
        }
        return super.calculateAttackAABB(anim, target, grow).inflate(0, grow * 2, 0);
    }

    @Override
    public AABB attackBB(AnimatedAction anim) {
        double width = this.getBbWidth() * 2.2;
        double length = this.getBbWidth() * 1.7;
        if (anim.is(FLICK)) {
            width = this.getBbWidth() * 1.6;
            length = this.getBbWidth() * 1.8;
        }
        return new AABB(-width * 0.5, -0.5, 0, width * 0.5, this.getBbHeight() + 0.5, length);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 2 ? ModSpells.DARK_BULLETS.get() : null))
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
    public void baseTick() {
        super.baseTick();
        if (this.getAnimationHandler().isCurrent(GRAB, GRAB_CAUGHT)) {
            boolean invis = this.getAnimationHandler().isCurrent(GRAB) ? this.getAnimationHandler().getAnimation().isPast("invis_start") : this.getAnimationHandler().isCurrent(GRAB_CAUGHT);
            this.caughtEntities.forEach(e -> {
                if (e.isAlive()) {
                    if (e instanceof ServerPlayer player)
                        player.moveTo(this.getX(), this.getY(), this.getZ());
                    else
                        e.setPos(this.getX(), this.getY(), this.getZ());
                    if (invis)
                        e.addEffect(new MobEffectInstance(ModEffects.TRUE_INVIS.get(), 10, 1, true, false, false));
                }
            });
        }
    }

    private void catchEntity(LivingEntity entity) {
        this.caughtEntities.add(entity);
    }

    @Override
    public void push(Entity entityIn) {
        if (this.getAnimationHandler().isCurrent(PUNCH, GRAB))
            return;
        super.push(entityIn);
    }

    @Override
    public boolean isOnGround() {
        return super.isOnGround();
    }

    @Override
    public void travel(Vec3 vec) {
        this.handleFreeTravel(vec);
    }

    @Override
    public AnimationHandler<EntityHandonetta> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public Vec3 passengerOffset(Entity passenger) {
        return new Vec3(0, 23.25 / 16d, -6 / 16d);
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    protected void setMoveDirection(Vec3 moveDirection) {
        this.moveDirection = moveDirection;
        S2CMobUpdate.send(this, SyncableDatas.MOTION_DIR, this.moveDirection);
    }

    @Override
    public void onUpdate(SyncableEntityData.SyncedContainer<?> data) {
        super.onUpdate(data);
        data.runIf(SyncableDatas.MOTION_DIR, motion -> this.moveDirection = motion);
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
                Vec3 target = this.mob.getTarget().position();
                Vec3 dist = this.mob.position().subtract(target);
                if (dist.y() < 4) {
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
