package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.RunecraftoryBossbar;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MoveToTargetAttackRunner;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityMarionettaTrap;
import io.github.flemmli97.runecraftory.common.registry.ModEffects;
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
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.DoNothingRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.EvadingRangedRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.JumpEvadeAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.StrafingRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.TimedWrappedRunner;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
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

    public static final AnimatedAction MELEE = new AnimatedAction(10, 5, "melee");
    public static final AnimatedAction SPIN = new AnimatedAction(31, 6, "spin");
    public static final AnimatedAction CARD_ATTACK = new AnimatedAction(13, 9, "card_attack");
    public static final AnimatedAction CHEST_ATTACK = new AnimatedAction(24, 6, "chest_attack");
    public static final AnimatedAction CHEST_THROW = new AnimatedAction(105, 7, "chest_throw");
    public static final AnimatedAction STUFFED_ANIMALS = new AnimatedAction(15, 9, "stuffed_animals");
    public static final AnimatedAction DARK_BEAM = new AnimatedAction(16, 6, "dark_beam");
    public static final AnimatedAction FURNITURE = new AnimatedAction(24, 8, "furniture");
    public static final AnimatedAction DEFEAT = AnimatedAction.builder(204, "defeat").marker(150).infinite().build();
    public static final AnimatedAction ANGRY = new AnimatedAction(28, 0, "angry");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(MELEE, "interact");

    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{MELEE, SPIN, CARD_ATTACK, CHEST_ATTACK, CHEST_THROW, STUFFED_ANIMALS, DARK_BEAM, FURNITURE, DEFEAT, ANGRY, INTERACT};
    private static final ImmutableMap<String, BiConsumer<AnimatedAction, EntityMarionetta>> ATTACK_HANDLER = createAnimationHandler(b -> {
        b.put(MELEE, (anim, entity) -> {
            LivingEntity target = entity.getTarget();
            if (target != null) {
                entity.getNavigation().moveTo(target, 1.0);
                if (anim.getTick() == 1) {
                    entity.targetPosition = target.position();
                }
            }
            if (anim.canAttack()) {
                entity.mobAttack(anim, target, entity::doHurtTarget);
            }
        });
        b.put(SPIN, (anim, entity) -> {
            entity.getNavigation().stop();
            if (entity.aiVarHelper == null) {
                Vec3 dir = entity.getTarget() != null ? entity.getTarget().position().subtract(entity.position()) : entity.getLookAngle();
                dir = new Vec3(dir.x(), 0, dir.z()).normalize().scale(8d / anim.getLength());
                entity.aiVarHelper = dir;
            }
            entity.setDeltaMovement(entity.aiVarHelper);
            if (anim.getTick() >= anim.getAttackTime()) {
                entity.mobAttack(anim, null, e -> CombatUtils.mobAttack(entity, e, new CustomDamage.Builder(entity).hurtResistant(8)));
            }
        });
        b.put(CARD_ATTACK, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.canAttack())
                ModSpells.CARD_THROW.get().use(entity);
        });
        b.put(CHEST_ATTACK, (anim, entity) -> {
            entity.getNavigation().stop();
            if (entity.aiVarHelper == null) {
                Vec3 dir = entity.getTarget() != null ? entity.getTarget().position().subtract(entity.position()) : entity.getLookAngle();
                dir = new Vec3(dir.x(), 0, dir.z()).normalize().scale(8d / anim.getLength());
                entity.aiVarHelper = dir;
            }
            entity.setDeltaMovement(entity.aiVarHelper);
            if (anim.getTick() >= anim.getAttackTime()) {
                entity.mobAttack(anim, null, e -> {
                    if (!entity.caughtEntities.contains(e)) {
                        entity.catchEntity(e);
                    }
                });
            }
        });
        b.put(CHEST_THROW, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.canAttack()) {
                Vec3 throwVec = new Vec3(entity.getLookAngle().x(), 0, entity.getLookAngle().z())
                        .normalize().scale(1.2).add(0, 0.85, 0);
                EntityMarionettaTrap trap = new EntityMarionettaTrap(entity.level, entity);
                trap.setDamageMultiplier(0.9f);
                entity.caughtEntities.forEach(e -> {
                    e.addEffect(new MobEffectInstance(ModEffects.TRUE_INVIS.get(), 100, 1, true, false, false));
                    trap.addCaughtEntity(e);
                });
                trap.setDeltaMovement(throwVec);
                entity.level.addFreshEntity(trap);
                entity.caughtEntities.clear();
            }
        });
        b.put(STUFFED_ANIMALS, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.canAttack())
                ModSpells.PLUSH_THROW.get().use(entity);
        });
        b.put(DARK_BEAM, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.canAttack() && !EntityUtils.sealed(entity))
                ModSpells.DARK_BEAM.get().use(entity);
        });
        b.put(FURNITURE, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.canAttack() && !EntityUtils.sealed(entity))
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
                    .prepare(() -> new TimedWrappedRunner<>(new EvadingRangedRunner<>(7, 3, 1.2), e -> 30 + e.getRandom().nextInt(20))), 10),
            WeightedEntry.wrap(MonsterActionUtils.<EntityMarionetta>enragedBossAttack(DARK_BEAM)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetRunner<>(1.1, 6, true, true), e -> 30 + e.getRandom().nextInt(20))), 10),
            WeightedEntry.wrap(MonsterActionUtils.<EntityMarionetta>enragedBossAttack(FURNITURE)
                    .prepare(() -> new TimedWrappedRunner<>(new DoNothingRunner<>(true), e -> 5)), 1)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityMarionetta>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new JumpEvadeAction<>(2, 0.9, 0.5f, 0.015f, 0.4f, new StrafingRunner<>(7, 1))), 10),
            WeightedEntry.wrap(new IdleAction<>(() -> new StrafingRunner<>(7, 1)), 8)
    );

    public final AnimatedAttackGoal<EntityMarionetta> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityMarionetta> animationHandler = new AnimationHandler<>(this, ANIMS)
            .setAnimationChangeFunc(anim -> {
                this.aiVarHelper = null;
                if (this.entityData.get(CAUGHT)) {
                    if (!this.level.isClientSide) {
                        this.entityData.set(CAUGHT, false);
                        this.getAnimationHandler().setAnimation(CHEST_THROW);
                    }
                    return true;
                }
                return false;
            });

    private final List<LivingEntity> caughtEntities = new ArrayList<>();
    private Vec3 aiVarHelper;

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
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CAUGHT, false);
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
    public void handleAttack(AnimatedAction anim) {
        LivingEntity target = this.getTarget();
        if (target != null) {
            if (!anim.is(SPIN))
                this.lookAt(target, 180.0f, 50.0f);
        }
        BiConsumer<AnimatedAction, EntityMarionetta> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    @Override
    public AABB calculateAttackAABB(AnimatedAction anim, Vec3 target, double grow) {
        if (anim.is(SPIN)) {
            return this.getBoundingBox().inflate(1.6, 0.1, 1.6);
        }
        if (anim.is(CHEST_ATTACK)) {
            return this.getBoundingBox().inflate(1.2, 0.1, 1.2);
        }
        return super.calculateAttackAABB(anim, target, grow);
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
}
