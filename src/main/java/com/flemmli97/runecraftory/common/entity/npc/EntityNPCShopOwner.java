package com.flemmli97.runecraftory.common.entity.npc;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityNPCShopOwner extends EntityNPCBase{

	private static final DataParameter<String> SHOPTYPE = EntityDataManager.<String>createKey(EntityAgeable.class, DataSerializers.STRING);

    public EntityNPCShopOwner(World worldIn) {
		super(worldIn);
	}

	public enum EnumShop
	{
		FLOWER,
		GENERAL,
		MAGIC,
		RUNESKILL,
		//FURNITURE,
		WEAPON,
		CLINIC,
		FOOD;
	}
}
