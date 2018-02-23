package com.flemmli97.runecraftory.common.core.handler;

import com.flemmli97.runecraftory.common.lib.enums.EnumElement;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;

public class CustomDamage extends EntityDamageSource{

    public static final DamageSource EXHAUST = (new DamageSource("rfExhaust")).setDamageBypassesArmor().setDamageIsAbsolute();

	private EnumElement element;
	private boolean ignoreMagic;
	public CustomDamage(String damageTypeIn, EntityLivingBase attacker, EnumElement element) {
		super(damageTypeIn, attacker);
		this.element=element;
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
