package io.github.flemmli97.runecraftory.common.items.equipment;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public class ItemAccessoireBase extends ItemArmorBase {

    private EquipmentSlotType renderSlot;

    public ItemAccessoireBase(Properties properties) {
        super(EquipmentSlotType.LEGS, properties);
        this.renderSlot = this.getEquipmentSlot();
    }

    public ItemAccessoireBase(EquipmentSlotType renderSlot, Properties properties) {
        super(EquipmentSlotType.LEGS, properties);
        this.renderSlot = renderSlot;
    }

    public EquipmentSlotType getRenderSlot() {
        return this.renderSlot;
    }

    @Override
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        _default.setVisible(false);
        return _default;
    }
}
