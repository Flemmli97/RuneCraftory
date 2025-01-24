package io.github.flemmli97.runecraftory.common.quests.tasks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.simplequests_api.mixin.ItemPredicateAccessor;
import io.github.flemmli97.simplequests_api.quest.entry.QuestEntry;
import io.github.flemmli97.simplequests_api.quest.entry.QuestEntryKey;
import io.github.flemmli97.simplequests_api.util.JsonCodecs;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShippingEntry implements QuestEntry {

    public static final QuestEntryKey<ShippingEntry> ID = new QuestEntryKey<>(new ResourceLocation(RuneCraftory.MODID, "shipping"));
    public static final Codec<ShippingEntry> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(JsonCodecs.ITEM_PREDICATE_CODEC.fieldOf("predicate").forGetter(d -> d.predicate),
                    ExtraCodecs.POSITIVE_INT.fieldOf("amount").forGetter(d -> d.amount),
                    Codec.STRING.optionalFieldOf("description").forGetter(d -> d.description == null ? Optional.empty() : Optional.of(d.description.getString()))
            ).apply(instance, (pred, amount, desc) -> new ShippingEntry(pred, amount, desc.orElse(""))));

    public final ItemPredicate predicate;
    public final int amount;
    public final MutableComponent description;

    public ShippingEntry(ItemPredicate predicate, int amount) {
        this(predicate, amount, "");
    }

    public ShippingEntry(ItemPredicate predicate, int amount, String description) {
        this.predicate = predicate;
        this.amount = amount;
        this.description = description.isEmpty() ? null : new TranslatableComponent(description);
    }

    @Override
    public boolean submit(ServerPlayer player) {
        return false;
    }

    @Override
    public QuestEntryKey<?> getId() {
        return ID;
    }

    @Override
    public MutableComponent translation(ServerPlayer player) {
        if (this.description != null)
            return this.description;
        List<MutableComponent> formattedItems = itemComponents(this.predicate);
        if (formattedItems.isEmpty())
            return new TranslatableComponent(this.getId().toString() + ".empty");
        if (formattedItems.size() == 1) {
            return new TranslatableComponent(this.getId().toString() + ".single", formattedItems.get(0).withStyle(ChatFormatting.AQUA), this.amount);
        }
        MutableComponent items = null;
        for (MutableComponent c : formattedItems) {
            if (items == null)
                items = new TextComponent("[").append(c);
            else
                items.append(new TextComponent(", ")).append(c);
        }
        items.append("]");
        return new TranslatableComponent(this.getId().toString() + ".multi", items.withStyle(ChatFormatting.AQUA), this.amount);
    }

    public static List<MutableComponent> itemComponents(ItemPredicate predicate) {
        ItemPredicateAccessor acc = (ItemPredicateAccessor) predicate;
        List<MutableComponent> formattedItems = new ArrayList<>();
        if (acc.getItems() != null)
            acc.getItems().forEach(i -> formattedItems.add(new TranslatableComponent(i.getDescriptionId())));
        if (acc.getTag() != null)
            Registry.ITEM.getTag(acc.getTag()).ifPresent(n -> n.forEach(h -> formattedItems.add(new TranslatableComponent(h.value().getDescriptionId()))));
        return formattedItems;
    }
}
