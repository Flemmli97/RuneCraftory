package com.flemmli97.runecraftory.common.registry;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.inventory.container.ContainerCrafting;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainer {

    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, RuneCraftory.MODID);

    public static final RegistryObject<ContainerType<ContainerCrafting>> craftingContainer = CONTAINERS.register("crafting_container", ()-> new ContainerType<>(ContainerCrafting::new));

}
