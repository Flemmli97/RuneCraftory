package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.npc.NPCAttackAction;
import io.github.flemmli97.runecraftory.common.entities.ai.memory.ConversationGroupMemory;
import io.github.flemmli97.runecraftory.common.entities.ai.memory.MeetingPoint;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

import java.util.List;
import java.util.Optional;

public class RuneCraftoryMemoryTypes {

    public static final LoaderRegister<MemoryModuleType<?>> MEMORIES = LoaderRegistryAccess.INSTANCE.of(Registries.MEMORY_MODULE_TYPE, RuneCraftory.MODID);

    public static final RegistryEntrySupplier<MemoryModuleType<?>, MemoryModuleType<String>> LAST_ANIMATION = MEMORIES.register("last_animation", () -> new MemoryModuleType<>(Optional.empty()));
    public static final RegistryEntrySupplier<MemoryModuleType<?>, MemoryModuleType<Unit>> FARMING = MEMORIES.register("farming", () -> new MemoryModuleType<>(Optional.empty()));
    public static final RegistryEntrySupplier<MemoryModuleType<?>, MemoryModuleType<Unit>> STAYING = MEMORIES.register("staying", () -> new MemoryModuleType<>(Optional.empty()));
    public static final RegistryEntrySupplier<MemoryModuleType<?>, MemoryModuleType<NPCAttackAction>> NPC_ATTACK_ACTION = MEMORIES.register("npc_attack_action", () -> new MemoryModuleType<>(Optional.empty()));
    public static final RegistryEntrySupplier<MemoryModuleType<?>, MemoryModuleType<Entity>> ITEM_THROW_TARGET = MEMORIES.register("item_throw_target", () -> new MemoryModuleType<>(Optional.empty()));
    public static final RegistryEntrySupplier<MemoryModuleType<?>, MemoryModuleType<List<NPCEntity>>> NEARBY_NPCS = MEMORIES.register("nearby_npcs", () -> new MemoryModuleType<>(Optional.empty()));
    public static final RegistryEntrySupplier<MemoryModuleType<?>, MemoryModuleType<ConversationGroupMemory>> CONVERSATION_GROUP = MEMORIES.register("conversation_group", () -> new MemoryModuleType<>(Optional.empty()));
    public static final RegistryEntrySupplier<MemoryModuleType<?>, MemoryModuleType<MeetingPoint>> MEETING_LOCATION = MEMORIES.register("meeting_location", () -> new MemoryModuleType<>(Optional.empty()));
}
