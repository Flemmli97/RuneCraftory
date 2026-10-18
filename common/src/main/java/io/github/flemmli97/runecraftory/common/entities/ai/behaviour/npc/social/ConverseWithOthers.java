package io.github.flemmli97.runecraftory.common.entities.ai.behaviour.npc.social;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.common.entities.ai.memory.ConversationGroupMemory;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryMemoryTypes;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.schedule.Activity;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.function.Predicate;

public class ConverseWithOthers<E extends NPCEntity> extends ExtendedBehaviour<E> {

    private static final MemoryTest MEMORIES = MemoryTest.builder(2)
            .usesMemories(RuneCraftoryMemoryTypes.CONVERSATION_GROUP.get())
            .hasMemories(RuneCraftoryMemoryTypes.NEARBY_NPCS.get());

    private final double rangeSqr;

    private final Predicate<E> newGroupChance = e -> e.getRandom().nextInt(150) == 0;

    public ConverseWithOthers() {
        this(10);
    }

    public ConverseWithOthers(double range) {
        this.rangeSqr = range * range;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORIES;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        boolean valid = this.valid(entity);
        if (!valid) {
            this.clearMemory(entity);
            return false;
        }
        return true;
    }

    private boolean valid(E entity) {
        if (!entity.getNPCSchedule().wantsToSocialize()) {
            return false;
        }
        Activity activity = entity.getActivity();
        GlobalPos target = null;
        if (activity == Activity.WORK) {
            target = BrainUtils.getMemory(entity, MemoryModuleType.JOB_SITE);
        } else if (activity == Activity.IDLE) {
            return true;
        } else if (activity == Activity.MEET) {
            target = BrainUtils.getMemory(entity, MemoryModuleType.MEETING_POINT);
        }
        if (target == null || !entity.level().dimension().equals(target.dimension())) {
            return false;
        }
        return entity.distanceToSqr(target.pos().getX() + 0.5, target.pos().getY(), target.pos().getZ() + 0.5) < 144;
    }

    private void clearMemory(E entity) {
        ConversationGroupMemory group = BrainUtils.getMemory(entity, RuneCraftoryMemoryTypes.CONVERSATION_GROUP.get());
        if (group != null) {
            group.leave(entity);
            BrainUtils.clearMemory(entity, RuneCraftoryMemoryTypes.CONVERSATION_GROUP.get());
        }
    }

    @Override
    protected void start(E entity) {
        ConversationGroupMemory current = BrainUtils.getMemory(entity, RuneCraftoryMemoryTypes.CONVERSATION_GROUP.get());
        if (current != null && current.isValid(this.rangeSqr)) {
            if (!current.isGroupComplete() && this.newGroupChance.test(entity)) {
                this.clearMemory(entity);
            }
            return;
        }

        List<NPCEntity> nearby = BrainUtils.getMemory(entity, RuneCraftoryMemoryTypes.NEARBY_NPCS.get());
        List<NPCEntity> groups = nearby.stream().filter(npc -> {
            if (npc.distanceToSqr(entity) > this.rangeSqr) {
                return false;
            }
            ConversationGroupMemory group = BrainUtils.getMemory(npc, RuneCraftoryMemoryTypes.CONVERSATION_GROUP.get());
            return group != null && group.canJoin(npc);
        }).toList();
        if (groups.isEmpty()) {
            // Create a new group
            BrainUtils.setMemory(entity, RuneCraftoryMemoryTypes.CONVERSATION_GROUP.get(), new ConversationGroupMemory(entity));
        } else {
            NPCEntity random = groups.get(entity.getRandom().nextInt(groups.size()));
            ConversationGroupMemory group = BrainUtils.getMemory(random, RuneCraftoryMemoryTypes.CONVERSATION_GROUP.get());
            group.join(entity);
            BrainUtils.setMemory(entity, RuneCraftoryMemoryTypes.CONVERSATION_GROUP.get(), new ConversationGroupMemory(entity, random));
        }
    }
}
