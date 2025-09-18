package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.tenshilib.TenshiLib;

public class RuneCraftoryRegistries {

    public static void register() {
        TenshiLib.registerSyncedRegistry();
        RuneCraftoryActivities.ACTIVITIES.registerContent();
        RuneCraftoryArmorEffects.ARMOR_EFFECTS.register().registerContent();
        RuneCraftoryArmorMaterials.MATERIALS.registerContent();
        RuneCraftoryAttackActions.ATTACK_ACTIONS.register().registerContent();
        RuneCraftoryAttributes.ATTRIBUTES.registerContent();
        RuneCraftoryBlocks.BLOCK_ENTITY_TYPES.registerContent();
        RuneCraftoryBlocks.BLOCKS.registerContent();
        RuneCraftoryCrafting.RECIPESERIALIZER.registerContent();
        RuneCraftoryCrafting.RECIPETYPE.registerContent();
        RuneCraftoryCreativeTabs.CREATIVE_MODE_TABS.registerContent();
        RuneCraftoryCriteria.TRIGGERS.registerContent();
        RuneCraftoryDataComponentTypes.DATA_COMPONENTS.registerContent();
        RuneCraftoryEffects.EFFECTS.registerContent();
        RuneCraftoryEntities.ENTITIES.registerContent();
        RuneCraftoryFeatures.FEATURES.registerContent();
        RuneCraftoryFeatures.TREE_DECORATORS.registerContent();
        RuneCraftoryFeatures.TRUNK_PLACER.registerContent();
        RuneCraftoryFluids.FLUIDS.registerContent();
        RuneCraftoryItems.ITEMS.registerContent();
        RuneCraftoryLootRegistries.LOOTCONDITIONS.registerContent();
        RuneCraftoryLootRegistries.LOOTFUNCTION.registerContent();
        RuneCraftoryLootRegistries.NUMBER_PROVIDERS.registerContent();
        RuneCraftoryMemoryTypes.MEMORYIES.registerContent();
        RuneCraftoryMenuTypes.CONTAINERS.registerContent();
        RuneCraftoryNPCBehaviour.BEHAVIOURS.register().registerContent();
        RuneCraftoryNPCLooks.NPC_FEATURES.register().registerContent();
        RuneCraftoryNPCProfessions.PROFESSIONS.register().registerContent();
        RuneCraftoryParticles.PARTICLES.registerContent();
        RuneCraftoryPoiTypes.POI.registerContent();
        RuneCraftorySounds.SOUND_EVENTS.registerContent();
        RuneCraftorySpells.SPELLS.register().registerContent();
        RuneCraftoryStructures.STRUCTURE_PROCESSORS.registerContent();
        RuneCraftoryStructures.STRUCTURES.registerContent();
    }
}
