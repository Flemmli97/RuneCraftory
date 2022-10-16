package io.github.flemmli97.runecraftory.common.entities.ai;

import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;

public class AvoidWhenNotFollowing extends AvoidEntityGoal<LivingEntity> {

    public AvoidWhenNotFollowing(EntityNPCBase npc, Class<LivingEntity> clss, float dist, double walkSpd, double sprintSpd) {
        super(npc, clss, dist, walkSpd, sprintSpd, e -> e == npc.getLastHurtByMob());
    }

    @Override
    public boolean canUse() {
        return ((EntityNPCBase) this.mob).getEntityToFollowUUID() == null && super.canUse();
    }
}
