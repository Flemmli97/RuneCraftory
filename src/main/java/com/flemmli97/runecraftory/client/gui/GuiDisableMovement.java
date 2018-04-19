package com.flemmli97.runecraftory.client.gui;

import java.io.IOException;

import com.flemmli97.runecraftory.common.init.PotionRegistry;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;

public class GuiDisableMovement extends GuiChat{
	private int ticker;

	@Override
	public void updateScreen()
    {
		this.ticker++;
		if(this.ticker>1000 || (this.mc!=null && (this.mc.player.capabilities.isCreativeMode || this.mc.player.getActivePotionEffect(PotionRegistry.sleep)==null && this.ticker>10)))
		{
			this.mc.displayGuiScreen((GuiScreen)null);
		}
    }
	
	@Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
    		if (keyCode!=1 &&keyCode != 28 && keyCode != 156 && (this.inputField.getText().startsWith("/") || String.valueOf(typedChar).equals("/")))
        {
            super.keyTyped(typedChar, keyCode);
        }
        else
        {
            String s = this.inputField.getText().trim();

            if (!s.isEmpty())
            {
                this.sendChatMessage(s);
            }

            this.inputField.setText("");
            this.mc.ingameGUI.getChatGUI().resetScroll();
        }
    }
    
	@Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
