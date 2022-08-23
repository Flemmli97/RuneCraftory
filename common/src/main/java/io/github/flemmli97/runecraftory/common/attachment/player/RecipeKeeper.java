package io.github.flemmli97.runecraftory.common.attachment.player;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import io.github.flemmli97.runecraftory.common.network.S2CRecipe;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class RecipeKeeper {

    private Set<ResourceLocation> unlockedRecipes = new HashSet<>();

    public void unlockRecipe(Player player, Recipe<?> recipe) {
        this.unlockRecipesRes(player, Sets.newHashSet(recipe.getId()));
    }

    public void unlockRecipe(Player player, ResourceLocation res) {
        this.unlockRecipesRes(player, Sets.newHashSet(res));
    }

    public void unlockRecipes(Player player, Collection<? extends Recipe<?>> recipes) {
        this.unlockRecipesRes(player, recipes.stream().map(Recipe::getId).collect(Collectors.toSet()));
    }

    public void unlockRecipesRes(Player player, Collection<ResourceLocation> recipes) {
        this.unlockedRecipes.addAll(recipes);
        if (player instanceof ServerPlayer)
            Platform.INSTANCE.sendToClient(new S2CRecipe(recipes, false), (ServerPlayer) player);
    }

    public void lockRecipe(Player player, Recipe<?> recipe) {
        this.lockRecipesRes(player, Sets.newHashSet(recipe.getId()));
    }

    public void lockRecipe(Player player, ResourceLocation res) {
        this.lockRecipesRes(player, Sets.newHashSet(res));
    }

    public void lockRecipes(Player player, Collection<? extends Recipe<?>> recipes) {
        this.lockRecipesRes(player, recipes.stream().map(Recipe::getId).collect(Collectors.toSet()));
    }

    public void lockRecipesRes(Player player, Collection<ResourceLocation> recipes) {
        recipes.forEach(this.unlockedRecipes::remove);
        if (player instanceof ServerPlayer)
            Platform.INSTANCE.sendToClient(new S2CRecipe(recipes, true), (ServerPlayer) player);
    }

    public boolean isUnlocked(Recipe<?> recipe) {
        return this.unlockedRecipes.contains(recipe.getId());
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
        recipes.forEach(inbt -> this.unlockedRecipes.add(new ResourceLocation(inbt.getAsString())));
    }

    public void clientUpdate(Collection<ResourceLocation> recipes) {
        this.unlockedRecipes.clear();
        this.unlockedRecipes.addAll(recipes);
    }
}
