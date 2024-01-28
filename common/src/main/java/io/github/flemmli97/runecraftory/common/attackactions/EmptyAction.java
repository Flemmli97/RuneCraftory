package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.AttackAction;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.entity.LivingEntity;

public class EmptyAction extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int chain) {
        return null;
    }

    @Override
    public boolean disableItemSwitch() {
        return false;
    }

    @Override
    public boolean disableMovement(AnimatedAction current) {
        return false;
    }
}
