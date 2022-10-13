package io.github.flemmli97.runecraftory.common.utils;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.ItemStat;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.api.items.IItemUsable;
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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class ItemNBT {

    public static int itemLevel(ItemStack stack) {
        CompoundTag tag = getItemNBT(stack);
        return tag != null ? tag.getInt(LibNBT.Level) : 1;
    }

    public static boolean addItemLevel(ItemStack stack) {
        if (itemLevel(stack) < 10) {
            CompoundTag tag = getItemNBT(stack);
            if (tag != null) {
                tag.putInt(LibNBT.Level, Mth.clamp(tag.getInt(LibNBT.Level) + 1, 1, 10));
                return true;
            }
        }
        return false;
    }

    public static ItemStack getLeveledItem(ItemStack stack, int level) {
        /*CompoundTag compound = ItemNBT.getItemNBT(stack);
        if (compound != null) {
            compound.putInt(LibNBT.Level, MathHelper.clamp(level, 1, 10));
        }*/
        return stack;
    }

    public static Map<Attribute, Double> statIncrease(ItemStack stack) {
        Map<Attribute, Double> map = new TreeMap<>(ModAttributes.sorted);
        if (shouldHaveStats(stack)) {
            CompoundTag compound = getItemNBT(stack);
            if (compound != null && !compound.contains(LibNBT.Stats)) {
                initNBT(stack);
                compound = getItemNBT(stack);
            }
            if (compound != null) {
                CompoundTag tag = compound.getCompound(LibNBT.Stats);
                for (String attName : tag.getAllKeys()) {
                    Attribute att = PlatformUtils.INSTANCE.attributes().getFromId(new ResourceLocation(attName));
                    if (PlatformUtils.INSTANCE.attributes().getIDFrom(att).toString().equals(attName))
                        map.put(att, tag.getDouble(attName));
                }
            }
        }
        return map;
    }

    public static Map<Attribute, Double> statBonusRaw(ItemStack stack) {
        Map<Attribute, Double> map = new TreeMap<>(ModAttributes.sorted);
        if (shouldHaveStats(stack)) {
            CompoundTag compound = getItemNBT(stack);
            if (compound != null && !compound.contains(LibNBT.Stats)) {
                initNBT(stack);
                compound = getItemNBT(stack);
            }
            if (compound != null) {
                CompoundTag tag = compound.getCompound(LibNBT.Stats);
                for (String attName : tag.getAllKeys()) {
                    Attribute att = PlatformUtils.INSTANCE.attributes().getFromId(new ResourceLocation(attName));
                    if (PlatformUtils.INSTANCE.attributes().getIDFrom(att).toString().equals(attName))
                        map.put(att, tag.getDouble(attName));
                }
            }
            return map;
        }
        return DataPackHandler.getStats(stack.getItem()).map(ItemStat::itemStats).orElse(map);
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
        return EnumElement.NONE;
    }

    public static void addUpgradeItem(ItemStack stack, ItemStack stackToAdd) {
        CompoundTag tag = getItemNBT(stack);
        int level = itemLevel(stack);
        if (level >= 10)
            return;
        if (tag != null) {
            tag.putInt(LibNBT.Level, level + 1);
            float efficiency = 1.0f;
            int similar = 1;

            ListTag upgrades = tag.getList(LibNBT.Upgrades, Tag.TAG_COMPOUND);
            //Searches for items, which are already applied to the itemstack. Reduces the efficiency for each identical item found.
            for (Tag item : upgrades) {
                CompoundTag nbt = (CompoundTag) item;
                if (PlatformUtils.INSTANCE.items().getIDFrom(stackToAdd.getItem()).toString().equals(nbt.getString("Id"))) {
                    efficiency = (float) Math.max(0.0, efficiency - Math.max(0.125, 0.5 / similar));
                    ++similar;
                }
            }
            CompoundTag upgradeItem = new CompoundTag();
            upgradeItem.putString("Id", PlatformUtils.INSTANCE.items().getIDFrom(stackToAdd.getItem()).toString());
            upgradeItem.putInt("Level", ItemNBT.itemLevel(stackToAdd));
            upgrades.add(upgradeItem);
            tag.put(LibNBT.Upgrades, upgrades);
            if (stackToAdd.getItem() == ModItems.glass.get() && stack.getItem() instanceof IItemUsable)
                tag.putBoolean("MagnifyingGlass", true);
            if (!shouldHaveStats(stackToAdd)) {
                float effRes = efficiency;
                DataPackHandler.getStats(stackToAdd.getItem()).ifPresent(stat -> {
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
                        Platform.INSTANCE.getStaffData(stack).ifPresent(data -> {
                            if (stat.getTier1Spell() != null)
                                data.setTier1Spell(stat.getTier1Spell());
                            if (stat.getTier2Spell() != null)
                                data.setTier2Spell(stat.getTier2Spell());
                            if (stat.getTier3Spell() != null)
                                data.setTier3Spell(stat.getTier3Spell());
                        });
                    }
                });
            }
        }
    }

    public static void updateStatIncrease(Attribute attribute, double amount, CompoundTag tag) {
        String att = PlatformUtils.INSTANCE.attributes().getIDFrom(attribute).toString();
        double oldValue = tag.getCompound(LibNBT.Stats).getDouble(att);
        tag.getCompound(LibNBT.Stats).putDouble(att, oldValue + amount);
    }

    public static CompoundTag getItemNBT(ItemStack stack) {
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
                CompoundTag stackTag = stack.getOrCreateTag();
                CompoundTag compound = stackTag.getCompound(RuneCraftory.MODID);
                if (!compound.contains(LibNBT.Level))
                    compound.putInt(LibNBT.Level, 1);

                if (!compound.contains(LibNBT.Upgrades))
                    compound.put(LibNBT.Upgrades, new ListTag());
                ostat.ifPresent(stat -> {
                    if (!compound.contains(LibNBT.Stats)) {
                        CompoundTag stats = new CompoundTag();
                        for (Map.Entry<Attribute, Double> entry : stat.itemStats().entrySet()) {
                            stats.putDouble(PlatformUtils.INSTANCE.attributes().getIDFrom(entry.getKey()).toString(), entry.getValue());
                        }
                        compound.put(LibNBT.Stats, stats);
                    }
                    if (stack.getItem() instanceof IItemUsable && !compound.contains(LibNBT.Element)) {
                        compound.putString(LibNBT.Element, stat.element().toString());
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
        EquipmentSlot slotType;
        return stack.getItem() instanceof IItemUsable
                || ((slotType = Mob.getEquipmentSlotForItem(stack)) != null && slotType != EquipmentSlot.MAINHAND);
    }

    public static boolean canBeUsedAsMagnifyingGlass(ItemStack stack) {
        if (stack.getItem() == ModItems.glass.get())
            return true;
        if (stack.hasTag()) {
            CompoundTag tag = stack.getTag().getCompound(RuneCraftory.MODID);
            return tag.getBoolean("MagnifyingGlass");
        }
        return false;
    }
}
