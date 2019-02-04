package com.flemmli97.runecraftory.client.gui;

import java.io.IOException;

import com.flemmli97.runecraftory.api.items.ItemStatAttributes;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.inventory.ContainerInfoScreenSub;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumSkills;
import com.flemmli97.runecraftory.common.network.PacketHandler;
import com.flemmli97.runecraftory.common.network.PacketOpenGuiContainer;
import com.flemmli97.runecraftory.common.utils.LevelCalc;
import com.flemmli97.runecraftory.common.utils.RFCalculations;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiInfoScreenSub extends InventoryEffectRenderer
{
    private static final ResourceLocation texturepath = new ResourceLocation(LibReference.MODID,"textures/gui/skills2.png");
    private static final ResourceLocation texturepath2 = new ResourceLocation(LibReference.MODID,"textures/gui/skills3.png");
    private static final ResourceLocation bars = new ResourceLocation(LibReference.MODID,"textures/gui/bars.png");

    private Minecraft mc;
    private IPlayer cap;
    private int page;
    private ButtonPage pageButton1;
    private ButtonPage pageButton2;
    private final int texX = 224;
    private final int texY = 224;
    private int guiX;
    private int guiY;
    
    public GuiInfoScreenSub(Minecraft mc) {
        super(new ContainerInfoScreenSub(mc.player));
        this.mc = mc;
        this.cap = this.mc.player.getCapability(PlayerCapProvider.PlayerCap, null);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException 
    {
        if (keyCode == 1 || this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode)) 
        {
            this.mc.player.closeScreen();
        }
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) 
    {
        GuiInventory.drawEntityOnScreen(this.guiX + 58, this.guiY + 76, 29, (float)(this.guiX + 58 - mouseX), (float)(this.guiY + 76 - 50 - mouseY), this.mc.player);
        if (this.page == 2) 
        {
            this.mc.getTextureManager().bindTexture(texturepath2);
        }
        else 
        {
            this.mc.getTextureManager().bindTexture(texturepath);
        }
        this.drawTexturedModalRect(this.guiX, this.guiY, 15, 15, 224, 224);
        int healthWidth = Math.min(100, (int)((this.cap.getHealth(this.mc.player)) / (this.cap.getMaxHealth(this.mc.player)) * 100.0f));
        int runeWidth = Math.min(100, (int)(this.cap.getRunePoints() / (float)this.cap.getMaxRunePoints() * 100.0f));
        int exp = (int)(this.cap.getPlayerLevel()[1] / (float)LevelCalc.xpAmountForLevelUp(this.cap.getPlayerLevel()[0]) * 100.0f);
        this.mc.getTextureManager().bindTexture(bars);
        this.drawTexturedModalRect(this.guiX + 118, this.guiY + 23, 2, 51, healthWidth, 6);
        this.drawTexturedModalRect(this.guiX + 118, this.guiY + 33, 2, 58, runeWidth, 6);
        this.drawTexturedModalRect(this.guiX + 118, this.guiY + 44, 2, 66, exp, 12);
        if (this.page == 2) 
        {
            for (int i = 0; i < 5; ++i) 
            {
                EnumSkills skill = EnumSkills.values()[i + 27];
                int skillXP = (int)(this.cap.getSkillLevel(skill)[1] / (float)LevelCalc.xpAmountForSkills(this.cap.getSkillLevel(skill)[0]) * 96.0f);
                this.mc.getTextureManager().bindTexture(bars);
                this.drawTexturedModalRect(this.guiX + 10, this.guiY + 125 + 13 * i, 2, 80, skillXP, 9);
                this.mc.fontRenderer.drawString(skill.getIdentifier(), this.guiX + 11, this.guiY + 126 + 13 * i, 16777215);
                this.drawRightAlignedScaledString("" + this.cap.getSkillLevel(skill)[0], this.guiX + 105, this.guiY + 126 + 13 * i, 1.0f, 16777215);
            }
        }
        else 
        {
            for (int i = 0; i < 7; ++i) 
            {
                EnumSkills skill = EnumSkills.values()[i + this.page * 14];
                int skillXP = (int)(this.cap.getSkillLevel(skill)[1] / (float)LevelCalc.xpAmountForSkills(this.cap.getSkillLevel(skill)[0]) * 96.0f);
                this.mc.getTextureManager().bindTexture(bars);
                this.drawTexturedModalRect(this.guiX + 10, this.guiY + 125 + 13 * i, 2, 80, skillXP, 9);
                this.mc.fontRenderer.drawString(skill.getIdentifier(), this.guiX + 11, this.guiY + 126 + 13 * i, 16777215);
                this.drawRightAlignedScaledString("" + this.cap.getSkillLevel(skill)[0], this.guiX + 105, this.guiY + 126 + 13 * i, 1.0f, 16777215);
            }
            for (int i = 0; i < 7; ++i) 
            {
                EnumSkills skill = EnumSkills.values()[i + 7 + this.page * 14];
                int skillXP = (int)(this.cap.getSkillLevel(skill)[1] / (float)LevelCalc.xpAmountForSkills(this.cap.getSkillLevel(skill)[0]) * 96.0f);
                this.mc.getTextureManager().bindTexture(bars);
                this.drawTexturedModalRect(this.guiX + 120, this.guiY + 125 + 13 * i, 2, 80, skillXP, 9);
                this.mc.fontRenderer.drawString(skill.getIdentifier(), this.guiX + 121, this.guiY + 126 + 13 * i, 16777215);
                this.drawRightAlignedScaledString("" + this.cap.getSkillLevel(skill)[0], this.guiX + 215, this.guiY + 126 + 13 * i, 1.0f, 16777215);
            }
        }
        this.drawCenteredScaledString((int)this.cap.getHealth(this.mc.player) + "/" + (int)this.cap.getMaxHealth(this.mc.player), this.guiX + 173, this.guiY + 23.5f, 0.7f, 16777215);
        this.drawCenteredScaledString(this.cap.getRunePoints() + "/" + this.cap.getMaxRunePoints(), this.guiX + 173, this.guiY + 34, 0.7f, 16777215);
        this.mc.fontRenderer.drawString("Level", this.guiX + 120, this.guiY + 46, 0);
        this.mc.fontRenderer.drawString("Att.", this.guiX + 120, this.guiY + 63, 0);
        this.mc.fontRenderer.drawString("Def.", this.guiX + 120, this.guiY + 75, 0);
        this.mc.fontRenderer.drawString("M.Att.", this.guiX + 120, this.guiY + 87, 0);
        this.mc.fontRenderer.drawString("M.Def.", this.guiX + 120, this.guiY + 99, 0);
        this.drawRightAlignedScaledString("" + this.cap.getMoney(), this.guiX + 194 - ((this.mc.fontRenderer.getStringWidth("" + this.cap.getMoney()) == 6) ? 3 : 0), this.guiY + 11, 0.6f, 0);
        this.drawRightAlignedScaledString("" + this.cap.getPlayerLevel()[0], this.guiX + 216, this.guiY + 46, 1.0f, 0);
        this.drawRightAlignedScaledString("" + RFCalculations.getAttributeValue(this.mc.player, ItemStatAttributes.RFATTACK, null, null), this.guiX + 216, this.guiY + 64, 1.0f, 0);
        this.drawRightAlignedScaledString("" + RFCalculations.getAttributeValue(this.mc.player, ItemStatAttributes.RFDEFENCE, null, null), this.guiX + 216, this.guiY + 75, 1.0f, 0);
        this.drawRightAlignedScaledString("" + RFCalculations.getAttributeValue(this.mc.player, ItemStatAttributes.RFMAGICATT, null, null), this.guiX + 216, this.guiY + 87, 1.0f, 0);
        this.drawRightAlignedScaledString("" + RFCalculations.getAttributeValue(this.mc.player, ItemStatAttributes.RFMAGICDEF, null, null), this.guiX + 216, this.guiY + 99, 1.0f, 0);
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) 
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
    
    private void drawCenteredScaledString(String string, float x, float y, float scale, int color) 
    {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, scale);
        float xCenter = x - this.mc.fontRenderer.getStringWidth(string) / 2;
        int xScaled = (int)(xCenter / scale);
        int yScaled = (int)(y / scale);
        this.mc.fontRenderer.drawString(string, xScaled, yScaled, color);
        GlStateManager.popMatrix();
    }
    
    private void drawRightAlignedScaledString(String string, float x, float y, float scale, int color) 
    {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, scale);
        float xCenter = x - this.mc.fontRenderer.getStringWidth(string);
        int xScaled = (int)(xCenter / scale);
        int yScaled = (int)(y / scale);
        this.mc.fontRenderer.drawString(string, xScaled, yScaled, color);
        GlStateManager.popMatrix();
    }

    @Override
    public void initGui() {
        super.initGui();
        this.guiX = (this.width - 218) / 2;
        this.guiY = (this.height - 218) / 2;
        this.buttonList.clear();
        this.labelList.clear();
        if (this.page == 2) 
        {
            this.buttonList.add(this.pageButton2 = new ButtonPage(1, this.guiX + 194, this.guiY + 7, "<"));
        }
        else 
        {
            this.buttonList.add(this.pageButton2 = new ButtonPage(1, this.guiX + 194, this.guiY + 7, "<"));
            this.buttonList.add(this.pageButton1 = new ButtonPage(0, this.guiX + 207, this.guiY + 7, ">"));
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException 
    {
        if (button == this.pageButton1) 
        {
            if (this.page < 2) 
            {
                ++this.page;
            }
            this.initGui();
        }
        else if (button == this.pageButton2) 
        {
            if (this.page == 0) 
            {
                PacketHandler.sendToServer(new PacketOpenGuiContainer(LibReference.guiInfo));
            }
            else 
            {
                --this.page;
                this.initGui();
            }
        }
    }

    @Override
    public int getGuiLeft() 
    {
        return this.guiX;
    }

    @Override
    public int getGuiTop() 
    {
        return this.guiY;
    }

    @Override
    public int getXSize() 
    {
        return this.texX;
    }

    @Override
    public int getYSize() 
    {
        return this.texY;
    }
}
