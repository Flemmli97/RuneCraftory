package io.github.flemmli97.runecraftory.client.render.monster;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.monster.ModelAmbrosia;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityAmbrosia;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderAmbrosia<T extends EntityAmbrosia> extends RenderMonster<T, ModelAmbrosia<T>> {

    public RenderAmbrosia(EntityRendererProvider.Context ctx) {
        super(ctx, new ModelAmbrosia<>(ctx.bakeLayer(ModelAmbrosia.LAYER_LOCATION)), new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/ambrosia.png"), 0.5f);
    }

    @Override
    protected float getFlipDegrees(T entityLivingBaseIn) {
        return 0;
    }
}
