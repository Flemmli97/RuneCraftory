package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.registry.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.common.attachment.AttackActionHandler;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemAxeBase;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class HammerAxeUseAttack extends TimedUseAttack {

    public HammerAxeUseAttack() {
        super(PlayerModelAnimations.HAMME_AXE_USE, ItemAxeBase::delayedRightClickAction);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, AttackActionHandler handler, AnimationState anim) {
        if (anim.isAt("jump")) {
            entity.setDeltaMovement(new Vec3(0, 0.35, 0));
        }
        super.run(entity, stack, handler, anim);
    }
}
