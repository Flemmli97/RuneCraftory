package io.github.flemmli97.runecraftory.mixinhelper;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;

public interface StructureTemplateModifier {

    void addPoolElement(Pair<StructurePoolElement, Integer> pair);
}
