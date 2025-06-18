package io.github.flemmli97.runecraftory.common.utils;

import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.api.datapack.FoodProperties;
import io.github.flemmli97.runecraftory.api.datapack.ItemStat;
import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.components.ArmorEffectData;
import io.github.flemmli97.runecraftory.common.components.ItemAttributeData;
import io.github.flemmli97.runecraftory.common.components.ItemStackHolder;
import io.github.flemmli97.runecraftory.common.components.ListItemStackHolder;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModDataComponentTypes;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.TreeMap;

public class ItemNBT {

    public static int itemLevel(ItemStack stack) {
        return stack.getOrDefault(ModDataComponentTypes.LEVEL.get(), 1);
    }

    public static ItemStack getLeveledItem(ItemStack stack, int level) {
        if (shouldHaveLevel(stack)) {
            stack.set(ModDataComponentTypes.LEVEL.get(), Mth.clamp(level, 1, 10));
        }
        return stack;
    }

    public static Map<Holder<Attribute>, Double> statIncrease(ItemStack stack) {
        ItemAttributeData stats = stack.get(ModDataComponentTypes.STATS.get());
        if (stats == null) {
            return DataPackHandler.INSTANCE.itemStatManager().get(stack.getItem()).map(ItemStat::itemStats)
                    .orElse(Map.of());
        }
        return stats.getTotalStats();
    }

    public static Multimap<Holder<Attribute>, AttributeModifier> getStatsAttributeMap(ItemStack stack, Multimap<Holder<Attribute>, AttributeModifier> map, EquipmentSlot slot) {
//        if (ItemNBT.shouldHaveStats(stack) && ItemUtils.slotOf(stack) == slot) {
//            Multimap<Holder<Attribute>, AttributeModifier> multimap = MultimapBuilder.treeKeys(ModAttributes.SORTED).hashSetValues().build();
//            ItemNBT.statIncrease(stack).forEach((att, d) -> multimap.put(att, new AttributeModifier(LibConstants.EQUIPMENT_MODIFIERS[slot.ordinal()], d, AttributeModifier.Operation.ADD_VALUE)));
//            return multimap;
//        }
        return map;
    }

    public static Pair<Map<Holder<Attribute>, Double>, Map<Holder<Attribute>, Double>> foodStats(ItemStack stack) {
        FoodProperties props = DataPackHandler.INSTANCE.foodManager().get(stack.getItem());
        if (props == null)
            return Pair.of(new TreeMap<>(ModAttributes.SORTED), new TreeMap<>(ModAttributes.SORTED));
        ItemAttributeData data = stack.get(ModDataComponentTypes.FOOD_BUFF.get());
        if (data == null) {
            return Pair.of(props.effects(), props.effectsMultiplier());
        }
        return Pair.of(data.getBaseStats(), data.getStats());
    }

    public static void setElement(EnumElement element, ItemStack stack) {
        EnumElement stackElement = stack.get(ModDataComponentTypes.ELEMENT.get());
        stack.set(ModDataComponentTypes.ELEMENT.get(), stackElement == null || stackElement == element ? element : EnumElement.NONE);
    }

    public static EnumElement getElement(ItemStack stack) {
        EnumElement stackElement = stack.get(ModDataComponentTypes.ELEMENT.get());
        if (stackElement != null) {
            return stackElement;
        }
        return isWeapon(stack) ? DataPackHandler.INSTANCE.itemStatManager().get(stack.getItem()).map(ItemStat::element).orElse(EnumElement.NONE) : EnumElement.NONE;
    }

    public static ItemStack addUpgradeItem(ItemStack stack, ItemStack upgrade, boolean crafting, EnumCrafting type) {
        int level = itemLevel(stack);
        if (upgrade.isEmpty() || !ItemNBT.shouldHaveStats(stack) || level >= 10)
            return stack;
        ItemStat stat = DataPackHandler.INSTANCE.itemStatManager().get(upgrade.getItem()).orElse(null);
        if (ItemNBT.shouldHaveStats(upgrade)) {
            if (!crafting || stack.has(ModDataComponentTypes.ORIGINAL_ITEM.get()))
                return stack;
            boolean lightOre = stack.getOrDefault(ModDataComponentTypes.LIGHT_ORE.get(), false);
            if (stack.is(RunecraftoryTags.Items.EQUIPMENT)) {
                if (!upgrade.is(RunecraftoryTags.Items.EQUIPMENT))
                    return stack;
                if (lightOre)
                    return changeBaseItemTo(stack, upgrade, type);
                else {
                    Equipable current = Equipable.get(stack);
                    Equipable other = Equipable.get(upgrade);
                    boolean sameType = current == null && other == null
                            || (current != null && other != null && current.getEquipmentSlot() == other.getEquipmentSlot());
                    if (sameType)
                        return changeBaseItemTo(stack, upgrade, type);
                }
            }
            if (stack.is(RunecraftoryTags.Items.UPGRADABLE_HELD)) {
                if (!upgrade.is(RunecraftoryTags.Items.UPGRADABLE_HELD))
                    return stack;
                if (lightOre)
                    return changeBaseItemTo(stack, upgrade, type);
                else {
                    for (TagKey<Item> tag : RunecraftoryTags.Items.WEAPONTAGS) {
                        if (stack.is(tag) && upgrade.is(tag))
                            return changeBaseItemTo(stack, upgrade, type);
                    }
                }
            }
            if (stat != null) {
                stat.getArmorEffect().ifPresent(effect -> {
                    if (effect.value().canBeAppliedTo(stack)) {
                        stack.getOrDefault(ModDataComponentTypes.ARMOR_EFFECT.get(), ArmorEffectData.DEFAULT).add(effect);
                    }
                });
            }
            return stack;
        }
        stack.set(ModDataComponentTypes.LEVEL.get(), !crafting ? level + 1 : level);

        float efficiency = 1;
        if (!crafting) {
            ListItemStackHolder upgrades = stack.getOrDefault(ModDataComponentTypes.UPGRADES.get(), ListItemStackHolder.DEFAULT);
            int similar = upgrades.matchesItem(upgrade);
            efficiency = similar > 0 ? (float) (1 - Math.pow(0.5, similar)) : 1;
            stack.set(ModDataComponentTypes.UPGRADES.get(), upgrades.add(upgrade.copy()));
        } else {
            ListItemStackHolder bonus = stack.getOrDefault(ModDataComponentTypes.CRAFTING_BONUS.get(), ListItemStackHolder.DEFAULT);
            stack.set(ModDataComponentTypes.UPGRADES.get(), bonus.add(upgrade.copy()));
        }
        //Special Item Tags
        if (upgrade.getItem() == ModItems.GLASS.get() && stack.is(RunecraftoryTags.Items.UPGRADABLE_HELD))
            stack.set(ModDataComponentTypes.MAGNIFYING_GLASS.get(), Unit.INSTANCE);
        if (upgrade.getItem() == ModItems.SCRAP_PLUS.get() && stack.is(RunecraftoryTags.Items.UPGRADABLE_HELD))
            stack.set(ModDataComponentTypes.SCRAP_METAL_PLUS.get(), Unit.INSTANCE);
        boolean hasObjectX = stack.getOrDefault(ModDataComponentTypes.OBJECT_X.get(), false);
        if (upgrade.getItem() == ModItems.OBJECT_X.get())
            stack.set(ModDataComponentTypes.OBJECT_X.get(), !hasObjectX);
        if (type == EnumCrafting.FORGE && upgrade.getItem() == ModItems.INVIS_STONE.get())
            stack.set(ModDataComponentTypes.INVISIBLE.get(), Unit.INSTANCE);
        if (type == EnumCrafting.FORGE && upgrade.is(RunecraftoryTags.Items.SCALES))
            stack.set(ModDataComponentTypes.DRAGON_SCALE.get(), Unit.INSTANCE);
        if (crafting && upgrade.getItem() == ModItems.LIGHT_ORE.get() && !stack.has(ModDataComponentTypes.ORIGINAL_ITEM.get()))
            stack.set(ModDataComponentTypes.LIGHT_ORE.get(), true);
        if (upgrade.getItem() == ModItems.GLITTA_AUGITE.get() && stack.is(RunecraftoryTags.Items.UPGRADABLE_HELD))
            stack.set(ModDataComponentTypes.GLITTA_AUGITE.get(), Unit.INSTANCE);
        if (upgrade.getItem() == ModItems.RACCOON_LEAF.get() && stack.is(RunecraftoryTags.Items.UPGRADABLE_HELD))
            stack.set(ModDataComponentTypes.RACCOON_LEAF.get(), Unit.INSTANCE);
        // Apply double/tenfold steel. Works only once
        boolean applyDoubleSteel = stack.getOrDefault(ModDataComponentTypes.DOUBLE_STEEL.get(), false);
        if (!stack.has(ModDataComponentTypes.DOUBLE_STEEL.get())) {
            if (upgrade.getItem() == ModItems.STEEL_DOUBLE.get())
                stack.set(ModDataComponentTypes.DOUBLE_STEEL.get(), true);
        } else if (applyDoubleSteel) {
            stack.set(ModDataComponentTypes.DOUBLE_STEEL.get(), false);
        }
        boolean applyTenSteel = stack.getOrDefault(ModDataComponentTypes.TENFOLD_STEEL.get(), false);
        if (!stack.has(ModDataComponentTypes.TENFOLD_STEEL.get())) {
            if (upgrade.getItem() == ModItems.STEEL_TEN.get())
                stack.set(ModDataComponentTypes.TENFOLD_STEEL.get(), true);
        } else if (applyTenSteel) {
            stack.set(ModDataComponentTypes.TENFOLD_STEEL.get(), false);
        }

        if (stat != null) {
//            if (!tag.contains(LibNBT.BASE) && !stat.itemStats().isEmpty()) {
//                ItemStat base = DataPackHandler.INSTANCE.itemStatManager().get(stack.getItem()).orElse(null);
//                if (base != null) {
//                    CompoundTag statsTag = new CompoundTag();
//                    for (Map.Entry<Attribute, Double> entry : base.itemStats().entrySet()) {
//                        if (entry.getKey() == ModAttributes.ATTACK_RANGE.get()) {
//                            if (upgrade.getItem() == ModItems.RACCOON_LEAF.get() && tag.getBoolean(LibNBT.RACCOON_LEAF))
//                                continue;
//                            if (upgrade.getItem() == ModItems.GLITTA_AUGITE.get() && tag.getBoolean(LibNBT.GLITTA_AUGITE))
//                                continue;
//                        }
//                        statsTag.putDouble(Registry.ATTRIBUTE.getKey(entry.getKey()).toString(), entry.getValue());
//                    }
//                    tag.put(LibNBT.BASE, statsTag);
//                }
//            }
//
//            List<ResourceLocation> blacklist = List.of();
//            if (type == EnumCrafting.FORGE)
//                blacklist = ARMOR_ONLY;
//            if (type == EnumCrafting.ARMOR)
//                blacklist = WEAPON_ONLY;
//            CompoundTag statCompound = tag.getCompound(LibNBT.STATS);
//            for (Map.Entry<Attribute, Double> entry : stat.itemStats().entrySet()) {
//                if (blacklist.contains(Registry.ATTRIBUTE.getKey(entry.getKey())))
//                    continue;
//                double amount = entry.getValue() * efficiency;
//                if (hasObjectX)
//                    amount *= -1;
//                if (applyDoubleSteel)
//                    amount *= 2;
//                if (applyTenSteel)
//                    amount *= 8;
//                updateStatIncrease(entry.getKey(), amount, statCompound);
//            }
//            tag.put(LibNBT.STATS, statCompound);
//            if (!tag.contains(LibNBT.ELEMENT))
//                tag.putString(LibNBT.ELEMENT, getElement(stack).toString());
//            if (isWeapon(stack)) {
//                EnumElement current = getElement(stack);
//                if (stat.element() != EnumElement.NONE) {
//                    if (current == EnumElement.NONE) {
//                        tag.putString(LibNBT.ELEMENT, stat.element().toString());
//                    } else
//                        tag.putString(LibNBT.ELEMENT, EnumElement.NONE.toString());
//                }
//            }
//            if (stack.getItem() instanceof ItemStaffBase) {
//                Platform.INSTANCE.getStaffData(stack).ifPresent(data -> {
//                    if (stat.getTier1Spell() != null)
//                        data.setTier1Spell(stat.getTier1Spell());
//                    if (stat.getTier2Spell() != null)
//                        data.setTier2Spell(stat.getTier2Spell());
//                    if (stat.getTier3Spell() != null)
//                        data.setTier3Spell(stat.getTier3Spell());
//                });
//            }
//            if (stat.getArmorEffect() != null && stat.getArmorEffect().canBeAppliedTo(stack))
//                Platform.INSTANCE.getArmorEffects(stack).ifPresent(data -> data.addArmorEffects(stat.getArmorEffect()));
        }
        return stack;
    }

    private static ItemStack changeBaseItemTo(ItemStack stack, ItemStack toApply, EnumCrafting crafting) {
//        ItemStat stat = DataPackHandler.INSTANCE.itemStatManager().get(toApply.getItem()).orElse(null);
//        CompoundTag tag = new CompoundTag();
//        //Setup base stuff
//        if (stat != null) {
//            if (!stat.itemStats().isEmpty()) {
//                ItemStat base = DataPackHandler.INSTANCE.itemStatManager().get(toApply.getItem()).orElse(null);
//                if (base != null) {
//                    CompoundTag statsTag = new CompoundTag();
//                    Map<Attribute, Double> origin = DataPackHandler.INSTANCE.itemStatManager().get(stack.getItem())
//                            .map(ItemStat::itemStats).orElse(Map.of());
//                    for (Map.Entry<Attribute, Double> entry : base.itemStats().entrySet()) {
//                        if (NON_INHERITABLE.contains(Registry.ATTRIBUTE.getKey(entry.getKey())))
//                            statsTag.putDouble(Registry.ATTRIBUTE.getKey(entry.getKey()).toString(), origin.getOrDefault(entry.getKey(), 5d));
//                        else
//                            statsTag.putDouble(Registry.ATTRIBUTE.getKey(entry.getKey()).toString(), entry.getValue());
//                    }
//                    tag.put(LibNBT.BASE, statsTag);
//                }
//            }
//            tag.putString(LibNBT.ELEMENT, stat.element().toString());
//            if (stack.getItem() instanceof ItemStaffBase) {
//                Platform.INSTANCE.getStaffData(stack).ifPresent(data -> {
//                    if (stat.getTier1Spell() != null)
//                        data.setTier1Spell(stat.getTier1Spell());
//                    if (stat.getTier2Spell() != null)
//                        data.setTier2Spell(stat.getTier2Spell());
//                    if (stat.getTier3Spell() != null)
//                        data.setTier3Spell(stat.getTier3Spell());
//                });
//            }
//            if (stat.getArmorEffect() != null && stat.getArmorEffect().canBeAppliedTo(stack))
//                Platform.INSTANCE.getArmorEffects(stack).ifPresent(data -> data.addArmorEffects(stat.getArmorEffect()));
//        }
//        tag.putString(LibNBT.ORIGINITEM, Registry.ITEM.getKey(toApply.getItem()).toString());
//        CompoundTag stackTag = stack.getOrCreateTag();
//        stackTag.put(RuneCraftory.MODID, tag);
//        //Reapply all items used to craft the applied item
//        CompoundTag other = ItemNBT.getItemNBT(toApply);
//        if (other != null) {
//            ListTag bonus = other.getList(LibNBT.CRAFTING_BONUS, Tag.TAG_COMPOUND);
//            bonus.forEach(t -> {
//                CompoundTag nbt = (CompoundTag) t;
//                Item item = Registry.ITEM.get(new ResourceLocation(nbt.getString("Id")));
//                if (item != Items.AIR)
//                    addUpgradeItem(stack, new ItemStack(item), true, crafting);
//            });
//        }
        return stack;
    }

    public static ItemStack addFoodBonusItem(ItemStack stack, ItemStack stackToAdd) {
        if (stackToAdd.isEmpty())
            return stack;
//        CompoundTag tag = getItemNBT(stack);
//        if (tag == null)
//            tag = new CompoundTag();
//        ListTag bonus = tag.getList(LibNBT.CRAFTING_BONUS, Tag.TAG_COMPOUND);
//        CompoundTag bonusItem = new CompoundTag();
//        bonusItem.putString("Id", Registry.ITEM.getKey(stackToAdd.getItem()).toString());
//        bonusItem.putInt("Level", ItemNBT.itemLevel(stackToAdd));
//        bonus.add(bonusItem);
//        tag.put(LibNBT.CRAFTING_BONUS, bonus);
//
//        FoodProperties props = DataPackHandler.INSTANCE.foodManager().get(stackToAdd.getItem());
//        if (props != null) {
//            if (!tag.contains(LibNBT.FOOD_STATS)) {
//                FoodProperties base = DataPackHandler.INSTANCE.foodManager().get(stack.getItem());
//                if (base != null) {
//                    CompoundTag statsTag = new CompoundTag();
//                    for (Map.Entry<Attribute, Double> entry : base.effects().entrySet()) {
//                        statsTag.putDouble(Registry.ATTRIBUTE.getKey(entry.getKey()).toString(), entry.getValue());
//                    }
//                    tag.put(LibNBT.FOOD_STATS, statsTag);
//                    statsTag = new CompoundTag();
//                    for (Map.Entry<Attribute, Double> entry : base.effectsMultiplier().entrySet()) {
//                        statsTag.putDouble(Registry.ATTRIBUTE.getKey(entry.getKey()).toString(), entry.getValue());
//                    }
//                    tag.put(LibNBT.FOOD_STATS_MULT, statsTag);
//                }
//            }
//            boolean hasObjectX = tag.getBoolean(LibNBT.OBJECT_X);
//            if (stackToAdd.getItem() == ModItems.OBJECT_X.get())
//                tag.putBoolean(LibNBT.OBJECT_X, !hasObjectX);
//            for (Map.Entry<Attribute, Double> entry : props.cookingBonus().entrySet()) {
//                double amount = entry.getValue();
//                if (hasObjectX)
//                    amount *= -1;
//                updateStatIncrease(entry.getKey(), amount, tag.getCompound(LibNBT.FOOD_STATS));
//            }
//            for (Map.Entry<Attribute, Double> entry : props.cookingBonusPercent().entrySet()) {
//                double amount = entry.getValue();
//                if (hasObjectX)
//                    amount *= -1;
//                updateStatIncrease(entry.getKey(), amount, tag.getCompound(LibNBT.FOOD_STATS_MULT));
//            }
//        }
//        CompoundTag stackTag = stack.getOrCreateTag();
//        stackTag.put(RuneCraftory.MODID, tag);
        return stack;
    }

//    public static void updateStatIncrease(Attribute attribute, double amount, CompoundTag stats) {
//        String att = Registry.ATTRIBUTE.getKey(attribute).toString();
//        double oldValue = stats.getDouble(att);
//        stats.putDouble(att, oldValue + Math.floor(amount));
//    }

    public static boolean shouldHaveStats(ItemStack stack) {
        return stack.is(RunecraftoryTags.Items.UPGRADABLE_HELD) || stack.is(RunecraftoryTags.Items.EQUIPMENT);
    }

    public static boolean shouldHaveLevel(ItemStack stack) {
        return shouldHaveStats(stack) || stack.is(RunecraftoryTags.Items.SPELLS);
    }

    public static boolean isWeapon(ItemStack stack) {
        return stack.is(RunecraftoryTags.Items.UPGRADABLE_HELD);
    }

    public static boolean usedLightOre(ItemStack stack) {
        if (shouldHaveStats(stack)) {
            if (stack.getOrDefault(ModDataComponentTypes.ORIGINAL_ITEM.get(), ItemStackHolder.DEFAULT).isEmpty())
                return false;
            return stack.has(ModDataComponentTypes.LIGHT_ORE.get());
        }
        return false;
    }

    public static double attackSpeedModifier(LivingEntity entity) {
        return entity.getAttributeValue(ModAttributes.ATTACK_SPEED.asHolder());
    }
}
