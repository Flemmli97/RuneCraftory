package io.github.flemmli97.runecraftory.common.commands;

import com.google.common.collect.Sets;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.api.enums.EnumWeather;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.attachment.player.XpLevelHolder;
import io.github.flemmli97.runecraftory.common.components.StaffData;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.network.S2CCapSync;
import io.github.flemmli97.runecraftory.common.quests.QuestHandler;
import io.github.flemmli97.runecraftory.common.recipes.SextupleRecipe;
import io.github.flemmli97.runecraftory.common.registry.ModCrafting;
import io.github.flemmli97.runecraftory.common.registry.ModDataComponentTypes;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.world.WorldHandler;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class RunecraftoryCommand {

    public static void reg(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
        dispatcher.register(Commands.literal("runecraftory")
                .then(Commands.literal("skill").requires(src -> src.hasPermission(2))
                        .then(Commands.argument("player", EntityArgument.players())
                                .then(Commands.argument("skill", StringArgumentType.string()).suggests((context, builder) -> SharedSuggestionProvider.suggest(Stream.concat(Stream.of(EnumSkills.values()).map(Object::toString), Stream.of("ALL")), builder))
                                        .then(Commands.literal("add")
                                                .then(Commands.literal("level").then(Commands.argument("amount", IntegerArgumentType.integer()).executes(RunecraftoryCommand::addSkillLevel)))
                                                .then(Commands.literal("xp").then(Commands.argument("amount", IntegerArgumentType.integer()).executes(RunecraftoryCommand::addSkillXP))))
                                        .then(Commands.literal("set").then(Commands.argument("amount", IntegerArgumentType.integer()).executes(RunecraftoryCommand::setSkillLevel)))))
                )
                .then(Commands.literal("level").requires(src -> src.hasPermission(2))
                        .then(Commands.argument("player", EntityArgument.players())
                                .then(Commands.literal("set").then(Commands.argument("amount", IntegerArgumentType.integer()).executes(RunecraftoryCommand::setLevel)))
                                .then(Commands.literal("xp").then(Commands.argument("amount", IntegerArgumentType.integer()).executes(RunecraftoryCommand::addLevelXP))))
                )
                .then(Commands.literal("unlockRecipes").requires(src -> src.hasPermission(2))
                        .then(Commands.argument("player", EntityArgument.players())
                                .then(Commands.literal("all").executes(RunecraftoryCommand::unlockRecipes))
                                .then(Commands.literal("id").then(Commands.argument("id", ResourceLocationArgument.id())
                                        .suggests(RunecraftoryCommand::allRecipes).executes(RunecraftoryCommand::unlockRecipe)))))
                .then(Commands.literal("recalcStats").requires(src -> src.hasPermission(2)).then(Commands.argument("entities", EntityArgument.entities()).executes(RunecraftoryCommand::recalcStats)))
                .then(Commands.literal("weather").requires(src -> src.hasPermission(2)).then(Commands.argument("weather", StringArgumentType.string()).suggests((context, builder) -> SharedSuggestionProvider.suggest(Stream.of(EnumWeather.values()).map(Object::toString), builder)).executes(RunecraftoryCommand::setWeather)))
                .then(Commands.literal("reset").requires(src -> src.hasPermission(2))
                        .then(Commands.argument("player", EntityArgument.players())
                                .then(Commands.literal("all").executes(RunecraftoryCommand::resetAll))
                                .then(Commands.literal("recipes").executes(RunecraftoryCommand::resetRecipes)))
                )
                .then(Commands.literal("spell").requires(src -> src.hasPermission(2))
                        .then(Commands.literal("cast")
                                .then(Commands.argument("spell", ResourceArgument.resource(buildContext, ModSpells.SPELL_REGISTRY_KEY)).executes(ctx -> RunecraftoryCommand.castSpell(ctx, Set.of(ctx.getSource().getEntityOrException())))
                                        .then(Commands.argument("as", EntityArgument.entities()).executes(ctx -> RunecraftoryCommand.castSpell(ctx, EntityArgument.getEntities(ctx, "as"))))))
                        .then(Commands.literal("apply")
                                .then(Commands.argument("spell", ResourceArgument.resource(buildContext, ModSpells.SPELL_REGISTRY_KEY))
                                        .then(Commands.argument("tier", IntegerArgumentType.integer(1, 3)).executes(ctx -> RunecraftoryCommand.applySpellTo(ctx, Set.of(ctx.getSource().getEntityOrException())))
                                                .then(Commands.argument("as", EntityArgument.entities()).executes(ctx -> RunecraftoryCommand.applySpellTo(ctx, EntityArgument.getEntities(ctx, "as")))))))
                )
        );
    }

    private static int addSkillLevel(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        int ret = 0;
        int amount = IntegerArgumentType.getInteger(ctx, "amount");
        String s = StringArgumentType.getString(ctx, "skill");
        if (s.equals("ALL")) {
            for (ServerPlayer player : EntityArgument.getPlayers(ctx, "player")) {
                PlayerData data = Platform.INSTANCE.getPlayerData(player);
                for (EnumSkills skill : EnumSkills.values()) {
                    XpLevelHolder skLvl = data.getSkillLevel(skill);
                    data.setSkillLevel(skill, skLvl.getLevel() + amount, skLvl.getXp(), true);
                }
                ctx.getSource().sendSuccess(() -> Component.translatable("runecraftory.command.skill.lvl.add", s, player.getName(), amount), false);
                ret++;
            }
            return ret;
        }
        EnumSkills skill = EnumSkills.read(s);
        if (skill == null) {
            ctx.getSource().sendSuccess(() -> Component.translatable("runecraftory.command.skill.no", s), false);
            return 0;
        }
        for (ServerPlayer player : EntityArgument.getPlayers(ctx, "player")) {
            PlayerData data = Platform.INSTANCE.getPlayerData(player);
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
                PlayerData data = Platform.INSTANCE.getPlayerData(player);
                for (EnumSkills skill : EnumSkills.values())
                    data.increaseSkill(skill, amount);
                ctx.getSource().sendSuccess(() -> Component.translatable("runecraftory.command.skill.lvl.add", s, player.getName(), amount), false);
                ret++;
            }
            return ret;
        }
        EnumSkills skill = EnumSkills.read(s);
        if (skill == null) {
            ctx.getSource().sendSuccess(() -> Component.translatable("runecraftory.command.skill.no", s), false);
            return 0;
        }
        for (ServerPlayer player : EntityArgument.getPlayers(ctx, "player")) {
            Platform.INSTANCE.getPlayerData(player).increaseSkill(skill, amount);
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
                PlayerData data = Platform.INSTANCE.getPlayerData(player);
                for (EnumSkills skill : EnumSkills.values())
                    data.setSkillLevel(skill, amount, 0, true);
                ctx.getSource().sendSuccess(() -> Component.translatable("runecraftory.command.skill.lvl.set", s, player.getName(), amount), false);
                ret++;
            }
            return ret;
        }
        EnumSkills skill = EnumSkills.read(s);
        if (skill == null) {
            ctx.getSource().sendSuccess(() -> Component.translatable("runecraftory.command.skill.no", s), false);
            return 0;
        }
        for (ServerPlayer player : EntityArgument.getPlayers(ctx, "player")) {
            Platform.INSTANCE.getPlayerData(player).setSkillLevel(skill, amount, 0, true);
            ctx.getSource().sendSuccess(() -> Component.translatable("runecraftory.command.skill.lvl.set", Component.translatable(skill.getTranslation()), player.getName(), amount), false);
            ret++;
        }
        return ret;
    }

    private static int addLevelXP(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        int ret = 0;
        int amount = IntegerArgumentType.getInteger(ctx, "amount");
        for (ServerPlayer player : EntityArgument.getPlayers(ctx, "player")) {
            Platform.INSTANCE.getPlayerData(player).addXp(amount);
            ctx.getSource().sendSuccess(() -> Component.translatable("runecraftory.command.lvl.xp.add", player.getName(), amount), false);
            ret++;
        }
        return ret;
    }

    private static int setLevel(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        int ret = 0;
        int amount = Math.max(1, IntegerArgumentType.getInteger(ctx, "amount"));
        for (ServerPlayer player : EntityArgument.getPlayers(ctx, "player")) {
            Platform.INSTANCE.getPlayerData(player).setPlayerLevel(amount, 0, true);
            ctx.getSource().sendSuccess(() -> Component.translatable("runecraftory.command.lvl.set", player.getName(), amount), false);
            ret++;
        }
        return ret;
    }

    private static int resetAll(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        int ret = 0;
        for (ServerPlayer player : EntityArgument.getPlayers(ctx, "player")) {
            PlayerData data = Platform.INSTANCE.getPlayerData(player);
            data.resetAll();
            LoaderNetwork.INSTANCE.sendToPlayer(new S2CCapSync(data), player);
            QuestHandler.getData(player).resetAll();
            ctx.getSource().sendSuccess(() -> Component.translatable("runecraftory.command.reset.all", player.getName()), false);
            ret++;
        }
        return ret;
    }

    private static CompletableFuture<Suggestions> allRecipes(CommandContext<CommandSourceStack> ctx, SuggestionsBuilder builder) throws CommandSyntaxException {
        Set<ResourceLocation> allRecipes = Sets.newHashSet();
        PlayerData data = Platform.INSTANCE.getPlayerData(ctx.getSource().getPlayerOrException());
        if (data != null) {
            for (RecipeHolder<SextupleRecipe> r : ctx.getSource().getServer().getRecipeManager().getAllRecipesFor(ModCrafting.FORGE.get())) {
                if (!data.getRecipeKeeper().isUnlocked(r))
                    allRecipes.add(r.id());
            }
            for (RecipeHolder<SextupleRecipe> r : ctx.getSource().getServer().getRecipeManager().getAllRecipesFor(ModCrafting.CHEMISTRY.get())) {
                if (!data.getRecipeKeeper().isUnlocked(r))
                    allRecipes.add(r.id());
            }
            for (RecipeHolder<SextupleRecipe> r : ctx.getSource().getServer().getRecipeManager().getAllRecipesFor(ModCrafting.ARMOR.get())) {
                if (!data.getRecipeKeeper().isUnlocked(r))
                    allRecipes.add(r.id());
            }
            for (RecipeHolder<SextupleRecipe> r : ctx.getSource().getServer().getRecipeManager().getAllRecipesFor(ModCrafting.COOKING.get())) {
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
            Platform.INSTANCE.getPlayerData(player).getRecipeKeeper().unlockRecipesRes(player, List.of(res));
            ctx.getSource().sendSuccess(() -> Component.translatable("runecraftory.command.unlock.recipe", player.getName(), res.toString()), false);
            ret++;
        }
        return ret;
    }

    private static int unlockRecipes(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Set<ResourceLocation> allRecipes = Sets.newHashSet();
        ctx.getSource().getServer().getRecipeManager().getAllRecipesFor(ModCrafting.FORGE.get()).forEach(r -> allRecipes.add(r.id()));
        ctx.getSource().getServer().getRecipeManager().getAllRecipesFor(ModCrafting.CHEMISTRY.get()).forEach(r -> allRecipes.add(r.id()));
        ctx.getSource().getServer().getRecipeManager().getAllRecipesFor(ModCrafting.ARMOR.get()).forEach(r -> allRecipes.add(r.id()));
        ctx.getSource().getServer().getRecipeManager().getAllRecipesFor(ModCrafting.COOKING.get()).forEach(r -> allRecipes.add(r.id()));
        int ret = 0;
        for (ServerPlayer player : EntityArgument.getPlayers(ctx, "player")) {
            Platform.INSTANCE.getPlayerData(player).getRecipeKeeper().unlockRecipesRes(player, allRecipes);
            ctx.getSource().sendSuccess(() -> Component.translatable("runecraftory.command.unlock.recipes", player.getName()), false);
            ret++;
        }
        return ret;
    }

    private static int resetRecipes(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        int ret = 0;
        for (ServerPlayer player : EntityArgument.getPlayers(ctx, "player")) {
            PlayerData data = Platform.INSTANCE.getPlayerData(player);
            data.getRecipeKeeper().lockRecipesRes(player, data.getRecipeKeeper().unlockedRecipes());
            ctx.getSource().sendSuccess(() -> Component.translatable("runecraftory.command.reset.recipe", player.getName()), false);
            ret++;
        }
        return ret;
    }

    private static int setWeather(CommandContext<CommandSourceStack> ctx) {
        EnumWeather weather;
        String s = StringArgumentType.getString(ctx, "weather");
        try {
            weather = EnumWeather.valueOf(s);
        } catch (IllegalArgumentException e) {
            ctx.getSource().sendFailure(Component.translatable("runecraftory.command.weather.no", s));
            return 0;
        }
        WorldHandler.get(ctx.getSource().getServer()).updateWeatherTo(ctx.getSource().getLevel(), weather);
        ctx.getSource().sendSuccess(() -> Component.translatable("runecraftory.command.set.weather", Component.translatable(weather.translation)), false);
        return 1;
    }

    private static int recalcStats(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Collection<? extends Entity> entities = EntityArgument.getEntities(ctx, "entities");
        int i = 0;
        for (Entity e : entities) {
            if (e instanceof ServerPlayer player) {
                Platform.INSTANCE.getPlayerData(player).recalculateStats(false);
                i++;
            } else if (e instanceof EntityNPCBase npc) {
                npc.recalcStatsFull();
                i++;
            } else if (e instanceof BaseMonster monster) {
                monster.recalcStatsFull();
                i++;
            }
        }
        int affected = i;
        ctx.getSource().sendSuccess(() -> Component.translatable("runecraftory.command.recalc.stats", affected), false);
        return i;
    }

    @SuppressWarnings("unchecked")
    private static int castSpell(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        int success = 0;
        Holder.Reference<Spell> spell = ResourceArgument.getResource(ctx, "spell", (ResourceKey<Registry<Spell>>) ModSpells.SPELLS.registry().key());
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity living) {
                spell.value().use(living);
                success++;
            }
        }
        return success;
    }

    @SuppressWarnings("unchecked")
    private static int applySpellTo(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        int success = 0;
        Holder.Reference<Spell> spell = ResourceArgument.getResource(ctx, "spell", (ResourceKey<Registry<Spell>>) ModSpells.SPELLS.registry().key());
        int tier = IntegerArgumentType.getInteger(ctx, "tier");
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity living) {
                ItemStack stack = living.getMainHandItem();
                if (stack.getItem() instanceof ItemStaffBase) {
                    StaffData data = stack.getOrDefault(ModDataComponentTypes.STAFF.get(), StaffData.DEFAULT);
                    stack.set(ModDataComponentTypes.STAFF.get(), switch (tier) {
                        case 3 -> data.setTier3Spell(spell);
                        case 2 -> data.setTier2Spell(spell);
                        default -> data.setTier1Spell(spell);
                    });
                    success++;
                }
            }
        }
        return success;
    }
}
