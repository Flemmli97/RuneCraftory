package io.github.flemmli97.runecraftory.api.datapack.provider;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.common.datapack.manager.CropManager;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class CropProvider implements DataProvider {

    private final Map<ResourceLocation, CropProperties.Builder> data = new HashMap<>();
    private final Map<ResourceLocation, Consumer<JsonObject>> item = new HashMap<>();

    private final PackOutput packOutput;
    private final String modid;
    protected final CompletableFuture<HolderLookup.Provider> provider;

    private final Function<Item, ResourceLocation> keyExtractor;

    @SuppressWarnings("deprecation")
    public CropProvider(PackOutput packOutput, String modid, CompletableFuture<HolderLookup.Provider> provider) {
        this(packOutput, modid, provider, item -> item.builtInRegistryHolder().key().location());
    }

    public CropProvider(PackOutput packOutput, String modid, CompletableFuture<HolderLookup.Provider> provider, Function<Item, ResourceLocation> keyExtractor) {
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
            DynamicOps<JsonElement> ops = provider.createSerializationContext(JsonOps.INSTANCE);
            ImmutableList.Builder<CompletableFuture<?>> futures = new ImmutableList.Builder<>();
            this.data.forEach((res, builder) -> {
                Path path = this.packOutput.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(res.getNamespace() + "/" + CropManager.DIRECTORY + "/" + res.getPath() + ".json");
                JsonElement obj = CropProperties.CODEC.encodeStart(ops, builder.build()).getOrThrow();
                if (obj.isJsonObject())
                    this.item.get(res).accept(obj.getAsJsonObject());
                futures.add(DataProvider.saveStable(cache, obj, path));
            });
            return CompletableFuture.allOf(futures.build().toArray(CompletableFuture[]::new));
        });
    }

    @Override
    public String getName() {
        return "CropProps for " + this.modid;
    }

    public void addStat(ItemLike item, int growth, int maxDrops, boolean regrowable) {
        this.addStat(item, new CropProperties.Builder(growth, maxDrops, regrowable));
    }

    public void addStat(String id, ItemLike item, int growth, int maxDrops, boolean regrowable) {
        this.addStat(id, item, new CropProperties.Builder(growth, maxDrops, regrowable));
    }

    public void addStat(ItemLike item, CropProperties.Builder builder) {
        this.addStat(this.keyExtractor.apply(item.asItem()).getPath(), item, builder);
    }

    public void addStat(String id, ItemLike item, CropProperties.Builder builder) {
        ResourceLocation res = ResourceLocation.fromNamespaceAndPath(this.modid, id);
        this.data.put(res, builder);
        this.item.put(res, obj -> obj.addProperty("item", this.keyExtractor.apply(item.asItem()).toString()));
    }

    public void addStat(String id, TagKey<Item> tag, int growth, int maxDrops, boolean regrowable) {
        this.addStat(id, tag, new CropProperties.Builder(growth, maxDrops, regrowable));
    }

    public void addStat(String id, TagKey<Item> tag, CropProperties.Builder builder) {
        ResourceLocation res = ResourceLocation.fromNamespaceAndPath(this.modid, id);
        this.data.put(res, builder);
        this.item.put(res, obj -> obj.addProperty("item", "#" + tag.location()));
    }
}
