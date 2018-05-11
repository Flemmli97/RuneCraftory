package com.flemmli97.runecraftory.common.items;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IModelRegister {

	@SideOnly(Side.CLIENT)
	public void initModel();
}
