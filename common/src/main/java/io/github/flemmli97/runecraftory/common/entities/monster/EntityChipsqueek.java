package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.ChargeAttackGoal;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityChipsqueek extends ChargingMonster {

    public static final AnimatedAction MELEE = new AnimatedAction(11, 6, "tail_slap");
    public static final AnimatedAction ROLL = new AnimatedAction(12, 2, "roll");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(MELEE, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(1, "sleep").infinite().changeDelay(AnimationHandler.DEFAULT_ADJUST_TIME).build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{MELEE, ROLL, MELEE, SLEEP};

    public final ChargeAttackGoal<EntityChipsqueek> attack = new ChargeAttackGoal<>(this);
    private final AnimationHandler<EntityChipsqueek> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityChipsqueek(EntityType<? extends EntityChipsqueek> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    public float attackChance(AnimationType type) {
        return 0.8f;
    }

    @Override
    public float chargingLength() {
        return 5;
    }

    @Override
    public AnimationHandler<EntityChipsqueek> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        return switch (type) {
            case MELEE -> anim.is(MELEE);
            case CHARGE -> anim.is(ROLL);
            default -> false;
        };
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 0.8;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2)
                this.getAnimationHandler().setAnimation(ROLL);
            else
                this.getAnimationHandler().setAnimation(MELEE);
        }
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
