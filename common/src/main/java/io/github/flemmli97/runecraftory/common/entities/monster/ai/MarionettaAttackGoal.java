package io.github.flemmli97.runecraftory.common.entities.monster.ai;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityMarionetta;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.common.entity.ai.AnimatedAttackGoal;
import net.minecraft.world.phys.Vec3;

public class MarionettaAttackGoal<T extends EntityMarionetta> extends AnimatedAttackGoal<T> {

    private int moveDelay;
    private boolean moveFlag, iddleFlag, clockwise;

    public MarionettaAttackGoal(T entity) {
        super(entity);
    }

    @Override
    public boolean canUse() {
        return !this.attacker.isVehicle() && super.canUse();
    }

    @Override
    public AnimatedAction randomAttack() {
        return this.attacker.getRandomAnimation(AnimationType.GENERICATTACK);
    }

    @Override
    public int coolDown(AnimatedAction anim) {
        return this.attacker.animationCooldown(this.next);
    }

    @Override
    public void stop() {
        super.stop();
        this.moveDelay = 0;
        this.moveFlag = false;
    }

    @Override
    public void handlePreAttack() {
        this.iddleFlag = false;
        switch (this.next.getID()) {
            case "melee":
                this.moveToWithDelay(1.2);
                if (!this.moveFlag) {
                    this.pathFindDelay = 0;
                    this.moveDelay = 50 + this.attacker.getRandom().nextInt(10);
                    this.moveFlag = true;
                } else if (this.moveDelay-- <= 0 || this.distanceToTargetSq < 4) {
                    this.movementDone = true;
                    this.moveFlag = false;
                }
                break;
            case "card_attack":
            case "dark_beam":
                if (this.distanceToTargetSq > 45) {
                    if (!this.attacker.hasLineOfSight(this.target)) {
                        this.moveToWithDelay(1.2);
                        if (!this.moveFlag) {
                            this.pathFindDelay = 0;
                            this.moveDelay = 50 + this.attacker.getRandom().nextInt(10);
                            this.moveFlag = true;
                        } else if (this.moveDelay-- <= 0 || this.distanceToTargetSq < 4) {
                            this.movementDone = true;
                            this.moveFlag = false;
                        }
                    } else {
                        this.movementDone = true;
                        this.attacker.getNavigation().stop();
                    }
                } else
                    this.movementDone = true;
                break;
            case "stuffed_animals":
                if (this.distanceToTargetSq > 36) {
                    this.moveToWithDelay(1.2);
                    if (!this.moveFlag) {
                        this.pathFindDelay = 0;
                        this.moveDelay = 50 + this.attacker.getRandom().nextInt(10);
                        this.moveFlag = true;
                    } else if (this.moveDelay-- <= 0 || this.distanceToTargetSq < 4) {
                        this.movementDone = true;
                        this.moveFlag = false;
                    }
                } else
                    this.movementDone = true;
                break;
            case "spin":
            case "chest_attack":
                this.moveToWithDelay(1.2);
                if (!this.moveFlag) {
                    this.pathFindDelay = 0;
                    this.moveDelay = 45 + this.attacker.getRandom().nextInt(10);
                    this.moveFlag = true;
                } else if (this.moveDelay-- <= 0 || this.distanceToTargetSq < 40) {
                    Vec3 vec = this.target.position().subtract(this.attacker.position()).normalize().scale(8);
                    int length = this.next.getLength();
                    this.attacker.setAiVarHelper(new double[]{vec.x / length, 0, vec.z / length});
                    this.movementDone = true;
                    this.moveFlag = false;
                }
                break;
            case "furniture":
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
            this.clockwise = this.attacker.getRandom().nextBoolean();
            this.iddleFlag = true;
            if (this.distanceToTargetSq < 5 && this.attacker.getRandom().nextBoolean()) {
                Vec3 dir = this.target.position().subtract(this.attacker.position()).normalize();
                this.attacker.setDeltaMovement(-dir.x(), 0.2, -dir.z());
            }
        }
        this.circleAroundTargetFacing(7, this.clockwise, 1);
    }

}
