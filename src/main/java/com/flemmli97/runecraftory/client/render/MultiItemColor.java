package com.flemmli97.runecraftory.client.render;

import com.flemmli97.runecraftory.common.init.EntitySpawnEggList;
import com.flemmli97.runecraftory.common.init.EntitySpawnEggList.EntityEggInfo;
import com.flemmli97.runecraftory.common.items.ItemSpawnEgg;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class MultiItemColor implements IItemColor{

	@Override
	public int colorMultiplier(ItemStack stack, int tintIndex) {
		EntityEggInfo eggInfo = (EntitySpawnEggList.EntityEggInfo)EntitySpawnEggList.entityEggs.get(new ResourceLocation(ItemSpawnEgg.getEntityIdFromItem(stack)));
        return eggInfo == null ? -1 : (tintIndex == 0 ? eggInfo.primaryColor : eggInfo.secondaryColor);
	}

}
