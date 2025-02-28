package io.github.flemmli97.runecraftory.common.datapack.manager;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class StructureBossManager extends SimpleJsonResourceReloadListener {

    public static final String DIRECTORY = "runecraftory_bosses";

    private static final Gson GSON = new GsonBuilder().create();

    private Map<ResourceLocation, BossSpawnList> spawnList = new HashMap<>();

    public StructureBossManager() {
        super(GSON, DIRECTORY);
    }

    @Nullable
    public BossSpawnList getBoss(ResourceLocation id) {
        return this.spawnList.get(id);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, ResourceManager manager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, BossSpawnList> builder = new ImmutableMap.Builder<>();
        data.forEach((key, el) -> {
            try {
                BossSpawnList list = BossSpawnList.CODEC.parse(JsonOps.INSTANCE, el)
                        .getOrThrow(false, RuneCraftory.LOGGER::error);
                builder.put(key, list);
            } catch (Exception ex) {
                RuneCraftory.LOGGER.error("Couldn't parse boss spawn list json {} {}", key, ex);
                ex.fillInStackTrace();
            }
        });
        this.spawnList = builder.build();
    }

    public static class BossSpawnList {

        public static final Codec<BossSpawnList> CODEC = SimpleWeightedRandomList.wrappedCodec(Registry.ENTITY_TYPE.byNameCodec())
                .xmap(BossSpawnList::new, b -> b.list);

        private final SimpleWeightedRandomList<EntityType<?>> list;
        private final Set<EntityType<?>> direct;

        public BossSpawnList(SimpleWeightedRandomList<EntityType<?>> list) {
            this.list = list;
            this.direct = this.list.unwrap().stream().map(WeightedEntry.Wrapper::getData).collect(Collectors.toUnmodifiableSet());
        }

        public static BossSpawnList of(EntityType<?> type) {
            return new BossSpawnList(SimpleWeightedRandomList.single(type));
        }

        public Optional<EntityType<?>> getRandom(Random random) {
            return this.list.getRandomValue(random);
        }

        public boolean has(EntityType<?> type) {
            return this.direct.contains(type);
        }
    }
}
