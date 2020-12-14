package com.flemmli97.runecraftory.api.datapack;

import com.flemmli97.runecraftory.api.enums.EnumSeason;
import net.minecraft.network.PacketBuffer;

import java.util.EnumSet;
import java.util.Set;

public class CropProperties {

    private EnumSet<EnumSeason> bestSeason = EnumSet.noneOf(EnumSeason.class);
    private EnumSet<EnumSeason> badSeason = EnumSet.noneOf(EnumSeason.class);

    private int growth;
    private int maxDrops;
    private boolean regrowable;

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

    public void toPacket(PacketBuffer buffer) {
        buffer.writeInt(this.growth);
        buffer.writeInt(this.maxDrops);
        buffer.writeBoolean(this.regrowable);
        buffer.writeInt(this.bestSeason.size());
        this.bestSeason.forEach(season -> buffer.writeEnumValue(season));
        buffer.writeInt(this.badSeason.size());
        this.badSeason.forEach(season -> buffer.writeEnumValue(season));
    }

    public static CropProperties fromPacket(PacketBuffer buffer) {
        CropProperties prop = new CropProperties();
        prop.growth = buffer.readInt();
        prop.maxDrops = buffer.readInt();
        prop.regrowable = buffer.readBoolean();
        int size = buffer.readInt();
        for (int i = 0; i < size; i++)
            prop.bestSeason.add(buffer.readEnumValue(EnumSeason.class));
        size = buffer.readInt();
        for (int i = 0; i < size; i++)
            prop.badSeason.add(buffer.readEnumValue(EnumSeason.class));
        return prop;
    }

    @Override
    public String toString() {
        return "[BestSeasons:" + this.bestSeason + ";BadSeasons:" + this.badSeason + ";Growth:" + this.growth + ";Drops:" + this.maxDrops + ";Regrowable:" + this.regrowable + "]";
    }

    public static class MutableCropProps {

        private EnumSet<EnumSeason> bestSeason = EnumSet.noneOf(EnumSeason.class);
        private EnumSet<EnumSeason> badSeason = EnumSet.noneOf(EnumSeason.class);

        private int growth;
        private int maxDrops;
        private boolean regrowable;

        public MutableCropProps(int growth, int maxDrops, boolean regrowable) {
            this.growth = growth;
            this.maxDrops = maxDrops;
            this.regrowable = regrowable;
        }

        public MutableCropProps addGoodSeason(EnumSeason season) {
            bestSeason.add(season);
            return this;
        }

        public MutableCropProps addBadSeason(EnumSeason season) {
            badSeason.add(season);
            return this;
        }
    }
}