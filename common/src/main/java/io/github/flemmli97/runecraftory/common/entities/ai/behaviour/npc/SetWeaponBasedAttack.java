package io.github.flemmli97.runecraftory.common.entities.ai.behaviour.npc;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.common.components.AttackActionData;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryDataComponentTypes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryMemoryTypes;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.Optional;

public class SetWeaponBasedAttack<E extends LivingEntity> extends ExtendedBehaviour<E> {

    private static final MemoryTest MEMORIES = MemoryTest.builder(1)
            .usesMemories(RuneCraftoryMemoryTypes.NPC_ATTACK_ACTION.get());

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORIES;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        if (!super.checkExtraStartConditions(level, entity))
            return false;
        ItemStack weapon = entity.getMainHandItem();
        AttackActionData action = weapon.get(RuneCraftoryDataComponentTypes.ATTACK_ACTION.get());
        return action != null && action.attackAction().get().isPresent();
    }

    @Override
    protected void start(E entity) {
        super.start(entity);
        ItemStack weapon = entity.getMainHandItem();
        AttackActionData action = weapon.get(RuneCraftoryDataComponentTypes.ATTACK_ACTION.get());
        Optional<Holder<AttackAction>> opt = action.attackAction().get();
        int amount = entity.getRandom().nextInt(opt.get().value().combos().size()) + 1;
        NPCAttackAction attackAction = new NPCAttackAction(opt.get().value(), amount, Optional.empty());
        BrainUtils.setMemory(entity, RuneCraftoryMemoryTypes.NPC_ATTACK_ACTION.get(), attackAction);
    }
}
