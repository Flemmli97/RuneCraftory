package io.github.flemmli97.runecraftory.fabric.mixin;

import com.google.common.collect.Multimap;
import io.github.flemmli97.runecraftory.common.attachment.ArmorEffectData;
import io.github.flemmli97.runecraftory.common.attachment.StaffData;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.fabric.mixinhelper.ItemStackDataGetter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemStackDataGetter {

    @Unique
    private StaffData runecraftoryStaffData;
    @Unique
    private ArmorEffectData runecraftoryArmorEffectData;

    @Shadow
    private Item item;

    @Inject(method = "<init>(Lnet/minecraft/world/level/ItemLike;I)V", at = @At("RETURN"))
    private void onInit(ItemLike itemLike, int i, CallbackInfo info) {
        if (this.item instanceof ItemStaffBase)
            this.runecraftoryStaffData = new StaffData();
        if (this.item instanceof ArmorItem)
            this.runecraftoryArmorEffectData = new ArmorEffectData();
    }

    @Inject(method = "<init>(Lnet/minecraft/nbt/CompoundTag;)V", at = @At("RETURN"))
    private void onNBTInit(CompoundTag compound, CallbackInfo info) {
        if (this.item instanceof ItemStaffBase) {
            this.runecraftoryStaffData = new StaffData();
            if (compound.contains(ItemStackDataGetter.STAFF_ID.toString()))
                this.runecraftoryStaffData.readFromNBT(compound.getCompound(ItemStackDataGetter.STAFF_ID.toString()));
        }
        if (this.item instanceof ArmorItem) {
            this.runecraftoryArmorEffectData = new ArmorEffectData();
            if (compound.contains(ItemStackDataGetter.ARMOR_EFFECT_ID.toString()))
                this.runecraftoryArmorEffectData.readFromNBT(compound.getCompound(ItemStackDataGetter.ARMOR_EFFECT_ID.toString()));
        }
    }

    @Inject(method = "save", at = @At("RETURN"))
    private void saveData(CompoundTag compound, CallbackInfoReturnable<CompoundTag> info) {
        if (this.runecraftoryStaffData != null)
            compound.put(ItemStackDataGetter.STAFF_ID.toString(), this.runecraftoryStaffData.writeToNBT(new CompoundTag()));
        if (this.runecraftoryArmorEffectData != null)
            compound.put(ItemStackDataGetter.ARMOR_EFFECT_ID.toString(), this.runecraftoryArmorEffectData.writeToNBT(new CompoundTag()));
    }

    @Inject(method = "copy", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;setPopTime(I)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void onCopy(CallbackInfoReturnable<ItemStack> info, ItemStack stack) {
        StaffData staff = ((ItemStackDataGetter) (Object) stack).getStaffData();
        if (staff != null)
            staff.readFromNBT(this.runecraftoryStaffData.writeToNBT(new CompoundTag()));
        ArmorEffectData armor = ((ItemStackDataGetter) (Object) stack).getArmorEffectData();
        if (armor != null)
            armor.readFromNBT(this.runecraftoryArmorEffectData.writeToNBT(new CompoundTag()));
    }

    @Override
    public StaffData getStaffData() {
        return this.runecraftoryStaffData;
    }

    @Override
    public ArmorEffectData getArmorEffectData() {
        return this.runecraftoryArmorEffectData;
    }

    @ModifyVariable(method = "getAttributeModifiers", at = @At(value = "RETURN", shift = At.Shift.BY, by = -2))
    private Multimap<Attribute, AttributeModifier> getStats(Multimap<Attribute, AttributeModifier> old, EquipmentSlot slot) {
        return ItemNBT.getStatsAttributeMap((ItemStack) (Object) this, old, slot);
    }
}
