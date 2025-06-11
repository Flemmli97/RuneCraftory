package io.github.flemmli97.runecraftory.api.datapack.provider;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.GsonInstances;
import io.github.flemmli97.runecraftory.api.datapack.ShopItemProperties;
import io.github.flemmli97.runecraftory.common.datapack.manager.ShopItemsManager;
import io.github.flemmli97.runecraftory.common.entities.npc.job.NPCJob;
import io.github.flemmli97.runecraftory.common.registry.ModNPCJobs;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ShopItemProvider implements DataProvider {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Map<ResourceLocation, Collection<ShopItemProperties.IntermediaryShopItem>> props = new HashMap<>();

    private final Map<ResourceLocation, Boolean> overwrite = new HashMap<>();

    private final PackOutput packOutput;

    public ShopItemProvider(PackOutput packOutput) {
        this.gen = gen;
    }

    protected abstract void add();

    @Override
    public void run(HashCache cache) {
        this.add();
        this.props.forEach((res, builder) -> {
            Path path = this.gen.getOutputFolder().resolve("data/" + res.getNamespace() + "/" + ShopItemsManager.DIRECTORY + "/" + res.getPath() + ".json");
            try {
                JsonObject obj = new JsonObject();
                if (this.overwrite.getOrDefault(res, false))
                    obj.addProperty("replace", true);
                JsonArray arr = new JsonArray();
                builder.forEach(prop -> arr.add(ShopItemProperties.CODEC.encodeStart(JsonOps.INSTANCE, prop)
                        .getOrThrow(false, RuneCraftory.LOGGER::error)));
                obj.add("values", arr);
                DataProvider.save(GsonInstances.GSON, cache, obj, path);
            } catch (IOException e) {
                LOGGER.error("Couldn't save itemstat {}", path, e);
            }
        });
    }

    @Override
    public String getName() {
        return "ItemStats";
    }

    public void addItem(NPCJob shop, ItemLike item) {
        this.addItem(shop, item, ShopItemProperties.UnlockType.NEEDS_SHIPPING);
    }

    public void addItem(NPCJob shop, ItemLike item, ShopItemProperties.UnlockType unlockType) {
        this.addItem(shop, item, unlockType, EntityPredicate.ANY);
    }

    public void addItem(NPCJob shop, ItemLike item, ShopItemProperties.UnlockType unlockType, EntityPredicate predicate) {
        ResourceLocation res = ModNPCJobs.getIDFrom(shop);
        this.props.computeIfAbsent(new ResourceLocation(res.getNamespace(), res.getPath()),
                        r -> new ArrayList<>())
                .add(new ShopItemProperties.IntermediaryShopItem(ShopItemProperties.MultiItemValue.of(item.asItem()), unlockType, predicate));
    }

    public void addItem(NPCJob shop, ItemStack item) {
        this.addItem(shop, item, ShopItemProperties.UnlockType.NEEDS_SHIPPING);
    }

    public void addItem(NPCJob shop, ItemStack item, ShopItemProperties.UnlockType unlockType) {
        this.addItem(shop, item, unlockType, EntityPredicate.ANY);
    }

    public void addItem(NPCJob shop, ItemStack item, ShopItemProperties.UnlockType unlockType, EntityPredicate predicate) {
        ResourceLocation res = ModNPCJobs.getIDFrom(shop);
        this.props.computeIfAbsent(new ResourceLocation(res.getNamespace(), res.getPath()),
                        r -> new ArrayList<>())
                .add(new ShopItemProperties.IntermediaryShopItem(new ShopItemProperties.MultiItemValue(List.of(item)), unlockType, predicate));
    }

    public void addItem(NPCJob shop, TagKey<Item> tag) {
        this.addItem(shop, tag, ShopItemProperties.UnlockType.NEEDS_SHIPPING);
    }

    public void addItem(NPCJob shop, TagKey<Item> tag, ShopItemProperties.UnlockType unlockType) {
        this.addItem(shop, tag, unlockType, EntityPredicate.ANY);
    }

    public void addItem(NPCJob shop, TagKey<Item> tag, ShopItemProperties.UnlockType unlockType, EntityPredicate predicate) {
        ResourceLocation res = ModNPCJobs.getIDFrom(shop);
        this.props.computeIfAbsent(new ResourceLocation(res.getNamespace(), res.getPath()),
                        r -> new ArrayList<>())
                .add(new ShopItemProperties.IntermediaryShopItem(new ShopItemProperties.MultiItemValue(tag), unlockType, predicate));
    }

    public void overwrite(NPCJob shop, boolean defaults) {
        ResourceLocation res = ModNPCJobs.getIDFrom(shop);
        this.overwrite.put(new ResourceLocation(res.getNamespace(), res.getPath() + (defaults ? "_defaults" : "")), true);
    }
}