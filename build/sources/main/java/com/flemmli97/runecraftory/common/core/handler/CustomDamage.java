package com.flemmli97.runecraftory.common.core.handler;

import com.flemmli97.runecraftory.common.lib.enums.EnumElement;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class CustomDamage extends EntityDamageSource{

    public static final DamageSource EXHAUST = (new DamageSource("rfExhaust")).setDamageBypassesArmor().setDamageIsAbsolute();

	private EnumElement element;
	private KnockBackType knock;
	private boolean ignoreMagic;
	private Entity sourceEntity;
	public CustomDamage(String damageTypeIn, EntityLivingBase attacker, EnumElement element, KnockBackType knock) {
		super(damageTypeIn, attacker);
		this.element=element;
		if(knock == KnockBackType.NONE)
		{
			this.damageSourceEntity=null;
			this.sourceEntity=attacker;
		}
	}
    
    public EnumElement getElement()
    {
    		return this.element;
    }
    
    public boolean ignoreMagicDef()
    {
    		return this.ignoreMagic;
    }
    
    public KnockBackType getKnockBackType()
    {
    		return this.knock;
    }
    
    public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn)
    {
    		if(this.damageSourceEntity!=null)
    		{
    			ItemStack itemstack = this.damageSourceEntity instanceof EntityLivingBase ? ((EntityLivingBase)this.damageSourceEntity).getHeldItemMainhand() : ItemStack.EMPTY;
        		String s = "death.attack." + this.damageType;
        		String s1 = s + ".item";
        		return !itemstack.isEmpty() && itemstack.hasDisplayName() && I18n.hasKey(s1) ? new TextComponentTranslation(s1, new Object[] {entityLivingBaseIn.getDisplayName(), this.damageSourceEntity.getDisplayName(), itemstack.getTextComponent()}) : new TextComponentTranslation(s, new Object[] {entityLivingBaseIn.getDisplayName(), this.damageSourceEntity.getDisplayName()});
    		}
    		else 
    		{
    			ItemStack itemstack = this.sourceEntity instanceof EntityLivingBase ? ((EntityLivingBase)this.sourceEntity).getHeldItemMainhand() : ItemStack.EMPTY;
    	        String s = "death.attack." + this.damageType;
    	        String s1 = s + ".item";
    	        return !itemstack.isEmpty() && itemstack.hasDisplayName() && I18n.hasKey(s1) ? new TextComponentTranslation(s1, new Object[] {entityLivingBaseIn.getDisplayName(), this.sourceEntity.getDisplayName(), itemstack.getTextComponent()}) : new TextComponentTranslation(s, new Object[] {entityLivingBaseIn.getDisplayName(), this.sourceEntity.getDisplayName()});
    		}
    }
    
    /**
     * Custom Damage
     * @param attacker : attacking entity
     * @param element : elemental damage
     * @param type : 0 = normal physical, 1 = ignore def, 2 = magic, 3 = ignore magic def
     * @return
     */
    public static final CustomDamage doAttack(EntityLivingBase attacker, EnumElement element, DamageType type, KnockBackType knock)
    {
    		CustomDamage source = new CustomDamage("rfAttack", attacker, element, knock);
    		if(element!=EnumElement.NONE)
    			source.setMagicDamage();
    		switch(type)
    		{
    			case NORMAL:
    				break;
    			case IGNOREDEF:
    				source.setDamageBypassesArmor();
    				source.ignoreMagic=true;
    				break;
    		}
    		return source;
    }
    public static final CustomDamage doAttack(EntityLivingBase attacker, EnumElement element, DamageType type)
    {
    		return doAttack(attacker, element, type, KnockBackType.VANILLA);
    }
    
    public enum DamageType
    {
    		NORMAL,
    		IGNOREDEF;
    }
    
    public enum KnockBackType
    {
    		NONE,
    		VANILLA;
    	}
}
