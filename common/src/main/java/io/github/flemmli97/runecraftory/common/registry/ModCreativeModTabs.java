package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ModCreativeModTabs {

    private static final Map<ResourceLocation, List<Supplier<ItemStack>>> CONTENTS = new HashMap<>();

    public static final LoaderRegister<CreativeModeTab> CREATIVE_MODE_TABS = LoaderRegistryAccess.INSTANCE.of(Registries.CREATIVE_MODE_TAB, RuneCraftory.MODID);

    public static final RegistryEntrySupplier<CreativeModeTab, CreativeModeTab> WEAPON_TOOL_TAB = register("weapons_tools", () -> ModItems.SHORT_DAGGER);
    public static final RegistryEntrySupplier<CreativeModeTab, CreativeModeTab> EQUIPMENT = register("equipment", () -> ModItems.CHEAP_BRACELET);
    public static final RegistryEntrySupplier<CreativeModeTab, CreativeModeTab> UPGRADE_ITEMS = register("upgrade", () -> ModItems.DRAGONIC);
    public static final RegistryEntrySupplier<CreativeModeTab, CreativeModeTab> BLOCKS = register("blocks", () -> ModItems.MINERAL_IRON);
    public static final RegistryEntrySupplier<CreativeModeTab, CreativeModeTab> MEDICINE = register("medicine", () -> ModItems.RECOVERY_POTION);
    public static final RegistryEntrySupplier<CreativeModeTab, CreativeModeTab> SPELLS = register("spells", () -> ModItems.TELEPORT);
    public static final RegistryEntrySupplier<CreativeModeTab, CreativeModeTab> FOOD = register("food", () -> ModItems.ONIGIRI);
    public static final RegistryEntrySupplier<CreativeModeTab, CreativeModeTab> CROPS = register("crops", () -> ModItems.TURNIP_SEEDS);
    public static final RegistryEntrySupplier<CreativeModeTab, CreativeModeTab> MONSTERS = register("monsters", () -> ModItems.ICON_0);

    public static synchronized void appendTo(ResourceLocation tab, RegistryEntrySupplier<Item, ?> entry) {
        CONTENTS.computeIfAbsent(tab, k -> new ArrayList<>())
                .add(() -> new ItemStack(entry.get()));
    }

    private static RegistryEntrySupplier<CreativeModeTab, CreativeModeTab> register(String name, Supplier<Supplier<? extends Item>> item) {
        ResourceLocation id = RuneCraftory.modRes(name);
        return CREATIVE_MODE_TABS.register(name, () -> Platform.INSTANCE.tabBuilder()
                .icon(() -> item.get().get().getDefaultInstance())
                .title(Component.translatable("itemGroup." + RuneCraftory.MODID + "." + name))
                .displayItems((params, output) -> CONTENTS.get(id).forEach(s -> output.accept(s.get())))
                .build());
    }
}
