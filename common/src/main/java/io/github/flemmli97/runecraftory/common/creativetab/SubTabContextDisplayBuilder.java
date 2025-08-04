package io.github.flemmli97.runecraftory.common.creativetab;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SubTabContextDisplayBuilder implements CreativeModeTab.DisplayItemsGenerator {

    private final Set<ResourceLocation> all;
    private final ExtendedGenerator generator;

    private Set<ResourceLocation> enabledTabs = new HashSet<>();

    public SubTabContextDisplayBuilder(Collection<ResourceLocation> tabs, ExtendedGenerator generator) {
        this.all = Set.copyOf(tabs);
        this.generator = generator;
    }

    public void enabledTabs(Set<ResourceLocation> enabledTabs) {
        this.enabledTabs = enabledTabs;
    }

    @Override
    public void accept(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output) {
        if (this.enabledTabs.isEmpty())
            this.generator.accept(this.all, params, output);
        else
            this.generator.accept(this.enabledTabs, params, output);
    }

    public interface ExtendedGenerator {

        void accept(Set<ResourceLocation> enabledTabs, CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output);
    }
}
