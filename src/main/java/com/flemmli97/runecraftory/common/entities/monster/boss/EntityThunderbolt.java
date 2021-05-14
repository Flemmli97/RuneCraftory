package com.flemmli97.runecraftory.common.entities.monster.boss;

import com.flemmli97.runecraftory.common.entities.AnimationType;
import com.flemmli97.runecraftory.common.entities.BossMonster;
import com.flemmli97.runecraftory.common.entities.monster.ai.ThunderboltAttackGoal;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;

public class EntityThunderbolt extends BossMonster {

    //Turns and kicks with backlegs
    private static final AnimatedAction back_kick = new AnimatedAction(13, 7, "back_kick");
    //Shoots out 5 wind lazer beams in front
    private static final AnimatedAction laser_x5 = new AnimatedAction(29, 25, "laser_x5");
    //AOE around thunderbolt, throws player into air. after that tries to kick him away if players staggering
    private static final AnimatedAction stomp = new AnimatedAction(9, 6, "stomp");
    //Horn attack: throws player into air and then kicks him away
    private static final AnimatedAction horn_attack = new AnimatedAction(9, 5, "horn_attack");
    private static final AnimatedAction back_kick_horn = new AnimatedAction(13, 7, "back_kick_horn", "back_kick");

    //Charges 2 times at the player. 2-3 when enraged
    private static final AnimatedAction charge = new AnimatedAction(31, 12, "charge");
    private static final AnimatedAction charge_2 = new AnimatedAction(31, 12, "charge_2", "charge");
    private static final AnimatedAction charge_3 = new AnimatedAction(31, 12, "charge_3", "charge");

    //Shoots lazers in all directions. only enraged
    private static final AnimatedAction laser_aoe = new AnimatedAction(29, 25, "laser_aoe");
    //Kicks with backlegs causes wind lazerbeam in looking direction. only enraged
    private static final AnimatedAction laser_kick = new AnimatedAction(16, 6, "laser_kick");
    private static final AnimatedAction laser_kick_2 = new AnimatedAction(16, 6, "laser_kick_2", "laser_kick");

    //Shoots 2 slight homing wind blades
    private static final AnimatedAction wind_blade = new AnimatedAction(15, 8, "wind_blade");
    //Used after feinting death
    private static final AnimatedAction laser_kick_3 = new AnimatedAction(16, 6, "laser_kick_3", "laser_kick");

    private static final AnimatedAction feint = new AnimatedAction(40, 2, "feint");
    private static final AnimatedAction defeat = new AnimatedAction(80, 60, "defeat");

    private static final AnimatedAction neigh = new AnimatedAction(24, 9, "neigh");

    public static final ImmutableList<String> nonChoosableAttacks = ImmutableList.of(charge_2.getID(), charge_3.getID(), laser_kick_2.getID(), laser_kick_3.getID(), back_kick_horn.getID());

    private static final AnimatedAction[] anims = new AnimatedAction[]{back_kick, laser_x5, stomp, horn_attack, back_kick_horn, charge, charge_2, charge_3,
            laser_aoe, laser_kick, laser_kick_2, wind_blade, laser_kick_3, feint, defeat, neigh};

    public final ThunderboltAttackGoal attack = new ThunderboltAttackGoal(this);

    protected boolean feintedDeath, hornAttackSuccess;

    public EntityThunderbolt(EntityType<? extends BossMonster> type, World world) {
        super(type, world);
        this.bossInfo.setColor(BossInfo.Color.BLUE);
        if (!world.isRemote)
            this.goalSelector.addGoal(1, this.attack);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.31);
        super.applyAttributes();
    }

    @Override
    public void setEnraged(boolean flag, boolean load) {
        if (flag && !load) {
            if (!this.isEnraged())
                this.setAnimation(neigh);
            else
                this.setAnimation(defeat);
        }
        super.setEnraged(flag, load);
    }

    public boolean isRunning() {
        return this.getMoveFlag() == 2;
    }

    @Override
    public void setMoving(boolean flag) {
        if (flag && (this.moveController.getSpeed() > 1 || this.isBeingRidden()))
            this.setMoveFlag(2);
        else
            super.setMoving(flag);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return (this.getAnimation() == null || !(this.getAnimation().getID().equals(feint.getID()) || this.getAnimation().getID().equals(defeat.getID())) || this.getAnimation().getID().equals(neigh.getID())) && super.attackEntityFrom(source, amount);
    }

    @Override
    protected boolean checkRage() {
        if (this.getHealth() / this.getMaxHealth() < 0.3)
            return !this.feintedDeath;
        if (this.getHealth() / this.getMaxHealth() < 0.7)
            return !this.isEnraged();
        return false;
    }

    @Override
    public void livingTick() {
        super.livingTick();
        if (!this.world.isRemote && this.getAnimation() != null && this.getAnimation().getID().equals(defeat.getID()) && !this.feintedDeath) {
            if (this.getAnimation().getTick() > this.getAnimation().getLength()) {
                this.feintedDeath = true;
                this.setAnimation(feint);
            }
        }
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (anim.getID().equals(neigh.getID()) || anim.getID().equals(feint.getID()) || anim.getID().equals(defeat.getID()))
            return type == AnimationType.IDLE;
        if (type == AnimationType.GENERICATTACK)
            return this.isEnraged() ? !anim.getID().equals(laser_x5.getID()) : !anim.getID().equals(laser_aoe.getID());
        return false;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {

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
        if (this.getAnimation() == null) {
            if (command == 2)
                this.setAnimation(laser_x5);
            else if (command == 1)
                this.setAnimation(stomp);
            else
                this.setAnimation(horn_attack);
        }
    }

    @Override
    protected void playDeathAnimation() {
        this.setAnimation(defeat);
    }

    @Override
    public int animationCooldown(AnimatedAction anim) {
        int diffAdd = this.difficultyCooldown() / 2;
        if (anim != null)
            switch (anim.getID()) {
                case "laser_kick_2":
                case "laser_kick_3":
                case "charge_2":
                case "charge_3":
                case "back_kick_horn":
                    return 2;
            }
        return 18 + this.getRNG().nextInt(22) - (this.isEnraged() ? 12 : 0) + diffAdd;
    }

    public boolean isAnimEqual(String prev, AnimatedAction other) {
        if (other == null)
            return true;
        if (prev.equals(laser_kick_2.getID()) || prev.equals(laser_kick_3.getID()))
            return other.getID().equals(laser_kick.getID());
        if (prev.equals(back_kick.getID()))
            return other.getID().equals(horn_attack.getID());
        return prev.equals(other.getID());
    }

    public AnimatedAction chainAnim(String prev) {
        switch (prev) {
            case "laser_kick":
                return laser_kick_2;
            case "laser_kick_2":
                return this.feintedDeath ? laser_kick_3 : null;
            case "horn_attack":
                return back_kick_horn;
            case "charge":
                return charge_2;
            case "charge_2":
                return this.isEnraged() ? charge_3 : null;
        }
        return null;
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("Feint", this.feintedDeath);
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.feintedDeath = compound.getBoolean("Feint");
    }

    @Override
    public double ridingSpeedModifier() {
        return 1.1;
    }

    @Override
    public double getMountedYOffset() {
        return this.getHeight() * 0.825D;
    }

    @Override
    public void setAnimation(AnimatedAction anim) {
        if (this.getAnimation() != null && this.getAnimation().getID().equals(defeat.getID()) && anim == null) {
        } else
            super.setAnimation(anim);
    }
}