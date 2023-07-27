package io.github.flemmli97.runecraftory.client.particles;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.client.model.monster.ModelSkelefang;
import io.github.flemmli97.runecraftory.client.render.monster.RenderSkelefang;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntitySkelefang;
import io.github.flemmli97.runecraftory.common.particles.SkelefangParticleData;
import io.github.flemmli97.tenshilib.client.model.ModelPartHandler;
import io.github.flemmli97.tenshilib.client.model.PoseExtended;
import io.github.flemmli97.tenshilib.common.utils.MathUtils;
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
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class SkelefangParticle extends Particle {

    private static final Supplier<ModelSkelefang<EntitySkelefang>> MODEL = Suppliers.memoize(() -> {
        ModelSkelefang<EntitySkelefang> model = new ModelSkelefang<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelSkelefang.LAYER_LOCATION), RenderType::entityTranslucentCull);
        model.body.loadPoseRecursive(PoseExtended.ZERO);
        return model;
    });

    private final SkelefangParticleData.SkelefangBoneType boneType;
    private final float initialRotX, initialRotY, pitchSpin, yawSpin;
    private int groundTick;

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
        ModelPartHandler.ModelPartExtended[] parts = switch (this.boneType) {
            case TAIL -> new ModelPartHandler.ModelPartExtended[]{MODEL.get().body, MODEL.get().spineBack, MODEL.get().tailBase, MODEL.get().tail};
            case TAIL_BASE -> new ModelPartHandler.ModelPartExtended[]{MODEL.get().body, MODEL.get().spineBack, MODEL.get().tailBase};
            case LEFT_LEG -> new ModelPartHandler.ModelPartExtended[]{MODEL.get().body, MODEL.get().spineBack, MODEL.get().leftLegBase};
            case RIGHT_LEG -> new ModelPartHandler.ModelPartExtended[]{MODEL.get().body, MODEL.get().spineBack, MODEL.get().rightLegBase};
            case HEAD -> new ModelPartHandler.ModelPartExtended[]{MODEL.get().body, MODEL.get().spineFront, MODEL.get().neck, MODEL.get().head};
            case NECK -> new ModelPartHandler.ModelPartExtended[]{MODEL.get().body, MODEL.get().spineFront, MODEL.get().neck};
            case BACK -> new ModelPartHandler.ModelPartExtended[]{MODEL.get().body, MODEL.get().spineBack};
            case BACK_RIBS -> new ModelPartHandler.ModelPartExtended[]{MODEL.get().body, MODEL.get().spineBack, MODEL.get().ribsSpine};
            case FRONT -> new ModelPartHandler.ModelPartExtended[]{MODEL.get().body, MODEL.get().spineFront};
            case FRONT_RIBS -> new ModelPartHandler.ModelPartExtended[]{MODEL.get().body, MODEL.get().spineFront, MODEL.get().ribsBody};
            default -> null;
        };
        //Offset pos based on part
        if (parts != null) {
            double mX = 0;
            double mY = 0;
            double mZ = 0;
            for (ModelPartHandler.ModelPartExtended part : parts) {
                mX += part.x;
                mY += part.y;
                mZ += part.z;
            }
            double[] offset = MathUtils.rotate(0, 1, 0, mX, mY - 22.75, mZ, Mth.DEG_TO_RAD * Mth.wrapDegrees(this.initialRotY));
            this.setPos(this.x + offset[0] * 1 / 16d, this.y - offset[1] * 1 / 16d, this.z - offset[2] * 1 / 16d);
            this.xo = this.x;
            this.yo = this.y;
            this.zo = this.z;
        }
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
        Vec3 vec3 = renderInfo.getPosition();
        float x = (float) (Mth.lerp(partialTicks, this.xo, this.x) - vec3.x());
        float y = (float) (Mth.lerp(partialTicks, this.yo, this.y) - vec3.y());
        float z = (float) (Mth.lerp(partialTicks, this.zo, this.z) - vec3.z());
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
        float yaw = Mth.lerp(partialTicks, this.initialRotY + this.yawSpin * spinAge, this.initialRotY + this.yawSpin * next);
        float pitch = Mth.lerp(partialTicks, this.initialRotX + this.pitchSpin * spinAge, this.initialRotX + this.pitchSpin * next);
        stack.mulPose(Vector3f.YP.rotationDegrees(180.0F - yaw));
        stack.mulPose(Vector3f.XP.rotationDegrees(pitch));
        stack.scale(-1.0F, -1.0F, 1.0F);
        stack.translate(0.0D, -1.5, 0.0D);
        VertexConsumer consumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(MODEL.get().renderType(RenderSkelefang.TEXTURE));
        float alpha = 0.9f - this.age * 07f / this.lifetime;
        BlockPos pos = new BlockPos(this.x, this.y, this.z);
        int block = this.level.getBrightness(LightLayer.BLOCK, pos);
        int light = this.level.getBrightness(LightLayer.SKY, pos);
        MODEL.get().renderAsParticle(stack, consumer, this.boneType, LightTexture.pack(block, light), OverlayTexture.NO_OVERLAY, 1, 1, 1, alpha);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return CustomParticleRenderTypes.ENTITY_MODEL_TYPE;
    }

    public record SkelefangParticleFactoryBase(
            SpriteSet sprite) implements ParticleProvider<SkelefangParticleData> {

        @Override
        public Particle createParticle(SkelefangParticleData data, ClientLevel level, double x, double y, double z, double motionX, double motionY, double motionZ) {
            return new SkelefangParticle(level, x, y, z, motionX, motionY, motionZ, data);
        }
    }
}
