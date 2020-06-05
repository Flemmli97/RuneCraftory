package com.flemmli97.runecraftory.client.render.monsters;

import com.flemmli97.runecraftory.client.models.monsters.ModelWooly;
import com.flemmli97.runecraftory.client.models.monsters.ModelWoolyWool;
import com.flemmli97.runecraftory.common.entity.monster.EntityWooly;
import com.flemmli97.runecraftory.common.lib.LibReference;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class LayerWooly<T extends EntityWooly> implements LayerRenderer<T> {

    private ResourceLocation tex = new ResourceLocation(LibReference.MODID, "textures/entity/monsters/wooly_wool.png");
    private final RenderWooly<T> renderer;
    private final ModelWoolyWool woolModel = new ModelWoolyWool();
    private final ModelWooly mainModel;

    public LayerWooly(RenderWooly<T> renderer, ModelWooly mainModel) {
        this.renderer = renderer;
        this.mainModel=mainModel;
    }

    @Override
    public void doRenderLayer(T wooly, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if(!wooly.isSheared() && !wooly.isInvisible()){
            this.renderer.bindTexture(tex);
            //float[] afloat = EntitySheep.getDyeRgb(wooly.getFleeceColor());
            //GlStateManager.color(afloat[0], afloat[1], afloat[2]);
            this.woolModel.syncModel(this.mainModel);
            this.woolModel.render(wooly, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}
