package io.github.flemmli97.runecraftory.fabric.mixinhelper;

import io.github.flemmli97.runecraftory.client.ClientCalls;
import io.github.flemmli97.runecraftory.fabric.client.TooltipRegistry;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TooltipHelper {

    public static void gatherComponents(ItemStack stack, int screenWidth, int screenHeight, List<ClientTooltipComponent> components) {
        List<TooltipComponent> elements = new ArrayList<>();
        ClientCalls.tooltipComponentEvent(stack, elements::add, screenWidth, screenHeight);
        components.addAll(1, elements.stream().map(c -> {
            Function<TooltipComponent, ClientTooltipComponent> factory = TooltipRegistry.get(c.getClass());
            if (factory != null)
                return factory.apply(c);
            return ClientTooltipComponent.create(c);
        }).toList());
    }
}
