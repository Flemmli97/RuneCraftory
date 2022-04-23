package io.github.flemmli97.runecraftory.client.render.monster;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.monster.ModelThunderbolt;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityThunderbolt;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderThunderbolt<T extends EntityThunderbolt> extends RenderMonster<T, ModelThunderbolt<T>> {

    public RenderThunderbolt(EntityRendererProvider.Context ctx) {
        super(ctx, new ModelThunderbolt<>(ctx.bakeLayer(ModelThunderbolt.LAYER_LOCATION)), new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/thunderbolt.png"));
    }

    @Override
    protected float getFlipDegrees(T entityLivingBaseIn) {
        return 0;
    }
}