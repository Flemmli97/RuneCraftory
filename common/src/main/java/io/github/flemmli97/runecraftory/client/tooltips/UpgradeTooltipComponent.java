package io.github.flemmli97.runecraftory.client.tooltips;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class UpgradeTooltipComponent implements ClientTooltipComponent {

    private static final Supplier<ItemStack> MAGNIFYING_GLASS = Suppliers.memoize(() -> new ItemStack(ModItems.glass.get()));
    private static final Supplier<ItemStack> SCRAP_PLUS = Suppliers.memoize(() -> new ItemStack(ModItems.scrapPlus.get()));
    private static final Supplier<ItemStack> QUESTION = Suppliers.memoize(() -> new ItemStack(ModItems.unknown.get()));

    private final List<ItemStack> stacks = new ArrayList<>();

    public UpgradeTooltipComponent(UpgradeComponent comp) {
        ItemStack stack = comp.stack;
        if (ItemNBT.isWeapon(stack)) {
            EnumElement element = ItemNBT.getElement(stack);
            if (element != EnumElement.NONE)
                this.stacks.add(element.icon.get());
        }
        if (ItemNBT.doesFixedOneDamage(stack))
            this.stacks.add(SCRAP_PLUS.get());
        if (ItemNBT.isInvis(stack))
            this.stacks.add(QUESTION.get());
        if (ItemNBT.canBeUsedAsMagnifyingGlass(stack) && stack.getItem() != ModItems.glass.get())
            this.stacks.add(MAGNIFYING_GLASS.get());
        if (!this.stacks.isEmpty())
            this.stacks.add(ItemStack.EMPTY);
        ItemStack originItem = ItemNBT.getOriginItem(stack);
        if (!originItem.isEmpty())
            this.stacks.add(originItem);
    }

    public static boolean shouldAdd(ItemStack stack) {
        return (ItemNBT.canBeUsedAsMagnifyingGlass(stack) && stack.getItem() != ModItems.glass.get()) || (ItemNBT.isWeapon(stack) && ItemNBT.getElement(stack) != EnumElement.NONE) || ItemNBT.doesFixedOneDamage(stack)
                || ItemNBT.isInvis(stack) || !ItemNBT.getOriginItem(stack).isEmpty();
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
    public void renderImage(Font font, int mouseX, int mouseY, PoseStack poseStack, ItemRenderer itemRenderer, int blitOffset) {
        if (this.stacks.isEmpty())
            return;
        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushPose();
        float scale = 0.8f;
        modelViewStack.scale(scale, scale, 1);
        int x = (int) (mouseX / scale);
        int y = (int) (mouseY / scale);
        for (ItemStack stack : this.stacks) {
            if (!stack.isEmpty())
                this.renderItem(stack, itemRenderer, x, y);
            x += 16;
        }
        modelViewStack.popPose();
        RenderSystem.applyModelViewMatrix();
    }

    private void renderItem(ItemStack stack, ItemRenderer renderer, int x, int y) {
        renderer.renderAndDecorateItem(stack, x, y);
    }

    public record UpgradeComponent(ItemStack stack) implements TooltipComponent {
    }
}
