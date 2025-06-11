package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.BlushFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.FaceFeaturesType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.HairFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.IndexedColorSettingType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.OutfitFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.SimpleHatFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.SizeFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.SlimLookFeatureType;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class ModNPCLooks {

    public static final ResourceKey<? extends Registry<NPCFeatureType<?>>> NPC_FEATURE_REGISTRY_KEY = ResourceKey.createRegistryKey(RuneCraftory.modRes("npc_features"));
    public static final LoaderRegistryAccess.CustomLoaderRegistry<NPCFeatureType<?>> NPC_FEATURES = LoaderRegistryAccess.INSTANCE.newRegistry(NPC_FEATURE_REGISTRY_KEY,
            RuneCraftory.modRes("slim_feature"), true, true);

    public static final RegistryEntrySupplier<NPCFeatureType<?>, NPCFeatureType<SlimLookFeatureType.SlimLookFeature>> SLIM = NPC_FEATURES.register().register("slim_feature", () -> new NPCFeatureType<>(SlimLookFeatureType.CODEC, b -> SlimLookFeatureType.FEATURE,
            t -> SlimLookFeatureType.FEATURE));
    public static final RegistryEntrySupplier<NPCFeatureType<?>, NPCFeatureType<SizeFeatureType.SizeFeature>> SIZE = NPC_FEATURES.register().register("size_feature", () -> new NPCFeatureType<>(SizeFeatureType.CODEC, SizeFeatureType.SizeFeature::new, SizeFeatureType.SizeFeature::new));

    public static final RegistryEntrySupplier<NPCFeatureType<?>, NPCFeatureType<IndexedColorSettingType.IndexedColorFeature>> SKIN = NPC_FEATURES.register().register("skin_feature", ModNPCLooks::skin);
    public static final RegistryEntrySupplier<NPCFeatureType<?>, NPCFeatureType<FaceFeaturesType.FaceFeatures>> FACE = NPC_FEATURES.register().register("face_feature", () -> new NPCFeatureType<>(FaceFeaturesType.CODEC, FaceFeaturesType.FaceFeatures::new, FaceFeaturesType.FaceFeatures::new));
    public static final RegistryEntrySupplier<NPCFeatureType<?>, NPCFeatureType<BlushFeatureType.BlushFeature>> BLUSH = NPC_FEATURES.register().register("blush_feature", () -> new NPCFeatureType<>(BlushFeatureType.CODEC, BlushFeatureType.BlushFeature::new, BlushFeatureType.BlushFeature::new));
    public static final RegistryEntrySupplier<NPCFeatureType<?>, NPCFeatureType<HairFeatureType.HairFeature>> HAIR = NPC_FEATURES.register().register("hair_feature", () -> new NPCFeatureType<>(HairFeatureType.CODEC, HairFeatureType.HairFeature::new, HairFeatureType.HairFeature::new));
    public static final RegistryEntrySupplier<NPCFeatureType<?>, NPCFeatureType<OutfitFeatureType.OutfitFeature>> OUTFIT = NPC_FEATURES.register().register("outfit_feature", () -> new NPCFeatureType<>(OutfitFeatureType.CODEC, OutfitFeatureType.OutfitFeature::new, OutfitFeatureType.OutfitFeature::new));
    public static final RegistryEntrySupplier<NPCFeatureType<?>, NPCFeatureType<SimpleHatFeatureType.SimpleHatFeature>> HAT = NPC_FEATURES.register().register("simple_hat_feature", () -> new NPCFeatureType<>(SimpleHatFeatureType.CODEC, SimpleHatFeatureType.SimpleHatFeature::new, SimpleHatFeatureType.SimpleHatFeature::new));

    private static NPCFeatureType<IndexedColorSettingType.IndexedColorFeature> skin() {
        return IndexedColorSettingType.createSimple(SKIN);
    }
}
