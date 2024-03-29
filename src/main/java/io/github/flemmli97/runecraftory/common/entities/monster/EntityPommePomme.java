package io.github.flemmli97.runecraftory.common.entities.monster;


import com.flemmli97.tenshilib.api.entity.AnimatedAction;
import com.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.ChargeAttackGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.world.World;

public class EntityPommePomme extends ChargingMonster {

    public final ChargeAttackGoal<EntityPommePomme> ai = new ChargeAttackGoal<>(this);
    public static final AnimatedAction chargeAttack = new AnimatedAction(34, 2, "roll");
    public static final AnimatedAction kick = new AnimatedAction(17, 11, "kick");

    private static final AnimatedAction[] anims = new AnimatedAction[]{kick, chargeAttack};

    private final AnimationHandler<EntityPommePomme> animationHandler = new AnimationHandler<>(this, anims);

    public EntityPommePomme(EntityType<? extends EntityPommePomme> type, World world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.ai);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2);
        super.applyAttributes();
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
    public double maxAttackRange(AnimatedAction anim) {
        return 0.8;
    }

    @Override
    public float chargingLength() {
        return 7;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.CHARGE) {
            return anim.getID().equals(chargeAttack.getID());
        }
        return type == AnimationType.MELEE && anim.getID().equals(kick.getID());
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2)
                this.getAnimationHandler().setAnimation(chargeAttack);
            else
                this.getAnimationHandler().setAnimation(kick);
        }
    }
}
