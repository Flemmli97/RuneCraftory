package io.github.flemmli97.runecraftory.common.capability;

import io.github.flemmli97.runecraftory.common.network.PacketHandler;
import io.github.flemmli97.runecraftory.common.network.S2CRecipe;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class RecipeKeeper {

    private Set<ResourceLocation> unlockedRecipes = new HashSet<>();

    public void unlockRecipe(PlayerEntity player, IRecipe<?> recipe) {
        this.unlockRecipesRes(player, Sets.newHashSet(recipe.getId()));
    }

    public void unlockRecipe(PlayerEntity player, ResourceLocation res) {
        this.unlockRecipesRes(player, Sets.newHashSet(res));
    }

    public void unlockRecipes(PlayerEntity player, Collection<? extends IRecipe<?>> recipes) {
        this.unlockRecipesRes(player, recipes.stream().map(IRecipe::getId).collect(Collectors.toSet()));
    }

    public void unlockRecipesRes(PlayerEntity player, Collection<ResourceLocation> recipes) {
        recipes.forEach(this.unlockedRecipes::add);
        if (player instanceof ServerPlayerEntity)
            PacketHandler.sendToClient(new S2CRecipe(recipes, false), (ServerPlayerEntity) player);
    }

    public void lockRecipe(PlayerEntity player, IRecipe<?> recipe) {
        this.lockRecipesRes(player, Sets.newHashSet(recipe.getId()));
    }

    public void lockRecipe(PlayerEntity player, ResourceLocation res) {
        this.lockRecipesRes(player, Sets.newHashSet(res));
    }

    public void lockRecipes(PlayerEntity player, Collection<? extends IRecipe<?>> recipes) {
        this.lockRecipesRes(player, recipes.stream().map(IRecipe::getId).collect(Collectors.toSet()));
    }

    public void lockRecipesRes(PlayerEntity player, Collection<ResourceLocation> recipes) {
        recipes.forEach(this.unlockedRecipes::remove);
        if (player instanceof ServerPlayerEntity)
            PacketHandler.sendToClient(new S2CRecipe(recipes, true), (ServerPlayerEntity) player);
    }

    public boolean isUnlocked(IRecipe<?> recipe) {
        return this.unlockedRecipes.contains(recipe.getId());
    }

    public Collection<ResourceLocation> unlockedRecipes() {
        return ImmutableSet.copyOf(this.unlockedRecipes);
    }

    public CompoundNBT save() {
        CompoundNBT nbt = new CompoundNBT();
        ListNBT recipes = new ListNBT();
        this.unlockedRecipes.forEach(res -> recipes.add(StringNBT.valueOf(res.toString())));
        nbt.put("Unlocked", recipes);
        return nbt;
    }

    public void read(CompoundNBT nbt) {
        this.unlockedRecipes.clear();
        ListNBT recipes = nbt.getList("Unlocked", Constants.NBT.TAG_STRING);
        recipes.forEach(inbt -> this.unlockedRecipes.add(new ResourceLocation(inbt.getString())));
    }

    public void clientUpdate(Collection<ResourceLocation> recipes) {
        this.unlockedRecipes.clear();
        recipes.forEach(this.unlockedRecipes::add);
    }
}
