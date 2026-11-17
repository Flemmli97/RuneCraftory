package io.github.flemmli97.runecraftory.common.commands.sub;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class LevelCommand {

    public static <T extends ArgumentBuilder<CommandSourceStack, T>> void register(ArgumentBuilder<CommandSourceStack, T> builder) {
        builder.then(Commands.literal("level").requires(src -> src.hasPermission(2))
                .then(Commands.argument("player", EntityArgument.players())
                        .then(Commands.literal("set").then(Commands.argument("amount", IntegerArgumentType.integer()).executes(LevelCommand::setLevel)))
                        .then(Commands.literal("xp").then(Commands.argument("amount", IntegerArgumentType.integer()).executes(LevelCommand::addLevelXP))))
        );
    }

    private static int addLevelXP(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        int ret = 0;
        int amount = IntegerArgumentType.getInteger(ctx, "amount");
        for (ServerPlayer player : EntityArgument.getPlayers(ctx, "player")) {
            RunecraftoryAttachments.PLAYER_DATA.get().get(player).addXp(amount);
            ctx.getSource().sendSuccess(() -> Component.translatable("runecraftory.command.lvl.xp.add", player.getName(), amount), false);
            ret++;
        }
        return ret;
    }

    private static int setLevel(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        int ret = 0;
        int amount = Math.max(1, IntegerArgumentType.getInteger(ctx, "amount"));
        for (ServerPlayer player : EntityArgument.getPlayers(ctx, "player")) {
            RunecraftoryAttachments.PLAYER_DATA.get().get(player).setPlayerLevel(amount, 0, true);
            ctx.getSource().sendSuccess(() -> Component.translatable("runecraftory.command.lvl.set", player.getName(), amount), false);
            ret++;
        }
        return ret;
    }
}
