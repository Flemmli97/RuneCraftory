package io.github.flemmli97.runecraftory.api.datapack.npc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.utils.CodecHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public record GiftData(TagKey<Item> tag, boolean selectable, List<String> translations) {

    public static final Codec<GiftData> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(ResourceLocation.CODEC.xmap(res -> TagKey.create(Registry.ITEM_REGISTRY, res), TagKey::location).fieldOf("tag").forGetter(d -> d.tag),
                    Codec.BOOL.fieldOf("selectable").forGetter(d -> d.selectable),
                    CodecHelper.nonEmptyList(Codec.STRING, "Translations can't be empty").fieldOf("translations").forGetter(d -> d.translations)
            ).apply(inst, GiftData::new));

    public String translation(Random random) {
        return this.translations().get(random.nextInt(this.translations().size()));
    }

    public static Builder builder(TagKey<Item> tag, String key, String translation) {
        return new Builder(tag, key, translation);
    }

    public static class Builder {
        private final TagKey<Item> tag;
        private boolean selectable = true;
        public Map<String, String> translations = new LinkedHashMap<>();

        public Builder(TagKey<Item> tag, String key, String translation) {
            this.tag = tag;
            this.translations.put(key, translation);
        }

        public Builder noSelect() {
            this.selectable = false;
            return this;
        }

        public Builder addTranslation(String key, String translation) {
            this.translations.put(key, translation);
            return this;
        }

        public GiftData build() {
            return new GiftData(this.tag, this.selectable, this.translations.keySet().stream().toList());
        }
    }
}
