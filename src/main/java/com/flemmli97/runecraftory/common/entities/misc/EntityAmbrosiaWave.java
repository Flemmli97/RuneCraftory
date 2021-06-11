package com.flemmli97.runecraftory.common.entities.misc;

import com.flemmli97.runecraftory.api.enums.EnumElement;
import com.flemmli97.runecraftory.common.entities.BaseMonster;
import com.flemmli97.runecraftory.common.particles.ColoredParticleData;
import com.flemmli97.runecraftory.common.registry.ModAttributes;
import com.flemmli97.runecraftory.common.registry.ModEntities;
import com.flemmli97.runecraftory.common.registry.ModParticles;
import com.flemmli97.runecraftory.common.utils.CombatUtils;
import com.flemmli97.runecraftory.common.utils.CustomDamage;
import com.flemmli97.tenshilib.common.entity.EntityDamageCloud;
import com.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

public class EntityAmbrosiaWave extends EntityDamageCloud {

    private static final DataParameter<Integer> maxTick = EntityDataManager.createKey(EntityAmbrosiaWave.class, DataSerializers.VARINT);

    private Predicate<LivingEntity> pred = (e) -> !e.equals(this.getOwner());

    public static final float circleInc = 0.2f;
    public static final int timeTillFull = 25;

    private static final List<Vector3f> circleParticleMotion = RayTraceUtils.rotatedVecs(new Vector3d(0.25, 0, 0), new Vector3d(0, 1, 0), -180, 175, 5);

    public EntityAmbrosiaWave(EntityType<? extends EntityAmbrosiaWave> type, World world) {
        super(type, world);
    }

    public EntityAmbrosiaWave(World world, LivingEntity shooter, int maxLivingTick) {
        super(ModEntities.ambrosia_wave.get(), world, shooter);
        this.dataManager.set(maxTick, maxLivingTick);
        if (shooter instanceof BaseMonster)
            this.pred = (e) -> !e.equals(this.getOwner()) && ((BaseMonster) shooter).hitPred.test(e);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(maxTick, 200);
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.dataManager.set(maxTick, compound.getInt("MaxTick"));
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("MaxTick", this.dataManager.get(maxTick));
    }

    @Override
    public float radiusIncrease() {
        return circleInc;
    }

    @Override
    public double maxRadius() {
        return 5;
    }

    @Override
    public boolean canStartDamage() {
        return true;
    }

    @Override
    public int livingTickMax() {
        return this.dataManager.get(maxTick);
    }

    @Override
    protected boolean canHit(LivingEntity e) {
        return super.canHit(e) && e.getDistanceSq(this) <= this.getRadius() * this.getRadius() && (this.pred == null || this.pred.test(e));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.world.isRemote) {
            if (this.livingTicks < this.dataManager.get(maxTick) && this.livingTicks % 5 == 1) {
                for (Vector3f vec : circleParticleMotion) {
                    this.world.addParticle(new ColoredParticleData(ModParticles.staticLight.get(), 200 / 255F, 133 / 255F, 36 / 255F, 1, 0.4f), this.getPosX(), this.getPosY() + 0.2, this.getPosZ(), vec.getX(), vec.getY(), vec.getZ());
                }
            }
        } else if ((this.getOwner() != null && !this.getOwner().isAlive()))
            this.remove();
    }

    @Override
    protected boolean damageEntity(LivingEntity e) {
        if (CombatUtils.damage(this.getOwner(), e, new CustomDamage.Builder(this, this.getOwner()).hurtResistant(15).element(EnumElement.EARTH).get(), CombatUtils.getAttributeValueRaw(this.getOwner(), ModAttributes.RF_MAGIC.get()) * 0.3f, null)) {
            e.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 10, 6, true, false));
            e.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 10, 128, true, false));
            return true;
        }
        return false;
    }

    @Override
    public LivingEntity getOwner() {
        LivingEntity owner = super.getOwner();
        if (owner instanceof BaseMonster)
            this.pred = ((BaseMonster) owner).hitPred;
        return owner;
    }
}
