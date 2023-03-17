package io.github.flemmli97.runecraftory.common.world.structure.processors;

import com.mojang.serialization.Codec;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModStructures;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class NPCDataProcessor extends DataStructureBlockProcessor {

    /**
     * Lower case looks better in json
     */
    public static final Codec<NPCDataProcessor> CODEC = ResourceLocation.CODEC.fieldOf("shop_type")
            .xmap(NPCDataProcessor::new, d -> d.jobID).codec();

    protected final ResourceLocation jobID;

    public NPCDataProcessor(ResourceLocation jobID) {
        super("NPC", true);
        this.jobID = jobID;
    }

    @Override
    protected StructureTemplate.StructureBlockInfo handleDataMarker(String data, StructureTemplate.StructureBlockInfo origin, LevelReader level, StructurePlaceSettings settings) {
        CompoundTag entityTag = new CompoundTag();
        entityTag.putString("Shop", this.jobID.toString());
        ListTag listTag = new ListTag();
        listTag.add(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1200, 5, true, false).save(new CompoundTag()));
        entityTag.put("ActiveEffects", listTag);
        BlockState state = ModBlocks.singleSpawnBlock.get().defaultBlockState();
        CompoundTag tag = new CompoundTag();
        tag.putString("Entity", ModEntities.NPC.getID().toString());
        tag.put("EntityNBT", entityTag);
        return new StructureTemplate.StructureBlockInfo(origin.pos, state, tag);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ModStructures.NPC_PROCESSOR.get();
    }
}
