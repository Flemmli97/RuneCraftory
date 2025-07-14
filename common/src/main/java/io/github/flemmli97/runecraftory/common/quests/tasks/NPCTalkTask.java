package io.github.flemmli97.runecraftory.common.quests.tasks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.quests.NPCQuest;
import io.github.flemmli97.runecraftory.common.world.data.RunecraftorySavedData;
import io.github.flemmli97.simplequests_api.player.PlayerQuestData;
import io.github.flemmli97.simplequests_api.player.QuestProgress;
import io.github.flemmli97.simplequests_api.quest.QuestBase;
import io.github.flemmli97.simplequests_api.quest.entry.QuestEntryKey;
import io.github.flemmli97.simplequests_api.quest.entry.QuestTask;
import io.github.flemmli97.simplequests_api.quest.entry.ResolvedQuestTask;
import io.github.flemmli97.tenshilib.common.entity.EntityUtils;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;
import java.util.UUID;

public class NPCTalkTask implements QuestTask<NPCTalkTask.NPCTalkResolved> {

    public static final QuestEntryKey<NPCTalkTask> ID = new QuestEntryKey<>(RuneCraftory.modRes("npc_talk"));
    public static final MapCodec<NPCTalkTask> CODEC = RecordCodecBuilder.mapCodec((instance) ->
            instance.group(ResourceLocation.CODEC.optionalFieldOf("target_npc_id").forGetter(d -> Optional.ofNullable(d.targetNPCId)),
                    EntityPredicate.CODEC.optionalFieldOf("predicate").forGetter(d -> Optional.ofNullable(d.predicate))
            ).apply(instance, (npcId, predicate) -> new NPCTalkTask(npcId.orElse(null), predicate.orElse(null))));

    // Unused atm
    private final ResourceLocation targetNPCId;

    private final EntityPredicate predicate;

    public NPCTalkTask(ResourceLocation generic) {
        this(generic, null);
    }

    protected NPCTalkTask(ResourceLocation generic, EntityPredicate predicate) {
        this.targetNPCId = generic;
        this.predicate = predicate;
    }

    @Override
    public QuestEntryKey<NPCTalkTask> getId() {
        return ID;
    }

    @Override
    public MutableComponent translation(ServerPlayer player) {
        return Component.translatable(this.getId().toString() + ".not_resolved");
    }

    @Override
    public NPCTalkTask.NPCTalkResolved resolve(PlayerQuestData data, QuestProgress progress, QuestBase quest) {
        if (progress.getQuest() instanceof NPCQuest npcQuest)
            return new NPCTalkTask.NPCTalkResolved(npcQuest.getNpcUuid(), this.predicate);
        return null;
    }

    public static class NPCTalkResolved implements ResolvedQuestTask {

        public static final MapCodec<NPCTalkResolved> CODEC = RecordCodecBuilder.mapCodec((instance) ->
                instance.group(Codec.STRING.fieldOf("target_npc").xmap(UUID::fromString, UUID::toString).forGetter(d -> d.targetNPC),
                        EntityPredicate.CODEC.optionalFieldOf("predicate").forGetter(d -> Optional.ofNullable(d.predicate))
                ).apply(instance, (target, predicate) -> new NPCTalkResolved(target, predicate.orElse(null))));

        private final UUID targetNPC;
        private final EntityPredicate predicate;
        private NPCEntity npc;

        public NPCTalkResolved(UUID targetNPC, EntityPredicate predicate) {
            this.targetNPC = targetNPC;
            this.predicate = predicate;
        }

        @Override
        public boolean submit(ServerPlayer player) {
            return false;
        }

        @Override
        public QuestEntryKey<NPCTalkTask> getId() {
            return ID;
        }

        @Override
        public MutableComponent translation(ServerPlayer player) {
            if (this.npc == null || !this.npc.isAlive()) {
                if (this.targetNPC == null)
                    this.npc = null;
                else
                    this.npc = EntityUtils.findFromUUID(NPCEntity.class, player.level(), this.targetNPC);
            }
            Component name;
            if (this.npc == null) {
                name = this.targetNPC != null ? RunecraftorySavedData.get(player.getServer())
                        .npcHandler.getName(this.targetNPC) : null;
            } else {
                name = this.npc.getName();
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
