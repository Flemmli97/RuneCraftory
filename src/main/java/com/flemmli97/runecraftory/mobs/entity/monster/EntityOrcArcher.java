package com.flemmli97.runecraftory.mobs.entity.monster;

import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class EntityOrcArcher extends EntityOrc {

    public EntityOrcArcher(EntityType<? extends EntityOrcArcher> type, World world) {
        super(type, world);
        this.goalSelector.removeGoal(this.attack);
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
