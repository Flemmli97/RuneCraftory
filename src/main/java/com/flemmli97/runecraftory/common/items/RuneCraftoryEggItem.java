package com.flemmli97.runecraftory.common.items;

import com.flemmli97.runecraftory.common.entities.IBaseMob;
import com.flemmli97.runecraftory.lib.LibConstants;
import com.flemmli97.tenshilib.common.item.SpawnEgg;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Supplier;

public class RuneCraftoryEggItem extends SpawnEgg {

    public RuneCraftoryEggItem(Supplier<? extends EntityType<?>> type, int primary, int secondary, Properties props) {
        super(type, primary, secondary, props);
    }

    @Override
    public boolean onEntitySpawned(Entity e, ItemStack stack, PlayerEntity player) {
        if (e instanceof IBaseMob && stack.hasDisplayName()) {
            int level = LibConstants.baseLevel;
            try {
                level = Integer.parseInt(stack.getDisplayName().getUnformattedComponentText());
            } catch (NumberFormatException ex) {
            }
            ((IBaseMob) e).setLevel(level);
        }
        return super.onEntitySpawned(e, stack, player);
    }

    @Override
    public ITextComponent getEntityName(ItemStack stack) {
        ITextComponent comp = super.getEntityName(stack);
        if (comp != null) {
            try {
                Integer.parseInt(comp.getUnformattedComponentText());
                return null;
            } catch (NumberFormatException e) {
                return comp;
            }
        }
        return null;
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        tooltip.add(new TranslationTextComponent("item.spawn.tooltip").formatted(TextFormatting.GOLD));
        super.addInformation(stack, world, tooltip, flag);
    }
}
