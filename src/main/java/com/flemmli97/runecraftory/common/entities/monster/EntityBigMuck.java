package com.flemmli97.runecraftory.common.entities.monster;

import com.flemmli97.runecraftory.common.entities.AnimationType;
import com.flemmli97.runecraftory.common.entities.BaseMonster;
import com.flemmli97.runecraftory.common.entities.monster.ai.AnimatedMeleeGoal;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class EntityBigMuck extends BaseMonster {

    public final AnimatedMeleeGoal<EntityBigMuck> ai = new AnimatedMeleeGoal<>(this);
    public static final AnimatedAction slapAttack = new AnimatedAction(24, 7, "slap");
    public static final AnimatedAction sporeAttack = new AnimatedAction(44, 40, "spore");

    private static final AnimatedAction[] anims = new AnimatedAction[]{slapAttack, sporeAttack};

    public EntityBigMuck(EntityType<? extends EntityBigMuck> type, World world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.ai);
    }

    @Override
    public float attackChance(AnimationType type) {
        return 0.9f;
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return anims;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 0.8;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.MELEE)
            return anim.getID().equals(slapAttack.getID()) || anim.getID().equals(sporeAttack.getID());
        return false;
    }
}
