package io.github.flemmli97.runecraftory.common.datapack.manager;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.datapack.ListenerExtension;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class StructureBossManager extends SimpleJsonResourceReloadListener implements ListenerExtension {

    public static final ResourceLocation ID = RuneCraftory.modRes("runecraftory_bosses");

    private Map<ResourceLocation, BossSpawnList> spawnList = new HashMap<>();

    private HolderLookup.Provider provider;

    public StructureBossManager() {
        super(DataPackHandler.GSON, ID.toString());
    }

    @Nullable
    public BossSpawnList getBoss(ResourceLocation id) {
        return this.spawnList.get(id);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, ResourceManager manager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, BossSpawnList> builder = new ImmutableMap.Builder<>();
        DynamicOps<JsonElement> ops = this.provider.createSerializationContext(JsonOps.INSTANCE);
        data.forEach((key, el) -> {
            try {
                BossSpawnList list = BossSpawnList.CODEC.parse(ops, el).getOrThrow();
                builder.put(key, list);
            } catch (Exception ex) {
                RuneCraftory.LOGGER.error("Couldn't parse boss spawn list json {} {}", key, ex);
                ex.fillInStackTrace();
            }
        });
        this.spawnList = builder.build();
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void insertRegistryAccess(HolderLookup.Provider provider) {
        this.provider = provider;
    }

    public static class BossSpawnList {

        public static final Codec<BossSpawnList> CODEC = SimpleWeightedRandomList.wrappedCodec(BuiltInRegistries.ENTITY_TYPE.byNameCodec())
                .xmap(BossSpawnList::new, b -> b.list);

        private final SimpleWeightedRandomList<EntityType<?>> list;
        private final Set<EntityType<?>> direct;

        public BossSpawnList(SimpleWeightedRandomList<EntityType<?>> list) {
            this.list = list;
            this.direct = this.list.unwrap().stream().map(WeightedEntry.Wrapper::data).collect(Collectors.toUnmodifiableSet());
        }

        public static BossSpawnList of(EntityType<?> type) {
            return new BossSpawnList(SimpleWeightedRandomList.single(type));
        }

        public Optional<EntityType<?>> getRandom(RandomSource random) {
            return this.list.getRandomValue(random);
        }

        public boolean has(EntityType<?> type) {
            return this.direct.contains(type);
        }
    }
}
