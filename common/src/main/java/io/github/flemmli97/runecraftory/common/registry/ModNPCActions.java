package io.github.flemmli97.runecraftory.common.registry;

import com.mojang.serialization.Codec;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.registry.NPCAction;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.AttackMeleeAction;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.DoNothingAction;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.FoodThrowAction;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.PartyTargetAction;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.RunAwayAction;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.RunToLeadAction;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.SpellAttackAction;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.WalkAroundAction;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class ModNPCActions {

    public static final ResourceKey<? extends Registry<Codec<? extends NPCAction>>> ACTIONS_REGISTRY_KEY = ResourceKey.createRegistryKey(RuneCraftory.modRes("npc_actions"));
    public static final LoaderRegistryAccess.CustomLoaderRegistry<Codec<? extends NPCAction>> ACTIONS = LoaderRegistryAccess.INSTANCE.newRegistry(ACTIONS_REGISTRY_KEY,
            RuneCraftory.modRes("do_nothing"), true, false);

    public static final RegistryEntrySupplier<Codec<? extends NPCAction>, Codec<DoNothingAction>> DO_NOTHING_ACTION = register("do_nothing", DoNothingAction.CODEC);
    public static final RegistryEntrySupplier<Codec<? extends NPCAction>, Codec<AttackMeleeAction>> MELEE_ATTACK = register("melee_attack", AttackMeleeAction.CODEC);
    public static final RegistryEntrySupplier<Codec<? extends NPCAction>, Codec<SpellAttackAction>> SPELL_ATTACK = register("spell_attack", SpellAttackAction.CODEC);
    public static final RegistryEntrySupplier<Codec<? extends NPCAction>, Codec<PartyTargetAction>> PARTY_TARGET_ACTION = register("party_target_action", PartyTargetAction.CODEC);
    public static final RegistryEntrySupplier<Codec<? extends NPCAction>, Codec<FoodThrowAction>> FOOD_THROW_ACTION = register("food_throw_action", FoodThrowAction.CODEC);
    public static final RegistryEntrySupplier<Codec<? extends NPCAction>, Codec<RunAwayAction>> RUN_AWAY_ACTION = register("run_away", RunAwayAction.CODEC);
    public static final RegistryEntrySupplier<Codec<? extends NPCAction>, Codec<WalkAroundAction>> WALK_AROUND_ACTION = register("walk_around", WalkAroundAction.CODEC);
    public static final RegistryEntrySupplier<Codec<? extends NPCAction>, Codec<RunToLeadAction>> RUN_TO_LEADER = register("run_to_leader", RunToLeadAction.CODEC);

    private static <T extends NPCAction> RegistryEntrySupplier<Codec<? extends NPCAction>, Codec<T>> register(String name, Codec<T> codec) {
        return ACTIONS.register().register(name, () -> codec);
    }
}
