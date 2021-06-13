package io.github.flemmli97.runecraftory.api.items;

import io.github.flemmli97.runecraftory.api.enums.EnumWeaponType;
import net.minecraft.entity.player.ServerPlayerEntity;

public interface IItemUsable {

    EnumWeaponType getWeaponType();

    int itemCoolDownTicks();

    void onEntityHit(ServerPlayerEntity player);

    void onBlockBreak(ServerPlayerEntity player);
}
