package io.github.flemmli97.runecraftory.common.entities.ai.npc.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.NPCAttackGoal;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModNPCActions;
import io.github.flemmli97.runecraftory.common.utils.CodecHelper;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.function.Supplier;

public class RunAwayAction implements NPCAction {

    public static final Codec<RunAwayAction> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(CodecHelper.NUMER_PROVIDER_CODEC.fieldOf("duration").forGetter(d -> d.duration),
                    NPCAction.optionalCooldown(d -> d.cooldown)
            ).apply(instance, RunAwayAction::new));

    private final NumberProvider duration;
    private final NumberProvider cooldown;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private RunAwayAction(NumberProvider duration, Optional<NumberProvider> cooldown) {
        this(duration, cooldown.orElse(NPCAction.CONST_ZERO));
    }

    public RunAwayAction(NumberProvider duration) {
        this(duration, NPCAction.CONST_ZERO);
    }

    public RunAwayAction(NumberProvider duration, NumberProvider cooldown) {
        this.duration = duration;
        this.cooldown = cooldown;
    }

    @Override
    public Supplier<NPCActionCodec> codec() {
        return ModNPCActions.RUN_AWAY_ACTION;
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
    public AnimatedAction getAction(EntityNPCBase npc) {
        return null;
    }

    @Override
    public boolean doAction(EntityNPCBase npc, NPCAttackGoal<?> goal, AnimatedAction action) {
        if (npc.getNavigation().isDone()) {
            Vec3 vec3 = DefaultRandomPos.getPosAway(npc, 16, 7, goal.getAttackTarget().position());
            if (vec3 == null) {
                return false;
            }
            npc.getNavigation().moveTo(vec3.x, vec3.y, vec3.z, 1.2f);
        }
        return false;
    }
}
