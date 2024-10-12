package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.ChargeAction;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.RandomMoveAroundRunner;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityPommePomme extends ChargingMonster {

    public static final AnimatedAction CHARGE_ATTACK = new AnimatedAction(34, 2, "roll");
    public static final AnimatedAction KICK = new AnimatedAction(17, 11, "kick");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(KICK, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(1, "sleep").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{KICK, CHARGE_ATTACK, INTERACT, SLEEP};

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityPommePomme>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(KICK, e -> 0.8f), 1),
            WeightedEntry.wrap(new GoalAttackAction<EntityPommePomme>(CHARGE_ATTACK)
                    .cooldown(e -> e.animationCooldown(CHARGE_ATTACK))
                    .withCondition(MonsterActionUtils.chargeCondition())
                    .prepare(ChargeAction::new), 2)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityPommePomme>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 1)), 3),
            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(12, 5)), 5)
    );

    public final AnimatedAttackGoal<EntityPommePomme> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityPommePomme> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityPommePomme(EntityType<? extends EntityPommePomme> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 1.15;
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
    protected boolean isChargingAnim(AnimatedAction anim) {
        return anim.is(CHARGE_ATTACK);
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

    @Override
    public Vec3 passengerOffset(Entity passenger) {
        return new Vec3(0, 16 / 16d, -5 / 16d);
    }
}
