package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.RunecraftoryBossbar;
import io.github.flemmli97.runecraftory.common.entities.ai.NearestTargetHorizontal;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.entities.ai.pathing.FloatingFlyNavigator;
import io.github.flemmli97.runecraftory.common.registry.ModEffects;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.runecraftory.common.utils.MathsHelper;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.KeepDistanceRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetAttackRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.StrafingRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.TimedWrappedRunner;
import io.github.flemmli97.tenshilib.common.utils.OrientedBoundingBox;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class EntityHandonetta extends BossMonster {


    public static final AnimatedAction SWIPE = new AnimatedAction(1.28, 0.64, "swipe");
    public static final AnimatedAction FLICK = new AnimatedAction(1.32, 0.64, "flick");
    public static final AnimatedAction SHOOT = new AnimatedAction(1.56, 0.52, "shoot");
    public static final AnimatedAction LASER = new AnimatedAction(1.36, 0.64, "laser");
    public static final AnimatedAction PLATE = new AnimatedAction(0.88, 0.56, "plate");
    public static final AnimatedAction GRAB = new AnimatedAction(1.2, 0.48, "grab");
    public static final AnimatedAction GRAB_CAUGHT = new AnimatedAction(1.96, 0.12, "grab_caught");
    public static final AnimatedAction PUNCH = new AnimatedAction(1.2, 0.28, "punch");
    public static final AnimatedAction DEFEAT = AnimatedAction.builder(204, "defeat").marker(150).infinite().build();
    public static final AnimatedAction ANGRY = new AnimatedAction(28, 0, "angry");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(SWIPE, "interact");

    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{SWIPE, FLICK, SHOOT, LASER, PLATE, GRAB, GRAB_CAUGHT, PUNCH, DEFEAT, ANGRY, INTERACT};
    private static final ImmutableMap<String, BiConsumer<AnimatedAction, EntityHandonetta>> ATTACK_HANDLER = createAnimationHandler(b -> {
        b.put(SWIPE, (anim, entity) -> {
            LivingEntity target = entity.getTarget();
            entity.getNavigation().stop();
            if (target != null) {
                if (anim.isAtTick(1)) {
                    entity.targetPosition = target.position();
                }
            }
            if (anim.canAttack()) {
                entity.mobAttack(anim, target, entity::doHurtTarget);
            }
        });
        b.put(FLICK, (anim, entity) -> {
            LivingEntity target = entity.getTarget();
            entity.getNavigation().stop();
            if (target != null) {
                if (anim.isAtTick(1)) {
                    entity.targetPosition = target.position();
                }
            }
            if (anim.canAttack()) {
                entity.mobAttack(anim, target, entity::doHurtTarget);
            }
        });
        b.put(PUNCH, (anim, entity) -> {
            entity.getNavigation().stop();
            if (entity.aiVarHelper == null) {
                Vec3 dir = entity.getTarget() != null ? entity.getTarget().position().subtract(entity.position()) : entity.getLookAngle();
                dir = dir.normalize().scale(0.75);
                entity.aiVarHelper = dir;
                entity.caughtEntities.clear();
            }
            entity.setDeltaMovement(entity.aiVarHelper);
            if (anim.isPastTick(anim.getAttackTime())) {
                entity.mobAttack(anim, null, e -> {
                    if (!entity.caughtEntities.contains(e) && CombatUtils.mobAttack(entity, e, new CustomDamage.Builder(entity).hurtResistant(8))) {
                        entity.caughtEntities.add(e);
                    }
                });
            }
        });
        b.put(SHOOT, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.canAttack())
                ModSpells.DARK_BULLETS.get().use(entity);
        });
        b.put(LASER, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.canAttack())
                ModSpells.DARK_BEAM.get().use(entity);
        });
        b.put(PLATE, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.canAttack())
                ModSpells.PLATE.get().use(entity);
        });
        b.put(GRAB, (anim, entity) -> {
            entity.getNavigation().stop();
            if (entity.aiVarHelper == null) {
                Vec3 dir = entity.getTarget() != null ? entity.getTarget().position().subtract(entity.position()) : entity.getLookAngle();
                dir = dir.normalize().scale(0.45);
                entity.aiVarHelper = dir;
            }
            entity.setDeltaMovement(entity.aiVarHelper);
            if (anim.isPastTick(1.)) {
                if (!entity.caughtEntities.isEmpty())
                    entity.getAnimationHandler().setAnimation(GRAB_CAUGHT);
            } else if (anim.isPastTick(anim.getAttackTime()) && !anim.isPastTick(1.)) {
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
            if (anim.canAttack() || anim.isAtTick(0.6)
                    || anim.isAtTick(1.04) || anim.isAtTick(1.48)) {
                entity.caughtEntities.forEach(entity::doHurtTarget);
            }
            if (anim.isPastTick(1.8) && !entity.caughtEntities.isEmpty()) {
                entity.caughtEntities.forEach(e -> e.removeEffect(ModEffects.TRUE_INVIS.get()));
                entity.caughtEntities.clear();
            }
        });
    });
    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityHandonetta>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.<EntityHandonetta>nonRepeatableAttack(SWIPE)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetAttackRunner<>(1.1), e -> 60 + e.getRandom().nextInt(20))), 10),
            WeightedEntry.wrap(MonsterActionUtils.<EntityHandonetta>nonRepeatableAttack(FLICK)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetAttackRunner<>(1.1), e -> 60 + e.getRandom().nextInt(20))), 10),
            WeightedEntry.wrap(MonsterActionUtils.<EntityHandonetta>nonRepeatableAttack(SHOOT)
                    .prepare(() -> new TimedWrappedRunner<>(new KeepDistanceRunner<>(6, 9, 1), e -> 35 + e.getRandom().nextInt(20))), 10),
            WeightedEntry.wrap(MonsterActionUtils.<EntityHandonetta>nonRepeatableAttack(LASER)
                    .prepare(() -> new TimedWrappedRunner<>(new KeepDistanceRunner<>(5, 7, 1), e -> 35 + e.getRandom().nextInt(20))), 10),
//            WeightedEntry.wrap(MonsterActionUtils.<EntityHandonetta>nonRepeatableAttack(PLATE)
//                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetRunner<>(1, 7), e -> 35 + e.getRandom().nextInt(20))), 10),
            WeightedEntry.wrap(MonsterActionUtils.<EntityHandonetta>nonRepeatableAttack(GRAB)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetRunner<>(1, 5, true, true, true), e -> 35 + e.getRandom().nextInt(20))), 10),
            WeightedEntry.wrap(MonsterActionUtils.<EntityHandonetta>nonRepeatableAttack(PUNCH)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetRunner<>(1, 8, true, true, true), e -> 35 + e.getRandom().nextInt(20))), 10)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityHandonetta>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new KeepDistanceRunner<>(4, 8, 1)), 1),
            WeightedEntry.wrap(new IdleAction<>(() -> new StrafingRunner<EntityHandonetta>(10, 8, 1, 0.3f))
                    .withCondition((goal, target) -> goal.distanceToTargetSq < 36 && goal.distanceToTargetSq > 12), 2)
    );

    public final AnimatedAttackGoal<EntityHandonetta> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityHandonetta> animationHandler = new AnimationHandler<>(this, ANIMS)
            .setAnimationChangeFunc(anim -> {
                this.aiVarHelper = null;
                if (anim != null) {
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.1));
                }
                return false;
            });

    private final List<LivingEntity> caughtEntities = new ArrayList<>();
    private Vec3 aiVarHelper;

    public EntityHandonetta(EntityType<? extends EntityHandonetta> type, Level world) {
        super(type, world);
        if (!world.isClientSide)
            this.goalSelector.addGoal(1, this.attack);
        this.setNoGravity(true);
        this.moveControl = new HandMoveController(this);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new FloatingFlyNavigator(this, level);
    }

    @Override
    public RunecraftoryBossbar createBossBar() {
        return new RunecraftoryBossbar(null, this.getDisplayName(), BossEvent.BossBarColor.PINK, BossEvent.BossBarOverlay.PROGRESS)
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
        this.getAttribute(Attributes.FLYING_SPEED).setBaseValue(0.75);
    }

    @Override
    protected NearestAttackableTargetGoal<Player> createTargetGoalPlayer() {
        return new NearestTargetHorizontal<>(this, Player.class, 16, true, true, player -> !this.isTamed());
    }

    @Override
    protected NearestAttackableTargetGoal<Mob> createTargetGoalMobs() {
        return new NearestTargetHorizontal<>(this, Mob.class, 16, true, true, this.targetPred);
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
    public void tick() {
        super.tick();
        if (!this.level.isClientSide && this.getAnimationHandler().isCurrent(PUNCH, GRAB)) {
            float[] yxRot = MathsHelper.YXRotFrom(this.getDeltaMovement());
            this.setYRot(yxRot[0]);
            this.setXRot(yxRot[1]);
        }
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
        LivingEntity target = this.getTarget();
        if (target != null) {
            if (!anim.is(GRAB_CAUGHT))
                this.lookAtNow(target, 60.0f, 50.0f);
        }
        BiConsumer<AnimatedAction, EntityHandonetta> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimatedAction anim, Vec3 target, double grow) {
        return super.calculateAttackAABB(anim, target, grow);
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
            this.caughtEntities.forEach(e -> {
                if (e.isAlive()) {
                    if (e instanceof ServerPlayer player)
                        player.moveTo(this.getX(), this.getY(), this.getZ());
                    else
                        e.setPos(this.getX(), this.getY(), this.getZ());
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
        return this.isAlive() && super.isOnGround();
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

    @Override
    public boolean isNoGravity() {
        return this.isAlive() && super.isNoGravity();
    }

    static class HandMoveController extends FlyingMoveControl {

        public HandMoveController(Mob mob) {
            super(mob, 90, true);
        }

        @Override
        public void tick() {
            if (this.operation == Operation.STRAFE) {
                float f = (float) this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
                float g = (float) this.speedModifier * f;
                float h = this.strafeForwards;
                float i = this.strafeRight;
                float j = Mth.sqrt(h * h + i * i);
                if (j < 1.0F) {
                    j = 1.0F;
                }

                j = g / j;
                h *= j;
                i *= j;
                float k = Mth.sin(this.mob.getYRot() * 0.017453292F);
                float l = Mth.cos(this.mob.getYRot() * 0.017453292F);
                float m = h * l - i * k;

                this.operation = MoveControl.Operation.WAIT;
            } else {
                super.tick();
            }
            LivingEntity target = this.mob.getTarget();
            if (target != null && target.distanceToSqr(this.mob) < 24 * 24) {
                this.mob.lookAt(target, 60.0F, 30.0F);
            }
        }
    }
}
