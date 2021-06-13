package io.github.flemmli97.runecraftory.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.loot.CropLootModifier;
import io.github.flemmli97.runecraftory.common.registry.ModLootModifier;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.loot.conditions.ILootCondition;
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
        if (block instanceof CropsBlock) {
            return new CropLootModifier(new ILootCondition[]{BlockStateProperty.builder(block).fromProperties(
                    StatePropertiesPredicate.Builder.newBuilder().withIntProp(CropsBlock.AGE, ((CropsBlock) block).getMaxAge())).build()}, ignore);
        }
        throw new IllegalArgumentException("Block is not a crop");
    }
}
