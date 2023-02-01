package io.github.flemmli97.runecraftory.common.entities.ai;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

public class AirWanderGoal extends RandomStrollGoal {

    protected final BaseMonster monster;

    private float prevPrio;

    public AirWanderGoal(BaseMonster entity) {
        super(entity, 1);
        this.monster = entity;
    }

    @Override
    protected Vec3 getPosition() {
        //this.prevPrio = this.creature.getPathPriority(PathNodeType.OPEN);
        //this.creature.setPathPriority(PathNodeType.OPEN, 0);
        int radius = 10;
        if (this.monster.behaviourState() == BaseMonster.Behaviour.WANDER_HOME)
            radius = (int) (this.mob.getRestrictRadius() * 2);
        Vec3 vec = DefaultRandomPos.getPos(this.mob, radius, 7);
        //this.creature.setPathPriority(PathNodeType.OPEN, this.prevPrio);
        BlockPos pos;
        if (vec != null && this.mob.level.getBlockState(pos = new BlockPos(vec).below()).entityCanStandOn(this.mob.level, pos, this.mob)) {
            return vec.add(0, 1, 0);
        }
        return vec;
    }
}
