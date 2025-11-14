package io.github.flemmli97.runecraftory.common.entities.ai.behaviour.npc;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.datapack.ReloadableHolder;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.DummyBehaviour;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.SequentialBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.InvalidateMemory;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class NPCAttackActions {

    public static final Codec<NPCAttackActions> CODEC = NPCAttackSequence.CODEC.listOf().xmap(NPCAttackActions::new, h -> h.behaviours);
    public static final ReloadableHolder<NPCAttackActions> DEFAULT = new ReloadableHolder<>(RuneCraftory.modRes("default_action"),
            new NPCAttackActions(List.of()));

    private final List<NPCAttackSequence> behaviours;

    public NPCAttackActions(List<NPCAttackSequence> behaviours) {
        this.behaviours = behaviours;
    }

    public static LootContext createLootContext(NPCEntity npc) {
        LootParams.Builder builder = new LootParams.Builder((ServerLevel) npc.level())
                .withParameter(LootContextParams.THIS_ENTITY, npc).withParameter(LootContextParams.ORIGIN, npc.position());
        return new LootContext.Builder(builder.create(LootContextParamSets.ADVANCEMENT_ENTITY)).withOptionalRandomSource(npc.getRandom())
                .create(Optional.empty());
    }

    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings("unchecked")
    public ExtendedBehaviour<NPCEntity> create() {
        if (this.behaviours.isEmpty())
            return new InvalidateMemory<>(MemoryModuleType.ATTACK_TARGET);
        if (this.behaviours.size() == 1) {
            return this.behaviours.getFirst().create()
                    .startCondition(npc -> !npc.getAnimationHandler().hasAnimation() && !BrainUtils.hasMemory(npc, MemoryModuleType.ATTACK_COOLING_DOWN))
                    .stopIf(npc -> BrainUtils.hasMemory(npc, MemoryModuleType.ATTACK_COOLING_DOWN));
        }
        return new OneRandomBehaviour<NPCEntity>(this.behaviours.stream()
                .map(seq -> Pair.of(seq.create(), seq.weight())).toArray(Pair[]::new))
                .startCondition(npc -> !npc.getAnimationHandler().hasAnimation() && !BrainUtils.hasMemory(npc, MemoryModuleType.ATTACK_COOLING_DOWN))
                .stopIf(npc -> BrainUtils.hasMemory(npc, MemoryModuleType.ATTACK_COOLING_DOWN));
    }

    public record NPCAttackSequence(List<SerializableBehaviour.SerializabledBehaviourHolder<?>> sequence,
                                    Optional<EntityPredicate> predicate,
                                    NumberProvider timeout, NumberProvider cooldown, int weight) {

        public static final Codec<NPCAttackSequence> CODEC = RecordCodecBuilder.create((instance) ->
                instance.group(SerializableBehaviour.SerializabledBehaviourHolder.CODEC.listOf().fieldOf("sequence").forGetter(NPCAttackSequence::sequence),
                        EntityPredicate.CODEC.optionalFieldOf("predicate").forGetter(NPCAttackSequence::predicate),
                        NumberProviders.CODEC.fieldOf("time_out").forGetter(NPCAttackSequence::timeout),
                        NumberProviders.CODEC.fieldOf("cooldown").forGetter(NPCAttackSequence::cooldown),
                        ExtraCodecs.POSITIVE_INT.fieldOf("weight").forGetter(NPCAttackSequence::weight)
                ).apply(instance, NPCAttackSequence::new));

        @SuppressWarnings("unchecked")
        public ExtendedBehaviour<NPCEntity> create() {
            List<ExtendedBehaviour<NPCEntity>> behaviours = new ArrayList<>();
            ImmutableList.Builder<Predicate<NPCEntity>> builder = ImmutableList.builder();
            this.sequence.forEach(seq -> behaviours.addAll(seq.create(builder::add)));
            behaviours.add(DummyBehaviour.opt(new DoNPCAttackAction()));
            SequentialBehaviour<NPCEntity> seq = new SequentialBehaviour<>(behaviours.toArray(ExtendedBehaviour[]::new));
            this.predicate().ifPresent(pred -> builder.add(npc -> pred.matches((ServerLevel) npc.level(), npc.position(), npc)));
            List<Predicate<NPCEntity>> predicates = builder.build();
            if (!predicates.isEmpty()) {
                if (predicates.size() == 1) {
                    seq.startCondition(predicates.getFirst());
                } else {
                    seq.startCondition(entity -> {
                        for (Predicate<NPCEntity> pred : predicates) {
                            if (!pred.test(entity))
                                return false;
                        }
                        return true;
                    });
                }
            }
            seq.runFor(npc -> this.timeout().getInt(createLootContext(npc)));
            seq.whenStopping(npc -> BrainUtils.setForgettableMemory(npc, MemoryModuleType.ATTACK_COOLING_DOWN, true, this.cooldown().getInt(createLootContext(npc))));
            return seq;
        }
    }

    public static class Builder {

        private final List<NPCAttackSequence> behaviours = new ArrayList<>();

        public SequenceBuilder addSequence(int weight) {
            return new SequenceBuilder(weight);
        }

        public NPCAttackActions build() {
            return new NPCAttackActions(this.behaviours);
        }

        public class SequenceBuilder {

            private final int weight;
            private EntityPredicate predicate = null;
            private NumberProvider timeout = ConstantValue.exactly(20);
            private NumberProvider cooldown = ConstantValue.exactly(20);

            private final List<SerializableBehaviour.SerializabledBehaviourHolder<?>> sequence = new ArrayList<>();

            public SequenceBuilder(int weight) {
                this.weight = weight;
            }

            public SequenceBuilder predicate(EntityPredicate.Builder predicate) {
                this.predicate = predicate.build();
                return this;
            }

            public SequenceBuilder timeout(NumberProvider timeout) {
                this.timeout = timeout;
                return this;
            }

            public SequenceBuilder cooldown(NumberProvider cooldown) {
                this.cooldown = cooldown;
                return this;
            }

            public SequenceBuilder add(SerializableBehaviour.SerializabledBehaviourHolder<?> behaviour) {
                this.sequence.add(behaviour);
                return this;
            }

            public Builder end() {
                NPCAttackSequence seq = new NPCAttackSequence(this.sequence, Optional.ofNullable(this.predicate),
                        this.timeout, this.cooldown, this.weight);
                Builder.this.behaviours.add(seq);
                return Builder.this;
            }
        }
    }
}
