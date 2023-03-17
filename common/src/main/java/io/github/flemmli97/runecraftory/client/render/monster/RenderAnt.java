package io.github.flemmli97.runecraftory.client.render.monster;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.client.model.monster.ModelAnt;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityAnt;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderAnt<T extends EntityAnt> extends RenderMonster<T, ModelAnt<T>> {

    private final float scale;

    public RenderAnt(EntityRendererProvider.Context ctx, ResourceLocation texture) {
        this(ctx, texture, 1);
    }

    public RenderAnt(EntityRendererProvider.Context ctx, ResourceLocation texture, float scale) {
        super(ctx, new ModelAnt<>(ctx.bakeLayer(ModelAnt.LAYER_LOCATION)), texture, 0.5f);
        this.scale = scale;
    }

    @Override
    protected void scale(T entity, PoseStack stack, float partialTick) {
        super.scale(entity, stack, partialTick);
        stack.scale(this.scale, this.scale, this.scale);
    }
}
