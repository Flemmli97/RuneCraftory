package io.github.flemmli97.runecraftory.common.world;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.registry.ModNPCJobs;
import io.github.flemmli97.runecraftory.common.registry.ModStructures;
import io.github.flemmli97.runecraftory.mixinhelper.StructureTemplateModifier;
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VillageStructuresModification {

    public static Map<ResourceLocation, Holder<StructureProcessorList>> map = new HashMap<>();

    private static boolean init;

    public static void modifyVillagePools() {
        if (init)
            return;
        init = true;
        Pools.register(new StructureTemplatePool(new ResourceLocation(RuneCraftory.MODID, "npc/bath_house_under"), new ResourceLocation(RuneCraftory.MODID, "npc/bath_house_under"),
                ImmutableList.of(Pair.of(StructurePoolElement.single(RuneCraftory.MODID + ":npc/bath_house_under", ProcessorLists.EMPTY).apply(StructureTemplatePool.Projection.RIGID), 1))));

        String[] vanillaVillages = new String[]{"plains", "desert", "savanna", "snowy", "taiga"};
        for (String s : vanillaVillages) {
            if (!s.equals("savanna") && !s.equals("desert")) {
                //Register custom pool with big houses
                Pools.register(new StructureTemplatePool(new ResourceLocation(RuneCraftory.MODID, "npc/big_houses_" + s), new ResourceLocation(RuneCraftory.MODID, "npc/big_houses_" + s),
                        ImmutableList.of(Pair.of(StructurePoolElement.single(RuneCraftory.MODID + ":npc/bath_house", get(ModNPCJobs.BATHHOUSE.getFirst())).apply(StructureTemplatePool.Projection.RIGID), 1))));
                //Add a big street to the villages streets pool
                add(BuiltinRegistries.TEMPLATE_POOL.get(new ResourceLocation("village/" + s + "/streets")), List.of(
                        Pair.of(StructurePoolElement.legacy(RuneCraftory.MODID + ":npc/streets/big_street_" + s, fromVillage(s)).apply(StructureTemplatePool.Projection.TERRAIN_MATCHING), 1)
                ));
            }
            //Add all other smaller houses
            addHousesTo(s);
        }
    }

    private static Holder<StructureProcessorList> fromVillage(String villageType) {
        return switch (villageType) {
            case "plains" -> ProcessorLists.STREET_PLAINS;
            case "savanna" -> ProcessorLists.STREET_SAVANNA;
            case "snowy", "taiga" -> ProcessorLists.STREET_SNOWY_OR_TAIGA;
            default -> ProcessorLists.EMPTY;
        };
    }

    private static void addHousesTo(String villageType) {
        add(BuiltinRegistries.TEMPLATE_POOL.get(new ResourceLocation("village/" + villageType + "/houses")), List.of(
                Pair.of(StructurePoolElement.single(RuneCraftory.MODID + ":npc/npc_house_generic", get(ModNPCJobs.GENERAL.getFirst())).apply(StructureTemplatePool.Projection.RIGID), 4),
                Pair.of(StructurePoolElement.single(RuneCraftory.MODID + ":npc/npc_house_generic", get(ModNPCJobs.FLOWER.getFirst())).apply(StructureTemplatePool.Projection.RIGID), 4),
                Pair.of(StructurePoolElement.single(RuneCraftory.MODID + ":npc/npc_house_smith", get(ModNPCJobs.WEAPON.getFirst())).apply(StructureTemplatePool.Projection.RIGID), 4),
                Pair.of(StructurePoolElement.single(RuneCraftory.MODID + ":npc/npc_house_clinic", get(ModNPCJobs.CLINIC.getFirst())).apply(StructureTemplatePool.Projection.RIGID), 4),
                Pair.of(StructurePoolElement.single(RuneCraftory.MODID + ":npc/npc_house_single", get(ModNPCJobs.MAGIC.getFirst())).apply(StructureTemplatePool.Projection.RIGID), 2),
                Pair.of(StructurePoolElement.single(RuneCraftory.MODID + ":npc/npc_house_single", get(ModNPCJobs.RUNESKILL.getFirst())).apply(StructureTemplatePool.Projection.RIGID), 2)
        ));
    }

    private static Holder<StructureProcessorList> get(ResourceLocation shop) {
        return map.computeIfAbsent(shop, s -> ModStructures.NPC_PROCESSOR_LIST.get(shop));
    }

    private static void add(StructureTemplatePool pool, List<Pair<StructurePoolElement, Integer>> houses) {
        if (pool == null)
            return;
        for (Pair<StructurePoolElement, Integer> p : houses)
            ((StructureTemplateModifier) pool).addPoolElement(p);
    }
}
