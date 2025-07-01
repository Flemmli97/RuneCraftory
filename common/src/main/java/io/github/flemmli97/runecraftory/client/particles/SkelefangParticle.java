package io.github.flemmli97.runecraftory.client.particles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.monster.ModelSkelefang;
import io.github.flemmli97.runecraftory.client.render.monster.RenderSkelefang;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntitySkelefang;
import io.github.flemmli97.runecraftory.common.particles.SkelefangParticleData;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import io.github.flemmli97.tenshilib.client.model.PoseExtended;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.CommonColors;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;

public class SkelefangParticle extends Particle {

    private static final ModelSkelefang<EntitySkelefang> MODEL = particleModel();

    private static final RenderType RENDER_TYPE = MODEL.renderType(RenderSkelefang.TEXTURE);

    private final SkelefangParticleData.SkelefangBoneType boneType;
    private final float initialRotX, initialRotY, pitchSpin, yawSpin;
    private int groundTick;

    private float cameraLastPitch, cameraLastYaw;
    private boolean first = true;

    public SkelefangParticle(ClientLevel world, double x, double y, double z, double dirX, double dirY, double dirZ, SkelefangParticleData data) {
        super(world, x, y, z);
        this.setParticleSpeed(dirX, dirY, dirZ);
        if (data.hasGravity())
            this.gravity = 1;
        this.speedUpWhenYMotionIsBlocked = false;
        this.boneType = data.getBoneType();
        this.initialRotX = data.getInitialRotX();
        this.initialRotY = data.getInitialRotY();
        this.pitchSpin = data.getPitchSpin();
        this.yawSpin = data.getYawSpin();
        this.lifetime = data.getMaxTime();
        ModelPartsContainer.ModelPartExtended[] parts = switch (this.boneType) {
            case TAIL ->
                    new ModelPartsContainer.ModelPartExtended[]{MODEL.body, MODEL.spineBack, MODEL.tailBase, MODEL.tail};
            case TAIL_BASE -> new ModelPartsContainer.ModelPartExtended[]{MODEL.body, MODEL.spineBack, MODEL.tailBase};
            case LEFT_LEG ->
                    new ModelPartsContainer.ModelPartExtended[]{MODEL.body, MODEL.spineBack, MODEL.leftLegBase};
            case RIGHT_LEG ->
                    new ModelPartsContainer.ModelPartExtended[]{MODEL.body, MODEL.spineBack, MODEL.rightLegBase};
            case HEAD ->
                    new ModelPartsContainer.ModelPartExtended[]{MODEL.body, MODEL.spineFront, MODEL.neck, MODEL.head};
            case NECK -> new ModelPartsContainer.ModelPartExtended[]{MODEL.body, MODEL.spineFront, MODEL.neck};
            case BACK -> new ModelPartsContainer.ModelPartExtended[]{MODEL.body, MODEL.spineBack};
            case BACK_RIBS -> new ModelPartsContainer.ModelPartExtended[]{MODEL.body, MODEL.spineBack, MODEL.ribsSpine};
            case FRONT -> new ModelPartsContainer.ModelPartExtended[]{MODEL.body, MODEL.spineFront};
            case FRONT_RIBS ->
                    new ModelPartsContainer.ModelPartExtended[]{MODEL.body, MODEL.spineFront, MODEL.ribsBody};
            default -> null;
        };
        //Offset pos based on part
        if (parts != null) {
            double mX = 0;
            double mY = 0;
            double mZ = 0;
            for (ModelPartsContainer.ModelPartExtended part : parts) {
                mX += part.x;
                mY += part.y;
                mZ += part.z;
            }
            Vec3 offset = new Vec3(mX, mY - 22.75, mZ)
                    .yRot(Mth.wrapDegrees(this.initialRotY) * Mth.DEG_TO_RAD);
            this.setPos(this.x + offset.x() * 1 / 16d, this.y - offset.y() * 1 / 16d, this.z - offset.z() * 1 / 16d);
            this.xo = this.x;
            this.yo = this.y;
            this.zo = this.z;
        }
    }

    public static ModelSkelefang<EntitySkelefang> particleModel() {
        ModelSkelefang<EntitySkelefang> model = new ModelSkelefang<>(RenderType::entityTranslucentCull);
        model.getModel().getRoot().loadPoseRecursive(PoseExtended.ZERO);
        return model;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.onGround && this.groundTick == 0) {
            this.groundTick = this.age;
        }
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        if (this.first) {
            this.cameraLastPitch = renderInfo.getXRot();
            this.cameraLastYaw = renderInfo.getYRot() - 180;
            this.first = false;
        }
        Vec3 vec3 = renderInfo.getPosition();
        float x = (float) (Mth.lerp(partialTicks, this.xo, this.x) - vec3.x());
        float y = (float) (Mth.lerp(partialTicks, this.yo, this.y) - vec3.y());
        float z = (float) (Mth.lerp(partialTicks, this.zo, this.z) - vec3.z());
        PoseStack stack = new PoseStack();
        this.irisFix(stack, renderInfo, partialTicks);
        stack.translate(x, y, z);
        int next;
        int spinAge;
        if (this.groundTick > 0 && this.age > this.groundTick) {
            next = this.groundTick;
            spinAge = this.groundTick;
        } else {
            next = this.age + 1;
            spinAge = this.age;
        }
        float yaw = Mth.lerp(partialTicks, this.initialRotY + this.yawSpin * spinAge, this.initialRotY + this.yawSpin * next);
        float pitch = Mth.lerp(partialTicks, this.initialRotX + this.pitchSpin * spinAge, this.initialRotX + this.pitchSpin * next);
        stack.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
        stack.mulPose(Axis.XP.rotationDegrees(pitch));
        stack.scale(-1.0F, -1.0F, 1.0F);
        stack.translate(0.0D, -1.5, 0.0D);
        float alpha = 0.9f - this.age * 07f / this.lifetime;
        BlockPos pos = BlockPos.containing(this.x, this.y, this.z);
        int block = this.level.getBrightness(LightLayer.BLOCK, pos);
        int light = this.level.getBrightness(LightLayer.SKY, pos);
        CustomParticleRenderTypes.batchType(RENDER_TYPE);
        VertexConsumer consumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RENDER_TYPE);
        MODEL.renderAsParticle(stack, consumer, this.boneType, LightTexture.pack(block, light),
                OverlayTexture.NO_OVERLAY, FastColor.ARGB32.color((int) (alpha * 255), CommonColors.WHITE));
    }

    /**
     * Iris does some stuff with camera caching etc. which makes it so we need to do this
     */
    private void irisFix(PoseStack stack, Camera renderInfo, float partialTicks) {
        if (!RuneCraftory.iris)
            return;
        stack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(partialTicks, this.cameraLastPitch, renderInfo.getXRot())));
        stack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, this.cameraLastYaw, renderInfo.getYRot() - 180)));
        this.cameraLastPitch = renderInfo.getXRot();
        this.cameraLastYaw = renderInfo.getYRot() - 180;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    public record SkelefangParticleFactoryBase(SpriteSet sprite) implements ParticleProvider<SkelefangParticleData> {

        @Override
        public Particle createParticle(SkelefangParticleData data, ClientLevel level, double x, double y, double z, double motionX, double motionY, double motionZ) {
            return new SkelefangParticle(level, x, y, z, motionX, motionY, motionZ, data);
        }
    }
}
