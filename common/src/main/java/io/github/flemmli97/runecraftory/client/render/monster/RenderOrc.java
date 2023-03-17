package io.github.flemmli97.runecraftory.client.render.monster;

import io.github.flemmli97.runecraftory.client.model.monster.ModelOrc;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityOrc;
import io.github.flemmli97.tenshilib.client.render.ItemLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderOrc<T extends EntityOrc> extends RenderMonster<T, ModelOrc<T>> {

    public RenderOrc(EntityRendererProvider.Context ctx, ResourceLocation texture) {
        super(ctx, new ModelOrc<>(ctx.bakeLayer(ModelOrc.LAYER_LOCATION)), texture, 0.5f);
        this.layers.add(new ItemLayer<>(this));
    }
}
