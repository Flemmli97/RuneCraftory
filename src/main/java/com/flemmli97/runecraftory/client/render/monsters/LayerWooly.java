package com.flemmli97.runecraftory.client.render.monsters;

import com.flemmli97.runecraftory.common.entity.monster.EntityWooly;
import com.flemmli97.runecraftory.common.lib.LibReference;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class LayerWooly implements LayerRenderer<EntityWooly> {

    private ResourceLocation tex = new ResourceLocation(LibReference.MODID, "textures/entity/monsters/wooly_wool.png");
    private final RenderWooly<EntityWooly> renderer;
    //private final ModelWoolyWool sheepModel = new ModelSheep1();

    public LayerWooly(RenderWooly<EntityWooly> renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(EntityWooly entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {

    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
