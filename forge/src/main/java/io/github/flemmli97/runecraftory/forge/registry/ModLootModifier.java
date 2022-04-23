package io.github.flemmli97.runecraftory.forge.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.forge.loot.CropLootModifier;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.PlatformRegistry;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.registries.ForgeRegistries;

public class ModLootModifier {

    public static final PlatformRegistry<GlobalLootModifierSerializer<?>> SERIALZER = PlatformUtils.INSTANCE.of(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, RuneCraftory.MODID);

    public static final RegistryEntrySupplier<CropLootModifier.Serializer> crop = SERIALZER.register("crop_modifier", CropLootModifier.Serializer::new);

}
