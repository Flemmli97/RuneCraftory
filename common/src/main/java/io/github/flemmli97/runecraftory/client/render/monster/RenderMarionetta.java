package io.github.flemmli97.runecraftory.client.render.monster;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.monster.ModelMarionetta;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityMarionetta;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderMarionetta<T extends EntityMarionetta> extends RenderMonster<T, ModelMarionetta<T>> {

    public RenderMarionetta(EntityRendererProvider.Context ctx) {
        super(ctx, new ModelMarionetta<>(ctx.bakeLayer(ModelMarionetta.LAYER_LOCATION)), new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/marionetta.png"), 0.5f);
    }

    @Override
    protected float getFlipDegrees(T entityLivingBaseIn) {
        return 0;
    }
}
