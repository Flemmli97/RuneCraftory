package io.github.flemmli97.runecraftory.client.gui.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

public class PageButton extends Button {

    private static final ResourceLocation texturepath = new ResourceLocation(RuneCraftory.MODID, "textures/gui/bars.png");

    public PageButton(int x, int y, ITextComponent display, IPressable press) {
        super(x, y, 12, 12, display, press);
    }

    @Override
    public void renderWidget(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bindTexture(texturepath);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
        this.isHovered = (mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.blit(stack, this.x, this.y, 98 + (this.isHovered ? 13 : 0), 0, this.width, this.height);
        int j = this.getFGColor();
        drawCenteredString(stack, mc.fontRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
    }
}
