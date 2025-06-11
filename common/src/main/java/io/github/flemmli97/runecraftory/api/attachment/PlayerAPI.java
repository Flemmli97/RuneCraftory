package io.github.flemmli97.runecraftory.api.attachment;

import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.world.entity.player.Player;

public class PlayerAPI {

    public static int getBalance(Player player) {
        return Platform.INSTANCE.getPlayerData(player).getMoney();
    }

    public static int getRunepoints(Player player) {
        return Platform.INSTANCE.getPlayerData(player).getRunePoints();
    }

    public static boolean useRunepoints(Player player, int amount, boolean damage) {
        return Platform.INSTANCE.getPlayerData(player).decreaseRunePoints(amount, damage);
    }

    public static int getLevel(Player player) {
        return Platform.INSTANCE.getPlayerData(player).getPlayerLevel().getLevel();
    }

    public static int getSkillLevel(Player player, EnumSkills skill) {
        return Platform.INSTANCE.getPlayerData(player).getSkillLevel(skill).getLevel();
    }
}
