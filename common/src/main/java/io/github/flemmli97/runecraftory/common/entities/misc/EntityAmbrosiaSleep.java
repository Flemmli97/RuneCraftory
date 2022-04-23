package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityAmbrosia;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEffects;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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

    public EntityAmbrosiaSleep(EntityType<? extends EntityAmbrosiaSleep> type, Level world) {
        super(type, world);
    }

    public EntityAmbrosiaSleep(Level world, EntityAmbrosia caster) {
        this(ModEntities.sleep_ball.get(), world);
        this.owner = caster;
        this.ownerUUID = caster.getUUID();
        this.setPos(caster.getX(), caster.getY(), caster.getZ());
        this.pred = caster.hitPred;
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
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.livingTick = compound.getInt("livingTick");
        if (compound.contains("owner"))
            this.ownerUUID = compound.getUUID("owner");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("livingTick", this.livingTick);
        if (this.owner != null)
            compound.putUUID("owner", this.owner.getUUID());
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
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
                    if (CombatUtils.damage(this.getOwner(), e, new CustomDamage.Builder(this, this.getOwner()).element(EnumElement.EARTH).get(), CombatUtils.getAttributeValueRaw(this.getOwner(), ModAttributes.RF_MAGIC.get()) * 0.5f, null)) {
                        e.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 80, 0, true, false));
                        e.addEffect(new MobEffectInstance(ModEffects.sleep.get(), 80, 0, true, false));
                        this.remove(RemovalReason.KILLED);
                        break;
                    }
                }
            }
        }
    }
}
