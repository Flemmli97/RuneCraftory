package com.flemmli97.runecraftory.client.render.item;

import com.flemmli97.runecraftory.common.init.EntitySpawnEggList;
import com.flemmli97.runecraftory.common.items.creative.ItemSpawnEgg;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.EntityList.EntityEggInfo;
import net.minecraft.item.ItemStack;

public class MultiItemColor implements IItemColor{

	@Override
	public int colorMultiplier(ItemStack stack, int tintIndex) {
		EntityEggInfo eggInfo = EntitySpawnEggList.get(ItemSpawnEgg.getEntityIdFromItem(stack));
        return eggInfo == null ? -1 : (tintIndex == 0 ? eggInfo.primaryColor : eggInfo.secondaryColor);
	}

}
