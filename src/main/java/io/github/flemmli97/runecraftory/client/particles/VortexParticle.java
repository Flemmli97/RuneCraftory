package io.github.flemmli97.runecraftory.client.particles;

import com.flemmli97.tenshilib.common.utils.MathUtils;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.flemmli97.runecraftory.common.particles.ColoredParticleData;
import io.github.flemmli97.runecraftory.common.particles.ColoredParticleData4f;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

public class VortexParticle extends SpriteTexturedParticle {

    public final IAnimatedSprite spriteProvider;
    private float[][] points;
    private float angleInc, radInc, currentRadius, currentAngle, subOffset;
    private boolean renderOpposite;

    public VortexParticle(ClientWorld world, double x, double y, double z, float radius, float radiusInc, float angleOffset, float angleInc,
                          ColoredParticleData colorData, IAnimatedSprite sprite, int maxAge, float minAgeRand, float maxAgeRand, boolean renderOpposite, int amount) {
        super(world, x, y, z);
        if (amount < 1)
            amount = 1;
        this.setColor(colorData.getRed(), colorData.getGreen(), colorData.getBlue());
        this.setAlphaF(colorData.getAlpha());
        float mult = MathHelper.nextFloat(world.rand, minAgeRand, maxAgeRand);
        this.maxAge = (int) (maxAge * mult);
        this.spriteProvider = sprite;
        this.selectSpriteWithAge(this.spriteProvider);
        this.particleScale *= colorData.getScale();
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
        if (this.age++ >= this.maxAge) {
            this.setExpired();
        } else {
            this.selectSpriteWithAge(this.spriteProvider);
            this.currentAngle += this.angleInc;
            this.currentRadius += this.radInc;
            for (int i = 0; i < this.points.length; i++) {
                this.points[i] = MathUtils.rotate(0, 0, 1, this.currentRadius, this.currentRadius, 0, MathUtils.degToRad(this.currentAngle + this.subOffset * i));
            }
        }
    }

    @Override
    public float getScale(float partialTicks) {
        return this.particleScale * MathHelper.sin((this.age + partialTicks) * (float) (Math.PI / this.maxAge * 2));//MathHelper.clamp(((float) this.age + partialTicks) / (float) this.maxAge * 8.0F, 0.0F, 1.0F);
    }

    @Override
    public IParticleRenderType getRenderType() {
        return ParticleRenderTypes.TRANSLUCENTADD;
    }

    @Override
    public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks) {
        Vector3d vector3d = renderInfo.getProjectedView();
        float f = (float) (MathHelper.lerp(partialTicks, this.prevPosX, this.posX) - vector3d.getX());
        float f1 = (float) (MathHelper.lerp(partialTicks, this.prevPosY, this.posY) - vector3d.getY());
        float f2 = (float) (MathHelper.lerp(partialTicks, this.prevPosZ, this.posZ) - vector3d.getZ());
        Quaternion quaternion;
        if (this.particleAngle == 0.0F) {
            quaternion = renderInfo.getRotation();
        } else {
            quaternion = new Quaternion(renderInfo.getRotation());
            float f3 = MathHelper.lerp(partialTicks, this.prevParticleAngle, this.particleAngle);
            quaternion.multiply(Vector3f.ZP.rotation(f3));
        }
        float f4 = this.getScale(partialTicks);
        for (int p = 0; p < this.points.length; p++) {
            Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
            float[] point = this.points[p];
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

            float f7 = this.getMinU();
            float f8 = this.getMaxU();
            float f5 = this.getMinV();
            float f6 = this.getMaxV();
            int j = 0x500050;
            buffer.pos(avector3f[0].getX(), avector3f[0].getY(), avector3f[0].getZ()).tex(f8, f6).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j).endVertex();
            buffer.pos(avector3f[1].getX(), avector3f[1].getY(), avector3f[1].getZ()).tex(f8, f5).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j).endVertex();
            buffer.pos(avector3f[2].getX(), avector3f[2].getY(), avector3f[2].getZ()).tex(f7, f5).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j).endVertex();
            buffer.pos(avector3f[3].getX(), avector3f[3].getY(), avector3f[3].getZ()).tex(f7, f6).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j).endVertex();

            if (this.renderOpposite) {
                buffer.pos(opposite[0].getX(), opposite[0].getY(), opposite[0].getZ()).tex(f8, f6).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j).endVertex();
                buffer.pos(opposite[1].getX(), opposite[1].getY(), opposite[1].getZ()).tex(f8, f5).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j).endVertex();
                buffer.pos(opposite[2].getX(), opposite[2].getY(), opposite[2].getZ()).tex(f7, f5).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j).endVertex();
                buffer.pos(opposite[3].getX(), opposite[3].getY(), opposite[3].getZ()).tex(f7, f6).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j).endVertex();
            }
        }
    }

    public static class VortexFactoryBase implements IParticleFactory<ColoredParticleData4f> {

        private final IAnimatedSprite sprite;

        public VortexFactoryBase(IAnimatedSprite sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle makeParticle(ColoredParticleData4f data, ClientWorld world, double x, double y, double z, double motX, double motY, double motZ) {
            return new VortexParticle(world, x, y, z, data.getRadius(), data.getInc(), data.getOffset(), data.getAngle(), data, this.sprite, 40, 0.9f, 1.1f, true, 4);
        }
    }
}
