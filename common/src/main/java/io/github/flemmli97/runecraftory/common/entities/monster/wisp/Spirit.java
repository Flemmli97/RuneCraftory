package io.github.flemmli97.runecraftory.common.entities.monster.wisp;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryParticles;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import io.github.flemmli97.tenshilib.common.utils.math.MathUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class Spirit extends WispBase {

    public Spirit(EntityType<? extends WispBase> type, Level level) {
        super(type, level);
    }

    @Override
    protected Spell getSpellFor(int command) {
        return RuneCraftorySpells.SPIRIT_FLAME.get();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            AnimationState anim = this.getAnimationHandler().getAnimation();
            if (anim == null || (anim.is(VANISH) && anim.isPast("teleport_done"))) {
                double[] off = MathUtils.rotate2d(0, -3.5 / 16f, this.yBodyRot * Mth.DEG_TO_RAD);
                for (int i = 0; i < 4; i++) {
                    this.level().addParticle(new ColoredParticleData(RuneCraftoryParticles.LIGHT.get(), 112 / 255F, 238 / 255F, 236 / 255F, 0.2f, 1.5f), this.getX() + off[0] + this.random.nextGaussian() * 0.2, this.getY() + this.getBbHeight() * 0.4, this.getZ() + off[1] + this.random.nextGaussian() * 0.2, this.random.nextGaussian() * 0.01, Math.abs(this.random.nextGaussian() * 0.03), this.random.nextGaussian() * 0.01);
                    this.level().addParticle(new ColoredParticleData(RuneCraftoryParticles.LIGHT.get(), 210 / 255F, 247 / 255F, 247 / 255F, 0.2f, 1.5f), this.getX() + off[0] + this.random.nextGaussian() * 0.2, this.getY() + this.getBbHeight() * 0.4, this.getZ() + off[1] + this.random.nextGaussian() * 0.2, this.random.nextGaussian() * 0.01, Math.abs(this.random.nextGaussian() * 0.03), this.random.nextGaussian() * 0.01);
                }
            }
        }
    }

    @Override
    public void attackFar(LivingEntity target) {
        if (this.level().isClientSide)
            return;
        RuneCraftorySpells.SPIRIT_FLAME.get().use(this);
    }

    @Override
    public void attackClose(LivingEntity target) {
        this.attackFar(target);
    }
}
