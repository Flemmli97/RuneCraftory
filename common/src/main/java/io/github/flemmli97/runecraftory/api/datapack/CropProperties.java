package io.github.flemmli97.runecraftory.api.datapack;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.calendar.Season;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class CropProperties {

    public static final Codec<CropProperties> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(Codec.INT.fieldOf("growth").forGetter(CropProperties::growth),
                    Codec.INT.fieldOf("max_drops").forGetter(CropProperties::maxDrops),
                    Codec.BOOL.fieldOf("regrowable").forGetter(CropProperties::regrowable),

                    BuiltInRegistries.BLOCK.byNameCodec().optionalFieldOf("giant_crop").forGetter(CropProperties::getGiantVersion),
                    CodecUtils.stringEnumCodec(Season.class, Season.SPRING).listOf().fieldOf("best_season").forGetter(d -> List.copyOf(d.bestSeasons)),
                    CodecUtils.stringEnumCodec(Season.class, Season.SPRING).listOf().fieldOf("bad_season").forGetter(d -> List.copyOf(d.badSeasons))
            ).apply(instance, CropProperties::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, CropProperties> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public CropProperties decode(RegistryFriendlyByteBuf buf) {
            return new CropProperties(buf.readInt(), buf.readInt(), buf.readBoolean(),
                    ByteBufCodecs.optional(ByteBufCodecs.registry(Registries.BLOCK)).decode(buf),
                    buf.readEnumSet(Season.class), buf.readEnumSet(Season.class))
                    .setID(buf.readResourceLocation());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, CropProperties props) {
            buf.writeInt(props.growth());
            buf.writeInt(props.maxDrops());
            buf.writeBoolean(props.regrowable());
            ByteBufCodecs.optional(ByteBufCodecs.registry(Registries.BLOCK)).encode(buf, props.getGiantVersion());
            buf.writeEnumSet(props.bestSeasons, Season.class);
            buf.writeEnumSet(props.badSeasons, Season.class);
            buf.writeResourceLocation(props.id);
        }
    };

    private final EnumSet<Season> bestSeasons = EnumSet.noneOf(Season.class);
    private final EnumSet<Season> badSeasons = EnumSet.noneOf(Season.class);

    private final int growth;
    private final int maxDrops;
    private final boolean regrowable;

    private final Optional<Block> giantVersion;

    private ResourceLocation id;
    private List<Component> translationTexts;

    public CropProperties(int growth, int maxDrops, boolean regrowable, Optional<Block> giantVersion, Collection<Season> bestSeasons, Collection<Season> badSeasons) {
        this.growth = growth;
        this.maxDrops = maxDrops;
        this.giantVersion = giantVersion;
        this.regrowable = regrowable;
        this.bestSeasons.addAll(bestSeasons);
        this.badSeasons.addAll(badSeasons);
    }

    public CropProperties setID(ResourceLocation id) {
        if (this.id == null)
            this.id = id;
        return this;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public Set<Season> bestSeasons() {
        return ImmutableSet.copyOf(this.bestSeasons);
    }

    public Set<Season> badSeasons() {
        return ImmutableSet.copyOf(this.badSeasons);
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
        return this.giantVersion;
    }

    public float seasonMultiplier(Season season) {
        if (this.bestSeasons.contains(season))
            return 1.5f;
        if (this.badSeasons.contains(season))
            return 2 / 3f;
        return 1;
    }

    public List<Component> texts() {
        if (this.translationTexts == null) {
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
            this.translationTexts = list.build();
        }
        return this.translationTexts;
    }

    @Override
    public String toString() {
        String s = "[BestSeasons:" + this.bestSeasons + ";BadSeasons:" + this.badSeasons + ";Growth:" + this.growth + ";Drops:" + this.maxDrops + ";Regrowable:" + this.regrowable + "]";
        if (this.id != null)
            s = this.id + ":" + s;
        return s;
    }

    /**
     * Used in serialization
     */
    public static class Builder {

        private final EnumSet<Season> bestSeason = EnumSet.noneOf(Season.class);
        private final EnumSet<Season> badSeason = EnumSet.noneOf(Season.class);

        private final int growth, maxDrops;
        private final boolean regrowable;

        private Block giantVersion;

        public Builder(int growth, int maxDrops, boolean regrowable) {
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

        public Builder withGiantVersion(Block giantVersion) {
            this.giantVersion = giantVersion;
            return this;
        }

        public CropProperties build() {
            return new CropProperties(this.growth, this.maxDrops, this.regrowable, Optional.ofNullable(this.giantVersion), this.bestSeason, this.badSeason);
        }
    }
}