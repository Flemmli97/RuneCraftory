package io.github.flemmli97.runecraftory.common.entities.monster.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;

public class LookAtAliveGoal extends LookAtPlayerGoal {

    public LookAtAliveGoal(Mob mob, Class<? extends LivingEntity> clss, float dist) {
        super(mob, clss, dist);
    }

    @Override
    public boolean canUse() {
        return this.mob.isAlive() && super.canUse();
    }
}
