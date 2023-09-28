package io.github.flemmli97.runecraftory.fabric;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientCalls;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.GateEntity;
import io.github.flemmli97.runecraftory.common.events.EntityCalls;
import io.github.flemmli97.runecraftory.common.events.WorldCalls;
import io.github.flemmli97.runecraftory.common.integration.simplequest.SimpleQuestIntegration;
import io.github.flemmli97.runecraftory.common.registry.ModActivities;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.registry.ModContainer;
import io.github.flemmli97.runecraftory.common.registry.ModCrafting;
import io.github.flemmli97.runecraftory.common.registry.ModCriteria;
import io.github.flemmli97.runecraftory.common.registry.ModEffects;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModFeatures;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModLootCondition;
import io.github.flemmli97.runecraftory.common.registry.ModNPCActions;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.registry.ModPoiTypes;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.registry.ModStats;
import io.github.flemmli97.runecraftory.common.registry.ModStructures;
import io.github.flemmli97.runecraftory.common.world.farming.FarmlandHandler;
import io.github.flemmli97.runecraftory.fabric.config.ConfigHolder;
import io.github.flemmli97.runecraftory.fabric.config.GeneralConfigSpec;
import io.github.flemmli97.runecraftory.fabric.config.MobConfigSpec;
import io.github.flemmli97.runecraftory.fabric.event.CropGrowEvent;
import io.github.flemmli97.runecraftory.fabric.loot.CropLootModifiers;
import io.github.flemmli97.runecraftory.fabric.network.ServerPacketHandler;
import io.github.flemmli97.runecraftory.mixin.AttributeAccessor;
import io.github.flemmli97.tenshilib.fabric.events.AOEAttackEvent;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.mixin.object.builder.SpawnRestrictionAccessor;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;

import java.nio.file.Path;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

public class RuneCraftoryFabric implements ModInitializer {

    public static final Path confDir = FabricLoader.getInstance().getConfigDir().resolve(RuneCraftory.MODID);

    private static boolean initAttributes;

    public static void entityTick(LivingEntity entity) {
        if (entity instanceof Player player) {
            EntityCalls.updateLivingTick(player);
        }
        if (entity.level.isClientSide)
            ClientCalls.tick(entity);
    }

    @Override
    public void onInitialize() {
        RuneCraftory.simpleQuests = FabricLoader.getInstance().isModLoaded("simplequests");

        this.initContent();
        ConfigHolder.configs.get(GeneralConfigSpec.spec.getLeft())
                .reloadConfig();
        ConfigHolder.configs.get(MobConfigSpec.spec.getLeft())
                .reloadConfig();
        ServerPacketHandler.registerServer();

        SpawnRestrictionAccessor.callRegister(ModEntities.GATE.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GateEntity::canSpawnAt);

        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new IdentifiableResourceReloadListener() {
            @Override
            public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
                AtomicReference<CompletableFuture<Void>> ret = new AtomicReference<>();
                DataPackHandler.reloadItemStats(l -> ret.set(l.reload(preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor)));
                return ret.get();
            }

            @Override
            public ResourceLocation getFabricId() {
                return new ResourceLocation(RuneCraftory.MODID, "item_stats");
            }
        });
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new IdentifiableResourceReloadListener() {
            @Override
            public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
                AtomicReference<CompletableFuture<Void>> ret = new AtomicReference<>();
                DataPackHandler.reloadCropManager(l -> ret.set(l.reload(preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor)));
                return ret.get();
            }

            @Override
            public ResourceLocation getFabricId() {
                return new ResourceLocation(RuneCraftory.MODID, "crop_manager");
            }
        });
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new IdentifiableResourceReloadListener() {
            @Override
            public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
                AtomicReference<CompletableFuture<Void>> ret = new AtomicReference<>();
                DataPackHandler.reloadFoodManager(l -> ret.set(l.reload(preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor)));
                return ret.get();
            }

            @Override
            public ResourceLocation getFabricId() {
                return new ResourceLocation(RuneCraftory.MODID, "food_manager");
            }
        });
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new IdentifiableResourceReloadListener() {
            @Override
            public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
                AtomicReference<CompletableFuture<Void>> ret = new AtomicReference<>();
                DataPackHandler.reloadShopItems(l -> ret.set(l.reload(preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor)));
                return ret.get();
            }

            @Override
            public ResourceLocation getFabricId() {
                return new ResourceLocation(RuneCraftory.MODID, "shop_items");
            }
        });
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new IdentifiableResourceReloadListener() {
            @Override
            public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
                AtomicReference<CompletableFuture<Void>> ret = new AtomicReference<>();
                DataPackHandler.reloadNPCData(l -> ret.set(l.reload(preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor)));
                return ret.get();
            }

            @Override
            public ResourceLocation getFabricId() {
                return new ResourceLocation(RuneCraftory.MODID, "random_npc_data");
            }
        });
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new IdentifiableResourceReloadListener() {
            @Override
            public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
                AtomicReference<CompletableFuture<Void>> ret = new AtomicReference<>();
                DataPackHandler.reloadGateSpawns(l -> ret.set(l.reload(preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor)));
                return ret.get();
            }

            @Override
            public ResourceLocation getFabricId() {
                return new ResourceLocation(RuneCraftory.MODID, "gate_spawn_data");
            }
        });
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new IdentifiableResourceReloadListener() {
            @Override
            public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
                AtomicReference<CompletableFuture<Void>> ret = new AtomicReference<>();
                DataPackHandler.reloadProperties(l -> ret.set(l.reload(preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor)));
                return ret.get();
            }

            @Override
            public ResourceLocation getFabricId() {
                return new ResourceLocation(RuneCraftory.MODID, "datapack_properties");
            }
        });
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(CropLootModifiers.INSTANCE);

        ModEntities.registerAttributes(FabricDefaultAttributeRegistry::register);

        //MobCalls
        ServerEntityEvents.ENTITY_LOAD.register(((entity, world) -> {
            if (entity instanceof LivingEntity living)
                EntityCalls.onLoadEntity(living);
        }));

        //PlayerCalls
        EntityTrackingEvents.START_TRACKING.register((trackedEntity, player) -> EntityCalls.trackEntity(player, trackedEntity));
        AOEAttackEvent.ATTACK.register(EntityCalls::playerAoeAttack);
        EntitySleepEvents.ALLOW_SLEEP_TIME.register(((player, sleepingPos, vanillaResult) -> GeneralConfig.modifyBed ? InteractionResult.CONSUME : InteractionResult.PASS));
        ServerPlayerEvents.COPY_FROM.register((old, newPlayer, keepEverything) -> EntityCalls.clone(old, newPlayer, !keepEverything));
        ServerPlayConnectionEvents.JOIN.register(((handler, sender, server) -> EntityCalls.joinPlayer(handler.getPlayer())));
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (EntityCalls.playerAttack(player, entity))
                return InteractionResult.FAIL;
            return InteractionResult.PASS;
        });
        UseBlockCallback.EVENT.register(((player, world, hand, hitResult) -> {
            EntityCalls.cropRightClickHarvest(player, world.getBlockState(hitResult.getBlockPos()), hitResult.getBlockPos());
            return InteractionResult.PASS;
        }));

        //WorldCalls
        CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated) -> WorldCalls.command(dispatcher)));
        WorldCalls.addFeatures(((d, feature) -> BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(), d, feature.unwrapKey().get())),
                Biome.BiomeCategory.THEEND);
        WorldCalls.addFeatures(((d, feature) -> BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(), d, feature.unwrapKey().get())),
                Biome.BiomeCategory.THEEND);
        WorldCalls.addFeatures(((d, feature) -> BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), d, feature.unwrapKey().get())),
                Biome.BiomeCategory.NONE);
        BiomeModifications.addSpawn(t -> true, MobCategory.MONSTER, ModEntities.GATE.get(), 100, 1, 1);
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            if (world.dimension() == Level.OVERWORLD) {
                WorldCalls.daily(world);
            }
        });
        CropGrowEvent.EVENT.register((WorldCalls::disableVanillaCrop));
        ServerChunkEvents.CHUNK_LOAD.register(((world, chunk) -> FarmlandHandler.get(world.getServer()).onChunkLoad(world, chunk.getPos())));
        ServerChunkEvents.CHUNK_UNLOAD.register(((world, chunk) -> FarmlandHandler.get(world.getServer()).onChunkUnLoad(world, chunk.getPos())));

        SimpleQuestIntegration.register();
    }

    public void initContent() {
        ModBlocks.BLOCKS.getEntries();
        ModItems.ITEMS.getEntries();
        ModEntities.ENTITIES.registerContent();
        ModBlocks.BLOCKS.registerContent();
        ModItems.ITEMS.registerContent();

        ModBlocks.TILES.registerContent();
        ModContainer.CONTAINERS.registerContent();
        //ModAttributes.ATTRIBUTES.registerContent();
        ModEffects.EFFECTS.registerContent();
        ModCrafting.RECIPESERIALIZER.registerContent();
        ModFeatures.FEATURES.registerContent();
        ModSpells.SPELLS.registerContent();
        ModStructures.STRUCTURES.registerContent();
        ModParticles.PARTICLES.registerContent();
        ModActivities.ACTIVITIES.registerContent();
        ModPoiTypes.POI.registerContent();
        ModStats.STATS.registerContent();
        ModNPCActions.ACTIONS.registerContent();

        ModLootCondition.LOOTFUNCTION.registerContent();
        ModLootCondition.LOOTCONDITIONS.registerContent();
        ModStructures.STRUCTURESPROCESSORS.registerContent();
        ModCrafting.RECIPETYPE.registerContent();

        this.tweakVanillaAttribute(Attributes.MAX_HEALTH, Double.MAX_VALUE);
        this.tweakVanillaAttribute(Attributes.ATTACK_DAMAGE, Double.MAX_VALUE);
        ModFeatures.registerConfiguredMineralFeatures();
        ModCriteria.init();
    }

    public static Collection<RegistryEntrySupplier<Attribute>> attributes() {
        if (!initAttributes) {
            ModAttributes.ATTRIBUTES.registerContent();
            initAttributes = true;
        }
        return ModAttributes.ENTITY_ATTRIBUTES;
    }

    private void tweakVanillaAttribute(Attribute attribute, double value) {
        if (attribute instanceof RangedAttribute) {
            ((AttributeAccessor) attribute).setMaxValue(value);
        }
    }
}
