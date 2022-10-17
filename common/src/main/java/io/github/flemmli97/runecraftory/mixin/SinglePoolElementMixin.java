package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.common.world.structure.processors.DataStructureBlockProcessor;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SinglePoolElement.class)
public abstract class SinglePoolElementMixin {

    @Shadow
    protected Holder<StructureProcessorList> processors;

    /**
     * We shift vanillas processor to last. That way we can actually access structure blocks
     */
    @Inject(method = "getSettings", at = @At("RETURN"))
    private void modifySettings(Rotation rotation, BoundingBox boundingBox, boolean bl, CallbackInfoReturnable<StructurePlaceSettings> info) {
        if (this.processors.value().list().stream().anyMatch(p -> p instanceof DataStructureBlockProcessor)) {
            StructurePlaceSettings setting = info.getReturnValue();
            setting.popProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
            setting.addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        }
    }
}
