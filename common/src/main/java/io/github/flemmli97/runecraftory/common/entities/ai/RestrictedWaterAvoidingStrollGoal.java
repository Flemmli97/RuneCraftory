package io.github.flemmli97.runecraftory.common.entities.ai;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class RestrictedWaterAvoidingStrollGoal extends RandomStrollGoal {

    protected final BaseMonster monster;

    public RestrictedWaterAvoidingStrollGoal(BaseMonster monster, double d) {
        super(monster, d);
        this.monster = monster;
    }

    @Override
    @Nullable
    protected Vec3 getPosition() {
        if (this.mob.isInWaterOrBubble()) {
            Vec3 vec3 = LandRandomPos.getPos(this.mob, 15, 7);
            return vec3 == null ? super.getPosition() : vec3;
        }
        if (this.monster.behaviourState() == BaseMonster.Behaviour.WANDER_HOME && this.mob.getRestrictRadius() > 0)
            return DefaultRandomPos.getPos(this.mob, (int) (this.mob.getRestrictRadius() * 2), 0);
        return super.getPosition();
    }
}
