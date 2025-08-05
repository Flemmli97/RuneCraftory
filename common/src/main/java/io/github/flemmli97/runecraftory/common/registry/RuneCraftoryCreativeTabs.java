package io.github.flemmli97.runecraftory.common.registry;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.creativetab.SubTab;
import io.github.flemmli97.runecraftory.common.creativetab.SubTabContextDisplayBuilder;
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

public class RuneCraftoryCreativeTabs {

    private static final Map<ResourceLocation, List<Supplier<ItemStack>>> CONTENTS = new HashMap<>();
    private static final List<Pair<ResourceLocation, Supplier<SubTab>>> SUB_TABS = new ArrayList<>();

    public static final LoaderRegister<CreativeModeTab> CREATIVE_MODE_TABS = LoaderRegistryAccess.INSTANCE.of(Registries.CREATIVE_MODE_TAB, RuneCraftory.MODID);

    public static final ResourceLocation WEAPON_TOOL_TAB = registerSub("weapons_and_tools", () -> RuneCraftoryItems.SHORT_DAGGER);
    public static final ResourceLocation EQUIPMENT = registerSub("equipment", () -> RuneCraftoryItems.CHEAP_BRACELET);
    public static final ResourceLocation MATERIALS = registerSub("materials", () -> RuneCraftoryItems.DRAGONIC);
    public static final ResourceLocation BLOCKS = registerSub("blocks", () -> RuneCraftoryItems.MINERAL_IRON);
    public static final ResourceLocation MEDICINE = registerSub("medicine", () -> RuneCraftoryItems.RECOVERY_POTION);
    public static final ResourceLocation SPELLS = registerSub("spells_and_rune_abilities", () -> RuneCraftoryItems.TELEPORT);
    public static final ResourceLocation FOOD = registerSub("food", () -> RuneCraftoryItems.ONIGIRI);
    public static final ResourceLocation FARMING = registerSub("farming", () -> RuneCraftoryItems.TURNIP_SEEDS);
    public static final ResourceLocation MONSTERS = registerSub("spawn_eggs", () -> RuneCraftoryItems.ICON_0);

    public static final RegistryEntrySupplier<CreativeModeTab, CreativeModeTab> TAB = registerMain("creative_tab", () -> RuneCraftoryItems.ICON_0);

    public static synchronized void appendTo(ResourceLocation tab, RegistryEntrySupplier<Item, ?> entry) {
        CONTENTS.computeIfAbsent(tab, k -> new ArrayList<>())
                .add(() -> new ItemStack(entry.get()));
    }

    public static List<ResourceLocation> subTabs() {
        return SUB_TABS.stream().map(Pair::getFirst).toList();
    }

    private static RegistryEntrySupplier<CreativeModeTab, CreativeModeTab> registerMain(String name, Supplier<Supplier<? extends Item>> item) {
        return CREATIVE_MODE_TABS.register(name, () -> Platform.INSTANCE.tabBuilder(SUB_TABS.stream().map(p -> p.getSecond().get()).toList())
                .icon(() -> item.get().get().getDefaultInstance())
                .title(Component.translatable("itemGroup." + RuneCraftory.MODID + "." + name))
                .displayItems(new SubTabContextDisplayBuilder(subTabs(), (enabled, params, output) -> {
                    for (Pair<ResourceLocation, Supplier<SubTab>> sub : SUB_TABS) {
                        boolean correct = enabled.contains(sub.getFirst());
                        CONTENTS.get(sub.getFirst()).forEach(s -> {
                            if (correct)
                                output.accept(s.get());
                            else
                                output.accept(s.get(), CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY);
                        });
                    }
                }))
                .build());
    }

    private static ResourceLocation registerSub(String name, Supplier<Supplier<? extends Item>> item) {
        ResourceLocation id = RuneCraftory.modRes(name);
        SUB_TABS.add(Pair.of(id, () -> new SubTab(id, () -> item.get().get().getDefaultInstance())));
        return id;
    }
}
