package io.github.flemmli97.runecraftory.common.entities.ai;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class RestrictedWaterAvoidingStrollGoal extends RandomStrollGoal {

    public RestrictedWaterAvoidingStrollGoal(PathfinderMob pathfinderMob, double d) {
        super(pathfinderMob, d);
    }

    @Override
    @Nullable
    protected Vec3 getPosition() {
        if (this.mob.isInWaterOrBubble()) {
            Vec3 vec3 = LandRandomPos.getPos(this.mob, 15, 7);
            return vec3 == null ? super.getPosition() : vec3;
        }
        int radius = 10;
        if (this.mob.hasRestriction())
            radius = (int) (this.mob.getRestrictRadius() * 2);
        return DefaultRandomPos.getPos(this.mob, radius, 7);
    }
}
