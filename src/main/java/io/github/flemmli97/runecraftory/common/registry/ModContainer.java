package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerCrafting;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerInfoScreen;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerUpgrade;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainer {

    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, RuneCraftory.MODID);

    public static final RegistryObject<ContainerType<ContainerCrafting>> craftingContainer = CONTAINERS.register("crafting_container", () -> IForgeContainerType.create(ContainerCrafting::new));
    public static final RegistryObject<ContainerType<ContainerUpgrade>> upgradeContainer = CONTAINERS.register("upgrade_container", () -> IForgeContainerType.create(ContainerUpgrade::new));
    public static final RegistryObject<ContainerType<ContainerInfoScreen>> infoContainer = CONTAINERS.register("info_container", () -> new ContainerType<>((windowID, inv) -> new ContainerInfoScreen(windowID, inv, true)));
    public static final RegistryObject<ContainerType<ContainerInfoScreen>> infoSubContainer = CONTAINERS.register("info_sub_container", () -> new ContainerType<>((windowID, inv) -> new ContainerInfoScreen(windowID, inv, false)));

}
