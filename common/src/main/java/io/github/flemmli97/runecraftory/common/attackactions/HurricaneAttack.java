package io.github.flemmli97.runecraftory.common.attackactions;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.api.action.AttackAction;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class HurricaneAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int chain) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.HURRICANE.create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (anim.isAtTick(0.4)) {
            handler.setSpinStartRot(entity.getYRot() + 180);
            handler.resetHitEntityTracker();
        }
        if (anim.isAtTick(0.76) || anim.isAtTick(1.08)) {
            handler.resetHitEntityTracker();
        }
        if (!entity.level.isClientSide) {
            if (anim.isAtTick(0.2)) {
                CombatUtils.spinAttackHandler(entity, entity.getLookAngle(), 20, 0.5f, e -> true,
                        Pair.of(Map.of(), Map.of(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack))), null);
            }
            if (anim.isPastTick(0.44) && !anim.isPastTick(1.44)) {
                int start = Mth.ceil(0.44 * 20.0D);
                int end = Mth.ceil(1.44 * 20.0D);
                float len = (end - start) / anim.getSpeed();
                float f = (anim.getTick() - start) / anim.getSpeed();
                float angleInc = 1080 / len;
                float rot = handler.getSpinStartRot();
                handler.addHitEntityTracker(CombatUtils.spinAttackHandler(entity, (rot + f * angleInc), (rot + (f + 1) * angleInc), 0, e -> !handler.getHitEntityTracker().contains(e),
                        Pair.of(Map.of(), Map.of(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack))), null));
            }
        }
    }
}