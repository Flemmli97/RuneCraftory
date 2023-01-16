package io.github.flemmli97.runecraftory.api.items;

import io.github.flemmli97.runecraftory.api.enums.EnumWeaponType;
import net.minecraft.server.level.ServerPlayer;

public interface IItemUsable {

    EnumWeaponType getWeaponType();

    void onBlockBreak(ServerPlayer player);
}
