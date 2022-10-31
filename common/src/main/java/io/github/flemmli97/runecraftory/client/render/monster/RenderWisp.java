package io.github.flemmli97.runecraftory.client.render.monster;

import io.github.flemmli97.runecraftory.client.model.monster.ModelWisp;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.wisp.EntityWispBase;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderWisp<T extends EntityWispBase> extends RenderMonster<T, ModelWisp<T>> {

    public RenderWisp(EntityRendererProvider.Context ctx, ResourceLocation tex) {
        super(ctx, new ModelWisp<>(ctx.bakeLayer(ModelWisp.LAYER_LOCATION)), tex, 0);
    }

    @Override
    public boolean shouldRender(T entity, Frustum camera, double camX, double camY, double camZ) {
        AnimatedAction anim = entity.getAnimationHandler().getAnimation();
        if (anim != null && anim.getID().equals(EntityWispBase.vanish.getID())) {
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
}