package io.github.flemmli97.runecraftory.fabric.client;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Simple tooltip registry. Similar to forge impl
 */
public class TooltipRegistry {

    private static final Map<Class<? extends TooltipComponent>, Function<TooltipComponent, ClientTooltipComponent>> components = new ConcurrentHashMap<>();

    public static Function<TooltipComponent, ClientTooltipComponent> get(Class<? extends TooltipComponent> clss) {
        return components.get(clss);
    }

    @SuppressWarnings("unchecked")
    public static <T extends TooltipComponent> void registerFactory(Class<T> clss, Function<? super T, ? extends ClientTooltipComponent> factory) {
        components.put(clss, (Function<TooltipComponent, ClientTooltipComponent>) factory);
    }
}
