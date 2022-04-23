package io.github.flemmli97.runecraftory.client.render.monster;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.monster.ModelWooly;
import io.github.flemmli97.runecraftory.client.model.monster.ModelWoolyWool;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityWooly;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderWooly<T extends EntityWooly> extends RenderMonster<T, ModelWooly<T>> {

    public RenderWooly(EntityRendererProvider.Context ctx) {
        super(ctx, new ModelWooly<>(ctx.bakeLayer(ModelWooly.LAYER_LOCATION)), new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/wooly.png"));
        this.layers.add(new LayerWooly<>(this, new ModelWoolyWool<>(ctx.bakeLayer(ModelWoolyWool.LAYER_LOCATION))));
    }

}
