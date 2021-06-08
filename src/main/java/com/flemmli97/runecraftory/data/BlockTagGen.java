package com.flemmli97.runecraftory.data;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.registry.ModBlocks;
import com.flemmli97.runecraftory.common.registry.ModTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

public class BlockTagGen extends BlockTagsProvider {

    public BlockTagGen(DataGenerator gen, ExistingFileHelper existingFileHelper) {
        super(gen, RuneCraftory.MODID, existingFileHelper);
    }

    @Override
    public void registerTags() {
        this.getOrCreateTagBuilder(ModTags.farmlandTill)
                .addTags(Tags.Blocks.DIRT);

        this.getOrCreateTagBuilder(ModTags.sickleDestroyable)
                .addTags(BlockTags.CORAL_PLANTS, BlockTags.CROPS, BlockTags.FLOWERS, BlockTags.LEAVES,
                        BlockTags.SAPLINGS, BlockTags.TALL_FLOWERS)
                .add(Blocks.GRASS, Blocks.TALL_GRASS, Blocks.SEAGRASS, Blocks.TALL_SEAGRASS)
                .add(Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM, Blocks.CRIMSON_FUNGUS, Blocks.WARPED_FUNGUS);

        this.getOrCreateTagBuilder(ModTags.hammerFlattenable)
                .add(Blocks.FARMLAND, ModBlocks.farmland.get(), Blocks.GRASS_PATH);

        this.getOrCreateTagBuilder(ModTags.hammerBreakable)
                .add(ModBlocks.mineralMap.values().stream().map(RegistryObject::get).toArray(Block[]::new));
    }
}
