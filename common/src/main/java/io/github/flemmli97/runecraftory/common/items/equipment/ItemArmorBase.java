package io.github.flemmli97.runecraftory.common.items.equipment;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import io.github.flemmli97.runecraftory.common.lib.ItemTiers;
import io.github.flemmli97.runecraftory.platform.ExtendedItem;
import io.github.flemmli97.tenshilib.api.item.DynamicArmorTextureItem;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

public class ItemArmorBase extends ArmorItem implements ExtendedItem, DynamicArmorTextureItem {

    private static final String ARMOR_MODEL_PATH = "runecraftory:textures/models/armor/";
    private static final String ITEM_PATH = "runecraftory:textures/models/armor/empty.png";
    public final ResourceLocation registryID;
    private final boolean useItemTexture;

    public ItemArmorBase(EquipmentSlot slot, Properties properties, ResourceLocation registryID, boolean useItemTexture) {
        super(ItemTiers.ARMOR, slot, properties);
        this.registryID = registryID;
        this.useItemTexture = useItemTexture;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return this.useItemTexture ? ITEM_PATH : (ARMOR_MODEL_PATH + PlatformUtils.INSTANCE.items().getIDFrom(this).getPath() + ".png");
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return false;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return ImmutableMultimap.of();
    }
}
