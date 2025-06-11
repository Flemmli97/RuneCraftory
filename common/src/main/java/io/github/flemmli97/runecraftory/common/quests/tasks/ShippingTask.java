package io.github.flemmli97.runecraftory.common.quests.tasks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.utils.CodecHelper;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.simplequests_api.SimpleQuestsAPI;
import io.github.flemmli97.simplequests_api.player.PlayerQuestData;
import io.github.flemmli97.simplequests_api.player.QuestProgress;
import io.github.flemmli97.simplequests_api.quest.QuestBase;
import io.github.flemmli97.simplequests_api.quest.entry.QuestEntryKey;
import io.github.flemmli97.simplequests_api.quest.entry.QuestTask;
import io.github.flemmli97.simplequests_api.quest.entry.ResolvedQuestTask;
import io.github.flemmli97.simplequests_api.util.DescriptiveValue;
import io.github.flemmli97.simplequests_api.util.JsonCodecs;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.List;

public class ShippingTask implements QuestTask<ShippingTask.SkillLevelTaskResolved> {

    public static final QuestEntryKey<ShippingTask> ID = new QuestEntryKey<>(RuneCraftory.modRes("shipping"));
    public static final Codec<ShippingTask> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(Codec.STRING.fieldOf("description").forGetter(d -> d.description),
                    CodecHelper.nonEmptyList(DescriptiveValue.withTranslation(ItemPredicate.CODEC), "Item predicates can't be empty").fieldOf("item_predicates").forGetter(d -> d.itemPredicates),
                    NumberProviders.CODEC.fieldOf("amount").forGetter(d -> d.amount)
            ).apply(instance, ShippingTask::new));

    private final String description;

    private final List<DescriptiveValue<ItemPredicate>> itemPredicates;
    private final NumberProvider amount;

    public ShippingTask(String description, List<DescriptiveValue<ItemPredicate>> itemPredicates, NumberProvider amount) {
        this.description = description;
        this.itemPredicates = itemPredicates;
        this.amount = amount;
        if (this.description.isEmpty() && !this.simple())
            throw new IllegalStateException("Description is required");
    }

    private boolean simple() {
        return this.itemPredicates.size() == 1 && this.amount instanceof ConstantValue;
    }

    @Override
    public MutableComponent translation(ServerPlayer player) {
        if (this.description.isEmpty() && this.simple()) {
            return this.itemPredicates.get(0).getTranslation(this.getId().toString(), this.amount.getInt(null));
        }
        return Component.translatable(this.description);
    }

    @Override
    public QuestEntryKey<?> getId() {
        return ID;
    }

    @Override
    public SkillLevelTaskResolved resolve(PlayerQuestData data, QuestProgress progress, QuestBase base) {
        LootContext ctx = SimpleQuestsAPI.createContext(data, base.id);
        DescriptiveValue<ItemPredicate> val = this.itemPredicates.get(ctx.getRandom().nextInt(this.itemPredicates.size()));
        return new SkillLevelTaskResolved(val, this.amount.getInt(ctx));
    }

    public record SkillLevelTaskResolved(DescriptiveValue<ItemPredicate> item,
                                         int amount) implements ResolvedQuestTask {

        public static final Codec<SkillLevelTaskResolved> CODEC = RecordCodecBuilder.create((instance) ->
                instance.group(DescriptiveValue.withTranslation(JsonCodecs.ITEM_PREDICATE_CODEC).fieldOf("item").forGetter(d -> d.item),
                        ExtraCodecs.POSITIVE_INT.fieldOf("amount").forGetter(d -> d.amount)
                ).apply(instance, SkillLevelTaskResolved::new));

        @Override
        public boolean submit(ServerPlayer player) {
            return Platform.INSTANCE.getPlayerData(player).getPlayerLevel().getLevel() >= this.amount;
        }

        @Override
        public QuestEntryKey<ShippingTask> getId() {
            return ID;
        }

        @Override
        public MutableComponent translation(ServerPlayer player) {
            return this.item.getTranslation(this.getId().toString(), this.amount);
        }
    }
}
