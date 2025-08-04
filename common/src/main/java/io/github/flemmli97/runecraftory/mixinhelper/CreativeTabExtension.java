package io.github.flemmli97.runecraftory.mixinhelper;

import io.github.flemmli97.runecraftory.common.creativetab.SubTab;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface CreativeTabExtension {

    void runecraftory$setSubTabs(List<SubTab> tabs);

    @Nullable
    List<SubTab> runecraftory$getSubTabs();
}
