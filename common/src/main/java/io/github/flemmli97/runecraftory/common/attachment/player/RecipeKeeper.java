package io.github.flemmli97.runecraftory.common.attachment.player;

import com.google.common.collect.ImmutableSet;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.network.S2CRecipe;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RecipeKeeper {

    private final Set<ResourceLocation> unlockedRecipes = new HashSet<>();

    public void unlockRecipe(Player player, RecipeHolder<?> recipe) {
        this.unlockRecipes(player, Set.of(recipe));
    }

    public void unlockRecipes(Player player, Collection<? extends RecipeHolder<?>> recipes) {
        this.unlockRecipesRes(player, recipes.stream().map(RecipeHolder::id).toList());
    }

    public void unlockRecipesRes(Player player, Collection<ResourceLocation> recipes) {
        this.unlockedRecipes.addAll(recipes);
        if (player instanceof ServerPlayer)
            LoaderNetwork.INSTANCE.sendToPlayer(new S2CRecipe(recipes, false), (ServerPlayer) player);
    }

    public void lockRecipe(Player player, RecipeHolder<?> recipe) {
        this.lockRecipes(player, Set.of(recipe));
    }

    public void lockRecipes(Player player, Collection<? extends RecipeHolder<?>> recipes) {
        this.lockRecipesRes(player, recipes.stream().map(RecipeHolder::id).toList());
    }

    public void lockRecipesRes(Player player, Collection<ResourceLocation> recipes) {
        this.unlockedRecipes.removeAll(recipes);
        if (player instanceof ServerPlayer)
            LoaderNetwork.INSTANCE.sendToPlayer(new S2CRecipe(recipes, true), (ServerPlayer) player);
    }

    public boolean isUnlocked(RecipeHolder<?> recipe) {
        if (GeneralConfig.recipeSystem.lockIsIgnored())
            return true;
        return this.unlockedRecipes.contains(recipe.id());
    }

    public boolean isUnlockedForCrafting(RecipeHolder<?> recipe) {
        if (GeneralConfig.recipeSystem == GeneralConfig.RecipeSystem.SKILLBLOCKLOCK ||
                GeneralConfig.recipeSystem == GeneralConfig.RecipeSystem.BASEBLOCKLOCK)
            return this.isUnlocked(recipe);
        return true;
    }

    public Collection<ResourceLocation> unlockedRecipes() {
        return ImmutableSet.copyOf(this.unlockedRecipes);
    }

    public CompoundTag save() {
        CompoundTag nbt = new CompoundTag();
        ListTag recipes = new ListTag();
        this.unlockedRecipes.forEach(res -> recipes.add(StringTag.valueOf(res.toString())));
        nbt.put("Unlocked", recipes);
        return nbt;
    }

    public void read(CompoundTag nbt) {
        this.unlockedRecipes.clear();
        ListTag recipes = nbt.getList("Unlocked", Tag.TAG_STRING);
        recipes.forEach(inbt -> this.unlockedRecipes.add(ResourceLocation.parse(inbt.getAsString())));
    }

    public void clientUpdate(Collection<ResourceLocation> recipes) {
        this.unlockedRecipes.clear();
        this.unlockedRecipes.addAll(recipes);
    }
}
