package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.loot.BiomeLootCondition;
import io.github.flemmli97.runecraftory.common.loot.GiantLootCondition;
import io.github.flemmli97.runecraftory.common.loot.ItemLevelLootFunction;
import io.github.flemmli97.runecraftory.common.loot.MiningLootCondition;
import io.github.flemmli97.runecraftory.common.loot.NPCRelationCondition;
import io.github.flemmli97.runecraftory.common.loot.SeasonLootCondition;
import io.github.flemmli97.runecraftory.common.loot.VanillaDropCondition;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.PlatformRegistry;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class ModLootCondition {

    public static final PlatformRegistry<LootItemConditionType> LOOTCONDITIONS = PlatformUtils.INSTANCE.of(Registry.LOOT_ITEM_REGISTRY, RuneCraftory.MODID);
    public static final PlatformRegistry<LootItemFunctionType> LOOTFUNCTION = PlatformUtils.INSTANCE.of(Registry.LOOT_FUNCTION_REGISTRY, RuneCraftory.MODID);

    public static final RegistryEntrySupplier<LootItemConditionType> INT_CHECK = LOOTCONDITIONS.register("mining_check", () -> new LootItemConditionType(new MiningLootCondition.Serializer()));
    public static final RegistryEntrySupplier<LootItemConditionType> GIANTCROP = LOOTCONDITIONS.register("crop_giant", () -> new LootItemConditionType(new GiantLootCondition.Serializer()));
    public static final RegistryEntrySupplier<LootItemConditionType> VANILLADROP = LOOTCONDITIONS.register("drop_vanilla", () -> new LootItemConditionType(new VanillaDropCondition.Serializer()));
    public static final RegistryEntrySupplier<LootItemConditionType> INTERACTINGPLAYER = LOOTCONDITIONS.register("interacting_player", () -> new LootItemConditionType(new NPCRelationCondition.Serializer()));
    public static final RegistryEntrySupplier<LootItemConditionType> SEASONTYPE = LOOTCONDITIONS.register("season", () -> new LootItemConditionType(new SeasonLootCondition.Serializer()));
    public static final RegistryEntrySupplier<LootItemConditionType> BIOME = LOOTCONDITIONS.register("biome", () -> new LootItemConditionType(new BiomeLootCondition.Serializer()));

    public static final RegistryEntrySupplier<LootItemFunctionType> ITEM_LEVEL = LOOTFUNCTION.register("item_level", () -> new LootItemFunctionType(new ItemLevelLootFunction.Serializer()));

}
