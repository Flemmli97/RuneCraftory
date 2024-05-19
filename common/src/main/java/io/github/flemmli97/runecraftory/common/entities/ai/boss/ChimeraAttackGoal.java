package io.github.flemmli97.runecraftory.common.entities.ai.boss;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.ai.AnimatedMonsterAttackGoal;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityChimera;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;

public class ChimeraAttackGoal<T extends EntityChimera> extends AnimatedMonsterAttackGoal<T> {

    private int moveDelay;
    private boolean moveFlag;
    private boolean chargeSuccess;

    public ChimeraAttackGoal(T entity) {
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
        AnimatedAction anim = this.attacker.getRandomAnimation(AnimationType.GENERICATTACK);
        if (anim == null || anim.getID().equals(this.prevAnim)) {
            return this.randomAttack();
        }
        return anim;
    }

    @Override
    public void handlePreAttack() {
        this.moveToWithDelay(1.1);
        if (this.chargeSuccess) {
            this.movementDone = true;
            this.chargeSuccess = false;
            return;
        }
        double dist = this.next.is(EntityChimera.BUBBLE_BEAM, EntityChimera.WATER_TAIL_BUBBLE, EntityChimera.WATER_TAIL_BEAM,
                EntityChimera.FIRE_BREATH, EntityChimera.BUBBLE_BEAM) ? 7 : 4;
        if (!this.moveFlag) {
            this.pathFindDelay = 0;
            this.moveDelay = 30 + this.attacker.getRandom().nextInt(15);
            this.moveFlag = true;
        } else if (this.moveDelay-- <= 0 || this.distanceToTargetSq < dist) {
            this.movementDone = true;
            this.moveFlag = false;
            switch (this.next.getID()) {
                case "leap" -> {
                    this.attacker.setChargeMotion(this.attacker.getChargeTo(this.next, this.target.position().subtract(this.attacker.position())));
                    this.attacker.setAiTarget(f -> this.target.position());
                    this.attacker.lookAt(this.target, 360, 10);
                    this.attacker.lockYaw(this.attacker.getYRot());
                }
                case "tail_beam", "water_tail_bubble", "water_tail_beam",
                     "breath_attack", "bubble_beam" -> {
                    this.attacker.lookAt(this.target, 360, 10);
                    this.attacker.lockYaw(this.attacker.getYRot());
                    this.attacker.setAiTarget(from -> EntityUtil.getStraightProjectileTarget(from, this.target));
                }
            }
        }
    }

    public void chargeSuccess(int time) {
        this.idleTime = time;
        this.chargeSuccess = true;
    }

    @Override
    public void handleIdle() {
        super.handleIdle();
    }
}