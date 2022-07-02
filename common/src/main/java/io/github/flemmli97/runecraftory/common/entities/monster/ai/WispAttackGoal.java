package io.github.flemmli97.runecraftory.common.entities.monster.ai;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.monster.wisp.EntityWispBase;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;

public class WispAttackGoal<T extends EntityWispBase> extends AnimatedMeleeGoal<T> {

    private final float reach;

    public WispAttackGoal(T entity, float reach) {
        super(entity);
        this.reach = reach;
    }

    @Override
    public AnimatedAction randomAttack() {
        if (this.attacker.shouldVanishNext(this.prevAnim) || this.distanceToTargetSq > 100)
            return EntityWispBase.vanish;
        AnimatedAction anim = this.attacker.getRandomAnimation(AnimationType.RANGED);
        if (this.distanceToTargetSq <= this.reach)
            return anim;
        return this.attacker.getRandomAnimation(AnimationType.IDLE);
    }

    @Override
    public void handlePreAttack() {
        this.movementDone = true;
        this.attacker.getLookControl().setLookAt(this.target, 360, 90);
    }

    @Override
    public void handleIddle() {
        if (this.attacker.getY() < this.target.getY())
            this.attacker.setDeltaMovement(this.attacker.getDeltaMovement().add(0, 0.03, 0));
        if (this.distanceToTargetSq > this.reach - 5)
            this.moveToWithDelay(1);
    }
}