package io.github.flemmli97.runecraftory.common.entities.ai;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.LeapingMonster;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.phys.AABB;

public class LeapingAttackGoal<T extends LeapingMonster> extends AnimatedMeleeGoal<T> {

    public LeapingAttackGoal(T entity) {
        super(entity);
    }

    @Override
    public AnimatedAction randomAttack() {
        if (this.attacker.getRandom().nextFloat() < this.attacker.attackChance(AnimationType.GENERICATTACK)) {
            if (this.attacker.getRandom().nextFloat() < this.attacker.attackChance(AnimationType.MELEE)) {
                AnimatedAction anim = this.attacker.getRandomAnimation(AnimationType.MELEE);
                AABB aabb = this.attacker.calculateAttackAABB(anim, this.target, 1);
                if (aabb.intersects(this.target.getBoundingBox()))
                    return anim;
            }
            if (this.distanceToTargetSq <= (this.attacker.maxLeapDistance() * this.attacker.maxLeapDistance() + 1) && this.attacker.getY() >= this.target.getY())
                return this.attacker.getRandomAnimation(AnimationType.LEAP);
        }
        return this.attacker.getRandomAnimation(AnimationType.IDLE);
    }

    @Override
    public void handlePreAttack() {
        if (this.attacker.isAnimOfType(this.next, AnimationType.MELEE))
            super.handlePreAttack();
        else {
            this.attacker.lookAt(this.target, 360, 10);
            this.movementDone = true;
        }
    }
}