package io.github.flemmli97.runecraftory.client.tooltips;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.common.components.ItemStackHolder;
import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryDataComponentTypes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import io.github.flemmli97.runecraftory.common.utils.ItemComponentUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class UpgradeTooltipComponent implements ClientTooltipComponent {

    private static final Supplier<ItemStack> MAGNIFYING_GLASS = Suppliers.memoize(() -> new ItemStack(RuneCraftoryItems.GLASS.get()));
    private static final Supplier<ItemStack> SCRAP_PLUS = Suppliers.memoize(() -> new ItemStack(RuneCraftoryItems.SCRAP_PLUS.get()));
    private static final Supplier<ItemStack> QUESTION = Suppliers.memoize(() -> new ItemStack(RuneCraftoryItems.UNKNOWN.get()));

    private final List<ItemStack> stacks = new ArrayList<>();

    public UpgradeTooltipComponent(UpgradeComponent comp) {
        ItemStack stack = comp.stack;
        if (ItemComponentUtils.isWeapon(stack)) {
            ItemElement element = ItemComponentUtils.getElement(stack);
            if (element != ItemElement.NONE)
                this.stacks.add(element.icon.get());
        }
        if (stack.has(RuneCraftoryDataComponentTypes.SCRAP_METAL_PLUS.get()))
            this.stacks.add(SCRAP_PLUS.get());
        if (stack.has(RuneCraftoryDataComponentTypes.INVISIBLE.get()))
            this.stacks.add(QUESTION.get());
        if (stack.has(RuneCraftoryDataComponentTypes.MAGNIFYING_GLASS.get()) && stack.getItem() != RuneCraftoryItems.GLASS.get())
            this.stacks.add(MAGNIFYING_GLASS.get());
        if (!this.stacks.isEmpty())
            this.stacks.add(ItemStack.EMPTY);
        ItemStack originItem = stack.getOrDefault(RuneCraftoryDataComponentTypes.ORIGINAL_ITEM.get(), ItemStackHolder.DEFAULT).stack();
        if (!originItem.isEmpty())
            this.stacks.add(originItem);
    }

    public static boolean shouldAdd(ItemStack stack) {
        return (stack.has(RuneCraftoryDataComponentTypes.MAGNIFYING_GLASS.get()) && stack.getItem() != RuneCraftoryItems.GLASS.get()) || (ItemComponentUtils.isWeapon(stack) && ItemComponentUtils.getElement(stack) != ItemElement.NONE) || stack.has(RuneCraftoryDataComponentTypes.SCRAP_METAL_PLUS.get())
                || stack.has(RuneCraftoryDataComponentTypes.INVISIBLE.get()) || !stack.getOrDefault(RuneCraftoryDataComponentTypes.ORIGINAL_ITEM.get(), ItemStackHolder.DEFAULT).isEmpty();
    }

    @Override
    public int getHeight() {
        return 16;
    }

    @Override
    public int getWidth(Font font) {
        return this.stacks.size() * 16 + 2;
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        if (this.stacks.isEmpty())
            return;
        PoseStack pose = guiGraphics.pose();
        pose.pushPose();
        float scale = 0.8f;
        pose.scale(scale, scale, 1);
        x /= scale;
        y /= scale;
        for (ItemStack stack : this.stacks) {
            if (!stack.isEmpty()) {
                guiGraphics.renderItem(stack, x, y);
            }
            x += 16;
        }
        pose.popPose();
    }

    public record UpgradeComponent(ItemStack stack) implements TooltipComponent {

    }
}
