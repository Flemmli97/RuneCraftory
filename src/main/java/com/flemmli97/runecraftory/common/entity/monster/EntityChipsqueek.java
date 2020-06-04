package com.flemmli97.runecraftory.common.entity.monster;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.entity.ai.EntityAIMeleeBase;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

import net.minecraft.world.World;

public class EntityChipsqueek extends EntityMobBase{

    public EntityAIMeleeBase<EntityChipsqueek> attack = new EntityAIMeleeBase<EntityChipsqueek>(this);
    private static final AnimatedAction melee = new AnimatedAction(11,6, "tail_slap");
    private static final AnimatedAction roll = new AnimatedAction(12,4, "roll");

    private static final AnimatedAction[] anims = new AnimatedAction[] {melee, roll};

    public EntityChipsqueek(World world) {
        super(world);
        this.tasks.addTask(2, this.attack);
        this.setSize(0.65f, 1.15f);
    }

    @Override
    public float attackChance() {
        return 0.8f;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim){
        return 0.75;
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return anims;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        return type != AnimationType.MELEE || anim.getID().equals("tail_slap");
    }
}
