package io.github.flemmli97.runecraftory.common.world.structure.processors;

import com.mojang.serialization.Codec;
import io.github.flemmli97.runecraftory.common.entities.npc.EnumShop;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModStructures;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Locale;

public class NPCDataProcessor extends DataStructureBlockProcessor {

    /**
     * Lower case looks better in json
     */
    public static final Codec<NPCDataProcessor> CODEC = Codec.STRING.fieldOf("shop_type")
            .xmap(s -> EnumShop.valueOf(s.toUpperCase(Locale.ROOT)), e -> e.name().toLowerCase(Locale.ROOT))
            .xmap(NPCDataProcessor::new, d -> d.shopType).codec();

    protected final EnumShop shopType;

    public NPCDataProcessor(EnumShop shopType) {
        super("NPC", true);
        this.shopType = shopType;
    }

    @Override
    protected StructureTemplate.StructureBlockInfo handleDataMarker(String data, StructureTemplate.StructureBlockInfo origin, LevelReader level, StructurePlaceSettings settings) {
        CompoundTag entityTag = new CompoundTag();
        entityTag.putInt("Shop", this.shopType.ordinal());
        BlockState state = ModBlocks.singleSpawnBlock.get().defaultBlockState();
        CompoundTag tag = new CompoundTag();
        tag.putString("Entity", ModEntities.npc.getID().toString());
        tag.put("EntityNBT", entityTag);
        return new StructureTemplate.StructureBlockInfo(origin.pos, state, tag);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ModStructures.NPC_PROCESSOR.get();
    }
}
