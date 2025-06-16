package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import net.minecraft.world.entity.LivingEntity;

public class SelfBuffSpell extends SpellUseAttack {

    @Override
    public AnimationState getAnimation(LivingEntity entity, int comboIdx) {
        double speed = ItemNBT.attackSpeedModifier(entity);
        return AttackAction.create(PlayerModelAnimations.BUFF, speed);
    }
}
