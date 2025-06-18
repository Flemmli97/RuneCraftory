package io.github.flemmli97.runecraftory.api.datapack.provider;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.api.datapack.ItemStat;
import io.github.flemmli97.runecraftory.common.datapack.manager.ItemStatManager;
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
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

public abstract class ItemStatProvider implements DataProvider {

    private final Map<ResourceLocation, ItemStat.Builder> data = new HashMap<>();
    private final Map<ResourceLocation, Consumer<JsonObject>> item = new HashMap<>();

    private final PackOutput packOutput;
    private final String modid;
    protected final CompletableFuture<HolderLookup.Provider> provider;

    private final Function<Item, ResourceLocation> keyExtractor;

    @SuppressWarnings("deprecation")
    public ItemStatProvider(PackOutput packOutput, String modid, CompletableFuture<HolderLookup.Provider> provider) {
        this(packOutput, modid, provider, item -> item.builtInRegistryHolder().key().location());
    }

    public ItemStatProvider(PackOutput packOutput, String modid, CompletableFuture<HolderLookup.Provider> provider, Function<Item, ResourceLocation> keyExtractor) {
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
                Path path = this.packOutput.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(res.getNamespace() + "/" + ItemStatManager.ID + "/" + res.getPath() + ".json");
                JsonElement obj = ItemStat.CODEC.encodeStart(ops, builder.build()).getOrThrow();
                if (obj.isJsonObject())
                    this.item.get(res).accept(obj.getAsJsonObject());
                futures.add(DataProvider.saveStable(cache, obj, path));
            });
            return CompletableFuture.allOf(futures.build().toArray(CompletableFuture[]::new));
        });
    }

    @Override
    public String getName() {
        return "ItemStats for " + this.modid;
    }

    public void addStat(ItemLike item, int buy, int sell, int upgrade) {
        this.addStat(item, new ItemStat.Builder(buy, sell, upgrade));
    }

    public void addStat(String id, ItemLike item, int buy, int sell, int upgrade) {
        this.addStat(id, item, new ItemStat.Builder(buy, sell, upgrade));
    }

    public void addStat(ItemLike item, ItemStat.Builder builder) {
        this.addStat(this.keyExtractor.apply(item.asItem()).getPath(), item, builder);
    }

    public void addStat(String id, ItemLike item, ItemStat.Builder builder) {
        ResourceLocation res = ResourceLocation.fromNamespaceAndPath(this.modid, id);
        this.data.put(res, builder);
        this.item.put(res, obj -> obj.addProperty("item", this.keyExtractor.apply(item.asItem()).toString()));
    }

    public void addStat(String id, TagKey<Item> tag, int buy, int sell, int upgrade) {
        this.addStat(id, tag, new ItemStat.Builder(buy, sell, upgrade));
    }

    public void addStat(String id, TagKey<Item> tag, ItemStat.Builder builder) {
        ResourceLocation res = ResourceLocation.fromNamespaceAndPath(this.modid, id);
        this.data.put(res, builder);
        this.item.put(res, obj -> obj.addProperty("item", "#" + tag.location()));
    }

    protected int calcBuyOf(double multiplier, ItemLike... others) {
        return this.calcValueOf(multiplier, b -> b.buyPrice, Stream.of(others)
                .map(other -> ResourceLocation.fromNamespaceAndPath(this.modid, this.keyExtractor.apply(other.asItem()).getPath()))
                .toArray(ResourceLocation[]::new));
    }

    protected int calcSellOf(double multiplier, ItemLike... others) {
        return this.calcValueOf(multiplier, b -> b.sellPrice, Stream.of(others)
                .map(other -> ResourceLocation.fromNamespaceAndPath(this.modid, this.keyExtractor.apply(other.asItem()).getPath()))
                .toArray(ResourceLocation[]::new));
    }

    protected int calcValueOf(double multiplier, ToIntFunction<ItemStat.Builder> agg, ResourceLocation... others) {
        int price = 0;
        for (ResourceLocation other : others) {
            ItemStat.Builder s = this.data.get(other);
            if (s != null) {
                price += agg.applyAsInt(s);
            }
        }
        return (int) (price * multiplier);
    }

    public static int truncate(double value) {
        if (value < 10)
            return (int) value;
        return ((int) (value / 10.)) * 10;
    }
}
