package com.flemmli97.runecraftory.common.core.handler.event;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.flemmli97.runecraftory.api.items.CropProperties;
import com.flemmli97.runecraftory.api.items.FoodProperties;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.api.items.IItemWearable;
import com.flemmli97.runecraftory.api.mappings.CropMap;
import com.flemmli97.runecraftory.api.mappings.ItemFoodMap;
import com.flemmli97.runecraftory.api.mappings.ItemStatMap;
import com.flemmli97.runecraftory.client.gui.ButtonSkill;
import com.flemmli97.runecraftory.client.gui.GuiBars;
import com.flemmli97.runecraftory.client.gui.GuiInfoScreen;
import com.flemmli97.runecraftory.client.gui.GuiInfoScreenSub;
import com.flemmli97.runecraftory.client.gui.GuiSpellHotbar;
import com.flemmli97.runecraftory.common.core.handler.time.CalendarHandler.EnumSeason;
import com.flemmli97.runecraftory.common.core.handler.time.WeatherData;
import com.flemmli97.runecraftory.common.core.handler.time.WeatherData.EnumWeather;
import com.flemmli97.runecraftory.common.lib.LibReference;
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
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
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
            	event.getToolTip().addAll(1, injectAdditionalTooltip(stack));
            }
        }
    }
    
    private List<String> injectAdditionalTooltip(ItemStack stack)
    {
    	List<String> tooltip = Lists.newArrayList();
        CropProperties props = CropMap.getProperties(stack);
        if (props != null) 
        {
        	if(!props.bestSeasons().isEmpty())
        	{
        		String season=I18n.format("season.best") + ": ";
        		int i = 0;
        		for(EnumSeason seas : props.bestSeasons())
        		{
        			season+=(i!=0?TextFormatting.GRAY+"/":"")+TextFormatting.getValueByName(seas.getColor()) + I18n.format(seas.formattingText());
        			i++;
        		}
            	tooltip.add(season);
        	}      	
        	if(!props.badSeasons().isEmpty())
        	{
        		String sub = I18n.format("season.bad") + ": ";
        		int i = 0;
        		for(EnumSeason seas : props.badSeasons())
        			if(!props.bestSeasons().contains(seas))
        			{
        				sub+=(i!=0?TextFormatting.GRAY+"/":"")+TextFormatting.getValueByName(seas.getColor()) + I18n.format(seas.formattingText());
        				i++;
        			}
        		if(i!=0)
        			tooltip.add(sub);
        	}
            tooltip.add(I18n.format("growth") + ": " + props.growth() + "  " + I18n.format("harvested") + ": " + props.maxDrops());
        }
        boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
        if(shift && stack.getItem() instanceof ItemFood)
        {
            FoodProperties food = ItemFoodMap.get(stack);
            if(food!=null)
            {
                tooltip.add(I18n.format("item.eaten"));
                String hp = (food.getHPGain()!=0?"HP "+food.getHPGain():"");
                String hpPerc = (food.getHpPercentGain()!=0?"HP "+ food.getHpPercentGain()+"%":"");
                String rp = (food.getRPRegen()!=0?"RP "+ food.getRPRegen():"");
                String rpPerc = (food.getRpPercentRegen()!=0?"RP "+ food.getRpPercentRegen()+"%":"");
                
                tooltip.add(" " + hp + " "  + hpPerc +  " "  + rp +  " "  + rpPerc);
                for (Entry<IAttribute, Integer> entry : food.effects().entrySet()) {
                    tooltip.add(" " + I18n.format(entry.getKey().getName()) + ": " + entry.getValue());
                }
                for (Entry<IAttribute, Float> entry : food.effectsMultiplier().entrySet()) {
                    tooltip.add(" " + I18n.format(entry.getKey().getName()) + ": " + (int)(100*entry.getValue())+"%");
                }
            }
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
                if (shift) 
                {
                    Map<IAttribute, Integer> stats = ItemNBT.statIncrease(stack);
                    if (!stats.isEmpty()) {
                        String prefix = (stack.getItem() instanceof IItemWearable) ? "item.equipped" : "item.upgrade";
                        tooltip.add(I18n.format(prefix));
                    }
                    for (Entry<IAttribute, Integer> entry : stats.entrySet()) {
                        tooltip.add(" " + I18n.format(entry.getKey().getName()) + ": " + entry.getValue());
                    }
                }
            }
        }
        return tooltip;
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void runey(RenderWorldLastEvent event)
    {
    	if(WeatherData.get(Minecraft.getMinecraft().world).currentWeather()==EnumWeather.RUNEY)
    		renderRuneyWeather(Minecraft.getMinecraft(), event.getPartialTicks());
    }
    
    private final Random random = new Random();
    private static final ResourceLocation runeyTex = new ResourceLocation(LibReference.MODID, "environment/runey.png");
    
    private void renderRuneyWeather(Minecraft mc, float partialTicks)
    {
        /*mc.entityRenderer.enableLightmap();
        Entity entity = mc.getRenderViewEntity();
        World world = mc.world;
        int i = MathHelper.floor(entity.posX);
        int j = MathHelper.floor(entity.posY);
        int k = MathHelper.floor(entity.posZ);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.disableCull();
        GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.alphaFunc(516, 0.1F);
        double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
        double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
        double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
        int l = MathHelper.floor(d1);
        int i1 = 5;

        if (mc.gameSettings.fancyGraphics)
        {
            i1 = 10;
        }

        int j1 = -1;
        float f1 = (float)rendererUpdateCount + partialTicks;
        bufferbuilder.setTranslation(-d0, -d1, -d2);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (int k1 = k - i1; k1 <= k + i1; ++k1)
        {
            for (int l1 = i - i1; l1 <= i + i1; ++l1)
            {
                int i2 = (k1 - k + 16) * 32 + l1 - i + 16;
                double d3 = rainXCoords[i2] * 0.5D;
                double d4 = rainYCoords[i2] * 0.5D;
                blockpos$mutableblockpos.setPos(l1, 0, k1);
                Biome biome = world.getBiome(blockpos$mutableblockpos);
                    int j2 = world.getPrecipitationHeight(blockpos$mutableblockpos).getY();
                    int k2 = j - i1;
                    int l2 = j + i1;

                    if (k2 < j2)
                    {
                        k2 = j2;
                    }

                    if (l2 < j2)
                    {
                        l2 = j2;
                    }

                    int i3 = j2;

                    if (j2 < l)
                    {
                        i3 = l;
                    }

                    if (k2 != l2)
                    {
                        random.setSeed((long)(l1 * l1 * 3121 + l1 * 45238971 ^ k1 * k1 * 418711 + k1 * 13761));
                        blockpos$mutableblockpos.setPos(l1, k2, k1);
                        float f2 = biome.getTemperature(blockpos$mutableblockpos);

                        if (world.getBiomeProvider().getTemperatureAtHeight(f2, j2) >= 0.15F)
                        {
                            if (j1 != 0)
                            {
                                if (j1 >= 0)
                                {
                                    tessellator.draw();
                                }

                                j1 = 0;
                                mc.getTextureManager().bindTexture(runeyTex);
                                bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                            }

                            double d5 = -((rendererUpdateCount + l1 * l1 * 3121 + l1 * 45238971 + k1 * k1 * 418711 + k1 * 13761 & 31) + partialTicks) / 32.0D * (3.0D + random.nextDouble());
                            double d6 = ((float)l1 + 0.5F) - entity.posX;
                            double d7 = ((float)k1 + 0.5F) - entity.posZ;
                            float f3 = MathHelper.sqrt(d6 * d6 + d7 * d7) / (float)i1;
                            float f4 = ((1.0F - f3 * f3) * 0.5F + 0.5F) * f;
                            blockpos$mutableblockpos.setPos(l1, i3, k1);
                            int j3 = world.getCombinedLight(blockpos$mutableblockpos, 0);
                            int k3 = j3 >> 16 & 65535;
                            int l3 = j3 & 65535;
                            bufferbuilder.pos(l1 - d3 + 0.5D, l2, k1 - d4 + 0.5D).tex(0.0D, k2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
                            bufferbuilder.pos(l1 + d3 + 0.5D, l2, k1 + d4 + 0.5D).tex(1.0D, k2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
                            bufferbuilder.pos(l1 + d3 + 0.5D, k2, k1 + d4 + 0.5D).tex(1.0D, l2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
                            bufferbuilder.pos(l1 - d3 + 0.5D, k2, k1 - d4 + 0.5D).tex(0.0D, l2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
                        }
                    }
                }
            }

        if (j1 >= 0)
        {
            tessellator.draw();
        }

        bufferbuilder.setTranslation(0.0D, 0.0D, 0.0D);
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.alphaFunc(516, 0.1F);
        mc.entityRenderer.disableLightmap();*/
    }
}
