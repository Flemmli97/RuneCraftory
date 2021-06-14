package io.github.flemmli97.runecraftory.common.datapack.manager;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class CropManager extends JsonReloadListener {

    private static final Gson GSON = new GsonBuilder().create();
    private Map<ResourceLocation, CropProperties> crops = ImmutableMap.of();

    public CropManager() {
        super(GSON, "crop_properties");
    }

    public CropProperties get(Item item) {
        if (this.crops.containsKey(item.getRegistryName()))
            return this.crops.get(item.getRegistryName());
        for (ResourceLocation tag : item.getTags())
            if (this.crops.containsKey(tag))
                return this.crops.get(tag);
        return null;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, IResourceManager manager, IProfiler profiler) {
        ImmutableMap.Builder<ResourceLocation, CropProperties> builder = ImmutableMap.builder();
        data.forEach((fres, el) -> {
            try {
                JsonObject obj = el.getAsJsonObject();
                if (obj.has("tag")) {
                    ITag<Item> tag = TagCollectionManager.getManager().getItemTags().get(new ResourceLocation(obj.get("tag").getAsString()));
                    tag.getAllElements().forEach(item -> builder.put(item.getRegistryName(), GSON.fromJson(el, CropProperties.class)));
                } else if (obj.has("item")) {
                    ResourceLocation res = new ResourceLocation(obj.get("item").getAsString());
                    builder.put(res, GSON.fromJson(el, CropProperties.class));
                }
            } catch (JsonSyntaxException | IllegalStateException ex) {
                RuneCraftory.logger.error("Couldnt parse crop properties json {}", fres);
                ex.fillInStackTrace();
            }
        });
        this.crops = builder.build();
    }

    public void toPacket(PacketBuffer buffer) {
        buffer.writeInt(this.crops.size());
        this.crops.forEach((res, prop) -> {
            buffer.writeResourceLocation(res);
            prop.toPacket(buffer);
        });
    }

    public void fromPacket(PacketBuffer buffer) {
        ImmutableMap.Builder<ResourceLocation, CropProperties> builder = ImmutableMap.builder();
        int size = buffer.readInt();
        for (int i = 0; i < size; i++)
            builder.put(buffer.readResourceLocation(), CropProperties.fromPacket(buffer));
        this.crops = builder.build();
    }
}
