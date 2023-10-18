package io.github.flemmli97.runecraftory.client;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolFishingRod;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ItemModelProps {

    public static final ResourceLocation HELD_ID = new ResourceLocation(RuneCraftory.MODID, "held");
    public static final ResourceLocation GLOVE_HELD_ID = new ResourceLocation(RuneCraftory.MODID, "glove_held");
    public static final ResourceLocation FISHING_ROD_ID = new ResourceLocation(RuneCraftory.MODID, "fishing");

    public static int HELD_TYPE;

    public static final ClampedItemPropertyFunction HELD_MAIN_PROP = (stack, world, entity, i) -> HELD_TYPE;

    public static final ClampedItemPropertyFunction HELD_MAIN_GLOVE = (stack, world, entity, i) -> {
        if (entity != null) {
            if (HELD_TYPE == 0)
                return 0;
            int skin = entity instanceof AbstractClientPlayer player && player.getModelName().equals("slim") ? 1 : 0;
            return (HELD_TYPE + skin * 2) * 0.25f;
        }
        return 0;
    };

    public static final ClampedItemPropertyFunction FISHING_RODS = (stack, world, entity, i) -> {
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
