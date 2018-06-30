package com.flemmli97.runecraftory.common.init;

import com.flemmli97.runecraftory.common.lib.*;
import net.minecraft.item.*;
import net.minecraftforge.oredict.*;

public class OreDictInit
{
    public static final void init() {
        LibReference.logger.info("Adding Oredicts");
        OreDictionary.registerOre(LibOreDictionary.IRON, new ItemStack(ModItems.mineral, 1, 0));
        OreDictionary.registerOre(LibOreDictionary.BRONZE, new ItemStack(ModItems.mineral, 1, 1));
        OreDictionary.registerOre(LibOreDictionary.SILVER, new ItemStack(ModItems.mineral, 1, 2));
        OreDictionary.registerOre(LibOreDictionary.GOLD, new ItemStack(ModItems.mineral, 1, 3));
        OreDictionary.registerOre(LibOreDictionary.PLATINUM, new ItemStack(ModItems.mineral, 1, 4));
        OreDictionary.registerOre(LibOreDictionary.AMETHYST, new ItemStack(ModItems.jewel, 1, 0));
        OreDictionary.registerOre(LibOreDictionary.AQUAMARINE, new ItemStack(ModItems.jewel, 1, 1));
        OreDictionary.registerOre(LibOreDictionary.EMERALD, new ItemStack(ModItems.jewel, 1, 2));
        OreDictionary.registerOre(LibOreDictionary.RUBY, new ItemStack(ModItems.jewel, 1, 3));
        OreDictionary.registerOre(LibOreDictionary.SAPPHIRE, new ItemStack(ModItems.jewel, 1, 4));
        OreDictionary.registerOre(LibOreDictionary.DIAMOND, new ItemStack(ModItems.jewel, 1, 5));
        OreDictionary.registerOre(LibOreDictionary.STICK, new ItemStack(ModItems.sticks, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre(LibOreDictionary.FEATHER, new ItemStack(ModItems.feathers, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre(LibOreDictionary.BONES, new ItemStack(ModItems.bones, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre(LibOreDictionary.STONE, new ItemStack(ModItems.stones, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre(LibOreDictionary.STRING, new ItemStack(ModItems.strings, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre(LibOreDictionary.GUNPOWDER, new ItemStack(ModItems.powders, 1, 5));
        OreDictionary.registerOre(LibOreDictionary.TURNIPSEED, new ItemStack(ModItems.turnipSeeds));
        OreDictionary.registerOre(LibOreDictionary.PINKTURNIPSEED, new ItemStack(ModItems.turnipPinkSeeds));
        OreDictionary.registerOre(LibOreDictionary.PINKMELONSEED, new ItemStack(ModItems.pinkMelonSeeds));
        OreDictionary.registerOre(LibOreDictionary.CABBAGESEED, new ItemStack(ModItems.cabbageSeeds));
    }
}
