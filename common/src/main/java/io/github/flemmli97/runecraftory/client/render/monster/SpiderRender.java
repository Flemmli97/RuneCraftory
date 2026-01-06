package io.github.flemmli97.runecraftory.client.render.monster;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.monster.SpiderModel;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.Spider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SpiderRender<T extends Spider> extends RenderMonster<T, SpiderModel<T>> {

    public SpiderRender(EntityRendererProvider.Context ctx) {
        super(ctx, new SpiderModel<>(), RuneCraftory.modRes("textures/entity/monsters/spider.png"), 0.7f);
    }

    @Override
    protected void setupRotations(T entity, PoseStack stack, float ageInTicks, float rotationYaw, float partialTick, float scale) {
        super.setupRotations(entity, stack, ageInTicks, rotationYaw, partialTick, scale);
        if (entity.climbingTicker >= 0) {
            float f = (entity.climbingTicker + (entity.isClimbing() ? partialTick : -partialTick)) / Spider.CLIMB_MAX;
            if (f > 1)
                f = 1;
            stack.mulPose(Axis.XP.rotationDegrees(f * 90));
            stack.translate(0, -0.8 * f, -0.2 * f);
        }
    }
}