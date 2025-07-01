package io.github.flemmli97.runecraftory.client.render.monster;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.monster.ModelWooly;
import io.github.flemmli97.runecraftory.client.model.monster.ModelWoolyWool;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.client.render.layer.LayerWooly;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityWooly;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderWooly<T extends EntityWooly> extends RenderMonster<T, ModelWooly<T>> {

    public RenderWooly(EntityRendererProvider.Context ctx) {
        this(ctx, RuneCraftory.modRes("textures/entity/monsters/wooly.png"));
    }

    public RenderWooly(EntityRendererProvider.Context ctx, ResourceLocation texture) {
        super(ctx, new ModelWooly<>(), texture, 0.5f);
        this.layers.add(new LayerWooly<>(this, new ModelWoolyWool<>()));
    }
}
