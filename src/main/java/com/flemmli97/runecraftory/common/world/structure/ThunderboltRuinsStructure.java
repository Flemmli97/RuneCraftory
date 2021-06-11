package com.flemmli97.runecraftory.common.world.structure;

import com.flemmli97.runecraftory.common.world.structure.piece.ThunderboltRuinsPiece;
import com.mojang.serialization.Codec;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class ThunderboltRuinsStructure extends Structure<NoFeatureConfig> {

    public ThunderboltRuinsStructure(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return ThunderboltRuinsStructure.Start::new;
    }

    @Override
    public GenerationStage.Decoration getDecorationStage() {
        return GenerationStage.Decoration.TOP_LAYER_MODIFICATION;
    }

    public static class Start extends StructureStart<NoFeatureConfig> {

        public Start(Structure<NoFeatureConfig> struc, int chunkX, int chunkZ, MutableBoundingBox mbb, int ref, long seed) {
            super(struc, chunkX, chunkZ, mbb, ref, seed);
        }

        @Override
        public void func_230364_a_(DynamicRegistries reg, ChunkGenerator gen, TemplateManager templateManager, int chunkX, int chunkZ, Biome biome, NoFeatureConfig conf) {
            int x = chunkX * 16 + this.rand.nextInt(16);
            int z = chunkZ * 16 + this.rand.nextInt(16);
            int y = gen.getNoiseHeightMinusOne(x, z, Heightmap.Type.WORLD_SURFACE_WG);
            BlockPos blockpos = new BlockPos(x, y, z);
            Rotation rotation = Rotation.randomRotation(this.rand);
            ThunderboltRuinsPiece.add(templateManager, blockpos, rotation, this.components, this.rand);
            this.recalculateStructureSize();
        }
    }
}
