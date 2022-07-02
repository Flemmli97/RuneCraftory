package io.github.flemmli97.runecraftory.client.render.monster;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.monster.ModelGhost;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityGhostRay;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderGhostRay<T extends EntityGhostRay> extends RenderMonster<T, ModelGhost<T>> {

    public RenderGhostRay(EntityRendererProvider.Context ctx) {
        super(ctx, new ModelGhost<>(ctx.bakeLayer(ModelGhost.LAYER_LOCATION)), new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/ghost_ray.png"), 0);
    }

    @Override
    protected void scale(T entity, PoseStack stack, float partialTick) {
        super.scale(entity, stack, partialTick);
        stack.scale(1.4f, 1.4f, 1.4f);
    }
}