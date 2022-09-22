package io.github.flemmli97.runecraftory.client;

import io.github.flemmli97.runecraftory.common.items.tools.ItemToolFishingRod;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.world.item.ItemStack;

public class ItemModelProps {

    public static final ClampedItemPropertyFunction heldMainProp = (stack, world, entity, i) -> entity != null && entity.getMainHandItem() == stack ? 1 : 0;

    public static final ClampedItemPropertyFunction heldMainGlove = (stack, world, entity, i) -> {
        if (entity != null && entity.getMainHandItem() == stack)
            return entity instanceof LocalPlayer && ((LocalPlayer) entity).getModelName().equals("slim") ? 2 : 1;
        return 0;
    };

    public static final ClampedItemPropertyFunction fishingRods = (stack, world, entity, i) -> {
        if (entity == null) {
            return 0.0F;
        } else {
            ItemStack main = entity.getMainHandItem();
            boolean flag = main == stack;
            boolean flag1 = entity.getOffhandItem() == stack;
            if (main.getItem() instanceof ItemToolFishingRod) {
                flag1 = false;
            }
            return (flag || flag1) && Platform.INSTANCE.getEntityData(entity).map(d -> d.fishingHook != null).orElse(false) ? 1.0F : 0.0F;
        }
    };
}
