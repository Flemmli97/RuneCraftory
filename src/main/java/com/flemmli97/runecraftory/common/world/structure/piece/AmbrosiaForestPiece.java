package com.flemmli97.runecraftory.common.world.structure.piece;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.blocks.tile.TileSpawner;
import com.flemmli97.runecraftory.common.lib.LibEntities;
import com.flemmli97.runecraftory.common.registry.ModBlocks;
import com.flemmli97.runecraftory.common.registry.ModStructures;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Blocks;
import net.minecraft.command.arguments.BlockStateParser;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;
import java.util.Random;

public class AmbrosiaForestPiece {

    private static final ResourceLocation piece = new ResourceLocation(RuneCraftory.MODID, "bosses/ambrosia_forest");

    public static void add(TemplateManager templateManager, BlockPos pos, Rotation rot, List<StructurePiece> pieces, Random rand) {
        pieces.add(new AmbrosiaForestPiece.Piece(templateManager, pos, rot, -3));
    }

    public static class Piece extends TemplateStructurePiece {

        public Piece(TemplateManager templateManager, BlockPos pos, Rotation rot, int yoffSet) {
            super(ModStructures.AMBROSIA_PIECE, 0);
            this.templatePosition = pos.add(0, yoffSet, 0);
            this.setup(templateManager, rot);
        }

        public Piece(TemplateManager templateManager, CompoundNBT nbt) {
            super(ModStructures.AMBROSIA_PIECE, nbt);
            this.setup(templateManager, Rotation.valueOf(nbt.getString("Rot")));
        }

        private void setup(TemplateManager templateManager, Rotation rot) {
            Template template = templateManager.getTemplateDefaulted(piece);
            PlacementSettings placementsettings = new PlacementSettings().setRotation(rot).setMirror(Mirror.NONE).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
            //this.templatePosition = this.templatePosition.add(-template.getSize().getX()/2, 0, -template.getSize().getZ()/2);
            this.setup(template, this.templatePosition, placementsettings);
        }

        @Override
        protected void readAdditional(CompoundNBT nbt) {
            super.readAdditional(nbt);
            nbt.putString("Rot", this.placeSettings.getRotation().name());
        }

        @Override
        public boolean func_230383_a_(ISeedReader reader, StructureManager manager, ChunkGenerator generator, Random random, MutableBoundingBox mbb, ChunkPos chunk, BlockPos pos) {
            boolean flag = super.func_230383_a_(reader, manager, generator, random, mbb, chunk, pos);
            for (int x = 0; x < this.boundingBox.getXSize(); x++)
                for (int z = 0; z < this.boundingBox.getZSize(); z++)
                    this.replaceAirAndLiquidDownwards(reader, Blocks.DIRT.getDefaultState(), x, -1, z, mbb);
            return flag;
        }

        @Override
        protected void handleDataMarker(String id, BlockPos pos, IServerWorld world, Random random, MutableBoundingBox mbb) {
            if (id.startsWith(RuneCraftory.MODID + "_replace_non_air")) {
                String block = id.replace(RuneCraftory.MODID + "_replace_non_air-", "");
                if (block.isEmpty() || world.getBlockState(pos).matchesBlock(Blocks.AIR))
                    return;
                try {
                    BlockStateParser parser = new BlockStateParser(new StringReader(block), false);
                    parser.parse(false);
                    world.setBlockState(pos, parser.getState(), 3);
                } catch (CommandSyntaxException e) {
                    //Skip
                }
            }
            if (id.equals("Boss")) {
                world.setBlockState(pos, ModBlocks.bossSpawner.get().getDefaultState(), 3);
                TileEntity tile = world.getTileEntity(pos);
                if (tile instanceof TileSpawner) {
                    ((TileSpawner) tile).setEntity(LibEntities.ambrosia);
                }
            }
        }
    }
}
