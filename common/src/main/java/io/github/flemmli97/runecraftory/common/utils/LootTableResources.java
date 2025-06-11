package io.github.flemmli97.runecraftory.common.utils;

import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.List;

public class LootTableResources {

    public static final ResourceLocation FISHING = RuneCraftory.modRes("chore/fishing");
    public static final ResourceLocation SAND_FISHING = RuneCraftory.modRes("chore/sand_fishing");

    public static final ResourceLocation TIER_1_LOOT = RuneCraftory.modRes("entities/treasure_chest_tier_1");
    public static final ResourceLocation TIER_2_LOOT = RuneCraftory.modRes("entities/treasure_chest_tier_2");
    public static final ResourceLocation TIER_3_LOOT = RuneCraftory.modRes("entities/treasure_chest_tier_3");
    public static final ResourceLocation TIER_4_LOOT = RuneCraftory.modRes("entities/treasure_chest_tier_4");

    public static final ResourceKey<LootTable> CHEST_LOOT_SPELLS = ResourceKey.create(Registries.LOOT_TABLE, RuneCraftory.modRes("chest/inject/spells"));

    public static final List<ResourceKey<LootTable>> VANILLA_CHESTS = List.of(
            BuiltInLootTables.END_CITY_TREASURE,
            BuiltInLootTables.SIMPLE_DUNGEON,
            BuiltInLootTables.ABANDONED_MINESHAFT,
            BuiltInLootTables.NETHER_BRIDGE,
            BuiltInLootTables.STRONGHOLD_CORRIDOR,
            BuiltInLootTables.DESERT_PYRAMID,
            BuiltInLootTables.JUNGLE_TEMPLE,
            BuiltInLootTables.JUNGLE_TEMPLE_DISPENSER,
            BuiltInLootTables.IGLOO_CHEST,
            BuiltInLootTables.WOODLAND_MANSION,
            BuiltInLootTables.UNDERWATER_RUIN_SMALL,
            BuiltInLootTables.UNDERWATER_RUIN_BIG,
            BuiltInLootTables.BURIED_TREASURE,
            BuiltInLootTables.SHIPWRECK_TREASURE,
            BuiltInLootTables.BASTION_TREASURE
    );

    public static final ResourceLocation WOOLED_WHITE_LOOT = RuneCraftory.modRes("entities/wooly/white");

}
