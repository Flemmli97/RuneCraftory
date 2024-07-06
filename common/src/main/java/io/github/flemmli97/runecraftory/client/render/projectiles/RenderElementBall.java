package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.client.render.AnimatedTexture;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityElementalBall;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderElementBall extends EntityRenderer<EntityElementalBall> {

    private static final ResourceLocation FIRE = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/fireball_n.png");
    private static final ResourceLocation WATER = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/bubble.png");
    private static final ResourceLocation WATER_2 = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/ice_ball.png");
    private static final ResourceLocation EARTH = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/earth_ball.png");
    private static final ResourceLocation LOVE = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/staff_love.png");

    public final float xSize = 1, ySize = 1;

    protected final RenderUtils.TextureBuilder textureBuilder = new RenderUtils.TextureBuilder();

    protected final AnimatedTexture fireTexAnim = new AnimatedTexture(6, 1);

    public RenderElementBall(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(EntityElementalBall entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        stack.mulPose(Vector3f.YP.rotationDegrees(180));
        if (entity.getElement() == EnumElement.FIRE) {
            float[] uvOffset = this.fireTexAnim.uvOffset((int) (entity.tickCount * 0.5));
            this.textureBuilder.setUV(uvOffset[0], uvOffset[1]);
            this.textureBuilder.setUVLength(this.fireTexAnim.uLength, this.fireTexAnim.vLength);
        } else {
            this.textureBuilder.setUV(0, 0);
            this.textureBuilder.setUVLength(1, 1);
        }
        this.textureBuilder.setLight(packedLight);
        RenderUtils.renderTexture(stack, buffer.getBuffer(this.getRenderType(entity, this.getTextureLocation(entity))), this.xSize, this.ySize, this.textureBuilder);
        super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityElementalBall entity) {
        return switch (entity.getElement()) {
            case WATER -> entity.getVariant() == 1 ? WATER_2 : WATER;
            case LOVE -> LOVE;
            case EARTH -> EARTH;
            default -> FIRE;
        };
    }

    protected RenderType getRenderType(EntityElementalBall entity, ResourceLocation loc) {
        return RenderType.entityCutoutNoCull(loc);
    }
}
