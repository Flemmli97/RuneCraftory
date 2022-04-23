package io.github.flemmli97.runecraftory.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

public class SkillButton extends Button {

    private static final ResourceLocation texturepath = new ResourceLocation(RuneCraftory.MODID, "textures/gui/bars.png");

    private final Screen ownerGui;

    public SkillButton(int x, int y, Screen ownerGui, OnPress press) {
        super(x, y, 12, 12, TextComponent.EMPTY, press);
        this.ownerGui = ownerGui;
    }

    @Override
    public void renderButton(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        if (this.ownerGui instanceof CreativeModeInventoryScreen && ((CreativeModeInventoryScreen) this.ownerGui).getSelectedTab() != CreativeModeTab.TAB_INVENTORY.getId()) {
            this.active = false;
            return;
        } else
            this.active = true;
        Minecraft minecraft = Minecraft.getInstance();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texturepath);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.blit(stack, this.x, this.y, 131 + (this.isHoveredOrFocused() ? 13 : 0), 41, this.width, this.height);
        this.renderBg(stack, minecraft, mouseX, mouseY);
    }
}
