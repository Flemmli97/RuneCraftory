package io.github.flemmli97.runecraftory.common.entities.npc;

import io.github.flemmli97.runecraftory.common.entities.DailyEntityUpdater;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DailyNPCUpdater extends DailyEntityUpdater<EntityNPCBase> {

    private int bread;
    private final Set<UUID> acceptedRandomQuest = new HashSet<>();

    public DailyNPCUpdater(EntityNPCBase entity) {
        super(entity);
        this.bread = entity.getRandom().nextInt(4) + 1;
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();
        this.bread = this.entity.getRandom().nextInt(4) + 1;
        this.acceptedRandomQuest.clear();
    }

    public void acceptRandomQuest(ServerPlayer player) {
        this.acceptedRandomQuest.add(player.getUUID());
    }

    public boolean alreadyAcceptedRandomquest(ServerPlayer player) {
        return this.acceptedRandomQuest.contains(player.getUUID());
    }

    public int getBreadToBuy() {
        return this.bread;
    }

    public void onBuyBread() {
        this.bread--;
    }

    @Override
    public CompoundTag save() {
        CompoundTag tag = super.save();
        tag.putInt("Bread", this.bread);
        ListTag quests = new ListTag();
        this.acceptedRandomQuest.forEach(uuid -> quests.add(StringTag.valueOf(uuid.toString())));
        tag.put("AcceptedRandomQuests", quests);
        return tag;
    }

    @Override
    public void read(CompoundTag compound) {
        super.read(compound);
        this.bread = compound.getInt("Bread");
        ListTag quests = compound.getList("AcceptedRandomQuests", Tag.TAG_STRING);
        quests.forEach(t -> this.acceptedRandomQuest.add(UUID.fromString(t.getAsString())));
    }
}
