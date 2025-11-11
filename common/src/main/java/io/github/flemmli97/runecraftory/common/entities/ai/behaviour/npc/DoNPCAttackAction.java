package io.github.flemmli97.runecraftory.common.entities.ai.behaviour.npc;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryMemoryTypes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

public class DoNPCAttackAction extends ExtendedBehaviour<NPCEntity> {

    private static final MemoryTest MEMORIES = MemoryTest.builder(1)
            .hasMemories(RuneCraftoryMemoryTypes.NPC_ATTACK_ACTION.get());

    private NPCAttackAction current;

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORIES;
    }

    @Override
    protected void start(NPCEntity entity) {
        BrainUtils.withMemory(entity, RuneCraftoryMemoryTypes.NPC_ATTACK_ACTION.get(), selected -> {
            this.current = selected;
            entity.weaponHandler.executeAttack(selected.action(), entity.getMainHandItem(), selected.spell().orElse(null));
        });
        BrainUtils.clearMemory(entity, RuneCraftoryMemoryTypes.NPC_ATTACK_ACTION.get());
    }

    @Override
    protected boolean shouldKeepRunning(NPCEntity entity) {
        if (this.current == null)
            return false;
        if (entity.weaponHandler.shouldContinueAttack())
            return true;
        return this.tryScheduleCombo(entity);
    }

    @Override
    protected void stop(NPCEntity entity) {
        super.stop(entity);
        BrainUtils.clearMemory(entity, RuneCraftoryMemoryTypes.NPC_ATTACK_ACTION.get());
        this.current = null;
    }

    private boolean tryScheduleCombo(NPCEntity npc) {
        if (npc.weaponHandler.shouldContinueAttack())
            return false;
        int combo = npc.weaponHandler.getComboCount();
        if (this.current != null && combo < this.current.comboCount()) {
            npc.weaponHandler.executeAttack(this.current.action(), npc.getMainHandItem(), this.current.spell().orElse(null));
            return true;
        }
        return false;
    }
}
