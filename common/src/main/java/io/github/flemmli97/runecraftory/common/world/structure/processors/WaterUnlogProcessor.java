package io.github.flemmli97.runecraftory.common.world.structure.processors;

import com.mojang.serialization.Codec;
import io.github.flemmli97.runecraftory.common.registry.ModStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

public class WaterUnlogProcessor extends StructureProcessor {

    public static final WaterUnlogProcessor INST = new WaterUnlogProcessor();
    public static final Codec<WaterUnlogProcessor> CODEC = Codec.unit(INST);

    private WaterUnlogProcessor() {
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos blockPos, BlockPos pos, StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo, StructurePlaceSettings settings) {
        return relativeBlockInfo;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ModStructures.WATERUNLOG_PROCESSOR.get();
    }
}
