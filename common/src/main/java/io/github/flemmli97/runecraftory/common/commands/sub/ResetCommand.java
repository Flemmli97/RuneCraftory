package io.github.flemmli97.runecraftory.common.commands.sub;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.network.S2CCapSync;
import io.github.flemmli97.runecraftory.common.quests.QuestHandler;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class ResetCommand {

    public static <T extends ArgumentBuilder<CommandSourceStack, T>> void register(ArgumentBuilder<CommandSourceStack, T> builder) {
        builder.then(Commands.literal("reset").requires(src -> src.hasPermission(2))
                .then(Commands.argument("player", EntityArgument.players())
                        .then(Commands.literal("all").executes(ResetCommand::resetAll))
                        .then(Commands.literal("recipes").executes(ResetCommand::resetRecipes)))
        );
    }

    private static int resetAll(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        int ret = 0;
        for (ServerPlayer player : EntityArgument.getPlayers(ctx, "player")) {
            PlayerData data = RunecraftoryAttachments.PLAYER_DATA.get().get(player);
            data.resetAll();
            LoaderNetwork.INSTANCE.sendToPlayer(new S2CCapSync(data), player);
            QuestHandler.getData(player).resetAll();
            ctx.getSource().sendSuccess(() -> Component.translatable("runecraftory.command.reset.all", player.getName()), false);
            ret++;
        }
        return ret;
    }

    private static int resetRecipes(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        int ret = 0;
        for (ServerPlayer player : EntityArgument.getPlayers(ctx, "player")) {
            PlayerData data = RunecraftoryAttachments.PLAYER_DATA.get().get(player);
            data.getRecipeKeeper().lockRecipesRes(player, data.getRecipeKeeper().unlockedRecipes());
            ctx.getSource().sendSuccess(() -> Component.translatable("runecraftory.command.reset.recipe", player.getName()), false);
            ret++;
        }
        return ret;
    }
}