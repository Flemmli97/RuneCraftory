package io.github.flemmli97.runecraftory.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.attachment.PlayerData;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerUpgrade;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class UpgradeGui extends AbstractContainerScreen<ContainerUpgrade> {

    private static final ResourceLocation forging = new ResourceLocation(RuneCraftory.MODID, "textures/gui/forgef.png");
    private static final ResourceLocation crafting = new ResourceLocation(RuneCraftory.MODID, "textures/gui/craftingf.png");

    public UpgradeGui(ContainerUpgrade container, Inventory inv, Component name) {
        super(container, inv, name);
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
        ResourceLocation texture = forging;
        //if(this.menu.craftingType() == EnumCrafting.ARMOR)
        //    texture = crafting;
        RenderSystem.setShaderTexture(0, texture);
        this.blit(stack, this.leftPos, this.topPos, 0, 0, 176, 166);
        if (this.menu.rpCost() >= 0) {
            int rpMax = Platform.INSTANCE.getPlayerData(this.minecraft.player).map(PlayerData::getMaxRunePoints).orElse(0);
            MutableComponent cost = new TextComponent("" + this.menu.rpCost());
            if (rpMax < this.menu.rpCost()) {
                cost = new TranslatableComponent("crafting.rpMax.missing").withStyle(ChatFormatting.DARK_RED);
            }
            OverlayGui.drawStringCenter(stack, this.font, cost, this.leftPos + 91, this.topPos + 42, 0);
        }
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderTooltip(stack, mouseX, mouseY);
    }
}
