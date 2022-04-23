package io.github.flemmli97.runecraftory.common.world.structure;

import com.mojang.serialization.Codec;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.registry.ModStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

public class BossSpawnerProcessor extends StructureProcessor {

    public static final Codec<BossSpawnerProcessor> CODEC = ResourceLocation.CODEC.fieldOf("boss").xmap(BossSpawnerProcessor::new, d -> d.boss).codec();

    protected final ResourceLocation boss;

    public BossSpawnerProcessor(ResourceLocation boss) {
        this.boss = boss;
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos blockPos, BlockPos pos, StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo, StructurePlaceSettings settings) {
        if (relativeBlockInfo.state.is(Blocks.SPAWNER) && this.isNormalPigSpawner(relativeBlockInfo.nbt)) {
            BlockState state = ModBlocks.bossSpawner.get().defaultBlockState();
            CompoundTag tag = new CompoundTag();
            tag.putString("Entity", this.boss.toString());
            tag.putInt("LastUpdate", -1);
            return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos, state, tag);
        }
        return relativeBlockInfo;
    }

    private boolean isNormalPigSpawner(CompoundTag tag) {
        if (tag == null)
            return false;
        return tag.getCompound("SpawnData").getCompound("entity").getString("id").equals("minecraft:pig");
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ModStructures.BOSS_PROCESSOR.get();
    }
}
