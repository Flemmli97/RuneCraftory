package io.github.flemmli97.runecraftory.common.world.structure.processors;

import com.mojang.serialization.Codec;
import io.github.flemmli97.runecraftory.common.blocks.BlockBossSpawner;
import io.github.flemmli97.runecraftory.common.blocks.tile.BossSpawnerBlockEntity;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.registry.ModStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
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

    public BossSpawnerProcessor(TagKey<EntityType<?>> boss) {
        super("BOSS", false);
        this.boss = ResourceLocation.parse(boss.location().toString());
    }

    @Override
    protected StructureTemplate.StructureBlockInfo handleDataMarker(String data, StructureTemplate.StructureBlockInfo origin, LevelReader level, StructurePlaceSettings settings) {
        String[] s = data.split("#");
        int off = -2;
        if (s.length != 1) {
            try {
                off = Integer.parseInt(s[1]);
            } catch (NumberFormatException ignored) {
            }
        }
        BlockPos pos = origin.pos().above(off);
        BlockState state = ModBlocks.BOSS_SPAWNER.get().defaultBlockState()
                .setValue(BlockBossSpawner.FACING, Direction.SOUTH)
                .mirror(settings.getMirror()).rotate(settings.getRotation());
        return new StructureTemplate.StructureBlockInfo(pos, state, BossSpawnerBlockEntity.creatTagFor(this.boss));
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ModStructures.BOSS_PROCESSOR.get();
    }
}
