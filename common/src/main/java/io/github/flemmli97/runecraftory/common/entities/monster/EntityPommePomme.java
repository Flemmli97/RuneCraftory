package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.ChargeAttackGoal;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityPommePomme extends ChargingMonster {

    public static final AnimatedAction CHARGE_ATTACK = new AnimatedAction(34, 2, "roll");
    public static final AnimatedAction KICK = new AnimatedAction(17, 11, "kick");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(KICK, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(1, "sleep").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{KICK, CHARGE_ATTACK, INTERACT, SLEEP};

    public final ChargeAttackGoal<EntityPommePomme> ai = new ChargeAttackGoal<>(this);
    private final AnimationHandler<EntityPommePomme> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityPommePomme(EntityType<? extends EntityPommePomme> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.ai);
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.CHARGE) {
            return anim.is(CHARGE_ATTACK);
        }
        return type == AnimationType.MELEE && anim.is(KICK);
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 1;
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
    public float attackChance(AnimationType type) {
        if (type == AnimationType.MELEE)
            return 0.7f;
        return 1;
    }

    @Override
    public AnimationHandler<? extends EntityPommePomme> getAnimationHandler() {
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
