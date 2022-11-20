package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityDarkBullet;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderDarkBullet<T extends EntityDarkBullet> extends EntityRenderer<T> {

    private static final ResourceLocation TEX = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/dark_bullet.png");

    protected final RenderUtils.TextureBuilder textureBuilder = new RenderUtils.TextureBuilder();

    public RenderDarkBullet(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(T entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        this.textureBuilder.setLight(packedLight);
        this.textureBuilder.setColor(1, 1, 1, 1f);
        stack.pushPose();
        stack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) + 90.0f));
        stack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
        stack.translate(0, 0.125, 0);
        stack.mulPose(Vector3f.XP.rotationDegrees(45.0f));
        for (int r = 0; r < 4; ++r) {
            stack.mulPose(Vector3f.XP.rotationDegrees(90.0f));
            RenderUtils.renderTexture(stack, buffer.getBuffer(RenderType.entityCutout(this.getTextureLocation(entity))), 0.35f, 0.35f, this.textureBuilder);
        }
        stack.popPose();
        super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return TEX;
    }

    protected RenderType getRenderType(T entity, ResourceLocation loc) {
        return RenderType.entityCutoutNoCull(loc);
    }

}
