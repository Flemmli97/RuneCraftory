package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockTagGen extends BlockTagsProvider {

    public BlockTagGen(DataGenerator gen, ExistingFileHelper existingFileHelper) {
        super(gen, RuneCraftory.MODID, existingFileHelper);
    }

    @Override
    public void addTags() {
        this.tag(ModTags.ENDSTONES)
                .add(Blocks.END_STONE)
                .addOptional(Tags.Blocks.END_STONES.location());

        this.tag(ModTags.sickleDestroyable)
                .addTag(BlockTags.CORAL_PLANTS)
                .addTag(BlockTags.CROPS)
                .addTag(BlockTags.FLOWERS)
                .addTag(BlockTags.LEAVES)
                .addTag(BlockTags.SAPLINGS)
                .addTag(BlockTags.TALL_FLOWERS)
                .addTag(BlockTags.REPLACEABLE_PLANTS)
                .add(Blocks.SEAGRASS, Blocks.TALL_SEAGRASS)
                .add(Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM, Blocks.CRIMSON_FUNGUS, Blocks.WARPED_FUNGUS);

        this.tag(ModTags.hammerFlattenable)
                .add(Blocks.FARMLAND, ModBlocks.farmland.get(), Blocks.DIRT_PATH);

        this.tag(ModTags.hammerBreakable)
                .add(ModBlocks.mineralMap.values().stream().map(RegistryEntrySupplier::get).toArray(Block[]::new));

        for (RegistryEntrySupplier<Block> sup : ModBlocks.mineralMap.values()) {
            this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(sup.get());
            this.tag(BlockTags.NEEDS_IRON_TOOL).add(sup.get());
        }
        for (RegistryEntrySupplier<Block> sup : ModBlocks.brokenMineralMap.values()) {
            this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(sup.get());
            this.tag(BlockTags.NEEDS_IRON_TOOL).add(sup.get());
        }

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.forge.get(), ModBlocks.cooking.get(), ModBlocks.chemistry.get(), ModBlocks.accessory.get(),
                ModBlocks.bossSpawner.get());
        this.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(ModBlocks.farmland.get());
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.board.get(), ModBlocks.shipping.get());

        this.tag(ModTags.farmland).add(Blocks.FARMLAND, ModBlocks.farmland.get());
    }
}
