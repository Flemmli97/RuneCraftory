package io.github.flemmli97.runecraftory.common.entities.ai;

import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.mixinhelper.IDisableBrain;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class DisableGoal extends Goal {

    protected final Mob entity;

    public DisableGoal(Mob entity) {
        this.entity = entity;
        this.setFlags(EnumSet.allOf(Goal.Flag.class));
    }

    @Override
    public boolean canUse() {
        return EntityUtils.isDisabled(this.entity);
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void start() {
        ((IDisableBrain) this.entity.getBrain()).disableBrain(true);
        this.entity.getNavigation().stop();
    }

    @Override
    public void stop() {
        ((IDisableBrain) this.entity.getBrain()).disableBrain(false);
    }
}
