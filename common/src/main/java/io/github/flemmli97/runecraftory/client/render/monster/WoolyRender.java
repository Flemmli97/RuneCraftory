package io.github.flemmli97.runecraftory.client.render.monster;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.monster.WoolyModel;
import io.github.flemmli97.runecraftory.client.model.monster.WoolyWoolModel;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.client.render.layer.LayerWooly;
import io.github.flemmli97.runecraftory.common.entities.monster.Wooly;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class WoolyRender<T extends Wooly> extends RenderMonster<T, WoolyModel<T>> {

    public WoolyRender(EntityRendererProvider.Context ctx) {
        this(ctx, RuneCraftory.modRes("textures/entity/monsters/wooly.png"));
    }

    public WoolyRender(EntityRendererProvider.Context ctx, ResourceLocation texture) {
        super(ctx, new WoolyModel<>(), texture, 0.5f);
        this.layers.add(new LayerWooly<>(this, new WoolyWoolModel<>()));
    }
}
