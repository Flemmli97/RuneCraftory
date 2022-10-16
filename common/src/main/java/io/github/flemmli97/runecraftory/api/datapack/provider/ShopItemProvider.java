package io.github.flemmli97.runecraftory.api.datapack.provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.datapack.RegistryObjectSerializer;
import io.github.flemmli97.runecraftory.common.entities.npc.EnumShop;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
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
import java.util.Locale;
import java.util.Map;

public abstract class ShopItemProvider implements DataProvider {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Gson GSON = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().disableHtmlEscaping()
            .registerTypeAdapter(Attribute.class, new RegistryObjectSerializer<>(PlatformUtils.INSTANCE.attributes()))
            .registerTypeAdapter(Spell.class, new RegistryObjectSerializer<>(PlatformUtils.INSTANCE.registry(ModSpells.SPELLREGISTRY_KEY))).create();

    private final Map<ResourceLocation, Collection<ItemLike>> items = new HashMap<>();
    private final Map<ResourceLocation, Collection<Ingredient>> ingredients = new HashMap<>();

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
            Path path = this.gen.getOutputFolder().resolve("data/" + res.getNamespace() + "/shop_items/" + res.getPath() + ".json");
            try {
                JsonObject obj = new JsonObject();
                if (this.overwrite.getOrDefault(res, false))
                    obj.addProperty("replace", true);
                JsonArray arr = new JsonArray();
                builder.forEach(itemLike -> arr.add(PlatformUtils.INSTANCE.items().getIDFrom(itemLike.asItem()).toString()));
                this.ingredients.getOrDefault(res, new ArrayList<>())
                        .forEach(ing -> arr.add(ing.toJson()));
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

    public void addItem(EnumShop shop, ItemLike item) {
        this.addItem(shop, item, false);
    }

    public void addItem(EnumShop shop, ItemLike item, boolean defaults) {
        this.items.computeIfAbsent(new ResourceLocation(this.modid, shop.name().toLowerCase(Locale.ROOT) + (defaults ? "_defaults" : "")),
                        r -> new ArrayList<>())
                .add(item);
    }

    public void addItem(EnumShop shop, ItemStack item) {
        this.addItem(shop, item, false);
    }

    public void addItem(EnumShop shop, ItemStack item, boolean defaults) {
        this.ingredients.computeIfAbsent(new ResourceLocation(this.modid, shop.name().toLowerCase(Locale.ROOT) + (defaults ? "_defaults" : "")),
                        r -> new ArrayList<>())
                .add(Ingredient.of(item));
    }

    public void addItem(EnumShop shop, TagKey<Item> tag) {
        this.addItem(shop, tag, false);
    }

    public void addItem(EnumShop shop, TagKey<Item> tag, boolean defaults) {
        this.ingredients.computeIfAbsent(new ResourceLocation(this.modid, shop.name().toLowerCase(Locale.ROOT) + (defaults ? "_defaults" : "")),
                        r -> new ArrayList<>())
                .add(Ingredient.of(tag));
    }

    public void overwrite(EnumShop shop, boolean defaults) {
        this.overwrite.put(new ResourceLocation(this.modid, shop.name().toLowerCase(Locale.ROOT) + (defaults ? "_defaults" : "")), true);
    }
}