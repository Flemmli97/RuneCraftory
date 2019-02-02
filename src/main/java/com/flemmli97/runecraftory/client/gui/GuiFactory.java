package com.flemmli97.runecraftory.client.gui;

import java.util.List;
import java.util.Set;

import com.flemmli97.runecraftory.common.core.handler.config.ConfigHandler;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.tenshilib.common.config.ConfigUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class GuiFactory implements IModGuiFactory{

	@Override
	public void initialize(Minecraft minecraftInstance) {}

	@Override
	public boolean hasConfigGui() {
		return true;
	}

	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen) {
		//List<IConfigElement> l = ConfigUtils.list(Configs.mainConfig, Configs.mobConfig, Configs.generationConfig);
		//l.addAll(ConfigUtils.list(Configs.cropConfig, Configs.spawnConfig));
		return new GuiWithRefresh(parentScreen, ConfigUtils.list(ConfigHandler.mainConfig, ConfigHandler.mobConfig, ConfigHandler.generationConfig), LibReference.MODID, false, false, LibReference.MODNAME);
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}
	
	private static class GuiWithRefresh extends GuiConfig
	{
		private GuiButtonExt refreshJson;
		
		public GuiWithRefresh(GuiScreen parentScreen, List<IConfigElement> configElements, String modID,
	            boolean allRequireWorldRestart, boolean allRequireMcRestart, String title)
	    {
	        super(parentScreen, configElements, modID, allRequireWorldRestart, allRequireMcRestart, title);
	    }
		
		@Override
	    public void initGui()
	    {
			super.initGui();
			this.buttonList.add(this.refreshJson=new GuiButtonExt(2000, this.width / 2 +150, this.height + 79, 20, 20, "√"));
	    }
		
		@Override
	    protected void actionPerformed(GuiButton button)
	    {
			if(button == this.refreshJson)
			{
				
			}
			else
				super.actionPerformed(button);
	    }
	}
}
