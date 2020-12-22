package com.flemmli97.runecraftory.api.items;

import com.flemmli97.runecraftory.api.enums.EnumWeaponType;
import net.minecraft.entity.player.ServerPlayerEntity;

public interface IItemUsable {

    EnumWeaponType getWeaponType();

    int itemCoolDownTicks();

    void onEntityHit(ServerPlayerEntity player);

    void onBlockBreak(ServerPlayerEntity player);
}
