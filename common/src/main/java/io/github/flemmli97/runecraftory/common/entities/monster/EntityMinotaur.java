package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.ChargeAction;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.network.S2CScreenShake;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.RandomMoveAroundRunner;
import io.github.flemmli97.tenshilib.common.utils.OrientedBoundingBox;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EntityMinotaur extends ChargingMonster {

    public static final AnimatedAction SWING = new AnimatedAction(1.08, 0.72, "swing");
    public static final AnimatedAction SPIN = new AnimatedAction(1.48, 0.24, "spin");
    public static final AnimatedAction CHARGE = new AnimatedAction(2.64, 0.64, "charge");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(SWING, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(1, "sleep").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{SWING, SPIN, CHARGE, INTERACT, SLEEP};

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityMinotaur>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(SWING, e -> 1), 1),
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(SPIN, e -> 1), 1),
            WeightedEntry.wrap(new GoalAttackAction<EntityMinotaur>(CHARGE)
                    .cooldown(e -> e.animationCooldown(CHARGE))
                    .withCondition(MonsterActionUtils.chargeCondition())
                    .prepare(ChargeAction::new), 2)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityMinotaur>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 0.5)), 3),
            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(12, 5)), 5)
    );

    public final AnimatedAttackGoal<EntityMinotaur> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityMinotaur> animationHandler = new AnimationHandler<>(this, ANIMS);

    private Vec3 spinDirection;
    private float spinAngle;

    public EntityMinotaur(EntityType<? extends EntityMinotaur> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    protected Consumer<AnimatedAction> animatedActionConsumer() {
        return (anim) -> {
            super.animatedActionConsumer().accept(anim);
            if (!this.level.isClientSide) {
                if (anim == null || anim.is(SPIN)) {
                    this.hitEntity = null;
                    this.spinDirection = null;
                }
            }
        };
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimatedAction anim, Vec3 target, double grow) {
        if (anim.is(SPIN)) {
            return new OrientedBoundingBox(this.attackBB(anim), this.getYRot(), 0, this.position());
        }
        return super.calculateAttackAABB(anim, target, grow);
    }

    @Override
    public AABB attackBB(AnimatedAction anim) {
        if (anim.is(SPIN)) {
            double attackSize = this.getBbWidth() * 1.4;
            return new AABB(-attackSize, -0.2, -attackSize, attackSize, this.getBbHeight() + 0.2, attackSize);
        }
        double width = this.getBbWidth() * 1.6;
        double length = this.getBbWidth() * 2.1;
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(SPIN)) {
            if (this.hitEntity == null)
                this.hitEntity = new ArrayList<>();
            if (this.spinDirection == null) {
                this.spinDirection = EntityUtils.getTargetDirection(this, EntityAnchorArgument.Anchor.FEET, true)
                        .scale(0.1);
                this.spinAngle = this.getYRot() + 90;
            }
            if (anim.isPastTick(0.24) && !anim.isPastTick(1.28)) {
                this.setDeltaMovement(this.spinDirection.x(), this.getDeltaMovement().y, this.spinDirection.z());
                int start = Mth.ceil(0.24 * 20.0D);
                int end = Mth.ceil(1.28 * 20.0D);
                float len = (end - start) / anim.getSpeed();
                float f = (anim.getTick() - start) / anim.getSpeed();
                float angleInc = -490 / len;
                if (anim.isAtTick(0.84))
                    this.hitEntity.clear();
                this.hitEntity.addAll(CombatUtils.EntityAttack.create(this, CombatUtils.EntityAttack.circleTargetsFixedRange((this.spinAngle + f * angleInc), (this.spinAngle + (f + 1) * angleInc), 4.5f))
                        .withTargetPredicate(e -> this.hitPred.test(e) && !this.hitEntity.contains(e))
                        .executeAttack());
            }
        } else {
            if (anim.is(SWING) && anim.canAttack()) {
                S2CScreenShake.sendAround(this, 10, 5, 3);
                this.level.playSound(null, this.blockPosition(), SoundEvents.GENERIC_EXPLODE, this.getSoundSource(), 1.0f, 0.9f);
            }
            super.handleAttack(anim);
        }
    }

    @Override
    public CustomDamage.Builder damageSourceAttack() {
        CustomDamage.Builder source = super.damageSourceAttack();
        if (this.getAnimationHandler().isCurrent(CHARGE))
            source.knock(CustomDamage.KnockBackType.BACK).knockAmount(2);
        else if (this.getAnimationHandler().isCurrent(SWING))
            source.withChangedAttribute(ModAttributes.STUN.get(), 30);
        return source;
    }

    @Override
    protected boolean isChargingAnim(AnimatedAction anim) {
        return anim.is(CHARGE);
    }

    @Override
    public boolean handleChargeMovement(AnimatedAction anim) {
        if (anim.isPastTick(2.2))
            return false;
        boolean res = super.handleChargeMovement(anim);
        if (res) {
            if (this.tickCount % 7 == 0)
                this.playSound(ModSounds.ENTITY_GENERIC_HEAVY_CHARGE.get(), 1, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
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
    public AnimationHandler<? extends EntityMinotaur> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public double chargingSpeed() {
        return 0.45f;
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
    public Vec3 passengerOffset(Entity passenger) {
        return new Vec3(0, 37 / 16d, -7 / 16d);
    }
}
