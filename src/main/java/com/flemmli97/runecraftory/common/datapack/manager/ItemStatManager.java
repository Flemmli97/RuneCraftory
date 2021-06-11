package com.flemmli97.runecraftory.common.datapack.manager;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.Spell;
import com.flemmli97.runecraftory.api.datapack.ItemStat;
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
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryManager;

import java.util.Map;

public class ItemStatManager extends JsonReloadListener {

    private static final Gson GSON = new GsonBuilder().enableComplexMapKeySerialization()
            .registerTypeAdapter(Attribute.class, new RegistryObjectSerializer<>(ForgeRegistries.ATTRIBUTES))
            .registerTypeAdapter(Spell.class, new RegistryObjectSerializer<>(RegistryManager.ACTIVE.getRegistry(Spell.class))).create();

    private Map<ResourceLocation, ItemStat> itemstat = ImmutableMap.of();

    public ItemStatManager() {
        super(GSON, "item_stats");
    }

    public ItemStat get(Item item) {
        return this.itemstat.get(item.getRegistryName());
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, IResourceManager manager, IProfiler profiler) {
        ImmutableMap.Builder<ResourceLocation, ItemStat> builder = ImmutableMap.builder();
        data.forEach((fres, el) -> {
            try {
                JsonObject obj = el.getAsJsonObject();
                if (obj.has("tag")) {
                    ITag<Item> tag = TagCollectionManager.getManager().getItemTags().get(new ResourceLocation(obj.get("tag").getAsString()));
                    tag.getAllElements().forEach(item -> builder.put(item.getRegistryName(), GSON.fromJson(el, ItemStat.class)));
                } else if (obj.has("item")) {
                    ResourceLocation res = new ResourceLocation(obj.get("item").getAsString());
                    builder.put(res, GSON.fromJson(el, ItemStat.class));
                }
            } catch (JsonSyntaxException ex) {
                RuneCraftory.logger.error("Couldnt parse item stat json {}", fres);
                ex.fillInStackTrace();
            }
        });
        this.itemstat = builder.build();
    }

    public void toPacket(PacketBuffer buffer) {
        buffer.writeInt(this.itemstat.size());
        this.itemstat.forEach((res, stat) -> {
            buffer.writeResourceLocation(res);
            stat.toPacket(buffer);
        });
    }

    public void fromPacket(PacketBuffer buffer) {
        ImmutableMap.Builder<ResourceLocation, ItemStat> builder = ImmutableMap.builder();
        int size = buffer.readInt();
        for (int i = 0; i < size; i++)
            builder.put(buffer.readResourceLocation(), ItemStat.fromPacket(buffer));
        this.itemstat = builder.build();
    }
}
