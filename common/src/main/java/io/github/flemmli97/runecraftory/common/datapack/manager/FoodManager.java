package io.github.flemmli97.runecraftory.common.datapack.manager;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.FoodProperties;
import io.github.flemmli97.runecraftory.api.datapack.GsonInstances;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;
import java.util.Map;

public class FoodManager extends SimpleJsonResourceReloadListener {

    public static final String DIRECTORY = "food_stats";

    private Map<ResourceLocation, FoodProperties> food = ImmutableMap.of();
    private Map<TagKey<Item>, FoodProperties> foodTag = ImmutableMap.of();

    public FoodManager() {
        super(GsonInstances.ATTRIBUTE_EFFECTS, DIRECTORY);
    }

    @Nullable
    public FoodProperties get(Item item) {
        if (GeneralConfig.disableFoodSystem)
            return null;
        ResourceLocation res = PlatformUtils.INSTANCE.items().getIDFrom(item);
        FoodProperties prop = this.food.get(res);
        if (prop != null) {
            return prop;
        }
        if (!this.food.isEmpty()) {
            return item.builtInRegistryHolder().tags()
                    .filter(tag -> this.food.containsKey(tag.location()))
                    .findFirst().map(tag -> this.food.get(tag.location()))
                    .orElse(null);
        }
        return null;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, ResourceManager manager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, FoodProperties> builder = ImmutableMap.builder();
        ImmutableMap.Builder<TagKey<Item>, FoodProperties> tagBuilder = ImmutableMap.builder();
        data.forEach((fres, el) -> {
            try {
                JsonObject obj = el.getAsJsonObject();
                if (obj.has("tag")) {
                    TagKey<Item> tag = PlatformUtils.INSTANCE.itemTag(new ResourceLocation(obj.get("tag").getAsString()));
                    FoodProperties props = FoodProperties.CODEC.parse(JsonOps.INSTANCE, el)
                            .getOrThrow(false, RuneCraftory.logger::error);
                    props.setID(fres);
                    tagBuilder.put(tag, props);
                } else if (obj.has("item")) {
                    ResourceLocation res = new ResourceLocation(obj.get("item").getAsString());
                    FoodProperties props = FoodProperties.CODEC.parse(JsonOps.INSTANCE, el)
                            .getOrThrow(false, RuneCraftory.logger::error);
                    props.setID(fres);
                    builder.put(res, props);
                }
            } catch (Exception ex) {
                RuneCraftory.logger.error("Couldnt parse food stat json {}", fres);
                ex.fillInStackTrace();
            }
        });
        this.food = builder.build();
        this.foodTag = tagBuilder.build();
    }

    public void toPacket(FriendlyByteBuf buffer) {
        buffer.writeInt(this.food.size());
        this.food.forEach((res, prop) -> {
            buffer.writeResourceLocation(res);
            prop.toPacket(buffer);
        });
        buffer.writeInt(this.foodTag.size());
        this.foodTag.forEach((tag, stat) -> {
            buffer.writeResourceLocation(tag.location());
            stat.toPacket(buffer);
        });
    }

    public void fromPacket(FriendlyByteBuf buffer) {
        ImmutableMap.Builder<ResourceLocation, FoodProperties> builder = ImmutableMap.builder();
        int size = buffer.readInt();
        for (int i = 0; i < size; i++)
            builder.put(buffer.readResourceLocation(), FoodProperties.fromPacket(buffer));
        this.food = builder.build();
        ImmutableMap.Builder<TagKey<Item>, FoodProperties> tagBuilder = ImmutableMap.builder();
        int tagSize = buffer.readInt();
        for (int i = 0; i < tagSize; i++)
            tagBuilder.put(PlatformUtils.INSTANCE.itemTag(buffer.readResourceLocation()), FoodProperties.fromPacket(buffer));
        this.foodTag = tagBuilder.build();
    }
}
