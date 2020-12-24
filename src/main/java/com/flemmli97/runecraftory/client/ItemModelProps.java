package com.flemmli97.runecraftory.client;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.item.IItemPropertyGetter;

public class ItemModelProps {

    public static final IItemPropertyGetter heldMainProp = (stack, world, entity) -> entity != null && entity.getHeldItemMainhand().getItem() == stack.getItem() ? 1 : 0;

    public static final IItemPropertyGetter heldMainGlove = (stack, world, entity) -> entity instanceof AbstractClientPlayerEntity && ((AbstractClientPlayerEntity) entity).getSkinType().equals("slim") ? 2 : entity != null && entity.getHeldItemMainhand().getItem() == stack.getItem() ? 1 : 0;

}
