package com.flemmli97.runecraftory.common.init;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.client.render.RenderGate;
import com.flemmli97.runecraftory.client.render.monsters.RenderAnt;
import com.flemmli97.runecraftory.client.render.monsters.RenderOrc;
import com.flemmli97.runecraftory.client.render.monsters.RenderWooly;
import com.flemmli97.runecraftory.common.entity.EntityGate;
import com.flemmli97.runecraftory.common.entity.starter.EntityAnt;
import com.flemmli97.runecraftory.common.entity.starter.EntityOrc;
import com.flemmli97.runecraftory.common.entity.starter.EntityWooly;
import com.flemmli97.runecraftory.common.lib.RFReference;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModEntities {

	public static void init() 
	{
		EntityRegistry.registerModEntity(new ResourceLocation(RFReference.MODID, "gate"), EntityGate.class, "gate", 0, RuneCraftory.instance, 64, 3, true);

		EntityRegistry.registerModEntity(new ResourceLocation(RFReference.MODID, "wooly"), EntityWooly.class, "wooly", 1, RuneCraftory.instance, 64, 3, true);
		EntityRegistry.registerModEntity(new ResourceLocation(RFReference.MODID, "orc"), EntityOrc.class, "orc", 2, RuneCraftory.instance, 64, 3, true);
		EntityRegistry.registerModEntity(new ResourceLocation(RFReference.MODID, "ant"), EntityAnt.class, "ant", 3, RuneCraftory.instance, 64, 3, true);
	}
	
	public static void registerMobSpawn()
	{
		for(Biome biome : Biome.REGISTRY)
			EntityRegistry.addSpawn(EntityGate.class, 50, 1, 2, EnumCreatureType.MONSTER, biome);
		RuneCraftory.logger.info("Registering gate spawns");
		GateSpawning.addToBiomeType(EntityWooly.class,  Type.PLAINS, Type.BEACH, Type.CONIFEROUS, Type.FOREST, Type.HILLS, Type.MAGICAL, Type.MOUNTAIN, Type.SAVANNA);
		GateSpawning.addToBiomeType(EntityOrc.class, Type.PLAINS, Type.BEACH, Type.CONIFEROUS, Type.FOREST, Type.HILLS, Type.MAGICAL, Type.MOUNTAIN, Type.SAVANNA);
		GateSpawning.addToBiomeType(EntityAnt.class, Type.PLAINS, Type.CONIFEROUS, Type.FOREST, Type.HILLS, Type.SAVANNA, Type.LUSH);
		RuneCraftory.logger.info("Finished registering gate spawns");
	}
	
	@SideOnly(value=Side.CLIENT)
	public static void registerRenders()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityGate.class, RenderGate::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityWooly.class, RenderWooly::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityOrc.class, RenderOrc::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityAnt.class, RenderAnt::new);

	}
}
