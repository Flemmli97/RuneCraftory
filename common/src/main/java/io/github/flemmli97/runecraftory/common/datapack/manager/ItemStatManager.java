package io.github.flemmli97.runecraftory.common.datapack.manager;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.datapack.ItemStat;
import io.github.flemmli97.runecraftory.api.datapack.RegistryObjectSerializer;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ItemStatManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().enableComplexMapKeySerialization()
            .registerTypeAdapter(Attribute.class, new RegistryObjectSerializer<>(PlatformUtils.INSTANCE.attributes()))
            .registerTypeAdapter(Spell.class, new RegistryObjectSerializer<>(ModSpells.SPELLREGISTRY.get())).create();

    private Map<ResourceLocation, ItemStat> itemstat = ImmutableMap.of();
    private Map<TagKey<Item>, ItemStat> itemstatTag = ImmutableMap.of();

    public ItemStatManager() {
        super(GSON, "item_stats");
    }

    public ItemStat get(Item item) {
        if (GeneralConfig.disableItemStatSystem)
            return null;
        ResourceLocation res = PlatformUtils.INSTANCE.items().getIDFrom(item);
        ItemStat stat = this.itemstat.get(res);
        if (stat != null) {
            return stat;
        }
        if (!this.itemstatTag.isEmpty()) {
            return item.builtInRegistryHolder().tags()
                    .filter(this.itemstatTag::containsKey)
                    .findFirst().map(this.itemstatTag::get)
                    .orElse(null);
        }
        return null;
    }

    public Collection<Pair<ItemStack, ItemStat>> all() {
        List<Pair<ItemStack, ItemStat>> list = this.itemstat.entrySet().stream().map(e -> {
            Item item = PlatformUtils.INSTANCE.items().getFromId(e.getKey());
            if (item != null && item != Items.AIR)
                return Pair.of(new ItemStack(item), e.getValue());
            return null;
        }).filter(Objects::isNull).toList();
        this.itemstatTag.entrySet().stream()
                .forEach(e -> Registry.ITEM.getTag(e.getKey()).ifPresent(n -> n.forEach(h -> list.add(Pair.of(new ItemStack(h.value()), e.getValue())))));
        return list;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, ResourceManager manager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, ItemStat> builder = ImmutableMap.builder();
        ImmutableMap.Builder<TagKey<Item>, ItemStat> tagBuilder = ImmutableMap.builder();
        data.forEach((fres, el) -> {
            try {
                JsonObject obj = el.getAsJsonObject();
                if (obj.has("tag")) {
                    TagKey<Item> tag = PlatformUtils.INSTANCE.itemTag(new ResourceLocation(obj.get("tag").getAsString()));
                    ItemStat stat = GSON.fromJson(el, ItemStat.class);
                    stat.setID(fres);
                    tagBuilder.put(tag, stat);
                } else if (obj.has("item")) {
                    ResourceLocation res = new ResourceLocation(obj.get("item").getAsString());
                    ItemStat stat = GSON.fromJson(el, ItemStat.class);
                    stat.setID(fres);
                    builder.put(res, stat);
                }
            } catch (JsonSyntaxException ex) {
                RuneCraftory.logger.error("Couldnt parse item stat json {}", fres);
                ex.fillInStackTrace();
            }
        });
        this.itemstat = builder.build();
        this.itemstatTag = tagBuilder.build();
        ItemCraftingLevelManager.reset();
    }

    public void toPacket(FriendlyByteBuf buffer) {
        buffer.writeInt(this.itemstat.size());
        this.itemstat.forEach((res, stat) -> {
            buffer.writeResourceLocation(res);
            stat.toPacket(buffer);
        });
        buffer.writeInt(this.itemstatTag.size());
        this.itemstatTag.forEach((tag, stat) -> {
            buffer.writeResourceLocation(tag.location());
            stat.toPacket(buffer);
        });
    }

    public void fromPacket(FriendlyByteBuf buffer) {
        ImmutableMap.Builder<ResourceLocation, ItemStat> builder = ImmutableMap.builder();
        int size = buffer.readInt();
        for (int i = 0; i < size; i++)
            builder.put(buffer.readResourceLocation(), ItemStat.fromPacket(buffer));
        this.itemstat = builder.build();
        ImmutableMap.Builder<TagKey<Item>, ItemStat> tagBuilder = ImmutableMap.builder();
        int tagSize = buffer.readInt();
        for (int i = 0; i < tagSize; i++)
            tagBuilder.put(PlatformUtils.INSTANCE.itemTag(buffer.readResourceLocation()), ItemStat.fromPacket(buffer));
        this.itemstatTag = tagBuilder.build();
    }
}
