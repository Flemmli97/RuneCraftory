package io.github.flemmli97.runecraftory.client.gui;

import io.github.flemmli97.runecraftory.common.capability.CapabilityInsts;
import io.github.flemmli97.runecraftory.common.capability.IPlayerCap;
import io.github.flemmli97.runecraftory.common.inventory.InventorySpells;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class SpellInvOverlayGui extends AbstractGui {

    protected static final ResourceLocation WIDGETS_TEX_PATH = new ResourceLocation("textures/gui/widgets.png");

    private final Minecraft mc;

    public SpellInvOverlayGui(Minecraft mc) {
        this.mc = mc;
    }

    public void render(MatrixStack stack, float partialTicks) {
        if (!this.mc.player.isSpectator()) {
            InventorySpells inv = this.mc.player.getCapability(CapabilityInsts.PlayerCap).map(IPlayerCap::getInv).orElse(null);
            if (inv != null) {
                this.mc.getTextureManager().bindTexture(WIDGETS_TEX_PATH);
                int scaledWidth = this.mc.getMainWindow().getScaledWidth();
                int scaledHeight = this.mc.getMainWindow().getScaledHeight();
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
                RenderSystem.enableRescaleNormal();
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                for (int a = 0; a < 2; ++a) {
                    this.renderHotbarItem(stack, i + 117 + 29 * a, scaledHeight - 44, partialTicks, this.mc.player, inv.getStackInSlot(a));
                }
                for (int a = 0; a < 2; ++a) {
                    this.renderHotbarItem(stack, i + 117 + 29 * a, scaledHeight - 20, partialTicks, this.mc.player, inv.getStackInSlot(a + 2));
                }
            }
        }

    }

    private void renderHotbarItem(MatrixStack matrixStack, int x, int y, float partialTicks, PlayerEntity player, ItemStack stack) {
        if (!stack.isEmpty()) {
            float f = (float) stack.getAnimationsToGo() - partialTicks;
            if (f > 0.0F) {
                matrixStack.push();
                float f1 = 1.0F + f / 5.0F;
                matrixStack.translate((float) (x + 8), (float) (y + 12), 0.0F);
                matrixStack.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
                matrixStack.translate((float) (-(x + 8)), (float) (-(y + 12)), 0.0F);
            }

            this.mc.getItemRenderer().renderItemAndEffectIntoGUI(player, stack, x, y);
            if (f > 0.0F) {
                matrixStack.pop();
            }

            this.mc.getItemRenderer().renderItemOverlays(this.mc.fontRenderer, stack, x, y);
        }
    }
}