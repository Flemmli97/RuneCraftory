package com.flemmli97.runecraftory.common.init;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.client.render.RenderGate;
import com.flemmli97.runecraftory.client.render.monsters.RenderAmbrosia;
import com.flemmli97.runecraftory.client.render.monsters.RenderAnt;
import com.flemmli97.runecraftory.client.render.monsters.RenderBeetle;
import com.flemmli97.runecraftory.client.render.monsters.RenderOrc;
import com.flemmli97.runecraftory.client.render.monsters.RenderWooly;
import com.flemmli97.runecraftory.common.entity.EntityGate;
import com.flemmli97.runecraftory.common.entity.monster.EntityAnt;
import com.flemmli97.runecraftory.common.entity.monster.EntityBeetle;
import com.flemmli97.runecraftory.common.entity.monster.EntityOrc;
import com.flemmli97.runecraftory.common.entity.monster.EntityWooly;
import com.flemmli97.runecraftory.common.entity.monster.boss.EntityAmbrosia;
import com.flemmli97.runecraftory.common.entity.monster.projectile.EntityAmbrosiaSleep;
import com.flemmli97.runecraftory.common.entity.monster.projectile.EntityAmbrosiaWave;
import com.flemmli97.runecraftory.common.entity.monster.projectile.EntityButterfly;
import com.flemmli97.runecraftory.common.lib.LibReference;

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
		int id=0;
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "gate"), EntityGate.class, "gate", id++, RuneCraftory.instance, 64, 3, true);

		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "wooly"), EntityWooly.class, "wooly", id++, RuneCraftory.instance, 64, 3, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "orc"), EntityOrc.class, "orc", id++, RuneCraftory.instance, 64, 3, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "ant"), EntityAnt.class, "ant", id++, RuneCraftory.instance, 64, 3, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "beetle"), EntityBeetle.class, "beetle", id++, RuneCraftory.instance, 64, 3, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "ambrosia"), EntityAmbrosia.class, "ambrosia", id++, RuneCraftory.instance, 64, 3, true);

		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "butterfly"), EntityButterfly.class, "butterfly", id++, RuneCraftory.instance, 64, 3, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "ambrosiaSleep"), EntityAmbrosiaSleep.class, "ambrosiaSleep", id++, RuneCraftory.instance, 64, 3, true);
		EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "ambrosiaWave"), EntityAmbrosiaWave.class, "ambrosiaWave", id++, RuneCraftory.instance, 64, 3, true);

	}
	
	public static void registerMobSpawn()
	{
		for(Biome biome : Biome.REGISTRY)
			EntityRegistry.addSpawn(EntityGate.class, 50, 1, 2, EnumCreatureType.MONSTER, biome);
		RuneCraftory.logger.info("Registering gate spawns");
		GateSpawning.addToBiomeType(EntityWooly.class,  Type.PLAINS, Type.BEACH, Type.CONIFEROUS, Type.FOREST, Type.HILLS, Type.MAGICAL, Type.MOUNTAIN, Type.SAVANNA);
		GateSpawning.addToBiomeType(EntityOrc.class, Type.PLAINS, Type.BEACH, Type.CONIFEROUS, Type.FOREST, Type.HILLS, Type.MAGICAL, Type.MOUNTAIN, Type.SAVANNA);
		GateSpawning.addToBiomeType(EntityAnt.class, Type.PLAINS, Type.CONIFEROUS, Type.FOREST, Type.HILLS, Type.SAVANNA, Type.LUSH);
		GateSpawning.addToBiomeType(EntityBeetle.class, Type.PLAINS, Type.FOREST, Type.HILLS, Type.LUSH);
		RuneCraftory.logger.info("Finished registering gate spawns");
	}
	
	@SideOnly(value=Side.CLIENT)
	public static void registerRenders()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityGate.class, RenderGate::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityWooly.class, RenderWooly::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityOrc.class, RenderOrc::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityAnt.class, RenderAnt::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBeetle.class, RenderBeetle::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityAmbrosia.class, RenderAmbrosia::new);

	}
}
