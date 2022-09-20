package io.github.flemmli97.runecraftory.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import net.minecraft.client.model.EntityModel;
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
    protected void setupRotations(T entityLiving, PoseStack matrixStack, float ageInTicks, float rotationYaw, float partialTicks) {
        super.setupRotations(entityLiving, matrixStack, ageInTicks, rotationYaw, partialTicks);
        if (entityLiving.getPlayDeathTick() > 0) {
            float partial = partialTicks - 1;
            float f = (entityLiving.getPlayDeathTick() + (entityLiving.playDeath() ? partial : -partial)) / 20.0f * 1.6f;
            if ((f = Mth.sqrt(f)) > 1.0f) {
                f = 1.0f;
            }
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(f * this.getFlipDegrees(entityLiving)));
        }
    }
}
