package io.github.flemmli97.runecraftory.client.render.monster;

import io.github.flemmli97.runecraftory.client.model.monster.ModelVeggieGhost;
import io.github.flemmli97.runecraftory.client.render.ScaledRenderer;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityVeggieGhost;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderVeggieGhost<T extends EntityVeggieGhost> extends ScaledEntityRenderer<T, ModelVeggieGhost<T>> implements ScaledRenderer {

    public RenderVeggieGhost(EntityRendererProvider.Context ctx, ResourceLocation texture) {
        this(ctx, texture, 1);
    }

    public RenderVeggieGhost(EntityRendererProvider.Context ctx, ResourceLocation texture, float scale) {
        super(ctx, new ModelVeggieGhost<>(), texture, scale, 0);
    }

    @Override
    public boolean shouldRender(T entity, Frustum camera, double camX, double camY, double camZ) {
        AnimationState anim = entity.getAnimationHandler().getAnimation();
        if (anim != null && anim.is(EntityVeggieGhost.VANISH)) {
            int tick = (int) anim.getTick(1);
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
}