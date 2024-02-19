package io.github.flemmli97.runecraftory.common.attachment.player;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.Tag;

import java.util.function.IntUnaryOperator;

public class LevelExpPair {

    private int level = 1;
    private float xp;

    public void setLevel(int level) {
        this.level = level;
    }

    public void setXp(float xp) {
        this.xp = xp;
    }

    public int getLevel() {
        return this.level;
    }

    public float getXp() {
        return this.xp;
    }

    public boolean addXP(float amount, int maxLevel, IntUnaryOperator xpForNext, Runnable onLevelUp) {
        int neededXP = xpForNext.applyAsInt(this.level);
        if (neededXP <= 0)
            return false;
        float xpToNextLevel = neededXP - this.xp;
        if (amount >= xpToNextLevel) {
            float diff = amount - xpToNextLevel;
            this.level += 1;
            this.xp = 0;
            onLevelUp.run();
            this.addXP(diff, maxLevel, xpForNext, onLevelUp);
            return true;
        }
        if (this.level >= maxLevel)
            amount = 0;
        this.xp += amount;
        return false;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Level", this.level);
        tag.putFloat("XP", this.xp);
        return tag;
    }

    public void read(Tag tag) {
        if (tag instanceof IntArrayTag arrayTag) {
            this.level = arrayTag.getAsIntArray()[0];
            this.xp = arrayTag.getAsIntArray()[1];
        } else if (tag instanceof CompoundTag compoundTag) {
            this.level = compoundTag.getInt("Level");
            this.xp = compoundTag.getFloat("XP");
        }
    }

    @Override
    public String toString() {
        return String.format("Level: %s, XP: %s", this.level, this.xp);
    }
}
