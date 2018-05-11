package com.flemmli97.runecraftory.common.core.handler.config;

import com.flemmli97.runecraftory.api.items.CropProperties;
import com.flemmli97.runecraftory.common.core.handler.time.CalendarHandler.EnumSeason;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.init.defaultval.CropMap;
import com.flemmli97.runecraftory.common.items.food.ItemCrops;

import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

public class CropConfig {

	protected static void init(Configuration config) {
		config.load();
		cropData(config);
		config.save();
	}

	private static void cropData(Configuration config) {
		for(Item item : ModItems.CROPS)
		{
			String id = ((ItemCrops)item).cropSpecificOreDict();
			CropProperties prop = CropMap.getProperties( id );
			if(prop!=null)
			{
				CropProperties fromConfig = new CropProperties(
						EnumSeason.fromString(config.getString("season", id, prop.bestSeason().formattingText(), "The Season where this crop grows best")),
						ConfigHandler.getIntConfig(config, "growth", id, prop.growth(), "Time in days it take for this crop to grow"),
						ConfigHandler.getIntConfig(config, "drops", id, prop.maxDrops(), "Amount of drops per harvest"));
				CropMap.overwriteProps(id, fromConfig);
			}
		}
	}
}
