package io.github.flemmli97.runecraftory.common.entities.ai.behaviour.npc;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.common.entities.npc.ConversationGroupMemory;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryMemoryTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SpeakInConversation<E extends NPCEntity> extends ExtendedBehaviour<E> {

    private static final MemoryTest MEMORIES = MemoryTest.builder(2)
            .hasMemories(RuneCraftoryMemoryTypes.CONVERSATION_GROUP.get())
            .usesMemories(MemoryModuleType.INTERACTION_TARGET);

    private Function<E, Integer> speakDuration = e -> e.getRandom().nextIntBetweenInclusive(50, 100);

    private List<NPCEntity> participants;
    private LivingEntity target;

    public SpeakInConversation<E> setSpeakDuration(Function<E, Integer> speakDuration) {
        this.speakDuration = speakDuration;
        return this;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORIES;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        this.target = this.getTarget(entity);
        boolean match = this.target != null && this.target.isAlive();
        if (!match) {
            this.clearMemory(entity);
        }
        return match;
    }

    private LivingEntity getTarget(E entity) {
        ConversationGroupMemory group = BrainUtils.getMemory(entity, RuneCraftoryMemoryTypes.CONVERSATION_GROUP.get());
        if (group == null) {
            return null;
        }
        this.participants = new ArrayList<>(group.participants());
        if (this.participants.isEmpty()) {
            return null;
        }
        LivingEntity speaker = group.speaker();
        if (group.speaker() == null && entity.getRandom().nextInt(this.participants.size() + 4) == 0) {
            group.setSpeaker(entity, this.speakDuration.apply(entity));
            speaker = entity;
            this.clearMemory(entity);
        }
        return speaker;
    }

    private void clearMemory(E entity) {
        BrainUtils.clearMemory(entity, MemoryModuleType.INTERACTION_TARGET);
    }

    @Override
    protected void start(E entity) {
        if (this.target == entity) {
            if (BrainUtils.hasMemory(entity, MemoryModuleType.INTERACTION_TARGET))
                return;
            BrainUtils.setForgettableMemory(entity, MemoryModuleType.INTERACTION_TARGET, this.participants.get(entity.getRandom().nextInt(this.participants.size())),
                    entity.getRandom().nextIntBetweenInclusive(40, 80));
        } else {
            BrainUtils.setMemory(entity, MemoryModuleType.INTERACTION_TARGET, this.target);
        }
    }

    @Override
    protected void stop(E entity) {
        this.target = null;
        this.participants = null;
    }
}
