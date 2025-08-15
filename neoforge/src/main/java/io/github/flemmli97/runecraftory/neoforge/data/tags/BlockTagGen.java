package io.github.flemmli97.runecraftory.neoforge.data.tags;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryBlocks;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class BlockTagGen extends IntrinsicHolderTagsProvider<Block> {

    public BlockTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
        super(output, Registries.BLOCK, lookupProvider, block -> BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow(), RuneCraftory.MODID, fileHelper);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.tag(RunecraftoryTags.Blocks.ORES)
                .add(RuneCraftoryBlocks.MINERAL_MAP.values().stream().map(RegistryEntrySupplier::get).toArray(Block[]::new));

        this.tag(RunecraftoryTags.Blocks.SICKLE_DESTROYABLE)
                .addTag(BlockTags.CORAL_PLANTS)
                .addTag(BlockTags.CROPS)
                .addTag(BlockTags.FLOWERS)
                .addTag(BlockTags.LEAVES)
                .addTag(BlockTags.SAPLINGS)
                .addTag(BlockTags.TALL_FLOWERS)
                .add(Blocks.SEAGRASS, Blocks.TALL_SEAGRASS)
                .add(Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM, Blocks.CRIMSON_FUNGUS, Blocks.WARPED_FUNGUS);

        this.tag(RunecraftoryTags.Blocks.HAMMER_FLATTENABLE)
                .add(Blocks.FARMLAND, Blocks.DIRT_PATH);

        this.tag(RunecraftoryTags.Blocks.HAMMER_BREAKABLE)
                .add(RuneCraftoryBlocks.MINERAL_MAP.values().stream().map(RegistryEntrySupplier::get).toArray(Block[]::new));

        for (RegistryEntrySupplier<Block, ?> sup : RuneCraftoryBlocks.MINERAL_MAP.values()) {
            this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(sup.get());
            this.tag(BlockTags.NEEDS_IRON_TOOL).add(sup.get());
        }
        for (RegistryEntrySupplier<Block, ?> sup : RuneCraftoryBlocks.HERBS) {
            this.tag(RunecraftoryTags.Blocks.HERBS).add(sup.get());
        }
        this.tag(RunecraftoryTags.Blocks.SICKLE_EFFECTIVE)
                .addTag(BlockTags.LEAVES)
                .addTag(BlockTags.WART_BLOCKS);
        this.tag(RunecraftoryTags.Blocks.SICKLE_DESTROYABLE)
                .addTag(RunecraftoryTags.Blocks.HERBS)
                .addTag(RunecraftoryTags.Blocks.CROP_BLOCKS)
                .addTag(RunecraftoryTags.Blocks.FLOWER_BLOCKS);
        for (RegistryEntrySupplier<Block, ?> sup : RuneCraftoryBlocks.CROPS) {
            this.tag(RunecraftoryTags.Blocks.CROP_BLOCKS).add(sup.get());
            RegistryEntrySupplier<Block, ?> giant = RuneCraftoryBlocks.GIANT_CROP_MAP.get(sup);
            if (giant != null)
                this.tag(RunecraftoryTags.Blocks.GIANT_CROP_BLOCKS)
                        .add(giant.get());
        }
        for (RegistryEntrySupplier<Block, ?> sup : RuneCraftoryBlocks.FLOWERS) {
            this.tag(RunecraftoryTags.Blocks.FLOWER_BLOCKS).add(sup.get());
            RegistryEntrySupplier<Block, ?> giant = RuneCraftoryBlocks.GIANT_CROP_MAP.get(sup);
            if (giant != null)
                this.tag(RunecraftoryTags.Blocks.GIANT_CROP_BLOCKS)
                        .add(giant.get());
        }

        this.tag(RunecraftoryTags.Blocks.MONSTER_CLEARABLE).addTag(RunecraftoryTags.Blocks.HERBS);

        for (RegistryEntrySupplier<Block, ?> sup : RuneCraftoryBlocks.BROKEN_MINERAL_MAP.values()) {
            this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(sup.get());
            this.tag(BlockTags.NEEDS_IRON_TOOL).add(sup.get());
        }

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(RuneCraftoryBlocks.FORGE.get(), RuneCraftoryBlocks.COOKING_TABLE.get(), RuneCraftoryBlocks.CHEMISTRY_SET.get(),
                RuneCraftoryBlocks.BOSS_SPAWNER.get(), RuneCraftoryBlocks.CASH_REGISTER.get());
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(RuneCraftoryBlocks.SHIPPING.get(), RuneCraftoryBlocks.ACCESSORY_WORKBENCH.get(), RuneCraftoryBlocks.QUEST_BOARD.get());
        this.tag(BlockTags.MINEABLE_WITH_HOE).add(RuneCraftoryBlocks.MONSTER_BARN.get());

        this.tag(RunecraftoryTags.Blocks.FARMLAND).add(Blocks.FARMLAND).add(RuneCraftoryBlocks.TREE_SOIL.get());

        this.tag(RunecraftoryTags.Blocks.BARN_GROUND).add(Blocks.HAY_BLOCK);

        this.tag(RunecraftoryTags.Blocks.MINERAL_GEN_PLACE).addTag(BlockTags.DIRT).addTag(BlockTags.BASE_STONE_OVERWORLD).addTag(BlockTags.SAND).addTag(RunecraftoryTags.Blocks.STONE)
                .addTag(BlockTags.BASE_STONE_NETHER).addOptionalTag(RunecraftoryTags.Blocks.ENDSTONES);

        this.tag(BlockTags.LOGS)
                .add(RuneCraftoryBlocks.APPLE_TREE.get())
                .add(RuneCraftoryBlocks.APPLE_WOOD.get())
                .add(RuneCraftoryBlocks.ORANGE_TREE.get())
                .add(RuneCraftoryBlocks.ORANGE_WOOD.get())
                .add(RuneCraftoryBlocks.GRAPE_TREE.get())
                .add(RuneCraftoryBlocks.GRAPE_WOOD.get());
        this.tag(BlockTags.LEAVES)
                .add(RuneCraftoryBlocks.APPLE_LEAVES.get())
                .add(RuneCraftoryBlocks.APPLE.get())
                .add(RuneCraftoryBlocks.ORANGE_LEAVES.get())
                .add(RuneCraftoryBlocks.ORANGE.get())
                .add(RuneCraftoryBlocks.GRAPE_LEAVES.get())
                .add(RuneCraftoryBlocks.GRAPE.get());
    }
}
