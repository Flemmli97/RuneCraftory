package io.github.flemmli97.runecraftory.api.attachment;

import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import net.minecraft.world.entity.player.Player;

public class PlayerAPI {

    public static int getBalance(Player player) {
        return RunecraftoryAttachments.PLAYER_DATA.get().get(player).getMoney();
    }

    public static int getRunepoints(Player player) {
        return RunecraftoryAttachments.PLAYER_DATA.get().get(player).getRunePoints();
    }

    public static boolean useRunepoints(Player player, int amount, boolean damage) {
        return RunecraftoryAttachments.PLAYER_DATA.get().get(player).useRunePoints(amount, damage);
    }

    public static int getLevel(Player player) {
        return RunecraftoryAttachments.PLAYER_DATA.get().get(player).getPlayerLevel().getLevel();
    }

    public static int getSkillLevel(Player player, Skills skill) {
        return RunecraftoryAttachments.PLAYER_DATA.get().get(player).getSkillLevel(skill).getLevel();
    }
}
