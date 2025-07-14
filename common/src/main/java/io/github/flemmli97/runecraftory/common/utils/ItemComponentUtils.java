package io.github.flemmli97.runecraftory.common.utils;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.api.datapack.FoodProperties;
import io.github.flemmli97.runecraftory.api.datapack.ItemStat;
import io.github.flemmli97.runecraftory.common.components.ArmorEffectData;
import io.github.flemmli97.runecraftory.common.components.FoodAttributeData;
import io.github.flemmli97.runecraftory.common.components.ItemAttributeData;
import io.github.flemmli97.runecraftory.common.components.ItemStackHolder;
import io.github.flemmli97.runecraftory.common.components.ListItemStackHolder;
import io.github.flemmli97.runecraftory.common.components.StaffData;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.items.ToolItemTier;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.lib.LibConstants;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.recipes.CraftingType;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryDataComponentTypes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

public class ItemComponentUtils {

    public static int itemLevel(ItemStack stack) {
        return stack.getOrDefault(RuneCraftoryDataComponentTypes.LEVEL.get(), 1);
    }

    public static ItemStack getLeveledItem(ItemStack stack, int level) {
        if (shouldHaveLevel(stack)) {
            stack.set(RuneCraftoryDataComponentTypes.LEVEL.get(), Mth.clamp(level, 1, 10));
        }
        return stack;
    }

    public static void modifyAttribute(ItemStack stack, Consumer<ItemAttributeModifiers.Entry> remove, Consumer<ItemAttributeModifiers.Entry> add) {
        Pair<EquipmentSlot, Map<Holder<Attribute>, Double>> calculated = getStatsAttributes(stack);
        if (calculated != null) {
            // If this stack has custom stats remove default modifiers. This way we respect changes from other sources
            ItemAttributeModifiers defaultMap = stack.getPrototype().get(DataComponents.ATTRIBUTE_MODIFIERS);
            if (defaultMap != null) {
                defaultMap.modifiers().forEach(entry -> {
                    if (entry.slot().test(calculated.getFirst()))
                        remove.accept(entry);
                });
            }
            calculated.getSecond().forEach((att, d) ->
                    add.accept(new ItemAttributeModifiers.Entry(att,
                            new AttributeModifier(LibConstants.EQUIPMENT_MODIFIERS.get(calculated.getFirst()), d, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.bySlot(calculated.getFirst()))));
        }
    }

    public static Pair<EquipmentSlot, Map<Holder<Attribute>, Double>> getStatsAttributes(ItemStack stack) {
        Equipable equipable = Equipable.get(stack);
        if (ItemComponentUtils.shouldHaveStats(stack)) {
            EquipmentSlot slot = equipable != null ? equipable.getEquipmentSlot() : EquipmentSlot.MAINHAND;
            Map<Holder<Attribute>, Double> stats = ItemComponentUtils.statIncrease(stack);
            if (stats.isEmpty())
                return null;
            return Pair.of(slot, stats);
        }
        return null;
    }

    public static Map<Holder<Attribute>, Double> statIncrease(ItemStack stack) {
        ItemAttributeData stats = stack.get(RuneCraftoryDataComponentTypes.STATS.get());
        if (stats == null) {
            return DataPackHandler.INSTANCE.itemStatManager().get(stack.getItem()).map(ItemStat::itemStats)
                    .orElse(Map.of());
        }
        return stats.getTotalStats();
    }

    public static Pair<Map<Holder<Attribute>, Double>, Map<Holder<Attribute>, Double>> foodStats(ItemStack stack) {
        FoodProperties props = DataPackHandler.INSTANCE.foodManager().get(stack.getItem());
        if (props == null)
            return Pair.of(new TreeMap<>(RuneCraftoryAttributes.SORTED), new TreeMap<>(RuneCraftoryAttributes.SORTED));
        FoodAttributeData data = stack.get(RuneCraftoryDataComponentTypes.FOOD_BUFF.get());
        if (data == null) {
            return Pair.of(props.effects(), props.effectsMultiplier());
        }
        return Pair.of(data.getFlatStats(), data.getMultiplierStats());
    }

    public static void setElement(ItemElement element, ItemStack stack) {
        ItemElement stackElement = stack.get(RuneCraftoryDataComponentTypes.ELEMENT.get());
        stack.set(RuneCraftoryDataComponentTypes.ELEMENT.get(), stackElement == null || stackElement == element ? element : ItemElement.NONE);
    }

    public static ItemElement getElement(ItemStack stack) {
        ItemElement stackElement = stack.get(RuneCraftoryDataComponentTypes.ELEMENT.get());
        if (stackElement != null) {
            return stackElement;
        }
        return isWeapon(stack) ? DataPackHandler.INSTANCE.itemStatManager().get(stack.getItem()).map(ItemStat::element).orElse(ItemElement.NONE) : ItemElement.NONE;
    }

    public static ItemStack addUpgradeItem(ItemStack stack, ItemStack upgrade, boolean crafting, CraftingType type) {
        int level = itemLevel(stack);
        if (upgrade.isEmpty() || !ItemComponentUtils.shouldHaveStats(stack) || level >= 10)
            return ItemStack.EMPTY;
        ItemStat stat = DataPackHandler.INSTANCE.itemStatManager().get(upgrade.getItem()).orElse(null);
        if (ItemComponentUtils.shouldHaveStats(upgrade)) {
            if (!crafting || stack.has(RuneCraftoryDataComponentTypes.ORIGINAL_ITEM.get()))
                return stack;
            boolean lightOre = stack.getOrDefault(RuneCraftoryDataComponentTypes.LIGHT_ORE.get(), false);
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
                        stack.getOrDefault(RuneCraftoryDataComponentTypes.ARMOR_EFFECT.get(), ArmorEffectData.DEFAULT).add(effect);
                    }
                });
            }
            return stack;
        }
        stack.set(RuneCraftoryDataComponentTypes.LEVEL.get(), !crafting ? level + 1 : level);

        float efficiency = 1;
        if (!crafting) {
            ListItemStackHolder upgrades = stack.getOrDefault(RuneCraftoryDataComponentTypes.UPGRADES.get(), ListItemStackHolder.DEFAULT);
            int similar = upgrades.matchesItem(upgrade);
            efficiency = similar > 0 ? (float) (Math.pow(0.5, similar)) : 1;
            stack.set(RuneCraftoryDataComponentTypes.UPGRADES.get(), upgrades.add(upgrade));
        } else {
            ListItemStackHolder bonus = stack.getOrDefault(RuneCraftoryDataComponentTypes.CRAFTING_BONUS.get(), ListItemStackHolder.DEFAULT);
            stack.set(RuneCraftoryDataComponentTypes.UPGRADES.get(), bonus.add(upgrade));
        }
        //Special Item Tags
        if (upgrade.getItem() == RuneCraftoryItems.GLASS.get() && stack.is(RunecraftoryTags.Items.UPGRADABLE_HELD))
            stack.set(RuneCraftoryDataComponentTypes.MAGNIFYING_GLASS.get(), Unit.INSTANCE);
        if (upgrade.getItem() == RuneCraftoryItems.SCRAP_PLUS.get() && stack.is(RunecraftoryTags.Items.UPGRADABLE_HELD))
            stack.set(RuneCraftoryDataComponentTypes.SCRAP_METAL_PLUS.get(), Unit.INSTANCE);
        boolean hasObjectX = stack.getOrDefault(RuneCraftoryDataComponentTypes.OBJECT_X.get(), false);
        if (upgrade.getItem() == RuneCraftoryItems.OBJECT_X.get())
            stack.set(RuneCraftoryDataComponentTypes.OBJECT_X.get(), !hasObjectX);
        if (type == CraftingType.FORGE && upgrade.getItem() == RuneCraftoryItems.INVIS_STONE.get())
            stack.set(RuneCraftoryDataComponentTypes.INVISIBLE.get(), Unit.INSTANCE);
        if (type == CraftingType.FORGE && upgrade.is(RunecraftoryTags.Items.SCALES))
            stack.set(RuneCraftoryDataComponentTypes.DRAGON_SCALE.get(), Unit.INSTANCE);
        if (crafting && upgrade.getItem() == RuneCraftoryItems.LIGHT_ORE.get() && !stack.has(RuneCraftoryDataComponentTypes.ORIGINAL_ITEM.get()))
            stack.set(RuneCraftoryDataComponentTypes.LIGHT_ORE.get(), true);

        // Apply double/tenfold steel. Works only once
        boolean applyDoubleSteel = stack.getOrDefault(RuneCraftoryDataComponentTypes.DOUBLE_STEEL.get(), false);
        if (!stack.has(RuneCraftoryDataComponentTypes.DOUBLE_STEEL.get())) {
            if (upgrade.getItem() == RuneCraftoryItems.STEEL_DOUBLE.get())
                stack.set(RuneCraftoryDataComponentTypes.DOUBLE_STEEL.get(), true);
        } else if (applyDoubleSteel) {
            stack.set(RuneCraftoryDataComponentTypes.DOUBLE_STEEL.get(), false);
        }
        boolean applyTenSteel = stack.getOrDefault(RuneCraftoryDataComponentTypes.TENFOLD_STEEL.get(), false);
        if (!stack.has(RuneCraftoryDataComponentTypes.TENFOLD_STEEL.get())) {
            if (upgrade.getItem() == RuneCraftoryItems.STEEL_TEN.get())
                stack.set(RuneCraftoryDataComponentTypes.TENFOLD_STEEL.get(), true);
        } else if (applyTenSteel) {
            stack.set(RuneCraftoryDataComponentTypes.TENFOLD_STEEL.get(), false);
        }

        if (stat != null) {
            ItemAttributeData stats = stack.getOrDefault(RuneCraftoryDataComponentTypes.STATS.get(), ItemAttributeData.DEFAULT);
            if (stats.getBaseStats().isEmpty()) {
                ItemStat base = DataPackHandler.INSTANCE.itemStatManager().get(stack.getItem()).orElse(null);
                if (base != null && !base.itemStats().isEmpty()) {
                    stats = stats.base(base.itemStats());
                }
            }
            boolean applyUpgradeStat = !upgrade.is(type.upgradeBlacklist) &&
                    (!upgrade.is(RunecraftoryTags.Items.ONE_TIME_UPGRADE)
                            || stack.getOrDefault(RuneCraftoryDataComponentTypes.UPGRADES.get(), ListItemStackHolder.DEFAULT).matchesItem(upgrade) == 0);

            if (applyUpgradeStat) {
                TagKey<Attribute> blacklist = null;
                if (type == CraftingType.FORGE)
                    blacklist = RunecraftoryTags.Attributes.WEAPON_ONLY;
                if (type == CraftingType.ACCESSORY_WORKBENCH)
                    blacklist = RunecraftoryTags.Attributes.ARMOR_ONLY;
                Map<Holder<Attribute>, Double> upgradeStats = new HashMap<>();
                for (Map.Entry<Holder<Attribute>, Double> entry : stat.itemStats().entrySet()) {
                    if (blacklist != null && entry.getKey().is(blacklist))
                        continue;
                    double amount = entry.getValue() * efficiency;
                    if (hasObjectX)
                        amount *= -1;
                    if (applyDoubleSteel)
                        amount *= 2;
                    if (applyTenSteel)
                        amount *= 8;
                    upgradeStats.put(entry.getKey(), amount);
                }
                stats = stats.add(upgradeStats);
            }
            stack.set(RuneCraftoryDataComponentTypes.STATS.get(), stats);
            if (isWeapon(stack)) {
                setElement(stat.element(), stack);
            }
            if (stack.getItem() instanceof ItemStaffBase) {
                stack.update(RuneCraftoryDataComponentTypes.STAFF.get(), StaffData.DEFAULT, data -> {
                    if (stat.getTier1Spell().isPresent())
                        data = data.setTier1Spell(stat.getTier1Spell().get());
                    if (stat.getTier2Spell().isPresent())
                        data = data.setTier2Spell(stat.getTier2Spell().get());
                    if (stat.getTier3Spell().isPresent())
                        data = data.setTier3Spell(stat.getTier3Spell().get());
                    return data;
                });
            }
            if (stat.getArmorEffect().isPresent() && stat.getArmorEffect().get().value().canBeAppliedTo(stack)) {
                stack.update(RuneCraftoryDataComponentTypes.ARMOR_EFFECT.get(), ArmorEffectData.DEFAULT, data -> data.add(stat.getArmorEffect().get()));
            }
        }
        return stack;
    }

    private static ItemStack changeBaseItemTo(ItemStack stack, ItemStack toApply, CraftingType crafting) {
        ItemStat stat = DataPackHandler.INSTANCE.itemStatManager().get(toApply.getItem()).orElse(null);
        //Setup base stuff
        if (stat != null) {
            ItemAttributeData stats = stack.getOrDefault(RuneCraftoryDataComponentTypes.STATS.get(), ItemAttributeData.DEFAULT);
            ItemStat base = DataPackHandler.INSTANCE.itemStatManager().get(stack.getItem()).orElse(null);
            if (base != null) {
                Map<Holder<Attribute>, Double> baseStats = new HashMap<>();
                Map<Holder<Attribute>, Double> origin = DataPackHandler.INSTANCE.itemStatManager().get(stack.getItem())
                        .map(ItemStat::itemStats).orElse(Map.of());
                for (Map.Entry<Holder<Attribute>, Double> entry : base.itemStats().entrySet()) {
                    if (entry.getKey().is(RunecraftoryTags.Attributes.NON_INHERITABLE)) {
                        if (origin.containsKey(entry.getKey()))
                            baseStats.put(entry.getKey(), origin.get(entry.getKey()));
                    } else
                        baseStats.put(entry.getKey(), entry.getValue());
                }
                stack.set(RuneCraftoryDataComponentTypes.STATS.get(), stats.base(baseStats));
            }
            stack.set(RuneCraftoryDataComponentTypes.ELEMENT.get(), stat.element());
            if (stack.getItem() instanceof ItemStaffBase) {
                stack.update(RuneCraftoryDataComponentTypes.STAFF.get(), StaffData.DEFAULT, data -> {
                    if (stat.getTier1Spell().isPresent())
                        data = data.setTier1Spell(stat.getTier1Spell().get());
                    if (stat.getTier2Spell().isPresent())
                        data = data.setTier2Spell(stat.getTier2Spell().get());
                    if (stat.getTier3Spell().isPresent())
                        data = data.setTier3Spell(stat.getTier3Spell().get());
                    return data;
                });
            }
            if (stat.getArmorEffect().isPresent() && stat.getArmorEffect().get().value().canBeAppliedTo(stack)) {
                stack.update(RuneCraftoryDataComponentTypes.ARMOR_EFFECT.get(), ArmorEffectData.DEFAULT, data -> data.add(stat.getArmorEffect().get()));
            }
        }
        stack.set(RuneCraftoryDataComponentTypes.ORIGINAL_ITEM.get(), new ItemStackHolder(toApply));
        // Reapply all items used to craft the applied item
        ListItemStackHolder bonus = toApply.get(RuneCraftoryDataComponentTypes.CRAFTING_BONUS.get());
        if (bonus != null) {
            bonus.forEach(added -> addUpgradeItem(stack, added, true, crafting));
        }
        return stack;
    }

    public static ItemStack addFoodBonusItem(ItemStack stack, ItemStack stackToAdd) {
        ListItemStackHolder bonus = stack.getOrDefault(RuneCraftoryDataComponentTypes.CRAFTING_BONUS.get(), ListItemStackHolder.DEFAULT);
        stack.set(RuneCraftoryDataComponentTypes.UPGRADES.get(), bonus.add(stackToAdd));

        FoodProperties props = DataPackHandler.INSTANCE.foodManager().get(stackToAdd.getItem());
        boolean hasObjectX = stack.getOrDefault(RuneCraftoryDataComponentTypes.OBJECT_X.get(), false);
        if (stackToAdd.getItem() == RuneCraftoryItems.OBJECT_X.get())
            stack.set(RuneCraftoryDataComponentTypes.OBJECT_X.get(), !hasObjectX);
        if (props != null) {
            FoodAttributeData stats = stack.getOrDefault(RuneCraftoryDataComponentTypes.FOOD_BUFF.get(), FoodAttributeData.DEFAULT);
            Map<Holder<Attribute>, Double> flatStats = new HashMap<>();
            for (Map.Entry<Holder<Attribute>, Double> entry : props.cookingBonus().entrySet()) {
                double amount = entry.getValue();
                if (hasObjectX)
                    amount *= -1;
                flatStats.put(entry.getKey(), amount);
            }
            stats = stats.add(flatStats);
            Map<Holder<Attribute>, Double> multStats = new HashMap<>();
            for (Map.Entry<Holder<Attribute>, Double> entry : props.cookingBonusPercent().entrySet()) {
                double amount = entry.getValue();
                if (hasObjectX)
                    amount *= -1;
                multStats.put(entry.getKey(), amount);
            }
            stats = stats.addMultiplier(multStats);
            stack.set(RuneCraftoryDataComponentTypes.FOOD_BUFF.get(), stats);
        }
        return stack;
    }

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
            if (stack.getOrDefault(RuneCraftoryDataComponentTypes.ORIGINAL_ITEM.get(), ItemStackHolder.DEFAULT).isEmpty())
                return false;
            return stack.has(RuneCraftoryDataComponentTypes.LIGHT_ORE.get());
        }
        return false;
    }

    public static int getChargeTime(LivingEntity entity) {
        return Mth.ceil(EntityUtils.tryGetAttribute(entity, RuneCraftoryAttributes.CHARGE_TIME.asHolder()));
    }

    public static int getChargeTime(LivingEntity entity, ToolItemTier toolTier) {
        int time = Mth.ceil(EntityUtils.tryGetAttribute(entity, RuneCraftoryAttributes.CHARGE_TIME.asHolder()));
        if (toolTier == ToolItemTier.PLATINUM)
            time *= GeneralConfig.platinumChargeTime;
        return time;
    }

    public static float getShieldEfficiency(LivingEntity entity) {
        return getShieldEfficiency(entity.getMainHandItem());
    }

    public static float getShieldEfficiency(ItemStack stack) {
        float eff = stack.getOrDefault(RuneCraftoryDataComponentTypes.SHIELD_EFFICIENCY.get(), 1f);
        if (eff < 1) {
            if (stack.has(RuneCraftoryDataComponentTypes.DRAGON_SCALE.get()))
                eff = Mth.clamp(eff + 0.5f, 0.5f, 0.75f);
        }
        return eff;
    }
}
