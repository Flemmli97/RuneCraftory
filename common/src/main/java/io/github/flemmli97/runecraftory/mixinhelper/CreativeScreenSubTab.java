package io.github.flemmli97.runecraftory.mixinhelper;

import io.github.flemmli97.tenshilib.client.gui.widget.list.SelectableListWidget;
import org.jetbrains.annotations.Nullable;

public interface CreativeScreenSubTab {

    @Nullable
    SelectableListWidget runecraftory$subTabWidget();
}
