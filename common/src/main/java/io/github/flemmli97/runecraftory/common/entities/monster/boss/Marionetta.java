package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.entities.misc.MarionettaTrapEntity;
import io.github.flemmli97.runecraftory.common.entities.utils.RunecraftoryBossbar;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.DummyBehaviour;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.LeapInDirection;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetAwayFromTarget;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetWithinDist;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.entity.data.SyncableDatas;
import io.github.flemmli97.tenshilib.common.entity.data.SyncedDataContainer;
import io.github.flemmli97.tenshilib.common.utils.TypedResource;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.InvalidateMemory;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.StrafeTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class Marionetta extends BossMonster {

    private static final EntityDataAccessor<Boolean> CAUGHT = SynchedEntityData.defineId(Marionetta.class, EntityDataSerializers.BOOLEAN);

    public static final TypedResource<Vec3> MOVE_DIRECTION = new TypedResource<>(RuneCraftory.modRes("move_direction"));

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String MELEE = BUILDER.add("melee", AnimationsBuilder.definition(0.6).marker("attack", 0.4));
    public static final String INTERACT = BUILDER.add("interact", MELEE);
    public static final String SPIN = BUILDER.add("spin", AnimationsBuilder.definition(1.68)
            .marker("attack_start", 0.36).marker("attack_end", 1.56));
    public static final String CARD_ATTACK = BUILDER.add("card_attack", AnimationsBuilder.definition(0.76).marker("attack", 0.48));
    public static final String CHEST_ATTACK = BUILDER.add("chest_attack", AnimationsBuilder.definition(1.08)
            .marker("attack_start", 0.36).marker("attack_end", 0.88));
    public static final String CHEST_THROW = BUILDER.add("chest_throw", AnimationsBuilder.definition(MarionettaTrapEntity.DURATION, false).marker("attack", 0.28));
    public static final String STUFFED_ANIMALS = BUILDER.add("stuffed_animals", AnimationsBuilder.definition(0.88).marker("attack", 0.52));
    public static final String DARK_BEAM = BUILDER.add("dark_beam", AnimationsBuilder.definition(0.88).marker("attack", 0.48));
    public static final String FURNITURE = BUILDER.add("furniture", AnimationsBuilder.definition(1.12).marker("attack", 0.52));
    public static final String LEAP = BUILDER.add("leap", AnimationsBuilder.definition(0.6));
    public static final String ANGRY = BUILDER.add("angry", AnimationsBuilder.definition(1.32));
    public static final String DEFEAT = BUILDER.add("defeat", AnimationsBuilder.definition(10).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private static final ImmutableMap<String, BiConsumer<AnimationState, Marionetta>> ATTACK_HANDLER = createAnimationHandler(b -> {
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
            if (entity.getMoveDirection() == null) {
                entity.setMoveDirection(EntityUtils.getTargetDirection(entity, EntityAnchorArgument.Anchor.FEET, true)
                        .scale(0.5));
            }
            if (anim.isPast("attack_start") && !anim.isPast("attack_end")) {
                entity.setDeltaMovement(entity.getMoveDirection());
                entity.mobAttack(anim, null, e -> {
                    if (CombatUtils.mobAttack(entity, e, new DynamicDamage.Builder(entity).hurtResistant(8))) {
                        float strength = (float) (2 * (1.0D - e.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)));
                        if (strength > 0) {
                            e.setDeltaMovement(e.getDeltaMovement().add(entity.getMoveDirection().scale(strength)));
                        }
                    }
                });
            } else {
                entity.setDeltaMovement(entity.getDeltaMovement().scale(0.95));
            }
        });
        b.put(CARD_ATTACK, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack"))
                RuneCraftorySpells.CARD_THROW.get().use(entity);
        });
        b.put(CHEST_ATTACK, (anim, entity) -> {
            entity.getNavigation().stop();
            if (entity.getMoveDirection() == null) {
                entity.setMoveDirection(EntityUtils.getTargetDirection(entity, EntityAnchorArgument.Anchor.FEET, true)
                        .scale(0.65));
            }
            if (anim.isPast("attack_start") && !anim.isPast("attack_end")) {
                entity.setDeltaMovement(entity.getMoveDirection());
                entity.mobAttack(anim, null, e -> {
                    if (!entity.getType().is(RunecraftoryTags.EntityTypes.MARIONETTA_TRAP_IGNORE) && !entity.caughtEntities.contains(e)) {
                        entity.catchEntity(e);
                    }
                });
            }
        });
        b.put(CHEST_THROW, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack")) {
                Vec3 throwVec = EntityUtils.horizontalLookAngle(entity).scale(1.7).add(0, 0.85, 0);
                MarionettaTrapEntity trap = new MarionettaTrapEntity(entity.level(), entity);
                trap.setDamageMultiplier(0.8f);
                entity.caughtEntities.forEach(trap::addCaughtEntity);
                trap.setDeltaMovement(throwVec);
                entity.level().addFreshEntity(trap);
                entity.caughtEntities.clear();
            }
        });
        b.put(STUFFED_ANIMALS, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack"))
                RuneCraftorySpells.PLUSH_THROW.get().use(entity);
        });
        b.put(DARK_BEAM, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack") && !EntityUtils.sealed(entity))
                RuneCraftorySpells.DARK_BEAM.get().use(entity);
        });
        b.put(FURNITURE, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack") && !EntityUtils.sealed(entity))
                RuneCraftorySpells.FURNITURE.get().use(entity);
        });
    });

    private final AnimationHandler<Marionetta> animationHandler = new AnimationHandler<>(this, ANIMS)
            .withChangeListener(anim -> {
                this.setMoveDirection(null);
                if (this.entityData.get(CAUGHT)) {
                    if (!this.level().isClientSide) {
                        this.entityData.set(CAUGHT, false);
                        this.getAnimationHandler().setAnimation(CHEST_THROW);
                        return true;
                    }
                }
                return false;
            });
    private final List<LivingEntity> caughtEntities = new ArrayList<>();

    public Marionetta(EntityType<? extends Marionetta> type, Level level) {
        super(type, level);
    }

    @Override
    public RunecraftoryBossbar createBossBar() {
        return new RunecraftoryBossbar(RuneCraftoryEntities.MARIONETTA.getID(), this.getDisplayName(), BossEvent.BossBarColor.PINK, BossEvent.BossBarOverlay.PROGRESS)
                .setMusic(RuneCraftorySounds.MARIONETTA_FIGHT.get());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CAUGHT, false);
    }

    @Override
    protected void definedAdditinoalSyncedData(SyncedDataContainer.Builder<BaseMonster> builder) {
        super.definedAdditinoalSyncedData(builder);
        builder.define(MOVE_DIRECTION, SyncableDatas.VEC_3, null);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.26);
        super.applyAttributes();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<Marionetta>create()
                .start(MonsterBehaviourUtils.checkedAttack(MELEE)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(MonsterBehaviourUtils.ifCloserThan(4))
                .prepare(new SetWalkTargetToAttackTarget<Marionetta>().speedMod((e, t) -> 1.1f))
                .prepareOptional(MonsterBehaviourUtils.fastMovement())
                .end(9)
                .start(MonsterBehaviourUtils.checkedAttack(CARD_ATTACK)).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new LeapInDirection<Marionetta>()
                        .shouldLeap((entity, target) -> {
                            if (entity.distanceToSqr(target) < 9) {
                                entity.getAnimationHandler().setAnimation(LEAP);
                                return true;
                            }
                            return false;
                        })
                        .strength(1.2)
                        .horizontalDirection((entity, target) -> LeapInDirection.createBackwardsVec(entity.position(), target.position()))
                        .whenStarting(m -> m.getAnimationHandler().setAnimation(LEAP)))
                .prepareOptional(MonsterBehaviourUtils.moveAttack())
                .end(11)
                .start(MonsterBehaviourUtils.checkedAttack(SPIN)).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetToAttackTarget<Marionetta>()
                        .closeEnoughDist(MonsterBehaviourUtils.closeEnough(14)).speedMod((e, t) -> 1.1f))
                .prepareOptional(MonsterBehaviourUtils.moveAttack())
                .end(10)
                .start(MonsterBehaviourUtils.checkedAttack(CHEST_ATTACK)).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetToAttackTarget<Marionetta>()
                        .closeEnoughDist(MonsterBehaviourUtils.closeEnough(14)).speedMod((e, t) -> 1.1f))
                .prepareOptional(MonsterBehaviourUtils.moveAttack())
                .end(9)
                .start(MonsterBehaviourUtils.checkedAttack(STUFFED_ANIMALS)).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetWithinDist<Marionetta>()
                        .min(2).max(6).speedMod((e, t) -> 1.1f))
                .prepareOptional(MonsterBehaviourUtils.moveAttack())
                .end(8)
                .start(MonsterBehaviourUtils.checkedAttack(DARK_BEAM)).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetToAttackTarget<Marionetta>()
                        .closeEnoughDist(MonsterBehaviourUtils.closeEnough(12)).speedMod((e, t) -> 1.1f))
                .prepareOptional(MonsterBehaviourUtils.moveAttack())
                .end(6)
                .start(MonsterBehaviourUtils.checkedAttack(FURNITURE)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(BossMonster::isEnraged)
                .end(7)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseMonster>builder()
                .add(12,
                        DummyBehaviour.opt(new InvalidateMemory<>(MemoryModuleType.WALK_TARGET)),
                        DummyBehaviour.opt(new LeapInDirection<BaseMonster>()
                                .shouldLeap((e, t) -> {
                                    if (e.getRandom().nextFloat() < 4) {
                                        e.getAnimationHandler().setAnimation(LEAP);
                                        return true;
                                    }
                                    return false;
                                })
                                .horizontalDirection((entity, target) -> {
                                    if (entity.distanceToSqr(target) <= 5)
                                        return LeapInDirection.createBackwardsVec(entity.position(), target.position());
                                    return LeapInDirection.createSidewaysVec(entity.position(), target.position(), entity.getRandom().nextBoolean());
                                })
                                .strength(1.2)
                                .cooldownFor(e -> 60)),
                        new StrafeTarget<BaseMonster>().strafeDistance(10))
                .add(4, new SetWalkTargetAwayFromTarget<BaseMonster>().minDist(3).radius(6), MonsterBehaviourUtils.moveTo())
                .add(6, new SetWalkTargetToAttackTarget<>(), MonsterBehaviourUtils.moveTo()).build();
    }

    @Override
    protected boolean runCooldownBehaviour() {
        AnimationState anim = this.getAnimationHandler().getAnimation();
        return (anim == null || anim.is(LEAP))
                && BrainUtils.hasMemory(this, MemoryModuleType.ATTACK_COOLING_DOWN);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        this.caughtEntities.forEach(entity -> {
            if (entity.isAlive()) {
                if (entity instanceof ServerPlayer player) {
                    Vec3 dir = this.position().add(0, this.getBbHeight() + 0.2, 0).subtract(player.position());
                    player.setDeltaMovement(dir);
                    player.moveTo(this.getX(), this.getY() + this.getBbHeight() + 0.2, this.getZ());
                } else {
                    entity.setDeltaMovement(Vec3.ZERO);
                    entity.setPos(this.getX(), this.getY() + this.getBbHeight() + 0.2, this.getZ());
                }
                entity.hurtMarked = true;
            }
        });
    }

    @Override
    public void setEnraged(boolean flag, boolean load) {
        super.setEnraged(flag, load);
        if (flag && !load)
            this.getAnimationHandler().setAnimation(ANGRY);
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
    public void push(Entity entityIn) {
        if (this.getAnimationHandler().isCurrent(SPIN, CHEST_ATTACK))
            return;
        super.push(entityIn);
    }

    @Override
    protected Vec3 directionToLookAt() {
        if (this.getAnimationHandler().isCurrent(SPIN, CHEST_ATTACK)) {
            return this.getMoveDirection();
        }
        return super.directionToLookAt();
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimationState anim, Vec3 target, double grow) {
        Vec3 dir = this.getMoveDirection();
        if (dir == null) {
            if (target != null)
                dir = target.subtract(this.position());
            else
                dir = this.getLookAngle();
        }
        if (anim.is(SPIN)) {
            float rotY = -Mth.wrapDegrees((float) (Mth.atan2(dir.x(), dir.z()) * Mth.RAD_TO_DEG));
            return new OrientedBoundingBox(OrientedBoundingBox.originAABB(this)
                    .expandTowards(0, 0, -1)
                    .inflate(grow + 0.5, 0.1, grow + 1.2), rotY, 0, this.position());
        }
        if (anim.is(CHEST_ATTACK)) {
            float rotY = -Mth.wrapDegrees((float) (Mth.atan2(dir.x(), dir.z()) * Mth.RAD_TO_DEG));
            return new OrientedBoundingBox(OrientedBoundingBox.originAABB(this)
                    .inflate(grow + 0.5, 0.1, grow + 1.2), rotY, 0, this.position());
        }
        return super.calculateAttackAABB(anim, target, grow);
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double width = this.getBbWidth() * 1.5;
        double length = this.getBbWidth() * 1.7;
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public int animationCooldown(String anim) {
        int cooldown = super.animationCooldown(anim);
        if (anim.equals(CHEST_ATTACK)) {
            cooldown += this.getAnimationHandler().get(CHEST_THROW).length();
        }
        return cooldown;
    }

    @Override
    public void handleAttack(AnimationState anim) {
        BiConsumer<AnimationState, Marionetta> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    @Override
    public AnimationHandler<Marionetta> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 2 ? RuneCraftorySpells.CARD_THROW.get() : null))
                return;
            if (command == 2)
                this.getAnimationHandler().setAnimation(CARD_ATTACK);
            else if (command == 1)
                this.getAnimationHandler().setAnimation(SPIN);
            else
                this.getAnimationHandler().setAnimation(MELEE);
        }
    }

    private void catchEntity(LivingEntity entity) {
        this.caughtEntities.add(entity);
        this.entityData.set(CAUGHT, true);
    }

    public boolean caughtTarget() {
        return this.entityData.get(CAUGHT);
    }

    public Vec3 getMoveDirection() {
        return this.getDataContainer().get(MOVE_DIRECTION);
    }

    public void setMoveDirection(Vec3 direction) {
        this.getDataContainer().set(MOVE_DIRECTION, direction);
    }

    @Override
    public String getInteractAnimation() {
        return INTERACT;
    }

    @Override
    public String getDeathAnimation() {
        return DEFEAT;
    }
}
