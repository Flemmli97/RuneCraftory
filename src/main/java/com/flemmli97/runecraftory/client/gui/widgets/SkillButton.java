package com.flemmli97.runecraftory.client.gui.widgets;

import com.flemmli97.runecraftory.RuneCraftory;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

public class SkillButton extends Button {

    private static final ResourceLocation texturepath = new ResourceLocation(RuneCraftory.MODID, "textures/gui/bars.png");

    private final Screen ownerGui;

    public SkillButton(int x, int y, Screen ownerGui, IPressable press) {
        super(x, y, 12, 12, StringTextComponent.EMPTY, press);
        this.ownerGui = ownerGui;
    }

    @Override
    public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        if (this.ownerGui instanceof CreativeScreen && ((CreativeScreen) this.ownerGui).getSelectedTabIndex() != ItemGroup.INVENTORY.getIndex()) {
            this.active = false;
            return;
        } else
            this.active = true;
        Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bindTexture(texturepath);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
        this.hovered = (mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.drawTexture(stack, this.x, this.y, 131 + (this.hovered ? 13 : 0), 41, this.width, this.height);
    }
}
