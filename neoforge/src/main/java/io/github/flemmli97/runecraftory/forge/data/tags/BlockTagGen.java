package io.github.flemmli97.runecraftory.forge.data.tags;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class BlockTagGen extends IntrinsicHolderTagsProvider<Block> {

    @SuppressWarnings("deprecation")
    public BlockTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.BLOCK, lookupProvider, block -> block.builtInRegistryHolder().key(), RuneCraftory.MODID, null);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        //Snow
        this.tag(BlockTags.SNOW).add(ModBlocks.SNOW.get());
        this.tag(BlockTags.INSIDE_STEP_SOUND_BLOCKS).add(ModBlocks.SNOW.get());
        this.tag(BlockTags.GOATS_SPAWNABLE_ON).add(ModBlocks.SNOW.get());
        this.tag(BlockTags.RABBITS_SPAWNABLE_ON).add(ModBlocks.SNOW.get());
        this.tag(BlockTags.FOXES_SPAWNABLE_ON).add(ModBlocks.SNOW.get());
        this.tag(BlockTags.WOLVES_SPAWNABLE_ON).add(ModBlocks.SNOW.get());
        this.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(ModBlocks.SNOW.get());

        this.tag(RunecraftoryTags.Blocks.ORES)
                .add(ModBlocks.MINERAL_MAP.values().stream().map(RegistryEntrySupplier::get).toArray(Block[]::new));

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
                .add(ModBlocks.MINERAL_MAP.values().stream().map(RegistryEntrySupplier::get).toArray(Block[]::new));

        for (RegistryEntrySupplier<Block, ?> sup : ModBlocks.MINERAL_MAP.values()) {
            this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(sup.get());
            this.tag(BlockTags.NEEDS_IRON_TOOL).add(sup.get());
        }
        for (RegistryEntrySupplier<Block, ?> sup : ModBlocks.HERBS) {
            this.tag(RunecraftoryTags.Blocks.HERBS).add(sup.get());
        }
        this.tag(RunecraftoryTags.Blocks.SICKLE_EFFECTIVE)
                .addTag(BlockTags.LEAVES)
                .addTag(BlockTags.WART_BLOCKS);
        this.tag(RunecraftoryTags.Blocks.SICKLE_DESTROYABLE)
                .addTag(RunecraftoryTags.Blocks.HERBS)
                .addTag(RunecraftoryTags.Blocks.CROP_BLOCKS)
                .addTag(RunecraftoryTags.Blocks.FLOWER_BLOCKS);
        for (RegistryEntrySupplier<Block, ?> sup : ModBlocks.CROPS) {
            this.tag(RunecraftoryTags.Blocks.CROP_BLOCKS).add(sup.get());
            RegistryEntrySupplier<Block, ?> giant = ModBlocks.GIANT_CROP_MAP.get(sup);
            if (giant != null)
                this.tag(RunecraftoryTags.Blocks.GIANT_CROP_BLOCKS)
                        .add(giant.get());
        }
        for (RegistryEntrySupplier<Block, ?> sup : ModBlocks.FLOWERS) {
            this.tag(RunecraftoryTags.Blocks.FLOWER_BLOCKS).add(sup.get());
            RegistryEntrySupplier<Block, ?> giant = ModBlocks.GIANT_CROP_MAP.get(sup);
            if (giant != null)
                this.tag(RunecraftoryTags.Blocks.GIANT_CROP_BLOCKS)
                        .add(giant.get());
        }

        this.tag(RunecraftoryTags.Blocks.MONSTER_CLEARABLE).addTag(RunecraftoryTags.Blocks.HERBS);

        for (RegistryEntrySupplier<Block, ?> sup : ModBlocks.BROKEN_MINERAL_MAP.values()) {
            this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(sup.get());
            this.tag(BlockTags.NEEDS_IRON_TOOL).add(sup.get());
        }

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.FORGE.get(), ModBlocks.COOKING.get(), ModBlocks.CHEMISTRY.get(),
                ModBlocks.BOSS_SPAWNER.get(), ModBlocks.CASH_REGISTER.get());
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.SHIPPING.get(), ModBlocks.ACCESSORY.get(), ModBlocks.QUEST_BOARD.get());
        this.tag(BlockTags.MINEABLE_WITH_HOE).add(ModBlocks.MONSTER_BARN.get());

        this.tag(RunecraftoryTags.Blocks.FARMLAND).add(Blocks.FARMLAND).add(ModBlocks.TREE_SOIL.get());

        this.tag(RunecraftoryTags.Blocks.BARN_GROUND).add(Blocks.HAY_BLOCK);

        this.tag(RunecraftoryTags.Blocks.MINERAL_GEN_PLACE).addTag(BlockTags.DIRT).addTag(BlockTags.BASE_STONE_OVERWORLD).addTag(BlockTags.SAND).addTag(RunecraftoryTags.Blocks.STONE)
                .addTag(BlockTags.BASE_STONE_NETHER).addTag(RunecraftoryTags.Blocks.ENDSTONES);

        this.tag(BlockTags.LOGS)
                .add(ModBlocks.APPLE_TREE.get())
                .add(ModBlocks.APPLE_WOOD.get())
                .add(ModBlocks.ORANGE_TREE.get())
                .add(ModBlocks.ORANGE_WOOD.get())
                .add(ModBlocks.GRAPE_TREE.get())
                .add(ModBlocks.GRAPE_WOOD.get());
        this.tag(BlockTags.LEAVES)
                .add(ModBlocks.APPLE_LEAVES.get())
                .add(ModBlocks.APPLE.get())
                .add(ModBlocks.ORANGE_LEAVES.get())
                .add(ModBlocks.ORANGE.get())
                .add(ModBlocks.GRAPE_LEAVES.get())
                .add(ModBlocks.GRAPE.get());

    }
}
