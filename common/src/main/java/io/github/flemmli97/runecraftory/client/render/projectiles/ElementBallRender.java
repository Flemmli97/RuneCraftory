package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.render.AnimatedTexture;
import io.github.flemmli97.runecraftory.common.entities.misc.ElementalBallEntity;
import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ElementBallRender extends EntityRenderer<ElementalBallEntity> {

    private static final ResourceLocation FIRE = RuneCraftory.modRes("textures/entity/projectile/fireball_n.png");
    private static final ResourceLocation WATER = RuneCraftory.modRes("textures/entity/projectile/bubble.png");
    private static final ResourceLocation WATER_2 = RuneCraftory.modRes("textures/entity/projectile/ice_ball.png");
    private static final ResourceLocation EARTH = RuneCraftory.modRes("textures/entity/projectile/earth_ball.png");
    private static final ResourceLocation LOVE = RuneCraftory.modRes("textures/entity/projectile/love_ball.png");
    private static final ResourceLocation WIND = RuneCraftory.modRes("textures/entity/projectile/wind_blade.png");
    private static final ResourceLocation BLOB = RuneCraftory.modRes("textures/particle/light.png");

    public final float xSize = 1, ySize = 1;

    protected final RenderUtils.TextureBuilder textureBuilder = new RenderUtils.TextureBuilder();

    protected final AnimatedTexture fireTexAnim = new AnimatedTexture(6, 1);
    protected final AnimatedTexture windTexAnim = new AnimatedTexture(8, 1);

    public ElementBallRender(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(ElementalBallEntity entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        stack.mulPose(Axis.YP.rotationDegrees(180));
        AnimatedTexture text = null;
        switch (entity.getElement()) {
            case FIRE -> text = this.fireTexAnim;
            case WIND -> text = this.windTexAnim;
            case DARK -> this.textureBuilder.setColor(0xff5d17a3);
            case LIGHT -> this.textureBuilder.setColor(0xfff4f788);
        }
        if (text != null) {
            float[] uvOffset = text.uvOffset((int) ((entity.tickCount + entity.getId()) * 0.5));
            this.textureBuilder.setUV(uvOffset[0], uvOffset[1]);
            this.textureBuilder.setUVLength(text.uLength, text.vLength);
        } else {
            this.textureBuilder.setUV(0, 0);
            this.textureBuilder.setUVLength(1, 1);
        }
        this.textureBuilder.setLight(packedLight);
        RenderUtils.renderTexture(stack, buffer.getBuffer(this.getRenderType(entity, this.getTextureLocation(entity))), this.xSize, this.ySize, this.textureBuilder);
        super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ElementalBallEntity entity) {
        return switch (entity.getElement()) {
            case WATER -> entity.getVariant() == 1 ? WATER_2 : WATER;
            case LOVE -> LOVE;
            case EARTH -> EARTH;
            case WIND -> WIND;
            case DARK, LIGHT -> BLOB;
            default -> FIRE;
        };
    }

    protected RenderType getRenderType(ElementalBallEntity entity, ResourceLocation loc) {
        if (entity.getElement() == ItemElement.DARK || entity.getElement() == ItemElement.LIGHT)
            return RenderType.entityTranslucent(loc);
        return RenderType.entityCutoutNoCull(loc);
    }
}
