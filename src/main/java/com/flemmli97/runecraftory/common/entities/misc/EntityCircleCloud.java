package com.flemmli97.runecraftory.common.entities.misc;

import com.flemmli97.runecraftory.common.entities.monster.boss.EntityAmbrosia;
import com.flemmli97.runecraftory.common.particles.ColoredParticleData;
import com.flemmli97.runecraftory.common.registry.ModAttributes;
import com.flemmli97.runecraftory.common.registry.ModEntities;
import com.flemmli97.runecraftory.common.registry.ModParticles;
import com.flemmli97.runecraftory.common.utils.CombatUtils;
import com.flemmli97.runecraftory.common.utils.CustomDamage;
import com.flemmli97.tenshilib.api.entity.IOwnable;
import com.flemmli97.tenshilib.common.entity.EntityUtil;
import com.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class EntityCircleCloud extends Entity implements IOwnable<LivingEntity> {

    private static final DataParameter<Float> radius = EntityDataManager.createKey(EntityAmbrosiaWave.class, DataSerializers.FLOAT);

    private UUID ownerUUID;
    private LivingEntity owner;
    private int livingTick;
    private Predicate<LivingEntity> pred;

    public static final float circleInc = 0.2f;
    public static final int timeTillFull = 20;

    private static final List<Vector3f> circleParticleMotion = RayTraceUtils.rotatedVecs(new Vector3d(0.25,0,0), new Vector3d(0,1,0), -180, 175, 5);

    public EntityCircleCloud(EntityType<? extends EntityAmbrosiaWave> type, World world) {
        super(type, world);
    }

    public EntityCircleCloud(World world, LivingEntity caster, int maxLivingTick) {
        this(ModEntities.ambrosia_wave.get(), world);
        this.owner = caster;
        this.ownerUUID = caster.getUniqueID();
        this.setPosition(caster.getX(), caster.getY(), caster.getZ());
        //this.dataManager.set(maxTick, maxLivingTick);
        //this.pred = (e) -> !e.equals(this.getOwner()) && caster.attackPred.test(e);
    }

    @Override
    protected void registerData() {
        this.dataManager.register(radius, 0f);
        //this.dataManager.register(maxTick, 200);
    }

    public float getRadius() {
        return this.dataManager.get(radius);
    }

    public void increaseRadius() {
        this.dataManager.set(radius, (this.getRadius() + circleInc));
    }

    @Override
    public UUID ownerUUID() {
        return this.ownerUUID;
    }

    @Override
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null) {
            this.owner = EntityUtil.findFromUUID(EntityAmbrosia.class, this.world, this.ownerUUID);
            //if (this.owner != null)
            //    this.pred = this.owner.attackPred;
        }
        return this.owner;
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        this.livingTick = compound.getInt("livingTick");
        //this.dataManager.set(maxTick, compound.getInt("maxTick"));
        this.dataManager.set(radius, compound.getFloat("radius"));
        if (compound.contains("owner"))
            this.ownerUUID = compound.getUniqueId("owner");
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putInt("livingTick", this.livingTick);
        //compound.putInt("maxTick", this.dataManager.get(maxTick));
        compound.putFloat("radius", this.getRadius());
        if (this.owner != null)
            compound.putUniqueId("owner", this.owner.getUniqueID());
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        ++this.livingTick;
        if (this.world.isRemote) {
            if(this.livingTick % 5 == 1) {
                for(Vector3f vec : circleParticleMotion) {
                    //for(int i = 0; i < 2; i++)
                    this.world.addParticle(new ColoredParticleData(ModParticles.ambrosiaWaveParticle.get(), 219/255F, 153/255F, 66/255F, 1, 2), this.getX(), this.getY()+0.2, this.getZ(), vec.getX(), vec.getY(), vec.getZ());
                }
            }
        }
        if (!this.world.isRemote) {
            //if (this.livingTick > this.dataManager.get(maxTick) + timeTillFull || (this.owner != null && !this.owner.isAlive()))
                this.remove();
            if (this.getRadius() <= 5.0f) {
                this.increaseRadius();
            }
            //if (this.livingTick < this.dataManager.get(maxTick)) {
                List<LivingEntity> list = this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(this.getRadius(), 1.0, this.getRadius()), (e) -> e.getDistanceSq(this) <= this.getRadius() * this.getRadius() && (this.pred == null || this.pred.test(e)));
                for (LivingEntity e : list) {
                    if (CombatUtils.damage(this.getOwner(), e, new CustomDamage.Builder(this, this.getOwner()).hurtResistant(15).get(), CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.RF_MAGIC.get(), e) * 0.5f, null)) {//RFCalculations.getAttributeValue(this.owner, ItemStatAttributes.RFMAGICATT, null, null) / 2.5f)) {
                        e.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 10, 6, true, false));
                        e.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 10, 128, true, false));
                    }
                }
            //}
        }
    }
}
