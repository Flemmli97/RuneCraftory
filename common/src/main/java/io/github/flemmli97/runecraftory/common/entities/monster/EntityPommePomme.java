package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.ChargeAttackGoal;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class EntityPommePomme extends ChargingMonster {

    public static final AnimatedAction CHARGE_ATTACK = new AnimatedAction(34, 2, "roll");
    public static final AnimatedAction KICK = new AnimatedAction(17, 11, "kick");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(KICK, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(2, "sleep").infinite().changeDelay(AnimationHandler.DEFAULT_ADJUST_TIME).build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{KICK, CHARGE_ATTACK, INTERACT, SLEEP};

    public final ChargeAttackGoal<EntityPommePomme> ai = new ChargeAttackGoal<>(this);
    private final AnimationHandler<EntityPommePomme> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityPommePomme(EntityType<? extends EntityPommePomme> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.ai);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2);
        super.applyAttributes();
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.CHARGE) {
            return anim.getID().equals(CHARGE_ATTACK.getID());
        }
        return type == AnimationType.MELEE && anim.getID().equals(KICK.getID());
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 0.8;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2)
                this.getAnimationHandler().setAnimation(CHARGE_ATTACK);
            else
                this.getAnimationHandler().setAnimation(KICK);
        }
    }

    @Override
    public float attackChance(AnimationType type) {
        return 0.9f;
    }

    @Override
    public AnimationHandler<EntityPommePomme> getAnimationHandler() {
        return this.animationHandler;
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
}
