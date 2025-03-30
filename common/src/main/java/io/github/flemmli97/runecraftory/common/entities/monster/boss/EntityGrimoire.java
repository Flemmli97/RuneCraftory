package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.entities.data.SyncableDatas;
import io.github.flemmli97.runecraftory.common.entities.data.SyncableEntityData;
import io.github.flemmli97.runecraftory.common.entities.utils.RunecraftoryBossbar;
import io.github.flemmli97.runecraftory.common.network.S2CMobUpdate;
import io.github.flemmli97.runecraftory.common.network.S2CScreenShake;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveAwayRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetAttackRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.TimedWrappedRunner;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import io.github.flemmli97.tenshilib.common.utils.OrientedBoundingBox;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class EntityGrimoire extends BossMonster {

    private static final List<Vector3f> CIRCLE_PARTICLE_MOTION = RayTraceUtils.rotatedVecs(new Vec3(0.25, 0, 0), new Vec3(0, 1, 0), -180, 175, 5);

    public static final AnimatedAction TAIL_SWIPE = AnimatedAction.builder(0.84, "tail_swipe").marker("attack", 0.48).build();
    public static final AnimatedAction BITE = AnimatedAction.builder(0.8, "bite").marker("attack", 0.44).build();
    public static final AnimatedAction GUST = AnimatedAction.builder(1.96, "gust").marker("attack", 0.32).build();
    public static final AnimatedAction CHARGE = AnimatedAction.builder(1.72, "charge").infinite()
            .marker("charge_start", 0.16).marker("charge_end", 1.6).build();
    public static final AnimatedAction CHARGE_LAND = AnimatedAction.builder(0.48, "charge_land").marker("attack", 0.16).build();
    public static final AnimatedAction WIND_BREATH = AnimatedAction.builder(1.36, "wind_breath").marker("attack", 0.44).build();
    public static final AnimatedAction TORNADO = AnimatedAction.builder(1.24, "tornado").marker("attack", 0.4).build();
    public static final AnimatedAction DEFEAT = AnimatedAction.builder(10, "defeat").infinite().build();
    public static final AnimatedAction ANGRY = new AnimatedAction(1.44, "angry");
    public static final AnimatedAction SLEEP = new AnimatedAction(0, "sleep");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(TAIL_SWIPE, "interact");
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{TAIL_SWIPE, BITE, GUST, CHARGE, CHARGE_LAND, WIND_BREATH, TORNADO, DEFEAT, ANGRY, INTERACT};

    private static final ImmutableMap<String, BiConsumer<AnimatedAction, EntityGrimoire>> ATTACK_HANDLER = createAnimationHandler(b -> {
        BiConsumer<AnimatedAction, EntityGrimoire> melee = (anim, entity) -> {
            if (anim.isAt("attack")) {
                entity.mobAttack(anim, entity.getTarget(), entity::doHurtTarget);
            }
        };
        b.put(TAIL_SWIPE, melee);
        b.put(BITE, melee);
        b.put(GUST, (anim, entity) -> {
            if (anim.isAt("attack")) {
                ModSpells.GUST_ROCKS.get().use(entity);
            }
        });
        b.put(WIND_BREATH, (anim, entity) -> {
            if (anim.isAt("attack")) {
                ModSpells.WIND_BLADE_BARRAGE.get().use(entity);
            }
        });
        b.put(TORNADO, (anim, entity) -> {
            if (anim.isAt("attack")) {
                ModSpells.TORNADO.get().use(entity);
            }
        });
        b.put(CHARGE, (anim, entity) -> {
            if (anim.isPast("charge_start") && !anim.isPast("charge_end")) {
                if (entity.hitEntity == null) {
                    entity.hitEntity = new ArrayList<>();
                }
                if (entity.moveDirection == null) {
                    entity.setMoveDirection(EntityUtils.getTargetDirection(entity, EntityAnchorArgument.Anchor.FEET, true)
                            .scale(0.35));
                }
                entity.setDeltaMovement(entity.moveDirection);
                entity.mobAttack(anim, null, e -> {
                    if (!entity.hitEntity.contains(e) && CombatUtils.mobAttack(entity, e,
                            new CustomDamage.Builder(entity).hurtResistant(5).knock(CustomDamage.KnockBackType.BACK).knockAmount(2))) {
                        entity.hitEntity.add(e);
                    }
                });
            }
            if (anim.isPast("charge_end")) {
                entity.setDeltaMovement(entity.getDeltaMovement().add(0, -0.06, 0));
                if (entity.getDeltaMovement().y < -0.72) {
                    entity.setDeltaMovement(entity.getDeltaMovement().x, -0.72, entity.getDeltaMovement().z);
                }
                if (entity.isOnGround()) {
                    entity.getAnimationHandler().setAnimation(CHARGE_LAND);
                }
                // Stuck check. Or e.g. if in water
                if (anim.isPast(6) && (!entity.getFeetBlockState().is(Blocks.AIR) || !entity.getBlockStateOn().is(Blocks.AIR))) {
                    entity.getAnimationHandler().setAnimation(CHARGE_LAND);
                }
            }
        });
        b.put(CHARGE_LAND, (anim, entity) -> {
            if (anim.isAt("attack")) {
                CustomDamage.Builder source = new CustomDamage.Builder(entity).noKnockback().element(EnumElement.WIND).hurtResistant(5);
                entity.mobAttack(anim, entity.getTarget(), e -> CombatUtils.mobAttack(entity, e, source));
                S2CScreenShake.sendAround(entity, 24, 4, 3);
                entity.level.playSound(null, entity.blockPosition(), SoundEvents.GENERIC_EXPLODE, entity.getSoundSource(), 1.0f, 0.9f);
                entity.level.broadcastEntityEvent(entity, (byte) 66);
            }
        });
    });

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityGrimoire>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.<EntityGrimoire>nonRepeatableAttack(TAIL_SWIPE)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetAttackRunner<>(1.1), e -> 40 + e.getRandom().nextInt(15))), 10),
            WeightedEntry.wrap(MonsterActionUtils.<EntityGrimoire>nonRepeatableAttack(BITE)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetAttackRunner<>(1.1), e -> 40 + e.getRandom().nextInt(15))), 10),
            WeightedEntry.wrap(MonsterActionUtils.<EntityGrimoire>nonRepeatableAttack(GUST)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveAwayRunner<>(4, 1, 7), e -> 40 + e.getRandom().nextInt(15))), 9),
            WeightedEntry.wrap(MonsterActionUtils.<EntityGrimoire>nonRepeatableAttack(CHARGE)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetRunner<>(1, 6), e -> 40 + e.getRandom().nextInt(15))), 8),
            WeightedEntry.wrap(MonsterActionUtils.<EntityGrimoire>nonRepeatableAttack(WIND_BREATH)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveAwayRunner<>(4, 1, 6), e -> 40 + e.getRandom().nextInt(20))), 8),
            WeightedEntry.wrap(MonsterActionUtils.<EntityGrimoire>enragedBossAttack(TORNADO)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveAwayRunner<>(3, 1, 6), e -> 40 + e.getRandom().nextInt(15))), 10)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityGrimoire>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 1.5)), 1)
    );

    public final AnimatedAttackGoal<EntityGrimoire> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityGrimoire> animationHandler = new AnimationHandler<>(this, ANIMS).withChangeListener(anim -> {
        if (CHARGE.is(anim)) {
            this.hitEntity = null;
        }
        if (!this.level.isClientSide && anim == null) {
            boolean chain = !this.commanded;
            this.setMoveDirection(null);
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
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.29);
        super.applyAttributes();
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
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
    protected Vec3 directionToLookAt() {
        if (this.getAnimationHandler().isCurrent(CHARGE, CHARGE_LAND)) {
            return this.moveDirection;
        }
        return super.directionToLookAt();
    }

    @Override
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);
        if (id == 66) {
            for (Vector3f vec : CIRCLE_PARTICLE_MOTION) {
                this.level.addParticle(new ColoredParticleData(ModParticles.WIND.get(), 67 / 255F, 163 / 255F, 65 / 255F, 1, 0.4f), this.getX(), this.getY() + 0.2, this.getZ(), vec.x(), vec.y(), vec.z());
            }
        }
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        BiConsumer<AnimatedAction, EntityGrimoire> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimatedAction anim, Vec3 target, double grow) {
        if (anim.is(CHARGE_LAND))
            return new OrientedBoundingBox(OrientedBoundingBox.originAABB(this).inflate(1.2, 0.1, 1.2), this.getYRot(), 0, this.position());
        if (anim.is(CHARGE)) {
            double width = this.getBbWidth();
            double speed = Math.max(width, this.getDeltaMovement().length() - width);
            return new OrientedBoundingBox(OrientedBoundingBox.originAABB(this)
                    .inflate(grow + 0.5, 0.1, grow + 0.5).expandTowards(0, 0, speed), this.getYRot(), 0, this.position());
        }
        return super.calculateAttackAABB(anim, target, grow);
    }

    @Override
    public AABB attackBB(AnimatedAction anim) {
        double width = this.getBbWidth() * 1.4;
        double length = this.getBbWidth() * 1.5;
        if (anim.is(TAIL_SWIPE)) {
            width = this.getBbWidth() * 1.5;
            length = this.getBbWidth() * 1.45;
            return new AABB(-width * 0.65, -0.02, 0, width * 0.35, this.getBbHeight() + 0.02, length);
        }
        if (anim.is(BITE)) {
            width = this.getBbWidth() * 1.1;
            length = this.getBbWidth() * 1.4;
        }
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
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

    protected void setMoveDirection(Vec3 moveDirection) {
        this.moveDirection = moveDirection;
        S2CMobUpdate.send(this, SyncableDatas.MOTION_DIR, this.moveDirection);
    }

    @Override
    public void onUpdate(SyncableEntityData.SyncedContainer<?> data) {
        super.onUpdate(data);
        data.runIf(SyncableDatas.MOTION_DIR, motion -> this.moveDirection = motion);
    }
}
