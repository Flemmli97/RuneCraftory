package io.github.flemmli97.runecraftory.mixinhelper;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;

import java.util.List;

public interface StructureTemplateModifier {

    void runecraftory$addPoolElement(Pair<StructurePoolElement, Integer> pair);

    List<Pair<StructurePoolElement, Integer>> runecraftory$getRawTemplates();
}
