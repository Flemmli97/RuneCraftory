package io.github.flemmli97.runecraftory.common.entities.ai;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public class RiderAttackTargetGoal extends Goal {

    protected final Mob goalOwner;
    private final int updateChance;
    private Mob controller;

    public RiderAttackTargetGoal(Mob entity, int updateChance) {
        this.goalOwner = entity;
        this.updateChance = updateChance;
    }

    @Override
    public boolean canUse() {
        if (this.goalOwner.getControllingPassenger() instanceof Mob) {
            this.controller = (Mob) this.goalOwner.getControllingPassenger();
            return this.goalOwner.getRandom().nextInt(this.updateChance) == 0;
        }
        return false;
    }

    @Override
    public void start() {
        this.goalOwner.setTarget(this.controller.getTarget());
    }
}
