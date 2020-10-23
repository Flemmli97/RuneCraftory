package com.flemmli97.runecraftory.mobs.entity.monster;

import com.flemmli97.runecraftory.mobs.entity.ChargingMonster;
import com.flemmli97.runecraftory.mobs.entity.monster.ai.ChargeAttackGoal;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class EntityBeetle extends ChargingMonster {

    public final ChargeAttackGoal<EntityBeetle> ai = new ChargeAttackGoal<>(this);
    public static final AnimatedAction chargeAttack = new AnimatedAction(30, 2, "ramm");
    public static final AnimatedAction melee = new AnimatedAction(15, 8, "attack");

    private static final AnimatedAction[] anims = new AnimatedAction[]{melee, chargeAttack};

    public EntityBeetle(EntityType<? extends EntityBeetle> type, World world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.ai);
    }

    @Override
    public float attackChance() {
        return 1.1f;
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return anims;
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
}
