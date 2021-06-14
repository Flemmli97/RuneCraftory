package io.github.flemmli97.runecraftory.common.utils;

import com.flemmli97.tenshilib.api.entity.IOwnable;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.registry.ModEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityUtils {

    public static boolean isExhaust(LivingEntity entity) {
        return entity.getActivePotionEffect(ModEffects.fatigue.get()) != null;
    }

    public static boolean paralysed(LivingEntity entity) {
        return entity.getActivePotionEffect(ModEffects.paralysis.get()) != null;
    }

    public static boolean sleeping(LivingEntity entity) {
        return entity.getActivePotionEffect(ModEffects.sleep.get()) != null;
    }

    public static float playerLuck(PlayerEntity player) {
        float luckAtt = player.getLuck();

        return luckAtt;
    }

    public static boolean isDisabled(LivingEntity entity) {
        return entity.isPotionActive(ModEffects.sleep.get());
    }

    @Nullable
    public static UUID tryGetOwner(LivingEntity entity) {
        if (entity instanceof TameableEntity)
            return ((TameableEntity) entity).getOwnerId();
        if (entity instanceof IOwnable<?>)
            return ((IOwnable<?>) entity).getOwnerUUID();
        return null;
    }

    public static float tamingChance(BaseMonster monster, float multiplier) {
        return monster.tamingChance() * multiplier * GeneralConfig.tamingMultiplier;
    }

    public static NullPointerException capabilityException() {
        return new NullPointerException("Player capability is null. This shouldn't be");
    }
}
