package io.github.flemmli97.runecraftory.common.commands.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.flemmli97.runecraftory.common.quests.QuestHandler;
import io.github.flemmli97.runecraftory.common.quests.quest.NPCQuestDefinition;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import io.github.flemmli97.simplequests_api.datapack.QuestsManager;
import io.github.flemmli97.simplequests_api.quest.QuestDefinition;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class QuestCommand {

    public static <T extends ArgumentBuilder<CommandSourceStack, T>> void register(ArgumentBuilder<CommandSourceStack, T> builder) {
        builder.then(Commands.literal("quests").requires(src -> src.hasPermission(2))
                .then(Commands.literal("accept")
                        .then(Commands.argument("quest", ResourceLocationArgument.id())
                                .suggests(QuestCommand::quests).executes(QuestCommand::accept)))
                .then(Commands.literal("reset")
                        .then(Commands.argument("quest", ResourceLocationArgument.id()).suggests(QuestCommand::activequests).executes(QuestCommand::reset))));
    }

    private static int accept(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        ResourceLocation id = ResourceLocationArgument.getId(ctx, "quest");
        QuestDefinition quest = QuestsManager.instance().getQuest(id, QuestHandler.QUEST_CONTEXT);
        if (quest == null) {
            ctx.getSource().sendFailure(Component.translatable("runecraftory.command.quest.noexist", id.toString()));
            return 0;
        }
        if (!(quest instanceof NPCQuestDefinition npcQuest)) {
            ctx.getSource().sendFailure(Component.translatable("runecraftory.command.quest.not_npc", id.toString()));
            return 0;
        }
        QuestHandler.QuestboardQuestHolder data = NPCQuestDefinition.resolve(npcQuest, player, player.position(), true).stream().limit(1)
                .findFirst().map(QuestHandler.QuestboardQuestHolder::new).orElse(null);
        if (data == null) {
            ctx.getSource().sendFailure(Component.translatable("runecraftory.command.quest.no_npc", id.toString()));
            return 0;
        }
        if (RunecraftoryAttachments.PLAYER_QUEST_DATA.get().get(player).acceptQuest(data, true)) {
            ctx.getSource().sendSuccess(() -> Component.translatable("runecraftory.command.quest.accepted", id.toString()), false);
            return Command.SINGLE_SUCCESS;
        }
        return 0;
    }

    private static int reset(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        ResourceLocation res = ResourceLocationArgument.getId(ctx, "quest");
        RunecraftoryAttachments.PLAYER_QUEST_DATA.get().get(player).reset(res);
        ctx.getSource().sendSuccess(() -> Component.translatable("runecraftory.command.quest.reset", res.toString()), false);
        return Command.SINGLE_SUCCESS;
    }

    public static CompletableFuture<Suggestions> quests(CommandContext<CommandSourceStack> context, SuggestionsBuilder build) throws CommandSyntaxException {
        return SharedSuggestionProvider.suggest(acceptableQuests(), build);
    }

    public static CompletableFuture<Suggestions> activequests(CommandContext<CommandSourceStack> context, SuggestionsBuilder build) throws CommandSyntaxException {
        return SharedSuggestionProvider.suggest(RunecraftoryAttachments.PLAYER_QUEST_DATA.get().get(context.getSource().getPlayerOrException())
                .getCurrentQuest().stream().map(prog -> prog.getRootId().toString())
                .toList(), build);
    }

    private static List<String> acceptableQuests() {
        return QuestsManager.instance().getAllQuests()
                .entrySet().stream()
                .filter(e -> e.getValue() instanceof NPCQuestDefinition)
                .map(e -> e.getKey().toString()).toList();
    }
}
