package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerCrafting;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerInfoScreen;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerUpgrade;
import io.github.flemmli97.runecraftory.common.inventory.container.ShippingContainer;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.PlatformRegistry;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;

public class ModContainer {

    public static final PlatformRegistry<MenuType<?>> CONTAINERS = PlatformUtils.INSTANCE.of(Registry.MENU_REGISTRY, RuneCraftory.MODID);

    public static final RegistryEntrySupplier<MenuType<ContainerCrafting>> craftingContainer = CONTAINERS.register("crafting_container", () -> Platform.INSTANCE.menuType(ContainerCrafting::new));
    public static final RegistryEntrySupplier<MenuType<ContainerUpgrade>> upgradeContainer = CONTAINERS.register("upgrade_container", () -> Platform.INSTANCE.menuType(ContainerUpgrade::new));
    public static final RegistryEntrySupplier<MenuType<ContainerInfoScreen>> infoContainer = CONTAINERS.register("info_container", () -> Platform.INSTANCE.menuType((windowID, inv) -> new ContainerInfoScreen(windowID, inv, true)));
    public static final RegistryEntrySupplier<MenuType<ContainerInfoScreen>> infoSubContainer = CONTAINERS.register("info_sub_container", () -> Platform.INSTANCE.menuType((windowID, inv) -> new ContainerInfoScreen(windowID, inv, false)));
    public static final RegistryEntrySupplier<MenuType<ShippingContainer>> shippingContainer = CONTAINERS.register("shipping_container", () -> Platform.INSTANCE.menuType((windowID, inv) -> new ShippingContainer(windowID, inv)));

}
