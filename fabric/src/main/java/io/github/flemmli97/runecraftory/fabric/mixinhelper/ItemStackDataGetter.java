package io.github.flemmli97.runecraftory.fabric.mixinhelper;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.components.ArmorEffectData;
import io.github.flemmli97.runecraftory.common.components.StaffData;
import net.minecraft.resources.ResourceLocation;

public interface ItemStackDataGetter {

    ResourceLocation STAFF_ID = RuneCraftory.modRes("staff");

    ResourceLocation ARMOR_EFFECT_ID = RuneCraftory.modRes("armor_effects");

    StaffData runecraftory$getStaffData();

    ArmorEffectData runecraftory$getArmorEffectData();
}
