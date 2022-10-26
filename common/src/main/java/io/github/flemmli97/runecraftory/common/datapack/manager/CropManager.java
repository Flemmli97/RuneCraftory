package io.github.flemmli97.runecraftory.common.datapack.manager;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;

import java.util.Map;

public class CropManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().create();

    private Map<ResourceLocation, CropProperties> crops = ImmutableMap.of();
    private Map<TagKey<Item>, CropProperties> cropTag = ImmutableMap.of();

    public CropManager() {
        super(GSON, "crop_properties");
    }

    public CropProperties get(Item item) {
        if (GeneralConfig.disableCropSystem)
            return null;
        ResourceLocation res = PlatformUtils.INSTANCE.items().getIDFrom(item);
        CropProperties props = this.crops.get(res);
        if (props != null) {
            return props;
        }
        if (!this.cropTag.isEmpty()) {
            return item.builtInRegistryHolder().tags()
                    .filter(this.cropTag::containsKey)
                    .findFirst().map(this.cropTag::get)
                    .orElse(null);
        }
        return null;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, ResourceManager manager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, CropProperties> builder = ImmutableMap.builder();
        ImmutableMap.Builder<TagKey<Item>, CropProperties> tagBuilder = ImmutableMap.builder();
        data.forEach((fres, el) -> {
            try {
                JsonObject obj = el.getAsJsonObject();
                if (obj.has("tag")) {
                    TagKey<Item> tag = PlatformUtils.INSTANCE.itemTag(new ResourceLocation(obj.get("tag").getAsString()));
                    CropProperties props = CropProperties.CODEC.parse(JsonOps.INSTANCE, el)
                            .getOrThrow(false, RuneCraftory.logger::error);
                    props.setID(fres);
                    tagBuilder.put(tag, props);
                } else if (obj.has("item")) {
                    ResourceLocation res = new ResourceLocation(obj.get("item").getAsString());
                    CropProperties props = CropProperties.CODEC.parse(JsonOps.INSTANCE, el)
                            .getOrThrow(false, RuneCraftory.logger::error);
                    props.setID(fres);
                    builder.put(res, props);
                }
            } catch (Exception ex) {
                RuneCraftory.logger.error("Couldnt parse crop properties json {}", fres);
                ex.fillInStackTrace();
            }
        });
        this.crops = builder.build();
        this.cropTag = tagBuilder.build();
    }

    public void toPacket(FriendlyByteBuf buffer) {
        buffer.writeInt(this.crops.size());
        this.crops.forEach((res, prop) -> {
            buffer.writeResourceLocation(res);
            prop.toPacket(buffer);
        });
        buffer.writeInt(this.cropTag.size());
        this.cropTag.forEach((tag, stat) -> {
            buffer.writeResourceLocation(tag.location());
            stat.toPacket(buffer);
        });
    }

    public void fromPacket(FriendlyByteBuf buffer) {
        ImmutableMap.Builder<ResourceLocation, CropProperties> builder = ImmutableMap.builder();
        int size = buffer.readInt();
        for (int i = 0; i < size; i++)
            builder.put(buffer.readResourceLocation(), CropProperties.fromPacket(buffer));
        this.crops = builder.build();
        ImmutableMap.Builder<TagKey<Item>, CropProperties> tagBuilder = ImmutableMap.builder();
        int tagSize = buffer.readInt();
        for (int i = 0; i < tagSize; i++)
            tagBuilder.put(PlatformUtils.INSTANCE.itemTag(buffer.readResourceLocation()), CropProperties.fromPacket(buffer));
        this.cropTag = tagBuilder.build();
    }
}
