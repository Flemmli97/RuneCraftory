package io.github.flemmli97.runecraftory.neoforge.data.worldgen.structures;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.registry.NPCProfession;
import io.github.flemmli97.runecraftory.common.events.WorldRegistrationCalls;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCProfessions;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryStructures;
import io.github.flemmli97.runecraftory.common.world.structure.processors.NPCDataProcessor;
import io.github.flemmli97.runecraftory.neoforge.data.worldgen.StructureWorldGen;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.List;

public class TemplatePoolGen {

    public static void bootStrap(BootstrapContext<StructureTemplatePool> ctx) {
        ctx.register(ResourceKey.create(Registries.TEMPLATE_POOL, RuneCraftory.modRes("npc/bath_house_under")), new StructureTemplatePool(
                StructureWorldGen.create(ctx, Registries.TEMPLATE_POOL, RuneCraftory.modRes("npc/bath_house_under")),
                ImmutableList.of(Pair.of(StructurePoolElement.single(RuneCraftory.MODID + ":npc/bath_house_under").apply(StructureTemplatePool.Projection.RIGID), 1))));

        ctx.register(RuneCraftoryStructures.NPC_BIG_HOUSES, new StructureTemplatePool(
                empty(ctx),
                ImmutableList.of(Pair.of(StructurePoolElement.single(RuneCraftory.MODID + ":npc/bath_house", npcProcessor(RuneCraftoryNPCProfessions.BATHHOUSE_ATTENDANT.asHolder())).apply(StructureTemplatePool.Projection.RIGID), 2),
                        Pair.of(StructurePoolElement.single(RuneCraftory.MODID + ":npc/restaurant", npcProcessor(RuneCraftoryNPCProfessions.CHEF.asHolder())).apply(StructureTemplatePool.Projection.RIGID), 2))));

        ctx.register(RuneCraftoryStructures.NPC_HOUSES, new StructureTemplatePool(
                empty(ctx),
                ImmutableList.of(Pair.of(StructurePoolElement.single(RuneCraftory.MODID + ":npc/npc_house_generic", npcProcessor(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder())).apply(StructureTemplatePool.Projection.RIGID), 4),
                        Pair.of(StructurePoolElement.single(RuneCraftory.MODID + ":npc/npc_house_generic", npcProcessor(RuneCraftoryNPCProfessions.FLORIST.asHolder())).apply(StructureTemplatePool.Projection.RIGID), 4),
                        Pair.of(StructurePoolElement.single(RuneCraftory.MODID + ":npc/npc_house_smith", npcProcessor(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder())).apply(StructureTemplatePool.Projection.RIGID), 4),
                        Pair.of(StructurePoolElement.single(RuneCraftory.MODID + ":npc/npc_house_clinic", npcProcessor(RuneCraftoryNPCProfessions.DOCTOR.asHolder())).apply(StructureTemplatePool.Projection.RIGID), 4),
                        Pair.of(StructurePoolElement.single(RuneCraftory.MODID + ":npc/npc_house_single", npcProcessor(RuneCraftoryNPCProfessions.SPELL_MERCHANT.asHolder())).apply(StructureTemplatePool.Projection.RIGID), 2),
                        Pair.of(StructurePoolElement.single(RuneCraftory.MODID + ":npc/npc_house_single", npcProcessor(RuneCraftoryNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder())).apply(StructureTemplatePool.Projection.RIGID), 2))));

        for (String s : WorldRegistrationCalls.VANILLA_VILLAGES) {
            ctx.register(ResourceKey.create(Registries.TEMPLATE_POOL, RuneCraftory.modRes("npc/streets/big_street_" + s)), new StructureTemplatePool(
                    empty(ctx),
                    ImmutableList.of(Pair.of(StructurePoolElement.legacy(RuneCraftory.MODID + ":npc/streets/big_street_" + s, StructureWorldGen.create(ctx, fromVillage(s))).apply(StructureTemplatePool.Projection.TERRAIN_MATCHING), 2))));
        }
    }

    private static Holder<StructureTemplatePool> empty(BootstrapContext<?> ctx) {
        return ctx.lookup(Registries.TEMPLATE_POOL).getOrThrow(Pools.EMPTY);
    }

    private static ResourceKey<StructureProcessorList> fromVillage(String villageType) {
        return switch (villageType) {
            case "plains" -> ProcessorLists.STREET_PLAINS;
            case "savanna" -> ProcessorLists.STREET_SAVANNA;
            case "snowy", "taiga" -> ProcessorLists.STREET_SNOWY_OR_TAIGA;
            default -> ResourceKey.create(Registries.PROCESSOR_LIST, ResourceLocation.withDefaultNamespace("empty"));
        };
    }

    private static Holder<StructureProcessorList> npcProcessor(Holder<NPCProfession> shop) {
        return Holder.direct(new StructureProcessorList(List.of(
                new NPCDataProcessor(shop.unwrapKey().orElseThrow().location())
        )));
    }
}
