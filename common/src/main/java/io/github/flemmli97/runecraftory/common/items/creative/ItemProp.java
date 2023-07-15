package io.github.flemmli97.runecraftory.common.items.creative;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public class ItemProp extends Item {

    private final Function<ItemStack, ItemStack> clientStack;

    public ItemProp(Properties properties, Function<ItemStack, ItemStack> clientStack) {
        super(properties);
        this.clientStack = clientStack;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        tooltipComponents.add(new TranslatableComponent("tooltip.item.prop"));
    }

    public ItemStack clientItemStack(ItemStack real) {
        return this.clientStack.apply(real);
    }
}
