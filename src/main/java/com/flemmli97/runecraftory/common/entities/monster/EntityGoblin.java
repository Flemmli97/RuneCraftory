package com.flemmli97.runecraftory.common.entities.monster;

import com.flemmli97.runecraftory.common.entities.AnimationType;
import com.flemmli97.runecraftory.common.entities.ChargingMonster;
import com.flemmli97.runecraftory.common.entities.monster.ai.ChargeAttackGoal;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class EntityGoblin extends ChargingMonster {

    public ChargeAttackGoal<EntityGoblin> attack = new ChargeAttackGoal<>(this);
    private static final AnimatedAction melee = new AnimatedAction(22, 14, "attack");
    private static final AnimatedAction leap = new AnimatedAction(23, 13, "leap");
    private static final AnimatedAction stone = new AnimatedAction(23, 13, "stone");

    private static final AnimatedAction[] anims = new AnimatedAction[]{melee, leap, stone};

    public EntityGoblin(EntityType<? extends EntityGoblin> type, World world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    public float attackChance(AnimationType type) {
        return 0.85f;
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return anims;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        if (anim.getID().equals(stone.getID()))
            return 8;
        return 1;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.getID().equals(stone.getID())) {

        } else
            super.handleAttack(anim);
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.MELEE)
            return anim.getID().equals(melee.getID()) || anim.getID().equals(stone.getID());
        if (type == AnimationType.CHARGE)
            return anim.getID().equals(leap.getID());
        return false;
    }
}
