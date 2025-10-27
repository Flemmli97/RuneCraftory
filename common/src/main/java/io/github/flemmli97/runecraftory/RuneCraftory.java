package io.github.flemmli97.runecraftory;

import io.github.flemmli97.runecraftory.common.quests.QuestHandler;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryActivities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryArmorEffects;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryArmorMaterials;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttackActions;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryBlocks;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryCrafting;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryCreativeTabs;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryCriteria;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryDataComponentTypes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEffects;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryFeatures;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryFluids;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryLootRegistries;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryMemoryTypes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryMenuTypes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCBehaviour;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCLooks;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCProfessions;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryParticles;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryPoiTypes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryStructures;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySubPredicates;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import io.github.flemmli97.runecraftory.mixin.AttributeAccessor;
import io.github.flemmli97.tenshilib.TenshiLib;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class RuneCraftory {

    public static final String MODID = "runecraftory";
    public static final Logger LOGGER = LogManager.getLogger(RuneCraftory.MODID);

    public static ResourceLocation modRes(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    /**
     * Simple loot context. Needs server side entity
     */
    public static LootContext createContext(LivingEntity entity) {
        return new LootContext.Builder(new LootParams.Builder((ServerLevel) entity.level()).withParameter(LootContextParams.THIS_ENTITY, entity)
                .withParameter(LootContextParams.ORIGIN, entity.position())
                .create(LootContextParamSets.ADVANCEMENT_ENTITY))
                .create(Optional.empty());
    }

    public static void commonInit() {
        TenshiLib.registerSyncedRegistry();
        RuneCraftoryActivities.ACTIVITIES.registerContent();
        RuneCraftoryArmorEffects.ARMOR_EFFECTS.register().registerContent();
        RuneCraftoryArmorMaterials.MATERIALS.registerContent();
        RunecraftoryAttachments.ATTACHMENTS.registerContent();
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
        RuneCraftorySubPredicates.SUB_PREDICATES.registerContent();

        QuestHandler.register();
    }

    public static void updateAttributeLimits() {
        ((AttributeAccessor) Attributes.MAX_HEALTH.value()).setMaxValue(Double.MAX_VALUE);
        ((AttributeAccessor) Attributes.ATTACK_DAMAGE.value()).setMaxValue(Double.MAX_VALUE);
    }
}
