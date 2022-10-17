package io.github.flemmli97.runecraftory.common.world.structure.processors;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

public abstract class DataStructureBlockProcessor extends StructureProcessor {

    protected final String data;
    protected final boolean exactMatch;

    public DataStructureBlockProcessor(String data, boolean exactMatch) {
        this.data = data;
        this.exactMatch = exactMatch;
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos blockPos, BlockPos pos, StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo, StructurePlaceSettings settings) {
        if (!relativeBlockInfo.state.is(Blocks.STRUCTURE_BLOCK) || relativeBlockInfo.nbt == null)
            return relativeBlockInfo;
        if (StructureMode.valueOf(relativeBlockInfo.nbt.getString("mode")) == StructureMode.DATA) {
            String data = relativeBlockInfo.nbt.getString("metadata");
            boolean match = this.exactMatch ? data.equals(this.data) : data.startsWith(this.data);
            if (match) {
                return this.handleDataMarker(data, relativeBlockInfo, level, settings);
            }
        }
        return relativeBlockInfo;
    }

    protected StructureTemplate.StructureBlockInfo handleDataMarker(String data, StructureTemplate.StructureBlockInfo origin, LevelReader level, StructurePlaceSettings settings) {
        return origin;
    }
}
