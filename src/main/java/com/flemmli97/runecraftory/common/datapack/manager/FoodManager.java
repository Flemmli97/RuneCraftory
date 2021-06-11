package com.flemmli97.runecraftory.common.datapack.manager;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.datapack.FoodProperties;
import com.flemmli97.runecraftory.api.datapack.RegistryObjectSerializer;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Effect;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class FoodManager extends JsonReloadListener {

    private static final Gson GSON = new GsonBuilder().enableComplexMapKeySerialization()
            .registerTypeAdapter(Attribute.class, new RegistryObjectSerializer<>(ForgeRegistries.ATTRIBUTES))
            .registerTypeAdapter(Effect.class, new RegistryObjectSerializer<>(ForgeRegistries.POTIONS)).create();
    private Map<ResourceLocation, FoodProperties> food = ImmutableMap.of();

    public FoodManager() {
        super(GSON, "food_stats");
    }

    public FoodProperties get(Item item) {
        return this.food.get(item.getRegistryName());
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, IResourceManager manager, IProfiler profiler) {
        ImmutableMap.Builder<ResourceLocation, FoodProperties> builder = ImmutableMap.builder();
        data.forEach((fres, el) -> {
            try {
                JsonObject obj = el.getAsJsonObject();
                if (obj.has("tag")) {
                    ITag<Item> tag = TagCollectionManager.getManager().getItemTags().get(new ResourceLocation(obj.get("tag").getAsString()));
                    tag.getAllElements().forEach(item -> builder.put(item.getRegistryName(), GSON.fromJson(el, FoodProperties.class)));
                } else if (obj.has("item")) {
                    ResourceLocation res = new ResourceLocation(obj.get("item").getAsString());
                    builder.put(res, GSON.fromJson(el, FoodProperties.class));
                }
            } catch (JsonSyntaxException ex) {
                RuneCraftory.logger.error("Couldnt parse food stat json {}", fres);
                ex.fillInStackTrace();
            }
        });
        this.food = builder.build();
    }

    public void toPacket(PacketBuffer buffer) {
        buffer.writeInt(this.food.size());
        this.food.forEach((res, prop) -> {
            buffer.writeResourceLocation(res);
            prop.toPacket(buffer);
        });
    }

    public void fromPacket(PacketBuffer buffer) {
        ImmutableMap.Builder<ResourceLocation, FoodProperties> builder = ImmutableMap.builder();
        int size = buffer.readInt();
        for (int i = 0; i < size; i++)
            builder.put(buffer.readResourceLocation(), FoodProperties.fromPacket(buffer));
        this.food = builder.build();
    }
}
