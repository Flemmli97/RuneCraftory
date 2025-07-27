package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.SetChargeTarget;
import io.github.flemmli97.runecraftory.common.network.S2CScreenShake;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.MoveToAttackTarget;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinition;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Minotaur extends ChargingMonster {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String SWING = BUILDER.add("swing", AnimationsBuilder.definition(1.08).marker("attack", 0.72));
    public static final String INTERACT = BUILDER.add("interact", SWING);
    public static final String SPIN = BUILDER.add("spin", AnimationsBuilder.definition(1.48)
            .marker("attack_start", 0.24).marker("attack_end", 1.28).marker("reset", 0.84));
    public static final String CHARGE = BUILDER.add("charge", AnimationsBuilder.definition(2.64)
            .marker("attack_start", 0.64).marker("attack_end", 2.2));
    public static final String SLEEP = BUILDER.add("sleep", AnimationsBuilder.definition(0).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private final AnimationHandler<Minotaur> animationHandler = new AnimationHandler<>(this, ANIMS);

    private Vec3 spinDirection;
    private float spinAngle;

    public Minotaur(EntityType<? extends Minotaur> type, Level world) {
        super(type, world);
    }

    @Override
    protected Consumer<AnimationDefinition> animatedActionConsumer() {
        return (anim) -> {
            super.animatedActionConsumer().accept(anim);
            if (!this.level().isClientSide) {
                if (anim == null || anim.is(SPIN)) {
                    this.hitEntity = null;
                    this.spinDirection = null;
                }
            }
        };
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<ChargingMonster>create()
                .start(SWING).play(MonsterBehaviourUtils.requireInRangePlay())
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(new MoveToAttackTarget<>())
                .end(7)
                .start(SPIN).play(MonsterBehaviourUtils.requireInRangePlay())
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(new MoveToAttackTarget<>())
                .end(5)
                .start(CHARGE).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetChargeTarget<>())
                .end(3)
                .start(CHARGE).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(MonsterBehaviourUtils.ifFurtherThan(4))
                .prepare(new SetChargeTarget<>())
                .end(8)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseMonster>builder()
                .add(6, new SetWalkTargetToAttackTarget<>(), new MoveToWalkTarget<>())
                .add(2, new SetRandomWalkTarget<>(), new MoveToWalkTarget<>()).build();
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimationState anim, Vec3 target, double grow) {
        if (anim.is(SPIN)) {
            return new OrientedBoundingBox(this.attackBB(anim), this.getYRot(), 0, this.position());
        }
        return super.calculateAttackAABB(anim, target, grow);
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        if (anim.is(SPIN)) {
            double attackSize = this.getBbWidth() * 1.4;
            return new AABB(-attackSize, -0.2, -attackSize, attackSize, this.getBbHeight() + 0.2, attackSize);
        }
        double width = this.getBbWidth() * 1.6;
        double length = this.getBbWidth() * 2.1;
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public DynamicDamage.Builder damageSourceAttack() {
        DynamicDamage.Builder source = super.damageSourceAttack();
        if (this.getAnimationHandler().isCurrent(CHARGE))
            source.knock(DynamicDamage.KnockBackType.BACK, 2);
        else if (this.getAnimationHandler().isCurrent(SWING))
            source.withChangedAttribute(RuneCraftoryAttributes.STUN.asHolder(), 30);
        return source;
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(SPIN)) {
            if (this.hitEntity == null)
                this.hitEntity = new ArrayList<>();
            if (this.spinDirection == null) {
                this.spinDirection = EntityUtils.getTargetDirection(this, EntityAnchorArgument.Anchor.FEET, true)
                        .scale(0.1);
                this.spinAngle = this.getYRot() + 90;
            }
            if (anim.isPast("attack_start") && !anim.isPast("attack_end")) {
                this.setDeltaMovement(this.spinDirection.x(), this.getDeltaMovement().y, this.spinDirection.z());
                float start = (float) (anim.getMarker("attack_start", 0) * 20);
                float end = (float) (anim.getMarker("attack_end", 0) * 20);
                float f = (float) anim.progress(start, end, 1, 0);
                float fNext = (float) anim.progress(start, end, 1, 1);
                float angleInc = -490;
                if (anim.isAt("reset"))
                    this.hitEntity.clear();
                this.hitEntity.addAll(CombatUtils.EntityAttack.create(this,
                                CombatUtils.EntityAttack.circleTargetsFixedRange(this.spinAngle + f * angleInc, this.spinAngle + fNext * angleInc, 4.5f))
                        .withTargetPredicate(e -> this.hitPred.test(e) && !this.hitEntity.contains(e))
                        .executeAttack());
            }
        } else {
            if (anim.is(SWING) && anim.isAt("attack")) {
                S2CScreenShake.sendAround(this, 16, 5, 3);
                this.level().playSound(null, this.blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), this.getSoundSource(), 1.0f, 0.9f);
            }
            super.handleAttack(anim);
        }
    }

    @Override
    public AnimationHandler<? extends Minotaur> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    protected boolean isChargingAnim(String anim) {
        return anim.equals(CHARGE);
    }

    @Override
    public double chargingSpeed() {
        return 0.45f;
    }

    @Override
    public boolean handleChargeMovement(AnimationState anim) {
        boolean res = super.handleChargeMovement(anim);
        if (res) {
            if (this.tickCount % 7 == 0)
                this.playSound(RuneCraftorySounds.ENTITY_GENERIC_HEAVY_CHARGE.get(), 1, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
        }
        return res;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                return;
            if (command == 1) {
                this.getAnimationHandler().setAnimation(SPIN);
            } else
                this.getAnimationHandler().setAnimation(SWING);
        }
    }

    @Override
    public String getInteractAnimation() {
        return INTERACT;
    }

    @Override
    public String getSleepAnimation() {
        return SLEEP;
    }
}
