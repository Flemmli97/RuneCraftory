package io.github.flemmli97.runecraftory.common.entities.monster.wisp;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryParticles;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.particle.AdvancedParticleContainer;
import io.github.flemmli97.tenshilib.common.particle.data.ColorData;
import io.github.flemmli97.tenshilib.common.particle.data.MotionData;
import io.github.flemmli97.tenshilib.common.particle.data.ParticleMetaData;
import io.github.flemmli97.tenshilib.common.particle.data.ScaleData;
import io.github.flemmli97.tenshilib.common.utils.math.MathUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class Ignis extends WispBase {

    public Ignis(EntityType<? extends WispBase> type, Level level) {
        super(type, level);
    }

    @Override
    protected Spell getSpellFor(int command) {
        return RuneCraftorySpells.FIREBALL.get();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            AnimationState anim = this.getAnimationHandler().getAnimation();
            if (anim == null || (anim.is(VANISH) && anim.isPast("teleport_done"))) {
                double[] off = MathUtils.rotate2d(0, -3.5 / 16f, this.yBodyRot * Mth.DEG_TO_RAD);
                for (int i = 0; i < 4; i++) {
                    AdvancedParticleContainer.make(RuneCraftoryParticles.LIGHT.get())
                            .addData(new ColorData(180 / 255F, 60 / 255F, 60 / 255F, 0.2f))
                            .addData(new MotionData(this.random.nextGaussian() * 0.01, Math.abs(this.random.nextGaussian() * 0.03), this.random.nextGaussian() * 0.01))
                            .addData(new ScaleData(0.3f))
                            .addData(new ParticleMetaData(20, false, 0))
                            .build().add(this.level(), this.getX() + off[0] + this.random.nextGaussian() * 0.2,
                                    this.getY() + this.getBbHeight() * 0.4,
                                    this.getZ() + off[1] + this.random.nextGaussian() * 0.2);
                    AdvancedParticleContainer.make(RuneCraftoryParticles.LIGHT.get())
                            .addData(new ColorData(247 / 255F, 180 / 255F, 180 / 255F, 0.2f))
                            .addData(new MotionData(this.random.nextGaussian() * 0.01, Math.abs(this.random.nextGaussian() * 0.03), this.random.nextGaussian() * 0.01))
                            .addData(new ScaleData(0.3f))
                            .addData(new ParticleMetaData(20, false, 0))
                            .build().add(this.level(), this.getX() + off[0] + this.random.nextGaussian() * 0.2,
                                    this.getY() + this.getBbHeight() * 0.4,
                                    this.getZ() + off[1] + this.random.nextGaussian() * 0.2);
                }
            }
        }
    }

    @Override
    public void attackFar(LivingEntity target) {
        RuneCraftorySpells.FIREBALL.get().use(this);
    }

    @Override
    public void attackClose(LivingEntity target) {
        RuneCraftorySpells.IGNIS_FLAME.get().use(this);
    }
}
