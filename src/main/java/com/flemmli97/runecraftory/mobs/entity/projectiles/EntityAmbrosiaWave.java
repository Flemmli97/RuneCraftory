package com.flemmli97.runecraftory.mobs.entity.projectiles;

import com.flemmli97.runecraftory.lib.EnumElement;
import com.flemmli97.runecraftory.mobs.CustomDamage;
import com.flemmli97.runecraftory.mobs.MobUtils;
import com.flemmli97.runecraftory.mobs.entity.monster.boss.EntityAmbrosia;
import com.flemmli97.runecraftory.registry.ModAttributes;
import com.flemmli97.runecraftory.registry.ModEntities;
import com.flemmli97.tenshilib.common.entity.EntityUtil;
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
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class EntityAmbrosiaWave extends Entity {
    private static final DataParameter<Float> radius = EntityDataManager.createKey(EntityAmbrosiaWave.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> maxTick = EntityDataManager.createKey(EntityAmbrosiaWave.class, DataSerializers.VARINT);

    private UUID ownerUUID;
    private EntityAmbrosia owner;
    private int livingTick;
    private Predicate<LivingEntity> pred;

    public float[] clientWaveSizes = new float[]{0, -1, -2, -3, -4};
    public float clientAlphaMult = 1;
    public static final float circleInc = 0.2f;
    public static final int timeTillFull = 20;

    public EntityAmbrosiaWave(EntityType<? extends EntityAmbrosiaWave> type, World world) {
        super(type, world);
    }

    public EntityAmbrosiaWave(World world, EntityAmbrosia caster, int maxLivingTick) {
        this(ModEntities.ambrosia_wave, world);
        this.owner = caster;
        this.ownerUUID = caster.getUniqueID();
        this.setPosition(caster.getX(), caster.getY(), caster.getZ());
        this.dataManager.set(maxTick, maxLivingTick);
        this.pred = (e)-> !e.equals(this.getOwner()) && caster.attackPred.test(e);
    }

    @Override
    protected void registerData() {
        this.dataManager.register(radius, 0f);
        this.dataManager.register(maxTick, 200);
    }

    public float getRadius() {
        return this.dataManager.get(radius);
    }

    public void increaseRadius() {
        this.dataManager.set(radius, (this.getRadius() + circleInc));
    }

    protected EntityAmbrosia getOwner() {
        if (this.owner == null && this.ownerUUID != null) {
            this.owner = EntityUtil.findFromUUID(EntityAmbrosia.class, this.world, this.ownerUUID);
            if (this.owner != null)
                this.pred = this.owner.attackPred;
        }
        return this.owner;
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        this.livingTick = compound.getInt("livingTick");
        this.dataManager.set(maxTick, compound.getInt("maxTick"));
        this.dataManager.set(radius, compound.getFloat("radius"));
        if (compound.contains("owner"))
            this.ownerUUID = compound.getUniqueId("owner");
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putInt("livingTick", this.livingTick);
        compound.putInt("maxTick", this.dataManager.get(maxTick));
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
            for (int i = 0; i < this.clientWaveSizes.length; i++) {
                if (this.clientWaveSizes[i] > -100) {
                    this.clientWaveSizes[i] += circleInc;
                    if (this.clientWaveSizes[i] > 5)
                        if (this.dataManager.get(maxTick) > this.livingTick)
                            this.clientWaveSizes[i] = 0;
                        else
                            this.clientWaveSizes[i] = -100;
                }
            }
            if (this.dataManager.get(maxTick) < this.livingTick)
                this.clientAlphaMult = Math.max(0.2f, this.clientAlphaMult - 0.1f);
        }
        if (!this.world.isRemote) {
            if (this.livingTick > this.dataManager.get(maxTick) + timeTillFull || (this.owner != null && !this.owner.isAlive()))
                this.remove();
            if (this.getRadius() <= 5.0f) {
                this.increaseRadius();
            }
            if (this.livingTick < this.dataManager.get(maxTick)) {
                List<LivingEntity> list = this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(this.getRadius(), 1.0, this.getRadius()), (e) -> e.getDistanceSq(this) <= this.getRadius() * this.getRadius() && (this.pred == null || this.pred.test(e)));
                for (LivingEntity e : list) {
                    if (MobUtils.handleMobAttack(e, new CustomDamage.Builder(this).trueSource(this.getOwner()).hurtResistant(15).get(), MobUtils.getAttributeValue(this.getOwner(), ModAttributes.RF_MAGIC, e)*0.5f)){//RFCalculations.getAttributeValue(this.owner, ItemStatAttributes.RFMAGICATT, null, null) / 2.5f)) {
                        e.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 10, 6, true, false));
                        e.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 10, 128, true, false));
                    }
                }
            }
        }
    }
}
