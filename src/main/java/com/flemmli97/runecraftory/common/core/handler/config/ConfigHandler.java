package com.flemmli97.runecraftory.common.core.handler.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.flemmli97.runecraftory.api.entities.EntityProperties;
import com.flemmli97.runecraftory.api.entities.IEntityBase;
import com.flemmli97.runecraftory.api.items.CropProperties;
import com.flemmli97.runecraftory.api.items.FoodProperties;
import com.flemmli97.runecraftory.api.items.ItemStat;
import com.flemmli97.runecraftory.api.items.ItemStatAttributes;
import com.flemmli97.runecraftory.api.mappings.CropMap;
import com.flemmli97.runecraftory.api.mappings.EntityStatMap;
import com.flemmli97.runecraftory.api.mappings.ItemFoodMap;
import com.flemmli97.runecraftory.api.mappings.ItemStatMap;
import com.flemmli97.runecraftory.api.mappings.Quests;
import com.flemmli97.runecraftory.common.init.GateSpawning;
import com.flemmli97.runecraftory.common.init.ModBlocks;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.utils.MapWrapper;
import com.flemmli97.runecraftory.common.world.HerbGenerator;
import com.flemmli97.runecraftory.common.world.HerbGenerator.HerbEntry;
import com.flemmli97.tenshilib.api.config.ExtendedItemStackWrapper;
import com.flemmli97.tenshilib.api.config.SimpleItemStackWrapper;
import com.flemmli97.tenshilib.common.config.ConfigUtils;
import com.flemmli97.tenshilib.common.config.ConfigUtils.LoadState;
import com.flemmli97.tenshilib.common.config.JsonConfig;
import com.flemmli97.tenshilib.common.javahelper.ResourceStream;
import com.flemmli97.tenshilib.common.world.structure.GenerationType;
import com.flemmli97.tenshilib.common.world.structure.LocationType;
import com.flemmli97.tenshilib.common.world.structure.Structure;
import com.flemmli97.tenshilib.common.world.structure.StructureGenerator;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

public class ConfigHandler {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping()
    		.registerTypeHierarchyAdapter(IAttribute.class, new JsonAdapaters.AttributeAdapter())
    		.registerTypeAdapter(RandomValueRange.class, new RandomValueRange.Serializer())
			.registerTypeHierarchyAdapter(LootFunction.class, new LootFunctionManager.Serializer())
			.registerTypeAdapter(ExtendedItemStackWrapper.class, new ExtendedItemStackWrapper.Serializer())
			.registerTypeAdapter(ResourceLocation.class, new JsonAdapaters.ResourceLocAdapter())
    		.enableComplexMapKeySerialization().serializeNulls().create();

	public static Configuration mainConfig;
	public static Configuration mobConfig;
	public static JsonConfig<JsonObject> cropConfig;
	public static JsonConfig<JsonObject> spawnConfig;
	public static Configuration generationConfig;
	public static JsonConfig<JsonObject> itemStatConfig;
	public static JsonConfig<JsonObject> foodStatConfig;
	public static JsonConfig<JsonObject> shopConfig;
	public static JsonConfig<JsonObject> questConfig;

	private static File defFolder;

	public static void load(LoadState loadState)
	{
		
		File configDir = new File(LibReference.modDir + "/" + LibReference.MODID + "/");
		if(defFolder==null)
			defFolder=new File(configDir + "/default/");
		if(!defFolder.exists())
			defFolder.mkdirs();
		File def = new File(defFolder, "Doc.txt");
		copyDefaultConfigs(def, "Doc.txt");
		MainConfig.load(configDir);
		if(loadState==LoadState.POSTINIT || loadState==LoadState.SYNC)
			MobConfigs.load(configDir);
		if(loadState==LoadState.POSTINIT)
			CropConfig.load(configDir);
		if(loadState==LoadState.POSTINIT)
			SpawnConfig.load(configDir);
		if(loadState==LoadState.POSTINIT || loadState==LoadState.SYNC)
			GenerationConfig.load(configDir, loadState==LoadState.POSTINIT);
		if(loadState==LoadState.POSTINIT)
			ItemStatsConfig.load(configDir);
		if(loadState==LoadState.POSTINIT)
			FoodConfig.load(configDir);
		if(loadState==LoadState.POSTINIT)
			ShopConfig.load(configDir);
		if(loadState==LoadState.POSTINIT)
			QuestConfig.load(configDir);
		test(configDir);
	}
		
	public static class MainConfig
	{
		public static boolean debugMode;
		public static boolean mobs;
		public static boolean crops;
		public static boolean rp;
		
		public static float xpMultiplier;
		public static float skillXpMultiplier;
		
		public static float tamingMultiplier;
		public static float levelIncreaseMultiplier;
		public static float dropRateMultiplier;
		
		public static int mineralRarity;
	    
		public static boolean waila=true;
		public static boolean jei=true;
		//public static boolean harvestCraft=true;
		public static boolean seasons=true;
		public static boolean dynamicTrees=true;
		
		private static void load(File configDir) {
			if(mainConfig==null)
			{
				mainConfig = new Configuration(new File(configDir, "main.cfg"));
				mainConfig.load();
			}
	        debugMode = mainConfig.getBoolean("Debug Mode", "general", false, "");
	        mobs = mainConfig.getBoolean("Enable entity module", "general", true, "Enables/Disable entities in general from this mod. You will not be able to get drops normally if you disable this");
	        
	        ConfigCategory cat = mainConfig.getCategory("integration");
    		cat.setRequiresMcRestart(true);
	        waila = mainConfig.getBoolean("Waila", "integration", waila, "Waila integration");
	        jei = mainConfig.getBoolean("JEI", "integration", jei, "JEI integration");
	  		//harvestCraft = mainConfig.getBoolean("HarvestCraft", "integration", harvestCraft, "HarvestCraft integration");
	  		seasons = mainConfig.getBoolean("SereneSeason", "integration", seasons, "SereneSeason integration"); 
	  		mainConfig.save();
	    }
	}
	
	public static class MobConfigs
	{
		public static float gateDef=5f;
	    public static float gateMDef=5f;
	    public static float gateHealth=100;
	    public static int maxTimeout=150;
	    private static void load(File configDir)
	    {
	    	if(mobConfig==null)
			{
				mobConfig = new Configuration(new File(configDir, "mobs.cfg"));
				mobConfig.load();
			}
	    	ArrayList<String> validStatValues = new ArrayList<String>();
	        validStatValues.add(SharedMonsterAttributes.MAX_HEALTH.getName());
	        validStatValues.addAll(ItemStatAttributes.ATTRIBUTESTRINGMAP.keySet());
	        int i = 0;
	        String values = Configuration.NEW_LINE + "<";
	        for (String s : validStatValues) 
	        {
	            if (++i == 3) 
	            {
	                values = values + s + ">" + Configuration.NEW_LINE + "<";
	                i = 0;
	            }
	            else 
	            {
	                values = values + s + "> <";
	            }
	        }
	        values = values.substring(0, values.length() - 2);
	        mobConfig.addCustomCategoryComment("mobs", "Valid attribute names are:" + values);
	        mobData(mobConfig);
	        gateDef = ConfigUtils.getFloatConfig(mobConfig, "GateDef", "mobs.runecraftory:gate", gateDef, "Base health for gates");
	        gateMDef = ConfigUtils.getFloatConfig(mobConfig, "GateMDef", "mobs.runecraftory:gate", gateMDef, "Base defence for gates");
	        gateHealth = ConfigUtils.getFloatConfig(mobConfig, "GateHealth", "mobs.runecraftory:gate", gateHealth, "Base magic defence for gates");
	        maxTimeout = ConfigUtils.getIntConfig(mobConfig, "GateTimeout", "mobs.runecraftory:gate", maxTimeout, 0, "Max time till next wave of spawning");
	        mobConfig.save();
	    }
	    
	    @SuppressWarnings("unchecked")
		private static void mobData(Configuration config) {
	    	for (EntityEntry entry : ForgeRegistries.ENTITIES.getValuesCollection()) 
	        {
	            if (IEntityBase.class.isAssignableFrom(entry.getEntityClass())) 
	            {
	            	EntityProperties prop = EntityStatMap.getDefaultStats((Class<? extends IEntityBase>) entry.getEntityClass());
        			if(prop!=null)
        				prop.config(config, "mobs."+entry.getRegistryName().toString());
	            }
	        }
	    }
	    
	}
		
	public static class CropConfig
	{
		private static void load(File configDir)
	    {
			File def = new File(defFolder, "DefaultCrops.json");
			copyDefaultConfigs(def, "DefaultCrops.json");
			if(cropConfig==null)
				cropConfig = new JsonConfig<JsonObject>(new File(configDir, "crops.json"), JsonObject.class, def).setName("crops").setGson(GSON);
			LibReference.logger.info("Configuring crops");
			JsonObject obj = cropConfig.getElement();
			if (obj != null && !obj.isJsonNull()) 
            {
				Map<String, JsonElement> res = Maps.newHashMap();
				obj.entrySet().forEach(entry->
				{
					String string = entry.getKey();
		            if (string.contains(":"))
		            	res.put(string, entry.getValue());
		            else
		            	OreDictionary.getOres(string).forEach(stack->{
		                	CropMap.addProperties(new SimpleItemStackWrapper(stack).setIgnoreAmount(), GSON.fromJson(entry.getValue(), CropProperties.class));
		            	});
				});
				res.forEach((string, el)->{
                	CropMap.addProperties(new SimpleItemStackWrapper(string).setIgnoreAmount(), GSON.fromJson(el, CropProperties.class));
				});
            }
		    
	        LibReference.logger.info("Configured {} crops", CropMap.propCount());
			cropConfig.save();
	    }
	}
	
	public static class SpawnConfig
	{
		private static void load(File configDir)
	    {
			File def = new File(defFolder, "DefaultSpawns.json");
			copyDefaultConfigs(def, "DefaultSpawns.json");
			if(spawnConfig==null)
				spawnConfig = new JsonConfig<JsonObject>(new File(configDir, "mob_spawns.json"), JsonObject.class, def).setGson(GSON);
	        LibReference.logger.info("Configuring gate spawns");
			JsonObject obj = spawnConfig.getElement();
			if (obj != null && !obj.isJsonNull()) 
            {
				obj.entrySet().forEach(entry->{
					ResourceLocation s = new ResourceLocation(entry.getKey());
					JsonObject obj2 = (JsonObject) entry.getValue();
					JsonArray biomes = obj2.getAsJsonArray("biomes");
					JsonArray biomeTypes = obj2.getAsJsonArray("biomeTypes");
					List<String> biomeList = Lists.newArrayList();
					List<String> biomeTypeList = Lists.newArrayList();
					if(biomes!=null)
						biomes.forEach(e->{
							biomeList.add(e.getAsString());
						});
					if(biomeTypes!=null)
						biomeTypes.forEach(e->{
							biomeTypeList.add(e.getAsString());
						});
					GateSpawning.addToBiome(s, biomeList);
					GateSpawning.addToBiomeType(s, biomeTypeList);
				});
            }
	        LibReference.logger.info("Finished configuring gate spawns");
	        spawnConfig.save();
	    }
	}
	
	public static class GenerationConfig
	{
		public static int mineralRate = 25;

		public static Structure ambrosiaForest=new Structure(new ResourceLocation(LibReference.MODID, "ambrosia_forest"), null, 400, 100, 1, LocationType.GROUND, GenerationType.REPLACEBELOW,new int[] {0}, -4, true).addBiomeType(Sets.newHashSet(Type.FOREST));
		public static Structure thunderboltRuins=new Structure(new ResourceLocation(LibReference.MODID, "thunderbolt_ruins"), null, 800, 150, 1, LocationType.GROUND, GenerationType.FLOATING,new int[] {0}, -2, true).addBiome(Sets.newHashSet(Biomes.DEEP_OCEAN));
	    
		private static final Map<Block,String[]> defaultVals = new MapWrapper<Block,String[]>()
				.mapWrapperAdd(ModBlocks.mushroom, new String[] {})
				.mapWrapperAdd(ModBlocks.monarchMushroom, new String[] {})
				.mapWrapperAdd(ModBlocks.weeds, new String[] {})
				.mapWrapperAdd(ModBlocks.elliLeaves, new String[] {})
				.mapWrapperAdd(ModBlocks.witheredGrass, new String[] {"PLAINS,25,4,2"})
				.mapWrapperAdd(ModBlocks.weeds, new String[] {"PLAINS,30,6,5"})
				.mapWrapperAdd(ModBlocks.whiteGrass, new String[] {"SNOWY,10,4,3"})
				.mapWrapperAdd(ModBlocks.indigoGrass, new String[] {})
				.mapWrapperAdd(ModBlocks.purpleGrass, new String[] {})
				.mapWrapperAdd(ModBlocks.greenGrass, new String[] {})
				.mapWrapperAdd(ModBlocks.blueGrass, new String[] {})
				.mapWrapperAdd(ModBlocks.yellowGrass, new String[] {})
				.mapWrapperAdd(ModBlocks.redGrass, new String[] {})
				.mapWrapperAdd(ModBlocks.orangeGrass, new String[] {})
				.mapWrapperAdd(ModBlocks.blackGrass, new String[] {})
				.mapWrapperAdd(ModBlocks.antidoteGrass, new String[] {})
				.mapWrapperAdd(ModBlocks.medicinalHerb, new String[] {})
				.mapWrapperAdd(ModBlocks.bambooSprout, new String[] {});

		private static void load(File configDir, boolean register)
	    {
			if(generationConfig==null)
			{
				generationConfig = new Configuration(new File(configDir, "generation.cfg"));
				generationConfig.load();
			}
			if(register)
			{
				StructureGenerator.registerStructure(ambrosiaForest);
				StructureGenerator.registerStructure(thunderboltRuins);
			}
			mineralRate = generationConfig.get("structures", "Mineral Rate", mineralRate, "prop").getInt();
			ambrosiaForest.config(generationConfig, "structures");
			thunderboltRuins.config(generationConfig, "structures");
			HerbGenerator.clear();
			defaultVals.forEach((block,strArr)->
			{
				for(String s : generationConfig.getStringList(block.getRegistryName().toString(), "herbs", strArr, "Syntax: (BIOME/BIOME TYPE,weight,maxTries,maxAmount"))
				{
					String[] sub = s.split(",");
					if(sub.length<4)
						throw new ConfigError("Couldnt parse string " + s);
					Biome biome = Biome.REGISTRY.getObject(new ResourceLocation(sub[0]));
					HerbEntry herb = new HerbEntry(block, Integer.parseInt(sub[1]), Integer.parseInt(sub[2]), Integer.parseInt(sub[3]));
					if(biome!=null)
						HerbGenerator.add(biome, herb);
					else
					{
						BiomeDictionary.Type type = BiomeDictionary.Type.getType(sub[0]);
						for(Biome biome2 : BiomeDictionary.getBiomes(type))
							HerbGenerator.add(biome2, herb);
					}
				}
			});
			generationConfig.save();
	    }
	}
	
	public static class ItemStatsConfig
	{
		private static void load(File configDir)
	    {
			File def = new File(defFolder, "DefaultItemStats.json");
			copyDefaultConfigs(def, "DefaultItemStats.json");
			if(itemStatConfig==null)
				itemStatConfig = new JsonConfig<JsonObject>(new File(configDir, "item_stats.json"), JsonObject.class, def).setGson(GSON);
			LibReference.logger.info("Configuring itemstats");
			JsonObject obj = itemStatConfig.getElement();
			if (obj != null && !obj.isJsonNull()) 
            {
				Map<String, JsonElement> res = Maps.newHashMap();
				obj.entrySet().forEach(entry->{
					String string = entry.getKey();
					if(string.contains(":"))
						res.put(string, entry.getValue());
					else
					{
						OreDictionary.getOres(string).forEach(stack->{
							ItemStatMap.add(new SimpleItemStackWrapper(stack).setIgnoreAmount(), GSON.fromJson(entry.getValue(), ItemStat.class));
						});
					}
				});
				res.forEach((string, el)->{
					ItemStatMap.add(new SimpleItemStackWrapper(string).setIgnoreAmount(), GSON.fromJson(el, ItemStat.class));
				});
            }
	        LibReference.logger.info("Configured stats for {} items", ItemStatMap.configuredItems());
	        itemStatConfig.save();
	    }
	}
	
	public static class FoodConfig
	{
		private static void load(File configDir)
	    {
			File def = new File(defFolder, "DefaultFoodStats.json");
			copyDefaultConfigs(def, "DefaultFoodStats.json");
			if(foodStatConfig==null)
				foodStatConfig = new JsonConfig<JsonObject>(new File(configDir, "food_stats.json"), JsonObject.class, def).setGson(GSON);
			LibReference.logger.info("Configuring foodstats");
			JsonObject obj = foodStatConfig.getElement();
			if (obj != null) 
            {
				Map<String, JsonElement> res = Maps.newHashMap();
				obj.entrySet().forEach(entry->
				{
					String string = entry.getKey();
					if(string.contains(":"))
						res.put(string, entry.getValue());
					else
						OreDictionary.getOres(string).forEach(stack->{
							ItemFoodMap.add(new SimpleItemStackWrapper(stack).setIgnoreAmount(), GSON.fromJson(entry.getValue(), FoodProperties.class));
						});
				});
				res.forEach((string, el)->
				{
					ItemFoodMap.add(new SimpleItemStackWrapper(string).setIgnoreAmount(), GSON.fromJson(el, FoodProperties.class));
				});
            }
	        LibReference.logger.info("Configured stats for {} food items", ItemFoodMap.configuredFood());
	        foodStatConfig.save();
	    }
	}
	
	public static class ShopConfig
	{
		private static void load(File configDir)
	    {
			File def = new File(defFolder, "DefaultShopItems.json");
			copyDefaultConfigs(def, "DefaultShopItems.json");
			if(shopConfig==null)
				shopConfig = new JsonConfig<JsonObject>(new File(configDir, "shop_items.json"), JsonObject.class, def).setGson(GSON);
			shopConfig.save();
	    }
	}
	
	public static class QuestConfig
	{
		private static void load(File configDir)
	    {
			File def = new File(defFolder, "DefaultQuests.json");
			copyDefaultConfigs(def, "DefaultQuests.json");
			if(questConfig==null)
				questConfig = new JsonConfig<JsonObject>(new File(configDir, "quests.json"), JsonObject.class, def).setGson(GSON);
			LibReference.logger.info("Loading quests");
			JsonObject obj = questConfig.getElement();
			Quests.loadQuests(GSON, obj);
			questConfig.save();
	    }
	}
	
	public static void copyDefaultConfigs(File dest, File source)
	{
		if(!dest.exists())
		{
			try 
			{
				dest.createNewFile();
				OutputStream out = new FileOutputStream(source);
				InputStream in = new FileInputStream(dest);
				IOUtils.copy(in, out);
				in.close();
				out.close();
				Files.copy(source, dest);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void copyDefaultConfigs(File dest, String conf)
	{
		try 
		{
			if(!dest.exists())
				dest.createNewFile();
			OutputStream out = new FileOutputStream(dest);
			InputStream in = ResourceStream.getStream(LibReference.MODID, "configs", conf);
			IOUtils.copy(in, out);
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	private static void test(File configDir)
	{
		try
		{
			/*File def = new File(defFolder, "DefaultFoodStats.json");
			copyDefaultConfigs(def, "DefaultFoodStats.json");
			if(foodStatConfig==null)
				foodStatConfig = new JsonConfig<JsonObject>(new File(configDir, "food_stats.json"), JsonObject.class, def).setGson(GSON);
			LibReference.logger.info("Configuring foodstats");
			JsonObject obj = foodStatConfig.getElement();
			if (obj != null) 
            {
				Map<String, JsonElement> res = Maps.newHashMap();
				obj.entrySet().forEach(entry->
				{
					String string = entry.getKey();
					if(string.contains(":"))
						res.put(string, entry.getValue());
					else
						OreDictionary.getOres(string).forEach(stack->{
							ItemFoodMap.add(new SimpleItemStackWrapper(stack).setIgnoreAmount(), GSON.fromJson(entry.getValue(), FoodProperties.class));
						});
				});
				res.forEach((string, el)->
				{
					ItemFoodMap.add(new SimpleItemStackWrapper(string).setIgnoreAmount(), GSON.fromJson(el, FoodProperties.class));
				});
            }
	        LibReference.logger.info("Configured stats for {} food items", ItemFoodMap.configuredFood());
	        foodStatConfig.save();*/
		}
		catch(Exception e)
		{
			
		}
	}
	
	public static class ConfigError extends RuntimeException
	{
		private static final long serialVersionUID = 1L;

		public ConfigError(String message) {
	        super(message);
	    }
	}
}
