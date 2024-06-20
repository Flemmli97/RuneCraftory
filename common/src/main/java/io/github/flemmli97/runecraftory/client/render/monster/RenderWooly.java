package io.github.flemmli97.runecraftory.client.render.monster;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.monster.ModelWooly;
import io.github.flemmli97.runecraftory.client.model.monster.ModelWoolyWool;
import io.github.flemmli97.runecraftory.client.render.ScaledRenderer;
import io.github.flemmli97.runecraftory.client.render.layer.LayerWooly;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityWooly;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderWooly<T extends EntityWooly> extends ScaledEntityRenderer<T, ModelWooly<T>> implements ScaledRenderer {

    public RenderWooly(EntityRendererProvider.Context ctx) {
        this(ctx, new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/wooly.png"), 1);
    }

    public RenderWooly(EntityRendererProvider.Context ctx, float scale) {
        this(ctx, new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/wooly.png"), scale);
    }

    public RenderWooly(EntityRendererProvider.Context ctx, ResourceLocation texture, float scale) {
        super(ctx, new ModelWooly<>(ctx.bakeLayer(ModelWooly.LAYER_LOCATION)), texture, scale, 0.5f);
        this.layers.add(new LayerWooly<>(this, new ModelWoolyWool<>(ctx.bakeLayer(ModelWoolyWool.LAYER_LOCATION))));
    }
}
