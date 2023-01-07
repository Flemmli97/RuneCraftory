package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class EntityAmbrosiaSleep extends Entity implements OwnableEntity {

    private UUID ownerUUID;
    private Entity owner;
    private int livingTick;
    private Predicate<LivingEntity> pred;
    private float damageMultiplier = 0.6f;

    public EntityAmbrosiaSleep(EntityType<? extends EntityAmbrosiaSleep> type, Level world) {
        super(type, world);
    }

    public EntityAmbrosiaSleep(Level world, LivingEntity caster) {
        this(ModEntities.sleepBall.get(), world);
        this.owner = caster;
        this.ownerUUID = caster.getUUID();
        this.setPos(caster.getX(), caster.getY(), caster.getZ());
        if (caster instanceof BaseMonster monster)
            this.pred = monster.hitPred;
    }

    public void setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    @Override
    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    @Override
    public Entity getOwner() {
        if (this.owner == null || this.owner.isRemoved()) {
            this.owner = EntityUtil.findFromUUID(Entity.class, this.level, this.ownerUUID);
            if (this.owner instanceof BaseMonster monster)
                this.pred = monster.hitPred;
        }
        return this.owner;
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void tick() {
        ++this.livingTick;
        if (this.level.isClientSide) {
            for (int i = 0; i < 2; i++) {
                this.level.addParticle(new ColoredParticleData(ModParticles.light.get(), 207 / 255F, 13 / 255F, 38 / 255F, 1, 2), this.getX() + this.random.nextGaussian() * 0.15, this.getY() + 0.35 + this.random.nextGaussian() * 0.07, this.getZ() + this.random.nextGaussian() * 0.15, this.random.nextGaussian() * 0.01, Math.abs(this.random.nextGaussian() * 0.03), this.random.nextGaussian() * 0.01);
            }
        }
        if (this.livingTick > 40) {
            this.remove(RemovalReason.KILLED);
        }
        if (!this.level.isClientSide && this.getOwner() != null) {
            List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.3), this.pred);
            for (LivingEntity e : list) {
                if (!e.equals(this.getOwner()) && (this.pred == null || this.pred.test(e))) {
                    LivingEntity owner = this.getOwner() instanceof LivingEntity living ? living : null;
                    if (owner != null)
                        CombatUtils.applyTempAttribute(owner, ModAttributes.RFSLEEP.get(), 100);
                    if (CombatUtils.damage(this.getOwner(), e, new CustomDamage.Builder(this, this.getOwner()).element(EnumElement.EARTH), true, false, CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.MAGIC.get()) * this.damageMultiplier, null)) {
                        this.remove(RemovalReason.KILLED);
                        if (owner != null)
                            CombatUtils.removeAttribute(owner, ModAttributes.RFSLEEP.get());
                        break;
                    }
                    if (owner != null)
                        CombatUtils.removeAttribute(owner, ModAttributes.RFSLEEP.get());
                }
            }
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.livingTick = compound.getInt("livingTick");
        if (compound.contains("owner"))
            this.ownerUUID = compound.getUUID("owner");
        this.damageMultiplier = compound.getFloat("DamageMultiplier");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("livingTick", this.livingTick);
        if (this.owner != null)
            compound.putUUID("owner", this.owner.getUUID());
        compound.putFloat("DamageMultiplier", this.damageMultiplier);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
}
