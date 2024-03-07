package io.github.flemmli97.runecraftory.common.registry;

import com.mojang.serialization.Codec;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.AttackMeleeAction;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.DoNothingAction;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.FoodThrowAction;
import io.github.flemmli97.runecraftory.api.registry.NPCAction;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.PartyTargetAction;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.RunAwayAction;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.RunToLeadAction;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.SpellAttackAction;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.WalkAroundAction;
import io.github.flemmli97.runecraftory.platform.LazyGetter;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.PlatformRegistry;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import io.github.flemmli97.tenshilib.platform.registry.SimpleRegistryWrapper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class ModNPCActions {

    public static final ResourceKey<? extends Registry<NPCAction.NPCActionCodec>> ACTIONS_REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(RuneCraftory.MODID, "npc_actions"));
    public static final Supplier<SimpleRegistryWrapper<NPCAction.NPCActionCodec>> ACTIONS_REGISTRY = new LazyGetter<>(() -> PlatformUtils.INSTANCE.registry(ACTIONS_REGISTRY_KEY));

    public static final PlatformRegistry<NPCAction.NPCActionCodec> ACTIONS = PlatformUtils.INSTANCE
            .customRegistry(NPCAction.NPCActionCodec.class, ACTIONS_REGISTRY_KEY, new ResourceLocation(RuneCraftory.MODID, "do_nothing"), true, false);

    public static final RegistryEntrySupplier<NPCAction.NPCActionCodec> DO_NOTHING_ACTION = register("do_nothing", DoNothingAction.CODEC);
    public static final RegistryEntrySupplier<NPCAction.NPCActionCodec> MELEE_ATTACK = register("melee_attack", AttackMeleeAction.CODEC);
    public static final RegistryEntrySupplier<NPCAction.NPCActionCodec> SPELL_ATTACK = register("spell_attack", SpellAttackAction.CODEC);
    public static final RegistryEntrySupplier<NPCAction.NPCActionCodec> PARTY_TARGET_ACTION = register("party_target_action", PartyTargetAction.CODEC);
    public static final RegistryEntrySupplier<NPCAction.NPCActionCodec> FOOD_THROW_ACTION = register("food_throw_action", FoodThrowAction.CODEC);
    public static final RegistryEntrySupplier<NPCAction.NPCActionCodec> RUN_AWAY_ACTION = register("run_away", RunAwayAction.CODEC);
    public static final RegistryEntrySupplier<NPCAction.NPCActionCodec> WALK_AROUND_ACTION = register("walk_around", WalkAroundAction.CODEC);
    public static final RegistryEntrySupplier<NPCAction.NPCActionCodec> RUN_TO_LEADER = register("run_to_leader", RunToLeadAction.CODEC);

    private static RegistryEntrySupplier<NPCAction.NPCActionCodec> register(String name, Codec<? extends NPCAction> codec) {
        return ACTIONS.register(name, () -> new NPCAction.NPCActionCodec(codec));
    }
}
