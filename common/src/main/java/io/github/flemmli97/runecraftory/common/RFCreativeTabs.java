package io.github.flemmli97.runecraftory.common;

import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class RFCreativeTabs {

    public static final CreativeModeTab weaponToolTab = Platform.INSTANCE.tab("weapons_tools", () -> new ItemStack(ModItems.shortDagger.get()));

    public static final CreativeModeTab equipment = Platform.INSTANCE.tab("equipment", () -> new ItemStack(ModItems.cheapBracelet.get()));

    public static final CreativeModeTab upgradeItems = Platform.INSTANCE.tab("upgrade", () -> new ItemStack(ModItems.dragonic.get()));

    public static final CreativeModeTab blocks = Platform.INSTANCE.tab("blocks", () -> new ItemStack(ModItems.mineralIron.get()));

    public static final CreativeModeTab medicine = Platform.INSTANCE.tab("medicine", () -> new ItemStack(ModItems.recoveryPotion.get()));

    public static final CreativeModeTab cast = Platform.INSTANCE.tab("cast", () -> new ItemStack(ModItems.teleport.get()));

    public static final CreativeModeTab food = Platform.INSTANCE.tab("food", () -> new ItemStack(ModItems.onigiri.get()));

    public static final CreativeModeTab crops = Platform.INSTANCE.tab("crops", () -> new ItemStack(ModItems.turnipSeeds.get()));

    public static final CreativeModeTab monsters = Platform.INSTANCE.tab("monsters", () -> new ItemStack(ModItems.icon0.get()));
}
