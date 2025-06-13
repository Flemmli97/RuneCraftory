package io.github.flemmli97.runecraftory.common.items.equipment;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.lib.ItemTiers;
import io.github.flemmli97.runecraftory.platform.ExtendedItem;
import io.github.flemmli97.tenshilib.common.item.DynamicArmorTextureItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

public class ItemArmorBase extends ArmorItem implements ExtendedItem, DynamicArmorTextureItem {

    private static final String ARMOR_MODEL_PATH = "textures/models/armor/";
    private static final ResourceLocation ITEM_PATH = RuneCraftory.modRes("textures/models/armor/empty.png");
    public final ResourceLocation armorPath;

    public ItemArmorBase(ArmorItem.Type slot, Properties properties, ResourceLocation id, boolean useItemTexture) {
        super(ItemTiers.ARMOR, slot, properties);
        this.armorPath = useItemTexture ? ITEM_PATH : ResourceLocation.fromNamespaceAndPath(id.getPath(), ARMOR_MODEL_PATH + id.getPath() + ".png");
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return this.armorPath;
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return false;
    }
}
