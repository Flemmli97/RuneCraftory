package io.github.flemmli97.runecraftory.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class PageButton extends Button {

    private static final ResourceLocation texturepath = new ResourceLocation(RuneCraftory.MODID, "textures/gui/bars.png");

    public PageButton(int x, int y, Component display, OnPress press) {
        super(x, y, 12, 12, display, press);
    }

    @Override
    public void renderButton(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texturepath);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.blit(stack, this.x, this.y, 98 + (this.isHoveredOrFocused() ? 13 : 0), 0, this.width, this.height);
        this.renderBg(stack, minecraft, mouseX, mouseY);
        int j = this.active ? 0xFFFFFF : 0xA0A0A0;
        AbstractWidget.drawCenteredString(stack, font, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | Mth.ceil(this.alpha * 255.0f) << 24);
    }
}
