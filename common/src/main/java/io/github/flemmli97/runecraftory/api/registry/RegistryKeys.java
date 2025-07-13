package io.github.flemmli97.runecraftory.api.registry;

import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.common.registry.ModArmorEffects;
import io.github.flemmli97.runecraftory.common.registry.ModAttackActions;
import io.github.flemmli97.runecraftory.common.registry.ModNPCLooks;
import io.github.flemmli97.runecraftory.common.registry.ModNPCProfessions;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class RegistryKeys {

    public static final ResourceKey<? extends Registry<ArmorEffect>> ARMOR_EFFECT_KEY = ModArmorEffects.ARMOR_EFFECT_KEY;
    public static final ResourceKey<? extends Registry<AttackAction>> ATTACK_ACTION_KEY = ModAttackActions.ATTACK_ACTION_KEY;
    public static final ResourceKey<? extends Registry<NPCFeatureType<?>>> NPC_FEATURE_REGISTRY_KEY = ModNPCLooks.NPC_FEATURE_REGISTRY_KEY;
    public static final ResourceKey<? extends Registry<NPCProfession>> PROFESSION_REGISTRY_KEY = ModNPCProfessions.PROFESSION_REGISTRY_KEY;
}
