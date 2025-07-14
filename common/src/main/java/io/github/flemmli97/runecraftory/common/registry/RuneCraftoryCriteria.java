package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.advancements.CropHarvestTrigger;
import io.github.flemmli97.runecraftory.common.advancements.LevelTrigger;
import io.github.flemmli97.runecraftory.common.advancements.MoneyTrigger;
import io.github.flemmli97.runecraftory.common.advancements.ShippingTrigger;
import io.github.flemmli97.runecraftory.common.advancements.ShopTrigger;
import io.github.flemmli97.runecraftory.common.advancements.SkillLevelTrigger;
import io.github.flemmli97.runecraftory.common.advancements.TameMonsterTrigger;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.registries.Registries;

import java.util.function.Supplier;

public class RuneCraftoryCriteria {

    public static final LoaderRegister<CriterionTrigger<?>> TRIGGERS = LoaderRegistryAccess.INSTANCE.of(Registries.TRIGGER_TYPE, RuneCraftory.MODID);

    public static final RegistryEntrySupplier<CriterionTrigger<?>, LevelTrigger> LEVEL_TRIGGER = register("level_trigger", LevelTrigger::new);
    public static final RegistryEntrySupplier<CriterionTrigger<?>, MoneyTrigger> MONEY_TRIGGER = register("money", MoneyTrigger::new);
    public static final RegistryEntrySupplier<CriterionTrigger<?>, ShippingTrigger> SHIPPING_TRIGGER = register("shipping", ShippingTrigger::new);
    public static final RegistryEntrySupplier<CriterionTrigger<?>, ShopTrigger> SHOP_TRIGGER = register("shop_buy", ShopTrigger::new);
    public static final RegistryEntrySupplier<CriterionTrigger<?>, SkillLevelTrigger> SKILL_LEVEL_TRIGGER = register("skill_level_trigger", SkillLevelTrigger::new);
    public static final RegistryEntrySupplier<CriterionTrigger<?>, TameMonsterTrigger> TAME_MONSTER_TRIGGER = register("tame_monster", TameMonsterTrigger::new);
    public static final RegistryEntrySupplier<CriterionTrigger<?>, CropHarvestTrigger> HARVEST_CROP = register("crop_harvest", CropHarvestTrigger::new);
    public static final RegistryEntrySupplier<CriterionTrigger<?>, PlayerTrigger> UPGRADE_ITEM = register("upgrade_item", PlayerTrigger::new);
    public static final RegistryEntrySupplier<CriterionTrigger<?>, PlayerTrigger> CHANGE_SPELL = register("change_spell", PlayerTrigger::new);
    public static final RegistryEntrySupplier<CriterionTrigger<?>, PlayerTrigger> LIGHT_ORE = register("light_ore", PlayerTrigger::new);
    public static final RegistryEntrySupplier<CriterionTrigger<?>, PlayerTrigger> CHANGE_ELEMENT = register("change_element", PlayerTrigger::new);
    public static final RegistryEntrySupplier<CriterionTrigger<?>, PlayerTrigger> FERTILIZE_FARM = register("fertilize_farm", PlayerTrigger::new);
    public static final RegistryEntrySupplier<CriterionTrigger<?>, PlayerTrigger> COMMAND_FARMING = register("monster_farming", PlayerTrigger::new);
    public static final RegistryEntrySupplier<CriterionTrigger<?>, PlayerTrigger> FORGING = register("forging", PlayerTrigger::new);
    public static final RegistryEntrySupplier<CriterionTrigger<?>, PlayerTrigger> CRAFTING = register("crafting", PlayerTrigger::new);
    public static final RegistryEntrySupplier<CriterionTrigger<?>, PlayerTrigger> MEDICINE = register("medicine", PlayerTrigger::new);
    public static final RegistryEntrySupplier<CriterionTrigger<?>, PlayerTrigger> COOKING = register("cooking", PlayerTrigger::new);

    private static <T extends CriterionTrigger<?>> RegistryEntrySupplier<CriterionTrigger<?>, T> register(String name, Supplier<T> inst) {
        return TRIGGERS.register(name, inst);
    }
}
