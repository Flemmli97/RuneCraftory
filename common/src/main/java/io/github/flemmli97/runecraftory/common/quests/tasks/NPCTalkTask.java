package io.github.flemmli97.runecraftory.common.quests.tasks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.quests.quest.NPCQuestDefinition;
import io.github.flemmli97.runecraftory.common.world.data.RunecraftorySavedData;
import io.github.flemmli97.simplequests_api.player.QuestProgress;
import io.github.flemmli97.simplequests_api.quest.ResolvedQuest;
import io.github.flemmli97.simplequests_api.quest.entry.QuestTask;
import io.github.flemmli97.simplequests_api.quest.entry.QuestTaskKey;
import io.github.flemmli97.simplequests_api.quest.entry.ResolvedQuestTask;
import io.github.flemmli97.tenshilib.common.entity.EntityUtils;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.Optional;
import java.util.UUID;

public class NPCTalkTask implements QuestTask<NPCTalkTask.NPCTalkResolved>, NPCBasedTask {

    public static final QuestTaskKey<NPCTalkTask> ID = new QuestTaskKey<>(RuneCraftory.modRes("npc_talk"));
    public static final MapCodec<NPCTalkTask> CODEC = RecordCodecBuilder.mapCodec((instance) ->
            instance.group(ResourceLocation.CODEC.optionalFieldOf("target_npc_id").forGetter(d -> Optional.ofNullable(d.targetNPCId)),
                    ResourceLocation.CODEC.optionalFieldOf("dialogue").forGetter(d -> Optional.ofNullable(d.dialogue)),
                    EntityPredicate.CODEC.optionalFieldOf("predicate").forGetter(d -> Optional.ofNullable(d.predicate))
            ).apply(instance, (npcId, dialogue, predicate) ->
                    new NPCTalkTask(npcId.orElse(null), dialogue.orElse(null), predicate.orElse(null))));

    @Nullable
    private final ResourceLocation targetNPCId;
    @Nullable
    private final ResourceLocation dialogue;

    private final EntityPredicate predicate;

    public NPCTalkTask() {
        this(null, null, null);
    }

    public NPCTalkTask(ResourceLocation npc) {
        this(npc, null, null);
    }

    public NPCTalkTask(ResourceLocation npc, @Nullable ResourceLocation dialogue) {
        this(npc, dialogue, null);
    }

    protected NPCTalkTask(@Nullable ResourceLocation npc, @Nullable ResourceLocation dialogue, EntityPredicate predicate) {
        this.targetNPCId = npc;
        this.dialogue = dialogue;
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
            UUID target = this.targetNPCId == null ? npcQuest.getQuestGiverUUID() : npcQuest.relevantNpcs().get(this.targetNPCId);
            if (target == null) {
                return null;
            }
            return new NPCTalkTask.NPCTalkResolved(target, this.dialogue, this.predicate);
        }
        return null;
    }

    public static class NPCTalkResolved implements ResolvedQuestTask, ResolvedNPCBasedTask {

        public static final MapCodec<NPCTalkResolved> CODEC = RecordCodecBuilder.mapCodec((instance) ->
                instance.group(Codec.STRING.fieldOf("target_npc").xmap(UUID::fromString, UUID::toString).forGetter(d -> d.targetNPC),
                        ResourceLocation.CODEC.optionalFieldOf("dialogue").forGetter(d -> Optional.ofNullable(d.dialogue)),
                        EntityPredicate.CODEC.optionalFieldOf("predicate").forGetter(d -> Optional.ofNullable(d.predicate))
                ).apply(instance, (target, dialogue, predicate) -> new NPCTalkResolved(target, dialogue.orElse(null), predicate.orElse(null))));

        private final UUID targetNPC;
        @Nullable
        private final ResourceLocation dialogue;
        private final EntityPredicate predicate;
        private WeakReference<NPCEntity> npc;

        public NPCTalkResolved(UUID targetNPC, @Nullable ResourceLocation dialogue, EntityPredicate predicate) {
            this.targetNPC = targetNPC;
            this.dialogue = dialogue;
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
