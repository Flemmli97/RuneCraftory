package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.forge.loot.CropLootModifier;
import io.github.flemmli97.runecraftory.forge.registry.ModLootModifier;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class GlobalLootModifierGen extends GlobalLootModifierProvider {

    public GlobalLootModifierGen(DataGenerator gen) {
        super(gen, RuneCraftory.MODID);
    }

    @Override
    protected void start() {
        this.add("wheat", ModLootModifier.crop.get(), this.createForCrop(Blocks.WHEAT, Items.WHEAT_SEEDS));
        this.add("beetroot", ModLootModifier.crop.get(), this.createForCrop(Blocks.BEETROOTS, Items.BEETROOT_SEEDS));
        this.add("carrot", ModLootModifier.crop.get(), this.createForCrop(Blocks.CARROTS, Items.AIR));
        this.add("potato", ModLootModifier.crop.get(), this.createForCrop(Blocks.POTATOES, Items.AIR));
    }

    public CropLootModifier createForCrop(Block block, Item ignore) {
        if (block instanceof CropBlock) {
            return new CropLootModifier(new LootItemCondition[]{LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(
                    StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, ((CropBlock) block).getMaxAge())).build()}, ignore);
        }
        throw new IllegalArgumentException("Block is not a crop");
    }
}
