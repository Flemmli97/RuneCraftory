package com.flemmli97.runecraftory.common.entity.monster;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.entity.ai.EntityAIMeleeBase;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.world.World;

public class EntityBigMuck extends EntityMobBase {

    private EntityAIMeleeBase<EntityBigMuck> ai = new EntityAIMeleeBase<EntityBigMuck>(this);
    private static final AnimatedAction slapAttack = new AnimatedAction(24,7, "slap");
    private static final AnimatedAction sporeAttack = new AnimatedAction(44,40, "spore");

    private static final AnimatedAction[] anims = new AnimatedAction[] {slapAttack, sporeAttack};

    public EntityBigMuck(World world) {
        super(world);
        this.setSize(0.9f, 1.6f);

        this.tasks.addTask(2, this.ai);
    }

    @Override
    public float attackChance() {
        return 0.9f;
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return anims;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim){
        return 0.8;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if(type==AnimationType.MELEE)
            return anim.getID().equals(slapAttack.getID()) || anim.getID().equals(sporeAttack.getID());
        return false;
    }
}
