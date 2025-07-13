package io.github.flemmli97.runecraftory.api.datapack.provider;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.api.datapack.ShopItemProperties;
import io.github.flemmli97.runecraftory.api.registry.NPCProfession;
import io.github.flemmli97.runecraftory.common.datapack.manager.ShopItemsManager;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public abstract class ShopItemProvider implements DataProvider {

    private final Map<ResourceLocation, Collection<ShopItemProperties.IntermediaryShopItem>> props = new HashMap<>();

    private final Map<ResourceLocation, Boolean> overwrite = new HashMap<>();

    private final PackOutput packOutput;
    protected final String modid;
    protected final CompletableFuture<HolderLookup.Provider> provider;

    public ShopItemProvider(PackOutput packOutput, String modid, CompletableFuture<HolderLookup.Provider> provider) {
        this.packOutput = packOutput;
        this.modid = modid;
        this.provider = provider;
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
            this.props.forEach((res, builder) -> {
                Path path = this.packOutput.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(res.getNamespace() + "/" + ShopItemsManager.DIRECTORY + "/" + res.getPath() + ".json");
                JsonObject obj = new JsonObject();
                if (this.overwrite.getOrDefault(res, false))
                    obj.addProperty("replace", true);
                JsonArray arr = new JsonArray();
                builder.forEach(prop -> arr.add(ShopItemProperties.CODEC.encodeStart(ops, prop).getOrThrow()));
                obj.add("values", arr);
                futures.add(DataProvider.saveStable(cache, obj, path));
            });
            return CompletableFuture.allOf(futures.build().toArray(CompletableFuture[]::new));
        });
    }

    @Override
    public String getName() {
        return "ShopItems for " + this.modid;
    }

    public void addItem(Holder<NPCProfession> shop, ItemLike item) {
        this.addItem(shop, item, ShopItemProperties.UnlockType.NEEDS_SHIPPING);
    }

    public void addItem(Holder<NPCProfession> shop, ItemLike item, ShopItemProperties.UnlockType unlockType) {
        this.props.computeIfAbsent(shop.unwrapKey().get().location(),
                        r -> new ArrayList<>())
                .add(new ShopItemProperties.IntermediaryShopItem(ShopItemProperties.MultiItemValue.of(item.asItem()), unlockType, Optional.empty()));
    }

    public void addItem(Holder<NPCProfession> shop, ItemLike item, ShopItemProperties.UnlockType unlockType, EntityPredicate predicate) {
        this.props.computeIfAbsent(shop.unwrapKey().get().location(),
                        r -> new ArrayList<>())
                .add(new ShopItemProperties.IntermediaryShopItem(ShopItemProperties.MultiItemValue.of(item.asItem()), unlockType, Optional.of(predicate)));
    }

    public void addItem(Holder<NPCProfession> shop, ItemStack item) {
        this.addItem(shop, item, ShopItemProperties.UnlockType.NEEDS_SHIPPING);
    }

    public void addItem(Holder<NPCProfession> shop, ItemStack item, ShopItemProperties.UnlockType unlockType) {
        this.props.computeIfAbsent(shop.unwrapKey().get().location(),
                        r -> new ArrayList<>())
                .add(new ShopItemProperties.IntermediaryShopItem(new ShopItemProperties.MultiItemValue(List.of(item)), unlockType, Optional.empty()));
    }

    public void addItem(Holder<NPCProfession> shop, ItemStack item, ShopItemProperties.UnlockType unlockType, EntityPredicate predicate) {
        this.props.computeIfAbsent(shop.unwrapKey().get().location(),
                        r -> new ArrayList<>())
                .add(new ShopItemProperties.IntermediaryShopItem(new ShopItemProperties.MultiItemValue(List.of(item)), unlockType, Optional.of(predicate)));
    }

    public void addItem(Holder<NPCProfession> shop, TagKey<Item> tag) {
        this.addItem(shop, tag, ShopItemProperties.UnlockType.NEEDS_SHIPPING);
    }

    public void addItem(Holder<NPCProfession> shop, TagKey<Item> tag, ShopItemProperties.UnlockType unlockType) {
        this.props.computeIfAbsent(shop.unwrapKey().get().location(),
                        r -> new ArrayList<>())
                .add(new ShopItemProperties.IntermediaryShopItem(new ShopItemProperties.MultiItemValue(tag), unlockType, Optional.empty()));
    }

    public void addItem(Holder<NPCProfession> shop, TagKey<Item> tag, ShopItemProperties.UnlockType unlockType, EntityPredicate predicate) {
        this.props.computeIfAbsent(shop.unwrapKey().get().location(),
                        r -> new ArrayList<>())
                .add(new ShopItemProperties.IntermediaryShopItem(new ShopItemProperties.MultiItemValue(tag), unlockType, Optional.of(predicate)));
    }

    public void overwrite(Holder<NPCProfession> shop, boolean defaults) {
        ResourceLocation res = shop.unwrapKey().get().location();
        this.overwrite.put(ResourceLocation.fromNamespaceAndPath(res.getNamespace(), res.getPath() + (defaults ? "_defaults" : "")), true);
    }
}