package io.github.flemmli97.runecraftory.fabric.mixin;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.attachment.StaffData;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.fabric.mixinhelper.StaffDataGetter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements StaffDataGetter {

    @Unique
    private StaffData runecraftoryPlayerData;

    @Shadow
    private Item item;

    @Inject(method = "<init>(Lnet/minecraft/world/level/ItemLike;I)V", at = @At("RETURN"))
    private void onInit(ItemLike itemLike, int i, CallbackInfo info) {
        if (this.item instanceof ItemStaffBase)
            this.runecraftoryPlayerData = new StaffData();
    }

    @Inject(method = "<init>(Lnet/minecraft/nbt/CompoundTag;)V", at = @At("RETURN"))
    private void onNBTInit(CompoundTag compound, CallbackInfo info) {
        if (this.item instanceof ItemStaffBase) {
            this.runecraftoryPlayerData = new StaffData();
            if (compound.contains(RuneCraftory.MODID + ":staff"))
                this.runecraftoryPlayerData.readFromNBT(compound.getCompound(RuneCraftory.MODID + ":staff"));
        }
    }

    @Inject(method = "save", at = @At("RETURN"))
    private void saveData(CompoundTag compound, CallbackInfoReturnable<CompoundTag> info) {
        if (this.runecraftoryPlayerData != null)
            compound.put(RuneCraftory.MODID + ":staff", this.runecraftoryPlayerData.writeToNBT(new CompoundTag()));
    }

    @Inject(method = "copy", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;setPopTime(I)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void onCopy(CallbackInfoReturnable<ItemStack> info, ItemStack stack) {
        StaffData data = ((StaffDataGetter) (Object) stack).getStaffData();
        if (data != null)
            data.readFromNBT(this.runecraftoryPlayerData.writeToNBT(new CompoundTag()));
    }

    @Override
    public StaffData getStaffData() {
        return this.runecraftoryPlayerData;
    }
}
