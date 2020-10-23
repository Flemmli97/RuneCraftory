package com.flemmli97.runecraftory.mobs.entity.monster;

import com.flemmli97.runecraftory.mobs.entity.BaseMonster;
import com.flemmli97.runecraftory.mobs.entity.monster.ai.AnimatedMeleeGoal;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class EntityAnt extends BaseMonster {

    public final AnimatedMeleeGoal<EntityAnt> attack = new AnimatedMeleeGoal<>(this);
    public static final AnimatedAction melee = new AnimatedAction(23, 12, "attack");

    private static final AnimatedAction[] anims = new AnimatedAction[]{melee};

    public EntityAnt(EntityType<? extends EntityAnt> type, World world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    public float attackChance() {
        return 1f;
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return anims;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 0.7;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        return type == AnimationType.MELEE && anim.getID().equals(melee.getID());
    }
}
