package io.github.flemmli97.runecraftory.neoforge.data.worldgen.structures;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.npc.job.NPCJob;
import io.github.flemmli97.runecraftory.common.events.WorldRegistrationCalls;
import io.github.flemmli97.runecraftory.common.registry.ModNPCJobs;
import io.github.flemmli97.runecraftory.common.registry.ModStructures;
import io.github.flemmli97.runecraftory.common.world.structure.processors.NPCDataProcessor;
import io.github.flemmli97.runecraftory.neoforge.data.worldgen.StructureWorldGen;
import io.github.flemmli97.tenshilib.common.data.provider.CodecBasedProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TemplatePoolGen extends CodecBasedProvider<StructureTemplatePool> {

    public TemplatePoolGen(PackOutput output, String modid, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, PackOutput.Target.DATA_PACK, "Template Pools", modid, Registries.TEMPLATE_POOL.location().getPath(), StructureTemplatePool.DIRECT_CODEC, provider);
    }

    @Override
    protected void add(HolderLookup.Provider provider) {
        this.add(RuneCraftory.modRes("npc/bath_house_under"), new StructureTemplatePool(
                StructureWorldGen.create(provider, Registries.TEMPLATE_POOL, RuneCraftory.modRes("npc/bath_house_under")),
                ImmutableList.of(Pair.of(StructurePoolElement.single(RuneCraftory.MODID + ":npc/bath_house_under").apply(StructureTemplatePool.Projection.RIGID), 1))));

        this.add(ModStructures.NPC_BIG_HOUSES.location(), new StructureTemplatePool(
                StructureWorldGen.create(provider, Pools.EMPTY),
                ImmutableList.of(Pair.of(StructurePoolElement.single(RuneCraftory.MODID + ":npc/bath_house", this.npcProcessor(ModNPCJobs.BATHHOUSE.asHolder())).apply(StructureTemplatePool.Projection.RIGID), 2),
                        Pair.of(StructurePoolElement.single(RuneCraftory.MODID + ":npc/restaurant", this.npcProcessor(ModNPCJobs.COOK.asHolder())).apply(StructureTemplatePool.Projection.RIGID), 2))));

        this.add(ModStructures.NPC_HOUSES.location(), new StructureTemplatePool(
                StructureWorldGen.create(provider, Pools.EMPTY),
                ImmutableList.of(Pair.of(StructurePoolElement.single(RuneCraftory.MODID + ":npc/npc_house_generic", this.npcProcessor(ModNPCJobs.GENERAL.asHolder())).apply(StructureTemplatePool.Projection.RIGID), 4),
                        Pair.of(StructurePoolElement.single(RuneCraftory.MODID + ":npc/npc_house_generic", this.npcProcessor(ModNPCJobs.FLOWER.asHolder())).apply(StructureTemplatePool.Projection.RIGID), 4),
                        Pair.of(StructurePoolElement.single(RuneCraftory.MODID + ":npc/npc_house_smith", this.npcProcessor(ModNPCJobs.SMITH.asHolder())).apply(StructureTemplatePool.Projection.RIGID), 4),
                        Pair.of(StructurePoolElement.single(RuneCraftory.MODID + ":npc/npc_house_clinic", this.npcProcessor(ModNPCJobs.DOCTOR.asHolder())).apply(StructureTemplatePool.Projection.RIGID), 4),
                        Pair.of(StructurePoolElement.single(RuneCraftory.MODID + ":npc/npc_house_single", this.npcProcessor(ModNPCJobs.MAGIC.asHolder())).apply(StructureTemplatePool.Projection.RIGID), 2),
                        Pair.of(StructurePoolElement.single(RuneCraftory.MODID + ":npc/npc_house_single", this.npcProcessor(ModNPCJobs.RUNE_SKILLS.asHolder())).apply(StructureTemplatePool.Projection.RIGID), 2))));

        for (String s : WorldRegistrationCalls.VANILLA_VILLAGES) {
            this.add(RuneCraftory.modRes("npc/streets/big_street_" + s), new StructureTemplatePool(
                    StructureWorldGen.create(provider, Pools.EMPTY),
                    ImmutableList.of(Pair.of(StructurePoolElement.legacy(RuneCraftory.MODID + ":npc/streets/big_street_" + s, StructureWorldGen.create(provider, fromVillage(s))).apply(StructureTemplatePool.Projection.TERRAIN_MATCHING), 2))));
        }
    }

    private static ResourceKey<StructureProcessorList> fromVillage(String villageType) {
        return switch (villageType) {
            case "plains" -> ProcessorLists.STREET_PLAINS;
            case "savanna" -> ProcessorLists.STREET_SAVANNA;
            case "snowy", "taiga" -> ProcessorLists.STREET_SNOWY_OR_TAIGA;
            default -> ResourceKey.create(Registries.PROCESSOR_LIST, ResourceLocation.withDefaultNamespace("empty"));
        };
    }

    public void add(ResourceLocation id, StructureTemplatePool pool) {
        this.contents.put(id, pool);
    }

    private Holder<StructureProcessorList> npcProcessor(Holder<NPCJob> shop) {
        return Holder.direct(new StructureProcessorList(List.of(
                new NPCDataProcessor(shop.unwrapKey().orElseThrow().location())
        )));
    }
}
