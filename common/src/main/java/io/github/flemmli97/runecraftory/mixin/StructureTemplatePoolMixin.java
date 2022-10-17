package io.github.flemmli97.runecraftory.mixin;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.mixinhelper.StructureTemplateModifier;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(StructureTemplatePool.class)
public class StructureTemplatePoolMixin implements StructureTemplateModifier {

    @Shadow
    private List<Pair<StructurePoolElement, Integer>> rawTemplates;
    @Shadow
    private List<StructurePoolElement> templates;

    @Override
    public void addPoolElement(Pair<StructurePoolElement, Integer> pair) {
        this.rawTemplates.add(pair);
        StructurePoolElement structurePoolElement = pair.getFirst();
        for (int i = 0; i < pair.getSecond(); i++) {
            this.templates.add(structurePoolElement);
        }
    }
}
