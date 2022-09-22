package io.github.flemmli97.runecraftory.mixinhelper;

import io.github.flemmli97.runecraftory.common.entities.misc.EntityCustomFishingHook;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;

public interface ExtendedFishingRodHookTrigger {

    void customTrigger(ServerPlayer player, ItemStack rod, EntityCustomFishingHook entity, Collection<ItemStack> stacks);

}
