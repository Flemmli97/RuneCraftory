package io.github.flemmli97.runecraftory.common.entities.monster;


import com.flemmli97.tenshilib.api.entity.AnimatedAction;
import com.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.ChargeAttackGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class EntityBeetle extends ChargingMonster {

    public final ChargeAttackGoal<EntityBeetle> ai = new ChargeAttackGoal<>(this);
    public static final AnimatedAction chargeAttack = new AnimatedAction(30, 2, "ramm");
    public static final AnimatedAction melee = new AnimatedAction(15, 8, "attack");

    private static final AnimatedAction[] anims = new AnimatedAction[]{melee, chargeAttack};

    private final AnimationHandler<EntityBeetle> animationHandler = new AnimationHandler<>(this, anims);

    public EntityBeetle(EntityType<? extends EntityBeetle> type, World world) {
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
        switch (type) {
            case CHARGE:
                return anim.getID().equals(chargeAttack.getID()) && this.getRNG().nextFloat() < 0.8;
            case MELEE:
                return anim.getID().equals(melee.getID());
        }
        return false;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2)
                this.getAnimationHandler().setAnimation(chargeAttack);
            else
                this.getAnimationHandler().setAnimation(melee);
        }
    }
}
