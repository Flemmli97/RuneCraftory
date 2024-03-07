package io.github.flemmli97.runecraftory.common;

import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class RFCreativeTabs {

    public static final CreativeModeTab WEAPON_TOOL_TAB = Platform.INSTANCE.tab("weapons_tools", () -> new ItemStack(ModItems.SHORT_DAGGER.get()));

    public static final CreativeModeTab EQUIPMENT = Platform.INSTANCE.tab("equipment", () -> new ItemStack(ModItems.CHEAP_BRACELET.get()));

    public static final CreativeModeTab UPGRADE_ITEMS = Platform.INSTANCE.tab("upgrade", () -> new ItemStack(ModItems.DRAGONIC.get()));

    public static final CreativeModeTab BLOCKS = Platform.INSTANCE.tab("blocks", () -> new ItemStack(ModItems.MINERAL_IRON.get()));

    public static final CreativeModeTab MEDICINE = Platform.INSTANCE.tab("medicine", () -> new ItemStack(ModItems.RECOVERY_POTION.get()));

    public static final CreativeModeTab CAST = Platform.INSTANCE.tab("cast", () -> new ItemStack(ModItems.TELEPORT.get()));

    public static final CreativeModeTab FOOD = Platform.INSTANCE.tab("food", () -> new ItemStack(ModItems.ONIGIRI.get()));

    public static final CreativeModeTab CROPS = Platform.INSTANCE.tab("crops", () -> new ItemStack(ModItems.TURNIP_SEEDS.get()));

    public static final CreativeModeTab MONSTERS = Platform.INSTANCE.tab("monsters", () -> new ItemStack(ModItems.ICON_0.get()));
}
