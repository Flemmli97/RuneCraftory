package io.github.flemmli97.runecraftory.common.items.creative;

import com.google.common.base.Suppliers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.function.Supplier;

public class ItemProp extends Item {

    private final Supplier<ItemStack> clientStack;

    public ItemProp(Properties properties, Supplier<ItemStack> clientStack) {
        super(properties);
        this.clientStack = Suppliers.memoize(clientStack::get);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, list, tooltipFlag);
        list.add(Component.translatable("runecraftory.tooltip.item.prop"));
    }

    public ItemStack clientItemStack() {
        return this.clientStack.get();
    }
}
