package com.flemmli97.runecraftory.client;

import com.flemmli97.runecraftory.api.datapack.CropProperties;
import com.flemmli97.runecraftory.api.datapack.FoodProperties;
import com.flemmli97.runecraftory.api.datapack.ItemStat;
import com.flemmli97.runecraftory.api.enums.EnumElement;
import com.flemmli97.runecraftory.api.enums.EnumSeason;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.api.items.IItemWearable;
import com.flemmli97.runecraftory.common.datapack.DataPackHandler;
import com.flemmli97.runecraftory.common.entities.BaseMonster;
import com.flemmli97.runecraftory.common.utils.ItemNBT;
import com.flemmli97.runecraftory.common.utils.ItemUtils;
import com.flemmli97.runecraftory.network.C2SRideJump;
import com.flemmli97.runecraftory.network.PacketHandler;
import com.google.common.collect.Lists;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.Map;

public class ClientEvents {

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
    }

    @SubscribeEvent
    public void jump(LivingEvent.LivingUpdateEvent e) {
        if (e.getEntityLiving() instanceof ClientPlayerEntity && e.getEntityLiving().getRidingEntity() instanceof BaseMonster && ((ClientPlayerEntity) e.getEntityLiving()).movementInput.jump)
            PacketHandler.sendToServer(new C2SRideJump());
    }

    @SubscribeEvent
    public void tooltipEvent(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!stack.isEmpty()) {
            boolean showTooltip = true;
            if (stack.hasTag() && stack.getTag().contains("HideFlags", 99)) {
                showTooltip = (stack.getTag().getInt("HideFlags") & 0x20) == 0x0;
            }
            if (showTooltip) {
                event.getToolTip().addAll(1, this.injectAdditionalTooltip(stack));
            }
        }
    }

    private List<ITextComponent> injectAdditionalTooltip(ItemStack stack) {
        List<ITextComponent> tooltip = Lists.newArrayList();
        CropProperties props = DataPackHandler.getCropStat(stack.getItem());
        if (props != null) {
            if (!props.bestSeasons().isEmpty()) {
                IFormattableTextComponent season = new TranslationTextComponent("season.best").append(": ");
                int i = 0;
                for (EnumSeason seas : props.bestSeasons()) {
                    season.append(i != 0 ? "/" : "").formatted(TextFormatting.GRAY)
                            .append(new TranslationTextComponent(seas.formattingText()).formatted(seas.getColor()));
                    i++;
                }
                tooltip.add(season);
            }
            if (!props.badSeasons().isEmpty()) {
                IFormattableTextComponent season = new TranslationTextComponent("season.bad").append(": ");

                int i = 0;
                for (EnumSeason seas : props.badSeasons())
                    if (!props.bestSeasons().contains(seas)) {
                        season.append(i != 0 ? "/" : "").formatted(TextFormatting.GRAY)
                                .append(new TranslationTextComponent(seas.formattingText()).formatted(seas.getColor()));
                        i++;
                    }
                if (i != 0)
                    tooltip.add(season);
            }
            IFormattableTextComponent growth = new TranslationTextComponent("growth").append(new StringTextComponent("" + props.growth()));
            ITextComponent harvest = new TranslationTextComponent("harvested").append(new StringTextComponent("" + props.maxDrops()));

            tooltip.add(growth.append(harvest));
        }
        boolean shift = Screen.hasShiftDown();
        if (shift) {
            FoodProperties food = DataPackHandler.getFoodStat(stack.getItem());
            if (food != null) {
                tooltip.add(new TranslationTextComponent("item.eaten"));
                String hp = (food.getHPGain() != 0 ? "HP " + food.getHPGain() : "");
                String hpPerc = (food.getHpPercentGain() != 0 ? "HP " + food.getHpPercentGain() + "%" : "");
                String rp = (food.getRPRegen() != 0 ? "RP " + food.getRPRegen() : "");
                String rpPerc = (food.getRpPercentRegen() != 0 ? "RP " + food.getRpPercentRegen() + "%" : "");

                tooltip.add(new StringTextComponent(" " + hp + " " + hpPerc + " " + rp + " " + rpPerc));
                for (Map.Entry<Attribute, Integer> entry : food.effects().entrySet()) {
                    IFormattableTextComponent comp = new StringTextComponent(" ").append(new TranslationTextComponent(entry.getKey().getTranslationKey())).append(new StringTextComponent(": " + entry.getValue()));
                    tooltip.add(comp);
                }
                for (Map.Entry<Attribute, Float> entry : food.effectsMultiplier().entrySet()) {
                    IFormattableTextComponent comp = new StringTextComponent(" ").append(new TranslationTextComponent(entry.getKey().getTranslationKey())).append(new StringTextComponent(": " + (int) (100 * entry.getValue()) + "%"));
                    tooltip.add(comp);
                }
            }
        }
        ItemStat stat = DataPackHandler.getStats(stack.getItem());
        if (stat != null) {
            CompoundNBT tag = ItemNBT.getItemNBT(stack);
            if (tag != null) {
                if (stack.getItem() instanceof IItemUsable) {
                    EnumElement element = EnumElement.fromName(tag.getString("Element"));
                    if (element != EnumElement.NONE) {
                        tooltip.add(new TranslationTextComponent("attribute." + element.getName()).formatted(element.getColor()));
                    }
                }
                if (ItemUtils.getBuyPrice(stack) > 0) {
                    IFormattableTextComponent text = new TranslationTextComponent("item.level").append(": " + tag.getInt("ItemLevel") + "  ");
                    IFormattableTextComponent buy = new TranslationTextComponent("item.buy").append(": " + ItemUtils.getBuyPrice(stack) + "  ");
                    IFormattableTextComponent sell = new TranslationTextComponent("item.sell").append(": " + ItemUtils.getSellPrice(stack));
                    tooltip.add(text.append(buy).append(sell));
                } else {
                    IFormattableTextComponent text = new TranslationTextComponent("item.level").append(": " + tag.getInt("ItemLevel") + "  ");
                    IFormattableTextComponent sell = new TranslationTextComponent("item.sell").append(": " + ItemUtils.getSellPrice(stack));
                    tooltip.add(text.append(sell));
                }
                if (shift) {
                    Map<Attribute, Integer> stats = ItemNBT.statIncrease(stack);
                    if (!stats.isEmpty()) {
                        String prefix = (stack.getItem() instanceof IItemWearable) ? "item.equipped" : "item.upgrade";
                        tooltip.add(new TranslationTextComponent(prefix));
                    }
                    for (Map.Entry<Attribute, Integer> entry : stats.entrySet()) {
                        IFormattableTextComponent comp = new StringTextComponent(" ").append(new TranslationTextComponent(entry.getKey().getTranslationKey())).append(new StringTextComponent(": " + entry.getValue()));

                        tooltip.add(comp);
                    }
                }
            }
        }
        return tooltip;
    }

    @SubscribeEvent
    public void worldRender(RenderWorldLastEvent event) {
        /*if(WeatherData.get(Minecraft.getMinecraft().world).currentWeather()==EnumWeather.RUNEY)
            this.renderRuneyWeather(Minecraft.getMinecraft(), event.getPartialTicks());
        if(ConfigHandler.MainConfig.debugAttack)
            RenderAttackAABBHandler.INST.render();*/
    }
}
