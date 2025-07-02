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

    public static final RegistryEntrySupplier<CreativeModeTab, CreativeModeTab> WEAPON_TOOL_TAB = register("weapons_and_tools", () -> ModItems.SHORT_DAGGER);
    public static final RegistryEntrySupplier<CreativeModeTab, CreativeModeTab> EQUIPMENT = register("equipment", () -> ModItems.CHEAP_BRACELET, WEAPON_TOOL_TAB.getID());
    public static final RegistryEntrySupplier<CreativeModeTab, CreativeModeTab> MATERIALS = register("materials", () -> ModItems.DRAGONIC, EQUIPMENT.getID());
    public static final RegistryEntrySupplier<CreativeModeTab, CreativeModeTab> BLOCKS = register("blocks", () -> ModItems.MINERAL_IRON, MATERIALS.getID());
    public static final RegistryEntrySupplier<CreativeModeTab, CreativeModeTab> MEDICINE = register("medicine", () -> ModItems.RECOVERY_POTION, BLOCKS.getID());
    public static final RegistryEntrySupplier<CreativeModeTab, CreativeModeTab> SPELLS = register("spells_and_rune_abilities", () -> ModItems.TELEPORT, MEDICINE.getID());
    public static final RegistryEntrySupplier<CreativeModeTab, CreativeModeTab> FOOD = register("food", () -> ModItems.ONIGIRI, SPELLS.getID());
    public static final RegistryEntrySupplier<CreativeModeTab, CreativeModeTab> FARMING = register("farming", () -> ModItems.TURNIP_SEEDS, FOOD.getID());
    public static final RegistryEntrySupplier<CreativeModeTab, CreativeModeTab> MONSTERS = register("monsters", () -> ModItems.ICON_0, FARMING.getID());

    public static synchronized void appendTo(ResourceLocation tab, RegistryEntrySupplier<Item, ?> entry) {
        CONTENTS.computeIfAbsent(tab, k -> new ArrayList<>())
                .add(() -> new ItemStack(entry.get()));
    }

    private static RegistryEntrySupplier<CreativeModeTab, CreativeModeTab> register(String name, Supplier<Supplier<? extends Item>> item, ResourceLocation... after) {
        ResourceLocation id = RuneCraftory.modRes(name);
        return CREATIVE_MODE_TABS.register(name, () -> Platform.INSTANCE.tabBuilder(after)
                .icon(() -> item.get().get().getDefaultInstance())
                .title(Component.translatable("itemGroup." + RuneCraftory.MODID + "." + name))
                .displayItems((params, output) -> CONTENTS.get(id).forEach(s -> output.accept(s.get())))
                .build());
    }
}
