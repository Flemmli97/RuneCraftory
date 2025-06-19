package io.github.flemmli97.runecraftory.neoforge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.utils.LootTableResources;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.concurrent.CompletableFuture;

public class LootModifierGen extends GlobalLootModifierProvider {

    public LootModifierGen(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries, RuneCraftory.MODID);
    }

    @Override
    protected void start() {
        for (ResourceKey<LootTable> res : LootTableResources.VANILLA_CHESTS) {
            this.add("inject/" + res.location().getPath(), new AddTableLootModifier(new LootItemCondition[]{
                    LootTableIdCondition.builder(res.location()).build()},
                    LootTableResources.CHEST_LOOT_SPELLS));
        }
    }

    @Override
    public String getName() {
        return "LootModifier";
    }
}
