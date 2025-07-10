package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.entities.data.SyncableDatas;
import io.github.flemmli97.runecraftory.common.entities.data.SyncableEntityData;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityPollen;
import io.github.flemmli97.runecraftory.common.entities.utils.RunecraftoryBossbar;
import io.github.flemmli97.runecraftory.common.network.S2CMobUpdate;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.MoveToAttackTarget;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetAwayFromTarget;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.data.AnimationPlayHolder;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinition;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.StrafeTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;

import java.util.function.BiConsumer;

public class EntityAmbrosia extends BossMonster {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String KICK_1 = BUILDER.add("kick_1", AnimationsBuilder.definition(0.6).marker("attack", 0.32));
    public static final String INTERACT = BUILDER.add("interact", KICK_1);
    public static final String KICK_2 = BUILDER.add("kick_2", AnimationsBuilder.definition(0.6).marker("attack", 0.32));
    public static final String KICK_3 = BUILDER.add("kick_3", AnimationsBuilder.definition(0.84).marker("attack", 0.28));
    public static final String BUTTERFLY = BUILDER.add("butterfly", AnimationsBuilder.definition(2.04).marker("attack", 0.32));
    public static final String WAVE = BUILDER.add("wave", AnimationsBuilder.definition(2.24).marker("attack", 0.24));
    public static final String SLEEP = BUILDER.add("sleep", AnimationsBuilder.definition(0.76).marker("attack", 0.24));
    public static final String POLLEN = BUILDER.add("pollen", AnimationsBuilder.definition(0.72).marker("attack", 0.28));
    public static final String POLLEN_2 = BUILDER.add("pollen_2", POLLEN);
    public static final String DEFEAT = BUILDER.add("defeat", AnimationsBuilder.definition(10).infinite());
    public static final String ANGRY = BUILDER.add("angry", AnimationsBuilder.definition(2.4));
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private static final ImmutableMap<String, BiConsumer<AnimationState, EntityAmbrosia>> ATTACK_HANDLER = createAnimationHandler(b -> {
        b.put(BUTTERFLY, (anim, entity) -> {
            if (anim.isAt("attack")) {
                ModSpells.BUTTERFLY.get().use(entity);
            }
        });
        BiConsumer<AnimationState, EntityAmbrosia> kick = (anim, entity) -> {
            LivingEntity target = entity.getTarget();
            if (target != null) {
                entity.getNavigation().moveTo(target, 1.0);
            }
            if (anim.isAt("attack")) {
                entity.mobAttack(anim, target, entity::doHurtTarget);
            }
        };
        b.put(KICK_1, kick);
        b.put(KICK_2, kick);
        b.put(KICK_3, kick);
        b.put(SLEEP, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack"))
                ModSpells.SLEEP_BALLS.get().use(entity);
        });
        b.put(WAVE, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack"))
                ModSpells.WAVE.get().use(entity);
        });
        BiConsumer<AnimationState, EntityAmbrosia> pollenHandler = (anim, entity) -> {
            if (entity.moveDirection == null) {
                entity.setMoveDirection(EntityUtils.getTargetDirection(entity, EntityAnchorArgument.Anchor.FEET, true)
                        .scale(0.35));
            }
            entity.setDeltaMovement(entity.moveDirection);
            if (anim.isAt("attack") && !EntityUtils.sealed(entity)) {
                entity.getNavigation().stop();
                EntityPollen pollen = new EntityPollen(entity.level(), entity);
                pollen.setPos(pollen.getX(), pollen.getY() + 0.5, pollen.getZ());
                entity.level().addFreshEntity(pollen);
            }
        };
        b.put(POLLEN, pollenHandler);
        b.put(POLLEN_2, pollenHandler);
    });

    private final AnimationHandler<EntityAmbrosia> animationHandler = new AnimationHandler<>(this, ANIMS).withChangeListener(anim -> {
        if (!this.level().isClientSide && anim == null) {
            this.setMoveDirection(null);
        }
        return false;
    });
    private Vec3 moveDirection;

    public EntityAmbrosia(EntityType<? extends EntityAmbrosia> type, Level world) {
        super(type, world);
    }

    @Override
    public RunecraftoryBossbar createBossBar() {
        return new RunecraftoryBossbar(null, this.getDisplayName(), BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.PROGRESS)
                .setMusic(ModSounds.AMBROSIA_FIGHT.get());
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.29);
        super.applyAttributes();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<EntityAmbrosia>create()
                .start(MonsterBehaviourUtils.checkedAttack(BUTTERFLY)).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetAwayFromTarget<EntityAmbrosia>().speedMod(1.1f).radius(7)).prepareOptional(new MoveToAttackTarget<>())
                .end(8)
                .start(MonsterBehaviourUtils.checkedAttack(AnimationPlayHolder.<EntityAmbrosia>builder(KICK_1)
                        .start(KICK_2).chain(KICK_3).build())).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetToAttackTarget<EntityAmbrosia>().speedMod((e, t) -> 1.2f))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(7)
                .start(MonsterBehaviourUtils.checkedAttack(SLEEP)).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetToAttackTarget<EntityAmbrosia>().speedMod((e, t) -> 1.2f)
                        .closeEnoughDist(MonsterBehaviourUtils.closeEnough(2)))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(10)
                .start(MonsterBehaviourUtils.checkedAttack(WAVE)).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetToAttackTarget<EntityAmbrosia>().speedMod((e, t) -> 1.2f)
                        .closeEnoughDist(MonsterBehaviourUtils.closeEnough(2)))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(10)
                .start(MonsterBehaviourUtils.checkedAttack(AnimationPlayHolder.<EntityAmbrosia>builder(POLLEN)
                        .start(POLLEN_2).build())).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(BossMonster::isEnraged)
                .prepare(new SetWalkTargetToAttackTarget<EntityAmbrosia>().speedMod((e, t) -> 1.2f)
                        .closeEnoughDist(MonsterBehaviourUtils.closeEnough(2)))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(9)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseMonster>builder()
                .add(1, new StrafeTarget<BaseMonster>().strafeDistance(9)).build();
    }

    @Override
    public void setEnraged(boolean flag, boolean load) {
        super.setEnraged(flag, load);
        if (flag && !load)
            this.getAnimationHandler().setAnimation(ANGRY);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return (!this.getAnimationHandler().hasAnimation() || !(this.getAnimationHandler().isCurrent(WAVE, ANGRY))) && super.hurt(source, amount);
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.getAnimationHandler().isCurrent(ANGRY, DEFEAT);
    }

    @Override
    public boolean shouldFreezeTravel() {
        return this.getAnimationHandler().isCurrent(WAVE);
    }

    @Override
    protected Vec3 directionToLookAt() {
        if (this.getAnimationHandler().isCurrent(POLLEN, POLLEN_2)) {
            return this.moveDirection;
        }
        return super.directionToLookAt();
    }

    @Override
    public void setupAttack(AnimationDefinition anim) {
        if (anim.is(BUTTERFLY) && this.getTarget() != null) {
            LivingEntity target = this.getTarget();
            this.setTargetPosition(new TargetPosition(target.position(),
                    target.getY(), target.getY() + target.getBbHeight() * 0.3));
        } else
            super.setupAttack(anim);
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimationState anim, Vec3 target, double grow) {
        if (anim.is(POLLEN)) {
            return new OrientedBoundingBox(OrientedBoundingBox.originAABB(this)
                    .inflate(grow + 2, grow, grow + 2), this.getYRot(), this.getXRot(), this.position());
        }
        return super.calculateAttackAABB(anim, target, grow);
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double width = this.getBbWidth() * 1.6;
        double length = this.getBbWidth() * 2.1;
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public void handleAttack(AnimationState anim) {
        BiConsumer<AnimationState, EntityAmbrosia> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    @Override
    public AnimationHandler<EntityAmbrosia> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2) {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), ModSpells.WAVE.get()))
                    this.getAnimationHandler().setAnimation(WAVE);
            } else if (command == 1) {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), ModSpells.SLEEP_BALLS.get()))
                    this.getAnimationHandler().setAnimation(SLEEP);
            } else if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                this.getAnimationHandler().setAnimation(KICK_1);
        }
    }

    @Override
    public void onUpdate(SyncableEntityData.SyncedContainer<?> data) {
        super.onUpdate(data);
        data.runIf(SyncableDatas.VEC_3, motion -> this.moveDirection = motion);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    @Override
    public void push(double x, double y, double z) {
        if (this.getAnimationHandler().isCurrent(POLLEN, ANGRY, DEFEAT))
            return;
        super.push(x, y, z);
    }

    protected void setMoveDirection(Vec3 moveDirection) {
        this.moveDirection = moveDirection;
        S2CMobUpdate.send(this, SyncableDatas.VEC_3, this.moveDirection);
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
