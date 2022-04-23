package io.github.flemmli97.runecraftory.forge.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.forge.loot.CropLootModifier;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModLootModifier {

    public static final DeferredRegister<GlobalLootModifierSerializer<?>> SERIALZER = DeferredRegister.create(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, RuneCraftory.MODID);

    public static final RegistryObject<CropLootModifier.Serializer> crop = SERIALZER.register("crop_modifier", CropLootModifier.Serializer::new);

}
