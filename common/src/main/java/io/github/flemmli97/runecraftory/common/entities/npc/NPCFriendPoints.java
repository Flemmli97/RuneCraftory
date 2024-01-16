package io.github.flemmli97.runecraftory.common.entities.npc;

import io.github.flemmli97.runecraftory.common.attachment.player.LevelExpPair;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Set;

public class NPCFriendPoints {

    private int lastUpdateTalk = -1, lastUpdateGift = -1;
    private Set<String> answeredConversations = new HashSet<>();
    public final LevelExpPair points = new LevelExpPair();
    private int talkCount;

    public boolean talkTo(Level level, int xp) {
        int day = WorldUtils.day(level);
        if (day != this.lastUpdateTalk) {
            this.points.addXP(xp, 10, LevelCalc::friendPointsForNext, () -> {
            });
            this.lastUpdateTalk = day;
            this.answeredConversations.clear();
            this.talkCount++;
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

    public boolean answer(String conversation, int xp) {
        if (!this.answeredConversations.contains(conversation)) {
            this.points.addXP(xp, 10, LevelCalc::friendPointsForNext, () -> {
            });
            this.answeredConversations.add(conversation);
            return true;
        }
        return false;
    }

    public int getTalkCount() {
        return this.talkCount;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.put("FriendPoints", this.points.save());
        tag.putInt("LastTalk", this.lastUpdateTalk);
        tag.putInt("LastGift", this.lastUpdateGift);
        tag.putInt("TalkCount", this.talkCount);
        ListTag answers = new ListTag();
        this.answeredConversations.forEach(s -> answers.add(StringTag.valueOf(s)));
        tag.put("Answered", answers);
        return tag;
    }

    public void load(CompoundTag tag) {
        this.points.read(tag.getCompound("FriendPoints"));
        this.lastUpdateTalk = tag.getInt("LastTalk");
        this.lastUpdateGift = tag.getInt("LastGift");
        this.talkCount = tag.getInt("TalkCount");
        ListTag answers = tag.getList("Answered", Tag.TAG_STRING);
        answers.forEach(t -> this.answeredConversations.add(t.getAsString()));
    }
}
