package io.github.flemmli97.runecraftory.common.entities.ai.behaviour.actions;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.registry.NPCAction;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModNPCActions;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NPCAttackActions {

    public static final Codec<NPCAttackActions> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(WeightedAction.CODEC.listOf().fieldOf("actions").forGetter(d -> d.actions)
            ).apply(instance, NPCAttackActions::new));

    public static final NPCAttackActions DEFAULT = new NPCAttackActions(List.of());

    private final List<WeightedAction> actions;

    public NPCAttackActions(List<WeightedAction> chainedActions) {
        this.actions = ImmutableList.copyOf(chainedActions);
    }

    public List<NPCAction> getAction(EntityNPCBase npc) {
//        List<WeightedAction> list = this.actions.stream().filter(w -> w.predicate.matches((ServerLevel) npc.level, npc.position(), npc)).toList();
//        if (list.isEmpty())
        return List.of();
//        return WeightedRandom.getRandomItem(npc.getRandom(), list)
//                .map(w -> w.chainedActions).orElse(List.of());
    }

    public boolean isEmpty() {
        return this.actions.isEmpty();
    }

    public static class WeightedAction implements WeightedEntry {

        public static final Codec<WeightedAction> CODEC = RecordCodecBuilder.create((instance) ->
                instance.group(ExtraCodecs.POSITIVE_INT.fieldOf("weight").forGetter(d -> d.weight.asInt()),
                        EntityPredicate.CODEC.optionalFieldOf("predicate").forGetter(d -> d.predicate),
                        ModNPCActions.ACTIONS.registry().byNameCodec().<NPCAction>dispatchStable(NPCAction::codec, c -> c)
                                .listOf().fieldOf("concurrent_actions").forGetter(d -> d.chainedActions)
                ).apply(instance, WeightedAction::new));

        private final Weight weight;
        private final Optional<EntityPredicate> predicate;
        private final List<NPCAction> chainedActions;

        public WeightedAction(int weight, Optional<EntityPredicate> predicate, List<NPCAction> chainedActions) {
            this.weight = Weight.of(weight);
            this.predicate = predicate;
            this.chainedActions = chainedActions;
        }

        @Override
        public Weight getWeight() {
            return this.weight;
        }
    }

    public static class Builder {

        private final List<WeightedAction> chainedActions = new ArrayList<>();

        public Builder addAction(ActionBuilder builder) {
            this.chainedActions.add(builder.build());
            return this;
        }

        public NPCAttackActions build() {
            return new NPCAttackActions(this.chainedActions);
        }
    }

    public static class ActionBuilder {

        private final int weight;
        private Optional<EntityPredicate> predicate = Optional.empty();
        private final List<NPCAction> chainedActions = new ArrayList<>();

        public ActionBuilder(int weight) {
            this.weight = weight;
        }

        public ActionBuilder predicate(EntityPredicate predicate) {
            this.predicate = Optional.ofNullable(predicate);
            return this;
        }

        public ActionBuilder action(NPCAction action) {
            this.chainedActions.add(action);
            return this;
        }

        public WeightedAction build() {
            return new WeightedAction(this.weight, this.predicate, this.chainedActions);
        }
    }
}
