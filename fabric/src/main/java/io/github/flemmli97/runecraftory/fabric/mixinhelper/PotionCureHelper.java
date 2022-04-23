package io.github.flemmli97.runecraftory.fabric.mixinhelper;

import io.github.flemmli97.runecraftory.platform.ExtendedEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Iterator;

public class PotionCureHelper {

    public static boolean cureEffects(LivingEntity entity, ItemStack stack) {
        if (entity.level.isClientSide) {
            return false;
        }
        boolean ret = false;
        Iterator<MobEffectInstance> itr = entity.getActiveEffectsMap().values().iterator();
        while (itr.hasNext()) {
            MobEffectInstance effect = itr.next();
            if (effect.getEffect() instanceof ExtendedEffect extendedEffect && extendedEffect.getCurativeItems().stream().anyMatch(e -> e.sameItem(stack)))
                continue;
            ((EntityDataGetter) entity).onCureEffect(effect);
            itr.remove();
            ret = true;
        }
        return ret;
    }
}
