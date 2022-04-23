package io.github.flemmli97.runecraftory.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.network.S2CCapSync;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.stream.Stream;

public class RunecraftoryCommand {

    public static void reg(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("runecraftory")
                .then(Commands.literal("skill").requires(src -> src.hasPermission(2))
                        .then(Commands.argument("skill", StringArgumentType.string()).suggests((context, builder) -> SharedSuggestionProvider.suggest(Stream.of(EnumSkills.values()).map(Object::toString), builder)).then(Commands.literal("add")
                                .then(Commands.literal("level").then(Commands.argument("amount", IntegerArgumentType.integer()).executes(RunecraftoryCommand::setSkillXP)))
                                .then(Commands.literal("xp").then(Commands.argument("amount", IntegerArgumentType.integer()).executes(RunecraftoryCommand::addSkillXP)))
                        ))
                )
                .then(Commands.literal("level").requires(src -> src.hasPermission(2))
                        .then(Commands.literal("set").then(Commands.argument("amount", IntegerArgumentType.integer()).executes(RunecraftoryCommand::setLevel)))
                        .then(Commands.literal("xp").then(Commands.argument("amount", IntegerArgumentType.integer()).executes(RunecraftoryCommand::addXP)))
                )
                .then(Commands.literal("reset").requires(src -> src.hasPermission(2))
                        .then(Commands.literal("all").executes(RunecraftoryCommand::resetAll))
                        .then(Commands.literal("recipes").executes(RunecraftoryCommand::resetRecipes))
                )
        );
    }

    private static int addSkillXP(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        EnumSkills skill = EnumSkills.read(StringArgumentType.getString(ctx, "skill"));
        if (skill == null)
            return 0;
        int amount = IntegerArgumentType.getInteger(ctx, "amount");
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> data.increaseSkill(skill, player, amount));
        ctx.getSource().sendSuccess(new TranslatableComponent("command.skill.add", skill, amount), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int setSkillXP(CommandContext<CommandSourceStack> ctx) {

        return Command.SINGLE_SUCCESS;
    }

    private static int addXP(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        int amount = IntegerArgumentType.getInteger(ctx, "amount");
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> data.addXp(player, amount));
        ctx.getSource().sendSuccess(new TranslatableComponent("command.xp.add", amount), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int setLevel(CommandContext<CommandSourceStack> ctx) {

        return Command.SINGLE_SUCCESS;
    }

    private static int resetAll(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
            data.readFromNBT(data.resetNBT(), null);
            Platform.INSTANCE.sendToClient(new S2CCapSync(data), player);
        });
        ctx.getSource().sendSuccess(new TranslatableComponent("runecraftory.command.reset.all"), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int resetRecipes(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> data.getRecipeKeeper().lockRecipesRes(player, data.getRecipeKeeper().unlockedRecipes()));
        ctx.getSource().sendSuccess(new TranslatableComponent("runecraftory.command.reset.recipe"), false);
        return Command.SINGLE_SUCCESS;
    }
}
