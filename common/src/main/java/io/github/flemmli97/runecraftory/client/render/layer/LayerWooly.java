package io.github.flemmli97.runecraftory.client.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.monster.ModelWooly;
import io.github.flemmli97.runecraftory.client.model.monster.ModelWoolyWool;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityWooly;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;

public class LayerWooly<T extends EntityWooly> extends RenderLayer<T, ModelWooly<T>> {

    private final ResourceLocation tex = new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/wooly_wool.png");
    private final ModelWoolyWool<T> woolModel;

    public LayerWooly(RenderLayerParent<T, ModelWooly<T>> renderer, ModelWoolyWool<T> woolModel) {
        super(renderer);
        this.woolModel = woolModel;
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource buf, int light, T wooly, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!wooly.isSheared() && !wooly.isInvisible()) {
            float[] color = {1, 1, 1};
            if (wooly.hasCustomName() && "jeb_".equals(wooly.getName().getContents())) {
                int rand = wooly.tickCount / 25 + wooly.getId();
                int k = DyeColor.values().length;
                int l = rand % k;
                int m = (rand + 1) % k;
                float f = ((float) (wooly.tickCount % 25) + partialTicks) / 25.0f;
                float[] fs = Sheep.getColorArray(DyeColor.byId(l));
                float[] gs = Sheep.getColorArray(DyeColor.byId(m));
                color[0] = fs[0] * (1.0f - f) + gs[0] * f;
                color[1] = fs[1] * (1.0f - f) + gs[1] * f;
                color[2] = fs[2] * (1.0f - f) + gs[2] * f;
            }/* else {
                color = Sheep.getColorArray(wooly.getColor());
            }*/
            this.woolModel.syncModel(this.getParentModel());
            coloredCutoutModelCopyLayerRender(this.getParentModel(), this.woolModel, this.tex, stack, buf, light, wooly, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, color[0], color[1], color[2]);
        }
    }
}
