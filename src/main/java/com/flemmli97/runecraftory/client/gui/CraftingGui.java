package com.flemmli97.runecraftory.client.gui;

import com.flemmli97.runecraftory.common.inventory.container.ContainerCrafting;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class CraftingGui extends ContainerScreen<ContainerCrafting> {

    public CraftingGui(ContainerCrafting p_i51105_1_, PlayerInventory p_i51105_2_, ITextComponent p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
    }

    @Override
    protected void drawBackground(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {

    }
}
