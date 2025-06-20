package io.github.flemmli97.runecraftory.mixin;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Pools.class)
public abstract class PoolsMixin {

    @Inject(method = "bootstrap", at = @At("RETURN"))
    private static void modify(BootstrapContext<StructureTemplatePool> context, CallbackInfo ci) {
//        VillageStructuresModification.modifyVillagePools();
    }
}
