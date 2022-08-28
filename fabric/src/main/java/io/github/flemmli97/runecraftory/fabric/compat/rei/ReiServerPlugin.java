package io.github.flemmli97.runecraftory.fabric.compat.rei;

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

public class ReiServerPlugin implements REIServerPlugin {

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(ReiClientPlugin.FORGING, SextupleDisplay.serializer());
        registry.register(ReiClientPlugin.COOKING, SextupleDisplay.serializer());
        registry.register(ReiClientPlugin.ARMOR, SextupleDisplay.serializer());
        registry.register(ReiClientPlugin.CHEM, SextupleDisplay.serializer());
    }

    @Override
    public void registerMenuInfo(MenuInfoRegistry registry) {
        registry.register(ReiClientPlugin.FORGING, ContainerCrafting.class, of(ReiClientPlugin.FORGING, SextupleMenuProvider::new));
        registry.register(ReiClientPlugin.COOKING, ContainerCrafting.class, of(ReiClientPlugin.COOKING, SextupleMenuProvider::new));
        registry.register(ReiClientPlugin.ARMOR, ContainerCrafting.class, of(ReiClientPlugin.ARMOR, SextupleMenuProvider::new));
        registry.register(ReiClientPlugin.CHEM, ContainerCrafting.class, of(ReiClientPlugin.CHEM, SextupleMenuProvider::new));
    }

    static <T extends AbstractContainerMenu, D extends Display> SimpleMenuInfoProvider<T, D> of(CategoryIdentifier<?> id, Function<D, @Nullable MenuInfo<T, D>> provider) {
        return d -> {
            if (d.getCategoryIdentifier().equals(id))
                return provider.apply(d);
            return null;
        };
    }
}
