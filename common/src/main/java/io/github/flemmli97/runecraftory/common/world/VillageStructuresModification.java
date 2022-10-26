package io.github.flemmli97.runecraftory.common.world;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.npc.EnumShop;
import io.github.flemmli97.runecraftory.common.registry.ModStructures;
import io.github.flemmli97.runecraftory.mixinhelper.StructureTemplateModifier;
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VillageStructuresModification {

    public static Map<EnumShop, Holder<StructureProcessorList>> map = new HashMap<>();

    private static boolean init;

    public static void modifyVillagePools() {
        if (init)
            return;
        init = true;
        String[] vanillaVillages = new String[] {"plains", "desert", "savanna", "snowy", "taiga"};
        for(String s : vanillaVillages)
            addHousesTo(s);
    }

    private static void addHousesTo(String villageType) {
        addNewHouses(BuiltinRegistries.TEMPLATE_POOL.get(new ResourceLocation("village/"+villageType+"/houses")), List.of(
                Pair.of(StructurePoolElement.single(RuneCraftory.MODID + ":npc/npc_house_generic", get(EnumShop.GENERAL)).apply(StructureTemplatePool.Projection.RIGID), 3),
                Pair.of(StructurePoolElement.single(RuneCraftory.MODID + ":npc/npc_house_generic", get(EnumShop.FLOWER)).apply(StructureTemplatePool.Projection.RIGID), 3),
                Pair.of(StructurePoolElement.single(RuneCraftory.MODID + ":npc/npc_house_smith", get(EnumShop.WEAPON)).apply(StructureTemplatePool.Projection.RIGID), 3),
                Pair.of(StructurePoolElement.single(RuneCraftory.MODID + ":npc/npc_house_clinic", get(EnumShop.CLINIC)).apply(StructureTemplatePool.Projection.RIGID), 3)
        ));
    }

    private static Holder<StructureProcessorList> get(EnumShop shop) {
        return map.computeIfAbsent(shop, s -> ModStructures.NPC_PROCESSOR_LIST.get(shop));
    }

    private static void addNewHouses(StructureTemplatePool pool, List<Pair<StructurePoolElement, Integer>> houses) {
        if (pool == null)
            return;
        for (Pair<StructurePoolElement, Integer> p : houses)
            ((StructureTemplateModifier) pool).addPoolElement(p);
    }
}
