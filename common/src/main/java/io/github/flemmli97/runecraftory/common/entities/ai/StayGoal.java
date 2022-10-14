package io.github.flemmli97.runecraftory.common.entities.ai;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;
import java.util.function.Function;

public class StayGoal<T extends Mob> extends Goal {

    public static Function<BaseMonster, Boolean> CANSTAYMONSTER = monster -> {
        if (!monster.isTamed()) {
            return false;
        }
        if (monster.isInWaterOrBubble() && !monster.canBreatheUnderwater()) {
            return false;
        }
        if (!monster.isOnGround() && !monster.isNoGravity()) {
            return false;
        }
        LivingEntity livingEntity = monster.getOwner();
        if (livingEntity == null) {
            return true;
        }
        return monster.isStaying();
    };

    private final T mob;
    private final Function<T, Boolean> canStay;

    public StayGoal(T mob, Function<T, Boolean> canStay) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
        this.canStay = canStay;
    }

    @Override
    public boolean canUse() {
        return this.canStay.apply(this.mob);
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void start() {
        this.mob.getNavigation().stop();
    }
}

