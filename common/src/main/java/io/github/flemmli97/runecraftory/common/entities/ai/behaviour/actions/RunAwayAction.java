package io.github.flemmli97.runecraftory.common.entities.ai.behaviour.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.registry.NPCAction;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.NPCAttackGoal;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModNPCActions;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class RunAwayAction implements NPCAction {

    public static final MapCodec<RunAwayAction> CODEC = RecordCodecBuilder.mapCodec((instance) ->
            instance.group(NumberProviders.CODEC.fieldOf("duration").forGetter(d -> d.duration),
                    NPCAction.optionalNumCooldown(d -> d.cooldown),
                    Codec.FLOAT.fieldOf("max_dist").forGetter(d -> d.maxDist),
                    Codec.FLOAT.fieldOf("speed").forGetter(d -> d.speed)
            ).apply(instance, RunAwayAction::new));

    private final NumberProvider duration;
    private final NumberProvider cooldown;
    private final float maxDist, speed;

    private RunAwayAction(NumberProvider duration, Optional<NumberProvider> cooldown, float maxDist, float speed) {
        this(duration, cooldown.orElse(NPCAction.CONST_ZERO), maxDist, speed);
    }

    public RunAwayAction(NumberProvider duration, float maxDist) {
        this(duration, NPCAction.CONST_ZERO, maxDist, 1.3f);
    }

    public RunAwayAction(NumberProvider duration, NumberProvider cooldown, float maxDist, float speed) {
        this.duration = duration;
        this.cooldown = cooldown;
        this.maxDist = maxDist;
        this.speed = speed;
    }

    @Override
    public MapCodec<RunAwayAction> codec() {
        return ModNPCActions.RUN_AWAY_ACTION.get();
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
        if (npc.getNavigation().isDone()) {
            if (goal.getDistSqr() > this.maxDist * this.maxDist)
                return true;
            Vec3 vec3 = DefaultRandomPos.getPosAway(npc, 16, 7, goal.getAttackTarget().position());
            if (vec3 == null) {
                return false;
            }
            npc.getNavigation().moveTo(vec3.x, vec3.y, vec3.z, this.speed);
        }
        return false;
    }
}
