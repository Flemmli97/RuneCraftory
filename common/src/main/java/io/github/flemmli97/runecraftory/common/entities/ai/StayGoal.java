package io.github.flemmli97.runecraftory.common.entities.ai;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;
import java.util.function.Predicate;

public class StayGoal<T extends Mob> extends Goal {

    public static final Predicate<BaseMonster> CANSTAYMONSTER = monster -> {
        if (!monster.isTamed()) {
            return false;
        }
        if (monster.isInWaterOrBubble() && !monster.canBreatheUnderwater()) {
            return false;
        }
        if (!monster.isOnGround() && !monster.isNoGravity()) {
            return false;
        }
        return monster.isStaying();
    };
    public static final Predicate<EntityNPCBase> CANSTAYNPC = npc -> {
        if (npc.isInWaterOrBubble()) {
            return false;
        }
        if (!npc.isOnGround()) {
            return false;
        }
        return npc.isStaying();
    };

    private final T mob;
    private final Predicate<T> canStay;

    public StayGoal(T mob, Predicate<T> canStay) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
        this.canStay = canStay;
    }

    @Override
    public boolean canUse() {
        return this.canStay.test(this.mob);
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

