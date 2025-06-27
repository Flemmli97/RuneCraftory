package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class EntityBuffamoo extends ChargingMonster {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String CHARGE_ATTACK = BUILDER.add("charge", AnimationsBuilder.definition(2.2)
            .marker("attack_start", 0.72).marker("attack_end", 1.92));
    public static final String STAMP = BUILDER.add("stamp", AnimationsBuilder.definition(0.48).marker("attack", 0.28));
    public static final String INTERACT = BUILDER.add("interact", STAMP);
    public static final String SLEEP = BUILDER.add("sleep", AnimationsBuilder.definition(0).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    //    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityBuffamoo>>> ATTACKS = List.of(
//            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(STAMP, e -> 1), 1),
//            WeightedEntry.wrap(new GoalAttackAction<EntityBuffamoo>(CHARGE_ATTACK)
//                    .cooldown(e -> e.animationCooldown(CHARGE_ATTACK))
//                    .prepare(ChargeAction::new), 1)
//    );
//    private static final List<WeightedEntry.Wrapper<IdleAction<EntityBuffamoo>>> IDLE_ACTIONS = List.of(
//            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(8, 4)), 2),
//            WeightedEntry.wrap(new IdleAction<>(DoNothingRunner::new), 2)
//    );
//
//    public final AnimatedAttackGoal<EntityBuffamoo> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityBuffamoo> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityBuffamoo(EntityType<? extends EntityBuffamoo> type, Level world) {
        super(type, world);
    }

    @Override
    protected void applyAttributes() {
        super.applyAttributes();
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2);
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double width = this.getBbWidth() * 1.4;
        double length = this.getBbWidth() * 1.8;
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
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
    protected boolean isChargingAnim(String anim) {
        return anim.equals(CHARGE_ATTACK);
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
    public DynamicDamage.Builder damageSourceAttack() {
        DynamicDamage.Builder source = super.damageSourceAttack();
        if (this.getAnimationHandler().isCurrent(CHARGE_ATTACK))
            source.knock(DynamicDamage.KnockBackType.BACK).knockAmount(2);
        else if (this.getAnimationHandler().isCurrent(STAMP))
            source.withChangedAttribute(ModAttributes.STUN.asHolder(), 20);
        return source;
    }

    @Override
    public void doWhileCharge() {
        if (this.tickCount % 3 == 0)
            this.level().playSound(null, this.blockPosition(), SoundEvents.COW_STEP, this.getSoundSource(), 1, this.getRandom().nextFloat() * 0.2F);
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    @Override
    public double chargingSpeed() {
        return 0.28f;
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public String getSleepAnimation() {
        return SLEEP;
    }
//
//    @Override
//    public Vec3 passengerOffset(Entity passenger) {
//        return new Vec3(0, 23 / 16d, -2 / 16d);
//    }
}
