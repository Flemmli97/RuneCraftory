package io.github.flemmli97.runecraftory.common.world.structure.processors;

import com.mojang.serialization.Codec;
import io.github.flemmli97.runecraftory.common.blocks.BlockBossSpawner;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.registry.ModStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.ArrayList;
import java.util.List;

public class BossSpawnerProcessor extends DataStructureBlockProcessor {

    public static final Codec<BossSpawnerProcessor> CODEC = Codec.STRING.fieldOf("boss").xmap(BossSpawnerProcessor::new, d -> d.tag != null ? "#" + d.tag.location() : d.boss.toString()).codec();

    protected final ResourceLocation boss;
    protected final TagKey<EntityType<?>> tag;

    public BossSpawnerProcessor(String boss) {
        super("BOSS", false);
        if (boss.startsWith("#")) {
            this.boss = new ResourceLocation(boss.substring(1));
            this.tag = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, this.boss);
        } else {
            this.boss = new ResourceLocation(boss);
            this.tag = null;
        }
    }

    public BossSpawnerProcessor(ResourceLocation boss) {
        this(boss.toString());
    }

    public BossSpawnerProcessor(TagKey<EntityType<?>> boss) {
        super("BOSS", false);
        this.boss = new ResourceLocation(boss.location().toString());
        this.tag = boss;
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
        ResourceLocation entity = this.boss;
        BlockPos pos = origin.pos.above(off);
        if (this.tag != null) {
            List<ResourceLocation> types = new ArrayList<>();
            Registry.ENTITY_TYPE.getTagOrEmpty(this.tag).forEach(h -> types.add(Registry.ENTITY_TYPE.getKey(h.value())));
            if (!types.isEmpty())
                entity = types.get(settings.getRandom(pos).nextInt(types.size()));
        }
        BlockState state = ModBlocks.BOSS_SPAWNER.get().defaultBlockState()
                .setValue(BlockBossSpawner.FACING, settings.getRotation().rotate(Direction.NORTH));
        CompoundTag tag = new CompoundTag();
        tag.putString("Entity", entity.toString());
        tag.putInt("LastUpdate", -1);
        return new StructureTemplate.StructureBlockInfo(pos, state, tag);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ModStructures.BOSS_PROCESSOR.get();
    }
}
