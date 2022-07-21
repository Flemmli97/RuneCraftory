package io.github.flemmli97.runecraftory.common.entities.monster.ai;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class StayGoal extends Goal {
    private final BaseMonster mob;

    public StayGoal(BaseMonster monster) {
        this.mob = monster;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.mob.isTamed()) {
            return false;
        }
        if (this.mob.isInWaterOrBubble()) {
            return false;
        }
        if (!this.mob.isOnGround()) {
            return false;
        }
        LivingEntity livingEntity = this.mob.getOwner();
        if (livingEntity == null) {
            return true;
        }
        return this.mob.isStaying();
    }

    @Override
    public boolean canContinueToUse() {
        return this.mob.isStaying();
    }

    @Override
    public void start() {
        this.mob.getNavigation().stop();
    }
}

