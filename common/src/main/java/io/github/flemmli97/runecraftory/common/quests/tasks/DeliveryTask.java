package io.github.flemmli97.runecraftory.common.quests.tasks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.quests.progress.DeliveryTracker;
import io.github.flemmli97.runecraftory.common.quests.quest.NPCQuestDefinition;
import io.github.flemmli97.runecraftory.common.world.data.RunecraftorySavedData;
import io.github.flemmli97.simplequests_api.SimpleQuestsAPI;
import io.github.flemmli97.simplequests_api.player.QuestProgress;
import io.github.flemmli97.simplequests_api.quest.ResolvedQuest;
import io.github.flemmli97.simplequests_api.quest.entry.QuestTask;
import io.github.flemmli97.simplequests_api.quest.entry.QuestTaskKey;
import io.github.flemmli97.simplequests_api.quest.entry.ResolvedQuestTask;
import io.github.flemmli97.simplequests_api.util.DescriptiveValue;
import io.github.flemmli97.simplequests_api.util.JsonCodecs;
import io.github.flemmli97.simplequests_api.util.QuestUtils;
import io.github.flemmli97.tenshilib.common.entity.EntityUtils;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DeliveryTask implements QuestTask<DeliveryTask.DeliveryTaskResolved>, NPCBasedTask {

    public static final QuestTaskKey<DeliveryTask> ID = new QuestTaskKey<>(RuneCraftory.modRes("delivery"));
    public static final MapCodec<DeliveryTask> CODEC = RecordCodecBuilder.mapCodec((instance) ->
            instance.group(ResourceLocation.CODEC.optionalFieldOf("target_npc_id").forGetter(d -> Optional.ofNullable(d.targetNPCId)),
                    ResourceLocation.CODEC.optionalFieldOf("dialogue").forGetter(d -> Optional.ofNullable(d.dialogue)),
                    JsonCodecs.nonEmptyList(DescriptiveValue.withTranslation(ItemPredicate.CODEC), "items cant' be empty").fieldOf("items").forGetter(d -> d.predicates),
                    NumberProviders.CODEC.fieldOf("amount").forGetter(d -> d.amount)
            ).apply(instance, (npcId, dialogue, predicates, amount) ->
                    new DeliveryTask(npcId.orElse(null), dialogue.orElse(null), predicates, amount)));

    @Nullable
    private final ResourceLocation targetNPCId;
    @Nullable
    private final ResourceLocation dialogue;

    private final List<DescriptiveValue<ItemPredicate>> predicates;
    private final NumberProvider amount;

    public DeliveryTask(List<DescriptiveValue<ItemPredicate>> predicates, NumberProvider amount) {
        this(null, null, predicates, amount);
    }

    public DeliveryTask(@Nullable ResourceLocation npc, List<DescriptiveValue<ItemPredicate>> predicates, NumberProvider amount) {
        this(npc, null, predicates, amount);
    }

    public DeliveryTask(@Nullable ResourceLocation npc, @Nullable ResourceLocation dialogue,
                        List<DescriptiveValue<ItemPredicate>> predicates, NumberProvider amount) {
        this.targetNPCId = npc;
        this.dialogue = dialogue;
        this.predicates = predicates;
        this.amount = amount;
    }

    @Override
    public ResourceLocation targetNPCId() {
        return this.targetNPCId;
    }

    @Override
    public QuestTaskKey<DeliveryTask> getId() {
        return ID;
    }

    @Override
    public MutableComponent translation(ServerPlayer player) {
        return Component.translatable(this.getId().toString() + ".not_resolved");
    }

    @Override
    public DeliveryTaskResolved resolve(QuestProgress progress, ResolvedQuest quest) {
        if (progress.getRootQuest() instanceof NPCQuestDefinition.NPCQuest npcQuest) {
            UUID target = this.targetNPCId == null ? npcQuest.getQuestGiverUUID() : npcQuest.getRandomNpcFor(progress.player().getRandom(), this.targetNPCId);
            if (target == null) {
                return null;
            }
            LootContext ctx = SimpleQuestsAPI.createContext(progress.data(), quest.id());
            DescriptiveValue<ItemPredicate> val = this.predicates.get(ctx.getRandom().nextInt(this.predicates.size()));
            return new DeliveryTaskResolved(target, this.dialogue, val, QuestUtils.getAmount(this.amount, ctx, progress.data(), quest.id()));
        }
        return null;
    }

    public static class DeliveryTaskResolved implements ResolvedQuestTask, ResolvedNPCBasedTask {

        public static final MapCodec<DeliveryTaskResolved> CODEC = RecordCodecBuilder.mapCodec((instance) ->
                instance.group(Codec.STRING.fieldOf("target_npc").xmap(UUID::fromString, UUID::toString).forGetter(d -> d.targetNPC),
                        ResourceLocation.CODEC.optionalFieldOf("dialogue").forGetter(d -> Optional.ofNullable(d.dialogue)),
                        DescriptiveValue.withTranslation(ItemPredicate.CODEC).fieldOf("item").forGetter(d -> d.predicate),
                        ExtraCodecs.POSITIVE_INT.fieldOf("amount").forGetter(d -> d.amount)
                ).apply(instance, (target, dialogue, predicate, amount) -> new DeliveryTaskResolved(target, dialogue.orElse(null), predicate, amount)));

        private final UUID targetNPC;
        @Nullable
        private final ResourceLocation dialogue;
        private final DescriptiveValue<ItemPredicate> predicate;
        private final int amount;

        private WeakReference<NPCEntity> npc;

        public DeliveryTaskResolved(UUID targetNPC, @Nullable ResourceLocation dialogue, DescriptiveValue<ItemPredicate> predicate, int amount) {
            this.targetNPC = targetNPC;
            this.dialogue = dialogue;
            this.predicate = predicate;
            this.amount = amount;
        }

        @Override
        public boolean submit(ServerPlayer player) {
            return false;
        }

        @Override
        public QuestTaskKey<DeliveryTask> getId() {
            return ID;
        }

        @Override
        @Nullable
        public NPCEntity getNpc(Level level) {
            NPCEntity npc = this.npc == null ? null : this.npc.get();
            if (npc == null || !npc.isAlive()) {
                npc = EntityUtils.findFromUUID(NPCEntity.class, level, this.targetNPC);
                this.npc = new WeakReference<>(npc);
            }
            return npc;
        }

        @Override
        public Component progressComponent(QuestProgress progress, String taskName) {
            return progress.getOrCreateTracker(DeliveryTracker.KEY, this, taskName)
                    .formattedProgress(progress.player(), progress);
        }

        public UUID targetNPC() {
            return this.targetNPC;
        }

        @Override
        @Nullable
        public ResourceLocation dialogue() {
            return this.dialogue;
        }

        public DescriptiveValue<ItemPredicate> predicate() {
            return this.predicate;
        }

        public int amount() {
            return this.amount;
        }

        public MutableComponent description() {
            return this.predicate.getTranslation();
        }

        @Override
        public MutableComponent translation(ServerPlayer player) {
            NPCEntity npc = this.getNpc(player.level());
            Component name;
            if (npc == null) {
                name = this.targetNPC != null ? RunecraftorySavedData.get(player.getServer()).npcHandler.getName(this.targetNPC) : null;
            } else {
                name = npc.getName();
            }
            if (name != null)
                return Component.translatable(this.getId().toString(), name, this.description(), this.amount);
            return Component.translatable(this.getId().toString() + ".generic");
        }
    }
}
