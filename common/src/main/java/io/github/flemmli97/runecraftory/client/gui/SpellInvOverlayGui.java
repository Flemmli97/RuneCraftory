package io.github.flemmli97.runecraftory.client.gui;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SpellInvOverlayGui {

    protected static final ResourceLocation WIDGETS_TEX_PATH = ResourceLocation.withDefaultNamespace("textures/gui/widgets.png");

    private final Minecraft mc;

    public SpellInvOverlayGui(Minecraft mc) {
        this.mc = mc;
    }

    public void render(GuiGraphics graphics, DeltaTracker tracker) {
//        if (!this.mc.player.isSpectator()) {
//            InventorySpells inv = Platform.INSTANCE.getPlayerData(this.mc.player).getInv();
//            if (inv != null) {
//                RenderSystem.setShaderTexture(0, WIDGETS_TEX_PATH);
//                int scaledWidth = this.mc.getWindow().getGuiScaledWidth();
//                int scaledHeight = this.mc.getWindow().getGuiScaledHeight();
//                int i = scaledWidth / 2;
//                int j = this.getBlitOffset();
//                this.setBlitOffset(-90);
//                for (int a = 0; a < 2; ++a) {
//                    graphics.blit(WIDGETS_TEX_PATH, i + 114 + 29 * a, scaledHeight - 47, 24, 23, 22, 22);
//                }
//                for (int a = 0; a < 2; ++a) {
//                    graphics.blit(WIDGETS_TEX_PATH, i + 114 + 29 * a, scaledHeight - 23, 24, 23, 22, 22);
//                }
//                this.setBlitOffset(j);
//                RenderSystem.enableBlend();
//                RenderSystem.defaultBlendFunc();
//                for (int a = 0; a < 2; ++a) {
//                    this.renderHotbarItem(graphics.pose(), i + 117 + 29 * a, scaledHeight - 44, partialTicks, inv.getItem(a));
//                }
//                for (int a = 0; a < 2; ++a) {
//                    this.renderHotbarItem(graphics.pose(), i + 117 + 29 * a, scaledHeight - 20, partialTicks, inv.getItem(a + 2));
//                }
//            }
//        }
    }

    private void renderHotbarItem(GuiGraphics graphics, int x, int y, DeltaTracker delta, Player player, ItemStack stack) {
        if (!stack.isEmpty()) {
            float f = (float) stack.getPopTime() - delta.getGameTimeDeltaPartialTick(false);
            if (f > 0.0F) {
                float g = 1.0F + f / 5.0F;
                graphics.pose().pushPose();
                graphics.pose().translate((float) (x + 8), (float) (y + 12), 0.0F);
                graphics.pose().scale(1.0F / g, (g + 1.0F) / 2.0F, 1.0F);
                graphics.pose().translate((float) (-(x + 8)), (float) (-(y + 12)), 0.0F);
            }
            graphics.renderItem(player, stack, x, y, 0);
            if (f > 0.0F) {
                graphics.pose().popPose();
            }
            graphics.renderItemDecorations(this.mc.font, stack, x, y);
        }
    }
}