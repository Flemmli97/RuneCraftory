package io.github.flemmli97.runecraftory.common.datapack.manager;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.ItemStat;
import io.github.flemmli97.runecraftory.api.datapack.ShopItemProperties;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.datapack.ListenerExtension;
import io.github.flemmli97.runecraftory.common.entities.npc.job.NPCJob;
import io.github.flemmli97.runecraftory.common.registry.ModNPCJobs;
import io.github.flemmli97.runecraftory.common.utils.HolderUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * We could use tags but they dont have an order irrc. Also can make it possible to add specific item stacks.
 */
public class ShopItemsManager extends SimpleJsonResourceReloadListener implements ListenerExtension {

    public static final ResourceLocation ID = RuneCraftory.modRes("shop_items");

    private Map<NPCJob, Collection<ShopItemProperties>> shopItems = ImmutableMap.of();
    private Map<NPCJob, Collection<ShopItemProperties>> shopItemsDefaults = ImmutableMap.of();
    private boolean resolved;
    private Map<NPCJob, Collection<ShopItemProperties.IntermediaryShopItem>> intermediaryData = ImmutableMap.of();

    private HolderLookup.Provider provider;

    public ShopItemsManager() {
        super(DataPackHandler.GSON, ID.toString());
    }

    public Collection<ShopItemProperties> get(NPCJob shop) {
        this.resolve();
        return this.shopItems.getOrDefault(shop, Collections.emptyList());
    }

    public Collection<ShopItemProperties> getDefaultItems(NPCJob shop) {
        this.resolve();
        return this.shopItemsDefaults.getOrDefault(shop, Collections.emptyList());
    }

    public void resolve() {
        if (!this.resolved) {
            this.resolved = true;
            ImmutableMap.Builder<NPCJob, Collection<ShopItemProperties>> builder = ImmutableMap.builder();
            ImmutableMap.Builder<NPCJob, Collection<ShopItemProperties>> defaultsBuilder = ImmutableMap.builder();
            this.intermediaryData.forEach((job, items) -> {
                Collection<ShopItemProperties> newCollection = new ArrayList<>();
                Collection<ShopItemProperties> defaultCollection = new ArrayList<>();
                items.forEach(props -> {
                    List<ShopItemProperties> contents = ShopItemProperties.from(this.provider, props);
                    contents.forEach((prop -> {
                        if (DataPackHandler.INSTANCE.itemStatManager().get(prop.stack().getItem()).map(ItemStat::getBuy).orElse(0) > 0) {
                            if (prop.unlockType() == ShopItemProperties.UnlockType.DEFAULT) {
                                defaultCollection.add(prop);
                            } else {
                                newCollection.add(prop);
                            }
                        }
                    }));
                });
                builder.put(job, ImmutableList.copyOf(newCollection));
                defaultsBuilder.put(job, ImmutableList.copyOf(defaultCollection));
            });
            builder.put(ModNPCJobs.RANDOM.get(), DataPackHandler.INSTANCE.itemStatManager().all()
                    .stream().filter(p -> p.getSecond().getBuy() > 0)
                    .map(p -> new ShopItemProperties(p.getFirst(), ShopItemProperties.UnlockType.NEEDS_SHIPPING, Optional.empty()))
                    .toList());
            this.shopItems = builder.build();
            this.shopItemsDefaults = defaultsBuilder.build();
        }
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, ResourceManager manager, ProfilerFiller profiler) {
        HashMap<NPCJob, Collection<ShopItemProperties.IntermediaryShopItem>> shops = new HashMap<>();
        this.resolved = false;
        DynamicOps<JsonElement> ops = this.provider.createSerializationContext(JsonOps.INSTANCE);
        data.forEach((fres, el) -> {
            try {
                Optional<NPCJob> optionalNPCJob = HolderUtils.get(this.provider, ModNPCJobs.JOB_REGISTRY_KEY, fres);
                if (optionalNPCJob.isEmpty() || !optionalNPCJob.get().hasShop)
                    return;
                NPCJob job = optionalNPCJob.get();
                JsonObject obj = el.getAsJsonObject();
                boolean replace = GsonHelper.getAsBoolean(obj, "replace", false);
                JsonArray array = GsonHelper.getAsJsonArray(obj, "values");
                // Separate tag based and non tag based entries as non tag based should take priority
                List<ShopItemProperties.IntermediaryShopItem> contents = new ArrayList<>();
                array.forEach(val -> {
                    ShopItemProperties.IntermediaryShopItem prop = ShopItemProperties.CODEC.parse(ops, val).getOrThrow();
                    contents.add(prop);
                });
                if (replace)
                    shops.put(job, contents);
                else
                    shops.compute(job, (k, v) -> {
                        if (v == null)
                            return contents;
                        v.addAll(contents);
                        return v;
                    });
            } catch (JsonSyntaxException ex) {
                RuneCraftory.LOGGER.error("Couldn't parse shop stack json {} {}", fres, ex);
                ex.fillInStackTrace();
            }
        });
        this.intermediaryData = shops;
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void insertRegistryAccess(HolderLookup.Provider provider) {
        this.provider = provider;
    }
}
