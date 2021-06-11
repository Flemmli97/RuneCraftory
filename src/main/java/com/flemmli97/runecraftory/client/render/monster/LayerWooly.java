package com.flemmli97.runecraftory.client.render.monster;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.client.model.monster.ModelWooly;
import com.flemmli97.runecraftory.client.model.monster.ModelWoolyWool;
import com.flemmli97.runecraftory.common.entities.monster.EntityWooly;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class LayerWooly<T extends EntityWooly> extends LayerRenderer<T, ModelWooly<T>> {

    private final ResourceLocation tex = new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/wooly_wool.png");
    private final ModelWoolyWool<T> woolModel = new ModelWoolyWool<>();

    public LayerWooly(IEntityRenderer<T, ModelWooly<T>> renderer) {
        super(renderer);
    }

    @Override
    public void render(MatrixStack stack, IRenderTypeBuffer buf, int light, T wooly, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!wooly.isSheared() && !wooly.isInvisible()) {
            //float[] afloat = EntitySheep.getDyeRgb(wooly.getFleeceColor());
            //GlStateManager.color(afloat[0], afloat[1], afloat[2]);

            this.woolModel.syncModel(this.getEntityModel());
            renderCopyCutoutModel(this.getEntityModel(), this.woolModel, this.tex, stack, buf, light, wooly, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 1, 1, 1, 1);
        }
    }
}
