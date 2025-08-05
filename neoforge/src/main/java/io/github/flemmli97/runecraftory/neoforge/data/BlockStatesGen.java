package io.github.flemmli97.runecraftory.neoforge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.blocks.CraftingBlock;
import io.github.flemmli97.runecraftory.common.blocks.ExtendedCropBlock;
import io.github.flemmli97.runecraftory.common.blocks.FruitTreeLeafBlock;
import io.github.flemmli97.runecraftory.common.blocks.GiantCropBlock;
import io.github.flemmli97.runecraftory.common.blocks.HerbBlock;
import io.github.flemmli97.runecraftory.common.blocks.QuestboardBlock;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryBlocks;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BlockStatesGen extends BlockStateProvider {

    private static final ResourceLocation CROP_TINTED = RuneCraftory.modRes(ModelProvider.BLOCK_FOLDER + "/crop_tinted");
    private static final ResourceLocation CROP_BIG = RuneCraftory.modRes(ModelProvider.BLOCK_FOLDER + "/big_crop");
    private static final ResourceLocation CROP_GIANT_1 = RuneCraftory.modRes(ModelProvider.BLOCK_FOLDER + "/giant_crop_1");
    private static final ResourceLocation CROP_GIANT_2 = RuneCraftory.modRes(ModelProvider.BLOCK_FOLDER + "/giant_crop_2");
    private static final ResourceLocation FLOWER_BIG = RuneCraftory.modRes(ModelProvider.BLOCK_FOLDER + "/big_flower");
    private static final ResourceLocation FLOWER_GIANT = RuneCraftory.modRes(ModelProvider.BLOCK_FOLDER + "/giant_flower");
    private static final ResourceLocation CROSS_TINTED = RuneCraftory.modRes(ModelProvider.BLOCK_FOLDER + "/cross_tinted");

    public BlockStatesGen(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, RuneCraftory.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        RuneCraftoryBlocks.HERBS.forEach(reg -> {
            Block block = reg.get();
            if (block == RuneCraftoryBlocks.MEDICINAL_HERB.get())
                this.simpleBlock(block, this.models().withExistingParent(reg.getID().toString(), RuneCraftory.modRes("item/medicinal_herb")));
            else if (block instanceof HerbBlock) {
                ResourceLocation texture = this.itemTexture(block);
                this.simpleBlock(block, this.models().cross(reg.getID().toString(), texture)
                        .texture("particle", texture));
            }
        });
        for (RegistryEntrySupplier<Block, ?> reg : RuneCraftoryBlocks.FLOWERS) {
            Block block = reg.get();
            if (reg == RuneCraftoryBlocks.SWORD_CROP || reg == RuneCraftoryBlocks.SHIELD_CROP) {
                this.getVariantBuilder(block).forAllStatesExcept(state -> {
                    int stage = state.getValue(ExtendedCropBlock.AGE);
                    ResourceLocation text;
                    if (stage == 3 || stage == 4) {
                        text = this.blockTexture(RuneCraftory.MODID, reg.getID().getPath());
                    } else {
                        text = this.blockTexture(RuneCraftory.MODID, "plant_gear_" + stage);
                    }
                    return ConfiguredModel.builder().modelFile(this.models().singleTexture(text.toString(), CROSS_TINTED, "cross", text)).build();
                }, ExtendedCropBlock.WILTED);
                continue;
            }
            if (block instanceof GiantCropBlock giant)
                this.getVariantBuilder(block).forAllStatesExcept(state -> {
                    ResourceLocation texture = this.itemTexture(giant.getCrop(BuiltInRegistries.ITEM.asLookup()));
                    return ConfiguredModel.builder().modelFile(this.models().singleTexture(reg.getID().toString(), FLOWER_GIANT, "0", texture))
                            .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360).build();
                }, ExtendedCropBlock.WILTED, GiantCropBlock.AGE);
            else if (block instanceof ExtendedCropBlock)
                this.getVariantBuilder(block).forAllStatesExcept(state -> {
                    int stage = state.getValue(ExtendedCropBlock.AGE);
                    boolean defaultFlowerState = stage == 0 && reg != RuneCraftoryBlocks.EMERY_FLOWER && reg != RuneCraftoryBlocks.IRONLEAF
                            && reg != RuneCraftoryBlocks.NOEL_GRASS && reg != RuneCraftoryBlocks.LAMP_GRASS;
                    String name = defaultFlowerState ? "runecraftory:flower_stage_0" : reg.getID().toString() + "_" + stage;
                    ResourceLocation texture = defaultFlowerState ? this.blockTexture(RuneCraftory.MODID, "flower_stage_0")
                            : stage == 3 ? this.itemCropTexture(block) : this.blockTexture(RuneCraftory.MODID, reg.getID().getPath() + "_" + stage);
                    ResourceLocation parent = CROSS_TINTED;
                    if (stage == 4) {
                        parent = FLOWER_BIG;
                        RegistryEntrySupplier<Block, ?> giant = RuneCraftoryBlocks.GIANT_CROP_MAP.get(reg);
                        if (giant != null && giant.get() instanceof GiantCropBlock giantCrop)
                            texture = this.itemTexture(giantCrop.getCrop(BuiltInRegistries.ITEM.asLookup()));
                    }
                    return ConfiguredModel.builder().modelFile(this.models().singleTexture(name, parent, "cross", texture)).build();
                }, ExtendedCropBlock.WILTED);
        }
        RuneCraftoryBlocks.CROPS.forEach(reg -> {
            Block block = reg.get();
            if (block instanceof GiantCropBlock)
                this.getVariantBuilder(block).forAllStatesExcept(state -> {
                    ResourceLocation texture = this.blockTexture(RuneCraftory.MODID, reg.getID().getPath());
                    ResourceLocation parent = CROP_GIANT_1;
                    int rot = 0;
                    String file = reg.getID().toString();
                    switch (state.getValue(GiantCropBlock.DIRECTION)) {
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
                }, ExtendedCropBlock.WILTED, GiantCropBlock.AGE);
            else if (block instanceof ExtendedCropBlock)
                this.getVariantBuilder(block).forAllStatesExcept(state -> {
                    int stage = state.getValue(ExtendedCropBlock.AGE);
                    String name = reg.getID().toString() + "_" + stage;
                    ResourceLocation texture = this.blockTexture(RuneCraftory.MODID, reg.getID().getPath() + "_" + stage);
                    ResourceLocation parent = CROP_TINTED;
                    if (stage == 4) {
                        parent = CROP_BIG;
                        RegistryEntrySupplier<Block, ?> giant = RuneCraftoryBlocks.GIANT_CROP_MAP.get(reg);
                        if (giant != null)
                            texture = this.blockTexture(giant.get());
                    }
                    return ConfiguredModel.builder().modelFile(this.models().singleTexture(name, parent, "crop", texture)).build();
                }, ExtendedCropBlock.WILTED);
        });
        RuneCraftoryBlocks.MINERAL_MAP.values().forEach(reg -> {
            Block block = reg.get();
            this.getVariantBuilder(block)
                    .forAllStatesExcept(state -> {
                                BlockModelBuilder file = this.models().withExistingParent(reg.getID().toString(), "runecraftory:block/ore").texture("ore", this.blockTexture(block));
                                if (block == RuneCraftoryBlocks.MINERAL_DRAGONIC.get())
                                    file = file.texture("0", ResourceLocation.withDefaultNamespace(ModelProvider.BLOCK_FOLDER + "/end_stone"));
                                return ConfiguredModel.builder()
                                        .modelFile(file)
                                        .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot()) % 360)
                                        .build();
                            },
                            BlockStateProperties.WATERLOGGED
                    );
        });
        RuneCraftoryBlocks.BROKEN_MINERAL_MAP.values().forEach(reg -> {
            Block block = reg.get();
            this.getVariantBuilder(block)
                    .forAllStatesExcept(state -> {
                                BlockModelBuilder file = this.models().withExistingParent(reg.getID().toString(), "runecraftory:block/ore_broken").texture("ore", this.mineralTexture(reg));
                                if (block == RuneCraftoryBlocks.BROKEN_MINERAL_DRAGONIC.get())
                                    file = file.texture("0", ResourceLocation.withDefaultNamespace(ModelProvider.BLOCK_FOLDER + "/end_stone"));
                                return ConfiguredModel.builder()
                                        .modelFile(file)
                                        .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot()) % 360)
                                        .build();
                            },
                            BlockStateProperties.WATERLOGGED
                    );
        });
        this.craftingModel(RuneCraftoryBlocks.FORGE);
        this.craftingModel(RuneCraftoryBlocks.ACCESSORY_WORKBENCH);
        this.craftingModel(RuneCraftoryBlocks.COOKING_TABLE);
        this.craftingModel(RuneCraftoryBlocks.CHEMISTRY_SET);
        this.simpleBlock(RuneCraftoryBlocks.BOSS_SPAWNER.get(), this.models().getExistingFile(ResourceLocation.withDefaultNamespace(ModelProvider.BLOCK_FOLDER + "/" + this.key(Blocks.SPAWNER).getPath())));
        this.simpleBlock(RuneCraftoryBlocks.SINGLE_SPAWN_BLOCK.get(), this.models().getExistingFile(ResourceLocation.withDefaultNamespace(ModelProvider.BLOCK_FOLDER + "/" + this.key(Blocks.SPAWNER).getPath())));
        this.simpleBlock(RuneCraftoryBlocks.MONSTER_BARN.get(), this.models().getExistingFile(RuneCraftory.modRes(ModelProvider.BLOCK_FOLDER + "/" + RuneCraftoryBlocks.MONSTER_BARN.getID().getPath())));
        this.simpleBlock(RuneCraftoryBlocks.HOT_SPRING_WATER.get(),
                this.models().getExistingFile(this.blockTexture(Blocks.WATER)));

        this.getVariantBuilder(RuneCraftoryBlocks.SHIPPING.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(
                        this.models().orientableVertical(RuneCraftoryBlocks.SHIPPING.getID().getPath(), this.modLoc(ModelProvider.BLOCK_FOLDER + "/shipping_bin"), this.modLoc(ModelProvider.BLOCK_FOLDER + "/shipping_bin_top")))
                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot()) % 360)
                .build());

        this.getVariantBuilder(RuneCraftoryBlocks.CASH_REGISTER.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(
                        this.models().getExistingFile(RuneCraftory.modRes(ModelProvider.BLOCK_FOLDER + "/" + RuneCraftoryBlocks.CASH_REGISTER.getID().getPath())))
                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot()) % 360)
                .build());

        this.getVariantBuilder(RuneCraftoryBlocks.QUEST_BOARD.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(
                        this.models().getExistingFile(RuneCraftory.modRes(ModelProvider.BLOCK_FOLDER + "/" + RuneCraftoryBlocks.QUEST_BOARD.getID().getPath() + "_" + state.getValue(QuestboardBlock.PART).getSerializedName())))
                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot()) % 360)
                .build());

        this.simpleBlock(RuneCraftoryBlocks.TREE_SOIL.get(), this.models().getExistingFile(ResourceLocation.withDefaultNamespace(ModelProvider.BLOCK_FOLDER + "/" + this.key(Blocks.DIRT).getPath())));

        this.axisBlock(RuneCraftoryBlocks.APPLE_TREE.get(), this.models().getExistingFile(ResourceLocation.withDefaultNamespace(ModelProvider.BLOCK_FOLDER + "/" + this.key(Blocks.OAK_LOG).getPath())),
                this.models().getExistingFile(ResourceLocation.withDefaultNamespace(ModelProvider.BLOCK_FOLDER + "/" + this.key(Blocks.OAK_LOG).getPath() + "_horizontal")));
        this.simpleBlock(RuneCraftoryBlocks.APPLE_SAPLING.get(), this.models().cross(RuneCraftoryBlocks.APPLE_SAPLING.getID().toString(), this.itemTexture(RuneCraftoryItems.APPLE_SAPLING.get())));
        this.axisBlock(RuneCraftoryBlocks.APPLE_WOOD.get(), this.models().getExistingFile(ResourceLocation.withDefaultNamespace(ModelProvider.BLOCK_FOLDER + "/" + this.key(Blocks.OAK_LOG).getPath())),
                this.models().getExistingFile(ResourceLocation.withDefaultNamespace(ModelProvider.BLOCK_FOLDER + "/" + this.key(Blocks.OAK_LOG).getPath() + "_horizontal")));
        this.simpleBlock(RuneCraftoryBlocks.APPLE_LEAVES.get(), this.models().getExistingFile(ResourceLocation.withDefaultNamespace(ModelProvider.BLOCK_FOLDER + "/" + this.key(Blocks.OAK_LEAVES).getPath())));
        this.getVariantBuilder(RuneCraftoryBlocks.APPLE.get())
                .partialState().with(FruitTreeLeafBlock.HAS_FRUIT, true)
                .modelForState().modelFile(this.models().withExistingParent(RuneCraftoryBlocks.APPLE.getID().toString(), this.modLoc("fruit_leaves"))
                        .texture("base", this.blockTexture(Blocks.OAK_LEAVES))
                        .texture("particle", this.blockTexture(Blocks.OAK_LEAVES))
                        .texture("overlay", this.blockTexture(RuneCraftoryBlocks.APPLE.get())))
                .addModel()
                .partialState().with(FruitTreeLeafBlock.HAS_FRUIT, false)
                .modelForState().modelFile(this.models().withExistingParent(RuneCraftoryBlocks.APPLE.getID().toString() + "_fruitless", ModelProvider.BLOCK_FOLDER + "/leaves")
                        .texture("all", this.blockTexture(Blocks.OAK_LEAVES)))
                .addModel();

        this.axisBlock(RuneCraftoryBlocks.ORANGE_TREE.get(), this.models().getExistingFile(ResourceLocation.withDefaultNamespace(ModelProvider.BLOCK_FOLDER + "/" + this.key(Blocks.BIRCH_LOG).getPath())),
                this.models().getExistingFile(ResourceLocation.withDefaultNamespace(ModelProvider.BLOCK_FOLDER + "/" + this.key(Blocks.BIRCH_LOG).getPath() + "_horizontal")));
        this.simpleBlock(RuneCraftoryBlocks.ORANGE_SAPLING.get(), this.models().cross(RuneCraftoryBlocks.ORANGE_SAPLING.getID().toString(), this.itemTexture(RuneCraftoryItems.ORANGE_SAPLING.get())));
        this.axisBlock(RuneCraftoryBlocks.ORANGE_WOOD.get(), this.models().getExistingFile(ResourceLocation.withDefaultNamespace(ModelProvider.BLOCK_FOLDER + "/" + this.key(Blocks.BIRCH_LOG).getPath())),
                this.models().getExistingFile(ResourceLocation.withDefaultNamespace(ModelProvider.BLOCK_FOLDER + "/" + this.key(Blocks.BIRCH_LOG).getPath() + "_horizontal")));
        this.simpleBlock(RuneCraftoryBlocks.ORANGE_LEAVES.get(), this.models().getExistingFile(ResourceLocation.withDefaultNamespace(ModelProvider.BLOCK_FOLDER + "/" + this.key(Blocks.BIRCH_LEAVES).getPath())));
        this.getVariantBuilder(RuneCraftoryBlocks.ORANGE.get())
                .partialState().with(FruitTreeLeafBlock.HAS_FRUIT, true)
                .modelForState().modelFile(this.models().withExistingParent(RuneCraftoryBlocks.ORANGE.getID().toString(), this.modLoc("fruit_leaves"))
                        .texture("base", this.blockTexture(Blocks.BIRCH_LEAVES))
                        .texture("particle", this.blockTexture(Blocks.BIRCH_LEAVES))
                        .texture("overlay", this.blockTexture(RuneCraftoryBlocks.ORANGE.get())))
                .addModel()
                .partialState().with(FruitTreeLeafBlock.HAS_FRUIT, false)
                .modelForState().modelFile(this.models().withExistingParent(RuneCraftoryBlocks.ORANGE.getID().toString() + "_fruitless", ModelProvider.BLOCK_FOLDER + "/leaves")
                        .texture("all", this.blockTexture(Blocks.BIRCH_LEAVES)))
                .addModel();

        this.axisBlock(RuneCraftoryBlocks.GRAPE_TREE.get(), this.models().getExistingFile(ResourceLocation.withDefaultNamespace(ModelProvider.BLOCK_FOLDER + "/" + this.key(Blocks.SPRUCE_LOG).getPath())),
                this.models().getExistingFile(ResourceLocation.withDefaultNamespace(ModelProvider.BLOCK_FOLDER + "/" + this.key(Blocks.SPRUCE_LOG).getPath() + "_horizontal")));
        this.simpleBlock(RuneCraftoryBlocks.GRAPE_SAPLING.get(), this.models().cross(RuneCraftoryBlocks.GRAPE_SAPLING.getID().toString(), this.itemTexture(RuneCraftoryItems.GRAPE_SAPLING.get())));
        this.axisBlock(RuneCraftoryBlocks.GRAPE_WOOD.get(), this.models().getExistingFile(ResourceLocation.withDefaultNamespace(ModelProvider.BLOCK_FOLDER + "/" + this.key(Blocks.SPRUCE_LOG).getPath())),
                this.models().getExistingFile(ResourceLocation.withDefaultNamespace(ModelProvider.BLOCK_FOLDER + "/" + this.key(Blocks.SPRUCE_LOG).getPath() + "_horizontal")));
        this.simpleBlock(RuneCraftoryBlocks.GRAPE_LEAVES.get(), this.models().getExistingFile(ResourceLocation.withDefaultNamespace(ModelProvider.BLOCK_FOLDER + "/" + this.key(Blocks.DARK_OAK_LEAVES).getPath())));
        this.getVariantBuilder(RuneCraftoryBlocks.GRAPE.get())
                .partialState().with(FruitTreeLeafBlock.HAS_FRUIT, true)
                .modelForState().modelFile(this.models().withExistingParent(RuneCraftoryBlocks.GRAPE.getID().toString(), this.modLoc("fruit_leaves"))
                        .texture("base", this.blockTexture(Blocks.DARK_OAK_LEAVES))
                        .texture("particle", this.blockTexture(Blocks.DARK_OAK_LEAVES))
                        .texture("overlay", this.blockTexture(RuneCraftoryBlocks.GRAPE.get())))
                .addModel()
                .partialState().with(FruitTreeLeafBlock.HAS_FRUIT, false)
                .modelForState().modelFile(this.models().withExistingParent(RuneCraftoryBlocks.GRAPE.getID().toString() + "_fruitless", ModelProvider.BLOCK_FOLDER + "/leaves")
                        .texture("all", this.blockTexture(Blocks.DARK_OAK_LEAVES)))
                .addModel();
    }

    public ResourceLocation mineralTexture(RegistryEntrySupplier<Block, ?> block) {
        ResourceLocation name = block.getID();
        return ResourceLocation.fromNamespaceAndPath(name.getNamespace(), "block" + "/" + name.getPath().replace("broken_", ""));
    }

    private void craftingModel(RegistryEntrySupplier<Block, ?> block) {
        this.getVariantBuilder(block.get())
                .forAllStatesExcept(state -> ConfiguredModel.builder()
                                .modelFile(this.models().getExistingFile(ResourceLocation.fromNamespaceAndPath(block.getID().getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + block.getID().getPath() + "_" + state.getValue(CraftingBlock.PART).getSerializedName())))
                                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                                .build()
                        //, BlockStateProperties.WATERLOGGED
                );
    }

    public ResourceLocation blockTexture(String nameSpace, String block) {
        return ResourceLocation.fromNamespaceAndPath(nameSpace, "block" + "/" + block);
    }

    public ResourceLocation itemTexture(Block block) {
        ResourceLocation name = this.key(block);
        return ResourceLocation.fromNamespaceAndPath(name.getNamespace(), "item" + "/" + name.getPath());
    }

    public ResourceLocation itemTexture(Item item) {
        ResourceLocation name = BuiltInRegistries.ITEM.getKey(item);
        return ResourceLocation.fromNamespaceAndPath(name.getNamespace(), "item" + "/" + name.getPath());
    }

    public ResourceLocation itemCropTexture(Block block) {
        ResourceLocation name = this.key(block);
        return ResourceLocation.fromNamespaceAndPath(name.getNamespace(), "item" + "/" + name.getPath().replace("plant", "crop"));
    }

    private ResourceLocation key(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }
}