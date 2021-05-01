package com.flemmli97.runecraftory.common.entities.monster.ai;

import com.flemmli97.runecraftory.common.entities.AnimationType;
import com.flemmli97.runecraftory.common.entities.monster.boss.EntityAmbrosia;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.flemmli97.tenshilib.common.entity.ai.AnimatedAttackGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class AmbrosiaAttackGoal<T extends EntityAmbrosia> extends AnimatedAttackGoal<T> {

    private int moveDelay;
    private boolean moveFlag, iddleFlag, clockwise;

    public AmbrosiaAttackGoal(T entity) {
        super(entity);
    }

    @Override
    public AnimatedAction randomAttack() {
        AnimatedAction anim = this.attacker.chainAnim(this.prevAnim);
        if (anim == null) {
            anim = this.attacker.getRandomAnimation(AnimationType.GENERICATTACK);
            if (EntityAmbrosia.nonChoosableAttacks.contains(anim.getID()))
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
            case "butterfly":
                if (!this.moveFlag) {
                    if (this.distanceToTargetSq < 20.0) {
                        BlockPos pos = this.randomPosAwayFrom(this.target, 5.0f);
                        this.attacker.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 1);
                    }
                    this.moveDelay = 44 + this.attacker.getRNG().nextInt(10);
                    this.moveFlag = true;
                } else if (this.moveDelay-- <= 0 || this.attacker.getNavigator().noPath()) {
                    this.attacker.setAiVarHelper(new double[]{this.target.getX(), this.target.getY() - 2, this.target.getZ()});
                    this.movementDone = true;
                    this.moveFlag = false;
                }
                break;
            case "sleep":
            case "kick_1":
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
            case "pollen":
                this.moveToWithDelay(1.2);
                if (!this.moveFlag) {
                    this.pathFindDelay = 0;
                    this.moveDelay = 45 + this.attacker.getRNG().nextInt(10);
                    this.moveFlag = true;
                } else if (this.moveDelay-- <= 0 || this.distanceToTargetSq < 4) {
                    Vector3d vec = this.target.getPositionVec().subtract(this.attacker.getPositionVec()).normalize().scale(5);
                    int length = this.next.getLength();
                    this.attacker.setAiVarHelper(new double[]{vec.x / length, 0, vec.z / length});
                    this.movementDone = true;
                    this.moveFlag = false;
                }
                break;
            case "pollen_2":
                this.movementDone = true;
                Vector3d vec = this.target.getPositionVec().subtract(this.attacker.getPositionVec()).normalize().scale(5);
                int length = this.next.getLength();
                this.attacker.setAiVarHelper(new double[]{vec.x / length, 0, vec.z / length});
                break;
            case "wave":
                if (!this.moveFlag) {
                    if (this.distanceToTargetSq > 16) {
                        this.attacker.getNavigator().tryMoveToEntityLiving(this.target, 1.2);
                        this.moveDelay = 10 + this.attacker.getRNG().nextInt(10);
                    }
                    this.moveDelay += 10;
                    this.moveFlag = true;
                } else if (this.moveDelay-- <= 0 || this.attacker.getNavigator().noPath()) {
                    this.movementDone = true;
                    this.moveFlag = false;
                }
                break;
            case "kick_2":
            case "kick_3":
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
            this.clockwise = this.attacker.getRNG().nextBoolean();
            this.iddleFlag = true;
        }
        this.circleAroundTargetFacing(7, this.clockwise, 1);
    }

}
