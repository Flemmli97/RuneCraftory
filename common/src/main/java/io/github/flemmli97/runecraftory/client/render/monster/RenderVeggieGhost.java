package io.github.flemmli97.runecraftory.client.render.monster;

import io.github.flemmli97.runecraftory.client.model.monster.ModelVeggieGhost;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityVeggieGhost;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderVeggieGhost<T extends EntityVeggieGhost> extends RenderMonster<T, ModelVeggieGhost<T>> {

    public RenderVeggieGhost(EntityRendererProvider.Context ctx, ResourceLocation texture) {
        super(ctx, new ModelVeggieGhost<>(), texture, 0);
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