package io.github.flemmli97.runecraftory.common.quests.progress;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.quests.tasks.DeliveryTask;
import io.github.flemmli97.simplequests_api.impls.progression.ProgressionTrackerBase;
import io.github.flemmli97.simplequests_api.player.ProgressionTrackerKey;
import io.github.flemmli97.simplequests_api.player.QuestProgress;
import io.github.flemmli97.simplequests_api.player.TaskResult;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class DeliveryTracker extends ProgressionTrackerBase<Pair<ItemStack, NPCEntity>, DeliveryTask.DeliveryTaskResolved> {

    public static final String PROGRESS_TRANSLATION = DeliveryTask.ID + ".progress";
    public static final ProgressionTrackerKey<Pair<ItemStack, NPCEntity>, DeliveryTask.DeliveryTaskResolved> KEY = new ProgressionTrackerKey<>(RuneCraftory.MODID, "delivery_tracker",
            DeliveryTask.ID);

    private int value = 0;

    public DeliveryTracker(DeliveryTask.DeliveryTaskResolved questEntry) {
        super(questEntry);
    }

    @Override
    public TaskResult progress(ServerPlayer player, QuestProgress prog, Pair<ItemStack, NPCEntity> with) {
        if (!with.getSecond().getUUID().equals(this.questEntry().targetNPC()))
            return TaskResult.NONE;
        if (!this.questEntry().predicate().value().test(with.getFirst()))
            return TaskResult.NONE;
        ItemStack stack = with.getFirst();
        if (stack.isEmpty())
            return TaskResult.NONE;
        int amountNeeded = Math.max(0, this.questEntry().amount() - this.value);
        int amountGiven = Math.min(stack.getCount(), amountNeeded);
        stack.shrink(amountGiven);
        this.value += amountGiven;
        return this.value >= this.questEntry().amount() ? TaskResult.COMPLETED : TaskResult.ADVANCED;
    }

    @Override
    public MutableComponent formattedProgress(ServerPlayer player, QuestProgress progress) {
        float perc = this.value / (float) this.questEntry().amount();
        int amountNeeded = Math.max(0, this.questEntry().amount() - this.value);
        return Component.translatable(PROGRESS_TRANSLATION, this.questEntry().description(), amountNeeded).withStyle(ProgressionTrackerBase.of(perc));
    }

    @Override
    public Tag save() {
        return IntTag.valueOf(this.value);
    }

    @Override
    public void load(Tag tag) {
        try {
            this.value = ((NumericTag) tag).getAsInt();
        } catch (ClassCastException ignored) {
        }
    }
}