package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.loot.BiomeLootCondition;
import io.github.flemmli97.runecraftory.common.loot.CropWeaponLootFunction;
import io.github.flemmli97.runecraftory.common.loot.FirstKillCondition;
import io.github.flemmli97.runecraftory.common.loot.FriendPointCondition;
import io.github.flemmli97.runecraftory.common.loot.ItemLevelLootFunction;
import io.github.flemmli97.runecraftory.common.loot.LootingAndLuckLootFunction;
import io.github.flemmli97.runecraftory.common.loot.LuckBonusNumberProvider;
import io.github.flemmli97.runecraftory.common.loot.NPCRelationCondition;
import io.github.flemmli97.runecraftory.common.loot.SeasonLootCondition;
import io.github.flemmli97.runecraftory.common.loot.SkillLevelCondition;
import io.github.flemmli97.runecraftory.common.loot.TalkCountCondition;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;

public class ModLootRegistries {

    public static final LoaderRegister<LootItemConditionType> LOOTCONDITIONS = LoaderRegistryAccess.INSTANCE.of(Registries.LOOT_CONDITION_TYPE, RuneCraftory.MODID);
    public static final LoaderRegister<LootItemFunctionType<?>> LOOTFUNCTION = LoaderRegistryAccess.INSTANCE.of(Registries.LOOT_FUNCTION_TYPE, RuneCraftory.MODID);
    public static final LoaderRegister<LootNumberProviderType> NUMBER_PROVIDERS = LoaderRegistryAccess.INSTANCE.of(Registries.LOOT_NUMBER_PROVIDER_TYPE, RuneCraftory.MODID);

    public static final RegistryEntrySupplier<LootItemConditionType, LootItemConditionType> SKILL_CHECK = LOOTCONDITIONS.register("skill_check", () -> new LootItemConditionType(SkillLevelCondition.CODEC));
    public static final RegistryEntrySupplier<LootItemConditionType, LootItemConditionType> INTERACTINGPLAYER = LOOTCONDITIONS.register("interacting_player", () -> new LootItemConditionType(NPCRelationCondition.CODEC));
    public static final RegistryEntrySupplier<LootItemConditionType, LootItemConditionType> SEASONTYPE = LOOTCONDITIONS.register("season", () -> new LootItemConditionType(SeasonLootCondition.CODEC));
    public static final RegistryEntrySupplier<LootItemConditionType, LootItemConditionType> BIOME = LOOTCONDITIONS.register("biome", () -> new LootItemConditionType(BiomeLootCondition.CODEC));
    public static final RegistryEntrySupplier<LootItemConditionType, LootItemConditionType> FRIENDPOINTS = LOOTCONDITIONS.register("friend_points", () -> new LootItemConditionType(FriendPointCondition.CODEC));
    public static final RegistryEntrySupplier<LootItemConditionType, LootItemConditionType> FIRST_KILL = LOOTCONDITIONS.register("first_kill", () -> new LootItemConditionType(FirstKillCondition.CODEC));
    public static final RegistryEntrySupplier<LootItemConditionType, LootItemConditionType> TALKCOUNT = LOOTCONDITIONS.register("talk_count", () -> new LootItemConditionType(TalkCountCondition.CODEC));

    public static final RegistryEntrySupplier<LootItemFunctionType<?>, LootItemFunctionType<ItemLevelLootFunction>> ITEM_LEVEL = LOOTFUNCTION.register("item_level", () -> new LootItemFunctionType<>(ItemLevelLootFunction.CODEC));
    public static final RegistryEntrySupplier<LootItemFunctionType<?>, LootItemFunctionType<LootingAndLuckLootFunction>> LUCK_AND_LOOTING = LOOTFUNCTION.register("luck_and_looting", () -> new LootItemFunctionType<>(LootingAndLuckLootFunction.CODEC));
    public static final RegistryEntrySupplier<LootItemFunctionType<?>, LootItemFunctionType<CropWeaponLootFunction>> CROP_WEAPON_FUNCTION = LOOTFUNCTION.register("crop_weapon", () -> new LootItemFunctionType<>(CropWeaponLootFunction.CODEC));

    public static final RegistryEntrySupplier<LootNumberProviderType, LootNumberProviderType> LUCK_BOOSTED = NUMBER_PROVIDERS.register("luck_boosted", () -> new LootNumberProviderType(LuckBonusNumberProvider.CODEC));

}
