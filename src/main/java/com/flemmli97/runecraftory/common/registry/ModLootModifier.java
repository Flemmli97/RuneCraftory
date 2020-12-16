package com.flemmli97.runecraftory.common.registry;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.loot.CropLootModifier;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModLootModifier {

    public static final DeferredRegister<GlobalLootModifierSerializer<?>> SERIALZER = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, RuneCraftory.MODID);

    public static final RegistryObject<CropLootModifier.Serializer> crop = SERIALZER.register("crop_modifier", ()->new CropLootModifier.Serializer());
}
