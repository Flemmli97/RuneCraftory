package io.github.flemmli97.runecraftory.api.datapack.provider;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.api.datapack.FoodProperties;
import io.github.flemmli97.runecraftory.common.datapack.manager.FoodManager;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.RegistryOps;
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

public abstract class FoodProvider implements DataProvider {

    private final Map<ResourceLocation, FoodProperties.Builder> data = new HashMap<>();
    private final Map<ResourceLocation, Consumer<JsonObject>> item = new HashMap<>();

    private final PackOutput packOutput;
    private final String modid;
    protected final CompletableFuture<HolderLookup.Provider> provider;

    private final Function<Item, ResourceLocation> keyExtractor;

    @SuppressWarnings("deprecation")
    public FoodProvider(PackOutput packOutput, String modid, CompletableFuture<HolderLookup.Provider> provider) {
        this(packOutput, modid, provider, item -> item.builtInRegistryHolder().key().location());
    }

    public FoodProvider(PackOutput packOutput, String modid, CompletableFuture<HolderLookup.Provider> provider, Function<Item, ResourceLocation> keyExtractor) {
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
            this.data.forEach((res, builder) -> {
                Path path = this.packOutput.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(res.getNamespace() + "/" + FoodManager.ID + "/" + res.getPath() + ".json");
                JsonElement obj = FoodProperties.CODEC.encodeStart(ops, builder.build()).getOrThrow();
                if (obj.isJsonObject())
                    this.item.get(res).accept(obj.getAsJsonObject());
                futures.add(DataProvider.saveStable(cache, obj, path));
            });
            return CompletableFuture.allOf(futures.build().toArray(CompletableFuture[]::new));
        });
    }

    @Override
    public String getName() {
        return "FoodProps for " + this.modid;
    }

    public void addStat(ItemLike item, int duration) {
        this.addStat(item, new FoodProperties.Builder(duration));
    }

    public void addStat(String id, ItemLike item, int duration) {
        this.addStat(id, item, new FoodProperties.Builder(duration));
    }

    public void addStat(ItemLike item, FoodProperties.Builder builder) {
        this.addStat(this.keyExtractor.apply(item.asItem()).getPath(), item, builder);
    }

    public void addStat(String id, ItemLike item, FoodProperties.Builder builder) {
        ResourceLocation res = ResourceLocation.fromNamespaceAndPath(this.modid, id);
        this.data.put(res, builder);
        this.item.put(res, obj -> obj.addProperty("item", this.keyExtractor.apply(item.asItem()).toString()));
    }

    public void addStat(String id, TagKey<Item> tag, int duration) {
        this.addStat(id, tag, new FoodProperties.Builder(duration));
    }

    public void addStat(String id, TagKey<Item> tag, FoodProperties.Builder builder) {
        ResourceLocation res = ResourceLocation.fromNamespaceAndPath(this.modid, id);
        this.data.put(res, builder);
        this.item.put(res, obj -> obj.addProperty("item", "#" + tag.location()));
    }
}
