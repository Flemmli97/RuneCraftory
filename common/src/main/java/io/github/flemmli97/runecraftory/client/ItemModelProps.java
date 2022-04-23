package io.github.flemmli97.runecraftory.client;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;

public class ItemModelProps {

    public static final ClampedItemPropertyFunction heldMainProp = (stack, world, entity, i) -> entity != null && entity.getMainHandItem() == stack ? 1 : 0;

    public static final ClampedItemPropertyFunction heldMainGlove = (stack, world, entity, i) -> {
        if (entity != null && entity.getMainHandItem() == stack)
            return entity instanceof LocalPlayer && ((LocalPlayer) entity).getModelName().equals("slim") ? 2 : 1;
        return 0;
    };

}
