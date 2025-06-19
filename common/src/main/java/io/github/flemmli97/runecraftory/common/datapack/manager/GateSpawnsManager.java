package io.github.flemmli97.runecraftory.common.datapack.manager;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.GateSpawnData;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.datapack.ListenerExtension;
import io.github.flemmli97.runecraftory.common.entities.GateEntity;
import io.github.flemmli97.runecraftory.common.utils.HolderUtils;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class GateSpawnsManager extends SimpleJsonResourceReloadListener implements ListenerExtension {

    public static final ResourceLocation ID = RuneCraftory.modRes("gate_spawning");
    public static final String DIRECTORY = String.format("%s/%s", ID.getNamespace(), ID.getPath());

    private Map<TagKey<Biome>, List<SpawnResource>> biomeSpawns = new HashMap<>();
    private Map<Structure, List<SpawnResource>> structureSpawns = new HashMap<>();

    private HolderLookup.Provider provider;

    public GateSpawnsManager() {
        super(DataPackHandler.GSON, DIRECTORY);
    }

    public List<EntityType<?>> pickRandomMobs(ServerLevel level, GateEntity gate, Holder<Biome> biome, RandomSource rand, int amount, BlockPos pos, List<ServerPlayer> players) {
        List<SpawnResource> list = this.structureSpawns.entrySet().stream()
                .filter(e -> level.structureManager().getStructureWithPieceAt(pos, e.getKey()).isValid())
                .map(Map.Entry::getValue)
                .flatMap(List::stream).collect(Collectors.toList());
        if (list.isEmpty()) {
            biome.tags().forEach(tag -> {
                List<SpawnResource> l = this.biomeSpawns.get(tag);
                if (l != null)
                    list.addAll(l);
            });
        }
        list.removeIf(w -> w.playerPredicate.isPresent() && players.stream().noneMatch(p -> w.playerPredicate.get().matches(p, p)));
        if (list.isEmpty())
            return new ArrayList<>();
        double dist = pos.distSqr(level.getSharedSpawnPos());
        BlockState state = level.getBlockState(pos);
        list.removeIf(w -> !w.matches(level, pos, state, dist, gate));
        List<EntityType<?>> ret = new ArrayList<>();
        if (amount > list.size()) {
            list.forEach(w -> ret.add(w.entity.value()));
        } else {
            int i = amount;
            int totalWeight = WeightedRandom.getTotalWeight(list);
            EntityType<?> type;
            while (i > 0 && !ret.contains(type = WeightedRandom.getRandomItem(rand, list, totalWeight).map(w -> w.entity.value()).orElse(null))) {
                if (type != null)
                    ret.add(type);
                i--;
            }
        }
        return ret;
    }

    public boolean hasSpawns(ServerLevelAccessor level, BlockPos pos, BlockState state) {
        if (level.getBiome(pos).tags().anyMatch(t -> {
            List<SpawnResource> l = this.biomeSpawns.get(t);
            return l != null && l.stream().anyMatch(r -> r.canSpawn(level.getLevel(), pos, state));
        }))
            return true;
        return this.hasStructureSpawns(level.getLevel(), pos);
    }

    public boolean hasStructureSpawns(ServerLevel level, BlockPos pos) {
        return this.structureSpawns.entrySet().stream()
                .anyMatch(e -> level.structureManager().getStructureWithPieceAt(pos, e.getKey()).isValid());
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, ResourceManager manager, ProfilerFiller profiler) {
        Map<TagKey<Biome>, List<SpawnResource>> biomeSpawns = new LinkedHashMap<>();
        Map<Structure, List<SpawnResource>> structureSpawns = new LinkedHashMap<>();
        DynamicOps<JsonElement> ops = this.provider.createSerializationContext(JsonOps.INSTANCE);
        data.forEach((fres, el) -> {
            try {
                GateSpawnData spawnData = GateSpawnData.CODEC.parse(ops, el).getOrThrow();
                Optional<Holder<EntityType<?>>> optType = HolderUtils.getHolder(this.provider, Registries.ENTITY_TYPE, spawnData.entity());
                optType.ifPresentOrElse(type -> {
                    spawnData.biomes().forEach((key, weight) -> {
                        SpawnResource resource = new SpawnResource(type, spawnData, weight);
                        biomeSpawns.computeIfAbsent(key, o -> new ArrayList<>())
                                .add(resource);
                    });
                    spawnData.structures().forEach((key, weight) -> {
                        Optional<Structure> optFeat = HolderUtils.get(this.provider, Registries.STRUCTURE, key);
                        optFeat.ifPresentOrElse(feat -> {
                            SpawnResource resource = new SpawnResource(type, spawnData, weight);
                            structureSpawns.computeIfAbsent(feat, o -> new ArrayList<>())
                                    .add(resource);
                        }, () -> RuneCraftory.LOGGER.error("No such structure {} for spawn data {}", key, fres));
                    });
                }, () -> RuneCraftory.LOGGER.error("No such entity {} for spawn data {}", spawnData.entity(), fres));
            } catch (Exception ex) {
                RuneCraftory.LOGGER.error("Couldn't parse spawn data json {} {}", fres, ex);
                ex.fillInStackTrace();
            }
        });
        this.biomeSpawns = ImmutableMap.copyOf(biomeSpawns);
        this.structureSpawns = ImmutableMap.copyOf(structureSpawns);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void insertRegistryAccess(HolderLookup.Provider provider) {
        this.provider = provider;
    }

    public static class SpawnResource extends WeightedEntry.IntrusiveBase {

        private final Holder<EntityType<?>> entity;
        private final int distToSpawnSq;
        private final int minGateLevel;
        private final boolean allowWater;
        private final Optional<EntityPredicate> gatePredicate, playerPredicate;

        public SpawnResource(Holder<EntityType<?>> entity, GateSpawnData spawnData, int weight) {
            super(weight);
            this.entity = entity;
            this.distToSpawnSq = spawnData.minDistanceFromSpawn() * spawnData.minDistanceFromSpawn();
            this.minGateLevel = spawnData.minGateLevel();
            this.allowWater = spawnData.canSpawnInWater();
            this.gatePredicate = spawnData.gatePredicate();
            this.playerPredicate = spawnData.playerPredicate();
        }

        public boolean canSpawn(ServerLevel serverLevel, BlockPos pos, BlockState state) {
            return pos.distSqr(serverLevel.getSharedSpawnPos()) >= this.distToSpawnSq && (state.getFluidState().isEmpty() || (this.allowWater && state.getFluidState().is(FluidTags.WATER) && serverLevel.canSeeSkyFromBelowWater(pos)));
        }

        public boolean matches(ServerLevel serverLevel, BlockPos pos, BlockState state, double dist, GateEntity gate) {
            return dist >= this.distToSpawnSq && (state.getFluidState().isEmpty() || (this.allowWater && state.getFluidState().is(FluidTags.WATER) && serverLevel.canSeeSkyFromBelowWater(pos)))
                    && gate.xpLevel().getLevel() >= this.minGateLevel
                    && (this.gatePredicate.isEmpty() || this.gatePredicate.get().matches(serverLevel, gate.position(), gate));
        }

        @Override
        public String toString() {
            return String.format("Entity: %s, MinSpawnSq: %d, Weight: %s, MinGateLevel: %s", this.entity.getRegisteredName(), this.distToSpawnSq, this.getWeight(), this.minGateLevel);
        }
    }
}
