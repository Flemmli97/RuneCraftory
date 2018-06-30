package com.flemmli97.runecraftory.common.core.handler.event;

import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.flemmli97.runecraftory.api.items.CropProperties;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.api.items.IItemWearable;
import com.flemmli97.runecraftory.api.items.ItemStatAttributes;
import com.flemmli97.runecraftory.api.mappings.CropMap;
import com.flemmli97.runecraftory.api.mappings.ItemStatMap;
import com.flemmli97.runecraftory.client.gui.ButtonSkill;
import com.flemmli97.runecraftory.client.gui.GuiBars;
import com.flemmli97.runecraftory.client.gui.GuiInfoScreen;
import com.flemmli97.runecraftory.client.gui.GuiInfoScreenSub;
import com.flemmli97.runecraftory.client.gui.GuiSpellHotbar;
import com.flemmli97.runecraftory.common.items.weapons.DualBladeBase;
import com.flemmli97.runecraftory.common.items.weapons.GloveBase;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;
import com.flemmli97.runecraftory.common.network.PacketCastSpell;
import com.flemmli97.runecraftory.common.network.PacketHandler;
import com.flemmli97.runecraftory.common.utils.EntityUtils;
import com.flemmli97.runecraftory.common.utils.ItemNBT;
import com.flemmli97.runecraftory.common.utils.ItemUtils;
import com.flemmli97.runecraftory.proxy.ClientProxy;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventHandlerClient
{
	/**
	 * Disable vanilla hunger and health rendering
	 */
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void renderRunePoints(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD || event.getType() == RenderGameOverlayEvent.ElementType.HEALTH) 
            event.setCanceled(true);
    }
    
    /**
     * Spell inventory, health and runepoints rendering
     */
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void renderRunePoints(RenderGameOverlayEvent.Post event) {
        if (event.isCancelable())
            return;
        if (event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE && !(Minecraft.getMinecraft().currentScreen instanceof GuiInfoScreen) && !(Minecraft.getMinecraft().currentScreen instanceof GuiInfoScreenSub)) {
            new GuiBars(Minecraft.getMinecraft());
        }
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            new GuiSpellHotbar(Minecraft.getMinecraft());
        }
    }
    
    /**
     * Button for viewing player skills
     */
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void initSkillTab(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof GuiInventory || event.getGui() instanceof GuiContainerCreative) {
            int x = 0;
            int y = 0;
            if (event.getGui() instanceof GuiInventory) {
                x = (event.getGui().width - 174) / 2 - 28;
                y = (event.getGui().height - 166) / 2;
            }
            else {
                x = (event.getGui().width - 192) / 2 - 28;
                y = (event.getGui().height - 136) / 2;
            }
            event.getButtonList().add(new ButtonSkill(x, y));
        }
    }
    
    /**
     * Spell casting
     */
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void keyEvent(InputEvent.KeyInputEvent event) {
        if (ClientProxy.skill1.isPressed()) {
            PacketHandler.sendToServer(new PacketCastSpell(0));
        }
        else if (ClientProxy.skill2.isPressed()) {
            PacketHandler.sendToServer(new PacketCastSpell(1));
        }
        else if (ClientProxy.skill3.isPressed()) {
            PacketHandler.sendToServer(new PacketCastSpell(2));
        }
        else if (ClientProxy.skill4.isPressed()) {
            PacketHandler.sendToServer(new PacketCastSpell(3));
        }
    }
    
    /**
     * Rendering dual weapons in first person
     */
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void renderDualFirst(RenderSpecificHandEvent event) {
        ItemStack stackMain = Minecraft.getMinecraft().player.getHeldItem(EnumHand.MAIN_HAND);
        if ((stackMain.getItem() instanceof DualBladeBase || stackMain.getItem() instanceof GloveBase) && event.getHand() == EnumHand.OFF_HAND) {
            event.setCanceled(true);
            Minecraft.getMinecraft().getItemRenderer().renderItemInFirstPerson(Minecraft.getMinecraft().player, event.getPartialTicks(), event.getInterpolatedPitch(), EnumHand.OFF_HAND, event.getSwingProgress(), stackMain, event.getEquipProgress());
        }
    }
    
    /**
     * Disables player interactions when under special conditions
     */
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void disable(MouseEvent event) {
        if (EntityUtils.isEntityDisabled(Minecraft.getMinecraft().player)) {
            event.setCanceled(true);
        }
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void disable(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START && Minecraft.getMinecraft().player != null && EntityUtils.isEntityDisabled((EntityLivingBase)Minecraft.getMinecraft().player)) 
        {
            Mouse.getDX();
            Mouse.getDY();
            MouseHelper mouseHelper = Minecraft.getMinecraft().mouseHelper;
            MouseHelper mouseHelper2 = Minecraft.getMinecraft().mouseHelper;
            mouseHelper2.deltaY = 0;
            mouseHelper.deltaX = 0;
        }
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void disable(InputUpdateEvent event) {
        if (EntityUtils.isEntityDisabled((EntityLivingBase)event.getEntityPlayer())) {
            event.getMovementInput().backKeyDown = false;
            event.getMovementInput().forwardKeyDown = false;
            event.getMovementInput().jump = false;
            event.getMovementInput().leftKeyDown = false;
            event.getMovementInput().rightKeyDown = false;
            event.getMovementInput().sneak = false;
            event.getMovementInput().moveForward = 0.0f;
            event.getMovementInput().moveStrafe = 0.0f;
        }
    }
    
    /**
     * Modifies tooltip rendering, adding all relevant information like level, sell/buy prices and stuff
     */
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void tooltipEvent(ItemTooltipEvent event) 
    {
        ItemStack stack = event.getItemStack();
        if (!stack.isEmpty()) 
        {
            boolean showTooltip = true;
            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("HideFlags", 99)) 
            {
                showTooltip = (stack.getTagCompound().getInteger("HideFlags") & 0x20) == 0x0;
            }
            if (showTooltip) 
            {
                List<String> newToolTip = Lists.newArrayList();
                newToolTip.add(event.getToolTip().get(0));
                this.injectAdditionalTooltip(stack, newToolTip);
                newToolTip.addAll(event.getToolTip().subList(Math.min(event.getToolTip().size(), 1), event.getToolTip().size()));
                event.getToolTip().clear();
                event.getToolTip().addAll(newToolTip);
            }
        }
    }
    
    private void injectAdditionalTooltip(ItemStack stack, List<String> tooltip)
    {
        CropProperties props = CropMap.getProperties(stack.getItem().getRegistryName());
        if (props != null) 
        {
            tooltip.add(I18n.format("season") + ": " + TextFormatting.getValueByName(props.bestSeason().getColor()) + props.bestSeason().formattingText());
            tooltip.add(I18n.format("growth") + ": " + props.growth() + "  " + I18n.format("harvested") + ": " + props.maxDrops());
        }
        if (ItemStatMap.get(stack) != null) 
        {
            NBTTagCompound tag = ItemNBT.getItemNBT(stack);
            if (tag != null) 
            {
                if (stack.getItem() instanceof IItemUsable) 
                {
                    EnumElement element = EnumElement.fromName(tag.getString("Element"));
                    if (element != EnumElement.NONE) 
                    {
                        tooltip.add(TextFormatting.getValueByName(element.getColor()) + I18n.format("attribute." + element.getName()));
                    }
                }
                if (ItemUtils.getBuyPrice(stack) > 0) 
                {
                    tooltip.add(I18n.format("item.level") + ": " + tag.getInteger("ItemLevel") + "  " + I18n.format("item.buy") + ": " + ItemUtils.getBuyPrice(stack) + "  " + I18n.format("item.sell") + ": " + ItemUtils.getSellPrice(stack));
                }
                else 
                {
                    tooltip.add(I18n.format("item.level") + ": " + tag.getInteger("ItemLevel") + "  " + I18n.format("item.sell") + ": " + ItemUtils.getSellPrice(stack));
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) 
                {
                    Map<ItemStatAttributes, Integer> stats = ItemNBT.statIncrease(stack);
                    if (!stats.isEmpty()) {
                        String prefix = (stack.getItem() instanceof IItemWearable) ? "item.equipped" : "item.upgrade";
                        tooltip.add(I18n.format(prefix));
                    }
                    for (ItemStatAttributes att : stats.keySet()) {
                        tooltip.add(" " + I18n.format(att.getName()) + ": " + ItemNBT.statIncrease(stack).get(att));
                    }
                }
            }
        }
    }
}
