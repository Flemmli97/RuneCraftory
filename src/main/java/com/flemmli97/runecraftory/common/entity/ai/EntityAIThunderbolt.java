package com.flemmli97.runecraftory.common.entity.ai;

import com.flemmli97.runecraftory.common.entity.monster.boss.EntityThunderbolt;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

public class EntityAIThunderbolt extends EntityAIAttackBase<EntityThunderbolt> {

    private double[] circlePoint;
    private boolean clockwise;

    public EntityAIThunderbolt(EntityThunderbolt entity) {
        super(entity);
    }

    @Override
    public AnimatedAction randomAttack() {
        int enraged = this.attacker.isEnraged() ? 0 : 3;
        int feintedDeath = this.attacker.feintedDeath() ? 0 : 1;
        AnimatedAction anim = this.attacker.getAnimations()[this.attacker.getRNG().nextInt(this.attacker.getAnimations().length - enraged - feintedDeath)];
        if (!anim.getID().equals(this.prevAnim)) {
            return anim;
        }
        return this.randomAttack();
    }

    @Override
    public void handlePreAttack() {
        this.movementDone = true;
        this.circlePoint = null;
    }

    @Override
    public void handleIddle() {
        if (this.circlePoint == null) {
            this.circlePoint = new double[] {this.target.posX, this.target.posZ};
            this.clockwise = this.attacker.getRNG().nextBoolean();
        }
        this.circleAround(this.circlePoint[0], this.circlePoint[1], 4, this.clockwise, 1);
    }

}
