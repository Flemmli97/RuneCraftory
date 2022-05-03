package io.github.flemmli97.runecraftory.common.items.equipment;

import net.minecraft.world.entity.EquipmentSlot;

public class ItemAccessoireBase extends ItemArmorBase {

    private final EquipmentSlot renderSlot;

    public ItemAccessoireBase(Properties properties) {
        super(EquipmentSlot.LEGS, properties);
        this.renderSlot = this.getSlot();
    }

    public ItemAccessoireBase(EquipmentSlot renderSlot, Properties properties) {
        super(EquipmentSlot.LEGS, properties);
        this.renderSlot = renderSlot;
    }

    public EquipmentSlot getRenderSlot() {
        return this.renderSlot;
    }
}
