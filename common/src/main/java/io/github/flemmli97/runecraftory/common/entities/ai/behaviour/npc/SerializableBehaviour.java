package io.github.flemmli97.runecraftory.common.entities.ai.behaviour.npc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public record SerializableBehaviour<T>(MapCodec<T> codec, BehaviourSequenceFactory<T> factory) {

    public MapCodec<SerializabledBehaviourHolder<T>> holderCodec() {
        return this.codec().xmap(s -> new SerializabledBehaviourHolder<>(this, s), SerializabledBehaviourHolder::data);
    }

    public SerializabledBehaviourHolder<T> of(T data) {
        return new SerializabledBehaviourHolder<>(this, data);
    }

    public record SerializabledBehaviourHolder<T>(SerializableBehaviour<T> type, T data) {

        public static final Codec<SerializabledBehaviourHolder<?>> CODEC = RuneCraftoryNPCBehaviour.BEHAVIOURS.registry().byNameCodec()
                .dispatch(SerializabledBehaviourHolder::type, SerializableBehaviour::holderCodec);

        public List<ExtendedBehaviour<NPCEntity>> create(Consumer<Predicate<NPCEntity>> predicate) {
            return this.type.factory().create(this.data(), predicate);
        }
    }

    public interface BehaviourSequenceFactory<T> {

        default List<ExtendedBehaviour<NPCEntity>> create(T data, Consumer<Predicate<NPCEntity>> predicate) {
            this.addCondition(data, predicate);
            return this.create(data);
        }

        List<ExtendedBehaviour<NPCEntity>> create(T data);

        default void addCondition(T data, Consumer<Predicate<NPCEntity>> predicate) {
        }
    }
}
