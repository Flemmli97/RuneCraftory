package com.flemmli97.runecraftory.client.gui;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.inventory.container.ContainerCrafting;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class CraftingGui extends ContainerScreen<ContainerCrafting> {

    private static final ResourceLocation forging = new ResourceLocation(RuneCraftory.MODID,"textures/gui/forgec.png");
    private static final ResourceLocation crafting = new ResourceLocation(RuneCraftory.MODID,"textures/gui/craftingc.png");
    private static final ResourceLocation chem = new ResourceLocation(RuneCraftory.MODID,"textures/gui/chemc.png");
    private static final ResourceLocation cooking = new ResourceLocation(RuneCraftory.MODID,"textures/gui/cookingc.png");
    private static final ResourceLocation bars = new ResourceLocation(RuneCraftory.MODID,"textures/gui/bars.png");

    public CraftingGui(ContainerCrafting p_i51105_1_, PlayerInventory p_i51105_2_, ITextComponent p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
    }

    @Override
    protected void drawBackground(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        switch (this.container.craftingType())
        {
            case ARMOR:
                this.client.getTextureManager().bindTexture(crafting);
                this.drawTexture(p_230450_1_, this.guiLeft, this.guiTop, 0, 0, 176, 166);
                break;
            case COOKING:
                this.client.getTextureManager().bindTexture(cooking);
                this.drawTexture(p_230450_1_, this.guiLeft, this.guiTop, 0, 0, 176, 166);
                break;
            case FORGE:
                this.client.getTextureManager().bindTexture(forging);
                this.drawTexture(p_230450_1_, this.guiLeft, this.guiTop, 0, 0, 176, 166);
                break;
            case CHEM:
                this.client.getTextureManager().bindTexture(chem);
                this.drawTexture(p_230450_1_, this.guiLeft, this.guiTop, 0, 0, 176, 166);
                break;
        }
    }

    @Override
    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
        this.drawMouseoverTooltip(p_230430_1_, p_230430_2_, p_230430_3_);
    }
}
