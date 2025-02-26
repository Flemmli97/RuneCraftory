package io.github.flemmli97.runecraftory.common.quests.progress;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.quests.tasks.ShippingTask;
import io.github.flemmli97.simplequests_api.impls.progression.ProgressionTrackerBase;
import io.github.flemmli97.simplequests_api.player.ProgressionTrackerKey;
import io.github.flemmli97.simplequests_api.player.QuestProgress;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ShippingTracker extends ProgressionTrackerBase<ItemStack, ShippingTask.SkillLevelTaskResolved> {

    public static final String TAMING_PROGRESS = ShippingTask.ID + ".progress";
    public static final ProgressionTrackerKey<ItemStack, ShippingTask.SkillLevelTaskResolved> KEY = new ProgressionTrackerKey<>(RuneCraftory.MODID, "shipping_tracker",
            ShippingTask.ID);

    private int amount = 0;

    public ShippingTracker(ShippingTask.SkillLevelTaskResolved questEntry) {
        super(questEntry);
    }

    @Override
    public boolean progress(ServerPlayer player, QuestProgress prog, ItemStack with) {
        if (this.questEntry().item().value().matches(with)) {
            this.amount += with.getCount();
            return this.amount >= this.questEntry().amount();
        }
        return false;
    }

    @Override
    public MutableComponent formattedProgress(ServerPlayer player, QuestProgress progress) {
        float perc = this.amount / (float) this.questEntry().amount();
        return new TranslatableComponent(TAMING_PROGRESS, this.amount, this.questEntry().amount())
                .withStyle(ProgressionTrackerBase.of(perc));
    }

    @Override
    public Tag save() {
        return IntTag.valueOf(this.amount);
    }

    @Override
    public void load(Tag tag) {
        try {
            this.amount = ((NumericTag) tag).getAsInt();
        } catch (ClassCastException ignored) {
        }
    }
}
