package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class EntityBeetle extends ChargingMonster {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String CHARGE_ATTACK = BUILDER.add("ramm", AnimationsBuilder.definition(1.52).marker("attack_start", 0.16));
    public static final String MELEE = BUILDER.add("attack", AnimationsBuilder.definition(0.72).marker("attack", 0.4));
    public static final String INTERACT = BUILDER.add("interact", MELEE);
    public static final String SLEEP = BUILDER.add("sleep", AnimationsBuilder.definition(0).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    //    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityBeetle>>> ATTACKS = List.of(
//            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(MELEE, e -> 1), 1),
//            WeightedEntry.wrap(new GoalAttackAction<EntityBeetle>(CHARGE_ATTACK)
//                    .cooldown(e -> e.animationCooldown(CHARGE_ATTACK))
//                    .prepare(ChargeAction::new), 1)
//    );
//    private static final List<WeightedEntry.Wrapper<IdleAction<EntityBeetle>>> IDLE_ACTIONS = List.of(
//            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(8, 4)), 2),
//            WeightedEntry.wrap(new IdleAction<>(DoNothingRunner::new), 2)
//    );
//
//    public final AnimatedAttackGoal<EntityBeetle> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityBeetle> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityBeetle(EntityType<? extends EntityBeetle> type, Level world) {
        super(type, world);
    }

    @Override
    public AnimationHandler<EntityBeetle> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    protected boolean isChargingAnim(String anim) {
        return anim.equals(CHARGE_ATTACK);
    }

    @Override
    public double chargingSpeed() {
        return 0.3;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                return;
            if (command == 1)
                this.getAnimationHandler().setAnimation(CHARGE_ATTACK);
            else
                this.getAnimationHandler().setAnimation(MELEE);
        }
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double width = this.getBbWidth() * 1.4;
        double length = this.getBbWidth() * 2.6;
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
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
//        return new Vec3(0, 18 / 16d, -10 / 16d);
//    }
}
