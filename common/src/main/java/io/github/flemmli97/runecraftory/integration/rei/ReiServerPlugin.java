package io.github.flemmli97.runecraftory.integration.rei;

import io.github.flemmli97.runecraftory.common.inventory.container.ContainerCrafting;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import me.shedaniel.rei.api.common.transfer.info.MenuInfo;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoRegistry;
import me.shedaniel.rei.api.common.transfer.info.simple.SimpleMenuInfoProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@SuppressWarnings("removal")
public class ReiServerPlugin implements REIServerPlugin {

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(CraftingIdentifier.FORGING.identifier(), SextupleDisplay.serializer());
        registry.register(CraftingIdentifier.COOKING.identifier(), SextupleDisplay.serializer());
        registry.register(CraftingIdentifier.ARMOR.identifier(), SextupleDisplay.serializer());
        registry.register(CraftingIdentifier.CHEMISTRY.identifier(), SextupleDisplay.serializer());
    }

    @Override
    public void registerMenuInfo(MenuInfoRegistry registry) {
        registry.register(CraftingIdentifier.FORGING.identifier(), ContainerCrafting.class,
                of(CraftingIdentifier.FORGING.identifier(), SextupleMenuProvider::new));
        registry.register(CraftingIdentifier.COOKING.identifier(), ContainerCrafting.class,
                of(CraftingIdentifier.COOKING.identifier(), SextupleMenuProvider::new));
        registry.register(CraftingIdentifier.ARMOR.identifier(), ContainerCrafting.class,
                of(CraftingIdentifier.ARMOR.identifier(), SextupleMenuProvider::new));
        registry.register(CraftingIdentifier.CHEMISTRY.identifier(), ContainerCrafting.class,
                of(CraftingIdentifier.CHEMISTRY.identifier(), SextupleMenuProvider::new));
    }

    static <T extends AbstractContainerMenu, D extends Display> SimpleMenuInfoProvider<T, D> of(CategoryIdentifier<?> id, Function<D, @Nullable MenuInfo<T, D>> provider) {
        return d -> {
            if (d.getCategoryIdentifier().equals(id))
                return provider.apply(d);
            return null;
        };
    }
}
