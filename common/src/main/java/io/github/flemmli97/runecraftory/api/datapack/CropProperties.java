package io.github.flemmli97.runecraftory.api.datapack;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.calendar.Season;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class CropProperties {

    public static final Codec<CropProperties> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(CropMappingInfo.CODEC.fieldOf("crop").forGetter(CropProperties::getInfo),
                    Codec.INT.fieldOf("growth").forGetter(CropProperties::growth),
                    Codec.INT.fieldOf("max_drops").forGetter(CropProperties::maxDrops),
                    Codec.BOOL.fieldOf("regrowable").forGetter(CropProperties::regrowable),
                    CodecUtils.stringEnumCodec(Season.class, Season.SPRING).listOf().fieldOf("best_season").forGetter(d -> List.copyOf(d.bestSeasons)),
                    CodecUtils.stringEnumCodec(Season.class, Season.SPRING).listOf().fieldOf("bad_season").forGetter(d -> List.copyOf(d.badSeasons))
            ).apply(instance, CropProperties::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, CropProperties> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public CropProperties decode(RegistryFriendlyByteBuf buf) {
            return new CropProperties(CropMappingInfo.EMPTY, buf.readInt(), buf.readInt(), buf.readBoolean(),
                    buf.readEnumSet(Season.class), buf.readEnumSet(Season.class));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, CropProperties props) {
            buf.writeInt(props.growth());
            buf.writeInt(props.maxDrops());
            buf.writeBoolean(props.regrowable());
            buf.writeEnumSet(props.bestSeasons, Season.class);
            buf.writeEnumSet(props.badSeasons, Season.class);
        }
    };

    private final CropMappingInfo info;
    private final EnumSet<Season> bestSeasons;
    private final EnumSet<Season> badSeasons;

    private final int growth;
    private final int maxDrops;
    private final boolean regrowable;

    private final List<Component> translationTexts;

    public CropProperties(CropMappingInfo info, int growth, int maxDrops, boolean regrowable, Collection<Season> badSeasons, Collection<Season> bestSeasons) {
        this.info = info;
        this.growth = growth;
        this.maxDrops = maxDrops;
        this.regrowable = regrowable;
        this.bestSeasons = EnumSet.copyOf(bestSeasons);
        this.badSeasons = EnumSet.copyOf(badSeasons);
        this.translationTexts = this.generateText();
    }

    private List<Component> generateText() {
        ImmutableList.Builder<Component> list = ImmutableList.builder();
        if (!this.bestSeasons.isEmpty()) {
            MutableComponent txt = null;
            for (Season season : this.bestSeasons) {
                if (txt == null) {
                    txt = Component.translatable(season.translationKey()).withStyle(season.getColor());
                } else {
                    txt.append(Component.literal("/").withStyle(ChatFormatting.GRAY))
                            .append(Component.translatable(season.translationKey()).withStyle(season.getColor()));
                }
            }
            list.add(Component.translatable("runecraftory.tooltip.crops.season.best", txt).withStyle(ChatFormatting.GRAY));
        }
        EnumSet<Season> badSeasons = EnumSet.copyOf(this.badSeasons);
        badSeasons.removeAll(this.bestSeasons);
        if (!badSeasons.isEmpty()) {
            MutableComponent txt = null;
            for (Season season : badSeasons) {
                if (txt == null) {
                    txt = Component.translatable(season.translationKey()).withStyle(season.getColor());
                } else {
                    txt.append(Component.literal("/").withStyle(ChatFormatting.GRAY))
                            .append(Component.translatable(season.translationKey()).withStyle(season.getColor()));
                }
            }
            list.add(Component.translatable("runecraftory.tooltip.crops.season.bad", txt).withStyle(ChatFormatting.GRAY));
        }
        MutableComponent growth = Component.translatable("runecraftory.tooltip.crops.growth", this.growth()).withStyle(ChatFormatting.GOLD);
        Component harvest = Component.translatable("runecraftory.tooltip.crops.harvested", this.maxDrops()).withStyle(ChatFormatting.GOLD);
        if (this.regrowable()) {
            Component regrowable = Component.translatable("runecraftory.tooltip.crops.regrowable").withStyle(ChatFormatting.GREEN);
            list.add(Component.translatable("runecraftory.tooltip.crops.entry.3", growth, harvest, regrowable));
        } else {
            list.add(Component.translatable("runecraftory.tooltip.crops.entry.2", growth, harvest));
        }
        return list.build();
    }

    public CropMappingInfo getInfo() {
        return this.info;
    }

    public int growth() {
        return this.growth;
    }

    public int maxDrops() {
        return this.maxDrops;
    }

    public boolean regrowable() {
        return this.regrowable;
    }

    public Optional<Block> getGiantVersion() {
        return this.info.giant.map(Pair::getSecond);
    }

    public float seasonMultiplier(Season season) {
        if (this.bestSeasons.contains(season))
            return 1.5f;
        if (this.badSeasons.contains(season))
            return 2 / 3f;
        return 1;
    }

    public List<Component> texts() {
        return this.translationTexts;
    }

    @Override
    public String toString() {
        return "[BestSeasons:" + this.bestSeasons + ";BadSeasons:" + this.badSeasons + ";Growth:" + this.growth + ";Drops:" + this.maxDrops + ";Regrowable:" + this.regrowable + "]";
    }

    /**
     * Used in serialization
     */
    public static class Builder {

        private final EnumSet<Season> bestSeason = EnumSet.noneOf(Season.class);
        private final EnumSet<Season> badSeason = EnumSet.noneOf(Season.class);

        private final CropMappingInfo info;
        private final int growth, maxDrops;
        private final boolean regrowable;

        public Builder(Holder<Item> seed, Holder<Item> crop, Holder<Block> cropBlock, int growth, int maxDrops, boolean regrowable) {
            this(HolderSet.direct(seed),
                    HolderSet.direct(crop),
                    HolderSet.direct(cropBlock),
                    growth, maxDrops, regrowable);
        }

        public Builder(Holder<Item> seed, Holder<Item> crop, Holder<Block> cropBlock, Pair<Item, Block> giant, int growth, int maxDrops, boolean regrowable) {
            this(HolderSet.direct(seed),
                    HolderSet.direct(crop),
                    HolderSet.direct(cropBlock),
                    giant,
                    growth, maxDrops, regrowable);
        }

        public Builder(HolderSet<Item> seed, HolderSet<Item> crop, HolderSet<Block> cropBlock, int growth, int maxDrops, boolean regrowable) {
            this.info = new CropMappingInfo(seed, crop, cropBlock);
            this.growth = growth;
            this.maxDrops = maxDrops;
            this.regrowable = regrowable;
        }

        public Builder(HolderSet<Item> seed, HolderSet<Item> crop, HolderSet<Block> cropBlock, Pair<Item, Block> giant, int growth, int maxDrops, boolean regrowable) {
            this.info = new CropMappingInfo(seed, crop, cropBlock, Optional.of(giant));
            this.growth = growth;
            this.maxDrops = maxDrops;
            this.regrowable = regrowable;
        }

        public Builder addGoodSeason(Season season) {
            this.bestSeason.add(season);
            return this;
        }

        public Builder addBadSeason(Season season) {
            this.badSeason.add(season);
            return this;
        }

        public ResourceLocation tryGetId() {
            return this.info.seed.unwrap()
                    .map(TagKey::location,
                            l -> {
                                if (l.isEmpty())
                                    throw new IllegalStateException("Empty holderset. This is not allowed");
                                return l.getFirst().unwrapKey().orElseThrow().location();
                            });
        }

        public CropProperties build() {
            return new CropProperties(this.info, this.growth, this.maxDrops, this.regrowable, this.bestSeason, this.badSeason);
        }
    }

    public record CropMappingInfo(HolderSet<Item> seed, HolderSet<Item> crop, HolderSet<Block> cropBlock,
                                  Optional<Pair<Item, Block>> giant) {

        public static final Codec<CropMappingInfo> CODEC = RecordCodecBuilder.create((instance) ->
                instance.group(RegistryCodecs.homogeneousList(Registries.ITEM).fieldOf("seed").forGetter(CropMappingInfo::seed),
                        RegistryCodecs.homogeneousList(Registries.ITEM).fieldOf("crop").forGetter(CropMappingInfo::crop),
                        RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("crop_block").forGetter(CropMappingInfo::cropBlock),
                        Codec.mapPair(BuiltInRegistries.ITEM.byNameCodec().fieldOf("item"),
                                        BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block"))
                                .codec().optionalFieldOf("giant_crop").forGetter(CropMappingInfo::giant)
                ).apply(instance, CropMappingInfo::new));
        public static final CropMappingInfo EMPTY = new CropMappingInfo(HolderSet.empty(), HolderSet.empty(), HolderSet.empty(), Optional.empty());

        public CropMappingInfo(HolderSet<Item> seed, HolderSet<Item> crop, HolderSet<Block> cropBlock) {
            this(seed, crop, cropBlock, Optional.empty());
        }
    }
}