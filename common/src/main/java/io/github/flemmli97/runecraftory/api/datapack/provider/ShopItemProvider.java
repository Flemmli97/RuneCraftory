package io.github.flemmli97.runecraftory.api.datapack.provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.common.datapack.manager.ShopItemsManager;
import io.github.flemmli97.runecraftory.common.entities.npc.job.NPCJob;
import io.github.flemmli97.runecraftory.common.registry.ModNPCJobs;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class ShopItemProvider implements DataProvider {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Gson GSON = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().disableHtmlEscaping().create();

    private final Map<ResourceLocation, Collection<Pair<ItemLike, Boolean>>> items = new HashMap<>();
    private final Map<ResourceLocation, Collection<Pair<Ingredient, Boolean>>> ingredients = new HashMap<>();

    private final Map<ResourceLocation, Boolean> overwrite = new HashMap<>();

    private final DataGenerator gen;
    private final String modid;

    public ShopItemProvider(DataGenerator gen, String modid) {
        this.gen = gen;
        this.modid = modid;
    }

    protected abstract void add();

    @Override
    public void run(HashCache cache) {
        this.add();
        this.items.forEach((res, builder) -> {
            Path path = this.gen.getOutputFolder().resolve("data/" + res.getNamespace() + "/" + ShopItemsManager.DIRECTORY + "/" + res.getPath() + ".json");
            try {
                JsonObject obj = new JsonObject();
                if (this.overwrite.getOrDefault(res, false))
                    obj.addProperty("replace", true);
                JsonArray arr = new JsonArray();
                builder.forEach(pair -> {
                    JsonObject o = new JsonObject();
                    o.addProperty("item", PlatformUtils.INSTANCE.items().getIDFrom(pair.getFirst().asItem()).toString());
                    o.addProperty("needs_unlock", pair.getSecond());
                    arr.add(o);
                });
                this.ingredients.getOrDefault(res, new ArrayList<>())
                        .forEach(pair -> {
                            JsonObject o = new JsonObject();
                            o.add("item", pair.getFirst().toJson());
                            o.addProperty("needs_unlock", pair.getSecond());
                            arr.add(o);
                        });
                obj.add("values", arr);
                DataProvider.save(GSON, cache, obj, path);
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
        this.addItem(shop, item, false, false);
    }

    public void addItem(NPCJob shop, ItemLike item, boolean defaults) {
        this.addItem(shop, item, defaults, false);
    }

    public void addItem(NPCJob shop, ItemLike item, boolean defaults, boolean needsUnlock) {
        ResourceLocation res = ModNPCJobs.getIDFrom(shop);
        this.items.computeIfAbsent(new ResourceLocation(res.getNamespace(), res.getPath() + (defaults ? "_defaults" : "")),
                        r -> new ArrayList<>())
                .add(Pair.of(item, needsUnlock));
    }

    public void addItem(NPCJob shop, ItemStack item) {
        this.addItem(shop, item, false, false);
    }

    public void addItem(NPCJob shop, ItemStack item, boolean defaults) {
        this.addItem(shop, item, defaults, false);
    }

    public void addItem(NPCJob shop, ItemStack item, boolean defaults, boolean needsUnlock) {
        ResourceLocation res = ModNPCJobs.getIDFrom(shop);
        this.ingredients.computeIfAbsent(new ResourceLocation(res.getNamespace(), res.getPath() + (defaults ? "_defaults" : "")),
                        r -> new ArrayList<>())
                .add(Pair.of(Ingredient.of(item), needsUnlock));
    }

    public void addItem(NPCJob shop, TagKey<Item> tag) {
        this.addItem(shop, tag, false);
    }

    public void addItem(NPCJob shop, TagKey<Item> tag, boolean defaults) {
        this.addItem(shop, tag, defaults, false);
    }

    public void addItem(NPCJob shop, TagKey<Item> tag, boolean defaults, boolean needsUnlock) {
        ResourceLocation res = ModNPCJobs.getIDFrom(shop);
        this.ingredients.computeIfAbsent(new ResourceLocation(res.getNamespace(), res.getPath() + (defaults ? "_defaults" : "")),
                        r -> new ArrayList<>())
                .add(Pair.of(Ingredient.of(tag), needsUnlock));
    }

    public void overwrite(NPCJob shop, boolean defaults) {
        ResourceLocation res = ModNPCJobs.getIDFrom(shop);
        this.overwrite.put(new ResourceLocation(res.getNamespace(), res.getPath() + (defaults ? "_defaults" : "")), true);
    }
}