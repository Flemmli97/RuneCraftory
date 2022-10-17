package io.github.flemmli97.runecraftory.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class SpeechBubble extends GuiComponent implements Widget {

    protected static final ResourceLocation tex = new ResourceLocation(RuneCraftory.MODID, "textures/gui/bars.png");

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
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTick) {
        if (--this.showDuration > 0 && this.texts != null) {
            RenderSystem.setShaderTexture(0, tex);
            this.blit(stack, this.x, this.y, 1, 94, this.width / 2, this.height / 2);
            this.blit(stack, this.x + this.width / 2, this.y, 129 - this.width / 2, 94, this.width / 2, this.height / 2);
            this.blit(stack, this.x, this.y + this.height / 2, 1, 158 - this.height / 2, this.width / 2, this.height / 2 + 15);
            this.blit(stack, this.x + this.width / 2, this.y + this.height / 2, 129 - this.width / 2, 158 - this.height / 2, this.width / 2, this.height / 2 + 15);
            for (int i = 0; i < this.texts.size(); ++i) {
                this.mc.font.draw(stack, this.texts.get(i), this.x + 3, this.y + 3 + i * 8, 0);
            }
        }
    }

    public void showBubble(Component text, int duration) {
        this.showDuration = duration;
        this.texts = this.mc.font.split(text, this.width - 6);
        this.height = Math.min(this.texts.size() * 13 + 6, this.maxHeight);
    }
}
