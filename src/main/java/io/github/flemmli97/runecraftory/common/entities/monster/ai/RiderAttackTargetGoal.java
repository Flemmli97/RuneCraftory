package io.github.flemmli97.runecraftory.common.entities.monster.ai;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;

public class RiderAttackTargetGoal extends Goal {

    protected final MobEntity goalOwner;
    private MobEntity controller;
    private final int updateChance;

    public RiderAttackTargetGoal(MobEntity entity, int updateChance) {
        this.goalOwner = entity;
        this.updateChance = updateChance;
    }

    @Override
    public boolean shouldExecute() {
        if (this.goalOwner.getControllingPassenger() instanceof MobEntity) {
            this.controller = (MobEntity) this.goalOwner.getControllingPassenger();

            return this.goalOwner.getRNG().nextInt(this.updateChance) == 0;
        }
        return false;
    }

    @Override
    public void startExecuting() {
        this.goalOwner.setAttackTarget(this.controller.getAttackTarget());
    }
}
