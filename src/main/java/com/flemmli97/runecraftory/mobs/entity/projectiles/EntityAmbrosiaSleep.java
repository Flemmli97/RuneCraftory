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
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class EntityAmbrosiaSleep extends Entity {
    private UUID ownerUUID;
    private EntityAmbrosia owner;
    private int livingTick;
    private Predicate<LivingEntity> pred;

    public EntityAmbrosiaSleep(EntityType<? extends EntityAmbrosiaSleep> type, World world) {
        super(type, world);
    }

    public EntityAmbrosiaSleep(World world, EntityAmbrosia caster) {
        this(ModEntities.sleep_ball, world);
        this.owner = caster;
        this.ownerUUID = caster.getUniqueID();
        this.setPosition(caster.getX(), caster.getY(), caster.getZ());
        this.pred = caster.attackPred;
    }

    public EntityAmbrosia getOwner() {
        if (this.owner == null) {
            this.owner = EntityUtil.findFromUUID(EntityAmbrosia.class, this.world, this.ownerUUID);
            if (this.owner != null)
                this.pred = this.owner.attackPred;
        }
        return this.owner;
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        this.livingTick = compound.getInt("livingTick");
        if (compound.contains("owner"))
            this.ownerUUID = compound.getUniqueId("owner");
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putInt("livingTick", this.livingTick);
        if (this.owner != null)
            compound.putUniqueId("owner", this.owner.getUniqueID());
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void registerData() {

    }

    @Override
    public void tick() {
        ++this.livingTick;
        if (this.world.isRemote) {
            for (int i = 0; i < 4; i++) {
                this.world.addParticle(ParticleTypes.FLAME, this.getX(), this.getY() + 0.35, this.getZ(), this.rand.nextGaussian() * 0.008D, Math.abs(this.rand.nextGaussian() * 0.025), this.rand.nextGaussian() * 0.009D);
                //Particles.spawnParticle(LibParticles.particleFlame, this.world, this.posX, this.posY + 0.35, this.posZ, this.rand.nextGaussian() * 0.008D, Math.abs(this.rand.nextGaussian() * 0.025), this.rand.nextGaussian() * 0.009D);
            }
        }
        if (this.livingTick > 40) {
            this.remove();
        }
        if (!this.world.isRemote) {
            List<LivingEntity> list = this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(0.3), this.pred);
            for (LivingEntity e : list) {
                if (!e.equals(this.getOwner()) && (this.pred == null || this.pred.test(e))) {

                    if (MobUtils.handleMobAttack(e, CustomDamage.attack(this.owner, EnumElement.EARTH, CustomDamage.DamageType.NORMAL, CustomDamage.KnockBackType.BACK, 0.0f, 20), MobUtils.getAttributeValue(this.getOwner(), ModAttributes.RF_MAGIC, e)*0.3f)){//RFCalculations.getAttributeValue(this.owner, ItemStatAttributes.RFMAGICATT, null, null) / 3.0f)) {
                        e.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 80, 1, true, false));
                        //e.addPotionEffect(new EffectInstance(Potion.getPotionFromResourceLocation("runecraftory:sleep"), 80, 1, true, false));
                        this.remove();
                    }
                }
            }
        }
    }
}
