package io.github.flemmli97.runecraftory.mixin;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractContainerScreen.class)
public interface ContainerScreenAccessor {

    @Accessor("leftPos")
    int getLeft();

    @Accessor("topPos")
    int getTop();
}
