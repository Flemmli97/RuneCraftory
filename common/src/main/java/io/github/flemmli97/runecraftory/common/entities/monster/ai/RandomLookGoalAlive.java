package io.github.flemmli97.runecraftory.common.entities.monster.ai;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;

public class RandomLookGoalAlive extends RandomLookAroundGoal {

    private final Mob mob;

    public RandomLookGoalAlive(Mob mob) {
        super(mob);
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        return this.mob.isAlive() && super.canUse();
    }
}
