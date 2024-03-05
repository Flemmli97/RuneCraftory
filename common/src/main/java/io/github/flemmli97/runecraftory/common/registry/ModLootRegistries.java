package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.loot.BiomeLootCondition;
import io.github.flemmli97.runecraftory.common.loot.CropWeaponLootFunction;
import io.github.flemmli97.runecraftory.common.loot.FirstKillCondition;
import io.github.flemmli97.runecraftory.common.loot.FriendPointCondition;
import io.github.flemmli97.runecraftory.common.loot.ItemLevelLootFunction;
import io.github.flemmli97.runecraftory.common.loot.LootingAndLuckLootFunction;
import io.github.flemmli97.runecraftory.common.loot.NPCRelationCondition;
import io.github.flemmli97.runecraftory.common.loot.SeasonLootCondition;
import io.github.flemmli97.runecraftory.common.loot.SkillLevelCondition;
import io.github.flemmli97.runecraftory.common.loot.TalkCountCondition;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.PlatformRegistry;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class ModLootRegistries {

    public static final PlatformRegistry<LootItemConditionType> LOOTCONDITIONS = PlatformUtils.INSTANCE.of(Registry.LOOT_ITEM_REGISTRY, RuneCraftory.MODID);
    public static final PlatformRegistry<LootItemFunctionType> LOOTFUNCTION = PlatformUtils.INSTANCE.of(Registry.LOOT_FUNCTION_REGISTRY, RuneCraftory.MODID);
    public static final RegistryEntrySupplier<LootItemConditionType> SKILL_CHECK = LOOTCONDITIONS.register("skill_check", () -> new LootItemConditionType(new SkillLevelCondition.Serializer()));
    public static final RegistryEntrySupplier<LootItemConditionType> INTERACTINGPLAYER = LOOTCONDITIONS.register("interacting_player", () -> new LootItemConditionType(new NPCRelationCondition.Serializer()));
    public static final RegistryEntrySupplier<LootItemConditionType> SEASONTYPE = LOOTCONDITIONS.register("season", () -> new LootItemConditionType(new SeasonLootCondition.Serializer()));
    public static final RegistryEntrySupplier<LootItemConditionType> BIOME = LOOTCONDITIONS.register("biome", () -> new LootItemConditionType(new BiomeLootCondition.Serializer()));
    public static final RegistryEntrySupplier<LootItemConditionType> FRIENDPOINTS = LOOTCONDITIONS.register("friend_points", () -> new LootItemConditionType(new FriendPointCondition.Serializer()));
    public static final RegistryEntrySupplier<LootItemConditionType> FIRST_KILL = LOOTCONDITIONS.register("first_kill", () -> new LootItemConditionType(new FirstKillCondition.Serializer()));
    public static final RegistryEntrySupplier<LootItemConditionType> TALKCOUNT = LOOTCONDITIONS.register("talk_count", () -> new LootItemConditionType(new TalkCountCondition.Serializer()));

    public static final RegistryEntrySupplier<LootItemFunctionType> ITEM_LEVEL = LOOTFUNCTION.register("item_level", () -> new LootItemFunctionType(new ItemLevelLootFunction.Serializer()));
    public static final RegistryEntrySupplier<LootItemFunctionType> LUCK_AND_LOOTING = LOOTFUNCTION.register("luck_and_looting", () -> new LootItemFunctionType(new LootingAndLuckLootFunction.Serializer()));
    public static final RegistryEntrySupplier<LootItemFunctionType> CROP_WEAPON_FUNCTION = LOOTFUNCTION.register("crop_weapon", () -> new LootItemFunctionType(new CropWeaponLootFunction.Serializer()));

}
