package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.utils.LootTableResources;
import io.github.flemmli97.runecraftory.forge.loot.LootTableInjectModifier;
import io.github.flemmli97.runecraftory.forge.loot.ModGlobalLootModifiers;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class LootModifierGen extends GlobalLootModifierProvider {

    public LootModifierGen(DataGenerator gen) {
        super(gen, RuneCraftory.MODID);
    }

    @Override
    protected void start() {
        for (ResourceLocation res : LootTableResources.VANILLA_CHESTS) {
            this.add("inject/" + res.getPath(), ModGlobalLootModifiers.TABLE_REFERENCE.get(),
                    new LootTableInjectModifier(new LootItemCondition[]{
                            LootTableIdCondition.builder(res).build()
                    }, LootTableResources.CHEST_LOOT_SPELLS));
        }
    }

    @Override
    public String getName() {
        return "LootModifier";
    }
}
