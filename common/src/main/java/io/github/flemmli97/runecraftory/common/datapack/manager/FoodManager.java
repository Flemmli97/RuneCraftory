package io.github.flemmli97.runecraftory.common.datapack.manager;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.FoodProperties;
import io.github.flemmli97.runecraftory.api.datapack.RegistryObjectSerializer;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;

import java.util.Map;

public class FoodManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().enableComplexMapKeySerialization()
            .registerTypeAdapter(Attribute.class, new RegistryObjectSerializer<>(PlatformUtils.INSTANCE.attributes()))
            .registerTypeAdapter(MobEffect.class, new RegistryObjectSerializer<>(PlatformUtils.INSTANCE.effects())).create();

    private Map<ResourceLocation, FoodProperties> food = ImmutableMap.of();
    private Map<TagKey<Item>, FoodProperties> foodTag = ImmutableMap.of();

    public FoodManager() {
        super(GSON, "food_stats");
    }

    public FoodProperties get(Item item) {
        if (GeneralConfig.disableDatapack)
            return null;
        ResourceLocation res = PlatformUtils.INSTANCE.items().getIDFrom(item);
        FoodProperties prop = this.food.get(res);
        if (prop != null) {
            return prop;
        }
        if (!this.food.isEmpty()) {
            return item.builtInRegistryHolder().tags()
                    .filter(this.food::containsKey)
                    .findFirst().map(this.food::get)
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
                    tagBuilder.put(tag, GSON.fromJson(el, FoodProperties.class));
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
