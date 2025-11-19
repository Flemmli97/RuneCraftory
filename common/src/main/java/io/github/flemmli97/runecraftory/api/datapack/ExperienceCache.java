package io.github.flemmli97.runecraftory.api.datapack;

import io.github.flemmli97.tenshilib.common.utils.math.parser.VariableMap;

import java.util.function.IntSupplier;

public class ExperienceCache {

    private final IntSupplier maxLevel;
    private final boolean mutable;
    private ExpressionHolder experiencePerLevel;

    private long[] totalExperienceLevel;
    private int[] experienceTillNextLevel;

    public ExperienceCache(IntSupplier maxLevel, ExpressionHolder experiencePerLevel) {
        this(maxLevel, experiencePerLevel, false);
    }

    public ExperienceCache(IntSupplier maxLevel, ExpressionHolder experiencePerLevel, boolean mutable) {
        this.maxLevel = maxLevel;
        this.experiencePerLevel = experiencePerLevel;
        this.mutable = false;
    }

    public void updateExpression(String exp) {
        if (!this.mutable)
            return;
        this.experiencePerLevel = new ExpressionHolder(exp);
    }

    public ExpressionHolder experiencePerLevel() {
        return this.experiencePerLevel;
    }

    public int xpAmountForNext(int level) {
        if (level <= 0)
            return 1;
        if (level >= this.maxLevel.getAsInt())
            return 0;
        this.calculateExperienceValues(level);
        return this.experienceTillNextLevel[level - 1];
    }

    public long totalXpAmount(int level) {
        if (level <= 0)
            return 0;
        this.calculateExperienceValues(level);
        if (level >= this.maxLevel.getAsInt())
            return this.totalExperienceLevel[this.maxLevel.getAsInt()];
        return this.totalExperienceLevel[level - 1];
    }

    private void calculateExperienceValues(int level) {
        if (this.totalExperienceLevel != null && (this.totalExperienceLevel.length >= level || level > this.maxLevel.getAsInt())) {
            return;
        }
        int len = Math.min(this.maxLevel.getAsInt(), level + 50);
        long[] total = new long[len];
        int[] xp = new int[len];
        VariableMap map = new VariableMap();
        long cumulative = 0;
        for (int i = 1; i < len; i++) {
            map.setVariable("level", i + 1);
            xp[i] = (int) Math.round(this.experiencePerLevel.value().get(map));
            cumulative += xp[i];
            total[i] = cumulative;
        }
        this.totalExperienceLevel = total;
        this.experienceTillNextLevel = xp;
    }
}
