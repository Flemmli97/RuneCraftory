package com.flemmli97.runecraftory.mobs.client.render;

import com.flemmli97.runecraftory.mobs.entity.BaseMonster;
import com.flemmli97.runecraftory.mobs.entity.BossMonster;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;

public abstract class RenderMonster<T extends BaseMonster, M extends EntityModel<T>> extends MobRenderer<T, M> {

    public RenderMonster(EntityRendererManager manager, M model) {
        super(manager, model, 0);
    }

    @Override
    protected float getAnimationCounter(BaseMonster entity, float partialTicks) {
        //if (entity instanceof BossMonster)
        //    return ((BossMonster) entity).isEnraged() ? (float) entity.ticksExisted / 7F : 0;
        return 0;
    }
}
