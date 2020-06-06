package com.flemmli97.runecraftory.client.render.monsters;

import com.flemmli97.runecraftory.client.render.RenderMobBase;
import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class LayerItem<T extends EntityMobBase> implements LayerRenderer<T> {

    public LayerItem(RenderMobBase<T> renderer, ItemStack item, EnumHand hand){

    }

    @Override
    public void doRenderLayer(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {

    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
