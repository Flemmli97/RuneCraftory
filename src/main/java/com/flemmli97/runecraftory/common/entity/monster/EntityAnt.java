package com.flemmli97.runecraftory.common.entity.monster;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.entity.ai.EntityAIMeleeBase;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

import net.minecraft.world.World;

public class EntityAnt extends EntityMobBase {

    public EntityAIMeleeBase<EntityAnt> attack = new EntityAIMeleeBase<EntityAnt>(this);
    private static final AnimatedAction melee = new AnimatedAction(23,12, "attack");

    private static final AnimatedAction[] anims = new AnimatedAction[] {melee};

    public EntityAnt(World world) {
        super(world);
        this.setSize(1.1f, 0.44f);
        this.tasks.addTask(2, this.attack);
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
    public double maxAttackRange(AnimatedAction anim){
        return 0.7;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        return type==AnimationType.MELEE?anim.getID().equals("attack"):true;
    }
}
