package io.github.flemmli97.runecraftory.common.entities.ai;

import io.github.flemmli97.runecraftory.common.entities.monster.EntityPalmCat;

public class PalmCatAttack<T extends EntityPalmCat> extends LeapingAttackGoal<T> {

    public PalmCatAttack(T entity) {
        super(entity);
    }

    public void resetCooldown() {
        this.iddleTime = 0;
    }
}
