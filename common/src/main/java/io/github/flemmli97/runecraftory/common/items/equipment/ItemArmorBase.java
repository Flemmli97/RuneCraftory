package io.github.flemmli97.runecraftory.common.items.equipment;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import io.github.flemmli97.runecraftory.api.items.StatItem;
import io.github.flemmli97.runecraftory.common.lib.ItemTiers;
import io.github.flemmli97.runecraftory.platform.ExtendedItem;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class ItemArmorBase extends ArmorItem implements ExtendedItem, StatItem {

    private static final String armorModelPath = "runecraftory:textures/models/armor/";
    public final ResourceLocation registryID;

    public ItemArmorBase(EquipmentSlot slot, Properties properties, ResourceLocation registryID) {
        super(ItemTiers.armor, slot, properties);
        this.registryID = registryID;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    @Nullable
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return armorModelPath + PlatformUtils.INSTANCE.items().getIDFrom(stack.getItem()).getPath() + ".png";
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
