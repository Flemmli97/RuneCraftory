package com.flemmli97.runecraftory.api.mappings;

import com.flemmli97.runecraftory.api.entities.EntityProperties;
import com.flemmli97.runecraftory.api.entities.IEntityBase;
import com.flemmli97.runecraftory.api.items.ItemStatAttributes;
import com.flemmli97.runecraftory.common.entity.monster.*;
import com.flemmli97.runecraftory.common.entity.monster.boss.EntityAmbrosia;
import com.flemmli97.runecraftory.common.entity.monster.boss.EntityThunderbolt;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.utils.MapWrapper;
import com.flemmli97.tenshilib.api.config.SimpleItemStackWrapper;
import com.google.common.collect.Maps;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.init.Blocks;

import java.util.Map;

public class EntityStatMap
{
    private static final Map<Class<? extends IEntityBase>, EntityProperties> entityStatsMap = Maps.newHashMap();
    
    public static void setDefaultStats(Class<? extends IEntityBase> clss, EntityProperties props) {
        entityStatsMap.put(clss, props);
    }
    
    public static EntityProperties getDefaultStats(Class<? extends IEntityBase> clss) {
        return entityStatsMap.get(clss);
    }
    
    static {
        entityStatsMap.put(EntityWooly.class, new EntityProperties(
        		new MapWrapper<IAttribute, Double>()
        			.mapWrapperAdd(SharedMonsterAttributes.MAX_HEALTH, 166.0)
        			.mapWrapperAdd(ItemStatAttributes.RFATTACK, 14.0)
        			.mapWrapperAdd(ItemStatAttributes.RFDEFENCE, 9.0)
        			.mapWrapperAdd(ItemStatAttributes.RFMAGICATT, 12.0)
        			.mapWrapperAdd(ItemStatAttributes.RFMAGICDEF, 9.0), 
        		new MapWrapper<SimpleItemStackWrapper, Float>()
        			.mapWrapperAdd(new SimpleItemStackWrapper(ModItems.furSmall), 0.5f),
        		3, 1, 0.9f,
        		new SimpleItemStackWrapper[] { new SimpleItemStackWrapper(ModItems.furSmall),
        				new SimpleItemStackWrapper(ModItems.furMedium), 
        				new SimpleItemStackWrapper(ModItems.furLarge), 
        				new SimpleItemStackWrapper(ModItems.furball), 
        				new SimpleItemStackWrapper(Blocks.WOOL) }, 
        		new MapWrapper<SimpleItemStackWrapper, Integer>()
        			.mapWrapperAdd(new SimpleItemStackWrapper(ModItems.furSmall), 0)
        			.mapWrapperAdd(new SimpleItemStackWrapper(ModItems.furMedium), 5)
        			.mapWrapperAdd(new SimpleItemStackWrapper(ModItems.furLarge), 10), 
        		true, false));
        
        entityStatsMap.put(EntityAnt.class, new EntityProperties(
        		new MapWrapper<IAttribute, Double>()
        			.mapWrapperAdd(SharedMonsterAttributes.MAX_HEALTH, 112.0)
        			.mapWrapperAdd(ItemStatAttributes.RFATTACK, 13.5)
        			.mapWrapperAdd(ItemStatAttributes.RFDEFENCE, 10.5)
        			.mapWrapperAdd(ItemStatAttributes.RFMAGICATT, 9.0)
        			.mapWrapperAdd(ItemStatAttributes.RFMAGICDEF, 9.2),
        		new MapWrapper<SimpleItemStackWrapper, Float>()
        			.mapWrapperAdd(new SimpleItemStackWrapper(ModItems.carapaceInsect), 0.4f)
        			.mapWrapperAdd(new SimpleItemStackWrapper(ModItems.carapacePretty), 0.1f),
        		2, 1, 0.75f, 
        		new SimpleItemStackWrapper[] { new SimpleItemStackWrapper(ModItems.carapaceInsect) }, 
        		new MapWrapper<SimpleItemStackWrapper, Integer>()
        			.mapWrapperAdd(new SimpleItemStackWrapper(ModItems.carapaceInsect), 0),
        		false, false));
        
        entityStatsMap.put(EntityBeetle.class, new EntityProperties(
        		new MapWrapper<IAttribute, Double>()
        			.mapWrapperAdd(SharedMonsterAttributes.MAX_HEALTH, 125.0)
        			.mapWrapperAdd(ItemStatAttributes.RFATTACK, 18.5)
        			.mapWrapperAdd(ItemStatAttributes.RFDEFENCE, 7.0)
        			.mapWrapperAdd(ItemStatAttributes.RFMAGICATT, 10.0)
        			.mapWrapperAdd(ItemStatAttributes.RFMAGICDEF, 6.0),
        		new MapWrapper<SimpleItemStackWrapper, Float>()
        			.mapWrapperAdd(new SimpleItemStackWrapper(ModItems.hornInsect), 0.6f)
        			.mapWrapperAdd(new SimpleItemStackWrapper(ModItems.carapaceInsect), 0.4f)
        			.mapWrapperAdd(new SimpleItemStackWrapper(ModItems.carapacePretty), 0.1f),
        		5, 1, 0.65f, 
        		new SimpleItemStackWrapper[] { new SimpleItemStackWrapper(ModItems.hornInsect) }, 
        		new MapWrapper<SimpleItemStackWrapper, Integer>()
        			.mapWrapperAdd(new SimpleItemStackWrapper(ModItems.hornInsect), 0),
        		true, false));
        
        entityStatsMap.put(EntityOrc.class, new EntityProperties(
        		new MapWrapper<IAttribute, Double>()
        			.mapWrapperAdd(SharedMonsterAttributes.MAX_HEALTH, 145.0)
        			.mapWrapperAdd(ItemStatAttributes.RFATTACK, 15.5)
        			.mapWrapperAdd(ItemStatAttributes.RFDEFENCE, 10.2)
        			.mapWrapperAdd(ItemStatAttributes.RFMAGICATT, 11.0)
        			.mapWrapperAdd(ItemStatAttributes.RFMAGICDEF, 10.8),
        		new MapWrapper<SimpleItemStackWrapper, Float>()
        			.mapWrapperAdd(new SimpleItemStackWrapper(ModItems.clothCheap), 0.5f)
        			.mapWrapperAdd(new SimpleItemStackWrapper(ModItems.arrowHead), 0.5f)
        			.mapWrapperAdd(new SimpleItemStackWrapper(ModItems.cheapBracelet), 0.3f),
        		5, 1, 0.8f, 
        		new SimpleItemStackWrapper[] { new SimpleItemStackWrapper(ModItems.clothCheap) }, 
        		new MapWrapper<SimpleItemStackWrapper, Integer>(),
        		true, false));

		entityStatsMap.put(EntityOrcArcher.class, new EntityProperties(
				new MapWrapper<IAttribute, Double>()
						.mapWrapperAdd(SharedMonsterAttributes.MAX_HEALTH, 145.0)
						.mapWrapperAdd(ItemStatAttributes.RFATTACK, 15.5)
						.mapWrapperAdd(ItemStatAttributes.RFDEFENCE, 10.2)
						.mapWrapperAdd(ItemStatAttributes.RFMAGICATT, 11.0)
						.mapWrapperAdd(ItemStatAttributes.RFMAGICDEF, 10.8),
				new MapWrapper<SimpleItemStackWrapper, Float>()
						.mapWrapperAdd(new SimpleItemStackWrapper(ModItems.clothCheap), 0.5f)
						.mapWrapperAdd(new SimpleItemStackWrapper(ModItems.arrowHead), 0.5f)
						.mapWrapperAdd(new SimpleItemStackWrapper(ModItems.cheapBracelet), 0.3f),
				5, 1, 0.8f,
				new SimpleItemStackWrapper[] { new SimpleItemStackWrapper(ModItems.clothCheap) },
				new MapWrapper<SimpleItemStackWrapper, Integer>(),
				true, false));

		entityStatsMap.put(EntityCluckadoodle.class, new EntityProperties(
				new MapWrapper<IAttribute, Double>()
						.mapWrapperAdd(SharedMonsterAttributes.MAX_HEALTH, 145.0)
						.mapWrapperAdd(ItemStatAttributes.RFATTACK, 15.5)
						.mapWrapperAdd(ItemStatAttributes.RFDEFENCE, 10.2)
						.mapWrapperAdd(ItemStatAttributes.RFMAGICATT, 11.0)
						.mapWrapperAdd(ItemStatAttributes.RFMAGICDEF, 10.8),
				new MapWrapper<SimpleItemStackWrapper, Float>()
						.mapWrapperAdd(new SimpleItemStackWrapper(ModItems.clothCheap), 0.5f)
						.mapWrapperAdd(new SimpleItemStackWrapper(ModItems.arrowHead), 0.5f)
						.mapWrapperAdd(new SimpleItemStackWrapper(ModItems.cheapBracelet), 0.3f),
				5, 1, 0.8f,
				new SimpleItemStackWrapper[] { new SimpleItemStackWrapper(ModItems.clothCheap) },
				new MapWrapper<SimpleItemStackWrapper, Integer>(),
				true, false));

		entityStatsMap.put(EntityBuffamoo.class, new EntityProperties(
				new MapWrapper<IAttribute, Double>()
						.mapWrapperAdd(SharedMonsterAttributes.MAX_HEALTH, 145.0)
						.mapWrapperAdd(ItemStatAttributes.RFATTACK, 15.5)
						.mapWrapperAdd(ItemStatAttributes.RFDEFENCE, 10.2)
						.mapWrapperAdd(ItemStatAttributes.RFMAGICATT, 11.0)
						.mapWrapperAdd(ItemStatAttributes.RFMAGICDEF, 10.8),
				new MapWrapper<SimpleItemStackWrapper, Float>()
						.mapWrapperAdd(new SimpleItemStackWrapper(ModItems.clothCheap), 0.5f)
						.mapWrapperAdd(new SimpleItemStackWrapper(ModItems.arrowHead), 0.5f)
						.mapWrapperAdd(new SimpleItemStackWrapper(ModItems.cheapBracelet), 0.3f),
				5, 1, 0.8f,
				new SimpleItemStackWrapper[] { new SimpleItemStackWrapper(ModItems.clothCheap) },
				new MapWrapper<SimpleItemStackWrapper, Integer>(),
				true, false));

		entityStatsMap.put(EntityChipsqueek.class, new EntityProperties(
				new MapWrapper<IAttribute, Double>()
						.mapWrapperAdd(SharedMonsterAttributes.MAX_HEALTH, 145.0)
						.mapWrapperAdd(ItemStatAttributes.RFATTACK, 15.5)
						.mapWrapperAdd(ItemStatAttributes.RFDEFENCE, 10.2)
						.mapWrapperAdd(ItemStatAttributes.RFMAGICATT, 11.0)
						.mapWrapperAdd(ItemStatAttributes.RFMAGICDEF, 10.8),
				new MapWrapper<SimpleItemStackWrapper, Float>()
						.mapWrapperAdd(new SimpleItemStackWrapper(ModItems.clothCheap), 0.5f)
						.mapWrapperAdd(new SimpleItemStackWrapper(ModItems.arrowHead), 0.5f)
						.mapWrapperAdd(new SimpleItemStackWrapper(ModItems.cheapBracelet), 0.3f),
				5, 1, 0.8f,
				new SimpleItemStackWrapper[] { new SimpleItemStackWrapper(ModItems.clothCheap) },
				new MapWrapper<SimpleItemStackWrapper, Integer>(),
				true, false));

		entityStatsMap.put(EntityPommePomme.class, new EntityProperties(
				new MapWrapper<IAttribute, Double>()
						.mapWrapperAdd(SharedMonsterAttributes.MAX_HEALTH, 145.0)
						.mapWrapperAdd(ItemStatAttributes.RFATTACK, 15.5)
						.mapWrapperAdd(ItemStatAttributes.RFDEFENCE, 10.2)
						.mapWrapperAdd(ItemStatAttributes.RFMAGICATT, 11.0)
						.mapWrapperAdd(ItemStatAttributes.RFMAGICDEF, 10.8),
				new MapWrapper<SimpleItemStackWrapper, Float>()
						.mapWrapperAdd(new SimpleItemStackWrapper(ModItems.clothCheap), 0.5f)
						.mapWrapperAdd(new SimpleItemStackWrapper(ModItems.arrowHead), 0.5f)
						.mapWrapperAdd(new SimpleItemStackWrapper(ModItems.cheapBracelet), 0.3f),
				5, 1, 0.8f,
				new SimpleItemStackWrapper[] { new SimpleItemStackWrapper(ModItems.clothCheap) },
				new MapWrapper<SimpleItemStackWrapper, Integer>(),
				true, false));

		entityStatsMap.put(EntityBigMuck.class, new EntityProperties(
				new MapWrapper<IAttribute, Double>()
						.mapWrapperAdd(SharedMonsterAttributes.MAX_HEALTH, 145.0)
						.mapWrapperAdd(ItemStatAttributes.RFATTACK, 15.5)
						.mapWrapperAdd(ItemStatAttributes.RFDEFENCE, 10.2)
						.mapWrapperAdd(ItemStatAttributes.RFMAGICATT, 11.0)
						.mapWrapperAdd(ItemStatAttributes.RFMAGICDEF, 10.8),
				new MapWrapper<SimpleItemStackWrapper, Float>()
						.mapWrapperAdd(new SimpleItemStackWrapper(ModItems.clothCheap), 0.5f)
						.mapWrapperAdd(new SimpleItemStackWrapper(ModItems.arrowHead), 0.5f)
						.mapWrapperAdd(new SimpleItemStackWrapper(ModItems.cheapBracelet), 0.3f),
				5, 1, 0.8f,
				new SimpleItemStackWrapper[] { new SimpleItemStackWrapper(ModItems.clothCheap) },
				new MapWrapper<SimpleItemStackWrapper, Integer>(),
				true, false));
        
        entityStatsMap.put(EntityAmbrosia.class, new EntityProperties(
        		new MapWrapper<IAttribute, Double>()
        			.mapWrapperAdd(SharedMonsterAttributes.MAX_HEALTH, 186.0)
        			.mapWrapperAdd(ItemStatAttributes.RFATTACK, 21.5)
        			.mapWrapperAdd(ItemStatAttributes.RFDEFENCE, 13.5)
        			.mapWrapperAdd(ItemStatAttributes.RFMAGICATT, 18.3)
        			.mapWrapperAdd(ItemStatAttributes.RFMAGICDEF, 14.2),
        		new MapWrapper<SimpleItemStackWrapper, Float>()
        			.mapWrapperAdd(new SimpleItemStackWrapper(ModItems.ambrosiasThorns), 0.5f),
        		10, 12, 0.35f, 
        		new SimpleItemStackWrapper[0], 
        		new MapWrapper<SimpleItemStackWrapper, Integer>(),
        		true, true));
        
        entityStatsMap.put(EntityThunderbolt.class, new EntityProperties(
        		new MapWrapper<IAttribute, Double>()
        			.mapWrapperAdd(SharedMonsterAttributes.MAX_HEALTH, 186.0)
        			.mapWrapperAdd(ItemStatAttributes.RFATTACK, 23.5)
        			.mapWrapperAdd(ItemStatAttributes.RFDEFENCE, 16.5)
        			.mapWrapperAdd(ItemStatAttributes.RFMAGICATT, 14.3)
        			.mapWrapperAdd(ItemStatAttributes.RFMAGICDEF, 13.2),
        		new MapWrapper<SimpleItemStackWrapper, Float>()
        			.mapWrapperAdd(new SimpleItemStackWrapper(ModItems.lightningMane), 0.5f),
        		13, 11, 0.25f, 
        		new SimpleItemStackWrapper[0], 
        		new MapWrapper<SimpleItemStackWrapper, Integer>(),
        		true, false));
    }
}
