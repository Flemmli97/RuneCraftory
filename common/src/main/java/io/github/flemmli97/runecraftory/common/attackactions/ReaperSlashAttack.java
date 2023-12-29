package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.AttackAction;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ReaperSlashAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int chain) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.REAPER_SLASH.create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (anim.isAtTick(0.12)) {
            handler.setSpinStartRot(entity.getYRot() - 80);
        }
        if (anim.isPastTick(0.2) && !anim.isPastTick(0.64)) {
            int start = Mth.ceil(0.2 * 20.0D);
            int end = Mth.ceil(0.64 * 20.0D);
            float len = (end - start) / anim.getSpeed();
            float f = (anim.getTick() - start) / anim.getSpeed();
            float angleInc = 160 / len;
            float rot = handler.getSpinStartRot();
            handler.addHitEntityTracker(CombatUtils.spinAttackHandler(entity, (rot + f * angleInc), (rot + (f + 1) * angleInc), 0.5f, e -> !handler.getHitEntityTracker().contains(e)));
        }
    }
}
