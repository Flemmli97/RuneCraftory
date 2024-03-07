package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.blocks.BlockCrafting;
import io.github.flemmli97.runecraftory.common.blocks.BlockCrop;
import io.github.flemmli97.runecraftory.common.blocks.BlockFruitTreeLeaf;
import io.github.flemmli97.runecraftory.common.blocks.BlockGiantCrop;
import io.github.flemmli97.runecraftory.common.blocks.BlockHerb;
import io.github.flemmli97.runecraftory.common.blocks.BlockQuestboard;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStatesGen extends BlockStateProvider {

    private static final ResourceLocation CROP_TINTED = new ResourceLocation(RuneCraftory.MODID, "block/crop_tinted");
    private static final ResourceLocation CROP_BIG = new ResourceLocation(RuneCraftory.MODID, "block/big_crop");
    private static final ResourceLocation CROP_GIANT_1 = new ResourceLocation(RuneCraftory.MODID, "block/giant_crop_1");
    private static final ResourceLocation CROP_GIANT_2 = new ResourceLocation(RuneCraftory.MODID, "block/giant_crop_2");
    private static final ResourceLocation FLOWER_BIG = new ResourceLocation(RuneCraftory.MODID, "block/big_flower");
    private static final ResourceLocation FLOWER_GIANT = new ResourceLocation(RuneCraftory.MODID, "block/giant_flower");
    private static final ResourceLocation CROSS_TINTED = new ResourceLocation(RuneCraftory.MODID, "block/cross_tinted");

    public BlockStatesGen(DataGenerator gen, ExistingFileHelper helper) {
        super(gen, RuneCraftory.MODID, helper);
    }

    @Override
    protected void registerStatesAndModels() {
        ModBlocks.HERBS.forEach(reg -> {
            Block block = reg.get();
            if (block == ModBlocks.MEDICINAL_HERB.get())
                this.simpleBlock(block, this.models().withExistingParent(block.getRegistryName().toString(), new ResourceLocation(RuneCraftory.MODID + ":item/medicinal_herb")));
            else if (block instanceof BlockHerb) {
                ResourceLocation texture = this.itemTexture(block);
                this.simpleBlock(block, this.models().cross(block.getRegistryName().toString(), texture)
                        .texture("particle", texture));
            }
        });
        for (RegistryEntrySupplier<Block> reg : ModBlocks.FLOWERS) {
            Block block = reg.get();
            if (reg == ModBlocks.SWORD_CROP || reg == ModBlocks.SHIELD_CROP) {
                this.getVariantBuilder(block).forAllStatesExcept(state -> {
                    int stage = state.getValue(BlockCrop.AGE);
                    ResourceLocation text;
                    if (stage == 3 || stage == 4) {
                        text = this.blockTexture(RuneCraftory.MODID, reg.getID().getPath());
                    } else {
                        text = this.blockTexture(RuneCraftory.MODID, "plant_gear_" + stage);
                    }
                    return ConfiguredModel.builder().modelFile(this.models().singleTexture(text.toString(), CROSS_TINTED, "cross", text)).build();
                }, BlockCrop.WILTED);
                continue;
            }
            if (block instanceof BlockGiantCrop giant)
                this.getVariantBuilder(block).forAllStatesExcept(state -> {
                    ResourceLocation texture = this.itemTexture(giant.getCrop());
                    return ConfiguredModel.builder().modelFile(this.models().singleTexture(block.getRegistryName().toString(), FLOWER_GIANT, "0", texture))
                            .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360).build();
                }, BlockCrop.WILTED, BlockGiantCrop.AGE);
            else if (block instanceof BlockCrop)
                this.getVariantBuilder(block).forAllStatesExcept(state -> {
                    int stage = state.getValue(BlockCrop.AGE);
                    boolean defaultFlowerState = stage == 0 && reg != ModBlocks.EMERY_FLOWER && reg != ModBlocks.IRONLEAF
                            && reg != ModBlocks.NOEL_GRASS && reg != ModBlocks.LAMP_GRASS;
                    String name = defaultFlowerState ? "runecraftory:flower_stage_0" : block.getRegistryName().toString() + "_" + stage;
                    ResourceLocation texture = defaultFlowerState ? this.blockTexture(RuneCraftory.MODID, "flower_stage_0")
                            : stage == 3 ? this.itemCropTexture(block) : this.blockTexture(RuneCraftory.MODID, block.getRegistryName().getPath() + "_" + stage);
                    ResourceLocation parent = CROSS_TINTED;
                    if (stage == 4) {
                        parent = FLOWER_BIG;
                        RegistryEntrySupplier<Block> giant = ModBlocks.GIANT_CROP_MAP.get(reg);
                        if (giant != null && giant.get() instanceof BlockGiantCrop giantCrop)
                            texture = this.itemTexture(giantCrop.getCrop());
                    }
                    return ConfiguredModel.builder().modelFile(this.models().singleTexture(name, parent, "cross", texture)).build();
                }, BlockCrop.WILTED);
        }
        ModBlocks.CROPS.forEach(reg -> {
            Block block = reg.get();
            if (block instanceof BlockGiantCrop)
                this.getVariantBuilder(block).forAllStatesExcept(state -> {
                    ResourceLocation texture = this.blockTexture(RuneCraftory.MODID, block.getRegistryName().getPath());
                    ResourceLocation parent = CROP_GIANT_1;
                    int rot = 0;
                    String file = block.getRegistryName().toString();
                    switch (state.getValue(BlockGiantCrop.DIRECTION)) {
                        case EAST -> {
                            parent = CROP_GIANT_2;
                            file += "_2";
                        }
                        case SOUTH -> rot = 180;
                        case WEST -> {
                            parent = CROP_GIANT_2;
                            file += "_2";
                            rot = 180;
                        }
                    }
                    return ConfiguredModel.builder().modelFile(this.models().singleTexture(file, parent, "crop", texture))
                            .rotationY(rot).build();
                }, BlockCrop.WILTED, BlockGiantCrop.AGE);
            else if (block instanceof BlockCrop)
                this.getVariantBuilder(block).forAllStatesExcept(state -> {
                    int stage = state.getValue(BlockCrop.AGE);
                    String name = block.getRegistryName().toString() + "_" + stage;
                    ResourceLocation texture = this.blockTexture(RuneCraftory.MODID, block.getRegistryName().getPath() + "_" + stage);
                    ResourceLocation parent = CROP_TINTED;
                    if (stage == 4) {
                        parent = CROP_BIG;
                        RegistryEntrySupplier<Block> giant = ModBlocks.GIANT_CROP_MAP.get(reg);
                        if (giant != null)
                            texture = this.blockTexture(giant.get());
                    }
                    return ConfiguredModel.builder().modelFile(this.models().singleTexture(name, parent, "crop", texture)).build();
                }, BlockCrop.WILTED);
        });
        ModBlocks.MINERAL_MAP.values().forEach(reg -> {
            Block block = reg.get();
            this.getVariantBuilder(block)
                    .forAllStatesExcept(state -> {
                                BlockModelBuilder file = this.models().withExistingParent(block.getRegistryName().toString(), "runecraftory:block/ore").texture("ore", this.blockTexture(block));
                                if (block == ModBlocks.MINERAL_DRAGONIC.get())
                                    file = file.texture("0", new ResourceLocation("block/end_stone"));
                                return ConfiguredModel.builder()
                                        .modelFile(file)
                                        .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot()) % 360)
                                        .build();
                            },
                            BlockStateProperties.WATERLOGGED
                    );
        });
        ModBlocks.BROKEN_MINERAL_MAP.values().forEach(reg -> {
            Block block = reg.get();
            this.getVariantBuilder(block)
                    .forAllStatesExcept(state -> {
                                BlockModelBuilder file = this.models().withExistingParent(block.getRegistryName().toString(), "runecraftory:block/ore_broken").texture("ore", this.mineralTexture(block));
                                if (block == ModBlocks.BROKEN_MINERAL_DRAGONIC.get())
                                    file = file.texture("0", new ResourceLocation("block/end_stone"));
                                return ConfiguredModel.builder()
                                        .modelFile(file)
                                        .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot()) % 360)
                                        .build();
                            },
                            BlockStateProperties.WATERLOGGED
                    );
        });
        this.craftingModel(ModBlocks.FORGE.get());
        this.craftingModel(ModBlocks.ACCESSORY.get());
        this.craftingModel(ModBlocks.COOKING.get());
        this.craftingModel(ModBlocks.CHEMISTRY.get());
        //this.simpleBlock(ModBlocks.board.get());
        this.simpleBlock(ModBlocks.BOSS_SPAWNER.get(), this.models().getExistingFile(new ResourceLocation("block/" + Blocks.SPAWNER.getRegistryName().getPath())));
        this.simpleBlock(ModBlocks.SINGLE_SPAWN_BLOCK.get(), this.models().getExistingFile(new ResourceLocation("block/" + Blocks.SPAWNER.getRegistryName().getPath())));
        this.simpleBlock(ModBlocks.MONSTER_BARN.get(), this.models().getExistingFile(new ResourceLocation(RuneCraftory.MODID, "block/" + ModBlocks.MONSTER_BARN.getID().getPath())));

        this.getVariantBuilder(ModBlocks.SNOW.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(
                        this.models().getExistingFile(state.getValue(SnowLayerBlock.LAYERS) == 8 ? new ResourceLocation("block/snow_block") : new ResourceLocation("block/snow_height" + state.getValue(SnowLayerBlock.LAYERS) * 2)))
                .build());

        this.getVariantBuilder(ModBlocks.SHIPPING.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(
                        this.models().orientableVertical(ModBlocks.SHIPPING.getID().getPath(), this.modLoc("block/shipping_bin"), this.modLoc("block/shipping_bin_top")))
                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot()) % 360)
                .build());

        this.getVariantBuilder(ModBlocks.CASH_REGISTER.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(
                        this.models().getExistingFile(new ResourceLocation(RuneCraftory.MODID, "block/" + ModBlocks.CASH_REGISTER.getID().getPath())))
                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot()) % 360)
                .build());

        this.getVariantBuilder(ModBlocks.QUEST_BOARD.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(
                        this.models().getExistingFile(new ResourceLocation(RuneCraftory.MODID, "block/" + ModBlocks.QUEST_BOARD.getID().getPath() + "_" + state.getValue(BlockQuestboard.PART).getSerializedName())))
                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot()) % 360)
                .build());

        this.simpleBlock(ModBlocks.TREE_SOIL.get(), this.models().getExistingFile(new ResourceLocation("block/" + Blocks.DIRT.getRegistryName().getPath())));

        this.axisBlock(ModBlocks.APPLE_TREE.get(), this.models().getExistingFile(new ResourceLocation("block/" + Blocks.OAK_LOG.getRegistryName().getPath())),
                this.models().getExistingFile(new ResourceLocation("block/" + Blocks.OAK_LOG.getRegistryName().getPath() + "_horizontal")));
        this.simpleBlock(ModBlocks.APPLE_SAPLING.get(), this.models().cross(ModBlocks.APPLE_SAPLING.getID().toString(), this.itemTexture(ModItems.APPLE_SAPLING.get())));
        this.axisBlock(ModBlocks.APPLE_WOOD.get(), this.models().getExistingFile(new ResourceLocation("block/" + Blocks.OAK_LOG.getRegistryName().getPath())),
                this.models().getExistingFile(new ResourceLocation("block/" + Blocks.OAK_LOG.getRegistryName().getPath() + "_horizontal")));
        this.simpleBlock(ModBlocks.APPLE_LEAVES.get(), this.models().getExistingFile(new ResourceLocation("block/" + Blocks.OAK_LEAVES.getRegistryName().getPath())));
        this.getVariantBuilder(ModBlocks.APPLE.get())
                .partialState().with(BlockFruitTreeLeaf.HAS_FRUIT, true)
                .modelForState().modelFile(this.models().withExistingParent(ModBlocks.APPLE.getID().toString(), this.modLoc("fruit_leaves"))
                        .texture("base", this.blockTexture(Blocks.OAK_LEAVES))
                        .texture("particle", this.blockTexture(Blocks.OAK_LEAVES))
                        .texture("overlay", this.blockTexture(ModBlocks.APPLE.get())))
                .addModel()
                .partialState().with(BlockFruitTreeLeaf.HAS_FRUIT, false)
                .modelForState().modelFile(this.models().withExistingParent(ModBlocks.APPLE.getID().toString() + "_fruitless", "block/leaves")
                        .texture("all", this.blockTexture(Blocks.OAK_LEAVES)))
                .addModel();

        this.axisBlock(ModBlocks.ORANGE_TREE.get(), this.models().getExistingFile(new ResourceLocation("block/" + Blocks.OAK_LOG.getRegistryName().getPath())),
                this.models().getExistingFile(new ResourceLocation("block/" + Blocks.OAK_LOG.getRegistryName().getPath() + "_horizontal")));
        this.simpleBlock(ModBlocks.ORANGE_SAPLING.get(), this.models().cross(ModBlocks.ORANGE_SAPLING.getID().toString(), this.itemTexture(ModItems.ORANGE_SAPLING.get())));
        this.axisBlock(ModBlocks.ORANGE_WOOD.get(), this.models().getExistingFile(new ResourceLocation("block/" + Blocks.OAK_LOG.getRegistryName().getPath())),
                this.models().getExistingFile(new ResourceLocation("block/" + Blocks.OAK_LOG.getRegistryName().getPath() + "_horizontal")));
        this.simpleBlock(ModBlocks.ORANGE_LEAVES.get(), this.models().getExistingFile(new ResourceLocation("block/" + Blocks.OAK_LEAVES.getRegistryName().getPath())));
        this.getVariantBuilder(ModBlocks.ORANGE.get())
                .partialState().with(BlockFruitTreeLeaf.HAS_FRUIT, true)
                .modelForState().modelFile(this.models().withExistingParent(ModBlocks.ORANGE.getID().toString(), this.modLoc("fruit_leaves"))
                        .texture("base", this.blockTexture(Blocks.OAK_LEAVES))
                        .texture("particle", this.blockTexture(Blocks.OAK_LEAVES))
                        .texture("overlay", this.blockTexture(ModBlocks.ORANGE.get())))
                .addModel()
                .partialState().with(BlockFruitTreeLeaf.HAS_FRUIT, false)
                .modelForState().modelFile(this.models().withExistingParent(ModBlocks.ORANGE.getID().toString() + "_fruitless", "block/leaves")
                        .texture("all", this.blockTexture(Blocks.OAK_LEAVES)))
                .addModel();

        this.axisBlock(ModBlocks.GRAPE_TREE.get(), this.models().getExistingFile(new ResourceLocation("block/" + Blocks.SPRUCE_LOG.getRegistryName().getPath())),
                this.models().getExistingFile(new ResourceLocation("block/" + Blocks.SPRUCE_LOG.getRegistryName().getPath() + "_horizontal")));
        this.simpleBlock(ModBlocks.GRAPE_SAPLING.get(), this.models().cross(ModBlocks.GRAPE_SAPLING.getID().toString(), this.itemTexture(ModItems.GRAPE_SAPLING.get())));
        this.axisBlock(ModBlocks.GRAPE_WOOD.get(), this.models().getExistingFile(new ResourceLocation("block/" + Blocks.SPRUCE_LOG.getRegistryName().getPath())),
                this.models().getExistingFile(new ResourceLocation("block/" + Blocks.SPRUCE_LOG.getRegistryName().getPath() + "_horizontal")));
        this.simpleBlock(ModBlocks.GRAPE_LEAVES.get(), this.models().getExistingFile(new ResourceLocation("block/" + Blocks.SPRUCE_LEAVES.getRegistryName().getPath())));
        this.getVariantBuilder(ModBlocks.GRAPE.get())
                .partialState().with(BlockFruitTreeLeaf.HAS_FRUIT, true)
                .modelForState().modelFile(this.models().withExistingParent(ModBlocks.GRAPE.getID().toString(), this.modLoc("fruit_leaves"))
                        .texture("base", this.blockTexture(Blocks.SPRUCE_LEAVES))
                        .texture("particle", this.blockTexture(Blocks.SPRUCE_LEAVES))
                        .texture("overlay", this.blockTexture(ModBlocks.GRAPE.get())))
                .addModel()
                .partialState().with(BlockFruitTreeLeaf.HAS_FRUIT, false)
                .modelForState().modelFile(this.models().withExistingParent(ModBlocks.GRAPE.getID().toString() + "_fruitless", "block/leaves")
                        .texture("all", this.blockTexture(Blocks.SPRUCE_LEAVES)))
                .addModel();
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

    public ResourceLocation itemTexture(Item item) {
        ResourceLocation name = item.getRegistryName();
        return new ResourceLocation(name.getNamespace(), "item" + "/" + name.getPath());
    }

    public ResourceLocation itemCropTexture(Block block) {
        ResourceLocation name = block.getRegistryName();
        return new ResourceLocation(name.getNamespace(), "item" + "/" + name.getPath().replace("plant", "crop"));
    }
}