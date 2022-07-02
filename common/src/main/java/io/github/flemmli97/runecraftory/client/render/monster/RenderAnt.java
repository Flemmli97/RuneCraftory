package io.github.flemmli97.runecraftory.client.render.monster;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.monster.ModelAnt;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityAnt;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderAnt<T extends EntityAnt> extends RenderMonster<T, ModelAnt<T>> {

    public RenderAnt(EntityRendererProvider.Context ctx) {
        super(ctx, new ModelAnt<>(ctx.bakeLayer(ModelAnt.LAYER_LOCATION)), new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/ant.png"), 0.5f);
    }

    @Override
    protected void scale(T entity, PoseStack stack, float partialTick) {
        super.scale(entity, stack, partialTick);
        stack.scale(0.7f, 0.7f, 0.7f);
    }
}
