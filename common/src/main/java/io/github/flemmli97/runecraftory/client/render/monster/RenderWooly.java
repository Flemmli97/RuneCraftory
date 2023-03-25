package io.github.flemmli97.runecraftory.client.render.monster;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.monster.ModelWooly;
import io.github.flemmli97.runecraftory.client.model.monster.ModelWoolyWool;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.client.render.layer.LayerWooly;
import io.github.flemmli97.runecraftory.client.render.layer.RiderLayerRendererExt;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityWooly;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderWooly<T extends EntityWooly> extends RenderMonster<T, ModelWooly<T>> {

    public final float scale;

    public RenderWooly(EntityRendererProvider.Context ctx) {
        this(ctx, new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/wooly.png"), 1);
    }

    public RenderWooly(EntityRendererProvider.Context ctx, float scale) {
        this(ctx, new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/wooly.png"), scale);
    }

    public RenderWooly(EntityRendererProvider.Context ctx, ResourceLocation texture, float scale) {
        super(ctx, new ModelWooly<>(ctx.bakeLayer(ModelWooly.LAYER_LOCATION)), texture, 0.5f, false);
        this.scale = scale;
        this.layers.add(new LayerWooly<>(this, new ModelWoolyWool<>(ctx.bakeLayer(ModelWoolyWool.LAYER_LOCATION))));
        this.layers.add(new RiderLayerRendererExt<>(this, (stack, entity) -> stack.scale(1 / this.scale, 1 / this.scale, 1 / this.scale)));
    }

    @Override
    protected void scale(T entity, PoseStack stack, float partialTick) {
        super.scale(entity, stack, partialTick);
        stack.scale(this.scale, this.scale, this.scale);
    }

}
