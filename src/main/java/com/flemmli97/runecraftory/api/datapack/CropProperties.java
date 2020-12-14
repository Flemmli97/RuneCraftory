package com.flemmli97.runecraftory.api.datapack;

import com.flemmli97.runecraftory.api.enums.EnumSeason;

import java.util.EnumSet;
import java.util.Set;

public class CropProperties {
    private EnumSet<EnumSeason> bestSeason;
    private EnumSet<EnumSeason> badSeason;

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