package io.github.flemmli97.runecraftory.api.datapack;

import io.github.flemmli97.runecraftory.api.enums.EnumSeason;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class CropProperties {

    public static final CropProperties defaultProp = new CropProperties();
    private final EnumSet<EnumSeason> bestSeason = EnumSet.noneOf(EnumSeason.class);
    private final EnumSet<EnumSeason> badSeason = EnumSet.noneOf(EnumSeason.class);

    private int growth = 3;
    private int maxDrops = 2;
    private boolean regrowable;

    private transient List<Component> translationTexts;

    public static CropProperties fromPacket(FriendlyByteBuf buffer) {
        CropProperties prop = new CropProperties();
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

    public float seasonMultiplier(EnumSeason season) {
        if (this.bestSeason.contains(season))
            return 1.5f;
        if (this.badSeason.contains(season))
            return 2 / 3f;
        return 1;
    }

    public void toPacket(FriendlyByteBuf buffer) {
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
                MutableComponent season = new TranslatableComponent("tooltip.season.best").append(": ");
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
                MutableComponent season = new TranslatableComponent("tooltip.season.bad").append(": ");
                int i = 0;
                for (EnumSeason seas : badSeasons) {
                    season.append(i != 0 ? "/" : "").withStyle(ChatFormatting.GRAY)
                            .append(new TranslatableComponent(seas.translationKey()).withStyle(seas.getColor()));
                    i++;
                }
                this.translationTexts.add(season);
            }
            MutableComponent growth = new TranslatableComponent("tooltip.growth", this.growth());
            Component harvest = new TranslatableComponent("tooltip.harvested", this.maxDrops());
            this.translationTexts.add(growth.append("  ").append(harvest));
        }
        return this.translationTexts;
    }

    @Override
    public String toString() {
        return "[BestSeasons:" + this.bestSeason + ";BadSeasons:" + this.badSeason + ";Growth:" + this.growth + ";Drops:" + this.maxDrops + ";Regrowable:" + this.regrowable + "]";
    }

    /**
     * Used in serialization
     */
    public static class MutableCropProps {

        private final EnumSet<EnumSeason> bestSeason = EnumSet.noneOf(EnumSeason.class);
        private final EnumSet<EnumSeason> badSeason = EnumSet.noneOf(EnumSeason.class);

        private int growth;
        private int maxDrops;
        private boolean regrowable;

        public MutableCropProps(int growth, int maxDrops, boolean regrowable) {
            this.growth = growth;
            this.maxDrops = maxDrops;
            this.regrowable = regrowable;
        }

        public MutableCropProps addGoodSeason(EnumSeason season) {
            this.bestSeason.add(season);
            return this;
        }

        public MutableCropProps addBadSeason(EnumSeason season) {
            this.badSeason.add(season);
            return this;
        }
    }
}