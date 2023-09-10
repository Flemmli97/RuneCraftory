package io.github.flemmli97.runecraftory.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.gui.NPCDialogueGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import java.util.List;

public class DialogueOptionButton extends Button {

    private static final ResourceLocation TEXTURE = new ResourceLocation(RuneCraftory.MODID, "textures/gui/npc_dialogue.png");

    private final List<FormattedCharSequence> text;
    private final int txtX;
    private int maxWidth;

    public DialogueOptionButton(int x, int y, Font font, Component text, OnPress press) {
        super(x, y, 0, 0, text, press);
        this.text = font.split(text, (int) (NPCDialogueGui.ACTION_WIDTH * 0.7));
        if (this.text.size() == 1)
            this.maxWidth = font.width(text);
        else
            this.maxWidth = (int) (NPCDialogueGui.ACTION_WIDTH * 0.7);
        this.txtX = this.x;
        this.x -= this.maxWidth / 2 + NPCDialogueGui.BORDER_SIZE;
        this.width = this.maxWidth + NPCDialogueGui.BORDER_SIZE * 2;
        this.height = this.text.size() * (font.lineHeight - 2) + ((this.text.size() - 1) * 4) + NPCDialogueGui.BORDER_SIZE * 2;
    }

    @Override
    public void renderButton(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();

        int width = this.width - NPCDialogueGui.BORDER_SIZE;
        int height = this.height - NPCDialogueGui.BORDER_SIZE;
        this.blit(stack, this.x, this.y, 5, 5 + (this.isHoveredOrFocused() ? 110 : 0), width, height);
        this.blit(stack, this.x, this.y + height, 5, 5 + NPCDialogueGui.MAX_HEIGHT - NPCDialogueGui.BORDER_SIZE + (this.isHoveredOrFocused() ? 110 : 0), width, NPCDialogueGui.BORDER_SIZE);
        this.blit(stack, this.x + width, this.y, 5 + NPCDialogueGui.MAX_WIDTH - NPCDialogueGui.BORDER_SIZE, 5 + (this.isHoveredOrFocused() ? 110 : 0), NPCDialogueGui.BORDER_SIZE, height);
        this.blit(stack, this.x + width, this.y + height, 5 + NPCDialogueGui.MAX_WIDTH - NPCDialogueGui.BORDER_SIZE, 5 + NPCDialogueGui.MAX_HEIGHT - NPCDialogueGui.BORDER_SIZE + (this.isHoveredOrFocused() ? 110 : 0), NPCDialogueGui.BORDER_SIZE, NPCDialogueGui.BORDER_SIZE);

        this.renderBg(stack, minecraft, mouseX, mouseY);
        int j = this.active ? 0xFFFFFF : 0xA0A0A0;

        int y = 0;
        for (FormattedCharSequence comp : this.text) {
            AbstractWidget.drawCenteredString(stack, font, comp, this.txtX, this.y + NPCDialogueGui.BORDER_SIZE + y, j | Mth.ceil(this.alpha * 255.0f) << 24);
            y++;
        }
    }
}
