package io.github.flemmli97.runecraftory.common.registry;

import com.mojang.serialization.Codec;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.npc.features.NPCFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.SlimLookFeature;
import io.github.flemmli97.runecraftory.platform.LazyGetter;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.PlatformRegistry;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import io.github.flemmli97.tenshilib.platform.registry.SimpleRegistryWrapper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class ModNPCLooks {

    public static final ResourceKey<? extends Registry<NPCFeatureType<?>>> NPC_FEATURE_REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(RuneCraftory.MODID, "npc_features"));
    public static final Supplier<SimpleRegistryWrapper<NPCFeatureType<?>>> NPC_FEATURE_REGISTRY = new LazyGetter<>(() -> PlatformUtils.INSTANCE.registry(NPC_FEATURE_REGISTRY_KEY));
    public static final PlatformRegistry<NPCFeatureType<?>> NPC_FEATURES = createRegistry();
    public static final RegistryEntrySupplier<NPCFeatureType<SlimLookFeature>> SLIM = NPC_FEATURES.register("slim_feature", () -> new NPCFeatureType<>(b -> new SlimLookFeature(), Codec.unit(new SlimLookFeature())));

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static PlatformRegistry<NPCFeatureType<?>> createRegistry() {
        return PlatformUtils.INSTANCE.customRegistry(NPCFeatureType.class, (ResourceKey) NPC_FEATURE_REGISTRY_KEY, new ResourceLocation(RuneCraftory.MODID, "slim_feature"), true, true);
    }

}
