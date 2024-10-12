package io.github.flemmli97.runecraftory.api.attachment;

import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.world.entity.player.Player;

public class PlayerAPI {

    public static int getBalance(Player player) {
        return Platform.INSTANCE.getPlayerData(player).map(PlayerData::getMoney).orElse(0);
    }

    public static int getRunepoints(Player player) {
        return Platform.INSTANCE.getPlayerData(player).map(PlayerData::getRunePoints).orElse(0);
    }

    public static boolean useRunepoints(Player player, int amount, boolean damage) {
        return Platform.INSTANCE.getPlayerData(player).map(d -> d.decreaseRunePoints(player, amount, damage)).orElse(false);
    }

    public static int getLevel(Player player) {
        return Platform.INSTANCE.getPlayerData(player).map(d -> d.getPlayerLevel().getLevel()).orElse(1);
    }

    public static int getSkillLevel(Player player, EnumSkills skill) {
        return Platform.INSTANCE.getPlayerData(player).map(d -> d.getSkillLevel(skill).getLevel()).orElse(1);
    }
}
