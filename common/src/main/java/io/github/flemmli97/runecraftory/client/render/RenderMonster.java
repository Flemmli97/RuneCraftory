package io.github.flemmli97.runecraftory.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderMonster<T extends BaseMonster, M extends EntityModel<T>> extends MobRenderer<T, M> {

    private final ResourceLocation tex;

    public RenderMonster(EntityRendererProvider.Context ctx, M model, ResourceLocation texture, float shadow) {
        super(ctx, model, shadow);
        this.tex = texture;
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return this.tex;
    }

    @Override
    protected void setupRotations(T entity, PoseStack stack, float ageInTicks, float rotationYaw, float partialTicks) {
        super.setupRotations(entity, stack, ageInTicks, rotationYaw, partialTicks);
        if (entity.getPlayDeathTick() > 0 && entity.getDeathAnimation() == null) {
            float f = (entity.getPlayDeathTick() + (entity.playDeath() ? partialTicks : -partialTicks)) / 20.0f * 1.6f;
            if ((f = Mth.sqrt(f)) > 1.0f) {
                f = 1.0f;
            }
            stack.translate(0, f * 0.1, -f * entity.getBbHeight() * 0.5);
            stack.mulPose(Vector3f.XP.rotationDegrees(f * this.getFlipDegrees(entity)));
        }
    }

    @Override
    public boolean shouldRender(T entity, Frustum camera, double camX, double camY, double camZ) {
        if (entity.getPlayDeathTick() > 0 && !entity.playDeath()) {
            if (entity.getPlayDeathTick() > 8) {
                if (entity.getPlayDeathTick() % 2 == 0)
                    return false;
            }
            if (entity.getPlayDeathTick() % 3 == 0)
                return false;
        }
        return super.shouldRender(entity, camera, camX, camY, camZ);
    }
}
