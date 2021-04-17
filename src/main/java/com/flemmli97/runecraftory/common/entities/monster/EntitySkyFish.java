package com.flemmli97.runecraftory.common.entities.monster;

import com.flemmli97.runecraftory.common.entities.AnimationType;
import com.flemmli97.runecraftory.common.entities.BaseMonster;
import com.flemmli97.runecraftory.common.entities.SwimWalkMoveController;
import com.flemmli97.runecraftory.common.entities.monster.ai.AnimatedRangedGoal;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class EntitySkyFish extends BaseMonster {

    public AnimatedRangedGoal<EntitySkyFish> rangedGoal = new AnimatedRangedGoal<>(this, 8, e -> true);
    protected final SwimmerPathNavigator waterNavigator;
    protected final GroundPathNavigator groundNavigator;

    public EntitySkyFish(EntityType<? extends BaseMonster> type, World world) {
        super(type, world);
        this.setPathPriority(PathNodeType.WATER, 0.0F);
        //this.goalSelector.addGoal(2, this.ai);
        this.moveController = new SwimWalkMoveController(this);
        this.goalSelector.removeGoal(this.swimGoal);
        this.waterNavigator = new SwimmerPathNavigator(this, world);
        this.groundNavigator = new GroundPathNavigator(this, world);
        this.wander.setExecutionChance(3);
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        return false;
    }

    @Override
    public float attackChance(AnimationType type) {
        return 0;
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return new AnimatedAction[0];
    }

    @Override
    public void handleRidingCommand(int command) {

    }

    //==========Water stuff

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean isPushedByWater() {
        return false;
    }

    @Override
    public boolean canBeRiddenInWater() {
        return false;
    }

    @Override
    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.WATER;
    }

    @Override
    public void updateSwimming() {
        if (!this.world.isRemote) {
            if (this.isServerWorld() && this.isInWater()) {
                this.navigator = this.waterNavigator;
                this.setSwimming(true);
            } else {
                this.navigator = this.groundNavigator;
                this.setSwimming(false);
            }
        }
    }

    @Override
    public void travel(Vector3d vec) {
        if (this.isServerWorld() && this.isInWater()) {
            this.handleWaterTravel(vec);
        } else {
            super.travel(vec);
        }
    }

    @Override
    public double ridingSpeedModifier() {
        return 0.4;
    }
}
