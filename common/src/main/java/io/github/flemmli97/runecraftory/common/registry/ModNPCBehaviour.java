package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.npc.SerializableBehaviour;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.npc.SerializableBehaviours;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Unit;

public class ModNPCBehaviour {

    public static final ResourceKey<? extends Registry<SerializableBehaviour<?>>> NPC_BEHAVIOUR_KEY = ResourceKey.createRegistryKey(RuneCraftory.modRes("npc_behaviour"));
    public static final LoaderRegistryAccess.CustomLoaderRegistry<SerializableBehaviour<?>> BEHAVIOURS = LoaderRegistryAccess.INSTANCE.newRegistry(NPC_BEHAVIOUR_KEY,
            RuneCraftory.modRes("idle"), true, false);

    public static final RegistryEntrySupplier<SerializableBehaviour<?>, SerializableBehaviour<Unit>> IDLE = register("idle", SerializableBehaviours.IDLE);
    public static final RegistryEntrySupplier<SerializableBehaviour<?>, SerializableBehaviour<SerializableBehaviours.WalkToData>> WALK_TO = register("walk_to", SerializableBehaviours.WALK_TO);
    public static final RegistryEntrySupplier<SerializableBehaviour<?>, SerializableBehaviour<SerializableBehaviours.WalkAwayData>> WALK_AWAY = register("walk_away", SerializableBehaviours.WALK_AWAY);
    public static final RegistryEntrySupplier<SerializableBehaviour<?>, SerializableBehaviour<SerializableBehaviours.KeepDistanceData>> KEEP_DISTANCE = register("keep_distance", SerializableBehaviours.KEEP_DISTANCE);
    public static final RegistryEntrySupplier<SerializableBehaviour<?>, SerializableBehaviour<SerializableBehaviours.RandomWalkData>> RANDOM_WALK = register("random_walk", SerializableBehaviours.RANDOM_WALK);
    public static final RegistryEntrySupplier<SerializableBehaviour<?>, SerializableBehaviour<SerializableBehaviours.WalkToData>> WALK_TO_FOLLOW = register("walk_to_follow", SerializableBehaviours.WALK_TO_FOLLOW);
    public static final RegistryEntrySupplier<SerializableBehaviour<?>, SerializableBehaviour<Unit>> LOOK_AT_FOLLOW = register("look_at_follow", SerializableBehaviours.LOOK_AT_FOLLOW);
    public static final RegistryEntrySupplier<SerializableBehaviour<?>, SerializableBehaviour<Unit>> ATTACK_WITH_WEAPON = register("attack_with_weapon", SerializableBehaviours.ATTACK_WITH_WEAPON);
    public static final RegistryEntrySupplier<SerializableBehaviour<?>, SerializableBehaviour<SerializableBehaviours.SpellAttackData>> ATTACK_WITH_SPELL = register("attack_with_spell", SerializableBehaviours.ATTACK_WITH_SPELL);
    public static final RegistryEntrySupplier<SerializableBehaviour<?>, SerializableBehaviour<SerializableBehaviours.ItemThrowData>> THROW_ITEM = register("throw_item", SerializableBehaviours.THROW_ITEM);

    private static <T> RegistryEntrySupplier<SerializableBehaviour<?>, SerializableBehaviour<T>> register(String name, SerializableBehaviour<T> behaviour) {
        return BEHAVIOURS.register().register(name, () -> behaviour);
    }
}
