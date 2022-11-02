package io.github.flemmli97.runecraftory.forge.item;

import io.github.flemmli97.runecraftory.common.items.equipment.ItemArmorBase;
import io.github.flemmli97.runecraftory.forge.client.ClientEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.function.Consumer;

public class ForgeArmorBase extends ItemArmorBase {

    public ForgeArmorBase(EquipmentSlot slot, Properties properties, ResourceLocation registryID) {
        super(slot, properties, registryID);
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        ClientEvents.initClientItemProps(consumer);
    }
}
