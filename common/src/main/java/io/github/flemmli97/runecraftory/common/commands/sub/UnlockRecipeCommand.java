package io.github.flemmli97.runecraftory.common.commands.sub;

import com.google.common.collect.Sets;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.recipes.SextupleRecipe;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryCrafting;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class UnlockRecipeCommand {

    public static <T extends ArgumentBuilder<CommandSourceStack, T>> void register(ArgumentBuilder<CommandSourceStack, T> builder) {
        builder.then(Commands.literal("unlockRecipes").requires(src -> src.hasPermission(2))
                .then(Commands.argument("player", EntityArgument.players())
                        .then(Commands.literal("all").executes(UnlockRecipeCommand::unlockRecipes))
                        .then(Commands.literal("id").then(Commands.argument("id", ResourceLocationArgument.id())
                                .suggests(UnlockRecipeCommand::allRecipes).executes(UnlockRecipeCommand::unlockRecipe)))));
    }

    private static CompletableFuture<Suggestions> allRecipes(CommandContext<CommandSourceStack> ctx, SuggestionsBuilder builder) throws CommandSyntaxException {
        Set<ResourceLocation> allRecipes = Sets.newHashSet();
        PlayerData data = RunecraftoryAttachments.PLAYER_DATA.get().get(ctx.getSource().getPlayerOrException());
        if (data != null) {
            for (RecipeHolder<SextupleRecipe> r : ctx.getSource().getServer().getRecipeManager().getAllRecipesFor(RuneCraftoryCrafting.FORGE.get())) {
                if (!data.getRecipeKeeper().isUnlocked(r))
                    allRecipes.add(r.id());
            }
            for (RecipeHolder<SextupleRecipe> r : ctx.getSource().getServer().getRecipeManager().getAllRecipesFor(RuneCraftoryCrafting.CHEMISTRY.get())) {
                if (!data.getRecipeKeeper().isUnlocked(r))
                    allRecipes.add(r.id());
            }
            for (RecipeHolder<SextupleRecipe> r : ctx.getSource().getServer().getRecipeManager().getAllRecipesFor(RuneCraftoryCrafting.ARMOR.get())) {
                if (!data.getRecipeKeeper().isUnlocked(r))
                    allRecipes.add(r.id());
            }
            for (RecipeHolder<SextupleRecipe> r : ctx.getSource().getServer().getRecipeManager().getAllRecipesFor(RuneCraftoryCrafting.COOKING.get())) {
                if (!data.getRecipeKeeper().isUnlocked(r))
                    allRecipes.add(r.id());
            }
        }
        return SharedSuggestionProvider.suggest(allRecipes.stream().map(ResourceLocation::toString), builder);
    }

    private static int unlockRecipe(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ResourceLocation res = ResourceLocationArgument.getId(ctx, "id");
        int ret = 0;
        for (ServerPlayer player : EntityArgument.getPlayers(ctx, "player")) {
            RunecraftoryAttachments.PLAYER_DATA.get().get(player).getRecipeKeeper().unlockRecipesRes(player, List.of(res));
            ctx.getSource().sendSuccess(() -> Component.translatable("runecraftory.command.unlock.recipe", player.getName(), res.toString()), false);
            ret++;
        }
        return ret;
    }

    private static int unlockRecipes(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Set<ResourceLocation> allRecipes = Sets.newHashSet();
        ctx.getSource().getServer().getRecipeManager().getAllRecipesFor(RuneCraftoryCrafting.FORGE.get()).forEach(r -> allRecipes.add(r.id()));
        ctx.getSource().getServer().getRecipeManager().getAllRecipesFor(RuneCraftoryCrafting.CHEMISTRY.get()).forEach(r -> allRecipes.add(r.id()));
        ctx.getSource().getServer().getRecipeManager().getAllRecipesFor(RuneCraftoryCrafting.ARMOR.get()).forEach(r -> allRecipes.add(r.id()));
        ctx.getSource().getServer().getRecipeManager().getAllRecipesFor(RuneCraftoryCrafting.COOKING.get()).forEach(r -> allRecipes.add(r.id()));
        int ret = 0;
        for (ServerPlayer player : EntityArgument.getPlayers(ctx, "player")) {
            RunecraftoryAttachments.PLAYER_DATA.get().get(player).getRecipeKeeper().unlockRecipesRes(player, allRecipes);
            ctx.getSource().sendSuccess(() -> Component.translatable("runecraftory.command.unlock.recipes", player.getName()), false);
            ret++;
        }
        return ret;
    }
}
