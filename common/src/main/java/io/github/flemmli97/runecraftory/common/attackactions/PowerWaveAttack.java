package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.common.attachment.AttackActionHandler;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class PowerWaveAttack extends SpellUseAttack {

    @Override
    public AnimationState getAnimation(LivingEntity entity, int comboIdx) {
        double speed = EntityUtils.attackSpeedModifier(entity);
        return AttackAction.create(PlayerModelAnimations.POWER_WAVE, speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, AttackActionHandler handler, AnimationState anim) {
        if (anim.isAt("step")) {
            Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1)).scale(0.55);
            entity.setDeltaMovement(dir);
        }
        super.run(entity, stack, handler, anim);
    }
}
