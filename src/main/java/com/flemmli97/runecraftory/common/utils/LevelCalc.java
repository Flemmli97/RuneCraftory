package com.flemmli97.runecraftory.common.utils;

import com.flemmli97.runecraftory.api.enums.EnumSkills;
import com.flemmli97.runecraftory.common.capability.IPlayerCap;
import com.flemmli97.runecraftory.common.config.GeneralConfig;
import net.minecraft.entity.player.PlayerEntity;

public class LevelCalc {

    public static int xpAmountForLevelUp(int level) {
        return 1;
    }

    public static int xpAmountForSkills(int level) {
        return 1;
    }

    public static int getBaseXP(EnumSkills skill) {
        return GeneralConfig.skillProps.get(skill).getBaseXPGain();
    }

    public static void levelSkill(PlayerEntity player, IPlayerCap cap, EnumSkills skill, float multiplier) {
        cap.increaseSkill(skill, player, (int) (getBaseXP(skill) * multiplier));
    }
}
