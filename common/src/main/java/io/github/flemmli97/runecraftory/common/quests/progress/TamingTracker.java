package io.github.flemmli97.runecraftory.common.quests.progress;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.quests.tasks.TamingTask;
import io.github.flemmli97.simplequests_api.impls.progression.ProgressionTrackerBase;
import io.github.flemmli97.simplequests_api.player.ProgressionTrackerKey;
import io.github.flemmli97.simplequests_api.player.QuestProgress;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TamingTracker extends ProgressionTrackerBase<Entity, TamingTask.TamingTaskResolved> {

    public static final String TAMING_PROGRESS = TamingTask.ID + ".progress";
    public static final ProgressionTrackerKey<Entity, TamingTask.TamingTaskResolved> KEY = new ProgressionTrackerKey<>(RuneCraftory.MODID, "taming_tracker",
            TamingTask.ID);

    private final Set<UUID> entities = new HashSet<>();

    public TamingTracker(TamingTask.TamingTaskResolved questEntry) {
        super(questEntry);
    }

    @Override
    public boolean progress(ServerPlayer player, QuestProgress prog, Entity with) {
        if (this.questEntry().predicate().value().matches(player, with)) {
            if (this.entities.contains(with.getUUID())) {
                return false;
            }
            this.entities.add(with.getUUID());
            return this.entities.size() >= this.questEntry().amount();
        }
        return false;
    }

    @Override
    public MutableComponent formattedProgress(ServerPlayer player, QuestProgress progress) {
        float perc = this.entities.size() / (float) this.questEntry().amount();
        return new TranslatableComponent(TAMING_PROGRESS, this.entities.size(), this.questEntry().amount())
                .withStyle(ProgressionTrackerBase.of(perc));
    }

    @Override
    public Tag save() {
        ListTag list = new ListTag();
        this.entities.forEach(uuid -> list.add(NbtUtils.createUUID(uuid)));
        return list;
    }

    @Override
    public void load(Tag tag) {
        try {
            ListTag list = (ListTag) tag;
            list.forEach(t -> this.entities.add(NbtUtils.loadUUID(t)));
        } catch (ClassCastException ignored) {
        }
    }
}
