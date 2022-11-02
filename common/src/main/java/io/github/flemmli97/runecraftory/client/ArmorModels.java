package io.github.flemmli97.runecraftory.client;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.items.equipment.ItemArmorBase;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class ArmorModels {

    public static final Map<ResourceLocation, ArmorModelGetter> armorGetter = getArmorRenderer();

    private static Map<ResourceLocation, ArmorModelGetter> getArmorRenderer() {
        ImmutableMap.Builder<ResourceLocation, ArmorModelGetter> builder = ImmutableMap.builder();
        builder.put(ModItems.magicEarrings.getID(), ((entityLiving, itemStack, slot, origin) -> {
            origin.setAllVisible(false);
            origin.head.visible = true;
            origin.hat.visible = true;
            return null;
        }));
        return builder.build();
    }

    public static ArmorModelGetter fromItemStack(ItemStack stack) {
        if (stack.getItem() instanceof ItemArmorBase armor)
            return armorGetter.get(armor.registryID);
        return null;
    }

    public interface ArmorModelGetter {
        Model getModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot slot, HumanoidModel<?> origin);
    }
}
