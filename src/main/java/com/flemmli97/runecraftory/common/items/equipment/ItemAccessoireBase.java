package com.flemmli97.runecraftory.common.items.equipment;

import javax.annotation.Nullable;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.items.IItemWearable;
import com.flemmli97.runecraftory.client.models.ModelAccessory;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemAccessoireBase extends ItemArmor implements IItemWearable
{
    private static final String armorModelPath = "runecraftory:textures/models/armor/";
    private EntityEquipmentSlot type = EntityEquipmentSlot.LEGS;
    
    public ItemAccessoireBase(String name) {
        this(ModItems.chain, name);
    }
    
    public ItemAccessoireBase(ItemArmor.ArmorMaterial mat, String name) {
        super(mat, 0, EntityEquipmentSlot.LEGS);
        this.setMaxStackSize(1);
        this.setCreativeTab(RuneCraftory.equipment);
        this.setRegistryName(new ResourceLocation(LibReference.MODID, name));
        this.setUnlocalizedName(this.getRegistryName().toString());
    }
    
    public ItemAccessoireBase setModelType(EntityEquipmentSlot type)
    {
    	this.type=type;
    	return this;
    }
    
    public EntityEquipmentSlot modelType()
    {
    	return this.type;
    }
    
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }
    
    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return armorModelPath + stack.getItem().getRegistryName().getResourcePath() + ".png";
    }
    
    @SideOnly(Side.CLIENT)
    @Nullable
    @Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
    	return ModelAccessory.model.setItem(this);
    }
    
    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        return HashMultimap.create();
    }
    
    @SideOnly(Side.CLIENT)
    public ModelBase customModel()
    {
    	return null;
    }
}
