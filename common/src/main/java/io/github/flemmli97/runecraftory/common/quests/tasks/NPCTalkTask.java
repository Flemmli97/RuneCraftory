package io.github.flemmli97.runecraftory.common.quests.tasks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.misc.TreasureChestEntity;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.quests.quest.NPCQuestDefinition;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.world.data.RunecraftorySavedData;
import io.github.flemmli97.simplequests_api.player.QuestProgress;
import io.github.flemmli97.simplequests_api.quest.ResolvedQuest;
import io.github.flemmli97.simplequests_api.quest.entry.QuestTask;
import io.github.flemmli97.simplequests_api.quest.entry.QuestTaskKey;
import io.github.flemmli97.simplequests_api.quest.entry.ResolvedQuestTask;
import io.github.flemmli97.tenshilib.common.entity.EntityUtils;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.Optional;
import java.util.UUID;

public class NPCTalkTask implements QuestTask<NPCTalkTask.NPCTalkResolved>, NPCBasedTask {

    public static final QuestTaskKey<NPCTalkTask> ID = new QuestTaskKey<>(RuneCraftory.modRes("npc_talk"));
    public static final MapCodec<NPCTalkTask> CODEC = RecordCodecBuilder.mapCodec((instance) ->
            instance.group(ResourceLocation.CODEC.optionalFieldOf("target_npc_id").forGetter(d -> Optional.ofNullable(d.targetNPCId)),
                    ResourceLocation.CODEC.optionalFieldOf("dialogue").forGetter(d -> Optional.ofNullable(d.dialogue)),
                    ResourceLocation.CODEC.optionalFieldOf("loot_return").forGetter(d -> Optional.ofNullable(d.lootReturn)),
                    EntityPredicate.CODEC.optionalFieldOf("predicate").forGetter(d -> Optional.ofNullable(d.predicate))
            ).apply(instance, (npcId, dialogue, lootReturn, predicate) ->
                    new NPCTalkTask(npcId.orElse(null), dialogue.orElse(null), lootReturn.orElse(null), predicate.orElse(null))));

    @Nullable
    private final ResourceLocation targetNPCId;
    @Nullable
    private final ResourceLocation dialogue;
    @Nullable
    private final ResourceLocation lootReturn;

    private final EntityPredicate predicate;

    public NPCTalkTask() {
        this(null, null, null);
    }

    public NPCTalkTask(ResourceLocation npc) {
        this(npc, null, null, null);
    }

    public NPCTalkTask(@Nullable ResourceLocation npc, @Nullable ResourceLocation dialogue, @Nullable ResourceLocation lootReturn) {
        this(npc, dialogue, lootReturn, null);
    }

    protected NPCTalkTask(@Nullable ResourceLocation npc, @Nullable ResourceLocation dialogue, @Nullable ResourceLocation lootReturn, EntityPredicate predicate) {
        this.targetNPCId = npc;
        this.dialogue = dialogue;
        this.lootReturn = lootReturn;
        this.predicate = predicate;
    }

    @Override
    public ResourceLocation targetNPCId() {
        return this.targetNPCId;
    }

    @Override
    public QuestTaskKey<NPCTalkTask> getId() {
        return ID;
    }

    @Override
    public MutableComponent translation(ServerPlayer player) {
        return Component.translatable(this.getId().toString() + ".not_resolved");
    }

    @Override
    public NPCTalkTask.NPCTalkResolved resolve(QuestProgress progress, ResolvedQuest quest) {
        if (progress.getRootQuest() instanceof NPCQuestDefinition.NPCQuest npcQuest) {
            UUID target = this.targetNPCId == null ? npcQuest.getQuestGiverUUID() : npcQuest.getRandomNpcFor(progress.player().getRandom(), this.targetNPCId);
            if (target == null) {
                return null;
            }
            return new NPCTalkTask.NPCTalkResolved(target, this.dialogue, this.lootReturn, this.predicate);
        }
        return null;
    }

    public static class NPCTalkResolved implements ResolvedQuestTask, ResolvedNPCBasedTask {

        public static final MapCodec<NPCTalkResolved> CODEC = RecordCodecBuilder.mapCodec((instance) ->
                instance.group(Codec.STRING.fieldOf("target_npc").xmap(UUID::fromString, UUID::toString).forGetter(d -> d.targetNPC),
                        ResourceLocation.CODEC.optionalFieldOf("dialogue").forGetter(d -> Optional.ofNullable(d.dialogue)),
                        ResourceLocation.CODEC.optionalFieldOf("loot_return").forGetter(d -> d.lootReturn == null ? Optional.empty() : Optional.of(d.lootReturn.location())),
                        EntityPredicate.CODEC.optionalFieldOf("predicate").forGetter(d -> Optional.ofNullable(d.predicate))
                ).apply(instance, (target, dialogue, lootReturn, predicate) -> new NPCTalkResolved(target, dialogue.orElse(null), lootReturn.orElse(null), predicate.orElse(null))));

        private final UUID targetNPC;
        @Nullable
        private final ResourceLocation dialogue;
        @Nullable
        private final ResourceKey<LootTable> lootReturn;
        private final EntityPredicate predicate;
        private WeakReference<NPCEntity> npc;

        public NPCTalkResolved(UUID targetNPC, @Nullable ResourceLocation dialogue, @Nullable ResourceLocation lootReturn, EntityPredicate predicate) {
            this.targetNPC = targetNPC;
            this.dialogue = dialogue;
            this.lootReturn = lootReturn == null ? null : ResourceKey.create(Registries.LOOT_TABLE, lootReturn);
            this.predicate = predicate;
        }

        @Override
        public boolean submit(ServerPlayer player) {
            return false;
        }

        @Override
        public QuestTaskKey<NPCTalkTask> getId() {
            return ID;
        }

        public Runnable runOnCompleteFor(ServerPlayer player) {
            return () -> {
                TreasureChestEntity chest = RuneCraftoryEntities.TREASURE_CHEST.get().create(player.serverLevel());
                if (chest != null && this.lootReturn() != null && !this.lootReturn().equals(BuiltInLootTables.EMPTY)) {
                    chest.absMoveTo(player.getX(2), player.getY(1.5), player.getZ(2), player.getRandom().nextFloat() * 360.0f, 0.0f);
                    int tries = 0;
                    while (!player.level().noCollision(chest) && tries < 10) {
                        chest.absMoveTo(player.getX(2), player.getY(1.5), player.getZ(2), player.getRandom().nextFloat() * 360.0f, 0.0f);
                        tries++;
                    }
                    chest.setChestLoot(this.lootReturn());
                    player.serverLevel().addFreshEntity(chest);
                    chest.playSound(SoundEvents.CHEST_LOCKED, 1, 0.8f);
                }
            };
        }

        public ResourceKey<LootTable> lootReturn() {
            return this.lootReturn;
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

        public UUID targetNPC() {
            return this.targetNPC;
        }

        @Override
        @Nullable
        public ResourceLocation dialogue() {
            return this.dialogue;
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
                return Component.translatable(this.getId().toString(), name);
            return Component.translatable(this.getId().toString() + ".generic");
        }

        public boolean trySubmit(ServerPlayer player, NPCEntity npc) {
            return npc.getUUID().equals(this.targetNPC) && (this.predicate == null || this.predicate.matches(player, npc));
        }
    }
}
