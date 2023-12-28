package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemAxeBase;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class HammerAxeUseAttack extends TimedUseAttack {

    public HammerAxeUseAttack() {
        super(() -> new AnimatedAction(20 + 1, 12, "hammer_axe_use"), ItemAxeBase::delayedRightClickAction);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (anim.isAtTick(0.16)) {
            handler.setMoveTargetDir(new Vec3(0, 0.8, 0), anim, 0.44);
        }
        if (anim.isAtTick(0.4)) {
            handler.setMoveTargetDir(new Vec3(0, -0.8, 0), anim, 0.64);
        }
        super.run(entity, stack, handler, anim);
    }
}
