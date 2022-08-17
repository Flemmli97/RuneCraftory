package io.github.flemmli97.runecraftory.common.utils;

import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.registry.ModEffects;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityUtils {

    public static boolean isExhaust(LivingEntity entity) {
        return entity.hasEffect(ModEffects.fatigue.get());
    }

    public static boolean paralysed(LivingEntity entity) {
        return entity.hasEffect(ModEffects.paralysis.get());
    }

    public static boolean sleeping(LivingEntity entity) {
        return entity.hasEffect(ModEffects.sleep.get());
    }

    public static float playerLuck(Player player) {
        float luckAtt = player.getLuck();

        return luckAtt;
    }

    public static boolean isDisabled(LivingEntity entity) {
        return entity.hasEffect(ModEffects.sleep.get());
    }

    @Nullable
    public static UUID tryGetOwner(LivingEntity entity) {
        if (entity instanceof OwnableEntity ownableEntity)
            return ownableEntity.getOwnerUUID();
        return null;
    }

    public static float tamingChance(BaseMonster monster, Player player, float multiplier) {
        int lvl = Platform.INSTANCE.getPlayerData(player).map(d -> d.getPlayerLevel()[0]).orElse(1) + 1;
        float lvlPenalty = (float) Math.pow(0.89, Math.max(0, monster.level() - lvl));
        return monster.tamingChance() * multiplier * GeneralConfig.tamingMultiplier * lvlPenalty;
    }

    public static NullPointerException playerDataException() {
        return new NullPointerException("Player capability is null. This shouldn't be");
    }
}
