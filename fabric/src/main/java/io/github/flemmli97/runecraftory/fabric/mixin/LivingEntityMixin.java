package io.github.flemmli97.runecraftory.fabric.mixin;

import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.events.EntityCalls;
import io.github.flemmli97.runecraftory.fabric.RuneCraftoryFabric;
import io.github.flemmli97.runecraftory.fabric.mixinhelper.EntityDataGetter;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements EntityDataGetter {

    @Unique
    private final EntityData runecraftoryEntityData = new EntityData();

    @Shadow
    protected ItemStack useItem;

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickCall(CallbackInfo info) {
        RuneCraftoryFabric.entityTick((LivingEntity) (Object) this);
    }

    @Inject(method = "completeUsingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;triggerItemUseEffects(Lnet/minecraft/world/item/ItemStack;I)V", shift = At.Shift.AFTER))
    private void useItemDone(CallbackInfo info) {
        EntityCalls.foodHandling((LivingEntity) (Object) this, this.useItem.copy());
    }

    @Override
    public EntityData runecraftory$getEntityData() {
        return this.runecraftoryEntityData;
    }

    @Override
    public void runecraftory$onCureEffect(MobEffectInstance effect) {
        this.onEffectRemoved(effect);
    }

    @Shadow
    protected abstract void onEffectRemoved(MobEffectInstance effect);
}
