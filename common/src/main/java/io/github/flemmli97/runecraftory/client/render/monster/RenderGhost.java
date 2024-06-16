package io.github.flemmli97.runecraftory.client.render.monster;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.client.model.monster.ModelGhost;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.client.render.ScaledRenderer;
import io.github.flemmli97.runecraftory.client.render.layer.RiderLayerRendererExt;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityGhost;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderGhost<T extends EntityGhost> extends RenderMonster<T, ModelGhost<T>> implements ScaledRenderer {

    public final float scale;

    public RenderGhost(EntityRendererProvider.Context ctx, ResourceLocation texture) {
        this(ctx, texture, 1);
    }

    public RenderGhost(EntityRendererProvider.Context ctx, ResourceLocation texture, float scale) {
        super(ctx, new ModelGhost<>(ctx.bakeLayer(ModelGhost.LAYER_LOCATION)), texture, 0.5f, false);
        this.scale = scale * 0.85f;
        this.layers.add(new RiderLayerRendererExt<>(this, (stack, entity) -> stack.scale(1 / this.scale, 1 / this.scale, 1 / this.scale)));
    }

    @Override
    public boolean shouldRender(T entity, Frustum camera, double camX, double camY, double camZ) {
        AnimatedAction anim = entity.getAnimationHandler().getAnimation();
        if (anim != null && anim.is(EntityGhost.VANISH)) {
            int tick = anim.getTick();
            if (tick < 10 || tick > 90)
                return tick % 8 == 0;
            else if (tick < 20 || tick > 80)
                return tick % 5 == 0;
            else if (tick < 40 || tick > 60)
                return tick % 2 == 0;
            else
                return false;
        }
        return super.shouldRender(entity, camera, camX, camY, camZ);
    }


    @Override
    protected void scale(T entity, PoseStack stack, float partialTick) {
        super.scale(entity, stack, partialTick);
        stack.scale(this.scale, this.scale, this.scale);
    }

    @Override
    public float getScale() {
        return this.scale;
    }
}