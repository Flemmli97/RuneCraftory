package com.flemmli97.runecraftory.common.entities.monster.ai;

import com.flemmli97.runecraftory.common.utils.EntityUtils;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class DisableGoal extends Goal {

    protected final MobEntity entity;

    public DisableGoal(MobEntity entity) {
        this.entity = entity;
        this.setMutexFlags(EnumSet.allOf(Goal.Flag.class));
    }

    @Override
    public boolean shouldExecute() {
        return EntityUtils.isDisabled(this.entity);
    }

    @Override
    public boolean shouldContinueExecuting() {
        return EntityUtils.isDisabled(this.entity);
    }
}
