package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.BlushFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.HairFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.IndexedColorSettingType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.OutfitFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.SimpleHatFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.SizeFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.SlimLookFeatureType;
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
    public static final RegistryEntrySupplier<NPCFeatureType<SlimLookFeatureType.SlimLookFeature>> SLIM = NPC_FEATURES.register("slim_feature", () -> new NPCFeatureType<>(SlimLookFeatureType.CODEC, b -> SlimLookFeatureType.FEATURE,
            t -> SlimLookFeatureType.FEATURE));
    public static final RegistryEntrySupplier<NPCFeatureType<SizeFeatureType.SizeFeature>> SIZE = NPC_FEATURES.register("size_feature", () -> new NPCFeatureType<>(SizeFeatureType.CODEC, SizeFeatureType.SizeFeature::new, SizeFeatureType.SizeFeature::new));

    public static final RegistryEntrySupplier<NPCFeatureType<IndexedColorSettingType.IndexedColorFeature>> SKIN = NPC_FEATURES.register("skin_feature", ModNPCLooks::skin);
    public static final RegistryEntrySupplier<NPCFeatureType<IndexedColorSettingType.IndexedColorFeature>> IRIS = NPC_FEATURES.register("iris_feature", ModNPCLooks::iris);
    public static final RegistryEntrySupplier<NPCFeatureType<IndexedColorSettingType.IndexedColorFeature>> SCLERA = NPC_FEATURES.register("sclera_feature", ModNPCLooks::sclera);
    public static final RegistryEntrySupplier<NPCFeatureType<IndexedColorSettingType.IndexedColorFeature>> EYEBROWS = NPC_FEATURES.register("eyebrows_feature", ModNPCLooks::eyebrows);
    public static final RegistryEntrySupplier<NPCFeatureType<BlushFeatureType.BlushFeature>> BLUSH = NPC_FEATURES.register("blush_feature", () -> new NPCFeatureType<>(BlushFeatureType.CODEC, BlushFeatureType.BlushFeature::new, BlushFeatureType.BlushFeature::new));
    public static final RegistryEntrySupplier<NPCFeatureType<HairFeatureType.HairFeature>> HAIR = NPC_FEATURES.register("hair_feature", () -> new NPCFeatureType<>(HairFeatureType.CODEC, HairFeatureType.HairFeature::new, HairFeatureType.HairFeature::new));
    public static final RegistryEntrySupplier<NPCFeatureType<OutfitFeatureType.OutfitFeature>> OUTFIT = NPC_FEATURES.register("outfit_feature", () -> new NPCFeatureType<>(OutfitFeatureType.CODEC, OutfitFeatureType.OutfitFeature::new, OutfitFeatureType.OutfitFeature::new));
    public static final RegistryEntrySupplier<NPCFeatureType<SimpleHatFeatureType.SimpleHatFeature>> HAT = NPC_FEATURES.register("simple_hat_feature", () -> new NPCFeatureType<>(SimpleHatFeatureType.CODEC, SimpleHatFeatureType.SimpleHatFeature::new, SimpleHatFeatureType.SimpleHatFeature::new));

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static PlatformRegistry<NPCFeatureType<?>> createRegistry() {
        return PlatformUtils.INSTANCE.customRegistry(NPCFeatureType.class, (ResourceKey) NPC_FEATURE_REGISTRY_KEY, new ResourceLocation(RuneCraftory.MODID, "slim_feature"), true, true);
    }

    private static NPCFeatureType<IndexedColorSettingType.IndexedColorFeature> skin() {
        return IndexedColorSettingType.createSimple(SKIN);
    }

    private static NPCFeatureType<IndexedColorSettingType.IndexedColorFeature> iris() {
        return IndexedColorSettingType.createSimple(IRIS);
    }

    private static NPCFeatureType<IndexedColorSettingType.IndexedColorFeature> sclera() {
        return IndexedColorSettingType.createSimple(SCLERA);
    }

    private static NPCFeatureType<IndexedColorSettingType.IndexedColorFeature> eyebrows() {
        return IndexedColorSettingType.createSimple(EYEBROWS);
    }
}
