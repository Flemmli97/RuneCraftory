package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.ChargeAttackGoal;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class EntityChipsqueek extends ChargingMonster {

    public final ChargeAttackGoal<EntityChipsqueek> attack = new ChargeAttackGoal<>(this);
    public static final AnimatedAction melee = new AnimatedAction(11, 6, "tail_slap");
    public static final AnimatedAction roll = new AnimatedAction(12, 2, "roll");

    private static final AnimatedAction[] anims = new AnimatedAction[]{melee, roll};

    public EntityChipsqueek(EntityType<? extends EntityChipsqueek> type, World world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    public float attackChance(AnimationType type) {
        return 0.8f;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 0.75;
    }

    @Override
    public float chargingLength() {
        return 5;
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return anims;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        switch (type) {
            case MELEE:
                return anim.getID().equals(melee.getID());
            case CHARGE:
                return anim.getID().equals(roll.getID());
        }
        return false;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (this.getAnimation() == null) {
            if (command == 2)
                this.setAnimation(roll);
            else
                this.setAnimation(melee);
        }
    }
}
