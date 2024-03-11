package io.github.flemmli97.runecraftory.common.entities.ai.boss;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.ai.AnimatedMonsterAttackGoal;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityDeadTree;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;

public class DeadTreeAttackGoal<T extends EntityDeadTree> extends AnimatedMonsterAttackGoal<T> {

    private int moveDelay;
    private boolean moveFlag;

    public DeadTreeAttackGoal(T entity) {
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
        this.moveToWithDelay(1.2);
        if (this.next.is(EntityDeadTree.APPLE_SHIELD)) {
            this.movementDone = true;
            return;
        }
        if (!this.moveFlag) {
            this.pathFindDelay = 0;
            this.moveDelay = 35 + this.attacker.getRandom().nextInt(15);
            this.moveFlag = true;
        } else if (this.moveDelay-- <= 0 || this.distanceToTargetSq < 4) {
            this.movementDone = true;
            this.moveFlag = false;
        }
    }
}