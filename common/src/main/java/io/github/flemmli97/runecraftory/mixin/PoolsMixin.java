package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.common.world.VillageStructuresModification;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Pools.class)
public class PoolsMixin {

    @Inject(method = "bootstrap", at = @At("RETURN"))
    private static void modify(CallbackInfoReturnable<Holder<StructureTemplatePool>> info) {
        VillageStructuresModification.modifyVillagePools();
    }
}
