package com.flemmli97.runecraftory.client.gui;

import java.util.List;

import com.flemmli97.runecraftory.common.lib.LibReference;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SpeechBubble extends Gui
{
    private static final ResourceLocation texturepath = new ResourceLocation(LibReference.MODID,"textures/gui/bars.png");
    private final int x;
    private final int y;
    private final int width;
    private int height;
    private final int maxHeight;
    private int showDuration;
    private String text = "";
    List<String> formattedText = Lists.newArrayList();
    
    public SpeechBubble(int x, int y, int width, int height) 
    {
        this.text = "";
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.maxHeight = height;
    }
    
    public void drawBubble(Minecraft mc, float partialTicks) 
    {
        if (this.showDuration > 0) 
        {
            --this.showDuration;
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(texturepath);
            this.drawTexturedModalRect(this.x, this.y, 1, 94, this.width / 2, this.height / 2);
            this.drawTexturedModalRect(this.x + this.width / 2, this.y, 129 - this.width / 2, 94, this.width / 2, this.height / 2);
            this.drawTexturedModalRect(this.x, this.y + this.height / 2, 1, 158 - this.height / 2, this.width / 2, this.height / 2 + 15);
            this.drawTexturedModalRect(this.x + this.width / 2, this.y + this.height / 2, 129 - this.width / 2, 158 - this.height / 2, this.width / 2, this.height / 2 + 15);
            for (int i = 0; i < this.formattedText.size(); ++i) 
            {
                fontrenderer.drawString(this.formattedText.get(i), this.x + 3, this.y + 3 + i * 8, 0);
            }
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
        }
    }
    
    /**
     * Displays this speech bubble for a certain amount of time.
     * @param text Text to display
     * @param duration The duration to show this bubble
     */
    public void showBubble(String text, int duration) 
    {
        this.text = text;
        this.showDuration = duration;
        this.formattedText = Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(this.text, Math.max(10, this.width - 6));
        this.height = Math.min(this.formattedText.size() * 8 + 6, this.maxHeight);

    }
}
