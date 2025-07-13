package io.github.flemmli97.runecraftory.fabric.mixin;

import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.effects.UncurableEffect;
import io.github.flemmli97.runecraftory.common.events.EntityCalls;
import io.github.flemmli97.runecraftory.fabric.RuneCraftoryFabric;
import io.github.flemmli97.runecraftory.fabric.mixinhelper.EntityDataGetter;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements EntityDataGetter {

    @Shadow
    @Final
    private Map<Holder<MobEffect>, MobEffectInstance> activeEffects;

    @Unique
    private final EntityData runecraftory$EntityData = new EntityData((LivingEntity) (Object) this);
    @Unique
    private boolean runecraftory$EffectCuringProcess;

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

    @ModifyVariable(method = "removeAllEffects", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/Collection;iterator()Ljava/util/Iterator;"))
    private Iterator<MobEffectInstance> onEffectCure(Iterator<MobEffectInstance> value) {
        if (this.runecraftory$EffectCuringProcess) {
            return this.activeEffects.values().stream().filter(eff -> !(eff.getEffect().value() instanceof UncurableEffect))
                    .iterator();
        }
        return value;
    }

    @Override
    public EntityData runecraftory$getEntityData() {
        return this.runecraftory$EntityData;
    }

    @Override
    public void runecraftory$effectCuringProcess(boolean inProgress) {
        this.runecraftory$EffectCuringProcess = inProgress;
    }
}
