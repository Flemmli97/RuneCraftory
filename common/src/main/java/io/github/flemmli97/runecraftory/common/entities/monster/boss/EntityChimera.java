package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.RestrictedWaterAvoidingStrollGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.entities.data.SyncableDatas;
import io.github.flemmli97.runecraftory.common.entities.data.SyncableEntityData;
import io.github.flemmli97.runecraftory.common.entities.utils.RunecraftoryBossbar;
import io.github.flemmli97.runecraftory.common.network.S2CMobUpdate;
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
import io.github.flemmli97.tenshilib.common.utils.MathUtils;
import io.github.flemmli97.tenshilib.common.utils.OrientedBoundingBox;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class EntityChimera extends BossMonster {

    public static final AnimatedAction LEAP = AnimatedAction.builder(1.36, "leap").marker("attack_start", 0).marker("attack_end", 1.2).build();
    public static final AnimatedAction FIRE_TAIL_BUBBLE = AnimatedAction.builder(1.48, "tail_beam").marker("attack", 0.44).build();
    public static final AnimatedAction WATER_TAIL_BUBBLE = AnimatedAction.copyOf(FIRE_TAIL_BUBBLE, "water_tail_bubble");
    public static final AnimatedAction WATER_TAIL_BEAM = AnimatedAction.copyOf(FIRE_TAIL_BUBBLE, "water_tail_beam");
    public static final AnimatedAction FIRE_BREATH = AnimatedAction.builder(1.2, "breath_attack").marker("attack", 0.4).build();
    public static final AnimatedAction BUBBLE_BEAM = AnimatedAction.copyOf(FIRE_BREATH, "bubble_beam");
    public static final AnimatedAction SLASH = AnimatedAction.builder(0.64, "claw_attack").marker("attack", 0.36, 0.72).build();
    public static final AnimatedAction BITE = AnimatedAction.builder(1.04, "bite_attack").marker("attack_1", 0.4).marker("attack_2", 0.72).build();
    public static final AnimatedAction ANGRY = new AnimatedAction(1.04, "angry");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(0, "sleep").infinite().build();
    public static final AnimatedAction DEFEAT = AnimatedAction.builder(10, "defeat").infinite().build();
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(SLASH, "interact");
    private static final AnimatedAction[] ANIMATED_ACTIONS = new AnimatedAction[]{LEAP, FIRE_TAIL_BUBBLE, WATER_TAIL_BUBBLE, WATER_TAIL_BEAM, FIRE_BREATH, BUBBLE_BEAM, SLASH, BITE, DEFEAT, INTERACT, ANGRY, SLEEP};

    private static final ImmutableMap<String, BiConsumer<AnimatedAction, EntityChimera>> ATTACK_HANDLER = createAnimationHandler(b -> {
        BiConsumer<AnimatedAction, EntityChimera> summonFire = (anim, entity) -> {
            if (anim.isAt("attack")) {
                ModSpells.FIREBALL_BARRAGE.get().use(entity);
            }
        };
        b.put(FIRE_TAIL_BUBBLE, summonFire);
        b.put(FIRE_BREATH, summonFire);
        BiConsumer<AnimatedAction, EntityChimera> summonWater = (anim, entity) -> {
            if (anim.isAt("attack")) {
                ModSpells.BUBBLE_BEAM.get().use(entity);
            }
        };
        b.put(BUBBLE_BEAM, summonWater);
        b.put(WATER_TAIL_BUBBLE, summonWater);
        b.put(WATER_TAIL_BEAM, (anim, entity) -> {
            if (anim.isAt("attack")) {
                ModSpells.WATER_LASER.get().use(entity);
            }
        });
        b.put(LEAP, (anim, entity) -> {
            if (anim.isPast("attack_start") && !anim.isPast("attack_end")) {
                if (entity.hitEntity == null)
                    entity.hitEntity = new ArrayList<>();

                if (entity.chargeMotion == null) {
                    Vec3 dir = EntityUtils.getTargetDirection(entity, EntityAnchorArgument.Anchor.FEET, true)
                            .scale(0.4);
                    entity.setChargeMotion(new Vec3(dir.x, 0, dir.z));
                }
                entity.setDeltaMovement(entity.chargeMotion.x, entity.getDeltaMovement().y, entity.chargeMotion.z);
                entity.mobAttack(anim, null, e -> {
                    if (!entity.hitEntity.contains(e) && CombatUtils.mobAttack(entity, e,
                            new CustomDamage.Builder(entity).hurtResistant(5).knock(CustomDamage.KnockBackType.UP))) {
                        entity.chargeAttackSuccess = true;
                        entity.hitEntity.add(e);
                    }
                });
            }
        });
        b.put(SLASH, (anim, entity) -> {
            if (anim.isAt("attack")) {
                ModSpells.SLASH.get().use(entity);
            }
        });
        b.put(BITE, (anim, entity) -> {
            if (anim.isAt("attack_1")) {
                entity.mobAttack(anim, entity.getTarget(), e -> CombatUtils.mobAttack(entity, e,
                        new CustomDamage.Builder(entity).hurtResistant(5).knockAmount(0)));
            } else if (anim.isAt("attack_2")) {
                entity.mobAttack(anim, entity.getTarget(), entity::doHurtTarget);
            }
        });
    });

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityChimera>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.<EntityChimera>nonRepeatableAttack(LEAP)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetRunner<>(1.1, 4), e -> 30 + e.getRandom().nextInt(15))), 10),
            WeightedEntry.wrap(MonsterActionUtils.<EntityChimera>nonRepeatableAttack(FIRE_TAIL_BUBBLE)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetRunner<>(1.1, 6), e -> 30 + e.getRandom().nextInt(15))), 8),
            WeightedEntry.wrap(MonsterActionUtils.<EntityChimera>nonRepeatableAttack(WATER_TAIL_BUBBLE)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetRunner<>(1.1, 6), e -> 30 + e.getRandom().nextInt(15))), 8),
            WeightedEntry.wrap(MonsterActionUtils.<EntityChimera>nonRepeatableAttack(WATER_TAIL_BEAM)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetRunner<>(1.1, 6), e -> 30 + e.getRandom().nextInt(15))), 8),
            WeightedEntry.wrap(MonsterActionUtils.<EntityChimera>nonRepeatableAttack(FIRE_BREATH)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetRunner<>(1.1, 6), e -> 30 + e.getRandom().nextInt(15))), 8),
            WeightedEntry.wrap(MonsterActionUtils.<EntityChimera>nonRepeatableAttack(BUBBLE_BEAM)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetRunner<>(1.1, 6), e -> 30 + e.getRandom().nextInt(15))), 8),
            WeightedEntry.wrap(MonsterActionUtils.<EntityChimera>nonRepeatableAttack(SLASH)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetAttackRunner<>(1.1), e -> 30 + e.getRandom().nextInt(15))), 10)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityChimera>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1.1, 1)), 9),
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveAwayRunner<>(4, 1, 6)), 10)
    );

    public final AnimatedAttackGoal<EntityChimera> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityChimera> animationHandler = new AnimationHandler<>(this, ANIMATED_ACTIONS)
            .withChangeListener(anim -> {
                if (!this.level.isClientSide) {
                    if (anim == null) {
                        this.setChargeMotion(null);
                    }
                    if (this.getAnimationHandler().isCurrent(LEAP)) {
                        this.hitEntity = null;
                        if (this.chargeAttackSuccess) {
                            this.chargeAttackSuccess = false;
                            this.getAnimationHandler().setAnimation(BITE);
                            return true;
                        }
                    }
                    return false;
                }
                return false;
            });

    protected boolean chargeAttackSuccess;
    private Vec3 chargeMotion;
    protected List<LivingEntity> hitEntity;

    public EntityChimera(EntityType<? extends EntityChimera> type, Level world) {
        super(type, world);
        if (!world.isClientSide)
            this.goalSelector.addGoal(1, this.attack);
        this.maxUpStep = 1;
    }


    @Override
    public RunecraftoryBossbar createBossBar() {
        return new RunecraftoryBossbar(null, this.getDisplayName(), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS)
                .setMusic(ModSounds.CHIMERA_FIGHT.get());
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.31);
        super.applyAttributes();
    }

    @Override
    public void addGoal() {
        super.addGoal();
        this.goalSelector.removeGoal(this.wander);
        this.wander = new RestrictedWaterAvoidingStrollGoal(this, 0.6);
        this.goalSelector.addGoal(6, this.wander);
    }

    @Override
    public double sprintSpeedThreshold() {
        return 0.9;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return (!this.getAnimationHandler().hasAnimation() || !(this.getAnimationHandler().isCurrent(DEFEAT, ANGRY))) && super.hurt(source, amount);
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.getAnimationHandler().isCurrent(ANGRY, DEFEAT);
    }

    @Override
    public void push(double x, double y, double z) {
        if (this.getAnimationHandler().isCurrent(ANGRY, DEFEAT, LEAP))
            return;
        super.push(x, y, z);
    }

    @Override
    public AnimatedAction getDeathAnimation() {
        return DEFEAT;
    }

    @Override
    public AABB attackBB(AnimatedAction anim) {
        double width = this.getBbWidth() * 1.6;
        double length = this.getBbWidth() * 1.7;
        if (anim.is(BITE)) {
            width = this.getBbWidth() * 1.2;
            length = this.getBbWidth() * 1.3;
        }
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimatedAction anim, Vec3 target, double grow) {
        if (anim.is(LEAP)) {
            return new OrientedBoundingBox(OrientedBoundingBox.originAABB(this)
                    .inflate(0.3, 0.1, 0.3 + this.getDeltaMovement().scale(0.3).length()), this.getYRot(), 0, this.position());
        }
        if (!anim.is(BITE)) {
            return super.calculateAttackAABB(anim, target, grow);
        }
        double reach = this.getBbWidth() * 0.9;
        Vec3 dir;
        float offset = anim.isAt("attack") ? 45 : -5;
        if (target != null && !this.canBeControlledByRider()) {
            reach = Math.min(reach, this.position().distanceTo(target));
            dir = MathUtils.rotate(MathUtils.NORMAL_Y, target.subtract(this.position()).normalize(), offset * Mth.DEG_TO_RAD);
        } else {
            if (this.getControllingPassenger() instanceof Player player)
                dir = Vec3.directionFromRotation(player.getXRot(), player.getYRot() + offset);
            else
                dir = Vec3.directionFromRotation(this.getXRot(), this.getYRot() + offset);
        }
        Vec3 attackPos = this.position().add(dir.scale(reach));
        return new OrientedBoundingBox(this.attackBB(anim).inflate(grow, 0, grow), this.getYRot() + 45, 0, attackPos);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        BiConsumer<AnimatedAction, EntityChimera> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2) {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), ModSpells.BUBBLE_BEAM.get()))
                    this.getAnimationHandler().setAnimation(BUBBLE_BEAM);
            } else if (command == 1) {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), ModSpells.SLASH.get()))
                    this.getAnimationHandler().setAnimation(SLASH);
            } else {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                    this.getAnimationHandler().setAnimation(LEAP);
            }
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.ENTITY_CHIMERA_AMBIENT.get();
    }

    @Override
    public double ridingSpeedModifier() {
        return 1.5;
    }

    @Override
    protected Vec3 directionToLookAt() {
        if (this.getAnimationHandler().isCurrent(LEAP)) {
            return this.chargeMotion;
        }
        return super.directionToLookAt();
    }

    @Override
    public void setEnraged(boolean flag, boolean load) {
        super.setEnraged(flag, load);
        if (flag && !load)
            this.getAnimationHandler().setAnimation(ANGRY);
    }

    @Override
    public Vec3 passengerOffset(Entity passenger) {
        return new Vec3(0, 22.75 / 16d, -5 / 16d);
    }

    public void setChargeMotion(Vec3 charge) {
        this.chargeMotion = charge;
        S2CMobUpdate.send(this, SyncableDatas.MOTION_DIR, this.chargeMotion);
    }

    @Override
    public AnimationHandler<EntityChimera> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public AnimatedAction getSleepAnimation() {
        return SLEEP;
    }

    @Override
    public void onUpdate(SyncableEntityData.SyncedContainer<?> data) {
        super.onUpdate(data);
        data.runIf(SyncableDatas.MOTION_DIR, motion -> this.chargeMotion = motion);
    }
}