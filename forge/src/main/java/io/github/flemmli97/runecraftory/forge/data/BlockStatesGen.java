package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.blocks.BlockCrafting;
import io.github.flemmli97.runecraftory.common.blocks.BlockCrop;
import io.github.flemmli97.runecraftory.common.blocks.BlockHerb;
import io.github.flemmli97.runecraftory.common.blocks.BlockQuestboard;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStatesGen extends BlockStateProvider {

    private static final ResourceLocation cropTinted = new ResourceLocation(RuneCraftory.MODID, "block/crop_tinted");
    private static final ResourceLocation crossTinted = new ResourceLocation(RuneCraftory.MODID, "block/cross_tinted");

    public BlockStatesGen(DataGenerator gen, ExistingFileHelper helper) {
        super(gen, RuneCraftory.MODID, helper);
    }

    @Override
    protected void registerStatesAndModels() {
        ModBlocks.herbs.forEach(reg -> {
            Block block = reg.get();
            if (block == ModBlocks.medicinalHerb.get())
                this.simpleBlock(block, this.models().withExistingParent(block.getRegistryName().toString(), new ResourceLocation(RuneCraftory.MODID + ":item/medicinal_herb")));
            else if (block instanceof BlockHerb) {
                ResourceLocation texture = this.itemTexture(block);
                this.simpleBlock(block, this.models().cross(block.getRegistryName().toString(), texture)
                        .texture("particle", texture));
            }
        });
        ModBlocks.flowers.forEach(reg -> {
            Block block = reg.get();
            this.getVariantBuilder(block).forAllStatesExcept(state -> {
                int stage = state.getValue(BlockCrop.AGE);
                boolean defaultFlowerState = stage == 0 && reg != ModBlocks.emeryFlower && reg != ModBlocks.ironleaf
                        && reg != ModBlocks.noelGrass && reg != ModBlocks.lampGrass;
                String name = defaultFlowerState ? "runecraftory:flower_stage_0" : block.getRegistryName().toString() + "_" + stage;
                ResourceLocation texture = defaultFlowerState ? this.blockTexture(RuneCraftory.MODID, "flower_stage_0")
                        : stage == 3 ? this.itemCropTexture(block) : this.blockTexture(RuneCraftory.MODID, block.getRegistryName().getPath() + "_" + stage);
                return ConfiguredModel.builder().modelFile(this.models().singleTexture(name, crossTinted, "cross", texture)).build();
            }, BlockCrop.WILTED);
        });
        ModBlocks.crops.forEach(reg -> {
            Block block = reg.get();
            this.getVariantBuilder(block).forAllStatesExcept(state -> {
                int stage = state.getValue(BlockCrop.AGE);
                String name = block.getRegistryName().toString() + "_" + stage;
                ResourceLocation texture = this.blockTexture(RuneCraftory.MODID, block.getRegistryName().getPath() + "_" + stage);
                return ConfiguredModel.builder().modelFile(this.models().singleTexture(name, cropTinted, "crop", texture)).build();
            }, BlockCrop.WILTED);
        });
        ModBlocks.mineralMap.values().forEach(reg -> {
            Block block = reg.get();
            this.getVariantBuilder(block)
                    .forAllStatesExcept(state -> {
                                BlockModelBuilder file = this.models().withExistingParent(block.getRegistryName().toString(), "runecraftory:block/ore").texture("ore", this.blockTexture(block));
                                if (block == ModBlocks.mineralDragonic.get())
                                    file = file.texture("0", new ResourceLocation("block/end_stone"));
                                return ConfiguredModel.builder()
                                        .modelFile(file)
                                        .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot()) % 360)
                                        .build();
                            },
                            BlockStateProperties.WATERLOGGED
                    );
        });
        ModBlocks.brokenMineralMap.values().forEach(reg -> {
            Block block = reg.get();
            this.getVariantBuilder(block)
                    .forAllStatesExcept(state -> {
                                BlockModelBuilder file = this.models().withExistingParent(block.getRegistryName().toString(), "runecraftory:block/ore_broken").texture("ore", this.mineralTexture(block));
                                if (block == ModBlocks.brokenMineralDragonic.get())
                                    file = file.texture("0", new ResourceLocation("block/end_stone"));
                                return ConfiguredModel.builder()
                                        .modelFile(file)
                                        .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot()) % 360)
                                        .build();
                            },
                            BlockStateProperties.WATERLOGGED
                    );
        });
        this.craftingModel(ModBlocks.forge.get());
        this.craftingModel(ModBlocks.accessory.get());
        this.craftingModel(ModBlocks.cooking.get());
        this.craftingModel(ModBlocks.chemistry.get());
        //this.simpleBlock(ModBlocks.board.get());
        this.simpleBlock(ModBlocks.bossSpawner.get(), this.models().getExistingFile(new ResourceLocation("block/" + Blocks.SPAWNER.getRegistryName().getPath())));
        this.simpleBlock(ModBlocks.singleSpawnBlock.get(), this.models().getExistingFile(new ResourceLocation("block/" + Blocks.SPAWNER.getRegistryName().getPath())));
        this.simpleBlock(ModBlocks.monsterBarn.get(), this.models().getExistingFile(new ResourceLocation(RuneCraftory.MODID, "block/" + ModBlocks.monsterBarn.getID().getPath())));

        this.getVariantBuilder(ModBlocks.farmland.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(
                        this.models().getExistingFile(state.getValue(FarmBlock.MOISTURE) == 7 ? new ResourceLocation("block/farmland_moist") : new ResourceLocation("block/farmland")))
                .build());

        this.getVariantBuilder(ModBlocks.snow.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(
                        this.models().getExistingFile(state.getValue(SnowLayerBlock.LAYERS) == 8 ? new ResourceLocation("block/snow_block") : new ResourceLocation("block/snow_height" + state.getValue(SnowLayerBlock.LAYERS) * 2)))
                .build());

        this.getVariantBuilder(ModBlocks.shipping.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(
                        this.models().orientableVertical(ModBlocks.shipping.getID().getPath(), this.modLoc("block/shipping_bin"), this.modLoc("block/shipping_bin_top")))
                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot()) % 360)
                .build());

        this.getVariantBuilder(ModBlocks.cashRegister.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(
                        this.models().getExistingFile(new ResourceLocation(RuneCraftory.MODID, "block/" + ModBlocks.cashRegister.getID().getPath())))
                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot()) % 360)
                .build());

        this.getVariantBuilder(ModBlocks.questBoard.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(
                        this.models().getExistingFile(new ResourceLocation(RuneCraftory.MODID, "block/" + ModBlocks.questBoard.getID().getPath() + "_" + state.getValue(BlockQuestboard.PART).getSerializedName())))
                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot()) % 360)
                .build());
    }

    @Override
    public ResourceLocation blockTexture(Block block) {
        ResourceLocation name = block.getRegistryName();
        return new ResourceLocation(name.getNamespace(), "block" + "/" + name.getPath());
    }

    public ResourceLocation mineralTexture(Block block) {
        ResourceLocation name = block.getRegistryName();
        return new ResourceLocation(name.getNamespace(), "block" + "/" + name.getPath().replace("broken_", ""));
    }

    private void craftingModel(Block block) {
        this.getVariantBuilder(block)
                .forAllStatesExcept(state -> ConfiguredModel.builder()
                                .modelFile(this.models().getExistingFile(new ResourceLocation(block.getRegistryName().getNamespace(), "block/" + block.getRegistryName().getPath() + "_" + state.getValue(BlockCrafting.PART).getSerializedName())))
                                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                                .build()
                        //, BlockStateProperties.WATERLOGGED
                );
    }

    public ResourceLocation blockTexture(String nameSpace, String block) {
        return new ResourceLocation(nameSpace, "block" + "/" + block);
    }

    public ResourceLocation itemTexture(Block block) {
        ResourceLocation name = block.getRegistryName();
        return new ResourceLocation(name.getNamespace(), "item" + "/" + name.getPath());
    }

    public ResourceLocation itemCropTexture(Block block) {
        ResourceLocation name = block.getRegistryName();
        return new ResourceLocation(name.getNamespace(), "item" + "/" + name.getPath().replace("plant", "crop"));
    }
}