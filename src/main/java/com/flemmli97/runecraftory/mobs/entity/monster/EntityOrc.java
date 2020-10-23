package com.flemmli97.runecraftory.mobs.entity.monster;

import com.flemmli97.runecraftory.mobs.entity.BaseMonster;
import com.flemmli97.runecraftory.mobs.entity.monster.ai.AnimatedMeleeGoal;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class EntityOrc extends BaseMonster {

    public AnimatedMeleeGoal<EntityOrc> attack = new AnimatedMeleeGoal<>(this);
    private static final AnimatedAction melee1 = new AnimatedAction(22, 14, "attack1");
    private static final AnimatedAction melee2 = new AnimatedAction(23, 13, "attack2");

    private static final AnimatedAction[] anims = new AnimatedAction[]{melee1, melee2};

    public EntityOrc(EntityType<? extends EntityOrc> type, World world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    public float attackChance() {
        return 0.8f;
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return anims;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        if (anim.getID().equals(melee2.getID()))
            return 1.2;
        return 1.1;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.MELEE)
            return anim.getID().equals(melee1.getID()) || anim.getID().equals(melee2.getID());
        return false;
    }
}
