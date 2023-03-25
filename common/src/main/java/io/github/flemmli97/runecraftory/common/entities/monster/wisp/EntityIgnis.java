package io.github.flemmli97.runecraftory.common.entities.monster.wisp;

import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import io.github.flemmli97.tenshilib.common.utils.MathUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class EntityIgnis extends EntityWispBase {

    public EntityIgnis(EntityType<? extends EntityWispBase> type, Level world) {
        super(type, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            AnimatedAction anim = this.getAnimationHandler().getAnimation();
            if (anim == null || (anim.getID().equals(VANISH.getID()) && anim.getTick() > 60)) {
                double[] off = MathUtils.rotate2d(0, -3.5 / 16f, MathUtils.degToRad(this.yBodyRot));
                for (int i = 0; i < 4; i++) {
                    this.level.addParticle(new ColoredParticleData(ModParticles.light.get(), 180 / 255F, 60 / 255F, 60 / 255F, 0.2f, 1.5f), this.getX() + off[0] + this.random.nextGaussian() * 0.2, this.getY() + this.getBbHeight() * 0.4, this.getZ() + off[1] + this.random.nextGaussian() * 0.2, this.random.nextGaussian() * 0.01, Math.abs(this.random.nextGaussian() * 0.03), this.random.nextGaussian() * 0.01);
                    this.level.addParticle(new ColoredParticleData(ModParticles.light.get(), 247 / 255F, 180 / 255F, 180 / 255F, 0.2f, 1.5f), this.getX() + off[0] + this.random.nextGaussian() * 0.2, this.getY() + this.getBbHeight() * 0.4, this.getZ() + off[1] + this.random.nextGaussian() * 0.2, this.random.nextGaussian() * 0.01, Math.abs(this.random.nextGaussian() * 0.03), this.random.nextGaussian() * 0.01);
                }
            }
        }
    }

    @Override
    public void attackFar(LivingEntity target) {
        if (this.level.isClientSide)
            return;
        ModSpells.SPIRITFLAME.get().use(this);
    }

    @Override
    public void attackClose(LivingEntity target) {

    }
}
