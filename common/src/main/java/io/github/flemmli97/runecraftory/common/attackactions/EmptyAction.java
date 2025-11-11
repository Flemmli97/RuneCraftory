package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.common.attachment.WeaponHandler;

public class EmptyAction extends AttackAction {

    @Override
    public boolean disableItemSwitch() {
        return false;
    }

    @Override
    public float movementReduction(WeaponHandler<?> handler) {
        return 1;
    }
}
