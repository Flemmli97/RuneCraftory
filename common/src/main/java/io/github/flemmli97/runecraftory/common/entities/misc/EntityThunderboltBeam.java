package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.entity.EntityBeam;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class EntityThunderboltBeam extends EntityBeam {

    private Predicate<LivingEntity> pred;
    private float damageMultiplier = 1;

    public EntityThunderboltBeam(EntityType<? extends EntityThunderboltBeam> type, Level world) {
        super(type, world);
    }

    public EntityThunderboltBeam(Level world, LivingEntity shooter) {
        super(ModEntities.lightningBeam.get(), world, shooter);
        if (shooter instanceof BaseMonster)
            this.pred = ((BaseMonster) shooter).hitPred;
    }

    public void setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    @Override
    public float getRange() {
        return 9;
    }

    @Override
    public float radius() {
        return 0.5f;
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
        if (this.level.isClientSide) {
            Vec3 pos = this.position();
            Vec3 dir = this.hitVec.subtract(pos);
            for (double d = 0; d < 1; d += 0.025) {
                Vec3 scaleD = dir.scale(d).add(pos);
                this.level.addParticle(new ColoredParticleData(ModParticles.staticLight.get(), 34 / 255F, 34 / 255F, 180 / 255F, 0.6f, 0.2f), scaleD.x(), scaleD.y(), scaleD.z(), 0, 0, 0);
                this.level.addParticle(ModParticles.lightning.get(), scaleD.x() + (this.random.nextDouble() - 0.5) * 0.1, scaleD.y() + (this.random.nextDouble() - 0.5) * 0.1, scaleD.z() + (this.random.nextDouble() - 0.5) * 0.1, 0.02, 0.02, 0.02);
            }
        }
    }

    @Override
    protected boolean check(Entity e, Vec3 from, Vec3 to) {
        return (!(e instanceof LivingEntity) || this.pred == null || this.pred.test((LivingEntity) e)) && super.check(e, from, to);
    }

    @Override
    public void onImpact(EntityHitResult res) {
        CombatUtils.damage(this.getOwner(), res.getEntity(), new CustomDamage.Builder(this, this.getOwner()).hurtResistant(10).element(EnumElement.WIND), true, false, CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.MAGIC.get()) * this.damageMultiplier, null);
    }

    @Override
    public int attackCooldown() {
        return this.livingTickMax();
    }

    @Override
    public Entity getOwner() {
        Entity owner = super.getOwner();
        if (owner instanceof BaseMonster)
            this.pred = ((BaseMonster) owner).hitPred;
        return owner;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.damageMultiplier = compound.getFloat("DamageMultiplier");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("DamageMultiplier", this.damageMultiplier);
    }
}
