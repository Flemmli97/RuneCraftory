package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.common.world.structure.processors.WaterUnlogProcessor;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StructureTemplate.class)
public abstract class StructureTemplateMixin {

    @Inject(method = "placeInWorld", at = @At(value = "HEAD"))
    private void stopWaterLog(ServerLevelAccessor serverLevel, BlockPos offset, BlockPos pos, StructurePlaceSettings settings, RandomSource random, int flags, CallbackInfoReturnable<Boolean> info) {
        if (settings.getProcessors().contains(WaterUnlogProcessor.INST))
            settings.setLiquidSettings(LiquidSettings.IGNORE_WATERLOGGING);
    }
}
