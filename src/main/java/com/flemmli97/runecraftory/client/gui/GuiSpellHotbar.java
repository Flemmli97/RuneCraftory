package com.flemmli97.runecraftory.client.gui;

import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.inventory.InventorySpells;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSpellHotbar extends Gui
{
    protected static final ResourceLocation WIDGETS_TEX_PATH = new ResourceLocation("textures/gui/widgets.png");
    private Minecraft mc;
    
    public GuiSpellHotbar(Minecraft mc) 
    {
        this.mc = mc;
        this.renderSpellInv(new ScaledResolution(mc), mc.getRenderPartialTicks());
    }
    
    protected void renderSpellInv(ScaledResolution sr, float partialTicks) 
    {
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer && !this.mc.playerController.isSpectator()) 
        {
            InventorySpells inv = this.mc.player.getCapability(PlayerCapProvider.PlayerCap, null).getInv();
            EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.mc.getTextureManager().bindTexture(WIDGETS_TEX_PATH);
            int i = sr.getScaledWidth() / 2;
            for (int a = 0; a < 2; ++a) 
            {
                this.drawTexturedModalRect(i + 114 + 29 * a, sr.getScaledHeight() - 47, 24, 23, 22, 22);
            }
            for (int a = 0; a < 2; ++a) 
            {
                this.drawTexturedModalRect(i + 114 + 29 * a, sr.getScaledHeight() - 23, 24, 23, 22, 22);
            }
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderHelper.enableGUIStandardItemLighting();
            for (int a = 0; a < 2; ++a) 
            {
                this.renderHotbarItem(i + 117 + 29 * a, sr.getScaledHeight() - 44, partialTicks, entityplayer, inv.getStackInSlot(a));
            }
            for (int a = 0; a < 2; ++a) 
            {
                this.renderHotbarItem(i + 117 + 29 * a, sr.getScaledHeight() - 20, partialTicks, entityplayer, inv.getStackInSlot(a + 2));
            }
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
        }
    }
    
    protected void renderHotbarItem(int posX, int posY, float partialTicks, EntityPlayer player, ItemStack stack) 
    {
        if (!stack.isEmpty()) 
        {
            float f = stack.getAnimationsToGo() - partialTicks;
            if (f > 0.0f) 
            {
                GlStateManager.pushMatrix();
                float f2 = 1.0f + f / 5.0f;
                GlStateManager.translate((float)(posX + 8), (float)(posY + 12), 0.0f);
                GlStateManager.scale(1.0f / f2, (f2 + 1.0f) / 2.0f, 1.0f);
                GlStateManager.translate((float)(-(posX + 8)), (float)(-(posY + 12)), 0.0f);
            }
            this.mc.getRenderItem().renderItemAndEffectIntoGUI((EntityLivingBase)player, stack, posX, posY);
            if (f > 0.0f) 
            {
                GlStateManager.popMatrix();
            }
            this.mc.getRenderItem().renderItemOverlays(this.mc.fontRenderer, stack, posX, posY);
        }
    }
}