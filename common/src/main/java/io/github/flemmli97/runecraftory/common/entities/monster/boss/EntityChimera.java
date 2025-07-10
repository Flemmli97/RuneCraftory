package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.SetWalkTargetWithinDist;
import io.github.flemmli97.runecraftory.common.entities.data.SyncableDatas;
import io.github.flemmli97.runecraftory.common.entities.data.SyncableEntityData;
import io.github.flemmli97.runecraftory.common.entities.utils.RunecraftoryBossbar;
import io.github.flemmli97.runecraftory.common.network.S2CMobUpdate;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.MoveToAttackTarget;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetAwayFromTarget;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.data.AnimationPlayHolder;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class EntityChimera extends BossMonster {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String LEAP = BUILDER.add("leap", AnimationsBuilder.definition(1.36).marker("attack_start", 0).marker("attack_end", 1.2));
    public static final String FIRE_TAIL_BUBBLE = BUILDER.add("tail_beam", AnimationsBuilder.definition(1.48).marker("attack", 0.44));
    public static final String WATER_TAIL_BUBBLE = BUILDER.add("water_tail_bubble", FIRE_TAIL_BUBBLE);
    public static final String WATER_TAIL_BEAM = BUILDER.add("water_tail_beam", FIRE_TAIL_BUBBLE);
    public static final String FIRE_BREATH = BUILDER.add("breath_attack", AnimationsBuilder.definition(1.2).marker("attack", 0.4));
    public static final String BUBBLE_BEAM = BUILDER.add("bubble_beam", FIRE_BREATH);
    public static final String SLASH = BUILDER.add("claw_attack", AnimationsBuilder.definition(0.64).marker("attack", 0.36, 0.72));
    public static final String INTERACT = BUILDER.add("interact", SLASH);
    public static final String BITE = BUILDER.add("bite_attack", AnimationsBuilder.definition(1.04)
            .marker("attack_1", 0.4).marker("attack_2", 0.72));
    public static final String ANGRY = BUILDER.add("angry", AnimationsBuilder.definition(1.04));
    public static final String SLEEP = BUILDER.add("sleep", AnimationsBuilder.definition(0).infinite());
    public static final String DEFEAT = BUILDER.add("defeat", AnimationsBuilder.definition(10).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private static final ImmutableMap<String, BiConsumer<AnimationState, EntityChimera>> ATTACK_HANDLER = createAnimationHandler(b -> {
        BiConsumer<AnimationState, EntityChimera> summonFire = (anim, entity) -> {
            if (anim.isAt("attack")) {
                ModSpells.FIREBALL_BARRAGE.get().use(entity);
            }
        };
        b.put(FIRE_TAIL_BUBBLE, summonFire);
        b.put(FIRE_BREATH, summonFire);
        BiConsumer<AnimationState, EntityChimera> summonWater = (anim, entity) -> {
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
                            new DynamicDamage.Builder(entity).hurtResistant(5).knock(DynamicDamage.KnockBackType.UP))) {
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
                        new DynamicDamage.Builder(entity).hurtResistant(5).knockAmount(0)));
            } else if (anim.isAt("attack_2")) {
                entity.mobAttack(anim, entity.getTarget(), entity::doHurtTarget);
            }
        });
    });

    private final AnimationHandler<EntityChimera> animationHandler = new AnimationHandler<>(this, ANIMS)
            .withChangeListener(anim -> {
                if (!this.level().isClientSide) {
                    if (anim == null) {
                        this.setChargeMotion(null);
                    } else if (anim.is(LEAP))
                        this.chargeAttackSuccess = false;
                    this.hitEntity = null;
                    return false;
                }
                return false;
            });
    protected boolean chargeAttackSuccess;
    protected List<LivingEntity> hitEntity;
    private Vec3 chargeMotion;

    public EntityChimera(EntityType<? extends EntityChimera> type, Level world) {
        super(type, world);
    }

    @Override
    public RunecraftoryBossbar createBossBar() {
        return new RunecraftoryBossbar(null, this.getDisplayName(), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS)
                .setMusic(ModSounds.CHIMERA_FIGHT.get());
    }

    @Override
    public void setEnraged(boolean flag, boolean load) {
        super.setEnraged(flag, load);
        if (flag && !load)
            this.getAnimationHandler().setAnimation(ANGRY);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.31);
        this.getAttribute(Attributes.STEP_HEIGHT).setBaseValue(Attributes.STEP_HEIGHT.value().getDefaultValue() + 1);
        super.applyAttributes();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<EntityChimera>create()
                .start(MonsterBehaviourUtils.checkedAttack(AnimationPlayHolder.<EntityChimera>builder(LEAP)
                        .start(BITE, m -> m.chargeAttackSuccess).build())).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetToAttackTarget<EntityChimera>().speedMod((e, t) -> 1.1f)
                        .closeEnoughDist(MonsterBehaviourUtils.closeEnough(4))).prepareOptional(new MoveToAttackTarget<>())
                .end(10)
                .start(MonsterBehaviourUtils.checkedAttack(FIRE_TAIL_BUBBLE)).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetWithinDist<EntityChimera>().speedMod((e, t) -> 1.1f)
                        .min(4).max(10)).prepareOptional(new MoveToAttackTarget<>())
                .end(8)
                .start(MonsterBehaviourUtils.checkedAttack(FIRE_TAIL_BUBBLE)).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetWithinDist<EntityChimera>().speedMod((e, t) -> 1.1f)
                        .min(4).max(10)).prepareOptional(new MoveToAttackTarget<>())
                .end(8)
                .start(MonsterBehaviourUtils.checkedAttack(WATER_TAIL_BUBBLE)).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetWithinDist<EntityChimera>().speedMod((e, t) -> 1.1f)
                        .min(4).max(10)).prepareOptional(new MoveToAttackTarget<>())
                .end(8)
                .start(MonsterBehaviourUtils.checkedAttack(WATER_TAIL_BEAM)).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetWithinDist<EntityChimera>().speedMod((e, t) -> 1.1f)
                        .min(4).max(10)).prepareOptional(new MoveToAttackTarget<>())
                .end(8)
                .start(MonsterBehaviourUtils.checkedAttack(BUBBLE_BEAM)).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetWithinDist<EntityChimera>().speedMod((e, t) -> 1.1f)
                        .min(4).max(10)).prepareOptional(new MoveToAttackTarget<>())
                .end(8)
                .start(MonsterBehaviourUtils.checkedAttack(SLASH)).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetToAttackTarget<EntityChimera>().speedMod((e, t) -> 1.1f))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(10)
                .start(MonsterBehaviourUtils.checkedAttack(SLASH)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(MonsterBehaviourUtils.ifCloserThan(3))
                .prepare(new SetWalkTargetToAttackTarget<EntityChimera>().speedMod((e, t) -> 1.1f))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(15)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseMonster>builder()
                .add(10, new SetWalkTargetToAttackTarget<BaseMonster>().speedMod((e, t) -> 1.1f), new MoveToWalkTarget<>())
                .add(7, new SetWalkTargetAwayFromTarget<BaseMonster>().radius(5).speedMod((e, t) -> 1.1f), new MoveToWalkTarget<>()).build();
    }

    @Override
    protected ExtendedBehaviour<? extends BaseMonster> getWanderBehaviour() {
        return new SetRandomWalkTarget<BaseMonster>().speedModifier(0.7f);
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
    protected Vec3 directionToLookAt() {
        if (this.getAnimationHandler().isCurrent(LEAP)) {
            return this.chargeMotion;
        }
        return super.directionToLookAt();
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimationState anim, Vec3 target, double grow) {
        if (anim.is(LEAP)) {
            return new OrientedBoundingBox(OrientedBoundingBox.originAABB(this)
                    .inflate(0.3, 0.1, 0.3 + this.getDeltaMovement().scale(0.3).length()), this.getYRot(), 0, this.position());
        }
        if (!anim.is(BITE)) {
            return super.calculateAttackAABB(anim, target, grow);
        }
        double reach = this.getBbWidth() * 0.9;
        Vec3 dir;
        float offset = anim.isAt("attack_1") ? 45 : -5;
        if (this.getControllingPassenger() instanceof Player player)
            dir = Vec3.directionFromRotation(player.getXRot(), player.getYRot() + offset);
        else if (target != null) {
            reach = Math.min(reach, this.position().distanceTo(target));
            dir = target.subtract(this.position()).normalize().yRot(offset * Mth.DEG_TO_RAD);
        } else {
            dir = Vec3.directionFromRotation(this.getXRot(), this.getYRot() + offset);
        }
        Vec3 attackPos = this.position().add(dir.scale(reach));
        return new OrientedBoundingBox(this.attackBB(anim).inflate(grow, 0, grow), this.getYRot() + 45, 0, attackPos);
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double width = this.getBbWidth() * 1.6;
        double length = this.getBbWidth() * 1.7;
        if (anim.is(BITE)) {
            width = this.getBbWidth() * 1.2;
            length = this.getBbWidth() * 1.3;
        }
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public void handleAttack(AnimationState anim) {
        BiConsumer<AnimationState, EntityChimera> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    @Override
    public AnimationHandler<EntityChimera> getAnimationHandler() {
        return this.animationHandler;
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
    public double ridingSpeedModifier() {
        return 1.5;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.ENTITY_CHIMERA_AMBIENT.get();
    }

    public void setChargeMotion(Vec3 charge) {
        this.chargeMotion = charge;
        S2CMobUpdate.send(this, SyncableDatas.VEC_3, this.chargeMotion);
    }

    @Override
    public void onUpdate(SyncableEntityData.SyncedContainer<?> data) {
        super.onUpdate(data);
        data.runIf(SyncableDatas.VEC_3, motion -> this.chargeMotion = motion);
    }

    @Override
    public String getInteractAnimation() {
        return INTERACT;
    }

    @Override
    public String getDeathAnimation() {
        return DEFEAT;
    }

    @Override
    public String getSleepAnimation() {
        return SLEEP;
    }
}