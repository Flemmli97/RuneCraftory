package io.github.flemmli97.runecraftory.mixin;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.mixinhelper.StructureTemplateModifier;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(StructureTemplatePool.class)
public abstract class StructureTemplatePoolMixin implements StructureTemplateModifier {

    @Mutable
    @Final
    @Shadow
    private List<Pair<StructurePoolElement, Integer>> rawTemplates;
    @Final
    @Shadow
    private ObjectArrayList<StructurePoolElement> templates;

    @Override
    public void runecraftory$addPoolElement(Pair<StructurePoolElement, Integer> pair) {
        List<Pair<StructurePoolElement, Integer>> raw = new ArrayList<>(this.rawTemplates);
        raw.add(pair);
        this.rawTemplates = raw;
        StructurePoolElement structurePoolElement = pair.getFirst();
        for (int i = 0; i < pair.getSecond(); i++) {
            this.templates.add(structurePoolElement);
        }
    }

    @Override
    public List<Pair<StructurePoolElement, Integer>> runecraftory$getRawTemplates() {
        return this.rawTemplates;
    }
}
