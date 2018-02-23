package com.flemmli97.runecraftory.common.lib.enums;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum EnumStatusEffect {
	//poison, seal, para=slowness, sleep, fatigue, cold, knockout, dizzy
	//move sleep to effects potion
	POISON("poison"),
	SEAL("seal"),
	PARALYSIS("paralysis"),
	FATIGUE("fatigue"),
	COLD("cold");

	private String name;
	EnumStatusEffect(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return this.name;
	}
	public static EnumStatusEffect fromName(String name)
	{
		for(EnumStatusEffect effect : EnumStatusEffect.values())
		{
			if(effect.getName().equals(name))
				return effect;
		}
		return null;
	}
	public void update() {}
	//public abstract ResourceLocation statusIcon();
	public void addAttributeModifier(EntityLivingBase entity) {
		
	}
	public void removeAttributeModifier(EntityLivingBase entity)
	{
		
	}
	@SideOnly(value=Side.CLIENT)
	public void render()
	{
		
	}
	
}
