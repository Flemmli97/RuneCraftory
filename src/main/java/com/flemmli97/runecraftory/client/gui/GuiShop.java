package com.flemmli97.runecraftory.client.gui;

import java.io.IOException;

import org.lwjgl.input.Mouse;

import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.entity.npc.EntityNPCShopOwner;
import com.flemmli97.runecraftory.common.inventory.ContainerShop;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.network.PacketBuy;
import com.flemmli97.runecraftory.common.network.PacketHandler;
import com.flemmli97.runecraftory.common.utils.ItemUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiShop extends GuiContainer
{
    private static final ResourceLocation texturepath = new ResourceLocation(LibReference.MODID,"textures/gui/shop.png");
    private EntityNPCShopOwner shop;
    private final int texX = 256;
    private final int texY = 256;
    private int guiX;
    private int guiY;
    private int clickDelay;
    private int leftDelay;
    private int rightDelay;
    private boolean leftClick;
    private boolean rightClick;
    private IInventory playerInv;
    private IPlayer cap;
    private ButtonPage next;
    private ButtonPage prev;
    private ButtonBuy buy;
    private SpeechBubble speech;
    
    public GuiShop(EntityPlayer player, EntityNPCShopOwner shopOwner) {
        super(new ContainerShop(player, shopOwner));
        this.leftDelay = 25;
        this.rightDelay = 25;
        this.shop = shopOwner;
        this.playerInv = player.inventory;
        this.cap = player.getCapability(PlayerCapProvider.PlayerCap, null);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.mc.getTextureManager().bindTexture(texturepath);
        this.drawTexturedModalRect(this.guiX, this.guiY, 0, 0, 256, 256);
        drawEntityOnScreen(this.guiX + 209, this.guiY + 175, 60, this.guiX + 209 - mouseX, this.guiY + 175 - 100 - mouseY, (EntityNPCShopOwner)this.shop);
        this.drawRightAlignedScaledString(TextFormatting.GOLD + "$ " + this.cap.getMoney(), this.guiX + 220, this.guiY + 210, 1.0f, 0);
        this.drawRightAlignedScaledString("$ " + ItemUtils.getBuyPrice(this.outputStack()) * this.outputStack().getCount(), this.guiX + 190, this.guiY + 156, 1.0f, 0);
    }
    
    public void displayMessage(String msg) {
        this.speech.showBubble(msg, 200);
    }
    
    private ItemStack outputStack() {
        return this.inventorySlots.getSlot(25).getStack();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.speech.drawBubble(this.mc, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
        boolean shopSlot = this.isShopSlot();
        this.leftClick = (Mouse.isButtonDown(0) && shopSlot);
        this.rightClick = (Mouse.isButtonDown(1) && shopSlot);
        this.clickDelay = Math.max(0, --this.clickDelay);
        if (this.leftClick) {
            this.rightDelay = 25;
            if (this.clickDelay == 0 && (this.leftDelay == 0 || this.leftDelay == 25)) {
                ClickType click = GuiScreen.isShiftKeyDown() ? ClickType.PICKUP_ALL : ClickType.PICKUP;
                this.mc.playerController.windowClick(this.inventorySlots.windowId, this.getSlotUnderMouse().slotNumber, Mouse.getEventButton(), click, (EntityPlayer)this.mc.player);
                this.clickDelay = 4;
            }
            int n2 = 0;
            int leftDelay = this.leftDelay - 1;
            this.leftDelay = leftDelay;
            this.leftDelay = Math.max(n2, leftDelay);
        }
        else if (this.rightClick) {
            this.leftDelay = 25;
            if (this.clickDelay == 0 && (this.rightDelay == 0 || this.rightDelay == 25)) {
                ClickType click = GuiScreen.isShiftKeyDown() ? ClickType.QUICK_MOVE : ClickType.QUICK_CRAFT;
                this.mc.playerController.windowClick(this.inventorySlots.windowId, this.getSlotUnderMouse().slotNumber, Mouse.getEventButton(), click, (EntityPlayer)this.mc.player);
                this.clickDelay = 2;
            }
            int n3 = 0;
            int rightDelay = this.rightDelay - 1;
            this.rightDelay = rightDelay;
            this.rightDelay = Math.max(n3, rightDelay);
        }
        else {
            this.leftDelay = 25;
            this.rightDelay = 25;
        }
    }
    
    private boolean isShopSlot() {
        return this.getSlotUnderMouse() != null && this.getSlotUnderMouse().inventory != this.playerInv;
    }

    /**
     * Disables mouse handling for shop slot since its handled in drawScreen
     */
    @Override
    public void handleMouseInput() throws IOException {
        if (this.leftClick || this.rightClick || this.isShopSlot()) {
            return;
        }
        super.handleMouseInput();
    }

    @Override
    public void initGui() {
        super.initGui();
        this.guiX = (this.width - 218) / 2;
        this.guiY = (this.height - 218) / 2;
        this.buttonList.add(this.next = new ButtonPage(0, this.guiX + 126, this.guiY + 5, ">"));
        this.buttonList.add(this.prev = new ButtonPage(0, this.guiX + 12, this.guiY + 5, "<"));
        this.buttonList.add(this.buy = new ButtonBuy(this.guiX + 191, this.guiY + 170));
        this.speech = new SpeechBubble(this.guiX + 152, this.guiY + 10, 101, 30);
    }

    @Override
    public int getGuiLeft() {
        return this.guiX;
    }

    @Override
    public int getGuiTop() {
        return this.guiY;
    }

    @Override
    public int getXSize() {
        return this.texX;
    }

    @Override
    public int getYSize() {
        return this.texY;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException 
    {
        if (button == this.buy) 
        {
            PacketHandler.sendToServer(new PacketBuy(this.outputStack().copy()));
        }
        else if (button == this.next) 
        {
            ((ContainerShop)this.inventorySlots).getShopInv().next();
        }
        else if (button == this.prev) 
        {
            ((ContainerShop)this.inventorySlots).getShopInv().prev();
        }
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
    
    /**
     * Like in GuiInventory but with lightning disabled and z level set so its rendered behind the gui texture
     */
    public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)posX, (float)posY, -50.0f);
        GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        float f = ent.renderYawOffset;
        float f2 = ent.rotationYaw;
        float f3 = ent.rotationPitch;
        float f4 = ent.prevRotationYawHead;
        float f5 = ent.rotationYawHead;
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-(float)Math.atan(mouseY / 40.0f) * 20.0f, 1.0f, 0.0f, 0.0f);
        ent.renderYawOffset = (float)Math.atan(mouseX / 40.0f) * 20.0f;
        ent.rotationYaw = (float)Math.atan(mouseX / 40.0f) * 40.0f;
        ent.rotationPitch = -(float)Math.atan(mouseY / 40.0f) * 20.0f;
        ent.rotationYawHead = ent.rotationYaw;
        ent.prevRotationYawHead = ent.rotationYaw;
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0f);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntity((Entity)ent, 0.0, 0.0, 0.0, 0.0f, 1.0f, false);
        rendermanager.setRenderShadow(true);
        ent.renderYawOffset = f;
        ent.rotationYaw = f2;
        ent.rotationPitch = f3;
        ent.prevRotationYawHead = f4;
        ent.rotationYawHead = f5;
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
}
