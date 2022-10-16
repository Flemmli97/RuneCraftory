package io.github.flemmli97.runecraftory.common.datapack.manager;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.datapack.ItemStat;
import io.github.flemmli97.runecraftory.api.datapack.RegistryObjectSerializer;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.npc.EnumShop;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.common.utils.JsonUtils;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * We could use tags but they dont have an order irrc. Also can make it possible to add specific item stacks.
 */
public class ShopItemsManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().enableComplexMapKeySerialization()
            .registerTypeAdapter(Attribute.class, new RegistryObjectSerializer<>(PlatformUtils.INSTANCE.attributes()))
            .registerTypeAdapter(Spell.class, new RegistryObjectSerializer<>(ModSpells.SPELLREGISTRY.get())).create();

    private Map<EnumShop, Collection<ItemStack>> shopItems = ImmutableMap.of();
    private Map<EnumShop, Collection<ItemStack>> shopItemsDefaults = ImmutableMap.of();
    private boolean checkedStats;

    public ShopItemsManager() {
        super(GSON, "shop_items");
    }

    public Collection<ItemStack> get(EnumShop shop) {
        if (!this.checkedStats) {
            this.removeNoneBuyable();
            this.checkedStats = true;
        }
        return this.shopItems.getOrDefault(shop, Collections.emptyList());
    }

    public Collection<ItemStack> getDefaultItems(EnumShop shop) {
        if (!this.checkedStats) {
            this.removeNoneBuyable();
            this.checkedStats = true;
        }
        return this.shopItemsDefaults.getOrDefault(shop, Collections.emptyList());
    }

    private void removeNoneBuyable() {
        ImmutableMap.Builder<EnumShop, Collection<ItemStack>> b = ImmutableMap.builder();
        ImmutableMap.Builder<EnumShop, Collection<ItemStack>> bD = ImmutableMap.builder();
        b.put(EnumShop.RANDOM, DataPackHandler.getAll()
                .stream().filter(p -> p.getSecond().getBuy() > 0)
                .map(Pair::getFirst)
                .toList());
        this.shopItems.forEach((s, c) -> {
            if (s != EnumShop.RANDOM) {
                Collection<ItemStack> newCollection = new ArrayList<>();
                c.forEach(stack -> {
                    if (DataPackHandler.getStats(stack.getItem()).map(ItemStat::getBuy).orElse(0) > 0)
                        newCollection.add(stack);
                });
                b.put(s, ImmutableList.copyOf(newCollection));
            }
        });
        this.shopItemsDefaults.forEach((s, c) -> {
            Collection<ItemStack> newCollection = new ArrayList<>();
            c.forEach(stack -> {
                if (DataPackHandler.getStats(stack.getItem()).map(ItemStat::getBuy).orElse(0) > 0)
                    newCollection.add(stack);
            });
            bD.put(s, ImmutableList.copyOf(newCollection));
        });
        this.shopItems = b.build();
        this.shopItemsDefaults = bD.build();
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, ResourceManager manager, ProfilerFiller profiler) {
        HashMap<EnumShop, Collection<ItemStack>> shopBuilder = new HashMap<>();
        HashMap<EnumShop, Collection<ItemStack>> shopBuilder2 = new HashMap<>();
        this.checkedStats = false;
        data.forEach((fres, el) -> {
            try {
                boolean addToDefault = fres.getPath().contains("_defaults");
                EnumShop shop;
                try {
                    shop = EnumShop.valueOf(fres.getPath().replace("_defaults", "").toUpperCase(Locale.ROOT));
                } catch (IllegalArgumentException ignored) {
                    return;
                }
                JsonObject obj = el.getAsJsonObject();
                boolean replace = JsonUtils.get(obj, "replace", false);
                JsonArray array = obj.getAsJsonArray("values");
                Collection<ItemStack> items = new ArrayList<>();
                array.forEach(val -> {
                    if (val.isJsonPrimitive()) {
                        Item item = PlatformUtils.INSTANCE.items().getFromId(new ResourceLocation(val.getAsString()));
                        if (item != Items.AIR) {
                            items.add(new ItemStack(item));
                        }
                    } else {
                        items.addAll(Arrays.asList(Ingredient.fromJson(val).getItems()));
                    }
                });
                if (addToDefault) {
                    if (replace)
                        shopBuilder2.put(shop, items);
                    else
                        shopBuilder2.compute(shop, (k, v) -> {
                            if (v == null)
                                return items;
                            v.addAll(items);
                            return v;
                        });
                } else {
                    if (replace)
                        shopBuilder.put(shop, items);
                    else
                        shopBuilder.compute(shop, (k, v) -> {
                            if (v == null)
                                return items;
                            v.addAll(items);
                            return v;
                        });
                }
            } catch (JsonSyntaxException ex) {
                RuneCraftory.logger.error("Couldnt parse shop items json {}", fres);
                ex.fillInStackTrace();
            }
        });
        ImmutableMap.Builder<EnumShop, Collection<ItemStack>> b = ImmutableMap.builder();
        ImmutableMap.Builder<EnumShop, Collection<ItemStack>> bD = ImmutableMap.builder();
        shopBuilder.forEach((s, c) -> b.put(s, ImmutableList.copyOf(c)));
        shopBuilder2.forEach((s, c) -> bD.put(s, ImmutableList.copyOf(c)));
        this.shopItems = b.build();
        this.shopItemsDefaults = bD.build();
    }
}
