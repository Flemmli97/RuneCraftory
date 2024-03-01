package io.github.flemmli97.runecraftory.fabric.mixinhelper;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.attachment.ArmorEffectData;
import io.github.flemmli97.runecraftory.common.attachment.StaffData;
import net.minecraft.resources.ResourceLocation;

public interface ItemStackDataGetter {

    ResourceLocation STAFF_ID = new ResourceLocation(RuneCraftory.MODID, "staff");

    ResourceLocation ARMOR_EFFECT_ID = new ResourceLocation(RuneCraftory.MODID, "armor_effects");

    StaffData getStaffData();

    ArmorEffectData getArmorEffectData();
}
