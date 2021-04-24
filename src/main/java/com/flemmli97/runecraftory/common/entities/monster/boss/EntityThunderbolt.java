package com.flemmli97.runecraftory.common.entities.monster.boss;

import com.flemmli97.runecraftory.common.entities.AnimationType;
import com.flemmli97.runecraftory.common.entities.BossMonster;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class EntityThunderbolt extends BossMonster {

    //Turns and kicks with backlegs
    private static final AnimatedAction back_kick = new AnimatedAction(85,7,"back_kick");
    //Shoots out 5 wind lazer beams in front
    private static final AnimatedAction lazer_x5 = new AnimatedAction(125,5,"lazer_x5");
    //AOE around thunderbolt, throws player into air. after that tries to kick him away if players staggering
    private static final AnimatedAction stomp = new AnimatedAction(12,10,"stomp");
    //Horn attack: throws player into air and then kicks him away
    private static final AnimatedAction horn_attack = new AnimatedAction(36,12,"horn_attack");
    //Charges 2 times at the player. 2-3 when enraged
    private static final AnimatedAction charge = new AnimatedAction(36,12,"charge");
    //Shoots lazers in all directions. only enraged
    private static final AnimatedAction lazer_aoe = new AnimatedAction(36,12,"lazer_aoe");
    //Kicks with backlegs causes wind lazerbeam in looking direction. only enraged
    private static final AnimatedAction lazer_kick = new AnimatedAction(36,12,"lazer_kick");
    //Shoots 2 slight homing wind blades
    private static final AnimatedAction wind_blade = new AnimatedAction(36,12,"wind_blade");
    //Used after feinting death
    private static final AnimatedAction lazer_kick_x3 = new AnimatedAction(36,12,"lazer_kick_x3");

    public EntityThunderbolt(EntityType<? extends BossMonster> type, World world) {
        super(type, world);
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        return false;
    }

    @Override
    public void handleRidingCommand(int command) {

    }

    @Override
    public float attackChance(AnimationType type) {
        return 0;
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return new AnimatedAction[0];
    }
}
