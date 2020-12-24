package com.flemmli97.runecraftory.data;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.blocks.BlockCrafting;
import com.flemmli97.runecraftory.common.blocks.BlockCrop;
import com.flemmli97.runecraftory.common.blocks.BlockHerb;
import com.flemmli97.runecraftory.common.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStatesGen extends BlockStateProvider {

    public BlockStatesGen(DataGenerator gen, ExistingFileHelper helper) {
        super(gen, RuneCraftory.MODID, helper);
    }

    @Override
    protected void registerStatesAndModels() {
        ModBlocks.herbs.forEach(reg -> {
            Block block = reg.get();
            if (block instanceof BlockHerb) {
                ResourceLocation texture = this.itemTexture(block);
                this.simpleBlock(block, this.models().cross(block.getRegistryName().toString(), texture)
                        .texture("particle", texture));
            }
        });
        ModBlocks.flowers.forEach(reg -> {
            Block block = reg.get();
            this.getVariantBuilder(block).forAllStates(state -> {
                int stage = state.get(BlockCrop.AGE);
                String name = stage == 0 ? "runecraftory:flower_stage_0" : block.getRegistryName().toString() + "_" + stage;
                ResourceLocation texture = stage == 0 ? this.blockTexture(RuneCraftory.MODID, "flower_stage_0")
                        : stage == 3 ? this.itemCropTexture(block) : this.blockTexture(RuneCraftory.MODID, block.getRegistryName().getPath() + "_" + stage);
                return ConfiguredModel.builder().modelFile(this.models().cross(name, texture)
                        .texture("particle", texture)).build();
            });
        });
        ModBlocks.crops.forEach(reg -> {
            Block block = reg.get();
            this.getVariantBuilder(block).forAllStates(state -> {
                int stage = state.get(BlockCrop.AGE);
                String name = block.getRegistryName().toString() + "_" + stage;
                ResourceLocation texture = this.blockTexture(RuneCraftory.MODID, block.getRegistryName().getPath() + "_" + stage);
                return ConfiguredModel.builder().modelFile(this.models().crop(name, texture)
                        .texture("particle", texture)).build();
            });
        });
        ModBlocks.mineralMap.values().forEach(reg -> {
            Block block = reg.get();
            this.getVariantBuilder(block)
                    .forAllStatesExcept(state -> ConfiguredModel.builder()
                                    .modelFile(this.models().withExistingParent(block.getRegistryName().toString(), "runecraftory:block/ore").texture("ore", this.blockTexture(block)))
                                    .rotationY(((int) state.get(BlockStateProperties.HORIZONTAL_FACING).getHorizontalAngle()) % 360)
                                    .build(),
                            BlockStateProperties.WATERLOGGED
                    );
        });
        ModBlocks.brokenMineralMap.values().forEach(reg -> {
            Block block = reg.get();
            this.getVariantBuilder(block)
                    .forAllStatesExcept(state -> ConfiguredModel.builder()
                                    .modelFile(this.models().withExistingParent(block.getRegistryName().toString(), "runecraftory:block/ore_broken").texture("ore", this.mineralTexture(block)))
                                    .rotationY(((int) state.get(BlockStateProperties.HORIZONTAL_FACING).getHorizontalAngle()) % 360)
                                    .build(),
                            BlockStateProperties.WATERLOGGED
                    );
        });
        this.craftingModel(ModBlocks.forge.get());
        this.craftingModel(ModBlocks.accessory.get());
        this.craftingModel(ModBlocks.cooking.get());
        this.craftingModel(ModBlocks.chemistry.get());
        this.simpleBlock(ModBlocks.board.get());
        this.simpleBlock(ModBlocks.bossSpawner.get(), this.models().getExistingFile(new ResourceLocation("block/" + Blocks.SPAWNER.getRegistryName().getPath())));

        this.getVariantBuilder(ModBlocks.farmland.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(
                this.models().getExistingFile(state.get(FarmlandBlock.MOISTURE) == 7 ? new ResourceLocation("block/farmland_moist") : new ResourceLocation("block/farmland")))
                .build());

        this.getVariantBuilder(ModBlocks.shipping.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(
                this.models().getExistingFile(new ResourceLocation(RuneCraftory.MODID, "block/shipping_bin")))
                .rotationY(((int) state.get(BlockStateProperties.HORIZONTAL_FACING).getHorizontalAngle()) % 360)
                .build());
    }

    public ResourceLocation mineralTexture(Block block) {
        ResourceLocation name = block.getRegistryName();
        return new ResourceLocation(name.getNamespace(), "blocks" + "/" + name.getPath().replace("broken_", ""));
    }

    private void craftingModel(Block block) {
        this.getVariantBuilder(block)
                .forAllStatesExcept(state -> ConfiguredModel.builder()
                                .modelFile(this.models().cubeAll(block.getRegistryName().toString() + "_" + state.get(BlockCrafting.PART).getString(), new ResourceLocation("block/stone")))
                                .rotationY(((int) state.get(BlockStateProperties.HORIZONTAL_FACING).getHorizontalAngle()) % 360)
                                .build()
                        //, BlockStateProperties.WATERLOGGED
                );
    }

    @Override
    public ResourceLocation blockTexture(Block block) {
        ResourceLocation name = block.getRegistryName();
        return new ResourceLocation(name.getNamespace(), "blocks" + "/" + name.getPath());
    }

    public ResourceLocation blockTexture(String nameSpace, String block) {
        return new ResourceLocation(nameSpace, "blocks" + "/" + block);
    }

    public ResourceLocation itemTexture(Block block) {
        ResourceLocation name = block.getRegistryName();
        return new ResourceLocation(name.getNamespace(), "items" + "/" + name.getPath());
    }

    public ResourceLocation itemCropTexture(Block block) {
        ResourceLocation name = block.getRegistryName();
        return new ResourceLocation(name.getNamespace(), "items" + "/" + name.getPath().replace("plant", "crop"));
    }
}
