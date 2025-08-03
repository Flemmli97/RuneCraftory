package io.github.flemmli97.runecraftory.api.datapack.provider;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.common.datapack.manager.FarmlandHoeTileActionManager;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public abstract class FarmlandHoeTileActionProvider implements DataProvider {

    private final Map<ResourceLocation, FarmlandHoeTileActionManager.Data> data = new HashMap<>();

    private final PackOutput packOutput;
    private final String modid;
    protected final CompletableFuture<HolderLookup.Provider> provider;

    private final Function<Item, ResourceLocation> keyExtractor;

    @SuppressWarnings("deprecation")
    public FarmlandHoeTileActionProvider(PackOutput packOutput, String modid, CompletableFuture<HolderLookup.Provider> provider) {
        this(packOutput, modid, provider, item -> item.builtInRegistryHolder().key().location());
    }

    public FarmlandHoeTileActionProvider(PackOutput packOutput, String modid, CompletableFuture<HolderLookup.Provider> provider, Function<Item, ResourceLocation> keyExtractor) {
        this.packOutput = packOutput;
        this.modid = modid;
        this.provider = provider;
        this.keyExtractor = keyExtractor;
    }

    protected abstract void add(HolderLookup.Provider provider);

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return this.provider.thenApply(provider -> {
            this.add(provider);
            return provider;
        }).thenCompose(provider -> {
            DynamicOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, provider);
            ImmutableList.Builder<CompletableFuture<?>> futures = new ImmutableList.Builder<>();
            this.data.forEach((res, data) -> {
                Path path = this.packOutput.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(res.getNamespace() + "/" + FarmlandHoeTileActionManager.DIRECTORY + "/" + res.getPath() + ".json");
                JsonElement obj = FarmlandHoeTileActionManager.Data.CODEC.encodeStart(ops, data).getOrThrow();
                futures.add(DataProvider.saveStable(cache, obj, path));
            });
            return CompletableFuture.allOf(futures.build().toArray(CompletableFuture[]::new));
        });
    }

    @Override
    public String getName() {
        return "Fertilizer Data for " + this.modid;
    }

    public void add(ItemLike item, int amount) {
        this.add(this.keyExtractor.apply(item.asItem()).getPath(), item.asItem(), amount);
    }

    public void add(String id, ItemLike item, int amount) {
        ResourceLocation res = ResourceLocation.fromNamespaceAndPath(this.modid, id);
        this.data.put(res, new FarmlandHoeTileActionManager.Data(item.asItem(), amount));
    }
}
