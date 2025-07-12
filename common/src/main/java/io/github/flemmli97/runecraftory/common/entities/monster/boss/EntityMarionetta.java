package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.SetWalkTargetWithinDist;
import io.github.flemmli97.runecraftory.common.entities.data.SyncableDatas;
import io.github.flemmli97.runecraftory.common.entities.data.SyncableEntityData;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityMarionettaTrap;
import io.github.flemmli97.runecraftory.common.entities.utils.RunecraftoryBossbar;
import io.github.flemmli97.runecraftory.common.network.S2CMobUpdate;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.LeapInDirection;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.MoveToAttackTarget;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.StrafeTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class EntityMarionetta extends BossMonster {

    private static final EntityDataAccessor<Boolean> CAUGHT = SynchedEntityData.defineId(EntityMarionetta.class, EntityDataSerializers.BOOLEAN);

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String MELEE = BUILDER.add("melee", AnimationsBuilder.definition(0.48).marker("attack", 0.28));
    public static final String INTERACT = BUILDER.add("interact", MELEE);
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
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private static final ImmutableMap<String, BiConsumer<AnimationState, EntityMarionetta>> ATTACK_HANDLER = createAnimationHandler(b -> {
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
                entity.mobAttack(anim, null, e -> CombatUtils.mobAttack(entity, e, new DynamicDamage.Builder(entity).hurtResistant(8)));
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
                entity.caughtEntities.forEach(trap::addCaughtEntity);
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

    private final AnimationHandler<EntityMarionetta> animationHandler = new AnimationHandler<>(this, ANIMS)
            .withChangeListener(anim -> {
                this.moveDirection = null;
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
    private Vec3 moveDirection;

    public EntityMarionetta(EntityType<? extends EntityMarionetta> type, Level world) {
        super(type, world);
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
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.26);
        super.applyAttributes();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<EntityMarionetta>create()
                .start(MonsterBehaviourUtils.checkedAttack(MELEE)).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetToAttackTarget<EntityMarionetta>().speedMod((e, t) -> 1.1f))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(9)
                .start(MonsterBehaviourUtils.checkedAttack(CARD_ATTACK)).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new LeapInDirection<EntityMarionetta>()
                        .shouldLeap((entity, target) -> entity.distanceToSqr(target) < 9)
                        .horizontalDirection((entity, target) -> LeapInDirection.createBackwardsVec(entity.position(), target.position())))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(11)
                .start(MonsterBehaviourUtils.checkedAttack(SPIN)).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetToAttackTarget<EntityMarionetta>()
                        .closeEnoughDist(MonsterBehaviourUtils.closeEnough(6)).speedMod((e, t) -> 1.1f))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(10)
                .start(MonsterBehaviourUtils.checkedAttack(CHEST_ATTACK)).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetToAttackTarget<EntityMarionetta>()
                        .closeEnoughDist(MonsterBehaviourUtils.closeEnough(6)).speedMod((e, t) -> 1.1f))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(10)
                .start(MonsterBehaviourUtils.checkedAttack(STUFFED_ANIMALS)).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetWithinDist<EntityMarionetta>()
                        .min(3).max(7).speedMod((e, t) -> 1.1f))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(9)
                .start(MonsterBehaviourUtils.checkedAttack(DARK_BEAM)).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetToAttackTarget<EntityMarionetta>()
                        .closeEnoughDist(MonsterBehaviourUtils.closeEnough(6)).speedMod((e, t) -> 1.1f))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(6)
                .start(MonsterBehaviourUtils.checkedAttack(FURNITURE)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(BossMonster::isEnraged)
                .end(7)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseMonster>builder()
                .add(7, new LeapInDirection<BaseMonster>()
                                .horizontalDirection((entity, target) -> {
                                    if (entity.distanceToSqr(target) <= 4)
                                        return LeapInDirection.createBackwardsVec(entity.position(), target.position());
                                    return LeapInDirection.createSidewaysVec(entity.position(), target.position(), entity.getRandom().nextBoolean());
                                }),
                        new StrafeTarget<BaseMonster>().strafeDistance(8))
                .add(10, new StrafeTarget<BaseMonster>().strafeDistance(8)).build();
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
        if (anim.is(SPIN)) {
            float rotY = -Mth.wrapDegrees((float) (Mth.atan2(dir.x(), dir.z()) * Mth.RAD_TO_DEG));
            return new OrientedBoundingBox(OrientedBoundingBox.originAABB(this)
                    .inflate(grow + 1.6, 0.1, grow + 1.6), rotY, 0, this.position());
        }
        if (anim.is(CHEST_ATTACK)) {
            float rotY = -Mth.wrapDegrees((float) (Mth.atan2(dir.x(), dir.z()) * Mth.RAD_TO_DEG));
            return new OrientedBoundingBox(OrientedBoundingBox.originAABB(this)
                    .inflate(grow + 1.2, 0.1, grow + 1.2), rotY, 0, this.position());
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
    public void handleAttack(AnimationState anim) {
        BiConsumer<AnimationState, EntityMarionetta> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    @Override
    public AnimationHandler<EntityMarionetta> getAnimationHandler() {
        return this.animationHandler;
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

    private void catchEntity(LivingEntity entity) {
        this.caughtEntities.add(entity);
        this.entityData.set(CAUGHT, true);
    }

    public boolean caughtTarget() {
        return this.entityData.get(CAUGHT);
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
}
