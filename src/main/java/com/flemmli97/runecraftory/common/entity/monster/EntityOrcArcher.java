package com.flemmli97.runecraftory.common.entity.monster;

import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.world.World;

public class EntityOrcArcher extends EntityOrc {

    public EntityOrcArcher(World world) {
        super(world);
        this.setSize(0.73f, 2.4f);
        this.tasks.removeTask(this.attack);
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        return false;
    }

    @Override
    public float attackChance() {
        return 0.8f;
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return new AnimatedAction[0];
    }
}
