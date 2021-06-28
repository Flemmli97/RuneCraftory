package io.github.flemmli97.runecraftory.common.entities.misc;

import com.flemmli97.tenshilib.common.entity.EntityBeam;
import com.flemmli97.tenshilib.common.particle.ColoredParticleData;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class EntityWaterLaser extends EntityBeam {

    private static final DataParameter<Float> yawMotionVal = EntityDataManager.createKey(EntityWaterLaser.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> maxLivingTick = EntityDataManager.createKey(EntityWaterLaser.class, DataSerializers.VARINT);

    private List<Entity> hitEntities = new ArrayList<>();
    private float damageMultiplier = 1;

    private Predicate<LivingEntity> pred;

    public EntityWaterLaser(EntityType<? extends EntityBeam> type, World world) {
        super(type, world);
    }

    public EntityWaterLaser(World world, LivingEntity shooter) {
        super(ModEntities.waterLaser.get(), world, shooter);
    }

    public EntityWaterLaser(World world, LivingEntity shooter, float yawMotion) {
        super(ModEntities.waterLaser.get(), world, shooter);
        this.dataManager.set(yawMotionVal, yawMotion);
        if (shooter instanceof BaseMonster)
            this.pred = ((BaseMonster) shooter).hitPred;
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(yawMotionVal, 0f);
        this.dataManager.register(maxLivingTick, 20);
    }

    public void setRotationToDirWithOffset(double dirX, double dirY, double dirZ, float acc, float yawOffset) {
        super.setRotationToDir(dirX, dirY, dirZ, acc);
        this.rotationYaw += yawOffset;
    }

    public EntityWaterLaser setMaxTicks(int ticks) {
        this.dataManager.set(maxLivingTick, ticks);
        return this;
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
    public float getRange() {
        return 9;
    }

    @Override
    public boolean getHitVecFromShooter() {
        return this.getOwner() instanceof PlayerEntity;
    }

    @Override
    public int livingTickMax() {
        return this.dataManager.get(maxLivingTick);
    }

    @Override
    public int attackCooldown() {
        return 0;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.world.isRemote) {
            Vector3d pos = this.getPositionVec();
            Vector3d dir = this.hitVec.subtract(pos);
            int amount = this.dataManager.get(yawMotionVal) != 0 ? 3 : 1;
            for (int i = 0; i < amount; i++) {
                for (double d = 0; d < 1; d += 0.05) {
                    Vector3d scaleD = dir.scale(d).add(pos);
                    this.world.addParticle(new ColoredParticleData(ModParticles.staticLight.get(), 34 / 255F, 34 / 255F, 230 / 255F, 0.6f, 0.06f), scaleD.getX(), scaleD.getY(), scaleD.getZ(), 0, 0, 0);
                }
            }
        }
        if (this.dataManager.get(yawMotionVal) != 0) {
            this.rotationYaw += this.dataManager.get(yawMotionVal);
            this.hit = null;
        }
    }

    @Override
    protected boolean check(Entity e, Vector3d from, Vector3d to) {
        return (!(e instanceof LivingEntity) || this.pred == null || this.pred.test((LivingEntity) e)) && super.check(e, from, to);
    }

    @Override
    public void onImpact(EntityRayTraceResult res) {
        Entity e = res.getEntity();
        if (!this.hitEntities.contains(e) && CombatUtils.damage(this.getOwner(), e, new CustomDamage.Builder(this, this.getOwner()).hurtResistant(5).element(EnumElement.WATER).get(), CombatUtils.getAttributeValueRaw(this.getOwner(), ModAttributes.RF_MAGIC.get()) * this.damageMultiplier, null))
            this.hitEntities.add(res.getEntity());
    }

    public void setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.damageMultiplier = compound.getFloat("DamageMultiplier");
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putFloat("DamageMultiplier", this.damageMultiplier);
    }

    @Override
    public LivingEntity getOwner() {
        LivingEntity owner = super.getOwner();
        if (owner instanceof BaseMonster)
            this.pred = ((BaseMonster) owner).hitPred;
        return owner;
    }
}
