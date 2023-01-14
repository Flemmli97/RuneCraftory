package io.github.flemmli97.runecraftory.common.world.structure.processors;

import com.mojang.serialization.Codec;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.registry.ModStructures;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class POIProcessor extends DataStructureBlockProcessor {

    /**
     * Lower case looks better in json
     */
    public static final Codec<POIProcessor> CODEC = Codec.unit(POIProcessor::new);

    public POIProcessor() {
        super("RF4POI", true);
    }

    @Override
    protected StructureTemplate.StructureBlockInfo handleDataMarker(String data, StructureTemplate.StructureBlockInfo origin, LevelReader level, StructurePlaceSettings settings) {
        BlockState state = ModBlocks.cashRegister.get().defaultBlockState();
        return new StructureTemplate.StructureBlockInfo(origin.pos, state, null);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ModStructures.POI_PROCESSOR.get();
    }
}
