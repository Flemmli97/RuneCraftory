package io.github.flemmli97.runecraftory.fabric;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeModConfigEvents;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientCalls;
import io.github.flemmli97.runecraftory.common.commands.RunecraftoryCommand;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.config.specs.ConfigHolder;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.GateEntity;
import io.github.flemmli97.runecraftory.common.events.EntityCalls;
import io.github.flemmli97.runecraftory.common.events.WorldCalls;
import io.github.flemmli97.runecraftory.common.events.WorldRegistrationCalls;
import io.github.flemmli97.runecraftory.common.lib.LootTableResources;
import io.github.flemmli97.runecraftory.common.quests.QuestHandler;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryRegistries;
import io.github.flemmli97.runecraftory.common.world.data.farming.FarmlandHandler;
import io.github.flemmli97.runecraftory.fabric.event.CropGrowEvent;
import io.github.flemmli97.runecraftory.fabric.network.PacketHandler;
import io.github.flemmli97.tenshilib.fabric.loader.events.CommonSetupEvent;
import io.github.flemmli97.tenshilib.fabric.loader.events.EntityAttributeModifierEvent;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class RuneCraftoryFabric implements ModInitializer {

    public static void entityTick(LivingEntity entity) {
        EntityCalls.updateLivingTick(entity);
        if (entity.level().isClientSide)
            ClientCalls.tick(entity);
    }

    @Override
    public void onInitialize() {
        RuneCraftoryRegistries.register();
        NeoForgeModConfigEvents.loading(RuneCraftory.MODID).register(config -> {
            ConfigHolder<?> holder = ConfigHolder.CONFIGS.get(config.getSpec());
            if (holder != null)
                holder.reloadConfig();
        });
        NeoForgeModConfigEvents.reloading(RuneCraftory.MODID).register(config -> {
            ConfigHolder<?> holder = ConfigHolder.CONFIGS.get(config.getSpec());
            if (holder != null)
                holder.reloadConfig();
        });
        for (Map.Entry<IConfigSpec, ConfigHolder<?>> confs : ConfigHolder.CONFIGS.entrySet()) {
            ConfigHolder<?> loader = confs.getValue();
            NeoForgeConfigRegistry.INSTANCE.register(RuneCraftory.MODID, loader.configType() == ConfigHolder.ConfigType.COMMON ? ModConfig.Type.COMMON : ModConfig.Type.CLIENT, confs.getKey(), loader.configName());
        }
        PacketHandler.register();

        DataPackHandler.addListeners(listener -> ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(listener.id(), reg -> new IdentifiableResourceReloadListener() {
            @Override
            public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
                listener.insertRegistryAccess(reg);
                return listener.reload(preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor);
            }

            @Override
            public ResourceLocation getFabricId() {
                return listener.id();
            }
        }));

        //MobCalls
        ServerEntityEvents.ENTITY_LOAD.register(((entity, world) -> {
            if (entity.getClass().equals(ItemEntity.class)) {
                EntityCalls.handleItemJoinLevel((ItemEntity) entity);
            }
            if (entity instanceof LivingEntity living)
                EntityCalls.onLoadEntity(living);
        }));

        EntitySleepEvents.ALLOW_SLEEP_TIME.register(((player, sleepingPos, vanillaResult) -> GeneralConfig.modifyBed ? InteractionResult.CONSUME : InteractionResult.PASS));
        ServerPlayerEvents.COPY_FROM.register((old, newPlayer, keepEverything) -> EntityCalls.clone(old, newPlayer, !keepEverything));
        ServerPlayConnectionEvents.JOIN.register(((handler, sender, server) -> EntityCalls.joinPlayer(handler.getPlayer())));
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (EntityCalls.playerAttack(player, entity))
                return InteractionResult.FAIL;
            return InteractionResult.PASS;
        });
        UseBlockCallback.EVENT.register(((player, world, hand, hitResult) -> {
            if (hand == InteractionHand.MAIN_HAND)
                EntityCalls.cropRightClickHarvest(player, world.getBlockState(hitResult.getBlockPos()), hitResult.getBlockPos(), hand);
            return InteractionResult.PASS;
        }));
        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (!EntityCalls.onPlayerUseItem(player, hand))
                return InteractionResultHolder.pass(player.getItemInHand(hand));
            return InteractionResultHolder.pass(ItemStack.EMPTY);
        });

        //WorldCalls
        CommandRegistrationCallback.EVENT.register(((dispatcher, ctx, selection) -> RunecraftoryCommand.reg(dispatcher, ctx)));
        WorldRegistrationCalls.createFeatures(null, feat ->
                BiomeModifications.addFeature(ctx -> ctx.getBiomeRegistryEntry().is(feat.tag()),
                        feat.decoration(), feat.placedFeature()));
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            if (world.dimension() == Level.OVERWORLD) {
                WorldCalls.tick(world);
            }
        });
        CropGrowEvent.EVENT.register((WorldCalls::disableVanillaCrop));
        ServerChunkEvents.CHUNK_LOAD.register(((world, chunk) -> FarmlandHandler.get(world.getServer()).onChunkLoad(world, chunk.getPos())));
        ServerChunkEvents.CHUNK_UNLOAD.register(((world, chunk) -> FarmlandHandler.get(world.getServer()).onChunkUnLoad(world, chunk.getPos())));
        ServerLifecycleEvents.SERVER_STARTING.register(WorldRegistrationCalls::addVillageStructures);
        LootTableEvents.MODIFY.register(((id, builder, source, provider) -> {
            if (LootTableResources.VANILLA_CHESTS.contains(id))
                builder.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(LootTableResources.CHEST_LOOT_SPELLS)));
        }));

        EntityAttributeModifierEvent.EVENT.register(event -> {
            for (EntityType<? extends LivingEntity> t : event.getTypes()) {
                for (RegistryEntrySupplier<Attribute, ?> s : RuneCraftoryAttributes.ENTITY_ATTRIBUTES) {
                    if (!event.has(t, s.asHolder()))
                        event.add(t, s.asHolder());
                }
            }
            for (RegistryEntrySupplier<Attribute, ?> s : RuneCraftoryAttributes.PLAYER_ATTRIBUTES) {
                event.add(EntityType.PLAYER, s.asHolder());
            }
        });

        CommonSetupEvent.EVENT.register(listener -> listener.enqueue(RuneCraftory.MODID, () -> {
            RuneCraftoryEntities.registerAttributes(FabricDefaultAttributeRegistry::register);
            MobSpawnSettings.SpawnerData gateSetting = WorldRegistrationCalls.gateSetting();
            BiomeModifications.addSpawn(t -> true, gateSetting.type.getCategory(), gateSetting.type, gateSetting.getWeight().asInt(), gateSetting.minCount, gateSetting.maxCount);
            SpawnPlacements.register(RuneCraftoryEntities.GATE.get(), SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GateEntity::canSpawnAt);
            RuneCraftory.updateAttributeLimits();
        }));

        QuestHandler.register();
    }
}
