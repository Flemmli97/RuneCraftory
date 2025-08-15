package io.github.flemmli97.runecraftory.neoforge.data;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.api.datapack.EntityProperties;
import io.github.flemmli97.runecraftory.common.datapack.manager.MonsterPropertiesManager;
import io.github.flemmli97.runecraftory.common.lib.LibAdvancements;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MobPropertiesgen implements DataProvider {

    private final PackOutput packOutput;
    protected final CompletableFuture<HolderLookup.Provider> provider;

    public MobPropertiesgen(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
        this.packOutput = packOutput;
        this.provider = provider;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return this.provider.thenCompose(provider -> {
            DynamicOps<JsonElement> ops = provider.createSerializationContext(JsonOps.INSTANCE);
            ImmutableList.Builder<CompletableFuture<?>> futures = new ImmutableList.Builder<>();
            Map<ResourceLocation, EntityProperties.Builder> props = new HashMap<>(RuneCraftoryEntities.getDefaultMobProperties());
            props.put(RuneCraftoryEntities.SANO_AND_UNO.getID(), new EntityProperties.Builder()
                    .withSpawnerPredicate(LibAdvancements.playerAdvancementCheck(LibAdvancements.MARIONETTA)));
            props.forEach((res, builder) -> {
                Path path = this.packOutput.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(res.getNamespace() + "/" + MonsterPropertiesManager.DIRECTORY + "/" + res.getPath() + ".json");
                JsonElement obj = EntityProperties.CODEC.encodeStart(ops, builder.build()).getOrThrow();
                futures.add(DataProvider.saveStable(cache, obj, path));
            });
            return CompletableFuture.allOf(futures.build().toArray(CompletableFuture[]::new));
        });
    }

    @Override
    public String getName() {
        return "EntityProperties";
    }
}