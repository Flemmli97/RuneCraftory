package io.github.flemmli97.runecraftory.api.datapack.npc;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.datapack.ShopItemProperties;
import io.github.flemmli97.runecraftory.common.utils.CodecHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class GiftData {

    public static final Codec<GiftData> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(Codec.either(ShopItemProperties.TAG_CODEC, BuiltInRegistries.ITEM.byNameCodec())
                            .listOf().fieldOf("items").forGetter(d -> d.items),
                    Range.CODEC.optionalFieldOf("xp_range").forGetter(d -> Optional.ofNullable(d.xp_range)),
                    CodecHelper.nonEmptyList(Codec.STRING, "Translations can't be empty").fieldOf("translations").forGetter(d -> d.translations)
            ).apply(inst, GiftData::new));

    private final List<Either<TagKey<Item>, Item>> items;
    private final Range xp_range;
    private final List<String> translations;

    private GiftData(List<Either<TagKey<Item>, Item>> items, Optional<Range> xp_range, List<String> translations) {
        this(items, xp_range.orElse(null), translations);
    }

    private GiftData(List<Either<TagKey<Item>, Item>> items, Range xp_range, List<String> translations) {
        this.items = items;
        this.xp_range = xp_range;
        this.translations = translations;
    }

    public String translation(Random random) {
        return this.translations.get(random.nextInt(this.translations.size()));
    }

    public boolean is(ItemStack stack) {
        for (Either<TagKey<Item>, Item> item : this.items) {
            if (item.map(stack::is, stack::is))
                return true;
        }
        return false;
    }

    public boolean matches(int xp) {
        return this.xp_range != null &&
                (this.xp_range.min == null || this.xp_range.min <= xp) &&
                (this.xp_range.max == null || this.xp_range.max >= xp);
    }

    public static Builder builder(TagKey<Item> tag, String key, String translation) {
        return new Builder(tag, key, translation);
    }

    public static Builder builder(Item item, String key, String translation) {
        return new Builder(item, key, translation);
    }

    public static class Builder {
        private final List<Either<TagKey<Item>, Item>> items = new ArrayList<>();
        private Range range;
        public Map<String, String> translations = new LinkedHashMap<>();

        public Builder(TagKey<Item> tag, String key, String translation) {
            this.items.add(Either.left(tag));
            this.translations.put(key, translation);
        }

        public Builder(Item item, String key, String translation) {
            this.items.add(Either.right(item));
            this.translations.put(key, translation);
        }

        public Builder selectable() {
            this.range = new Range(null, null);
            return this;
        }

        public Builder min(int min) {
            this.range = new Range(min, null);
            return this;
        }

        public Builder max(int max) {
            this.range = new Range(null, max);
            return this;
        }

        public Builder range(int min, int max) {
            this.range = new Range(min, max);
            return this;
        }

        public Builder add(Item... items) {
            for (Item item : items)
                this.items.add(Either.right(item));
            return this;
        }

        public Builder add(TagKey<Item> tag) {
            this.items.add(Either.left(tag));
            return this;
        }

        public Builder addTranslation(String key, String translation) {
            this.translations.put(key, translation);
            return this;
        }

        public GiftData build() {
            return new GiftData(this.items, this.range, this.translations.keySet().stream().toList());
        }
    }

    private record Range(@Nullable Integer min, @Nullable Integer max) {
        public static final Codec<Range> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(Codec.INT.optionalFieldOf("min").forGetter(d -> Optional.ofNullable(d.min)),
                        Codec.INT.optionalFieldOf("max").forGetter(d -> Optional.ofNullable(d.max))
                ).apply(inst, (min, max) -> new Range(min.orElse(null), max.orElse(null))));
    }
}
