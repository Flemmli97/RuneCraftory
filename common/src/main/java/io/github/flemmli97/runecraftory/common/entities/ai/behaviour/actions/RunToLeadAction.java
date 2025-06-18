package io.github.flemmli97.runecraftory.common.entities.ai.behaviour.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.registry.NPCAction;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.NPCAttackGoal;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModNPCActions;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

import java.util.Optional;

public class RunToLeadAction implements NPCAction {

    public static final MapCodec<RunToLeadAction> CODEC = RecordCodecBuilder.mapCodec((instance) ->
            instance.group(NumberProviders.CODEC.fieldOf("duration").forGetter(d -> d.duration),
                    NPCAction.optionalNumCooldown(d -> d.cooldown)
            ).apply(instance, RunToLeadAction::new));

    private final NumberProvider duration;
    private final NumberProvider cooldown;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private RunToLeadAction(NumberProvider duration, Optional<NumberProvider> cooldown) {
        this(duration, cooldown.orElse(NPCAction.CONST_ZERO));
    }

    public RunToLeadAction(NumberProvider duration) {
        this(duration, NPCAction.CONST_ZERO);
    }

    public RunToLeadAction(NumberProvider duration, NumberProvider cooldown) {
        this.duration = duration;
        this.cooldown = cooldown;
    }

    @Override
    public MapCodec<RunToLeadAction> codec() {
        return ModNPCActions.RUN_TO_LEADER.get();
    }

    @Override
    public int getDuration(EntityNPCBase npc) {
        return this.duration.getInt(NPCAction.createLootContext(npc));
    }

    @Override
    public int getCooldown(EntityNPCBase npc) {
        return this.cooldown.getInt(NPCAction.createLootContext(npc));
    }

    @Override
    public boolean doAction(EntityNPCBase npc, NPCAttackGoal<?> goal, NPCAttackAction action) {
        if (npc.followEntity() == null)
            return true;
        goal.moveToEntityNearer(npc.followEntity(), 1.2f);
        return npc.distanceToSqr(npc.followEntity()) <= 2;
    }
}
