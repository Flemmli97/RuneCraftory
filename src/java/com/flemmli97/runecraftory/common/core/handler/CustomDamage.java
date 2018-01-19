package com.flemmli97.runecraftory.common.core.handler;

import com.flemmli97.runecraftory.api.enums.EnumElement;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

public class CustomDamage extends DamageSource{

    public static final DamageSource EXHAUST = (new DamageSource("rfExhaust")).setDamageBypassesArmor().setDamageIsAbsolute();

	private EntityLivingBase entity;
	private EnumElement element;
	private boolean ignoreMagic;
	public CustomDamage(String damageTypeIn, EntityLivingBase attacker, EnumElement element) {
		super(damageTypeIn);
		this.entity=attacker;
		this.element=element;
	}

	@Override
    public EntityLivingBase getTrueSource()
    {
    		return this.entity;
    }
    
    public EnumElement getElement()
    {
    		return this.element;
    }
    
    public boolean ignoreMagicDef()
    {
    		return this.ignoreMagic;
    }
    
    /**
     * Custom Damage
     * @param attacker : attacking entity
     * @param element : elemental damage
     * @param type : 0 = normal physical, 1 = ignore def, 2 = magic, 3 = ignore magic def
     * @return
     */
    public static final CustomDamage doAttack(EntityLivingBase attacker, EnumElement element, int type)
    {
    		CustomDamage source = new CustomDamage("rfAttack", attacker, element);
    		switch(type)
    		{
    			case 1:
    				source.setDamageBypassesArmor();
    				break;
    			case 2:
    				source.setMagicDamage();
    				break;
    			case 3:
    				source.setMagicDamage();
    				source.ignoreMagic=true;
    				break;
    		}
    		return source;
    }
}
