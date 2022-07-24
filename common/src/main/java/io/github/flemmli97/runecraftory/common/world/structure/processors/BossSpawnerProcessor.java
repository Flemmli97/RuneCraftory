package io.github.flemmli97.runecraftory.common.world.structure.processors;

import com.mojang.serialization.Codec;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.registry.ModStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
        if (relativeBlockInfo.state.is(Blocks.OAK_SIGN)) {
            if (relativeBlockInfo.nbt == null || !relativeBlockInfo.nbt.getString("Text1").equals("{\"text\":\"Boss\"}"))
                return relativeBlockInfo;
            Component txt2 = Component.Serializer.fromJson(relativeBlockInfo.nbt.getString("Text2"));
            int off = 0;
            try {
                if (txt2 != null) {
                    String s = txt2.getString();
                    if (!s.isEmpty())
                        off = Integer.parseInt(s);
                }
            } catch (NumberFormatException ignored) {
            }
            BlockState state = ModBlocks.bossSpawner.get().defaultBlockState();
            CompoundTag tag = new CompoundTag();
            tag.putString("Entity", this.boss.toString());
            tag.putInt("LastUpdate", -1);
            return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos.above(off), state, tag);
        }
        return relativeBlockInfo;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ModStructures.BOSS_PROCESSOR.get();
    }

    private boolean isNormalPigSpawner(CompoundTag tag) {
        if (tag == null)
            return false;
        return tag.getCompound("SpawnData").getCompound("entity").getString("id").equals("minecraft:pig");
    }
}
