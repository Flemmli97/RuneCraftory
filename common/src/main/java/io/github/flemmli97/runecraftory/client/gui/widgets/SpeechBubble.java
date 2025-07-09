package io.github.flemmli97.runecraftory.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.gui.NPCDialogueGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.CommonColors;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class SpeechBubble implements Renderable {

    protected static final ResourceLocation SPRITE = RuneCraftory.modRes("hud/speech_bubble");

    private final Minecraft mc;
    private final int x;
    private final int y;
    private final int width;
    private final int maxHeight;
    private int height;
    private int showDuration;
    private List<FormattedCharSequence> texts;

    public SpeechBubble(Minecraft mc, int x, int y, int width, int maxHeight) {
        this.mc = mc;
        this.x = x;
        this.y = y;
        this.width = width;
        this.maxHeight = maxHeight;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (--this.showDuration > 0 && this.texts != null) {
            RenderSystem.setShaderTexture(0, SPRITE);
            graphics.blitSprite(SPRITE, this.x, this.y, this.width, this.height);
            int y = this.y + NPCDialogueGui.BORDER_SIZE;
            for (FormattedCharSequence text : this.texts) {
                graphics.drawString(this.mc.font, text, this.x + NPCDialogueGui.BORDER_SIZE, y, CommonColors.WHITE, false);
                y += this.mc.font.lineHeight;
            }
        }
    }

    public void showBubble(Component text, int duration) {
        this.showDuration = duration;
        this.texts = this.mc.font.split(text, this.width - 2 * NPCDialogueGui.BORDER_SIZE);
        this.height = Math.min((this.texts.size() + 1) * this.mc.font.lineHeight, this.maxHeight) + 2 * NPCDialogueGui.BORDER_SIZE;
    }
}
