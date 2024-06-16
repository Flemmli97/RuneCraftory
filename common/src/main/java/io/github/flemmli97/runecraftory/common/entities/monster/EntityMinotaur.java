package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.ChargeAttackGoal;
import io.github.flemmli97.runecraftory.common.network.S2CScreenShake;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.function.Consumer;

public class EntityMinotaur extends ChargingMonster {

    public static final AnimatedAction SWING = new AnimatedAction(1.08, 0.72, "swing");
    public static final AnimatedAction SPIN = new AnimatedAction(1.48, 0.24, "spin");
    public static final AnimatedAction CHARGE = new AnimatedAction(2.64, 0.64, "charge");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(SWING, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(1, "sleep").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{SWING, SPIN, CHARGE, INTERACT, SLEEP};

    public final ChargeAttackGoal<EntityMinotaur> ai = new ChargeAttackGoal<>(this);
    private final AnimationHandler<EntityMinotaur> animationHandler = new AnimationHandler<>(this, ANIMS);
    private Vec3 spinDirection;
    private float spinAngle;

    public EntityMinotaur(EntityType<? extends EntityMinotaur> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.ai);
    }

    @Override
    protected Consumer<AnimatedAction> animatedActionConsumer() {
        return (anim) -> {
            super.animatedActionConsumer();
            if (!this.level.isClientSide) {
                if (anim == null || anim.is(SPIN)) {
                    this.hitEntity = null;
                    this.spinDirection = null;
                }
            }
        };
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.CHARGE) {
            return anim.is(CHARGE);
        }
        return type == AnimationType.MELEE && anim.is(SWING, SPIN);
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return anim.is(SPIN) ? 6 : 2;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(SPIN)) {
            if (this.hitEntity == null)
                this.hitEntity = new ArrayList<>();
            if (this.spinDirection == null) {
                Vec3 dir;
                if (this.getTarget() != null) {
                    dir = this.getTarget().position().subtract(this.position());
                    this.lookAtNow(this.getTarget(), 360, 90);
                }
                else
                    dir = this.getLookAngle();
                this.spinDirection = new Vec3(dir.x(), 0, dir.z()).normalize().scale(0.1);
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
                        .withTargetPredicate(e -> !this.hitEntity.contains(e))
                        .executeAttack());
            }
        } else {
            if (anim.is(SWING) && anim.canAttack()) {
                Platform.INSTANCE.sendToTrackingAndSelf(new S2CScreenShake(5, 0.8f), this);
                this.level.playSound(null, this.blockPosition(), SoundEvents.GENERIC_EXPLODE, this.getSoundSource(), 1.0f, 0.9f);
            }
            super.handleAttack(anim);
        }
    }

    @Override
    public boolean handleChargeMovement() {
        boolean res = super.handleChargeMovement();
        if (res) {
            this.playSound(ModSounds.ENTITY_SKELEFANG_CHARGE.get(), 1, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
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
    public float attackChance(AnimationType type) {
        return 1;
    }

    @Override
    public AnimationHandler<? extends EntityMinotaur> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public float chargingLength() {
        return 9;
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public AnimatedAction getSleepAnimation() {
        return SLEEP;
    }
}
