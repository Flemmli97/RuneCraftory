package io.github.flemmli97.runecraftory.client.particles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.particles.BlockStateParticleData;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class BlockParticle extends Particle {

    private final BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
    private final BlockState state;
    private final float yaw, pitch;

    private float cameraLastPitch, cameraLastYaw;
    private boolean first = true;

    public BlockParticle(ClientLevel level, double x, double y, double z, double motionX, double motionY, double motionZ, BlockState state, float yaw, float pitch, int duration) {
        super(level, x, y, z);
        this.xd = motionX;
        this.yd = motionY;
        this.zd = motionZ;
        this.state = state;
        this.yaw = yaw;
        this.pitch = pitch;
        this.lifetime = duration;
        this.gravity = 0.3f;
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        if (this.state.getRenderShape() != RenderShape.MODEL)
            return;
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
        stack.mulPose(Axis.YP.rotationDegrees(180.0F - this.yaw));
        stack.mulPose(Axis.XP.rotationDegrees(this.pitch));
        BlockPos pos = BlockPos.containing(this.x, this.y, this.z);
        if (this.level.getBlockState(pos).canOcclude())
            pos = pos.above();
        int block = this.level.getBrightness(LightLayer.BLOCK, pos);
        int light = this.level.getBrightness(LightLayer.SKY, pos);
        stack.translate(-0.5, 0, -0.5);
        this.dispatcher.renderSingleBlock(this.state, stack, Minecraft.getInstance().renderBuffers().bufferSource(), LightTexture.pack(block, light), OverlayTexture.NO_OVERLAY);
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

    public record Factory(SpriteSet sprite) implements ParticleProvider<BlockStateParticleData> {

        @Override
        public Particle createParticle(BlockStateParticleData data, ClientLevel level, double x, double y, double z, double motionX, double motionY, double motionZ) {
            return new BlockParticle(level, x, y, z, motionX, motionY, motionZ, data.getState(), data.getYaw(), data.getPitch(), data.getDuration());
        }
    }
}
