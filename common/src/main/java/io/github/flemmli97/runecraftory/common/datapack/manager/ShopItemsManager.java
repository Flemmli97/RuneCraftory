package io.github.flemmli97.runecraftory.common.datapack.manager;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.ItemStat;
import io.github.flemmli97.runecraftory.api.datapack.RegistryObjectSerializer;
import io.github.flemmli97.runecraftory.api.datapack.ShopItemProperties;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.npc.job.NPCJob;
import io.github.flemmli97.runecraftory.common.registry.ModNPCJobs;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * We could use tags but they dont have an order irrc. Also can make it possible to add specific item stacks.
 */
public class ShopItemsManager extends SimpleJsonResourceReloadListener {

    public static final String DIRECTORY = "shop_items";

    public static final Gson GSON = new GsonBuilder().enableComplexMapKeySerialization()
            .registerTypeAdapter(Attribute.class, new RegistryObjectSerializer<>(Suppliers.memoize(PlatformUtils.INSTANCE::attributes)))
            .registerTypeAdapter(Spell.class, new RegistryObjectSerializer<>(Suppliers.memoize(ModSpells.SPELL_REGISTRY::get))).create();

    private Map<NPCJob, Collection<ShopItemProperties>> shopItems = ImmutableMap.of();
    private Map<NPCJob, Collection<ShopItemProperties>> shopItemsDefaults = ImmutableMap.of();
    private boolean checkedStats;

    public ShopItemsManager() {
        super(GSON, DIRECTORY);
    }

    public Collection<ShopItemProperties> get(NPCJob shop) {
        if (!this.checkedStats) {
            this.removeNoneBuyable();
            this.checkedStats = true;
        }
        return this.shopItems.getOrDefault(shop, Collections.emptyList());
    }

    public Collection<ShopItemProperties> getDefaultItems(NPCJob shop) {
        if (!this.checkedStats) {
            this.removeNoneBuyable();
            this.checkedStats = true;
        }
        return this.shopItemsDefaults.getOrDefault(shop, Collections.emptyList());
    }

    private void removeNoneBuyable() {
        ImmutableMap.Builder<NPCJob, Collection<ShopItemProperties>> b = ImmutableMap.builder();
        ImmutableMap.Builder<NPCJob, Collection<ShopItemProperties>> bD = ImmutableMap.builder();
        b.put(ModNPCJobs.RANDOM.getSecond(), DataPackHandler.INSTANCE.itemStatManager().all()
                .stream().filter(p -> p.getSecond().getBuy() > 0)
                .map(p -> new ShopItemProperties(p.getFirst(), ShopItemProperties.UnlockType.NEEDS_SHIPPING, EntityPredicate.ANY))
                .toList());
        this.shopItems.forEach((s, c) -> {
            if (s != ModNPCJobs.RANDOM.getSecond()) {
                Collection<ShopItemProperties> newCollection = new ArrayList<>();
                c.forEach(props -> {
                    if (DataPackHandler.INSTANCE.itemStatManager().get(props.stack().getItem()).map(ItemStat::getBuy).orElse(0) > 0)
                        newCollection.add(props);
                });
                b.put(s, ImmutableList.copyOf(newCollection));
            }
        });
        this.shopItemsDefaults.forEach((s, c) -> {
            Collection<ShopItemProperties> newCollection = new ArrayList<>();
            c.forEach(stack -> {
                if (DataPackHandler.INSTANCE.itemStatManager().get(stack.stack().getItem()).map(ItemStat::getBuy).orElse(0) > 0)
                    newCollection.add(stack);
            });
            bD.put(s, ImmutableList.copyOf(newCollection));
        });
        this.shopItems = b.build();
        this.shopItemsDefaults = bD.build();
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, ResourceManager manager, ProfilerFiller profiler) {
        HashMap<NPCJob, Collection<ShopItemProperties>> shopBuilder = new HashMap<>();
        HashMap<NPCJob, Collection<ShopItemProperties>> defaultItemsBuilder = new HashMap<>();

        this.checkedStats = false;
        data.forEach((fres, el) -> {
            try {
                NPCJob shop = ModNPCJobs.getFromID(new ResourceLocation(fres.getNamespace(), fres.getPath()));
                if (!shop.hasShop)
                    return;
                JsonObject obj = el.getAsJsonObject();
                boolean replace = GsonHelper.getAsBoolean(obj, "replace", false);
                JsonArray array = GsonHelper.getAsJsonArray(obj, "values");
                // Separate tag based and non tag based entries as non tag based should take priority
                Collection<ShopItemProperties> items = new ArrayList<>();
                Collection<ShopItemProperties> itemTags = new ArrayList<>();
                array.forEach(val -> {
                    ShopItemProperties.IntermediaryShopItem prop = ShopItemProperties.CODEC.parse(JsonOps.INSTANCE, val)
                            .getOrThrow(false, RuneCraftory.LOGGER::error);
                    List<ShopItemProperties> props = ShopItemProperties.from(prop);
                    if (prop.isTag())
                        itemTags.addAll(props);
                    else
                        items.addAll(props);
                });
                Collection<ShopItemProperties> shopItems = new ArrayList<>();
                // Track if item already has been added before handling tags
                HashMap<Item, ShopItemProperties> dupeTracker = new HashMap<>();
                for (ShopItemProperties prop : items) {
                    shopItems.add(prop);
                    if (!prop.stack().hasTag())
                        dupeTracker.put(prop.stack().getItem(), prop);
                }
                for (ShopItemProperties prop : itemTags) {
                    if (!dupeTracker.containsKey(prop.stack().getItem()))
                        shopItems.add(prop);
                }
                if (replace)
                    shopBuilder.put(shop, shopItems);
                else
                    shopBuilder.compute(shop, (k, v) -> {
                        if (v == null)
                            return shopItems;
                        v.addAll(shopItems);
                        return v;
                    });
            } catch (JsonSyntaxException ex) {
                RuneCraftory.LOGGER.error("Couldnt parse shop stack json {} {}", fres, ex);
                ex.fillInStackTrace();
            }
        });
        ImmutableMap.Builder<NPCJob, Collection<ShopItemProperties>> b = ImmutableMap.builder();
        ImmutableMap.Builder<NPCJob, Collection<ShopItemProperties>> bD = ImmutableMap.builder();
        shopBuilder.forEach((s, c) -> {
            b.put(s, c.stream().filter(p -> p.unlockType() != ShopItemProperties.UnlockType.DEFAULT).toList());
            bD.put(s, c.stream().filter(p -> p.unlockType() == ShopItemProperties.UnlockType.DEFAULT).toList());
        });
        this.shopItems = b.build();
        this.shopItemsDefaults = bD.build();
    }
}
