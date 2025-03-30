package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.particles.BlockStateParticleData;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.utils.MathsHelper;
import io.github.flemmli97.tenshilib.common.utils.MathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;

public class GroundShakeParticleSpawner extends ProjectileSummonHelperEntity {

    private final HashSet<BlockPos> pos = new HashSet<>();
    private double arc, range;

    public GroundShakeParticleSpawner(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public GroundShakeParticleSpawner(Level level, LivingEntity caster, double arc, double range) {
        super(ModEntities.GROUND_SHAKE_PARTICLES.get(), level, caster);
        this.setPos(caster.getX(), caster.getY(), caster.getZ());
        this.maxLivingTicks = 8;
        this.arc = arc;
        this.range = range;
    }

    @Override
    protected void summonProjectiles() {
        Vec3 dir = new Vec3(this.targetX - this.getX(), 0, this.targetZ - this.getZ()).normalize();
        float yRot = MathsHelper.YRotFrom(dir);
        double progress = (double) this.ticksExisted / this.maxLivingTicks;
        double range = progress * this.range;
        int amount = Mth.ceil(1.2 * this.arc * Math.ceil(range) / 90);
        for (int i = 0; i < amount; i++) {
            float angle = yRot + (360 * ((float) i / amount) - 0.5f);
            Vec3 target = MathUtils.rotate(MathUtils.NORMAL_Y, dir.scale(range), angle * Mth.DEG_TO_RAD);
            target = this.position().add(target.x, -1, target.z);
            BlockPos pos = new BlockPos(target);
            if (this.pos.contains(pos))
                continue;
            this.pos.add(pos);
            BlockState state = this.level.getBlockState(pos);
            ((ServerLevel) this.level).sendParticles(new BlockStateParticleData(ModParticles.BLOCK.get(), state, this.random.nextFloat() * 360, this.random.nextFloat() * 10, 30),
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0, this.random.nextDouble() * 0.05 + 0.15, 0, 1);
        }
    }
}