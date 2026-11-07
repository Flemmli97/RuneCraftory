package io.github.flemmli97.runecraftory.client.particles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.flemmli97.runecraftory.client.model.monster.SkelefangModel;
import io.github.flemmli97.runecraftory.client.render.monster.SkelefangRender;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.Skelefang;
import io.github.flemmli97.runecraftory.common.particles.SkelefangParticleData;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.PartPose;
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

    private static final SkelefangModel<Skelefang> MODEL = particleModel();

    private static final RenderType RENDER_TYPE = MODEL.renderType(SkelefangRender.TEXTURE);

    private final SkelefangParticleData.SkelefangBoneType boneType;
    private final float initialRotX, initialRotY, pitchSpin, yawSpin;
    private int groundTick;

    public SkelefangParticle(ClientLevel level, double x, double y, double z, double dirX, double dirY, double dirZ, SkelefangParticleData data) {
        super(level, x, y, z);
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
        ModelPartsContainer.ModelPartExtended part = switch (this.boneType) {
            case TAIL -> MODEL.tail;
            case TAIL_BASE -> MODEL.tailBase;
            case LEFT_LEG -> MODEL.leftLegBase;
            case RIGHT_LEG -> MODEL.rightLegBase;
            case HEAD -> MODEL.head;
            case NECK -> MODEL.neck;
            case BACK -> MODEL.spineBack;
            case BACK_RIBS -> MODEL.ribsSpine;
            case FRONT -> MODEL.spineFront;
            case FRONT_RIBS -> MODEL.ribsBody;
            default -> null;
        };
        //Offset pos based on part
        if (part != null) {
            part.resetAll();
            PartPose pose = part.storePose(true);
            Vec3 offset = new Vec3(pose.x / 16, pose.y / 16, pose.z / 16)
                    .yRot(Mth.wrapDegrees(this.initialRotY) * Mth.DEG_TO_RAD);
            this.setPos(this.x + offset.x(), this.y - offset.y(), this.z - offset.z());
            this.xo = this.x;
            this.yo = this.y;
            this.zo = this.z;
        }
    }

    public static SkelefangModel<Skelefang> particleModel() {
        return new SkelefangModel<>(RenderType::entityTranslucentCull);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.onGround && this.groundTick == 0) {
            this.groundTick = this.age;
        }
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTick) {
        Vec3 vec3 = renderInfo.getPosition();
        float x = (float) (Mth.lerp(partialTick, this.xo, this.x) - vec3.x());
        float y = (float) (Mth.lerp(partialTick, this.yo, this.y) - vec3.y());
        float z = (float) (Mth.lerp(partialTick, this.zo, this.z) - vec3.z());
        PoseStack stack = new PoseStack();
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
        float yaw = Mth.lerp(partialTick, this.initialRotY + this.yawSpin * spinAge, this.initialRotY + this.yawSpin * next);
        float pitch = Mth.lerp(partialTick, this.initialRotX + this.pitchSpin * spinAge, this.initialRotX + this.pitchSpin * next);
        stack.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
        stack.mulPose(Axis.XP.rotationDegrees(pitch));
        // The flipping of models is a forever enigma... like why only y here...
        // And also no 1.5 offset
        stack.scale(1.0F, -1.0F, 1.0F);
        float alpha = 0.9f - this.age * 07f / this.lifetime;
        BlockPos pos = BlockPos.containing(this.x, this.y, this.z);
        int block = this.level.getBrightness(LightLayer.BLOCK, pos);
        int light = this.level.getBrightness(LightLayer.SKY, pos);
        VertexConsumer consumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RENDER_TYPE);
        MODEL.renderAsParticle(stack, consumer, this.boneType, LightTexture.pack(block, light),
                OverlayTexture.NO_OVERLAY, FastColor.ARGB32.color((int) (alpha * 255), CommonColors.WHITE));
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
