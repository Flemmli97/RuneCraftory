package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.entities.data.SyncableDatas;
import io.github.flemmli97.runecraftory.common.entities.data.SyncableEntityData;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityMarionettaTrap;
import io.github.flemmli97.runecraftory.common.entities.utils.RunecraftoryBossbar;
import io.github.flemmli97.runecraftory.common.network.S2CMobUpdate;
import io.github.flemmli97.runecraftory.common.registry.ModEffects;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.common.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.DoNothingRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.JumpEvadeAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.KeepDistanceRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetAttackRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.StrafingRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.TimedWrappedRunner;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class EntityMarionetta extends BossMonster {

    private static final EntityDataAccessor<Boolean> CAUGHT = SynchedEntityData.defineId(EntityMarionetta.class, EntityDataSerializers.BOOLEAN);

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String MELEE = BUILDER.add("melee", AnimationsBuilder.definition(0.48).marker("attack", 0.28));
    public static final String SPIN = BUILDER.add("spin", AnimationsBuilder.definition(1.52)
            .marker("attack_start", 0.28).marker("attack_end", 1.4));
    public static final String CARD_ATTACK = BUILDER.add("card_attack", AnimationsBuilder.definition(0.64).marker("attack", 0.36));
    public static final String CHEST_ATTACK = BUILDER.add("chest_attack", AnimationsBuilder.definition(1.2)
            .marker("attack_start", 0.28).marker("attack_end", 1));
    public static final String CHEST_THROW = BUILDER.add("chest_throw", AnimationsBuilder.definition(5).marker("attack", 0.28));
    public static final String STUFFED_ANIMALS = BUILDER.add("stuffed_animals", AnimationsBuilder.definition(0.76).marker("attack", 0.44));
    public static final String DARK_BEAM = BUILDER.add("dark_beam", AnimationsBuilder.definition(0.8).marker("attack", 0.36));
    public static final String FURNITURE = BUILDER.add("furniture", AnimationsBuilder.definition(1.2).marker("attack", 0.4));
    public static final String DEFEAT = BUILDER.add("defeat", AnimationsBuilder.definition(10).infinite());
    public static final String ANGRY = BUILDER.add("angry", AnimationsBuilder.definition(1.2));
    public static final String INTERACT = BUILDER.add("interact", MELEE);
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private static final ImmutableMap<String, BiConsumer<AnimatedAction, EntityMarionetta>> ATTACK_HANDLER = createAnimationHandler(b -> {
        b.put(MELEE, (anim, entity) -> {
            LivingEntity target = entity.getTarget();
            if (target != null) {
                entity.getNavigation().moveTo(target, 1.0);
            }
            if (anim.isAt("attack")) {
                entity.mobAttack(anim, target, entity::doHurtTarget);
            }
        });
        b.put(SPIN, (anim, entity) -> {
            entity.getNavigation().stop();
            if (entity.moveDirection == null) {
                entity.setMoveDirection(EntityUtils.getTargetDirection(entity, EntityAnchorArgument.Anchor.FEET, true)
                        .scale(0.5));
            }
            entity.setDeltaMovement(entity.moveDirection);
            if (anim.isPast("attack_start") && !anim.isPast("attack_end")) {
                entity.mobAttack(anim, null, e -> CombatUtils.mobAttack(entity, e, new CustomDamage.Builder(entity).hurtResistant(8)));
            }
        });
        b.put(CARD_ATTACK, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack"))
                ModSpells.CARD_THROW.get().use(entity);
        });
        b.put(CHEST_ATTACK, (anim, entity) -> {
            entity.getNavigation().stop();
            if (entity.moveDirection == null) {
                entity.setMoveDirection(EntityUtils.getTargetDirection(entity, EntityAnchorArgument.Anchor.FEET, true)
                        .scale(0.5));
            }
            entity.setDeltaMovement(entity.moveDirection);
            if (anim.isPast("attack_start") && !anim.isPast("attack_end")) {
                entity.mobAttack(anim, null, e -> {
                    if (!entity.caughtEntities.contains(e)) {
                        entity.catchEntity(e);
                    }
                });
            }
        });
        b.put(CHEST_THROW, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack")) {
                Vec3 throwVec = new Vec3(entity.getLookAngle().x(), 0, entity.getLookAngle().z())
                        .normalize().scale(1.2).add(0, 0.85, 0);
                EntityMarionettaTrap trap = new EntityMarionettaTrap(entity.level(), entity);
                trap.setDamageMultiplier(0.9f);
                entity.caughtEntities.forEach(e -> {
                    e.addEffect(new MobEffectInstance(ModEffects.TRUE_INVIS.get(), 100, 1, true, false, false));
                    trap.addCaughtEntity(e);
                });
                trap.setDeltaMovement(throwVec);
                entity.level().addFreshEntity(trap);
                entity.caughtEntities.clear();
            }
        });
        b.put(STUFFED_ANIMALS, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack"))
                ModSpells.PLUSH_THROW.get().use(entity);
        });
        b.put(DARK_BEAM, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack") && !EntityUtils.sealed(entity))
                ModSpells.DARK_BEAM.get().use(entity);
        });
        b.put(FURNITURE, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack") && !EntityUtils.sealed(entity))
                ModSpells.FURNITURE.get().use(entity);
        });
    });
    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityMarionetta>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.<EntityMarionetta>nonRepeatableAttack(MELEE)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetAttackRunner<>(1.1), e -> 30 + e.getRandom().nextInt(20))), 10),
            WeightedEntry.wrap(MonsterActionUtils.<EntityMarionetta>nonRepeatableAttack(SPIN)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetRunner<>(1.1, 6.5), e -> 35 + e.getRandom().nextInt(20))), 10),
            WeightedEntry.wrap(MonsterActionUtils.<EntityMarionetta>nonRepeatableAttack(CARD_ATTACK)
                    .prepare(() -> new TimedWrappedRunner<>(new JumpEvadeAction<>(2, 0.9, 0.5f, 0, 0.5f, new DoNothingRunner<>(true)), e -> 5)), 10),
            WeightedEntry.wrap(MonsterActionUtils.<EntityMarionetta>nonRepeatableAttack(CHEST_ATTACK)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetRunner<>(1.1, 6.5), e -> 35 + e.getRandom().nextInt(20))), 10),
            WeightedEntry.wrap(MonsterActionUtils.<EntityMarionetta>nonRepeatableAttack(STUFFED_ANIMALS)
                    .prepare(() -> new TimedWrappedRunner<>(new KeepDistanceRunner<>(3, 7, 1.2), e -> 30 + e.getRandom().nextInt(20))), 10),
            WeightedEntry.wrap(MonsterActionUtils.<EntityMarionetta>enragedBossAttack(DARK_BEAM)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetRunner<>(1.1, 6), e -> 30 + e.getRandom().nextInt(20))), 10),
            WeightedEntry.wrap(MonsterActionUtils.<EntityMarionetta>enragedBossAttack(FURNITURE)
                    .prepare(() -> new TimedWrappedRunner<>(new DoNothingRunner<>(true), e -> 5)), 1)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityMarionetta>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new JumpEvadeAction<>(2, 0.9, 0.5f, 0.015f, 0.4f, new StrafingRunner<>(7, 1))), 10),
            WeightedEntry.wrap(new IdleAction<>(() -> new StrafingRunner<>(7, 1)), 8)
    );

    public final AnimatedAttackGoal<EntityMarionetta> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityMarionetta> animationHandler = new AnimationHandler<>(this, ANIMS)
            .withChangeListener(anim -> {
                this.moveDirection = null;
                if (this.entityData.get(CAUGHT)) {
                    if (!this.level().isClientSide) {
                        this.entityData.set(CAUGHT, false);
                        this.getAnimationHandler().setAnimation(CHEST_THROW);
                    }
                    return true;
                }
                return false;
            });

    private final List<LivingEntity> caughtEntities = new ArrayList<>();
    private Vec3 moveDirection;

    public EntityMarionetta(EntityType<? extends EntityMarionetta> type, Level world) {
        super(type, world);
        if (!world.isClientSide)
            this.goalSelector.addGoal(1, this.attack);
    }


    @Override
    public RunecraftoryBossbar createBossBar() {
        return new RunecraftoryBossbar(null, this.getDisplayName(), BossEvent.BossBarColor.PINK, BossEvent.BossBarOverlay.PROGRESS)
                .setMusic(ModSounds.MARIONETTA_FIGHT.get());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CAUGHT, false);
    }

    @Override
    public void setEnraged(boolean flag, boolean load) {
        super.setEnraged(flag, load);
        if (flag && !load)
            this.getAnimationHandler().setAnimation(ANGRY);
    }

    public boolean caughtTarget() {
        return this.entityData.get(CAUGHT);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.26);
        super.applyAttributes();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.caughtEntities.contains(source.getEntity()))
            return false;
        return (!this.getAnimationHandler().hasAnimation() || !(this.getAnimationHandler().isCurrent(CHEST_THROW, ANGRY))) && super.hurt(source, amount);
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.getAnimationHandler().isCurrent(ANGRY, DEFEAT);
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
    protected Vec3 directionToLookAt() {
        if (this.getAnimationHandler().isCurrent(SPIN, CHEST_ATTACK)) {
            return this.moveDirection;
        }
        return super.directionToLookAt();
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        BiConsumer<AnimatedAction, EntityMarionetta> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimatedAction anim, Vec3 target, double grow) {
        if (anim.is(SPIN)) {
            float rotY = -Mth.wrapDegrees((float) (Mth.atan2(this.moveDirection.x(), this.moveDirection.z()) * Mth.RAD_TO_DEG));
            return new OrientedBoundingBox(OrientedBoundingBox.originAABB(this)
                    .inflate(grow + 1.6, 0.1, grow + 1.6), rotY, 0, this.position());
        }
        if (anim.is(CHEST_ATTACK)) {
            float rotY = -Mth.wrapDegrees((float) (Mth.atan2(this.moveDirection.x(), this.moveDirection.z()) * Mth.RAD_TO_DEG));
            return new OrientedBoundingBox(OrientedBoundingBox.originAABB(this)
                    .inflate(grow + 1.2, 0.1, grow + 1.2), rotY, 0, this.position());
        }
        return super.calculateAttackAABB(anim, target, grow);
    }

    @Override
    public AABB attackBB(AnimatedAction anim) {
        double width = this.getBbWidth() * 1.5;
        double length = this.getBbWidth() * 1.7;
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 2 ? ModSpells.CARD_THROW.get() : null))
                return;
            if (command == 2)
                this.getAnimationHandler().setAnimation(CARD_ATTACK);
            else if (command == 1)
                this.getAnimationHandler().setAnimation(SPIN);
            else
                this.getAnimationHandler().setAnimation(MELEE);
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();
        this.caughtEntities.forEach(e -> {
            if (e.isAlive()) {
                if (e instanceof ServerPlayer player)
                    player.moveTo(this.getX(), this.getY() + this.getBbHeight() + 0.2, this.getZ());
                else
                    e.setPos(this.getX(), this.getY() + this.getBbHeight() + 0.2, this.getZ());
            }
        });
    }

    private void catchEntity(LivingEntity entity) {
        this.caughtEntities.add(entity);
        this.entityData.set(CAUGHT, true);
    }

    @Override
    public void push(Entity entityIn) {
        if (this.getAnimationHandler().isCurrent(SPIN, CHEST_ATTACK))
            return;
        super.push(entityIn);
    }

    @Override
    public AnimationHandler<EntityMarionetta> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public Vec3 passengerOffset(Entity passenger) {
        return new Vec3(0, 17.25 / 16d, -6 / 16d);
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
}
