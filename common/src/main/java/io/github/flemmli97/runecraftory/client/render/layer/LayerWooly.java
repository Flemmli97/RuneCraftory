package io.github.flemmli97.runecraftory.client.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.monster.WoolyModel;
import io.github.flemmli97.runecraftory.client.model.monster.WoolyWoolModel;
import io.github.flemmli97.runecraftory.common.entities.monster.Wooly;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.CommonColors;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;

public class LayerWooly<T extends Wooly> extends RenderLayer<T, WoolyModel<T>> {

    private final ResourceLocation tex = RuneCraftory.modRes("textures/entity/monsters/wooly_wool.png");
    private final WoolyWoolModel<T> woolModel;

    public LayerWooly(RenderLayerParent<T, WoolyModel<T>> renderer, WoolyWoolModel<T> woolModel) {
        super(renderer);
        this.woolModel = woolModel;
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource buf, int light, T wooly, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!wooly.isSheared() && !wooly.isInvisible()) {
            int color = CommonColors.WHITE;
            if (wooly.hasCustomName() && "jeb_".equals(wooly.getName().getString())) {
                int tick = wooly.tickCount / 25 + wooly.getId();
                int colorCount = DyeColor.values().length;
                int l = tick % colorCount;
                int m = (tick + 1) % colorCount;
                float f = ((float) (wooly.tickCount % 25) + partialTick) / 25.0F;
                int n = Sheep.getColor(DyeColor.byId(l));
                int o = Sheep.getColor(DyeColor.byId(m));
                color = FastColor.ARGB32.lerp(f, n, o);
            }/* else {
                color = Sheep.getColorArray(wooly.getColor());
            }*/
            this.woolModel.syncModel(this.getParentModel());
            coloredCutoutModelCopyLayerRender(this.getParentModel(), this.woolModel, this.tex, stack, buf, light, wooly, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTick, color);
        }
    }
}
