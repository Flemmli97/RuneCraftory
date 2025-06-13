package io.github.flemmli97.runecraftory.common.quests.tasks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.simplequests_api.SimpleQuestsAPI;
import io.github.flemmli97.simplequests_api.player.PlayerQuestData;
import io.github.flemmli97.simplequests_api.player.QuestProgress;
import io.github.flemmli97.simplequests_api.quest.QuestBase;
import io.github.flemmli97.simplequests_api.quest.entry.QuestEntryKey;
import io.github.flemmli97.simplequests_api.quest.entry.QuestTask;
import io.github.flemmli97.simplequests_api.quest.entry.ResolvedQuestTask;
import io.github.flemmli97.simplequests_api.util.DescriptiveValue;
import io.github.flemmli97.simplequests_api.util.JsonCodecs;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

import java.util.List;

public class TamingTask implements QuestTask<TamingTask.TamingTaskResolved> {

    public static final QuestEntryKey<TamingTask> ID = new QuestEntryKey<>(RuneCraftory.modRes("taming"));
    public static final Codec<TamingTask> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(Codec.STRING.fieldOf("description").forGetter(d -> d.description),
                    JsonCodecs.nonEmptyList(DescriptiveValue.withTranslation(JsonCodecs.ENTITY_PREDICATE_CODEC), "predicates can't be empty").fieldOf("predicates").forGetter(d -> d.predicates),
                    NumberProviders.CODEC.fieldOf("amount").forGetter(d -> d.amount)
            ).apply(instance, TamingTask::new));

    private final String description;

    private final List<DescriptiveValue<EntityPredicate>> predicates;
    private final NumberProvider amount;

    public TamingTask(String description, List<DescriptiveValue<EntityPredicate>> predicates, NumberProvider amount) {
        this.description = description;
        this.predicates = predicates;
        this.amount = amount;
        if (this.description.isEmpty() && !this.simple())
            throw new IllegalStateException("Description is required");
    }

    private boolean simple() {
        return this.predicates.size() == 1 && this.amount instanceof ConstantValue;
    }

    @Override
    public MutableComponent translation(ServerPlayer player) {
        if (this.description.isEmpty() && this.simple()) {
            return this.predicates.get(0).getTranslation(this.getId().toString(),
                    this.amount.getInt(null));
        }
        return Component.translatable(this.description);
    }

    @Override
    public QuestEntryKey<?> getId() {
        return ID;
    }

    @Override
    public TamingTaskResolved resolve(PlayerQuestData data, QuestProgress progress, QuestBase base) {
        LootContext ctx = SimpleQuestsAPI.createContext(data, base.id);
        DescriptiveValue<EntityPredicate> val = this.predicates.get(ctx.getRandom().nextInt(this.predicates.size()));
        return new TamingTaskResolved(val, this.amount.getInt(ctx));
    }

    public record TamingTaskResolved(DescriptiveValue<EntityPredicate> predicate,
                                     int amount) implements ResolvedQuestTask {

        public static final Codec<TamingTaskResolved> CODEC = RecordCodecBuilder.create((instance) ->
                instance.group(DescriptiveValue.withTranslation(JsonCodecs.ENTITY_PREDICATE_CODEC).fieldOf("predicate").forGetter(d -> d.predicate),
                        ExtraCodecs.POSITIVE_INT.fieldOf("amount").forGetter(d -> d.amount)
                ).apply(instance, TamingTaskResolved::new));

        @Override
        public boolean submit(ServerPlayer player) {
            return false;
        }

        @Override
        public QuestEntryKey<TamingTask> getId() {
            return ID;
        }

        @Override
        public MutableComponent translation(ServerPlayer player) {
            return this.predicate.getTranslation(this.getId().toString(), this.amount);
        }
    }
}
