package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerCrafting;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerInfoScreen;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerShop;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerUpgrade;
import io.github.flemmli97.runecraftory.common.inventory.container.ShippingContainer;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Function;

public class RuneCraftoryMenuTypes {

    public static final LoaderRegister<MenuType<?>> CONTAINERS = LoaderRegistryAccess.INSTANCE.of(Registries.MENU, RuneCraftory.MODID);

    public static final RegistryEntrySupplier<MenuType<?>, MenuType<ContainerInfoScreen>> INFO_CONTAINER = CONTAINERS.register("info_container", () -> Platform.INSTANCE.menuType((windowID, inv) -> new ContainerInfoScreen(windowID, inv, true)));
    public static final RegistryEntrySupplier<MenuType<?>, MenuType<ContainerInfoScreen>> INFO_SUB_CONTAINER = CONTAINERS.register("info_sub_container", () -> Platform.INSTANCE.menuType((windowID, inv) -> new ContainerInfoScreen(windowID, inv, false)));
    public static final RegistryEntrySupplier<MenuType<?>, MenuType<ContainerCrafting>> CRAFTING_CONTAINER = CONTAINERS.register("crafting_container", () -> Platform.INSTANCE.menuType(ContainerCrafting::new, of(BlockPos.STREAM_CODEC)));
    public static final RegistryEntrySupplier<MenuType<?>, MenuType<ShippingContainer>> SHIPPING_CONTAINER = CONTAINERS.register("shipping_container", () -> Platform.INSTANCE.menuType(ShippingContainer::new));
    public static final RegistryEntrySupplier<MenuType<?>, MenuType<ContainerUpgrade>> UPGRADE_CONTAINER = CONTAINERS.register("upgrade_container", () -> Platform.INSTANCE.menuType(ContainerUpgrade::new, of(BlockPos.STREAM_CODEC)));
    public static final RegistryEntrySupplier<MenuType<?>, MenuType<ContainerShop>> SHOP_CONTAINER = CONTAINERS.register("shop_container", () -> Platform.INSTANCE.menuType(ContainerShop::new, ContainerShop.DATA_STREAM_CODEC));

    /**
     * Cause generics...
     */
    private static <D> StreamCodec<RegistryFriendlyByteBuf, D> of(StreamCodec<ByteBuf, D> codec) {
        return StreamCodec.composite(codec, Function.identity(), Function.identity());
    }
}
