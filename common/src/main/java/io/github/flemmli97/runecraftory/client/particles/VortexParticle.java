package io.github.flemmli97.runecraftory.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.common.particles.ColoredParticleData4f;
import io.github.flemmli97.tenshilib.client.particles.ParticleRenderTypes;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import io.github.flemmli97.tenshilib.common.utils.MathUtils;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class VortexParticle extends TextureSheetParticle {

    public final SpriteSet spriteProvider;
    private float[][] points;
    private float angleInc, radInc, currentRadius, currentAngle, subOffset;
    private boolean renderOpposite;

    public VortexParticle(ClientLevel world, double x, double y, double z, float radius, float radiusInc, float angleOffset, float angleInc,
                          ColoredParticleData colorData, SpriteSet sprite, int maxAge, float minAgeRand, float maxAgeRand, boolean renderOpposite, int amount) {
        super(world, x, y, z);
        if (amount < 1)
            amount = 1;
        this.setColor(colorData.getRed(), colorData.getGreen(), colorData.getBlue());
        this.setAlpha(colorData.getAlpha());
        float mult = Mth.nextFloat(world.random, minAgeRand, maxAgeRand);
        this.lifetime = (int) (maxAge * mult);
        this.spriteProvider = sprite;
        this.setSpriteFromAge(this.spriteProvider);
        this.quadSize *= colorData.getScale();
        this.angleInc = angleInc;
        this.currentAngle = this.angleInc + angleOffset;
        this.points = new float[amount][];
        this.subOffset = 360 / (float) (amount * (renderOpposite ? 2 : 1));
        for (int i = 0; i < this.points.length; i++) {
            this.points[i] = MathUtils.rotate(0, 0, 1, radius, radius, 0, MathUtils.degToRad(this.currentAngle + this.subOffset * i));
        }
        this.currentRadius = radius;
        this.radInc = radiusInc;
        this.renderOpposite = renderOpposite;
    }

    @Override
    public void tick() {
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.spriteProvider);
            this.currentAngle += this.angleInc;
            this.currentRadius += this.radInc;
            for (int i = 0; i < this.points.length; i++) {
                this.points[i] = MathUtils.rotate(0, 0, 1, this.currentRadius, this.currentRadius, 0, MathUtils.degToRad(this.currentAngle + this.subOffset * i));
            }
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderTypes.TRANSLUCENTADD;
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        Vec3 vector3d = renderInfo.getPosition();
        float f = (float) (Mth.lerp(partialTicks, this.xo, this.x) - vector3d.x());
        float f1 = (float) (Mth.lerp(partialTicks, this.yo, this.y) - vector3d.y());
        float f2 = (float) (Mth.lerp(partialTicks, this.zo, this.z) - vector3d.z());
        Quaternion quaternion;
        if (this.roll == 0.0F) {
            quaternion = renderInfo.rotation();
        } else {
            quaternion = new Quaternion(renderInfo.rotation());
            float f3 = Mth.lerp(partialTicks, this.oRoll, this.roll);
            quaternion.mul(Vector3f.ZP.rotation(f3));
        }
        float f4 = this.getQuadSize(partialTicks);
        for (float[] floats : this.points) {
            Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
            float[] point = floats;
            Vector3f offset = new Vector3f(point[0], point[1], point[2]);
            offset.transform(quaternion);

            Vector3f[] opposite = new Vector3f[4];
            Vector3f offSetOpp = offset.copy();
            offSetOpp.mul(-1);
            for (int i = 0; i < 4; ++i) {
                Vector3f vector3f = avector3f[i];
                vector3f.transform(quaternion);
                vector3f.mul(f4);
                vector3f.add(f, f1, f2);
                opposite[i] = vector3f.copy();
                vector3f.add(offset);
                opposite[i].add(offSetOpp);
            }

            float f7 = this.getU0();
            float f8 = this.getU1();
            float f5 = this.getV0();
            float f6 = this.getV1();
            int j = 0x500050;
            buffer.vertex(avector3f[0].x(), avector3f[0].y(), avector3f[0].z()).uv(f8, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
            buffer.vertex(avector3f[1].x(), avector3f[1].y(), avector3f[1].z()).uv(f8, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
            buffer.vertex(avector3f[2].x(), avector3f[2].y(), avector3f[2].z()).uv(f7, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
            buffer.vertex(avector3f[3].x(), avector3f[3].y(), avector3f[3].z()).uv(f7, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();

            if (this.renderOpposite) {
                buffer.vertex(opposite[0].x(), opposite[0].y(), opposite[0].z()).uv(f8, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
                buffer.vertex(opposite[1].x(), opposite[1].y(), opposite[1].z()).uv(f8, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
                buffer.vertex(opposite[2].x(), opposite[2].y(), opposite[2].z()).uv(f7, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
                buffer.vertex(opposite[3].x(), opposite[3].y(), opposite[3].z()).uv(f7, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
            }
        }
    }

    @Override
    public float getQuadSize(float partialTicks) {
        return this.quadSize * Mth.sin((this.age + partialTicks) * (float) (Math.PI / this.lifetime * 2));//Mth.clamp(((float) this.age + partialTicks) / (float) this.maxAge * 8.0F, 0.0F, 1.0F);
    }

    public record VortexFactoryBase(
            SpriteSet sprite) implements ParticleProvider<ColoredParticleData4f> {

        @Override
        public Particle createParticle(ColoredParticleData4f data, ClientLevel level, double x, double y, double z, double motX, double motY, double motZ) {
            return new VortexParticle(level, x, y, z, data.getRadius(), data.getInc(), data.getOffset(), data.getAngle(), data, this.sprite, 40, 0.9f, 1.1f, true, 4);
        }
    }
}
