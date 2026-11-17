package io.github.flemmli97.runecraftory.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.flemmli97.runecraftory.common.commands.sub.LevelCommand;
import io.github.flemmli97.runecraftory.common.commands.sub.QuestCommand;
import io.github.flemmli97.runecraftory.common.commands.sub.RecalcStatsCommand;
import io.github.flemmli97.runecraftory.common.commands.sub.ResetCommand;
import io.github.flemmli97.runecraftory.common.commands.sub.SkillCommand;
import io.github.flemmli97.runecraftory.common.commands.sub.SpellCommand;
import io.github.flemmli97.runecraftory.common.commands.sub.UnlockRecipeCommand;
import io.github.flemmli97.runecraftory.common.commands.sub.WeatherCommand;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class RunecraftoryCommand {

    public static void reg(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("runecraftory");
        LevelCommand.register(builder);
        QuestCommand.register(builder);
        RecalcStatsCommand.register(builder);
        ResetCommand.register(builder);
        SkillCommand.register(builder);
        SpellCommand.register(builder, buildContext);
        UnlockRecipeCommand.register(builder);
        WeatherCommand.register(builder);
        dispatcher.register(builder);
    }
}
