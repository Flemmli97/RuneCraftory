package io.github.flemmli97.runecraftory.common.entities.monster.ai;

import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.IDisableBrain;
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
        return this.shouldExecute();
    }

    @Override
    public void startExecuting() {
        ((IDisableBrain) this.entity.getBrain()).disableBrain(true);
        this.entity.getNavigator().clearPath();
    }

    @Override
    public void resetTask() {
        ((IDisableBrain) this.entity.getBrain()).disableBrain(false);
    }
}
