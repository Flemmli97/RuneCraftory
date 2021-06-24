package io.github.flemmli97.runecraftory.client.render;

import com.flemmli97.tenshilib.client.render.RenderUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.ModelGate;
import io.github.flemmli97.runecraftory.common.entities.GateEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class RenderGate extends LivingRenderer<GateEntity, ModelGate> {

    private static final ResourceLocation none =  new ResourceLocation(RuneCraftory.MODID, "textures/entity/gate_none.png");
    private static final ResourceLocation fire =  new ResourceLocation(RuneCraftory.MODID, "textures/entity/gate_fire.png");
    private static final ResourceLocation water =  new ResourceLocation(RuneCraftory.MODID, "textures/entity/gate_water.png");
    private static final ResourceLocation earth =  new ResourceLocation(RuneCraftory.MODID, "textures/entity/gate_earth.png");
    private static final ResourceLocation wind =  new ResourceLocation(RuneCraftory.MODID, "textures/entity/gate_wind.png");
    private static final ResourceLocation light =  new ResourceLocation(RuneCraftory.MODID, "textures/entity/gate_light.png");
    private static final ResourceLocation dark =  new ResourceLocation(RuneCraftory.MODID, "textures/entity/gate_dark.png");
    private static final ResourceLocation love =  new ResourceLocation(RuneCraftory.MODID, "textures/entity/gate_love.png");
    protected final RenderUtils.TextureBuilder textureBuilder = new RenderUtils.TextureBuilder();

    public RenderGate(EntityRendererManager renderManager) {
        super(renderManager, new ModelGate(), 0);
    }

    @Override
    public ResourceLocation getEntityTexture(GateEntity entity) {
        switch (entity.getElement()) {
            case NONE: return none;
            case WATER: return water;
            case EARTH: return earth;
            case WIND: return wind;
            case FIRE: return  fire;
            case LIGHT: return light;
            case DARK: return dark;
            case LOVE:return love;
        }
        return none;
    }

    @Override
    public void render(GateEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        /*ParticleStatus status = Minecraft.getInstance().gameSettings.particles;
        if (status == ParticleStatus.ALL) {
            if (entity.clientParticleFlag && entity.ticksExisted % 2 == 0) {
                int color = entity.entityElement().getParticleColor();
                float r = (color >> 16 & 255) / 255.0F;
                float g = (color >> 8 & 255) / 255.0F;
                float b = (color & 255) / 255.0F;
                entity.world.addOptionalParticle(new ColoredParticleData4f(ModParticles.vortex.get(),
                        r, g, b, 1, 3,
                        0, 0.01f, entity.clientParticles, 5), true, entity.getPosX(), entity.getPosY() + entity.getHeight() * 0.5, entity.getPosZ(), 0, 0, 0);
                entity.clientParticleFlag = false;
            }
        } else if (status == ParticleStatus.DECREASED) {
            if (entity.clientParticleFlag && entity.ticksExisted % 2 == 0) {
                int color = entity.entityElement().getParticleColor();
                float r = (color >> 16 & 255) / 255.0F;
                float g = (color >> 8 & 255) / 255.0F;
                float b = (color & 255) / 255.0F;
                entity.world.addOptionalParticle(new ColoredParticleData4f(ModParticles.vortex.get(),
                        r, g, b, 1, 3,
                        0, 0.01f, entity.clientParticles, 5), entity.getPosX(), entity.getPosY() + entity.getHeight() * 0.5, entity.getPosZ(), 0, 0, 0);
                entity.clientParticleFlag = false;
            }
        } else
            super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);*/
        matrixStackIn.push();
        float scale = 1.2f+ MathHelper.sin(entity.ticksExisted * 0.1f) * 0.04f;
        matrixStackIn.scale(scale, scale, scale);
        matrixStackIn.translate(0, entity.getHeight() * 0.5-0.1, 0);
        matrixStackIn.rotate(this.renderManager.getCameraOrientation());
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(entity.clientParticles * 0.1f));

        this.textureBuilder.setOverlay(getPackedOverlay(entity, this.getOverlayProgress(entity, partialTicks)));
        this.textureBuilder.setLight(packedLightIn);
        RenderUtils.renderTexture(matrixStackIn, bufferIn.getBuffer(RenderType.getEntityCutoutNoCull(this.getEntityTexture(entity))), 1, 1, this.textureBuilder);
        matrixStackIn.pop();
    }

    @Override
    protected int getBlockLight(GateEntity entityIn, BlockPos pos) {
        return 15;
    }

    /*@Override
    protected void preRenderCallback(GateEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        if (Minecraft.getInstance().gameSettings.particles == ParticleStatus.DECREASED) {
            matrixStackIn.scale(0.3f, 0.3f, 0.3f);
            matrixStackIn.translate(0, -1, 0);
        }
    }*/

    @Override
    protected boolean canRenderName(GateEntity entity) {
        return false;
    }

    @Override
    protected float getDeathMaxRotation(GateEntity entityLivingBaseIn) {
        return 0;
    }
}
