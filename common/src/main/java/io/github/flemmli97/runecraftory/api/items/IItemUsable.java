package io.github.flemmli97.runecraftory.api.items;

import io.github.flemmli97.runecraftory.api.enums.EnumWeaponType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public interface IItemUsable {

    EnumWeaponType getWeaponType();

    int itemCoolDownTicks();

    void onEntityHit(ServerPlayer player, ItemStack stack);

    void onBlockBreak(ServerPlayer player);
}
