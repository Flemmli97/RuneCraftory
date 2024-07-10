package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.RunecraftoryBossbar;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MoveToTargetAttackRunner;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveAwayRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.TimedWrappedRunner;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class EntityGrimoire extends BossMonster {

    protected static final EntityDataAccessor<Float> LOCKED_YAW = SynchedEntityData.defineId(EntityGrimoire.class, EntityDataSerializers.FLOAT);

    public static final AnimatedAction TAIL_SWIPE = new AnimatedAction(0.84, 0.48, "tail_swipe");
    public static final AnimatedAction BITE = new AnimatedAction(0.8, 0.44, "bite");
    public static final AnimatedAction GUST = new AnimatedAction(1.96, 0.32, "gust");
    public static final AnimatedAction CHARGE = new AnimatedAction(1.72, 0.16, "charge");
    public static final AnimatedAction WIND_BREATH = new AnimatedAction(1.36, 0.44, "wind_breath");
    public static final AnimatedAction TORNADO = new AnimatedAction(1.24, 0.4, "tornado");
    public static final AnimatedAction DEFEAT = AnimatedAction.builder(150, "defeat").marker(150).infinite().build();
    public static final AnimatedAction ANGRY = new AnimatedAction(1.44, 0, "angry");
    public static final AnimatedAction SLEEP = new AnimatedAction(0.2, 0, "sleep");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(TAIL_SWIPE, "interact");
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{TAIL_SWIPE, BITE, GUST, CHARGE, WIND_BREATH, TORNADO, DEFEAT, ANGRY, INTERACT};

    private static final ImmutableMap<String, BiConsumer<AnimatedAction, EntityGrimoire>> ATTACK_HANDLER = createAnimationHandler(b -> {
        BiConsumer<AnimatedAction, EntityGrimoire> melee = (anim, entity) -> {
            if (anim.canAttack()) {
                entity.mobAttack(anim, entity.getTarget(), entity::doHurtTarget);
            }
        };
        b.put(TAIL_SWIPE, melee);
        b.put(BITE, melee);
        b.put(GUST, (anim, entity) -> {
            if (anim.canAttack()) {
                ModSpells.GUST_ROCKS.get().use(entity);
            }
        });
        b.put(WIND_BREATH, (anim, entity) -> {
            if (anim.canAttack()) {
                ModSpells.WIND_BLADE_BARRAGE.get().use(entity);
            }
        });
        b.put(TORNADO, (anim, entity) -> {
            if (anim.canAttack()) {
                ModSpells.TORNADO.get().use(entity);
            }
        });
        b.put(CHARGE, (anim, entity) -> {
            if (anim.isPastTick(0.16) && !anim.isPastTick(1.6)) {
                if (entity.moveDirection == null) {
                    Vec3 dir = entity.getTarget() != null ? entity.getTarget().position().subtract(entity.position()) : entity.getLookAngle();
                    dir = new Vec3(dir.x(), 0, dir.z()).normalize().scale(7);
                    int length = anim.getLength();
                    entity.moveDirection = new Vec3(dir.x / length, 0, dir.z / length);
                }
                if (entity.hitEntity == null) {
                    entity.hitEntity = new ArrayList<>();
                }
                entity.setDeltaMovement(entity.moveDirection);
                entity.mobAttack(anim, null, e -> {
                    if (!entity.hitEntity.contains(e) && CombatUtils.mobAttack(entity, e,
                            new CustomDamage.Builder(entity).hurtResistant(5).knock(CustomDamage.KnockBackType.UP), CombatUtils.getAttributeValue(entity, Attributes.ATTACK_DAMAGE))) {
                        entity.hitEntity.add(e);
                    }
                });
            }
        });
    });

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityGrimoire>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.<EntityGrimoire>nonRepeatableAttack(TAIL_SWIPE)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetAttackRunner<>(1.1), e -> 40 + e.getRandom().nextInt(15))), 10),
            WeightedEntry.wrap(MonsterActionUtils.<EntityGrimoire>nonRepeatableAttack(BITE)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetAttackRunner<>(1.1), e -> 40 + e.getRandom().nextInt(15))), 10),
            WeightedEntry.wrap(MonsterActionUtils.<EntityGrimoire>nonRepeatableAttack(GUST)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveAwayRunner<>(3, 1, 6), e -> 40 + e.getRandom().nextInt(15))), 9),
            WeightedEntry.wrap(MonsterActionUtils.<EntityGrimoire>nonRepeatableAttack(CHARGE)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetRunner<>(1, 6), e -> 40 + e.getRandom().nextInt(15))), 8),
            WeightedEntry.wrap(MonsterActionUtils.<EntityGrimoire>nonRepeatableAttack(WIND_BREATH)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveAwayRunner<>(4, 1, 6), e -> 40 + e.getRandom().nextInt(20))), 8),
            WeightedEntry.wrap(MonsterActionUtils.<EntityGrimoire>enragedBossAttack(TORNADO)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveAwayRunner<>(3, 1, 6), e -> 40 + e.getRandom().nextInt(15))), 10)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityGrimoire>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 3)), 1)
    );

    public final AnimatedAttackGoal<EntityGrimoire> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityGrimoire> animationHandler = new AnimationHandler<>(this, ANIMS).setAnimationChangeFunc(anim -> {
        if (CHARGE.is(anim)) {
            this.hitEntity = null;
            if (!this.level.isClientSide) {
                if (this.isVehicle()) {
                    this.entityData.set(LOCKED_YAW, this.getControllingPassenger().getYHeadRot());
                } else if (this.getTarget() != null) {
                    this.lookAt(this.getTarget(), 360, 10);
                    this.entityData.set(LOCKED_YAW, this.getYRot());
                }
            }
        }
        if (!this.level.isClientSide && anim == null) {
            boolean chain = !this.commanded;
            this.moveDirection = null;
            this.commanded = false;
            if (chain) {
                if (this.isEnraged() && this.getAnimationHandler().isCurrent(BITE)) {
                    this.getAnimationHandler().setAnimation(TAIL_SWIPE);
                    return true;
                }
            }
        }
        return false;
    });

    private boolean commanded;
    protected List<LivingEntity> hitEntity;
    private Vec3 moveDirection;

    public EntityGrimoire(EntityType<? extends EntityGrimoire> type, Level world) {
        super(type, world);
        if (!world.isClientSide)
            this.goalSelector.addGoal(1, this.attack);
    }

    @Override
    public RunecraftoryBossbar createBossBar() {
        return new RunecraftoryBossbar(null, this.getDisplayName(), BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.PROGRESS)
                .setMusic(ModSounds.GRIMOIRE_FIGHT.get());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LOCKED_YAW, 0f);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.29);
        super.applyAttributes();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getAnimationHandler().isCurrent(CHARGE)) {
            this.setXRot(0);
            this.setYRot(this.entityData.get(LOCKED_YAW));
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return (!this.getAnimationHandler().isCurrent(ANGRY)) && super.hurt(source, amount);
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.getAnimationHandler().isCurrent(ANGRY, DEFEAT);
    }

    @Override
    public void push(double x, double y, double z) {
        if (this.getAnimationHandler().isCurrent(ANGRY, DEFEAT, CHARGE))
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
            this.lookAt(target, 180.0f, 50.0f);
        }
        BiConsumer<AnimatedAction, EntityGrimoire> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }


    @Override
    public AABB calculateAttackAABB(AnimatedAction anim, Vec3 target, double grow) {
        if (anim.is(CHARGE)) {
            return this.getBoundingBox().inflate(0.5, 0.1, 0.5);
        }
        return super.calculateAttackAABB(anim, target, grow);
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 2.5;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2) {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), ModSpells.WIND_BLADE_BARRAGE.get()))
                    this.getAnimationHandler().setAnimation(WIND_BREATH);
            } else if (command == 1) {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                    this.getAnimationHandler().setAnimation(TAIL_SWIPE);
            } else if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                this.getAnimationHandler().setAnimation(BITE);
            this.commanded = true;
        }
    }

    @Override
    public void setEnraged(boolean flag, boolean load) {
        super.setEnraged(flag, load);
        if (flag && !load)
            this.getAnimationHandler().setAnimation(ANGRY);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    @Override
    public Vec3 passengerOffset(Entity passenger) {
        return new Vec3(0, 39 / 16d, 11.5 / 16d).scale(1.5);
    }

    @Override
    public AnimationHandler<EntityGrimoire> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean allowAnimation(String prev, AnimatedAction other) {
        if (prev.equals(BITE.getID()))
            return !this.isEnraged() || !TAIL_SWIPE.is(other);
        return super.allowAnimation(prev, other);
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public AnimatedAction getSleepAnimation() {
        return SLEEP;
    }
}
