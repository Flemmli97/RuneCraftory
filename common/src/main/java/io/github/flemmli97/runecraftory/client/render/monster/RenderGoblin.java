package io.github.flemmli97.runecraftory.client.render.monster;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.monster.ModelGoblin;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityGoblin;
import io.github.flemmli97.tenshilib.client.render.ItemLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderGoblin<T extends EntityGoblin> extends RenderMonster<T, ModelGoblin<T>> {

    public RenderGoblin(EntityRendererProvider.Context ctx) {
        this(ctx, new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/goblin.png"));
    }

    public RenderGoblin(EntityRendererProvider.Context ctx, ResourceLocation texture) {
        super(ctx, new ModelGoblin<>(ctx.bakeLayer(ModelGoblin.LAYER_LOCATION)), texture, 0.4f);
        this.layers.add(new ItemLayer<>(this));
    }
}
