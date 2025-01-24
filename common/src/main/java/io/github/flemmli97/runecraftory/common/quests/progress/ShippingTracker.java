package io.github.flemmli97.runecraftory.common.quests.progress;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.quests.tasks.ShippingEntry;
import io.github.flemmli97.simplequests_api.impls.progression.ProgressionTrackerBase;
import io.github.flemmli97.simplequests_api.player.ProgressionTrackerKey;
import io.github.flemmli97.simplequests_api.player.QuestProgress;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ShippingTracker extends ProgressionTrackerBase<ItemStack, ShippingEntry> {

    public static final String TAMING_PROGRESS = ShippingEntry.ID + ".progress";
    public static final ProgressionTrackerKey<ItemStack, ShippingEntry> KEY = new ProgressionTrackerKey<>(RuneCraftory.MODID, "shipping_tracker",
            ShippingEntry.ID);

    private int amount = 0;

    public ShippingTracker(ShippingEntry questEntry) {
        super(questEntry);
    }

    @Override
    public boolean progress(ServerPlayer player, QuestProgress prog, ItemStack with) {
        if (this.questEntry().predicate.matches(with)) {
            this.amount += with.getCount();
            return this.amount >= this.questEntry().amount;
        }
        return false;
    }

    @Override
    public MutableComponent formattedProgress(ServerPlayer player, QuestProgress progress) {
        float perc = this.amount / (float) this.questEntry().amount;
        ChatFormatting form = ChatFormatting.DARK_GREEN;
        if (perc <= 0.35) {
            form = ChatFormatting.DARK_RED;
        } else if (perc <= 0.7) {
            form = ChatFormatting.GOLD;
        }
        return new TranslatableComponent(TAMING_PROGRESS, this.amount, this.questEntry().amount).withStyle(form);
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
