package io.github.flemmli97.runecraftory.common.world.structure.processors;

import com.mojang.serialization.Codec;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.registry.ModStructures;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class BossSpawnerProcessor extends DataStructureBlockProcessor {

    public static final Codec<BossSpawnerProcessor> CODEC = ResourceLocation.CODEC.fieldOf("boss").xmap(BossSpawnerProcessor::new, d -> d.boss).codec();

    protected final ResourceLocation boss;

    public BossSpawnerProcessor(ResourceLocation boss) {
        super("BOSS", false);
        this.boss = boss;
    }

    @Override
    protected StructureTemplate.StructureBlockInfo handleDataMarker(String data, StructureTemplate.StructureBlockInfo origin, LevelReader level, StructurePlaceSettings settings) {
        String[] s = data.split("#");
        int off = 0;
        if (s.length != 1) {
            try {
                off = Integer.parseInt(s[1]);

            } catch (NumberFormatException ignored) {
            }
        }
        BlockState state = ModBlocks.bossSpawner.get().defaultBlockState();
        CompoundTag tag = new CompoundTag();
        tag.putString("Entity", this.boss.toString());
        tag.putInt("LastUpdate", -1);
        return new StructureTemplate.StructureBlockInfo(origin.pos.above(off), state, tag);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ModStructures.BOSS_PROCESSOR.get();
    }
}
