package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.mixinhelper.MixinUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Unique
    private boolean hitSomething;

    @Inject(method = "tick", at = @At("TAIL"))
    private void checkEntityCollision(CallbackInfo info) {
        if (!this.hitSomething && !((ItemEntity) (Object) this).isOnGround())
            this.hitSomething = MixinUtils.handleEntityCollision(((ItemEntity) (Object) this));
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    private void saveHit(CompoundTag tag, CallbackInfo info) {
        tag.putBoolean("HitSomething", this.hitSomething);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    private void readHit(CompoundTag tag, CallbackInfo info) {
        this.hitSomething = tag.getBoolean("HitSomething");
    }
}
