package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryParticles;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class ThunderboltBeamEntity extends BaseBeam {

    public ThunderboltBeamEntity(EntityType<? extends ThunderboltBeamEntity> type, Level level) {
        super(type, level);
    }

    public ThunderboltBeamEntity(Level level, LivingEntity shooter) {
        super(RuneCraftoryEntities.LIGHTNING_BEAM.get(), level, shooter);
    }

    @Override
    public float getRange() {
        return 14;
    }

    @Override
    public float radius() {
        return 0.4f;
    }

    @Override
    public boolean piercing() {
        return true;
    }

    @Override
    public int livingTickMax() {
        return 25;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            Vec3 pos = this.position();
            Vec3 dir = this.hitVec.subtract(pos);
            for (double d = 0; d < 1; d += 0.025) {
                Vec3 scaleD = dir.scale(d).add(pos);
                this.level().addParticle(new ColoredParticleData(RuneCraftoryParticles.STATIC_LIGHT.get(), 34 / 255F, 34 / 255F, 180 / 255F, 0.6f, 0.2f), scaleD.x(), scaleD.y(), scaleD.z(), 0, 0, 0);
                this.level().addParticle(RuneCraftoryParticles.LIGHTNING.get(), scaleD.x() + (this.random.nextDouble() - 0.5) * 0.1, scaleD.y() + (this.random.nextDouble() - 0.5) * 0.1, scaleD.z() + (this.random.nextDouble() - 0.5) * 0.1, 0.02, 0.02, 0.02);
            }
        }
    }

    @Override
    public void onImpact(EntityHitResult res) {
        CombatUtils.damageWithFaintAndCrit(this.getOwner(), res.getEntity(), new DynamicDamage.Builder(this, this.getOwner()).magic().noKnockback().hurtResistant(10).element(ItemElement.WIND), CombatUtils.getAttributeValue(this.getOwner(), RuneCraftoryAttributes.MAGIC_ATTACK.asHolder()) * this.damageMultiplier, null);
    }

    @Override
    public boolean canStartDamage() {
        return this.livingTicks == 1;
    }
}
