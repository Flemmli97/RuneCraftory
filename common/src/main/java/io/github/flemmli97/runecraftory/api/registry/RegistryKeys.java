package io.github.flemmli97.runecraftory.api.registry;

import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryArmorEffects;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttackActions;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCLooks;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCProfessions;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class RegistryKeys {

    public static final ResourceKey<? extends Registry<ArmorEffect>> ARMOR_EFFECT_KEY = RuneCraftoryArmorEffects.ARMOR_EFFECT_KEY;
    public static final ResourceKey<? extends Registry<AttackAction>> ATTACK_ACTION_KEY = RuneCraftoryAttackActions.ATTACK_ACTION_KEY;
    public static final ResourceKey<? extends Registry<NPCFeatureType<?>>> NPC_FEATURE_REGISTRY_KEY = RuneCraftoryNPCLooks.NPC_FEATURE_REGISTRY_KEY;
    public static final ResourceKey<? extends Registry<NPCProfession>> PROFESSION_REGISTRY_KEY = RuneCraftoryNPCProfessions.PROFESSION_REGISTRY_KEY;
}
