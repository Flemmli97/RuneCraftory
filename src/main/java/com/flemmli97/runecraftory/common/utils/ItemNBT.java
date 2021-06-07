package com.flemmli97.runecraftory.common.utils;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.datapack.ItemStat;
import com.flemmli97.runecraftory.api.enums.EnumElement;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import com.flemmli97.runecraftory.common.datapack.DataPackHandler;
import com.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import com.flemmli97.runecraftory.common.lib.LibNBT;
import com.flemmli97.runecraftory.common.registry.ModAttributes;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
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
        /*CompoundNBT compound = ItemNBT.getItemNBT(stack);
        if (compound != null) {
            compound.putInt(LibNBT.Level, MathHelper.clamp(level, 1, 10));
        }*/
        return stack;
    }

    public static Map<Attribute, Double> statIncrease(ItemStack stack) {
        Map<Attribute, Double> map = new TreeMap<>(ModAttributes.sorted);
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
                        map.put(att, tag.getDouble(attName));
                }
            }
        }
        return map;
    }

    public static Map<Attribute, Double> statBonusRaw(ItemStack stack) {
        Map<Attribute, Double> map = new TreeMap<>(ModAttributes.sorted);
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
                        map.put(att, tag.getDouble(attName));
                }
            }
        }
        return DataPackHandler.getStats(stack.getItem()).map(ItemStat::itemStats).orElse(map);
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
            int similar = 1;

            ListNBT upgrades = tag.getList(LibNBT.Upgrades, Constants.NBT.TAG_COMPOUND);
            //Searches for items, which are already applied to the itemstack. Reduces the efficiency for each identical item found.
            for (INBT item : upgrades) {
                CompoundNBT nbt = (CompoundNBT) item;
                if (stackToAdd.getItem().getRegistryName().toString().equals(nbt.getString("Id"))) {
                    efficiency = (float) Math.max(0.0, efficiency - Math.max(0.125, 0.5 / similar));
                    ++similar;
                }
            }
            CompoundNBT upgradeItem = new CompoundNBT();
            upgradeItem.putString("Id", stackToAdd.getItem().getRegistryName().toString());
            upgradeItem.putInt("Level", ItemNBT.itemLevel(stackToAdd));
            upgrades.add(upgradeItem);
            tag.put(LibNBT.Upgrades, upgrades);
            if (!shouldHaveStats(stackToAdd)) {
                float effRes = efficiency;
                DataPackHandler.getStats(stackToAdd.getItem()).ifPresent(stat->{
                    for (Map.Entry<Attribute, Double> entry : stat.itemStats().entrySet()) {
                        updateStatIncrease(entry.getKey(), entry.getValue() * effRes, tag);
                    }
                    if (stat.element() != EnumElement.NONE) {
                        if (EnumElement.valueOf(tag.getString(LibNBT.Element)) == EnumElement.NONE) {
                            tag.putString(LibNBT.Element, stat.element().toString());
                        } else {
                            tag.putString(LibNBT.Element, EnumElement.NONE.toString());
                        }
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
                });
            }
        }
    }

    public static void updateStatIncrease(Attribute attribute, double amount, CompoundNBT tag) {
        double oldValue = tag.getCompound(LibNBT.Stats).getInt(attribute.getRegistryName().toString());
        tag.getCompound(LibNBT.Stats).putDouble(attribute.getRegistryName().toString(), oldValue + amount);
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
        Optional<ItemStat> ostat = DataPackHandler.getStats(stack.getItem());
        if (ostat.isPresent() || forced) {
            if (shouldHaveStats(stack)) {
                CompoundNBT stackTag = stack.getOrCreateTag();
                CompoundNBT compound = stackTag.getCompound(RuneCraftory.MODID);
                if (!compound.contains(LibNBT.Level))
                    compound.putInt(LibNBT.Level, 1);

                if (!compound.contains(LibNBT.Upgrades))
                    compound.put(LibNBT.Upgrades, new ListNBT());
                ostat.ifPresent(stat->{
                    if (!compound.contains(LibNBT.Stats)) {
                        CompoundNBT stats = new CompoundNBT();
                        for (Map.Entry<Attribute, Double> entry : stat.itemStats().entrySet()) {
                            stats.putDouble(entry.getKey().getRegistryName().toString(), entry.getValue());
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
                });
                stackTag.put(RuneCraftory.MODID, compound);
                stack.setTag(stackTag);
                return true;
            }
            return false;
        }
        return false;
    }

    public static boolean shouldHaveStats(ItemStack stack) {
        if (stack.getItem() instanceof BlockItem)
            return false;
        EquipmentSlotType slotType;
        return stack.getItem() instanceof IItemUsable || stack.getItem() instanceof ToolItem || stack.getItem() instanceof SwordItem
                || ((slotType = MobEntity.getSlotForItemStack(stack)) != null && slotType != EquipmentSlotType.MAINHAND);
    }
}
