package io.github.flemmli97.runecraftory.common.entities.ai.behaviour.npc;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.common.registry.ModMemoryTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.ToIntFunction;

public class SetSpellAttack<E extends LivingEntity> extends ExtendedBehaviour<E> {

    private static final MemoryTest MEMORIES = MemoryTest.builder(1)
            .usesMemories(ModMemoryTypes.NPC_ATTACK_ACTION.get());

    private final Spell spell;
    private final ToIntFunction<E> combos;

    public SetSpellAttack(Spell spell, ToIntFunction<E> combos) {
        this.spell = spell;
        this.combos = combos;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORIES;
    }

    @Override
    protected void start(E entity) {
        AttackAction act = this.spell.useAction();
        int combos = act.combos() == null ? 1 : Mth.clamp(this.combos.applyAsInt(entity), 1, act.combos().size());
        NPCAttackAction action = new NPCAttackAction(act, combos, Optional.of(this.spell));
        BrainUtils.setMemory(entity, ModMemoryTypes.NPC_ATTACK_ACTION.get(), action);
    }
}
