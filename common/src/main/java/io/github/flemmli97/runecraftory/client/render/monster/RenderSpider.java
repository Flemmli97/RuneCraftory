package io.github.flemmli97.runecraftory.client.render.monster;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.monster.ModelSpider;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.EntitySpider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderSpider<T extends EntitySpider> extends RenderMonster<T, ModelSpider<T>> {

    public RenderSpider(EntityRendererProvider.Context ctx) {
        super(ctx, new ModelSpider<>(ctx.bakeLayer(ModelSpider.LAYER_LOCATION)), new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/spider.png"), 0.7f);
    }

    @Override
    protected void setupRotations(T entity, PoseStack stack, float ageInTicks, float rotationYaw, float partialTicks) {
        super.setupRotations(entity, stack, ageInTicks, rotationYaw, partialTicks);
        if (entity.climbingTicker >= 0) {
            float f = (entity.climbingTicker + (entity.isClimbing() ? partialTicks : -partialTicks)) / EntitySpider.CLIMB_MAX;
            if (f > 1)
                f = 1;
            stack.mulPose(Vector3f.XP.rotationDegrees(f * 90));
            stack.translate(0, -0.8 * f, -0.2 * f);
        }
    }
}