package com.flemmli97.runecraftory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.utils.ItemNBT;
import com.flemmli97.runecraftory.proxy.CommonProxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = LibReference.MODID, name = LibReference.MODNAME, version = LibReference.VERSION, dependencies = LibReference.dependencies, guiFactory = LibReference.guiFactory)
public class RuneCraftory
{
    @Instance
    public static RuneCraftory instance = new RuneCraftory();
    
    @SidedProxy(clientSide = LibReference.clientProxy, serverSide = LibReference.commonProxy)
    public static CommonProxy proxy;
    
    @EventHandler
    public void construction(FMLConstructionEvent event) {
        FluidRegistry.enableUniversalBucket();
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        this.modifyAttributes();
        proxy.preInit(e);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }
    
    @EventHandler
    public void serverStart(FMLServerStartingEvent event) {
    }
    
    public static CreativePlus weaponToolTab = new CreativePlus("runecraftory.weaponsTools") 
    {
    	@Override
        public ItemStack getTabIconItem() 
        {
            return new ItemStack(ModItems.icon0);
        }
    };
    
    public static CreativePlus equipment = new CreativePlus("runecraftory.equipment") 
    {
    	@Override
        public ItemStack getTabIconItem() 
        {
            return new ItemStack(ModItems.cheapBracelet);
        }
    };
    
    public static CreativePlus upgradeItems = new CreativePlus("runecraftory.upgrade") 
    {
    	@Override
        public ItemStack getTabIconItem() 
        {
            return new ItemStack(ModItems.dragonic);
        }
    };
    
    public static CreativePlus blocks = new CreativePlus("runecraftory.blocks") 
    {
    	@Override
        public ItemStack getTabIconItem() 
        {
            return new ItemStack(ModItems.itemBlockForge);
        }
    };
    
    public static CreativePlus medicine = new CreativePlus("runecraftory.medicine") 
    {
    	@Override
        public ItemStack getTabIconItem() 
        {
            return new ItemStack(ModItems.recoveryPotion);
        }
    };
    
    public static CreativePlus cast = new CreativePlus("runecraftory.cast") 
    {
    	@Override
        public ItemStack getTabIconItem() 
        {
            return new ItemStack(ModItems.teleport);
        }
    };
    
    public static CreativePlus food = new CreativePlus("runecraftory.food") 
    {
    	@Override
        public ItemStack getTabIconItem() 
        {
            return new ItemStack(ModItems.icon1);
        }
    };
    
    public static CreativePlus crops = new CreativePlus("runecraftory.crops") 
    {
    	@Override
        public ItemStack getTabIconItem() 
        {
            return new ItemStack(ModItems.turnipSeeds);
        }
    };
    
    public static CreativePlus monsters = new CreativePlus("runecraftory.monsters") 
    {
    	@Override
        public ItemStack getTabIconItem() 
        {
            return new ItemStack(ModItems.icon2);
        }
    };
    
    private static abstract class CreativePlus extends CreativeTabs
    {
		public CreativePlus(String label) {
			super(label);
		}

		@Override
		public void displayAllRelevantItems(NonNullList<ItemStack> list) {
			for (Item item : Item.REGISTRY)
	        {
				NonNullList<ItemStack> sub = NonNullList.create();
	            item.getSubItems(this, sub);
	            sub.forEach(stack->ItemNBT.initNBT(stack));
	            list.addAll(sub);
	        }
		}
    }
    
    private void modifyAttributes()
    {
    	this.configureRangedAttribute((RangedAttribute) SharedMonsterAttributes.MAX_HEALTH, Integer.MAX_VALUE);
    	this.configureRangedAttribute((RangedAttribute) SharedMonsterAttributes.ATTACK_DAMAGE, Integer.MAX_VALUE);
    }
    
    public void configureRangedAttribute(RangedAttribute attribute, double value) {
    	try {
    		//maximumValue
    		Field f = ObfuscationReflectionHelper.findField(RangedAttribute.class, "field_111118_b");
			f.setAccessible(true);
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
			f.set(attribute, value);
			modifiersField.setInt(f, f.getModifiers()&Modifier.FINAL);
			f.setAccessible(false);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e1) {
			LibReference.logger.error("Error modifying attribute {}", attribute.getName());
			e1.printStackTrace();
		}
    }
}
