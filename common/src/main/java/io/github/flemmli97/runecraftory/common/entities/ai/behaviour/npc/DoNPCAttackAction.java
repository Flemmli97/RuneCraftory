package io.github.flemmli97.runecraftory.common.entities.ai.behaviour.npc;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModMemoryTypes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

public class DoNPCAttackAction extends ExtendedBehaviour<EntityNPCBase> {

    private static final MemoryTest MEMORIES = MemoryTest.builder(1)
            .hasMemories(ModMemoryTypes.NPC_ATTACK_ACTION.get());

    private NPCAttackAction current;

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORIES;
    }

    @Override
    protected void start(EntityNPCBase entity) {
        BrainUtils.withMemory(entity, ModMemoryTypes.NPC_ATTACK_ACTION.get(), selected -> {
            this.current = selected;
            entity.weaponHandler.doWeaponAttack(selected.action(), entity.getMainHandItem(), selected.spell().orElse(null));
        });
        BrainUtils.clearMemory(entity, ModMemoryTypes.NPC_ATTACK_ACTION.get());
    }

    @Override
    protected boolean shouldKeepRunning(EntityNPCBase entity) {
        if (this.current == null)
            return false;
        if (entity.weaponHandler.isScheduledAction())
            return true;
        return this.tryScheduleCombo(entity);
    }

    @Override
    protected void stop(EntityNPCBase entity) {
        super.stop(entity);
        BrainUtils.clearMemory(entity, ModMemoryTypes.NPC_ATTACK_ACTION.get());
        this.current = null;
    }

    private boolean tryScheduleCombo(EntityNPCBase npc) {
        if (npc.weaponHandler.isScheduledAction())
            return false;
        int combo = npc.weaponHandler.getComboCount();
        if (this.current != null && combo < this.current.comboCount()) {
            npc.weaponHandler.doWeaponAttack(this.current.action(), npc.getMainHandItem(), this.current.spell().orElse(null));
            return true;
        }
        return false;
    }
}
