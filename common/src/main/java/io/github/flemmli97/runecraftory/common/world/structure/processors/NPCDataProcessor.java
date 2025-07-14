package io.github.flemmli97.runecraftory.common.world.structure.processors;

import com.mojang.serialization.MapCodec;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryBlocks;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryStructures;
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
    public static final MapCodec<NPCDataProcessor> CODEC = ResourceLocation.CODEC.fieldOf("profession")
            .xmap(NPCDataProcessor::new, d -> d.profession);

    protected final ResourceLocation profession;

    public NPCDataProcessor(ResourceLocation profession) {
        super("NPC", true);
        this.profession = profession;
    }

    @Override
    protected StructureTemplate.StructureBlockInfo handleDataMarker(String data, StructureTemplate.StructureBlockInfo origin, LevelReader level, StructurePlaceSettings settings) {
        CompoundTag entityTag = new CompoundTag();
        ListTag listTag = new ListTag();
        listTag.add(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1200, 5, true, false).save());
        entityTag.put("ActiveEffects", listTag);
        BlockState state = RuneCraftoryBlocks.SINGLE_SPAWN_BLOCK.get().defaultBlockState();
        CompoundTag tag = new CompoundTag();
        tag.putString("Entity", RuneCraftoryEntities.NPC.getID().toString());
        tag.put("EntityNBT", entityTag);
        tag.putString("NPCProfession", this.profession.toString());
        return new StructureTemplate.StructureBlockInfo(origin.pos(), state, tag);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return RuneCraftoryStructures.NPC_PROCESSOR.get();
    }
}
