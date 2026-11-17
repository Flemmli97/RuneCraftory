package io.github.flemmli97.runecraftory.common.commands.sub;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.attachment.player.XpLevelHolder;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.stream.Stream;

public class SkillCommand {

    public static <T extends ArgumentBuilder<CommandSourceStack, T>> void register(ArgumentBuilder<CommandSourceStack, T> builder) {
        builder.then(Commands.literal("skill").requires(src -> src.hasPermission(2))
                .then(Commands.argument("player", EntityArgument.players())
                        .then(Commands.argument("skill", StringArgumentType.string()).suggests((context, sb) -> SharedSuggestionProvider.suggest(Stream.concat(Stream.of(Skills.values()).map(Object::toString), Stream.of("ALL")), sb))
                                .then(Commands.literal("add")
                                        .then(Commands.literal("level").then(Commands.argument("amount", IntegerArgumentType.integer()).executes(SkillCommand::addSkillLevel)))
                                        .then(Commands.literal("xp").then(Commands.argument("amount", IntegerArgumentType.integer()).executes(SkillCommand::addSkillXP))))
                                .then(Commands.literal("set").then(Commands.argument("amount", IntegerArgumentType.integer()).executes(SkillCommand::setSkillLevel)))))
        );
    }

    private static int addSkillLevel(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        int ret = 0;
        int amount = IntegerArgumentType.getInteger(ctx, "amount");
        String s = StringArgumentType.getString(ctx, "skill");
        if (s.equals("ALL")) {
            for (ServerPlayer player : EntityArgument.getPlayers(ctx, "player")) {
                PlayerData data = RunecraftoryAttachments.PLAYER_DATA.get().get(player);
                for (Skills skill : Skills.values()) {
                    XpLevelHolder skLvl = data.getSkillLevel(skill);
                    data.setSkillLevel(skill, skLvl.getLevel() + amount, skLvl.getXp(), true);
                }
                ctx.getSource().sendSuccess(() -> Component.translatable("runecraftory.command.skill.lvl.add", s, player.getName(), amount), false);
                ret++;
            }
            return ret;
        }
        Skills skill = Skills.read(s);
        if (skill == null) {
            ctx.getSource().sendSuccess(() -> Component.translatable("runecraftory.command.skill.no", s), false);
            return 0;
        }
        for (ServerPlayer player : EntityArgument.getPlayers(ctx, "player")) {
            PlayerData data = RunecraftoryAttachments.PLAYER_DATA.get().get(player);
            XpLevelHolder skLvl = data.getSkillLevel(skill);
            data.setSkillLevel(skill, skLvl.getLevel() + amount, skLvl.getXp(), true);
            ctx.getSource().sendSuccess(() -> Component.translatable("runecraftory.command.skill.lvl.add", s, player.getName(), amount), false);
            ret++;
        }
        return ret;
    }

    private static int addSkillXP(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        int ret = 0;
        int amount = IntegerArgumentType.getInteger(ctx, "amount");
        String s = StringArgumentType.getString(ctx, "skill");
        if (s.equals("ALL")) {
            for (ServerPlayer player : EntityArgument.getPlayers(ctx, "player")) {
                PlayerData data = RunecraftoryAttachments.PLAYER_DATA.get().get(player);
                for (Skills skill : Skills.values())
                    data.increaseSkill(skill, amount);
                ctx.getSource().sendSuccess(() -> Component.translatable("runecraftory.command.skill.lvl.add", s, player.getName(), amount), false);
                ret++;
            }
            return ret;
        }
        Skills skill = Skills.read(s);
        if (skill == null) {
            ctx.getSource().sendSuccess(() -> Component.translatable("runecraftory.command.skill.no", s), false);
            return 0;
        }
        for (ServerPlayer player : EntityArgument.getPlayers(ctx, "player")) {
            RunecraftoryAttachments.PLAYER_DATA.get().get(player).increaseSkill(skill, amount);
            ctx.getSource().sendSuccess(() -> Component.translatable("runecraftory.command.skill.xp.add", s, player.getName(), amount), false);
            ret++;
        }
        return ret;
    }

    private static int setSkillLevel(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        int ret = 0;
        int amount = Math.max(1, IntegerArgumentType.getInteger(ctx, "amount"));
        String s = StringArgumentType.getString(ctx, "skill");
        if (s.equals("ALL")) {
            for (ServerPlayer player : EntityArgument.getPlayers(ctx, "player")) {
                PlayerData data = RunecraftoryAttachments.PLAYER_DATA.get().get(player);
                for (Skills skill : Skills.values())
                    data.setSkillLevel(skill, amount, 0, true);
                ctx.getSource().sendSuccess(() -> Component.translatable("runecraftory.command.skill.lvl.set", s, player.getName(), amount), false);
                ret++;
            }
            return ret;
        }
        Skills skill = Skills.read(s);
        if (skill == null) {
            ctx.getSource().sendSuccess(() -> Component.translatable("runecraftory.command.skill.no", s), false);
            return 0;
        }
        for (ServerPlayer player : EntityArgument.getPlayers(ctx, "player")) {
            RunecraftoryAttachments.PLAYER_DATA.get().get(player).setSkillLevel(skill, amount, 0, true);
            ctx.getSource().sendSuccess(() -> Component.translatable("runecraftory.command.skill.lvl.set", Component.translatable(skill.getTranslation()), player.getName(), amount), false);
            ret++;
        }
        return ret;
    }
}