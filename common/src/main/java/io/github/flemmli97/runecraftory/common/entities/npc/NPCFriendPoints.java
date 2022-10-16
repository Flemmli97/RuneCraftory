package io.github.flemmli97.runecraftory.common.entities.npc;

import io.github.flemmli97.runecraftory.common.attachment.player.LevelExpPair;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

public class NPCFriendPoints {

    private int lastUpdateTalk = -1, lastUpdateGift = -1;
    public final LevelExpPair points = new LevelExpPair();

    public boolean talkTo(Level level, int xp) {
        int day = WorldUtils.day(level);
        if (day != this.lastUpdateTalk) {
            this.points.addXP(xp, 10, LevelCalc::friendPointsForNext, () -> {
            });
            this.lastUpdateTalk = day;
            return true;
        }
        return false;
    }

    public boolean giftXP(Level level, int xp) {
        int day = WorldUtils.day(level);
        if (day != this.lastUpdateGift) {
            this.points.addXP(xp, 10, LevelCalc::friendPointsForNext, () -> {
            });
            this.lastUpdateGift = day;
            return true;
        }
        return false;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.put("FriendPoints", this.points.save());
        tag.putInt("LastTalk", this.lastUpdateTalk);
        tag.putInt("LastGift", this.lastUpdateGift);
        return tag;
    }

    public void load(CompoundTag tag) {
        this.points.read(tag.getCompound("FriendPoints"));
        this.lastUpdateTalk = tag.getInt("LastTalk");
        this.lastUpdateGift = tag.getInt("LastGift");
    }
}
