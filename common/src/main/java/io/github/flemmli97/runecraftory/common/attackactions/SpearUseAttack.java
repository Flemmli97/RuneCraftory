package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemSpearBase;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class SpearUseAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int chain) {
        return chain > 1 ? new AnimatedAction((int) Math.ceil(1.12 * 20) + 1, 3, "spear_use_continue") :
                new AnimatedAction((int) Math.ceil(1.2 * 20) + 1, 5, "spear_use");
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        boolean finish = (anim.getID().equals("spear_use_continue") ? anim.isAtTick(0.88) : anim.isAtTick(0.96));
        if ((anim.canAttack() || finish) && entity instanceof ServerPlayer serverPlayer && stack.getItem() instanceof ItemSpearBase spear) {
            spear.useSpear(serverPlayer, stack, finish);
        }
    }

    @Override
    public boolean disableItemSwitch() {
        return false;
    }

    @Override
    public AttackChain attackChain(LivingEntity entity, int chain) {
        return new AttackChain(20, 0);
    }

    @Override
    public boolean canOverride(LivingEntity entity, WeaponHandler handler) {
        AnimatedAction anim = handler.getCurrentAnim();
        return anim == null || (anim.getID().equals("spear_use_continue") ? (anim.isPastTick(0.2) && !anim.isPastTick(0.48))
                : (anim.isPastTick(0.28) && !anim.isPastTick(0.56)));
    }
}
