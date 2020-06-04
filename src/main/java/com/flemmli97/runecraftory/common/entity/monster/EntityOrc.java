package com.flemmli97.runecraftory.common.entity.monster;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.entity.ai.EntityAIMeleeBase;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

import net.minecraft.world.World;

public class EntityOrc extends EntityMobBase {

    public EntityAIMeleeBase<EntityOrc> attack = new EntityAIMeleeBase<EntityOrc>(this);
    private static final AnimatedAction melee1 = new AnimatedAction(22,14, "attack1");
    private static final AnimatedAction melee2 = new AnimatedAction(23,13, "attack2");

    private static final AnimatedAction[] anims = new AnimatedAction[] {melee1, melee2};

    public EntityOrc(World world) {
        super(world);
        this.setSize(0.73f, 2.4f);

        this.tasks.addTask(2, this.attack);
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
    public double maxAttackRange(AnimatedAction anim){
        if(anim.getID().equals("attack2"))
            return 1.2;
        return 1.1;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if(type==AnimationType.MELEE)
            return anim.getID().equals("attack1") || anim.getID().equals("attack2");
        return true;
    }
}
