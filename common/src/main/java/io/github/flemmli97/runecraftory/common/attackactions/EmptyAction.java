package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.tenshilib.common.entity.AnimatedAction;


public class EmptyAction extends AttackAction {

    @Override
    public boolean disableItemSwitch() {
        return false;
    }

    @Override
    public float movementReduction(AnimatedAction current) {
        return 1;
    }
}
