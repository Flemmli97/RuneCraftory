package io.github.flemmli97.runecraftory.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.gui.widgets.PageButton;
import io.github.flemmli97.runecraftory.common.attachment.PlayerData;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerCrafting;
import io.github.flemmli97.runecraftory.common.network.C2SUpdateCraftingScreen;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CraftingGui extends AbstractContainerScreen<ContainerCrafting> {

    private static final ResourceLocation forging = new ResourceLocation(RuneCraftory.MODID, "textures/gui/forgec.png");
    private static final ResourceLocation crafting = new ResourceLocation(RuneCraftory.MODID, "textures/gui/craftingc.png");
    private static final ResourceLocation chem = new ResourceLocation(RuneCraftory.MODID, "textures/gui/chemc.png");
    private static final ResourceLocation cooking = new ResourceLocation(RuneCraftory.MODID, "textures/gui/cookingc.png");

    public CraftingGui(ContainerCrafting container, Inventory inv, Component name) {
        super(container, inv, name);
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
        ResourceLocation texture = forging;
        /*texture = switch (this.menu.craftingType()) {
            case ARMOR -> crafting;
            case COOKING -> cooking;
            case FORGE -> forging;
            case CHEM -> chem;
        };*/
        RenderSystem.setShaderTexture(0, forging);
        this.blit(stack, this.leftPos, this.topPos, 0, 0, 176, 166);
        if (this.menu.rpCost() >= 0) {
            int rpMax = Platform.INSTANCE.getPlayerData(this.minecraft.player).map(PlayerData::getMaxRunePoints).orElse(0);
            MutableComponent cost = new TextComponent("" + this.menu.rpCost());
            if (rpMax < this.menu.rpCost()) {
                cost = new TranslatableComponent("crafting.rpMax.missing").withStyle(ChatFormatting.DARK_RED);
            }
            OverlayGui.drawStringCenter(stack, this.font, cost, this.leftPos + 123, this.topPos + 20, 0);
        }
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(new PageButton(this.leftPos + 108, this.topPos + 60, new TextComponent("<"), b -> Platform.INSTANCE.sendToServer(new C2SUpdateCraftingScreen(false))));
        this.addRenderableWidget(new PageButton(this.leftPos + 128, this.topPos + 60, new TextComponent(">"), b -> Platform.INSTANCE.sendToServer(new C2SUpdateCraftingScreen(true))));
    }
}