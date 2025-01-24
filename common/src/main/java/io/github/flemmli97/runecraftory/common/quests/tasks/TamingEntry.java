package io.github.flemmli97.runecraftory.common.quests.tasks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.simplequests_api.quest.entry.QuestEntry;
import io.github.flemmli97.simplequests_api.quest.entry.QuestEntryKey;
import io.github.flemmli97.simplequests_api.util.JsonCodecs;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;

import java.util.Optional;

public record TamingEntry(EntityPredicate predicate, int amount, String description) implements QuestEntry {

    public static final QuestEntryKey<TamingEntry> ID = new QuestEntryKey<>(new ResourceLocation(RuneCraftory.MODID, "taming"));
    public static final Codec<TamingEntry> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(JsonCodecs.ENTITY_PREDICATE_CODEC.optionalFieldOf("predicate").forGetter(d -> d.predicate == EntityPredicate.ANY ? Optional.empty() : Optional.of(d.predicate)),
                    ExtraCodecs.POSITIVE_INT.fieldOf("amount").forGetter(d -> d.amount),
                    Codec.STRING.fieldOf("description").forGetter(d -> d.description)
            ).apply(instance, (pred, amount, desc) -> new TamingEntry(pred.orElse(EntityPredicate.ANY), amount, desc)));

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
        if (this.description.isEmpty())
            return new TranslatableComponent(this.getId().toString());
        return new TranslatableComponent(this.description);
    }
}
