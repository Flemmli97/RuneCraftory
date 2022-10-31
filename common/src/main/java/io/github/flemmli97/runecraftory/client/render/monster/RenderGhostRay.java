package io.github.flemmli97.runecraftory.client.render.monster;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityGhostRay;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderGhostRay<T extends EntityGhostRay> extends RenderGhost<T> {

    public RenderGhostRay(EntityRendererProvider.Context ctx) {
        super(ctx, new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/ghost_ray.png"));
    }

    @Override
    protected void scale(T entity, PoseStack stack, float partialTick) {
        super.scale(entity, stack, partialTick);
        stack.scale(1.4f, 1.4f, 1.4f);
    }
}