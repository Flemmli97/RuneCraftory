package com.flemmli97.runecraftory.common.registry;

import com.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModActivities {

    public static DeferredRegister<Activity> ACTIVITIES = DeferredRegister.create(ForgeRegistries.ACTIVITIES, RuneCraftory.MODID);

    public static RegistryObject<Activity> DISABLED = ACTIVITIES.register("disabled", () -> new Activity("disabled"));
}
