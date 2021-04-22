package com.flemmli97.runecraftory.common.entities.monster;

import com.flemmli97.runecraftory.common.entities.AnimationType;
import com.flemmli97.runecraftory.common.entities.BaseMonster;
import com.flemmli97.runecraftory.common.entities.monster.ai.AirWanderGoal;
import com.flemmli97.runecraftory.common.entities.monster.ai.AnimatedRangedGoal;
import com.flemmli97.runecraftory.common.entities.monster.ai.FloatingFlyNavigator;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.world.World;

public class EntityWeagle extends BaseMonster {

    public AnimatedRangedGoal<EntityWeagle> rangedGoal = new AnimatedRangedGoal<>(this, 8, e -> true);
    public static final AnimatedAction gale = new AnimatedAction(19, 5, "gale");
    public static final AnimatedAction peck = new AnimatedAction(11, 4, "peck");
    public static final AnimatedAction swoop = new AnimatedAction(14, 4, "swoop");

    private static final AnimatedAction[] anims = new AnimatedAction[]{gale, peck, swoop};

    public EntityWeagle(EntityType<? extends BaseMonster> type, World world) {
        super(type, world);
        this.goalSelector.removeGoal(this.wander);
        this.goalSelector.addGoal(2, this.wander = new AirWanderGoal(this));
        this.goalSelector.addGoal(2, this.rangedGoal);
        this.moveController = new FlyingMovementController(this, 20, true);
        this.setNoGravity(true);
    }

    @Override
    protected PathNavigator createNavigator(World world) {
        return new FloatingFlyNavigator(this, world);
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.RANGED) {
            return anim.getID().equals(gale.getID());
        }
        return type == AnimationType.MELEE && (anim.getID().equals(peck.getID()) || anim.getID().equals(swoop.getID()));
    }

    @Override
    public float attackChance(AnimationType type) {
        return 1;
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return anims;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (this.getAnimations() == null) {
            switch (command) {
                case 2:
                    this.setAnimation(gale);
                    break;
                case 1:
                    this.setAnimation(swoop);
                    break;
                default:
                    this.setAnimation(peck);
                    break;
            }
        }
    }
}
