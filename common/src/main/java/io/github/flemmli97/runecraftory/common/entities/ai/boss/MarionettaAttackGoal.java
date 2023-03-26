package io.github.flemmli97.runecraftory.common.entities.ai.boss;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityMarionetta;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.common.entity.ai.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.utils.MathUtils;
import net.minecraft.world.phys.Vec3;

public class MarionettaAttackGoal<T extends EntityMarionetta> extends AnimatedAttackGoal<T> {

    private int moveDelay;
    private boolean moveFlag, iddleFlag, clockwise;

    public MarionettaAttackGoal(T entity) {
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
        return this.attacker.getRandomAnimation(AnimationType.GENERICATTACK);
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
            this.jumpAround();
        }
        if (this.attacker.getRandom().nextFloat() < 0.0015)
            this.jumpAround();
        this.circleAroundTargetFacing(7, this.clockwise, 1);
    }


    private void jumpAround() {
        if (this.distanceToTargetSq < 6 && this.attacker.getRandom().nextBoolean()) {
            Vec3 dir = this.target.position().subtract(this.attacker.position()).normalize();
            this.attacker.setDeltaMovement(-dir.x(), 0.2, -dir.z());
        } else if (this.attacker.getRandom().nextFloat() < 0.45) {
            Vec3 dir = this.target.position().subtract(this.attacker.position());
            dir = new Vec3(dir.x(), 0, dir.z()).normalize().scale(0.6);
            dir = MathUtils.rotate(new Vec3(0, 1, 0), dir, this.attacker.getRandom().nextBoolean() ? 90 : -90);
            this.attacker.setDeltaMovement(-dir.x(), 0.22, -dir.z());
        }
    }

    @Override
    public int coolDown(AnimatedAction anim) {
        return this.attacker.animationCooldown(this.next);
    }

}
