package io.github.flemmli97.runecraftory.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.flemmli97.runecraftory.common.events.EntityCalls;
import io.github.flemmli97.runecraftory.common.utils.SeasonUtils;
import io.github.flemmli97.runecraftory.mixinhelper.MixinUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {

    @ModifyExpressionValue(method = "tickPrecipitation", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;getPrecipitationAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;"))
    private Biome.Precipitation withSeason(Biome.Precipitation original, BlockPos pos, @Local Biome biome, @Local(ordinal = 1) BlockPos pos2) {
        if (original == Biome.Precipitation.RAIN && SeasonUtils.coldEnoughForSnowSeason((ServerLevel) (Object) this, pos.below(), biome)) {
            return Biome.Precipitation.SNOW;
        }
        return original;
    }

    @Inject(method = "onBlockStateChange", at = @At("RETURN"))
    private void stateChangeInject(BlockPos pos, BlockState blockState, BlockState newState, CallbackInfo info) {
        MixinUtils.onBlockStateChange((ServerLevel) (Object) this, pos, blockState, newState);
    }

    @Inject(method = "tickNonPassenger", at = @At("HEAD"), cancellable = true)
    private void onTickEntity(Entity entity, CallbackInfo info) {
        if (entity instanceof LivingEntity living && EntityCalls.rootTick(living)) {
            info.cancel();
            for (Entity passengers : entity.getPassengers()) {
                if (entity instanceof LivingEntity livingPass && EntityCalls.rootTick(livingPass))
                    continue;
                this.tickPassenger(entity, passengers);
            }
        }
    }

    @Shadow
    protected abstract void tickPassenger(Entity ridingEntity, Entity passengerEntity);
}
