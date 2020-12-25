package com.flemmli97.runecraftory.common.utils;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.datapack.ItemStat;
import com.flemmli97.runecraftory.api.enums.EnumElement;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import com.flemmli97.runecraftory.common.datapack.DataPackHandler;
import com.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import com.flemmli97.runecraftory.common.registry.ModAttributes;
import com.flemmli97.runecraftory.lib.LibNBT;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.TreeMap;

public class ItemNBT {

    public static int itemLevel(ItemStack stack) {
        CompoundNBT tag = getItemNBT(stack);
        return tag != null ? tag.getInt(LibNBT.Level) : 1;
    }

    public static boolean addItemLevel(ItemStack stack) {
        if (itemLevel(stack) < 10) {
            CompoundNBT tag = getItemNBT(stack);
            if (tag != null) {
                tag.putInt(LibNBT.Level, MathHelper.clamp(tag.getInt(LibNBT.Level) + 1, 1, 10));
                return true;
            }
        }
        return false;
    }

    public static ItemStack getLeveledItem(ItemStack stack, int level) {
        CompoundNBT compound = ItemNBT.getItemNBT(stack);
        if (compound != null) {
            compound.putInt(LibNBT.Level, MathHelper.clamp(level, 1, 10));
        }
        return stack;
    }

    public static Map<Attribute, Integer> statIncrease(ItemStack stack) {
        Map<Attribute, Integer> map = new TreeMap<>(ModAttributes.sorted);
        if (shouldHaveStats(stack)) {
            CompoundNBT compound = getItemNBT(stack);
            if (compound != null && !compound.contains(LibNBT.Stats)) {
                initNBT(stack);
                compound = getItemNBT(stack);
            }
            if (compound != null) {
                CompoundNBT tag = compound.getCompound(LibNBT.Stats);
                for (String attName : tag.keySet()) {
                    Attribute att = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(attName));
                    if (att.getRegistryName().toString().equals(attName))
                        map.put(att, tag.getInt(attName));
                }
            }
        }
        return map;
    }

    public static Map<Attribute, Integer> statBonusRaw(ItemStack stack) {
        Map<Attribute, Integer> map = new TreeMap<>(ModAttributes.sorted);
        if (shouldHaveStats(stack)) {
            CompoundNBT compound = getItemNBT(stack);
            if (compound != null && !compound.contains(LibNBT.Stats)) {
                initNBT(stack);
                compound = getItemNBT(stack);
            }
            if (compound != null) {
                CompoundNBT tag = compound.getCompound(LibNBT.Stats);
                for (String attName : tag.keySet()) {
                    Attribute att = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(attName));
                    if (att.getRegistryName().toString().equals(attName))
                        map.put(att, tag.getInt(attName));
                }
            }
        } else {
            ItemStat stat = DataPackHandler.getStats(stack.getItem());
            if (stat != null) {
                return stat.itemStats();
            }
        }
        return map;
    }

    public static void setElement(EnumElement element, ItemStack stack) {
        CompoundNBT tag = getItemNBT(stack);
        if (tag != null) {
            if (EnumElement.valueOf(tag.getString(LibNBT.Element)) == EnumElement.NONE) {
                tag.putString(LibNBT.Element, element.toString());
            } else {
                tag.putString(LibNBT.Element, EnumElement.NONE.toString());
            }
        }
    }

    public static EnumElement getElement(ItemStack stack) {
        CompoundNBT tag = getItemNBT(stack);
        if (tag != null) {
            try {
                return EnumElement.valueOf(tag.getString(LibNBT.Element));
            } catch (IllegalArgumentException e) {
                return EnumElement.NONE;
            }
        }
        return EnumElement.NONE;
    }

    public static void addUpgradeItem(ItemStack stack, ItemStack stackToAdd) {
        CompoundNBT tag = getItemNBT(stack);
        int level = itemLevel(stack);
        if (level >= 10)
            return;
        if (tag != null) {
            tag.putInt(LibNBT.Level, level + 1);
            float efficiency = 1.0f;
            float similar = 1.0f;

            CompoundNBT upgrades = tag.getCompound(LibNBT.Upgrades);
            //Searches for items, which are already applied to the itemstack. Reduces the efficiency for each identical item found.
            for (String item : upgrades.keySet()) {
                if (stackToAdd.getItem().getRegistryName().toString().equals(item)) {
                    efficiency = (float) Math.max(0.0, efficiency - Math.max(0.25, 0.5 / similar));
                    ++similar;
                }
            }
            upgrades.putInt(stackToAdd.getItem().getRegistryName().toString(), ItemNBT.itemLevel(stackToAdd));
            tag.put(LibNBT.Upgrades, upgrades);
            if (!shouldHaveStats(stackToAdd)) {
                ItemStat stat = DataPackHandler.getStats(stackToAdd.getItem());
                if (stat != null) {
                    for (Map.Entry<Attribute, Integer> entry : stat.itemStats().entrySet()) {
                        updateStatIncrease(entry.getKey(), Math.round(entry.getValue() * efficiency), tag);
                    }

                    if (stack.getItem() instanceof ItemStaffBase) {
                        stack.getCapability(CapabilityInsts.StaffCap).ifPresent(cap -> {
                            System.out.println(stat.getTier2Spell());
                            if (stat.getTier1Spell() != null)
                                cap.setTier1Spell(stat.getTier1Spell());
                            if (stat.getTier2Spell() != null)
                                cap.setTier2Spell(stat.getTier2Spell());
                            if (stat.getTier3Spell() != null)
                                cap.setTier3Spell(stat.getTier3Spell());
                        });
                    }
                }
            }
        }
    }

    public static void updateStatIncrease(Attribute attribute, int amount, CompoundNBT tag) {
        int oldValue = tag.getCompound(LibNBT.Stats).getInt(attribute.getRegistryName().toString());
        tag.getCompound(LibNBT.Stats).putInt(attribute.getRegistryName().toString(), oldValue + amount);
    }

    @Nullable
    public static CompoundNBT getItemNBT(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains(RuneCraftory.MODID)) {
            return stack.getTag().getCompound(RuneCraftory.MODID);
        }
        if (initNBT(stack)) {
            return stack.getTag().getCompound(RuneCraftory.MODID);
        }
        return null;
    }

    public static boolean initNBT(ItemStack stack) {
        return initNBT(stack, false);
    }

    public static boolean initNBT(ItemStack stack, boolean forced) {
        ItemStat stat = DataPackHandler.getStats(stack.getItem());
        if (stat != null || forced) {
            CompoundNBT stackTag = stack.getOrCreateTag();
            CompoundNBT compound = stackTag.getCompound(RuneCraftory.MODID);
            if (!compound.contains(LibNBT.Level))
                compound.putInt(LibNBT.Level, 1);
            if (shouldHaveStats(stack)) {
                if (!compound.contains(LibNBT.Upgrades))
                    compound.put(LibNBT.Upgrades, new ListNBT());
                if (stat != null) {
                    if (!compound.contains(LibNBT.Stats)) {
                        CompoundNBT stats = new CompoundNBT();
                        for (Map.Entry<Attribute, Integer> entry : stat.itemStats().entrySet()) {
                            stats.putInt(entry.getKey().getRegistryName().toString(), entry.getValue());
                        }
                        compound.put(LibNBT.Stats, stats);
                    }
                    if (stack.getItem() instanceof IItemUsable && !compound.contains(LibNBT.Element)) {
                        compound.putString(LibNBT.Element, stat.element().toString());
                    }

                    if (stack.getItem() instanceof ItemStaffBase) {
                        stack.getCapability(CapabilityInsts.StaffCap).ifPresent(cap -> {
                            if (stat.getTier1Spell() != null)
                                cap.setTier1Spell(stat.getTier1Spell());
                            if (stat.getTier2Spell() != null)
                                cap.setTier2Spell(stat.getTier2Spell());
                            if (stat.getTier3Spell() != null)
                                cap.setTier3Spell(stat.getTier3Spell());
                        });
                    }
                }
            }
            stackTag.put(RuneCraftory.MODID, compound);
            stack.setTag(stackTag);
            return true;
        }
        return false;
    }

    public static boolean shouldHaveStats(ItemStack stack) {
        return stack.getItem() instanceof IItemUsable || MobEntity.getSlotForItemStack(stack) != EquipmentSlotType.MAINHAND
                || stack.getItem() instanceof ToolItem || stack.getItem() instanceof SwordItem;
    }
}
