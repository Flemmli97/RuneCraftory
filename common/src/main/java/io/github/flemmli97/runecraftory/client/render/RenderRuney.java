package io.github.flemmli97.runecraftory.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityRuney;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderRuney extends EntityRenderer<EntityRuney> {

    private static final ResourceLocation TEX_1 = new ResourceLocation(RuneCraftory.MODID, "textures/particle/runey_0.png");
    private static final ResourceLocation TEX_2 = new ResourceLocation(RuneCraftory.MODID, "textures/particle/runey_1.png");
    private static final ResourceLocation TEX_3 = new ResourceLocation(RuneCraftory.MODID, "textures/particle/runey_2.png");
    private static final ResourceLocation TEX_4 = new ResourceLocation(RuneCraftory.MODID, "textures/particle/runey_3.png");

    protected final RenderUtils.TextureBuilder textureBuilder = new RenderUtils.TextureBuilder();

    public RenderRuney(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(EntityRuney entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        this.textureBuilder.setLight(packedLight);
        this.textureBuilder.setColor(255, 255, 255, Math.min(entity.tickCount * 10, 255));
        stack.pushPose();
        stack.translate(0, 0.25, 0);
        stack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        stack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        stack.translate(0, Mth.sin(entity.tickCount * 0.1f) * 0.1, 0);
        RenderUtils.renderTexture(stack, buffer.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(entity))), 0.7f, 0.7f, this.textureBuilder);
        stack.popPose();
        super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityRuney entity) {
        return switch (entity.type()) {
            case 0 -> TEX_1;
            case 1 -> TEX_2;
            case 2 -> TEX_3;
            default -> TEX_4;
        };
    }
}
