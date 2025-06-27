package io.github.flemmli97.runecraftory.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.mixinhelper.GuiGraphicsExtension;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin implements GuiGraphicsExtension {

    @Shadow
    @Final
    private PoseStack pose;

    @Shadow
    @Final
    private MultiBufferSource.BufferSource bufferSource;

    @Shadow
    protected abstract void flushIfUnmanaged();

    @Override
    public int runeraftory$drawString(Font font, @Nullable String text, float x, float y, int color, boolean dropShadow) {
        if (text == null) {
            return 0;
        } else {
            int i = font.drawInBatch(text, x, y, color, dropShadow, this.pose.last().pose(), this.bufferSource, Font.DisplayMode.NORMAL, 0, LightTexture.FULL_BRIGHT, font.isBidirectional());
            this.flushIfUnmanaged();
            return i;
        }
    }

    @Override
    public int runeraftory$drawString(Font font, Component text, float x, float y, int color, boolean dropShadow) {
        int i = font.drawInBatch(text, x, y, color, dropShadow, this.pose.last().pose(), this.bufferSource, Font.DisplayMode.NORMAL, 0, LightTexture.FULL_BRIGHT);
        this.flushIfUnmanaged();
        return i;
    }
}
