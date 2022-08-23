package io.github.flemmli97.runecraftory.common.datapack.manager;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.crafting.SextupleRecipe;
import io.github.flemmli97.runecraftory.common.registry.ModCrafting;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ItemCraftingLevelManager {

    private static Map<Item, Pair<Integer, EnumSkills>> map;

    public static void reload(MinecraftServer server) {
        map = new HashMap<>();
        add(server.getRecipeManager().getAllRecipesFor(ModCrafting.FORGE.get()), EnumSkills.FORGING);
        add(server.getRecipeManager().getAllRecipesFor(ModCrafting.CHEMISTRY.get()), EnumSkills.CHEMISTRY);
        add(server.getRecipeManager().getAllRecipesFor(ModCrafting.ARMOR.get()), EnumSkills.CRAFTING);
        add(server.getRecipeManager().getAllRecipesFor(ModCrafting.COOKING.get()), EnumSkills.COOKING);
    }

    private static void add(Collection<SextupleRecipe> recipes, EnumSkills skills) {
        for (SextupleRecipe recipe : recipes) {
            map.compute(recipe.getResultItem().getItem(), (item, old) -> {
                if (old != null && old.getFirst() <= recipe.getCraftingLevel())
                    return old;
                else
                    return Pair.of(recipe.getCraftingLevel(), skills);
            });
        }
    }

    public static Pair<Integer, EnumSkills> getLowestLevel(MinecraftServer server, Item item) {
        if (map == null) {
            reload(server);
        }
        return map.get(item);
    }

    public static void reset() {
        map = null;
    }
}
