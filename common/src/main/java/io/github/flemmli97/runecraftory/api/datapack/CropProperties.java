package io.github.flemmli97.runecraftory.api.datapack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.enums.EnumSeason;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class CropProperties {

    public static final Codec<CropProperties> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(Codec.INT.fieldOf("growth").forGetter(d -> d.growth),
                    Codec.INT.fieldOf("maxDrops").forGetter(d -> d.maxDrops),
                    Codec.BOOL.fieldOf("regrowable").forGetter(d -> d.regrowable),

                    Registry.BLOCK.byNameCodec().optionalFieldOf("giantCrop").forGetter(d -> d.giantVersion == Blocks.AIR ? Optional.empty() : Optional.of(d.giantVersion)),
                    Codec.list(CodecUtils.stringEnumCodec(EnumSeason.class, EnumSeason.SPRING)).fieldOf("bestSeason").forGetter(d -> List.copyOf(d.bestSeason)),
                    Codec.list(CodecUtils.stringEnumCodec(EnumSeason.class, EnumSeason.SPRING)).fieldOf("badSeason").forGetter(d -> List.copyOf(d.badSeason))
            ).apply(instance, (growth, drops, regrowable, giant, best, bad) -> new CropProperties(growth, drops, regrowable, giant.orElse(Blocks.AIR), best, bad)));

    public static final CropProperties DEFAULT_PROP = new CropProperties();
    private final EnumSet<EnumSeason> bestSeason = EnumSet.noneOf(EnumSeason.class);
    private final EnumSet<EnumSeason> badSeason = EnumSet.noneOf(EnumSeason.class);

    private int growth = 3;
    private int maxDrops = 2;
    private boolean regrowable;

    private Block giantVersion = Blocks.AIR;
    private transient List<Component> translationTexts;
    private transient ResourceLocation id;

    private CropProperties() {
    }

    public CropProperties(int growth, int maxDrops, boolean regrowable, Block giantVersion, Collection<EnumSeason> bestSeason, Collection<EnumSeason> badSeason) {
        this.growth = growth;
        this.maxDrops = maxDrops;
        this.giantVersion = giantVersion;
        this.regrowable = regrowable;
        this.bestSeason.addAll(bestSeason);
        this.badSeason.addAll(badSeason);
    }

    public static CropProperties fromPacket(FriendlyByteBuf buffer) {
        CropProperties prop = new CropProperties();
        prop.id = buffer.readResourceLocation();
        prop.growth = buffer.readInt();
        prop.maxDrops = buffer.readInt();
        prop.regrowable = buffer.readBoolean();
        int size = buffer.readInt();
        for (int i = 0; i < size; i++)
            prop.bestSeason.add(buffer.readEnum(EnumSeason.class));
        size = buffer.readInt();
        for (int i = 0; i < size; i++)
            prop.badSeason.add(buffer.readEnum(EnumSeason.class));
        return prop;
    }

    public void setID(ResourceLocation id) {
        if (this.id == null)
            this.id = id;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public Set<EnumSeason> bestSeasons() {
        return this.bestSeason;
    }

    public Set<EnumSeason> badSeasons() {
        return this.badSeason;
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

    public Block getGiantVersion() {
        return this.giantVersion;
    }

    public float seasonMultiplier(EnumSeason season) {
        if (this.bestSeason.contains(season))
            return 1.5f;
        if (this.badSeason.contains(season))
            return 2 / 3f;
        return 1;
    }

    public void toPacket(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(this.id);
        buffer.writeInt(this.growth);
        buffer.writeInt(this.maxDrops);
        buffer.writeBoolean(this.regrowable);
        buffer.writeInt(this.bestSeason.size());
        this.bestSeason.forEach(buffer::writeEnum);
        buffer.writeInt(this.badSeason.size());
        this.badSeason.forEach(buffer::writeEnum);
    }

    public List<Component> texts() {
        if (this.translationTexts == null) {
            this.translationTexts = new ArrayList<>();
            if (!this.bestSeason.isEmpty()) {
                MutableComponent season = new TranslatableComponent("runecraftory.tooltip.season.best").append(": ");
                int i = 0;
                for (EnumSeason seas : this.bestSeasons()) {
                    season.append(i != 0 ? "/" : "").withStyle(ChatFormatting.GRAY)
                            .append(new TranslatableComponent(seas.translationKey()).withStyle(seas.getColor()));
                    i++;
                }
                this.translationTexts.add(season);
            }
            EnumSet<EnumSeason> badSeasons = EnumSet.copyOf(this.badSeason);
            badSeasons.removeAll(this.bestSeasons());
            if (!badSeasons.isEmpty()) {
                MutableComponent season = new TranslatableComponent("runecraftory.tooltip.season.bad").append(": ");
                int i = 0;
                for (EnumSeason seas : badSeasons) {
                    season.append(i != 0 ? "/" : "").withStyle(ChatFormatting.GRAY)
                            .append(new TranslatableComponent(seas.translationKey()).withStyle(seas.getColor()));
                    i++;
                }
                this.translationTexts.add(season);
            }
            MutableComponent growth = new TranslatableComponent("runecraftory.tooltip.growth", this.growth()).withStyle(ChatFormatting.GOLD);
            Component harvest = new TranslatableComponent("runecraftory.tooltip.harvested", this.maxDrops()).withStyle(ChatFormatting.GOLD);
            this.translationTexts.add(growth.append("  ").append(harvest));
        }
        return this.translationTexts;
    }

    @Override
    public String toString() {
        String s = "[BestSeasons:" + this.bestSeason + ";BadSeasons:" + this.badSeason + ";Growth:" + this.growth + ";Drops:" + this.maxDrops + ";Regrowable:" + this.regrowable + "]";
        if (this.id != null)
            s = this.id + ":" + s;
        return s;
    }

    /**
     * Used in serialization
     */
    public static class Builder {

        private final EnumSet<EnumSeason> bestSeason = EnumSet.noneOf(EnumSeason.class);
        private final EnumSet<EnumSeason> badSeason = EnumSet.noneOf(EnumSeason.class);

        private final int growth, maxDrops;
        private final boolean regrowable;

        private Block giantVersion = Blocks.AIR;

        public Builder(int growth, int maxDrops, boolean regrowable) {
            this.growth = growth;
            this.maxDrops = maxDrops;
            this.regrowable = regrowable;
        }

        public Builder addGoodSeason(EnumSeason season) {
            this.bestSeason.add(season);
            return this;
        }

        public Builder addBadSeason(EnumSeason season) {
            this.badSeason.add(season);
            return this;
        }

        public Builder withGiantVersion(Block giantVersion) {
            this.giantVersion = giantVersion;
            return this;
        }

        public CropProperties build() {
            return new CropProperties(this.growth, this.maxDrops, this.regrowable, this.giantVersion, this.bestSeason, this.badSeason);
        }
    }
}