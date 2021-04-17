package com.flemmli97.runecraftory.common.entities.monster;

import com.flemmli97.runecraftory.common.entities.AnimationType;
import com.flemmli97.runecraftory.common.entities.BaseMonster;
import com.flemmli97.runecraftory.common.entities.monster.ai.AnimatedRangedGoal;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class EntityWeagle extends BaseMonster {

    public AnimatedRangedGoal<EntityWeagle> rangedGoal = new AnimatedRangedGoal<>(this, 8, e -> true);

    public EntityWeagle(EntityType<? extends BaseMonster> type, World world) {
        super(type, world);
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        return false;
    }

    @Override
    public float attackChance(AnimationType type) {
        return 0;
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return new AnimatedAction[0];
    }

    @Override
    public void handleRidingCommand(int command) {

    }
}
