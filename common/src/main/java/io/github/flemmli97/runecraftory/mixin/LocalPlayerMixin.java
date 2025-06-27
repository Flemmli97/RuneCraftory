package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.mixinhelper.PlayerExtended;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin implements PlayerExtended {

    @Override
    public boolean runcraftory$hasActualShiftKeyDown() {
        return Screen.hasShiftDown();
    }
}
