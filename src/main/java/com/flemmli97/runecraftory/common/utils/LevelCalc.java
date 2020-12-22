package com.flemmli97.runecraftory.common.utils;

import com.flemmli97.runecraftory.api.enums.EnumSkills;
import com.flemmli97.runecraftory.common.capability.IPlayerCap;
import com.flemmli97.runecraftory.common.config.GeneralConfig;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LevelCalc {

    public static int xpAmountForLevelUp(int level) {
        return 1;
    }

    public static int xpAmountForSkills(int level) {
        return 1;
    }

    public static void addXP(ServerPlayerEntity player, IPlayerCap cap, int amount) {
        cap.addXp(player, (int) (amount * GeneralConfig.xpMultiplier));
    }

    public static int getBaseXP(EnumSkills skill) {
        return GeneralConfig.skillProps.get(skill).getBaseXPGain();
    }

    public static void levelSkill(ServerPlayerEntity player, IPlayerCap cap, EnumSkills skill, float multiplier) {
        cap.increaseSkill(skill, player, (int) (getBaseXP(skill) * multiplier * GeneralConfig.skillXpMultiplier));
    }

    public static int levelFromPos(World world, BlockPos pos) {
        return 5;
    }
}
