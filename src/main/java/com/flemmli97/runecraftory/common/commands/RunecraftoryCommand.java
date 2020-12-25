package com.flemmli97.runecraftory.common.commands;

import com.flemmli97.runecraftory.api.enums.EnumSkills;
import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.server.command.EnumArgument;

public class RunecraftoryCommand {

    public static void reg(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("runecraftory")
                .then(Commands.literal("skill").requires(src -> src.hasPermissionLevel(2))
                        .then(Commands.argument("skill", EnumArgument.enumArgument(EnumSkills.class)).then(Commands.literal("add")
                            .then(Commands.literal("level").then(Commands.argument("amount", IntegerArgumentType.integer()).executes(RunecraftoryCommand::setSkillXP)))
                            .then(Commands.literal("xp").then(Commands.argument("amount", IntegerArgumentType.integer()).executes(RunecraftoryCommand::addSkillXP)))
                        )))
                .then(Commands.literal("level").requires(src -> src.hasPermissionLevel(2))
                        .then(Commands.literal("set").then(Commands.argument("amount", IntegerArgumentType.integer()).executes(RunecraftoryCommand::setLevel)))
                        .then(Commands.literal("xp").then(Commands.argument("amount", IntegerArgumentType.integer()).executes(RunecraftoryCommand::addXP)))

                )
        );
    }

    private static int addSkillXP(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        ServerPlayerEntity player =  ctx.getSource().asPlayer();
        EnumSkills skill = ctx.getArgument("skill", EnumSkills.class);
        int amount= IntegerArgumentType.getInteger(ctx, "amount");
        player.getCapability(CapabilityInsts.PlayerCap).ifPresent(cap->{
            cap.increaseSkill(skill, player, amount);
        });
        ctx.getSource().sendFeedback(new TranslationTextComponent("command.skill.add", skill, amount), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int setSkillXP(CommandContext<CommandSource> ctx) {

        return Command.SINGLE_SUCCESS;
    }

    private static int addXP(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        ServerPlayerEntity player =  ctx.getSource().asPlayer();
        int amount= IntegerArgumentType.getInteger(ctx, "amount");
        player.getCapability(CapabilityInsts.PlayerCap).ifPresent(cap->{
            cap.addXp(player, amount);
        });
        ctx.getSource().sendFeedback(new TranslationTextComponent("command.xp.add", amount), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int setLevel(CommandContext<CommandSource> ctx) {

        return Command.SINGLE_SUCCESS;
    }
}
