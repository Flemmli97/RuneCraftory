package io.github.flemmli97.runecraftory.client.render.monster;

import io.github.flemmli97.runecraftory.client.model.monster.OrcModel;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.Orc;
import io.github.flemmli97.tenshilib.client.render.layer.ItemLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class OrcRender<T extends Orc> extends RenderMonster<T, OrcModel<T>> {

    public OrcRender(EntityRendererProvider.Context ctx, ResourceLocation texture) {
        super(ctx, new OrcModel<>(), texture, 0.5f);
        this.layers.add(new ItemLayer<>(this, ctx.getItemInHandRenderer()));
    }
}
