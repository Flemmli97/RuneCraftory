package io.github.flemmli97.runecraftory.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.gui.widgets.PageButton;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerCrafting;
import io.github.flemmli97.runecraftory.common.network.C2SUpdateCraftingScreen;
import io.github.flemmli97.runecraftory.common.network.PacketHandler;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class CraftingGui extends ContainerScreen<ContainerCrafting> {

    private static final ResourceLocation forging = new ResourceLocation(RuneCraftory.MODID, "textures/gui/forgec.png");
    private static final ResourceLocation crafting = new ResourceLocation(RuneCraftory.MODID, "textures/gui/craftingc.png");
    private static final ResourceLocation chem = new ResourceLocation(RuneCraftory.MODID, "textures/gui/chemc.png");
    private static final ResourceLocation cooking = new ResourceLocation(RuneCraftory.MODID, "textures/gui/cookingc.png");

    public CraftingGui(ContainerCrafting container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {

        /*switch (this.container.craftingType()) {
            case ARMOR:
                this.client.getTextureManager().bindTexture(crafting);
                this.drawTexture(stack, this.guiLeft, this.guiTop, 0, 0, 176, 166);
                break;
            case COOKING:
                this.client.getTextureManager().bindTexture(cooking);
                this.drawTexture(stack, this.guiLeft, this.guiTop, 0, 0, 176, 166);
                break;
            case FORGE:
                this.client.getTextureManager().bindTexture(forging);
                this.drawTexture(stack, this.guiLeft, this.guiTop, 0, 0, 176, 166);
                break;
            case CHEM:
                this.client.getTextureManager().bindTexture(chem);
                this.drawTexture(stack, this.guiLeft, this.guiTop, 0, 0, 176, 166);
                break;
        }*/

        this.minecraft.getTextureManager().bindTexture(forging);
        this.blit(stack, this.guiLeft, this.guiTop, 0, 0, 176, 166);
        if (this.container.rpCost() >= 0)
            OverlayGui.drawStringCenter(stack, this.font, new StringTextComponent("" + this.container.rpCost()), this.guiLeft + 123, this.guiTop + 20, 0);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
        this.addButton(new PageButton(this.guiLeft + 108, this.guiTop + 60, new StringTextComponent("<"), b -> PacketHandler.sendToServer(new C2SUpdateCraftingScreen(false))));
        this.addButton(new PageButton(this.guiLeft + 128, this.guiTop + 60, new StringTextComponent(">"), b -> PacketHandler.sendToServer(new C2SUpdateCraftingScreen(true))));
    }
}
