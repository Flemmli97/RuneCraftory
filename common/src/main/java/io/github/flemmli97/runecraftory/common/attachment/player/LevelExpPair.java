package io.github.flemmli97.runecraftory.common.attachment.player;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.IntUnaryOperator;

public class LevelExpPair {

    private int level = 1;
    private float xp, required;

    public LevelExpPair() {
    }

    public LevelExpPair(FriendlyByteBuf buf) {
        this.fromPacket(buf);
    }

    public void setLevel(int level, IntUnaryOperator xpForNext) {
        this.level = level;
        this.required = xpForNext.applyAsInt(this.level);
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

    public float getProgress() {
        if(this.required == -1)
            return 1;
        if (this.required <= 0)
            return 0;
        return Math.min(1, this.xp / this.required);
    }

    /**
     * Add the given amount of xp returning true if it was enough to level up
     */
    public boolean addXP(float amount, int maxLevel, IntUnaryOperator xpForNext, Runnable onLevelUp) {
        if (amount < 0) {
            int neededXP = xpForNext.applyAsInt(this.level - 1);
            this.required = neededXP;
            if (this.xp < -amount) {
                this.level -= 1;
                float current = this.xp;
                this.xp = neededXP;
                return this.addXP(amount + current, maxLevel, xpForNext, onLevelUp);
            }
            this.xp -= amount;
            return false;
        }
        int neededXP = xpForNext.applyAsInt(this.level);
        this.required = neededXP;
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
        if (this.level >= maxLevel) {
            amount = 0;
            this.required = -1;
        }
        this.xp += amount;
        return false;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Level", this.level);
        tag.putFloat("XP", this.xp);
        tag.putFloat("Required", this.required);
        return tag;
    }

    public void read(Tag tag) {
        if (tag instanceof IntArrayTag arrayTag) {
            this.level = arrayTag.getAsIntArray()[0];
            this.xp = arrayTag.getAsIntArray()[1];
        } else if (tag instanceof CompoundTag compoundTag) {
            this.level = compoundTag.getInt("Level");
            this.xp = compoundTag.getFloat("XP");
            this.required = compoundTag.getFloat("Required");
        }
    }

    public void toPacket(FriendlyByteBuf buf) {
        buf.writeInt(this.level);
        buf.writeFloat(this.xp);
        buf.writeFloat(this.required);
    }

    public void from(LevelExpPair pair) {
        this.level = pair.getLevel();
        this.xp = pair.getXp();
        this.required = pair.required;
    }

    public void fromPacket(FriendlyByteBuf buf) {
        this.level = buf.readInt();
        this.xp = buf.readFloat();
        this.required = buf.readFloat();
    }

    @Override
    public String toString() {
        return String.format("Level: %s, XP: %s", this.level, this.xp);
    }
}
