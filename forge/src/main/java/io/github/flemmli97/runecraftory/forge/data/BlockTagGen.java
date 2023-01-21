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
        //Forge tag clone
        this.tag(ModTags.ENDSTONES)
                .add(Blocks.END_STONE)
                .addOptional(Tags.Blocks.END_STONES.location());
        this.tag(ModTags.STONE)
                .add(Blocks.ANDESITE, Blocks.DIORITE, Blocks.GRANITE, Blocks.INFESTED_STONE, Blocks.STONE, Blocks.POLISHED_ANDESITE, Blocks.POLISHED_DIORITE, Blocks.POLISHED_GRANITE, Blocks.DEEPSLATE, Blocks.POLISHED_DEEPSLATE, Blocks.INFESTED_DEEPSLATE, Blocks.TUFF)
                .addOptional(Tags.Blocks.STONE.location());

        //Snow
        this.tag(BlockTags.SNOW).add(ModBlocks.snow.get());
        this.tag(BlockTags.INSIDE_STEP_SOUND_BLOCKS).add(ModBlocks.snow.get());
        this.tag(BlockTags.GOATS_SPAWNABLE_ON).add(ModBlocks.snow.get());
        this.tag(BlockTags.RABBITS_SPAWNABLE_ON).add(ModBlocks.snow.get());
        this.tag(BlockTags.FOXES_SPAWNABLE_ON).add(ModBlocks.snow.get());
        this.tag(BlockTags.WOLVES_SPAWNABLE_ON).add(ModBlocks.snow.get());
        this.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(ModBlocks.snow.get());

        this.tag(ModTags.SICKLE_DESTROYABLE)
                .addTag(BlockTags.CORAL_PLANTS)
                .addTag(BlockTags.CROPS)
                .addTag(BlockTags.FLOWERS)
                .addTag(BlockTags.LEAVES)
                .addTag(BlockTags.SAPLINGS)
                .addTag(BlockTags.TALL_FLOWERS)
                .addTag(BlockTags.REPLACEABLE_PLANTS)
                .add(Blocks.SEAGRASS, Blocks.TALL_SEAGRASS)
                .add(Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM, Blocks.CRIMSON_FUNGUS, Blocks.WARPED_FUNGUS);

        this.tag(ModTags.HAMMER_FLATTENABLE)
                .add(Blocks.FARMLAND, ModBlocks.farmland.get(), Blocks.DIRT_PATH);

        this.tag(ModTags.HAMMER_BREAKABLE)
                .add(ModBlocks.mineralMap.values().stream().map(RegistryEntrySupplier::get).toArray(Block[]::new));

        for (RegistryEntrySupplier<Block> sup : ModBlocks.mineralMap.values()) {
            this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(sup.get());
            this.tag(BlockTags.NEEDS_IRON_TOOL).add(sup.get());
        }
        for (RegistryEntrySupplier<Block> sup : ModBlocks.herbs) {
            this.tag(ModTags.SICKLE_DESTROYABLE).add(sup.get());
            this.tag(ModTags.HERBS).add(sup.get());
        }
        for (RegistryEntrySupplier<Block> sup : ModBlocks.crops) {
            this.tag(ModTags.SICKLE_DESTROYABLE).add(sup.get());
        }
        for (RegistryEntrySupplier<Block> sup : ModBlocks.flowers) {
            this.tag(ModTags.SICKLE_DESTROYABLE).add(sup.get());
        }

        this.tag(ModTags.MONSTER_CLEARABLE).addTag(ModTags.HERBS);

        for (RegistryEntrySupplier<Block> sup : ModBlocks.brokenMineralMap.values()) {
            this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(sup.get());
            this.tag(BlockTags.NEEDS_IRON_TOOL).add(sup.get());
        }

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.forge.get(), ModBlocks.cooking.get(), ModBlocks.chemistry.get(), ModBlocks.accessory.get(),
                ModBlocks.bossSpawner.get(), ModBlocks.cashRegister.get());
        this.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(ModBlocks.farmland.get());
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.shipping.get(), ModBlocks.questBoard.get());
        this.tag(BlockTags.MINEABLE_WITH_HOE).add(ModBlocks.monsterBarn.get());

        this.tag(ModTags.FARMLAND).add(Blocks.FARMLAND, ModBlocks.farmland.get());

        this.tag(ModTags.BARN_GROUND).add(Blocks.HAY_BLOCK);

        this.tag(ModTags.ONSEN_PROVIDER).addTag(BlockTags.CAMPFIRES);

        this.tag(ModTags.MINERAL_GEN_PLACE).addTag(BlockTags.DIRT).addTag(BlockTags.BASE_STONE_OVERWORLD).addTag(BlockTags.SAND).addTag(ModTags.STONE)
                .addTag(BlockTags.BASE_STONE_NETHER).addTag(ModTags.ENDSTONES);
    }
}
