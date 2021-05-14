package com.flemmli97.runecraftory.common.entities.monster.ai;

import com.flemmli97.runecraftory.common.entities.AnimationType;
import com.flemmli97.runecraftory.common.entities.monster.boss.EntityThunderbolt;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.flemmli97.tenshilib.common.entity.ai.AnimatedAttackGoal;
import net.minecraft.util.math.BlockPos;

public class ThunderboltAttackGoal<T extends EntityThunderbolt> extends AnimatedAttackGoal<T> {

    private int moveDelay;
    private boolean moveFlag, iddleFlag;

    public ThunderboltAttackGoal(T entity) {
        super(entity);
    }

    @Override
    public AnimatedAction randomAttack() {
        AnimatedAction anim = this.attacker.chainAnim(this.prevAnim);
        if (anim == null) {
            anim = this.attacker.getRandomAnimation(AnimationType.GENERICATTACK);
            if (EntityThunderbolt.nonChoosableAttacks.contains(anim.getID()))
                return this.randomAttack();
        }
        if (!this.attacker.isAnimEqual(this.prevAnim, anim)) {
            return anim;
        }
        return this.randomAttack();
    }

    @Override
    public int coolDown(AnimatedAction anim) {
        return this.attacker.animationCooldown(this.next);
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.moveDelay = 0;
        this.moveFlag = false;
    }

    @Override
    public void handlePreAttack() {
        this.iddleFlag = false;
        switch (this.next.getID()) {
            case "laser_x5":
            case "laser_aoe":
                if (!this.moveFlag) {
                    if (this.distanceToTargetSq > 49.0) {
                        this.moveToWithDelay(1.2);
                    } else if (this.distanceToTargetSq < 7.0) {
                        BlockPos pos = this.randomPosAwayFrom(this.target, 5.0f);
                        this.attacker.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 1);
                    }
                    this.moveDelay = 27 + this.attacker.getRNG().nextInt(10);
                    this.moveFlag = true;
                } else if (this.moveDelay-- <= 0 || this.attacker.getNavigator().noPath()) {
                    this.movementDone = true;
                    this.moveFlag = false;
                }
            case "wind_blade":
                if (!this.moveFlag) {
                    if (this.distanceToTargetSq > 25.0) {
                        this.moveToWithDelay(1.2);
                    }
                    this.moveDelay = 44 + this.attacker.getRNG().nextInt(10);
                    this.moveFlag = true;
                } else if (this.moveDelay-- <= 0 || this.attacker.getNavigator().noPath()) {
                    this.movementDone = true;
                    this.moveFlag = false;
                }
                break;
            case "laser_kick":
            case "horn_attack":
            case "stomp":
            case "back_kick":
                this.moveToWithDelay(1.2);
                if (!this.moveFlag) {
                    this.pathFindDelay = 0;
                    this.moveDelay = 50 + this.attacker.getRNG().nextInt(10);
                    this.moveFlag = true;
                } else if (this.moveDelay-- <= 0 || this.distanceToTargetSq < 4) {
                    this.movementDone = true;
                    this.moveFlag = false;
                }
                break;
            case "charge":
                if (!this.moveFlag) {
                    if (this.distanceToTargetSq > 16.0) {
                        this.moveToWithDelay(1.2);
                    }
                    this.moveDelay = 37 + this.attacker.getRNG().nextInt(6);
                    this.moveFlag = true;
                } else if (this.moveDelay-- <= 0 || this.attacker.getNavigator().noPath()) {
                    this.movementDone = true;
                    this.moveFlag = false;
                }
                break;
            case "charge_2":
            case "charge_3":
            case "back_kick_horn":
            case "laser_kick_2":
            case "laser_kick_3":
                this.movementDone = true;
                break;
        }
    }

    @Override
    public void handleAttack(AnimatedAction animatedAction) {
    }

    @Override
    public void handleIddle() {
        if (!this.iddleFlag) {
            this.iddleFlag = true;
            BlockPos pos = this.randomPosAwayFrom(this.target, 7);
            this.moveToWithDelay(pos.getX(), pos.getY(), pos.getZ(), 1.1);
        }
    }

}
