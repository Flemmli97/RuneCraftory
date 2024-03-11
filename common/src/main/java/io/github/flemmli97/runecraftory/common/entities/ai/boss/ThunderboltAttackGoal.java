package io.github.flemmli97.runecraftory.common.entities.ai.boss;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.ai.AnimatedMonsterAttackGoal;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityThunderbolt;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.core.BlockPos;

public class ThunderboltAttackGoal<T extends EntityThunderbolt> extends AnimatedMonsterAttackGoal<T> {

    private int moveDelay, idleData;
    private boolean moveFlag, idleFlag;

    public ThunderboltAttackGoal(T entity) {
        super(entity);
    }

    @Override
    public boolean canUse() {
        return !this.attacker.canBeControlledByRider() && super.canUse();
    }

    @Override
    public void stop() {
        super.stop();
        this.moveDelay = 0;
        this.moveFlag = false;
    }

    @Override
    public AnimatedAction randomAttack() {
        AnimatedAction anim = this.attacker.chainAnim(this.prevAnim);
        if (anim == null) {
            anim = this.attacker.getRandomAnimation(AnimationType.GENERICATTACK);
            if (EntityThunderbolt.NON_CHOOSABLE_ATTACKS.contains(anim.getID()))
                return this.randomAttack();
        }
        if (!this.attacker.isAnimEqual(this.prevAnim, anim)) {
            return anim;
        }
        return this.randomAttack();
    }

    @Override
    public void handlePreAttack() {
        this.idleFlag = false;
        this.idleData = 0;
        switch (this.next.getID()) {
            case "laser_x5":
            case "laser_aoe":
                if (!this.moveFlag) {
                    if (this.distanceToTargetSq > 49.0) {
                        this.moveToWithDelay(1.2);
                    } else if (this.distanceToTargetSq < 9.0) {
                        BlockPos pos = this.randomPosAwayFrom(this.target, 5.0f);
                        this.attacker.getNavigation().moveTo(pos.getX(), pos.getY(), pos.getZ(), 1);
                    }
                    this.moveDelay = 27 + this.attacker.getRandom().nextInt(10);
                    this.moveFlag = true;
                } else if (this.moveDelay-- <= 0 || this.attacker.getNavigation().isDone()) {
                    this.movementDone = true;
                    this.attacker.getLookControl().setLookAt(this.target, 30.0f, 30.0f);
                    this.moveFlag = false;
                }
            case "wind_blade":
                if (!this.moveFlag) {
                    if (this.distanceToTargetSq > 36.0) {
                        this.moveToWithDelay(1.2);
                    }
                    this.moveDelay = 44 + this.attacker.getRandom().nextInt(10);
                    this.moveFlag = true;
                } else if (this.moveDelay-- <= 0 || this.attacker.getNavigation().isDone() || this.distanceToTargetSq < 25) {
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
                    this.moveDelay = 35 + this.attacker.getRandom().nextInt(15);
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
                    this.moveDelay = 37 + this.attacker.getRandom().nextInt(6);
                    this.moveFlag = true;
                } else if (this.moveDelay-- <= 0 || this.attacker.getNavigation().isDone()) {
                    this.movementDone = true;
                    this.moveFlag = false;
                    this.attacker.setChargeMotion(this.attacker.getChargeTo(this.next, this.target.position().subtract(this.attacker.position())));
                    this.attacker.lookAt(this.target, 360, 10);
                    this.attacker.lockYaw(this.attacker.getYRot());
                }
                break;
            case "charge_2":
            case "charge_3":
                this.attacker.setChargeMotion(this.attacker.getChargeTo(this.next, this.target.position().subtract(this.attacker.position())));
                this.attacker.lookAt(this.target, 360, 10);
                this.attacker.lockYaw(this.attacker.getYRot());
                this.movementDone = true;
                break;
            case "back_kick_horn":
            case "laser_kick_2":
            case "laser_kick_3":
                this.attacker.getLookControl().setLookAt(this.target, 360, 10);
                this.movementDone = true;
                break;
        }
    }

    @Override
    public void handleAttack(AnimatedAction animatedAction) {
    }

    @Override
    public void handleIdle() {
        if (!this.idleFlag && this.idleTime > 2) {
            this.idleFlag = true;
            if (this.attacker.getRandom().nextBoolean()) {
                BlockPos pos = this.randomPosAwayFrom(this.target, 5);
                this.moveToWithDelay(pos.getX(), pos.getY(), pos.getZ(), 1.1);
            } else {
                this.idleData = this.attacker.getRandom().nextInt(2) + 1;
            }
        }
        if (this.idleData != 0)
            this.circleAroundTargetFacing(7, this.idleData == 1, 1.1f);
    }

    @Override
    public int coolDown(AnimatedAction anim) {
        return this.attacker.animationCooldown(this.next);
    }

    public void setIdleTime(int time) {
        this.idleTime = time;
    }

}
