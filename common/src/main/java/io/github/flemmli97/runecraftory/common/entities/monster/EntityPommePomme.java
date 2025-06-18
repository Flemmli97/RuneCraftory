package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class EntityPommePomme extends ChargingMonster {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String CHARGE_ATTACK = BUILDER.add("roll", AnimationsBuilder.definition(2).marker("attack", 0.05));
    public static final String KICK = BUILDER.add("kick", AnimationsBuilder.definition(0.88).marker("attack", 0.52));
    public static final String INTERACT = BUILDER.add("interact", KICK);
    public static final String SLEEP = BUILDER.add("sleep", AnimationsBuilder.definition(0).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();
    //
//    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityPommePomme>>> ATTACKS = List.of(
//            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(KICK, e -> 0.8f), 1),
//            WeightedEntry.wrap(new GoalAttackAction<EntityPommePomme>(CHARGE_ATTACK)
//                    .cooldown(e -> e.animationCooldown(CHARGE_ATTACK))
//                    .withCondition(MonsterActionUtils.chargeCondition())
//                    .prepare(ChargeAction::new), 2)
//    );
//    private static final List<WeightedEntry.Wrapper<IdleAction<EntityPommePomme>>> IDLE_ACTIONS = List.of(
//            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 0.5)), 3),
//            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(12, 5)), 5)
//    );
//
//    public final AnimatedAttackGoal<EntityPommePomme> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityPommePomme> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityPommePomme(EntityType<? extends EntityPommePomme> type, Level world) {
        super(type, world);
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double width = this.getBbWidth() * 1.3;
        double length = this.getBbWidth() * 1.7;
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                return;
            if (command == 1) {
                this.getAnimationHandler().setAnimation(CHARGE_ATTACK);
            } else
                this.getAnimationHandler().setAnimation(KICK);
        }
    }

    @Override
    public AnimationHandler<? extends EntityPommePomme> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    protected boolean isChargingAnim(String anim) {
        return anim.is(CHARGE_ATTACK);
    }

    @Override
    public double chargingSpeed() {
        return 0.3;
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public String getSleepAnimation() {
        return SLEEP;
    }

//    @Override
//    public Vec3 passengerOffset(Entity passenger) {
//        return new Vec3(0, 16 / 16d, -5 / 16d);
//    }
}
