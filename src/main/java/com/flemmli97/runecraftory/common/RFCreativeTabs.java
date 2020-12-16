package com.flemmli97.runecraftory.common;

import com.flemmli97.runecraftory.common.registry.ModItems;
import com.flemmli97.runecraftory.common.utils.ItemNBT;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.registries.ForgeRegistries;

public class RFCreativeTabs {

    public static CreativePlus weaponToolTab = new CreativePlus("runecraftory.weaponsTools") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.icon0.get());
        }
    };
/*
    public static CreativePlus equipment = new CreativePlus("runecraftory.equipment") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.cheapBracelet);
        }
    };*/

    public static CreativePlus upgradeItems = new CreativePlus("runecraftory.upgrade") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.dragonic.get());
        }
    };
/*
    public static CreativePlus blocks = new CreativePlus("runecraftory.blocks") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.itemBlockForge);
        }
    };*/

    public static CreativePlus medicine = new CreativePlus("runecraftory.medicine") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.recoveryPotion.get());
        }
    };
/*
    public static CreativePlus cast = new CreativePlus("runecraftory.cast") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.teleport);
        }
    };*/

    public static CreativePlus food = new CreativePlus("runecraftory.food") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.onigiri.get());
        }
    };

    public static CreativePlus crops = new CreativePlus("runecraftory.crops") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.turnipSeeds.get());
        }
    };

    public static CreativePlus monsters = new CreativePlus("runecraftory.monsters") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.icon1.get());
        }
    };

    private static abstract class CreativePlus extends ItemGroup {
        public CreativePlus(String label) {
            super(label);
        }

        @Override
        public void fill(NonNullList<ItemStack> list) {
            for (Item item : ForgeRegistries.ITEMS)
                item.fillItemGroup(this, list);
            list.forEach(ItemNBT::initNBT);
        }
    }
}
