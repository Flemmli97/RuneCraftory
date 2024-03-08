package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
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
        //Forge tag clone
        this.tag(RunecraftoryTags.ENDSTONES)
                .add(Blocks.END_STONE)
                .addOptional(Tags.Blocks.END_STONES.location());
        this.tag(RunecraftoryTags.STONE)
                .add(Blocks.ANDESITE, Blocks.DIORITE, Blocks.GRANITE, Blocks.INFESTED_STONE, Blocks.STONE, Blocks.POLISHED_ANDESITE, Blocks.POLISHED_DIORITE, Blocks.POLISHED_GRANITE, Blocks.DEEPSLATE, Blocks.POLISHED_DEEPSLATE, Blocks.INFESTED_DEEPSLATE, Blocks.TUFF)
                .addOptional(Tags.Blocks.STONE.location());

        //Snow
        this.tag(BlockTags.SNOW).add(ModBlocks.SNOW.get());
        this.tag(BlockTags.INSIDE_STEP_SOUND_BLOCKS).add(ModBlocks.SNOW.get());
        this.tag(BlockTags.GOATS_SPAWNABLE_ON).add(ModBlocks.SNOW.get());
        this.tag(BlockTags.RABBITS_SPAWNABLE_ON).add(ModBlocks.SNOW.get());
        this.tag(BlockTags.FOXES_SPAWNABLE_ON).add(ModBlocks.SNOW.get());
        this.tag(BlockTags.WOLVES_SPAWNABLE_ON).add(ModBlocks.SNOW.get());
        this.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(ModBlocks.SNOW.get());

        this.tag(RunecraftoryTags.ORES)
                .add(ModBlocks.MINERAL_MAP.values().stream().map(RegistryEntrySupplier::get).toArray(Block[]::new));

        this.tag(RunecraftoryTags.SICKLE_DESTROYABLE)
                .addTag(BlockTags.CORAL_PLANTS)
                .addTag(BlockTags.CROPS)
                .addTag(BlockTags.FLOWERS)
                .addTag(BlockTags.LEAVES)
                .addTag(BlockTags.SAPLINGS)
                .addTag(BlockTags.TALL_FLOWERS)
                .addTag(BlockTags.REPLACEABLE_PLANTS)
                .add(Blocks.SEAGRASS, Blocks.TALL_SEAGRASS)
                .add(Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM, Blocks.CRIMSON_FUNGUS, Blocks.WARPED_FUNGUS);

        this.tag(RunecraftoryTags.HAMMER_FLATTENABLE)
                .add(Blocks.FARMLAND, Blocks.DIRT_PATH);

        this.tag(RunecraftoryTags.HAMMER_BREAKABLE)
                .add(ModBlocks.MINERAL_MAP.values().stream().map(RegistryEntrySupplier::get).toArray(Block[]::new));

        for (RegistryEntrySupplier<Block> sup : ModBlocks.MINERAL_MAP.values()) {
            this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(sup.get());
            this.tag(BlockTags.NEEDS_IRON_TOOL).add(sup.get());
        }
        for (RegistryEntrySupplier<Block> sup : ModBlocks.HERBS) {
            this.tag(RunecraftoryTags.HERBS).add(sup.get());
        }
        this.tag(RunecraftoryTags.SICKLE_DESTROYABLE)
                .addTag(RunecraftoryTags.HERBS)
                .addTag(RunecraftoryTags.CROP_BLOCKS)
                .addTag(RunecraftoryTags.FLOWER_BLOCKS);
        for (RegistryEntrySupplier<Block> sup : ModBlocks.CROPS) {
            this.tag(RunecraftoryTags.CROP_BLOCKS).add(sup.get());
            RegistryEntrySupplier<Block> giant = ModBlocks.GIANT_CROP_MAP.get(sup);
            if (giant != null)
                this.tag(RunecraftoryTags.GIANT_CROP_BLOCKS)
                        .add(giant.get());
        }
        for (RegistryEntrySupplier<Block> sup : ModBlocks.FLOWERS) {
            this.tag(RunecraftoryTags.FLOWER_BLOCKS).add(sup.get());
            RegistryEntrySupplier<Block> giant = ModBlocks.GIANT_CROP_MAP.get(sup);
            if (giant != null)
                this.tag(RunecraftoryTags.GIANT_CROP_BLOCKS)
                        .add(giant.get());
        }

        this.tag(RunecraftoryTags.MONSTER_CLEARABLE).addTag(RunecraftoryTags.HERBS);

        for (RegistryEntrySupplier<Block> sup : ModBlocks.BROKEN_MINERAL_MAP.values()) {
            this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(sup.get());
            this.tag(BlockTags.NEEDS_IRON_TOOL).add(sup.get());
        }

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.FORGE.get(), ModBlocks.COOKING.get(), ModBlocks.CHEMISTRY.get(),
                ModBlocks.BOSS_SPAWNER.get(), ModBlocks.CASH_REGISTER.get());
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.SHIPPING.get(), ModBlocks.ACCESSORY.get(), ModBlocks.QUEST_BOARD.get());
        this.tag(BlockTags.MINEABLE_WITH_HOE).add(ModBlocks.MONSTER_BARN.get());

        this.tag(RunecraftoryTags.FARMLAND).add(Blocks.FARMLAND).add(ModBlocks.TREE_SOIL.get());

        this.tag(RunecraftoryTags.BARN_GROUND).add(Blocks.HAY_BLOCK);

        this.tag(RunecraftoryTags.ONSEN_PROVIDER).addTag(BlockTags.CAMPFIRES);

        this.tag(RunecraftoryTags.MINERAL_GEN_PLACE).addTag(BlockTags.DIRT).addTag(BlockTags.BASE_STONE_OVERWORLD).addTag(BlockTags.SAND).addTag(RunecraftoryTags.STONE)
                .addTag(BlockTags.BASE_STONE_NETHER).addTag(RunecraftoryTags.ENDSTONES);

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
