package io.github.flemmli97.runecraftory.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.config.ClientConfig;
import io.github.flemmli97.runecraftory.common.inventory.InventorySpells;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class SpellInvOverlayGui {

    protected static final ResourceLocation TEXTURE = RuneCraftory.modRes("hud/spell_slots");

    private final Minecraft mc;

    public SpellInvOverlayGui(Minecraft mc) {
        this.mc = mc;
    }

    public void render(GuiGraphics graphics, DeltaTracker tracker) {
        if (!this.mc.player.isSpectator()) {
            InventorySpells inv = Platform.INSTANCE.getPlayerData(this.mc.player).getInv();
            int x = ClientConfig.spellsDisplayPosition
                    .positionX(this.mc.getWindow().getGuiScaledWidth(), 44, ClientConfig.spellsDisplayX);
            int y = ClientConfig.spellsDisplayPosition
                    .positionY(this.mc.getWindow().getGuiScaledHeight(), 44, ClientConfig.spellsDisplayY);
            RenderSystem.enableBlend();
            graphics.blitSprite(TEXTURE, x, y, 44, 44);
            RenderSystem.disableBlend();
            for (int a = 0; a < 2; ++a) {
                this.renderHotbarItem(graphics, x + 3 + 22 * a, y + 3, tracker, inv.getItem(a));
            }
            for (int a = 0; a < 2; ++a) {
                this.renderHotbarItem(graphics, x + 3 + 22 * a, y + 25, tracker, inv.getItem(a + 2));
            }
        }
    }

    private void renderHotbarItem(GuiGraphics graphics, int x, int y, DeltaTracker delta, ItemStack stack) {
        if (!stack.isEmpty()) {
            float f = (float) stack.getPopTime() - delta.getGameTimeDeltaPartialTick(false);
            if (f > 0.0F) {
                float g = 1.0F + f / 5.0F;
                graphics.pose().pushPose();
                graphics.pose().translate((float) (x + 8), (float) (y + 12), 0.0F);
                graphics.pose().scale(1.0F / g, (g + 1.0F) / 2.0F, 1.0F);
                graphics.pose().translate((float) (-(x + 8)), (float) (-(y + 12)), 0.0F);
            }
            graphics.renderItem(this.mc.player, stack, x, y, 0);
            if (f > 0.0F) {
                graphics.pose().popPose();
            }
            graphics.renderItemDecorations(this.mc.font, stack, x, y);
        }
    }
}