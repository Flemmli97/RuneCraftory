package com.flemmli97.runecraftory.common.entities.monster.ai;

import com.flemmli97.runecraftory.common.entities.monster.boss.EntityThunderbolt;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.flemmli97.tenshilib.common.entity.ai.AnimatedAttackGoal;

public class ThunderboltAttackGoal<T extends EntityThunderbolt> extends AnimatedAttackGoal<T> {

    public ThunderboltAttackGoal(T entity) {
        super(entity);
    }

    @Override
    public AnimatedAction randomAttack() {
        return null;
    }

    @Override
    public void handlePreAttack() {

    }

    @Override
    public void handleAttack(AnimatedAction animatedAction) {

    }

    @Override
    public void handleIddle() {

    }

    @Override
    public int coolDown(AnimatedAction animatedAction) {
        return 0;
    }
}
