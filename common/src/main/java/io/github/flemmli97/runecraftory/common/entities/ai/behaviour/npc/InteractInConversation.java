package io.github.flemmli97.runecraftory.common.entities.ai.behaviour.npc;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryMemoryTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class InteractInConversation<E extends NPCEntity> extends ExtendedBehaviour<E> {

    private static final MemoryTest MEMORIES = MemoryTest.builder(2)
            .usesMemories(MemoryModuleType.LOOK_TARGET)
            .hasMemories(MemoryModuleType.INTERACTION_TARGET)
            .hasMemories(RuneCraftoryMemoryTypes.CONVERSATION_GROUP.get());

    private LivingEntity target;

    private BiPredicate<E, LivingEntity> interactionPredicate = (e, target) -> target instanceof NPCEntity;

    private Predicate<E> playEmoteChance = e -> e.getRandom().nextInt(150) == 0;
    private List<ParticleOptions> particles = List.of(ParticleTypes.HAPPY_VILLAGER, ParticleTypes.ANGRY_VILLAGER, ParticleTypes.HEART,
            ParticleTypes.NOTE);

    public InteractInConversation<E> setPlayEmoteChance(Predicate<E> playEmoteChance) {
        this.playEmoteChance = playEmoteChance;
        return this;
    }

    public InteractInConversation<E> particles(ParticleOptions... particles) {
        this.particles = List.of(particles);
        return this;
    }

    public InteractInConversation<E> setInteractionPredicate(BiPredicate<E, LivingEntity> interactionPredicate) {
        this.interactionPredicate = interactionPredicate;
        return this;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORIES;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        this.target = BrainUtils.getMemory(entity, MemoryModuleType.INTERACTION_TARGET);
        return this.target != null && this.target.isAlive() && this.interactionPredicate.test(entity, this.target);
    }

    @Override
    protected void start(E entity) {
        BrainUtils.setMemory(entity, MemoryModuleType.LOOK_TARGET, new EntityTracker(this.target, true));
        if (this.playEmoteChance.test(entity) && !this.particles.isEmpty()) {
            ((ServerLevel) entity.level()).sendParticles(this.particles.get(entity.getRandom().nextInt(this.particles.size())),
                    entity.getX(), entity.getY(1.2), entity.getZ(), 0, 0, 0.01, 0, 0);
        }
    }

    @Override
    protected void stop(E entity) {
        this.target = null;
    }
}
