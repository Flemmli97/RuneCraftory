package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.ChargeAction;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.DoNothingRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.RandomMoveAroundRunner;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityBuffamoo extends ChargingMonster {

    public static final AnimatedAction CHARGE_ATTACK = new AnimatedAction(44, 16, "charge");
    public static final AnimatedAction STAMP = new AnimatedAction(8, 4, "stamp");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(STAMP, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(1, "sleep").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{STAMP, CHARGE_ATTACK, INTERACT, SLEEP};

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityBuffamoo>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(STAMP, e -> 1), 1),
            WeightedEntry.wrap(new GoalAttackAction<EntityBuffamoo>(CHARGE_ATTACK)
                    .cooldown(e -> e.animationCooldown(CHARGE_ATTACK))
                    .prepare(ChargeAction::new), 1)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityBuffamoo>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(8, 4)), 2),
            WeightedEntry.wrap(new IdleAction<>(DoNothingRunner::new), 2)
    );

    public final AnimatedAttackGoal<EntityBuffamoo> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityBuffamoo> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityBuffamoo(EntityType<? extends EntityBuffamoo> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    protected void applyAttributes() {
        super.applyAttributes();
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2);
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 1.3;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                return;
            if (command == 1)
                this.getAnimationHandler().setAnimation(CHARGE_ATTACK);
            else
                this.getAnimationHandler().setAnimation(STAMP);
        }
    }

    @Override
    protected boolean isChargingAnim(AnimatedAction anim) {
        return anim.is(CHARGE_ATTACK);
    }

    @Override
    public boolean handleChargeMovement(AnimatedAction anim) {
        if (anim.isPastTick(2.04))
            return false;
        return super.handleChargeMovement(anim);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.COW_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.COW_DEATH;
    }

    @Override
    public float getVoicePitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 0.7f;
    }

    @Override
    public AnimationHandler<EntityBuffamoo> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void doWhileCharge() {
        if (this.tickCount % 3 == 0)
            this.level.playSound(null, this.blockPosition(), SoundEvents.COW_STEP, this.getSoundSource(), 1, this.getRandom().nextFloat() * 0.2F);
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    @Override
    public float chargingLength() {
        return 7;
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
        return new Vec3(0, 23 / 16d, -2 / 16d);
    }
}
