package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.misc.DarkBulletEntity;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class DarkBulletRender<T extends DarkBulletEntity> extends EntityRenderer<T> {

    private static final ResourceLocation TEX = RuneCraftory.modRes("textures/entity/projectile/dark_bullet.png");

    protected final RenderUtils.TextureBuilder textureBuilder = new RenderUtils.TextureBuilder();

    public DarkBulletRender(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(T entity, float rotation, float partialTick, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        this.textureBuilder.setLight(packedLight);
        this.textureBuilder.setColor(1, 1, 1, 1f);
        stack.pushPose();
        stack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, entity.yRotO, entity.getYRot()) + 90.0f));
        stack.mulPose(Axis.ZP.rotationDegrees(-Mth.lerp(partialTick, entity.xRotO, entity.getXRot())));
        stack.translate(0, 0.125, 0);
        stack.mulPose(Axis.XP.rotationDegrees(45.0f));
        for (int r = 0; r < 4; ++r) {
            stack.mulPose(Axis.XP.rotationDegrees(90.0f));
            RenderUtils.renderTexture(stack, buffer.getBuffer(RenderType.entityCutout(this.getTextureLocation(entity))), 0.35f, 0.35f, this.textureBuilder);
        }
        stack.popPose();
        super.render(entity, rotation, partialTick, stack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return TEX;
    }

    protected RenderType getRenderType(T entity, ResourceLocation loc) {
        return RenderType.entityCutoutNoCull(loc);
    }
}
