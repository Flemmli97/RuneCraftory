package com.flemmli97.runecraftory.api.items;

import com.flemmli97.runecraftory.api.enums.EnumWeaponType;
import net.minecraft.entity.player.PlayerEntity;

public interface IItemUsable extends IItemWearable{

    EnumWeaponType getWeaponType();

    int itemCoolDownTicks();

    void onEntityHit(PlayerEntity player);

    void onBlockBreak(PlayerEntity player);
}
