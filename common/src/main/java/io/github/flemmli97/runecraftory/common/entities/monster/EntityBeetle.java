package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.ChargeAttackGoal;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.level.Level;

public class EntityBeetle extends ChargingMonster {

    public static final AnimatedAction CHARGE_ATTACK = new AnimatedAction(30, 2, "ramm");
    public static final AnimatedAction MELEE = new AnimatedAction(15, 8, "attack");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(MELEE, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(2, "sleep").infinite().changeDelay(AnimationHandler.DEFAULT_ADJUST_TIME).build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{MELEE, CHARGE_ATTACK, INTERACT, SLEEP};
    public final ChargeAttackGoal<EntityBeetle> ai = new ChargeAttackGoal<>(this);
    private final AnimationHandler<EntityBeetle> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityBeetle(EntityType<? extends EntityBeetle> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.ai);
    }

    @Override
    public float attackChance(AnimationType type) {
        return 1f;
    }

    @Override
    public AnimationHandler<EntityBeetle> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public float chargingLength() {
        return 9;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        return switch (type) {
            case CHARGE -> anim.getID().equals(CHARGE_ATTACK.getID()) && this.getRandom().nextFloat() < 0.8;
            case MELEE -> anim.getID().equals(MELEE.getID());
            default -> false;
        };
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2)
                this.getAnimationHandler().setAnimation(CHARGE_ATTACK);
            else
                this.getAnimationHandler().setAnimation(MELEE);
        }
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
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
