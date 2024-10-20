package io.github.flemmli97.runecraftory.common.utils;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.FoodProperties;
import io.github.flemmli97.runecraftory.api.datapack.ItemStat;
import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.api.items.IItemUsable;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.lib.LibConstants;
import io.github.flemmli97.runecraftory.common.lib.LibNBT;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ItemNBT {

    private static final List<ResourceLocation> WEAPON_ONLY = List.of(
            ModAttributes.PARA.getID(),
            ModAttributes.POISON.getID(),
            ModAttributes.SEAL.getID(),
            ModAttributes.SLEEP.getID(),
            ModAttributes.FATIGUE.getID(),
            ModAttributes.COLD.getID(),
            ModAttributes.FAINT.getID(),
            ModAttributes.DRAIN.getID()
    );
    private static final List<ResourceLocation> ARMOR_ONLY = List.of(
            ModAttributes.RES_WATER.getID(),
            ModAttributes.RES_EARTH.getID(),
            ModAttributes.RES_WIND.getID(),
            ModAttributes.RES_FIRE.getID(),
            ModAttributes.RES_DARK.getID(),
            ModAttributes.RES_LIGHT.getID(),
            ModAttributes.RES_LOVE.getID(),
            ModAttributes.RES_PARA.getID(),
            ModAttributes.RES_POISON.getID(),
            ModAttributes.RES_SEAL.getID(),
            ModAttributes.RES_SLEEP.getID(),
            ModAttributes.RES_FAT.getID(),
            ModAttributes.RES_COLD.getID(),
            ModAttributes.RES_DIZZY.getID(),
            ModAttributes.RES_CRIT.getID(),
            ModAttributes.RES_STUN.getID(),
            ModAttributes.RES_FAINT.getID(),
            ModAttributes.RES_DRAIN.getID()
    );

    public static int itemLevel(ItemStack stack) {
        CompoundTag tag = getItemNBT(stack);
        return tag != null ? Math.max(1, tag.getInt(LibNBT.LEVEL)) : 1;
    }

    public static boolean addItemLevel(ItemStack stack) {
        int level = itemLevel(stack);
        if (level < 10) {
            CompoundTag tag = getItemNBT(stack);
            if (tag != null) {
                tag.putInt(LibNBT.LEVEL, level + 1);
                return true;
            }
        }
        return false;
    }

    public static ItemStack getLeveledItem(ItemStack stack, int level) {
        if (shouldHaveLevel(stack)) {
            CompoundTag compound = ItemNBT.getItemNBT(stack);
            if (compound == null)
                compound = new CompoundTag();
            compound.putInt(LibNBT.LEVEL, Mth.clamp(level, 1, 10));
            stack.getOrCreateTag().put(RuneCraftory.MODID, compound);
        }
        return stack;
    }

    public static Map<Attribute, Double> statIncrease(ItemStack stack) {
        CompoundTag compound = getItemNBT(stack);
        if (compound == null || !compound.contains(LibNBT.BASE)) {
            return DataPackHandler.INSTANCE.itemStatManager().get(stack.getItem()).map(ItemStat::itemStats).orElse(new TreeMap<>(ModAttributes.SORTED));
        }
        Map<Attribute, Double> map = new TreeMap<>(ModAttributes.SORTED);
        CompoundTag base = compound.getCompound(LibNBT.BASE);
        for (String attName : base.getAllKeys()) {
            Attribute att = PlatformUtils.INSTANCE.attributes().getFromId(new ResourceLocation(attName));
            if (PlatformUtils.INSTANCE.attributes().getIDFrom(att).toString().equals(attName))
                map.put(att, base.getDouble(attName));
        }
        CompoundTag tag = compound.getCompound(LibNBT.STATS);
        for (String attName : tag.getAllKeys()) {
            Attribute att = PlatformUtils.INSTANCE.attributes().getFromId(new ResourceLocation(attName));
            if (PlatformUtils.INSTANCE.attributes().getIDFrom(att).toString().equals(attName))
                map.compute(att, (key, old) -> old == null ? tag.getDouble(attName) : old + tag.getDouble(attName));
        }
        return map;
    }

    public static Multimap<Attribute, AttributeModifier> getStatsAttributeMap(ItemStack stack, Multimap<Attribute, AttributeModifier> map, EquipmentSlot slot) {
        if (ItemNBT.shouldHaveStats(stack) && ItemUtils.slotOf(stack) == slot) {
            Multimap<Attribute, AttributeModifier> multimap = MultimapBuilder.treeKeys(ModAttributes.SORTED).hashSetValues().build();
            ItemNBT.statIncrease(stack).forEach((att, d) -> multimap.put(att, new AttributeModifier(LibConstants.EQUIPMENT_MODIFIERS[slot.ordinal()], "rf.stat_increase", d, AttributeModifier.Operation.ADDITION)));
            return multimap;
        }
        return map;
    }

    public static Pair<Map<Attribute, Double>, Map<Attribute, Double>> foodStats(ItemStack stack) {
        FoodProperties props = DataPackHandler.INSTANCE.foodManager().get(stack.getItem());
        if (props == null)
            return Pair.of(new TreeMap<>(ModAttributes.SORTED), new TreeMap<>(ModAttributes.SORTED));
        CompoundTag compound = getItemNBT(stack);
        if (compound == null) {
            return Pair.of(props.effects(), props.effectsMultiplier());
        }
        Map<Attribute, Double> map;
        Map<Attribute, Double> mapMulti;
        if (compound.contains(LibNBT.FOOD_STATS)) {
            CompoundTag tag = compound.getCompound(LibNBT.FOOD_STATS);
            map = new TreeMap<>(ModAttributes.SORTED);
            for (String attName : tag.getAllKeys()) {
                Attribute att = PlatformUtils.INSTANCE.attributes().getFromId(new ResourceLocation(attName));
                if (PlatformUtils.INSTANCE.attributes().getIDFrom(att).toString().equals(attName))
                    map.put(att, tag.getDouble(attName));
            }
        } else
            map = props.effects();
        if (compound.contains(LibNBT.FOOD_STATS_MULT)) {
            CompoundTag tag = compound.getCompound(LibNBT.FOOD_STATS_MULT);
            mapMulti = new TreeMap<>(ModAttributes.SORTED);
            for (String attName : tag.getAllKeys()) {
                Attribute att = PlatformUtils.INSTANCE.attributes().getFromId(new ResourceLocation(attName));
                if (PlatformUtils.INSTANCE.attributes().getIDFrom(att).toString().equals(attName))
                    mapMulti.put(att, tag.getDouble(attName));
            }
        } else
            mapMulti = props.effectsMultiplier();
        return Pair.of(map, mapMulti);
    }

    public static void setElement(EnumElement element, ItemStack stack) {
        CompoundTag tag = getItemNBT(stack);
        if (tag != null) {
            if (EnumElement.valueOf(tag.getString(LibNBT.ELEMENT)) == EnumElement.NONE) {
                tag.putString(LibNBT.ELEMENT, element.toString());
            } else {
                tag.putString(LibNBT.ELEMENT, EnumElement.NONE.toString());
            }
        }
    }

    public static EnumElement getElement(ItemStack stack) {
        CompoundTag tag = getItemNBT(stack);
        if (tag != null) {
            try {
                return EnumElement.valueOf(tag.getString(LibNBT.ELEMENT));
            } catch (IllegalArgumentException e) {
                return EnumElement.NONE;
            }
        }
        return isWeapon(stack) ? DataPackHandler.INSTANCE.itemStatManager().get(stack.getItem()).map(ItemStat::element).orElse(EnumElement.NONE) : EnumElement.NONE;
    }

    public static ItemStack addUpgradeItem(ItemStack stack, ItemStack stackToAdd, boolean crafting, EnumCrafting type) {
        int level = itemLevel(stack);
        if (stackToAdd.isEmpty() || !ItemNBT.shouldHaveStats(stack) || level >= 10)
            return stack;
        CompoundTag tag = getItemNBT(stack);
        if (tag == null)
            tag = new CompoundTag();
        ItemStat stat = DataPackHandler.INSTANCE.itemStatManager().get(stackToAdd.getItem()).orElse(null);
        if (ItemNBT.shouldHaveStats(stackToAdd)) {
            if (!crafting || tag.contains(LibNBT.ORIGINITEM))
                return stack;
            boolean lightOreApplied = tag.getBoolean(LibNBT.LIGHTORETAG);
            if (stack.is(RunecraftoryTags.EQUIPMENT)) {
                if (!stackToAdd.is(RunecraftoryTags.EQUIPMENT))
                    return stack;
                if (lightOreApplied)
                    return changeBaseItemTo(stack, stackToAdd, type);
                else if (stack.getItem() instanceof ArmorItem armor1 && stackToAdd.getItem() instanceof ArmorItem armor2 && armor1.getSlot() == armor2.getSlot())
                    return changeBaseItemTo(stack, stackToAdd, type);
            }
            if (stack.is(RunecraftoryTags.UPGRADABLE_HELD)) {
                if (!stackToAdd.is(RunecraftoryTags.UPGRADABLE_HELD))
                    return stack;
                if (lightOreApplied)
                    return changeBaseItemTo(stack, stackToAdd, type);
                else if (stack.getItem() instanceof IItemUsable armor1 && stackToAdd.getItem() instanceof IItemUsable armor2 && armor1.getWeaponType() == armor2.getWeaponType())
                    return changeBaseItemTo(stack, stackToAdd, type);
            }
            if (stat.getArmorEffect() != null && stat.getArmorEffect().canBeAppliedTo(stack))
                Platform.INSTANCE.getArmorEffects(stack).ifPresent(data -> data.addArmorEffects(stat.getArmorEffect()));
            return stack;
        }
        tag.putInt(LibNBT.LEVEL, !crafting ? level + 1 : level);

        float efficiency = 1;
        if (!crafting) {
            int similar = 0;
            ListTag upgrades = tag.getList(LibNBT.UPGRADES, Tag.TAG_COMPOUND);
            //Searches for items, which are already applied to the itemstack. Reduces the efficiency for each identical item found.
            for (Tag item : upgrades) {
                CompoundTag nbt = (CompoundTag) item;
                if (PlatformUtils.INSTANCE.items().getIDFrom(stackToAdd.getItem()).toString().equals(nbt.getString("Id"))) {
                    ++similar;
                }
            }
            efficiency = similar > 0 ? (float) (1 - Math.pow(0.5, similar)) : 1;
            CompoundTag upgradeItem = new CompoundTag();
            upgradeItem.putString("Id", PlatformUtils.INSTANCE.items().getIDFrom(stackToAdd.getItem()).toString());
            upgradeItem.putInt("Level", ItemNBT.itemLevel(stackToAdd));
            upgrades.add(upgradeItem);
            tag.put(LibNBT.UPGRADES, upgrades);
        } else {
            ListTag bonus = tag.getList(LibNBT.CRAFTING_BONUS, Tag.TAG_COMPOUND);
            CompoundTag bonusItem = new CompoundTag();
            bonusItem.putString("Id", PlatformUtils.INSTANCE.items().getIDFrom(stackToAdd.getItem()).toString());
            bonusItem.putInt("Level", ItemNBT.itemLevel(stackToAdd));
            bonus.add(bonusItem);
            tag.put(LibNBT.CRAFTING_BONUS, bonus);
        }
        //Special Item Tags
        if (stackToAdd.getItem() == ModItems.GLASS.get() && stack.getItem() instanceof IItemUsable)
            tag.putBoolean(LibNBT.MAGNIFYING_GLASS, true);
        if (stackToAdd.getItem() == ModItems.SCRAP_PLUS.get() && stack.getItem() instanceof IItemUsable)
            tag.putBoolean(LibNBT.SCRAP_METAL_PLUS, true);
        boolean hasObjectX = tag.getBoolean(LibNBT.OBJECT_X);
        if (stackToAdd.getItem() == ModItems.OBJECT_X.get())
            tag.putBoolean(LibNBT.OBJECT_X, !hasObjectX);
        if (type == EnumCrafting.FORGE && stackToAdd.getItem() == ModItems.INVIS_STONE.get())
            tag.putBoolean(LibNBT.INVIS, true);
        if (type == EnumCrafting.FORGE && stackToAdd.is(RunecraftoryTags.SCALES))
            tag.putBoolean(LibNBT.DRAGON_SCALE, true);
        if (crafting && stackToAdd.getItem() == ModItems.LIGHT_ORE.get() && !tag.contains(LibNBT.ORIGINITEM))
            tag.putBoolean(LibNBT.LIGHTORETAG, true);
        if (stackToAdd.getItem() == ModItems.GLITTA_AUGITE.get())
            tag.putBoolean(LibNBT.GLITTA_AUGITE, true);
        if (stackToAdd.getItem() == ModItems.RACCOON_LEAF.get())
            tag.putBoolean(LibNBT.RACCOON_LEAF, true);
        // Apply double/tenfold steel. Works only once
        boolean applyDoubleSteel = tag.getBoolean(LibNBT.DOUBLE_STEEL);
        if (!tag.contains(LibNBT.DOUBLE_STEEL)) {
            if (stackToAdd.getItem() == ModItems.STEEL_DOUBLE.get())
                tag.putBoolean(LibNBT.DOUBLE_STEEL, true);
        } else if (applyDoubleSteel) {
            tag.putBoolean(LibNBT.DOUBLE_STEEL, false);
        }
        boolean applyTenSteel = tag.getBoolean(LibNBT.TENFOLD_STEEL);
        if (!tag.contains(LibNBT.TENFOLD_STEEL)) {
            if (stackToAdd.getItem() == ModItems.STEEL_TEN.get())
                tag.putBoolean(LibNBT.TENFOLD_STEEL, true);
        } else if (applyTenSteel) {
            tag.putBoolean(LibNBT.TENFOLD_STEEL, false);
        }

        if (stat != null) {
            if (!tag.contains(LibNBT.BASE) && !stat.itemStats().isEmpty()) {
                ItemStat base = DataPackHandler.INSTANCE.itemStatManager().get(stack.getItem()).orElse(null);
                if (base != null) {
                    CompoundTag statsTag = new CompoundTag();
                    for (Map.Entry<Attribute, Double> entry : base.itemStats().entrySet()) {
                        if (entry.getKey() == ModAttributes.ATTACK_RANGE.get()) {
                            if (stackToAdd.getItem() == ModItems.RACCOON_LEAF.get() && tag.getBoolean(LibNBT.RACCOON_LEAF))
                                continue;
                            if (stackToAdd.getItem() == ModItems.GLITTA_AUGITE.get() && tag.getBoolean(LibNBT.GLITTA_AUGITE))
                                continue;
                        }
                        statsTag.putDouble(PlatformUtils.INSTANCE.attributes().getIDFrom(entry.getKey()).toString(), entry.getValue());
                    }
                    tag.put(LibNBT.BASE, statsTag);
                }
            }

            List<ResourceLocation> blacklist = List.of();
            if (type == EnumCrafting.FORGE)
                blacklist = ARMOR_ONLY;
            if (type == EnumCrafting.ARMOR)
                blacklist = WEAPON_ONLY;
            CompoundTag statCompound = tag.getCompound(LibNBT.STATS);
            for (Map.Entry<Attribute, Double> entry : stat.itemStats().entrySet()) {
                if (blacklist.contains(PlatformUtils.INSTANCE.attributes().getIDFrom(entry.getKey())))
                    continue;
                double amount = entry.getValue() * efficiency;
                if (hasObjectX)
                    amount *= -1;
                if (applyDoubleSteel)
                    amount *= 2;
                if (applyTenSteel)
                    amount *= 8;
                updateStatIncrease(entry.getKey(), amount, statCompound);
            }
            tag.put(LibNBT.STATS, statCompound);
            if (!tag.contains(LibNBT.ELEMENT))
                tag.putString(LibNBT.ELEMENT, getElement(stack).toString());
            if (isWeapon(stack)) {
                EnumElement current = getElement(stack);
                if (stat.element() != EnumElement.NONE) {
                    if (current == EnumElement.NONE) {
                        tag.putString(LibNBT.ELEMENT, stat.element().toString());
                    } else
                        tag.putString(LibNBT.ELEMENT, EnumElement.NONE.toString());
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
            if (stat.getArmorEffect() != null && stat.getArmorEffect().canBeAppliedTo(stack))
                Platform.INSTANCE.getArmorEffects(stack).ifPresent(data -> data.addArmorEffects(stat.getArmorEffect()));
        }
        CompoundTag stackTag = stack.getOrCreateTag();
        stackTag.put(RuneCraftory.MODID, tag);
        return stack;
    }

    private static ItemStack changeBaseItemTo(ItemStack stack, ItemStack toApply, EnumCrafting crafting) {
        ItemStat stat = DataPackHandler.INSTANCE.itemStatManager().get(toApply.getItem()).orElse(null);
        CompoundTag tag = new CompoundTag();
        //Setup base stuff
        if (stat != null) {
            if (!stat.itemStats().isEmpty()) {
                ItemStat base = DataPackHandler.INSTANCE.itemStatManager().get(toApply.getItem()).orElse(null);
                if (base != null) {
                    CompoundTag statsTag = new CompoundTag();
                    Map<Attribute, Double> origin = DataPackHandler.INSTANCE.itemStatManager().get(stack.getItem())
                            .map(ItemStat::itemStats).orElse(Map.of());
                    for (Map.Entry<Attribute, Double> entry : base.itemStats().entrySet()) {
                        if (entry.getKey() == ModAttributes.ATTACK_SPEED.get() || entry.getKey() == ModAttributes.ATTACK_RANGE.get()) //Do not copy att speed and range
                            statsTag.putDouble(PlatformUtils.INSTANCE.attributes().getIDFrom(entry.getKey()).toString(), origin.getOrDefault(entry.getKey(), 5d));
                        else
                            statsTag.putDouble(PlatformUtils.INSTANCE.attributes().getIDFrom(entry.getKey()).toString(), entry.getValue());
                    }
                    tag.put(LibNBT.BASE, statsTag);
                }
            }
            tag.putString(LibNBT.ELEMENT, stat.element().toString());
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
            if (stat.getArmorEffect() != null && stat.getArmorEffect().canBeAppliedTo(stack))
                Platform.INSTANCE.getArmorEffects(stack).ifPresent(data -> data.addArmorEffects(stat.getArmorEffect()));
        }
        tag.putString(LibNBT.ORIGINITEM, PlatformUtils.INSTANCE.items().getIDFrom(toApply.getItem()).toString());
        CompoundTag stackTag = stack.getOrCreateTag();
        stackTag.put(RuneCraftory.MODID, tag);
        //Reapply all items used to craft the applied item
        CompoundTag other = ItemNBT.getItemNBT(toApply);
        if (other != null) {
            ListTag bonus = other.getList(LibNBT.CRAFTING_BONUS, Tag.TAG_COMPOUND);
            bonus.forEach(t -> {
                CompoundTag nbt = (CompoundTag) t;
                Item item = PlatformUtils.INSTANCE.items().getFromId(new ResourceLocation(nbt.getString("Id")));
                if (item != Items.AIR)
                    addUpgradeItem(stack, new ItemStack(item), true, crafting);
            });
        }
        return stack;
    }

    public static ItemStack addFoodBonusItem(ItemStack stack, ItemStack stackToAdd) {
        if (stackToAdd.isEmpty())
            return stack;
        CompoundTag tag = getItemNBT(stack);
        if (tag == null)
            tag = new CompoundTag();
        ListTag bonus = tag.getList(LibNBT.CRAFTING_BONUS, Tag.TAG_COMPOUND);
        CompoundTag bonusItem = new CompoundTag();
        bonusItem.putString("Id", PlatformUtils.INSTANCE.items().getIDFrom(stackToAdd.getItem()).toString());
        bonusItem.putInt("Level", ItemNBT.itemLevel(stackToAdd));
        bonus.add(bonusItem);
        tag.put(LibNBT.CRAFTING_BONUS, bonus);

        FoodProperties props = DataPackHandler.INSTANCE.foodManager().get(stackToAdd.getItem());
        if (props != null) {
            if (!tag.contains(LibNBT.FOOD_STATS)) {
                FoodProperties base = DataPackHandler.INSTANCE.foodManager().get(stack.getItem());
                if (base != null) {
                    CompoundTag statsTag = new CompoundTag();
                    for (Map.Entry<Attribute, Double> entry : base.effects().entrySet()) {
                        statsTag.putDouble(PlatformUtils.INSTANCE.attributes().getIDFrom(entry.getKey()).toString(), entry.getValue());
                    }
                    tag.put(LibNBT.FOOD_STATS, statsTag);
                    statsTag = new CompoundTag();
                    for (Map.Entry<Attribute, Double> entry : base.effectsMultiplier().entrySet()) {
                        statsTag.putDouble(PlatformUtils.INSTANCE.attributes().getIDFrom(entry.getKey()).toString(), entry.getValue());
                    }
                    tag.put(LibNBT.FOOD_STATS_MULT, statsTag);
                }
            }
            boolean hasObjectX = tag.getBoolean(LibNBT.OBJECT_X);
            if (stackToAdd.getItem() == ModItems.OBJECT_X.get())
                tag.putBoolean(LibNBT.OBJECT_X, !hasObjectX);
            for (Map.Entry<Attribute, Double> entry : props.cookingBonus().entrySet()) {
                double amount = entry.getValue();
                if (hasObjectX)
                    amount *= -1;
                updateStatIncrease(entry.getKey(), amount, tag.getCompound(LibNBT.FOOD_STATS));
            }
            for (Map.Entry<Attribute, Double> entry : props.cookingBonusPercent().entrySet()) {
                double amount = entry.getValue();
                if (hasObjectX)
                    amount *= -1;
                updateStatIncrease(entry.getKey(), amount, tag.getCompound(LibNBT.FOOD_STATS_MULT));
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

    private static void addAsAttributeModifier(Attribute attribute, double val, ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        ListTag list = tag.contains("AttributeModifiers", Tag.TAG_LIST) ? tag.getList("AttributeModifiers", Tag.TAG_COMPOUND) : new ListTag();
        EquipmentSlot slot = ItemUtils.slotOf(stack);
        CompoundTag attComp = new CompoundTag();
        String att = PlatformUtils.INSTANCE.attributes().getIDFrom(attribute).toString();
        for (int i = 0; i < list.size(); ++i) {
            CompoundTag compoundTag = list.getCompound(i);
            if (compoundTag.getString("AttributeName").equals(att) && compoundTag.getString("Slot").equals(slot.getName())) {
                AttributeModifier attributeModifier = AttributeModifier.load(compoundTag);
                if (attributeModifier != null && attributeModifier.getId().equals(LibConstants.EQUIPMENT_MODIFIERS[slot.ordinal()])) {
                    val += attributeModifier.getAmount();
                    attComp = compoundTag;
                    break;
                }
            }
        }
        list.remove(attComp);
        AttributeModifier mod = new AttributeModifier(LibConstants.EQUIPMENT_MODIFIERS[slot.ordinal()], "rf.stat_increase", val, AttributeModifier.Operation.ADDITION);
        attComp = mod.save();
        attComp.putString("AttributeName", att);
        attComp.putString("Slot", slot.getName());
        list.add(attComp);
        tag.put("AttributeModifiers", list);
    }

    public static CompoundTag getItemNBT(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains(RuneCraftory.MODID)) {
            return stack.getTag().getCompound(RuneCraftory.MODID);
        }
        return null;
    }

    public static boolean shouldHaveStats(ItemStack stack) {
        return stack.is(RunecraftoryTags.UPGRADABLE_HELD) || stack.is(RunecraftoryTags.EQUIPMENT);
    }

    public static boolean shouldHaveLevel(ItemStack stack) {
        return shouldHaveStats(stack) || stack.is(RunecraftoryTags.SPELLS);
    }

    public static boolean isWeapon(ItemStack stack) {
        return stack.is(RunecraftoryTags.UPGRADABLE_HELD);
    }

    public static boolean canBeUsedAsMagnifyingGlass(ItemStack stack) {
        if (stack.getItem() == ModItems.GLASS.get())
            return true;
        if (stack.hasTag()) {
            CompoundTag tag = stack.getTag().getCompound(RuneCraftory.MODID);
            return tag.getBoolean(LibNBT.MAGNIFYING_GLASS);
        }
        return false;
    }

    public static boolean doesFixedOneDamage(ItemStack stack) {
        if (stack.hasTag()) {
            CompoundTag tag = stack.getTag().getCompound(RuneCraftory.MODID);
            return tag.getBoolean(LibNBT.SCRAP_METAL_PLUS);
        }
        return false;
    }

    public static boolean reverseStats(ItemStack stack) {
        if (stack.hasTag()) {
            CompoundTag tag = stack.getTag().getCompound(RuneCraftory.MODID);
            return tag.getBoolean(LibNBT.OBJECT_X);
        }
        return false;
    }

    public static boolean isInvis(ItemStack stack) {
        if (stack.hasTag()) {
            CompoundTag tag = stack.getTag().getCompound(RuneCraftory.MODID);
            return tag.getBoolean(LibNBT.INVIS);
        }
        return false;
    }

    public static boolean hasDragonScaleUpgrade(ItemStack stack) {
        if (stack.hasTag()) {
            CompoundTag tag = stack.getTag().getCompound(RuneCraftory.MODID);
            return tag.getBoolean(LibNBT.DRAGON_SCALE);
        }
        return false;
    }

    public static ItemStack getOriginItem(ItemStack stack) {
        if (shouldHaveStats(stack) && stack.hasTag()) {
            CompoundTag tag = stack.getTag().getCompound(RuneCraftory.MODID);
            String s = tag.getString(LibNBT.ORIGINITEM);
            if (s.isEmpty())
                return ItemStack.EMPTY;
            return new ItemStack(PlatformUtils.INSTANCE.items().getFromId(new ResourceLocation(s)));
        }
        return ItemStack.EMPTY;
    }

    public static boolean usedLightOre(ItemStack stack) {
        if (shouldHaveStats(stack) && stack.hasTag()) {
            CompoundTag tag = stack.getTag().getCompound(RuneCraftory.MODID);
            String s = tag.getString(LibNBT.ORIGINITEM);
            if (s.isEmpty())
                return false;
            ItemStack changed = new ItemStack(PlatformUtils.INSTANCE.items().getFromId(new ResourceLocation(s)));
            return changed.isEmpty() && tag.getBoolean(LibNBT.LIGHTORETAG);
        }
        return false;
    }

    public static double attackSpeedModifier(LivingEntity entity) {
        return entity.getAttributeValue(ModAttributes.ATTACK_SPEED.get());
    }
}
