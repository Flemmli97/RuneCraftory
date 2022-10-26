package io.github.flemmli97.runecraftory.common.utils;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.ItemStat;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.api.items.IItemUsable;
import io.github.flemmli97.runecraftory.api.items.StatItem;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.lib.LibNBT;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.TreeMap;

public class ItemNBT {

    public static int itemLevel(ItemStack stack) {
        CompoundTag tag = getItemNBT(stack);
        return tag != null ? Math.max(1, tag.getInt(LibNBT.Level)) : 1;
    }

    public static boolean addItemLevel(ItemStack stack) {
        int level = itemLevel(stack);
        if (level < 10) {
            CompoundTag tag = getItemNBT(stack);
            if (tag != null) {
                tag.putInt(LibNBT.Level, level + 1);
                return true;
            }
        }
        return false;
    }

    public static ItemStack getLeveledItem(ItemStack stack, int level) {
        if (shouldHaveLevel(stack)) {
            CompoundTag compound = ItemNBT.getItemNBT(stack);
            if (compound != null) {
                compound.putInt(LibNBT.Level, Mth.clamp(level, 1, 10));
            }
        }
        return stack;
    }

    public static Map<Attribute, Double> statIncrease(ItemStack stack) {
        CompoundTag compound = getItemNBT(stack);
        if (compound == null || !compound.contains(LibNBT.Stats)) {
            return DataPackHandler.getStats(stack.getItem()).map(ItemStat::itemStats).orElse(new TreeMap<>(ModAttributes.sorted));
        }
        CompoundTag tag = compound.getCompound(LibNBT.Stats);
        Map<Attribute, Double> map = new TreeMap<>(ModAttributes.sorted);
        for (String attName : tag.getAllKeys()) {
            Attribute att = PlatformUtils.INSTANCE.attributes().getFromId(new ResourceLocation(attName));
            if (PlatformUtils.INSTANCE.attributes().getIDFrom(att).toString().equals(attName))
                map.put(att, tag.getDouble(attName));
        }
        return map;
    }

    public static void setElement(EnumElement element, ItemStack stack) {
        CompoundTag tag = getItemNBT(stack);
        if (tag != null) {
            if (EnumElement.valueOf(tag.getString(LibNBT.Element)) == EnumElement.NONE) {
                tag.putString(LibNBT.Element, element.toString());
            } else {
                tag.putString(LibNBT.Element, EnumElement.NONE.toString());
            }
        }
    }

    public static EnumElement getElement(ItemStack stack) {
        CompoundTag tag = getItemNBT(stack);
        if (tag != null) {
            try {
                return EnumElement.valueOf(tag.getString(LibNBT.Element));
            } catch (IllegalArgumentException e) {
                return EnumElement.NONE;
            }
        }
        return shouldHaveElement(stack) ? DataPackHandler.getStats(stack.getItem()).map(ItemStat::element).orElse(EnumElement.NONE) : EnumElement.NONE;
    }

    public static ItemStack addUpgradeItem(ItemStack stack, ItemStack stackToAdd) {
        int level = itemLevel(stack);
        if (stackToAdd.isEmpty() || !ItemNBT.shouldHaveStats(stack) || level >= 10)
            return ItemStack.EMPTY;
        CompoundTag tag = getItemNBT(stack);
        if (tag == null)
            tag = new CompoundTag();
        tag.putInt(LibNBT.Level, level + 1);

        int similar = 0;
        ListTag upgrades = tag.getList(LibNBT.Upgrades, Tag.TAG_COMPOUND);
        //Searches for items, which are already applied to the itemstack. Reduces the efficiency for each identical item found.
        for (Tag item : upgrades) {
            CompoundTag nbt = (CompoundTag) item;
            if (PlatformUtils.INSTANCE.items().getIDFrom(stackToAdd.getItem()).toString().equals(nbt.getString("Id"))) {
                ++similar;
            }
        }
        float efficiency = (float) Math.max(0.0, 1 - 0.3 * similar);
        CompoundTag upgradeItem = new CompoundTag();
        upgradeItem.putString("Id", PlatformUtils.INSTANCE.items().getIDFrom(stackToAdd.getItem()).toString());
        upgradeItem.putInt("Level", ItemNBT.itemLevel(stackToAdd));
        upgrades.add(upgradeItem);
        tag.put(LibNBT.Upgrades, upgrades);
        if (stackToAdd.getItem() == ModItems.glass.get() && stack.getItem() instanceof IItemUsable)
            tag.putBoolean(LibNBT.MagnifyingGlass, true);
        ItemStat stat = DataPackHandler.getStats(stackToAdd.getItem()).orElse(null);
        if (stat != null) {
            if (!tag.contains(LibNBT.Stats) && !stat.itemStats().isEmpty()) {
                ItemStat base = DataPackHandler.getStats(stack.getItem()).orElse(null);
                if (base != null) {
                    CompoundTag statsTag = new CompoundTag();
                    for (Map.Entry<Attribute, Double> entry : base.itemStats().entrySet()) {
                        statsTag.putDouble(PlatformUtils.INSTANCE.attributes().getIDFrom(entry.getKey()).toString(), entry.getValue());
                    }
                    tag.put(LibNBT.Stats, statsTag);
                }
            }
            for (Map.Entry<Attribute, Double> entry : stat.itemStats().entrySet()) {
                updateStatIncrease(entry.getKey(), entry.getValue() * efficiency, tag.getCompound(LibNBT.Stats));
            }
            if (shouldHaveElement(stack) && stat.element() != EnumElement.NONE) {
                EnumElement current = getElement(stack);
                if (current == EnumElement.NONE) {
                    tag.putString(LibNBT.Element, stat.element().toString());
                } else {
                    tag.putString(LibNBT.Element, EnumElement.NONE.toString());
                }
            }
            if (stack.getItem() instanceof ItemStaffBase) {
                Platform.INSTANCE.getStaffData(stack).ifPresent(data -> {
                    if (stat.getTier1Spell() != null)
                        data.setTier1Spell(stat.getTier1Spell());
                    if (stat.getTier2Spell() != null)
                        data.setTier2Spell(stat.getTier2Spell());
                    if (stat.getTier3Spell() != null)
                        data.setTier3Spell(stat.getTier3Spell());
                });
            }
        }
        CompoundTag stackTag = stack.getOrCreateTag();
        stackTag.put(RuneCraftory.MODID, tag);
        return stack;
    }

    public static void updateStatIncrease(Attribute attribute, double amount, CompoundTag stats) {
        String att = PlatformUtils.INSTANCE.attributes().getIDFrom(attribute).toString();
        double oldValue = stats.getDouble(att);
        stats.putDouble(att, oldValue + Math.floor(amount));
    }

    public static CompoundTag getItemNBT(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains(RuneCraftory.MODID)) {
            return stack.getTag().getCompound(RuneCraftory.MODID);
        }
        return null;
    }

    public static boolean shouldHaveStats(ItemStack stack) {
        return stack.getItem() instanceof StatItem;
    }

    public static boolean shouldHaveLevel(ItemStack stack) {
        return shouldHaveStats(stack);
    }

    public static boolean shouldHaveElement(ItemStack stack) {
        return stack.getItem() instanceof IItemUsable;
    }

    public static boolean canBeUsedAsMagnifyingGlass(ItemStack stack) {
        if (stack.getItem() == ModItems.glass.get())
            return true;
        if (stack.hasTag()) {
            CompoundTag tag = stack.getTag().getCompound(RuneCraftory.MODID);
            return tag.getBoolean(LibNBT.MagnifyingGlass);
        }
        return false;
    }
}
