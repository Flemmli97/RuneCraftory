package io.github.flemmli97.runecraftory.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.common.attachment.PlayerData;
import io.github.flemmli97.runecraftory.common.inventory.InventorySpells;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class SpellInvOverlayGui extends GuiComponent {

    protected static final ResourceLocation WIDGETS_TEX_PATH = new ResourceLocation("textures/gui/widgets.png");

    private final Minecraft mc;

    public SpellInvOverlayGui(Minecraft mc) {
        this.mc = mc;
    }

    public void render(PoseStack stack, float partialTicks) {
        if (!this.mc.player.isSpectator()) {
            InventorySpells inv = Platform.INSTANCE.getPlayerData(this.mc.player).map(PlayerData::getInv).orElse(null);
            if (inv != null) {
                RenderSystem.setShaderTexture(0, WIDGETS_TEX_PATH);
                int scaledWidth = this.mc.getWindow().getGuiScaledWidth();
                int scaledHeight = this.mc.getWindow().getGuiScaledHeight();
                int i = scaledWidth / 2;
                int j = this.getBlitOffset();
                this.setBlitOffset(-90);
                for (int a = 0; a < 2; ++a) {
                    this.blit(stack, i + 114 + 29 * a, scaledHeight - 47, 24, 23, 22, 22);
                }
                for (int a = 0; a < 2; ++a) {
                    this.blit(stack, i + 114 + 29 * a, scaledHeight - 23, 24, 23, 22, 22);
                }
                this.setBlitOffset(j);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                for (int a = 0; a < 2; ++a) {
                    this.renderHotbarItem(stack, i + 117 + 29 * a, scaledHeight - 44, partialTicks, inv.getItem(a));
                }
                for (int a = 0; a < 2; ++a) {
                    this.renderHotbarItem(stack, i + 117 + 29 * a, scaledHeight - 20, partialTicks, inv.getItem(a + 2));
                }
            }
        }

    }

    private void renderHotbarItem(PoseStack matrixStack, int x, int y, float partialTicks, ItemStack stack) {
        if (!stack.isEmpty()) {
            float f = (float) stack.getPopTime() - partialTicks;
            if (f > 0.0F) {
                matrixStack.pushPose();
                float f1 = 1.0F + f / 5.0F;
                matrixStack.translate((float) (x + 8), (float) (y + 12), 0.0F);
                matrixStack.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
                matrixStack.translate((float) (-(x + 8)), (float) (-(y + 12)), 0.0F);
            }

            this.mc.getItemRenderer().renderAndDecorateItem(stack, x, y);
            if (f > 0.0F) {
                matrixStack.popPose();
            }

            this.mc.getItemRenderer().renderGuiItemDecorations(this.mc.font, stack, x, y);
        }
    }
}