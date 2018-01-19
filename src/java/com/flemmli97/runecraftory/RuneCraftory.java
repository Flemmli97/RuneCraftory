package com.flemmli97.runecraftory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.lib.RFReference;
import com.flemmli97.runecraftory.proxy.CommonProxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = RuneCraftory.MODID, name = RuneCraftory.MODNAME, version = RuneCraftory.VERSION)
public class RuneCraftory {

    public static final String MODID = RFReference.MODID;
    public static final String MODNAME = RFReference.MODNAME;
    public static final String VERSION = RFReference.VERSION;
    public static final Logger logger = LogManager.getLogger(RuneCraftory.MODID);
        
    @Instance
    public static RuneCraftory instance = new RuneCraftory();
        
     
    @SidedProxy(clientSide="com.flemmli97.runecraftory.proxy.ClientProxy", serverSide="com.flemmli97.runecraftory.proxy.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
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
    
	public static CreativeTabs weaponToolTab = new CreativeTabs("runecraftory.weaponsTools") {

		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ModItems.icon,1,0);
		}
	};
	
	public static CreativeTabs upgradeItems = new CreativeTabs("runecraftory.upgrade") {

		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ModItems.icon,1,1);
		}
	};
	
	public static CreativeTabs blocks = new CreativeTabs("runecraftory.blocks") {

		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ModItems.icon,1,2);
		}
	};
	
	public static CreativeTabs cast = new CreativeTabs("runecraftory.cast") {

		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ModItems.icon,1,4);
		}
	};
	
	public static CreativeTabs monsters = new CreativeTabs("runecraftory.monsters") {

		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ModItems.icon,1,3);
		}
	};
}
    