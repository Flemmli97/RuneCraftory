package com.flemmli97.runecraftory.common.entities.monster.boss;

import com.flemmli97.runecraftory.common.entities.AnimationType;
import com.flemmli97.runecraftory.common.entities.BossMonster;
import com.flemmli97.runecraftory.common.entities.monster.ai.ThunderboltAttackGoal;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;

public class EntityThunderbolt extends BossMonster {

    //Turns and kicks with backlegs
    private static final AnimatedAction back_kick = new AnimatedAction(85, 7, "back_kick");
    //Shoots out 5 wind lazer beams in front
    private static final AnimatedAction laser_x5 = new AnimatedAction(125, 5, "laser_x5");
    //AOE around thunderbolt, throws player into air. after that tries to kick him away if players staggering
    private static final AnimatedAction stomp = new AnimatedAction(12, 10, "stomp");
    //Horn attack: throws player into air and then kicks him away
    private static final AnimatedAction horn_attack = new AnimatedAction(36, 12, "horn_attack");
    //Charges 2 times at the player. 2-3 when enraged
    private static final AnimatedAction charge = new AnimatedAction(36, 12, "charge");
    //Shoots lazers in all directions. only enraged
    private static final AnimatedAction laser_aoe = new AnimatedAction(36, 12, "laser_aoe");
    //Kicks with backlegs causes wind lazerbeam in looking direction. only enraged
    private static final AnimatedAction laser_kick = new AnimatedAction(36, 12, "laser_kick");
    //Shoots 2 slight homing wind blades
    private static final AnimatedAction wind_blade = new AnimatedAction(36, 12, "wind_blade");
    //Used after feinting death
    private static final AnimatedAction laser_kick_x3 = new AnimatedAction(36, 12, "laser_kick_x3");

    private static final AnimatedAction feint = new AnimatedAction(36, 12, "feint");
    private static final AnimatedAction death = new AnimatedAction(36, 12, "death");

    private static final AnimatedAction[] anims = new AnimatedAction[]{back_kick, laser_x5, stomp, horn_attack, laser_aoe, laser_kick, wind_blade, feint, death};

    public final ThunderboltAttackGoal attack = new ThunderboltAttackGoal(this);

    public EntityThunderbolt(EntityType<? extends BossMonster> type, World world) {
        super(type, world);
        this.bossInfo.setColor(BossInfo.Color.BLUE);
        if (!world.isRemote)
            this.goalSelector.addGoal(1, this.attack);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.29);
        super.applyAttributes();
    }

    @Override
    public void setEnraged(boolean flag, boolean load) {
        super.setEnraged(flag, load);

    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        return false;
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

    }

    @Override
    protected void playDeathAnimation() {
        this.setAnimation(death);
    }
}
