package io.github.flemmli97.runecraftory.client.render.monster;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.monster.GoblinModel;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.Goblin;
import io.github.flemmli97.tenshilib.client.render.layer.ItemLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class GoblinRender<T extends Goblin> extends RenderMonster<T, GoblinModel<T>> {

    public GoblinRender(EntityRendererProvider.Context ctx) {
        this(ctx, RuneCraftory.modRes("textures/entity/monsters/goblin.png"));
    }

    public GoblinRender(EntityRendererProvider.Context ctx, ResourceLocation texture) {
        super(ctx, new GoblinModel<>(), texture, 0.4f);
        this.layers.add(new ItemLayer<>(this, ctx.getItemInHandRenderer()));
    }
}
