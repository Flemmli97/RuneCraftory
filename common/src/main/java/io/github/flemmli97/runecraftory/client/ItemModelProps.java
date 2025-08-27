package io.github.flemmli97.runecraftory.client;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.items.BabySpawnEgg;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolFishingRod;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ItemModelProps {

    public static final ResourceLocation SLIM_PLAYER_ID = RuneCraftory.modRes("slim_player");
    public static final ResourceLocation FISHING_ROD_ID = RuneCraftory.modRes("fishing");
    public static final ResourceLocation BABY_GENDER = RuneCraftory.modRes("baby_gender");

    public static final ClampedItemPropertyFunction SLIM_PLAYER_PROPERTY = (stack, world, entity, i) -> {
        if (entity != null) {
            return entity instanceof AbstractClientPlayer player && player.getSkin().model() == PlayerSkin.Model.SLIM ? 1 : 0;
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
            return (flag || flag1) && Platform.INSTANCE.getEntityData(entity).fishingHook != null ? 1.0F : 0.0F;
        }
    };

    public static final ClampedItemPropertyFunction BABY_GENDER_PROPS = (stack, world, entity, i) -> {
        if (stack.getItem() != RuneCraftoryItems.NPC_BABY.get())
            return 0;
        return BabySpawnEgg.isBoy(stack) ? 0 : 1;
    };
}
