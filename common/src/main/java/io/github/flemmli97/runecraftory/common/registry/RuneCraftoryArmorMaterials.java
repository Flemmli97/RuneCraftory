package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.Map;

public class RuneCraftoryArmorMaterials {

    public static final LoaderRegister<ArmorMaterial> MATERIALS = LoaderRegistryAccess.INSTANCE.of(Registries.ARMOR_MATERIAL, RuneCraftory.MODID);

    public static final RegistryEntrySupplier<ArmorMaterial, ArmorMaterial> GENERIC_MATERIAL = MATERIALS.register("generic", () ->
            new ArmorMaterial(Map.of(), 0, SoundEvents.ARMOR_EQUIP_LEATHER, () -> Ingredient.EMPTY,
                    List.of(new ArmorMaterial.Layer(RuneCraftory.modRes("dynamic_armor_texture"))), 0, 0));
}
