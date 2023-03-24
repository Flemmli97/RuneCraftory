package io.github.flemmli97.runecraftory.common.entities.misc;

import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.function.Consumer;

public class EntityStatusBall extends BaseDamageCloud {

    private static final EntityDataAccessor<Integer> TYPE_DATA = SynchedEntityData.defineId(EntityStatusBall.class, EntityDataSerializers.INT);

    private Type type = Type.SLEEP;

    public EntityStatusBall(EntityType<? extends EntityStatusBall> type, Level world) {
        super(type, world);
    }

    public EntityStatusBall(Level world, LivingEntity shooter) {
        super(ModEntities.STATUS_BALL.get(), world, shooter);
        this.setPos(shooter.getX(), shooter.getY(), shooter.getZ());
    }

    @Override
    public int livingTickMax() {
        return 40;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.set(TYPE_DATA, 0);
    }

    public void setType(Type type) {
        this.type = type;
        this.entityData.set(TYPE_DATA, this.type.ordinal());
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (key == TYPE_DATA) {
            int id = this.entityData.get(TYPE_DATA);
            if (id >= 0 && id < Type.values().length)
                this.type = Type.values()[id];
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            for (int i = 0; i < 2; i++) {
                Vector3f color = this.type.particleColor;
                this.level.addParticle(new ColoredParticleData(ModParticles.light.get(), color.x(), color.y(), color.z(), 1, 2), this.getX() + this.random.nextGaussian() * 0.15, this.getY() + 0.35 + this.random.nextGaussian() * 0.07, this.getZ() + this.random.nextGaussian() * 0.15, this.random.nextGaussian() * 0.01, Math.abs(this.random.nextGaussian() * 0.03), this.random.nextGaussian() * 0.01);
            }
        }
    }

    @Override
    protected boolean damageEntity(LivingEntity target) {
        CustomDamage.Builder builder = new CustomDamage.Builder(this, this.getOwner()).noKnockback();
        this.type.damageMod.accept(builder);
        if (CombatUtils.damageWithFaintAndCrit(this.getOwner(), target, builder, CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.MAGIC.get()) * this.damageMultiplier, null)) {
            this.discard();
        }
        return false;
    }

    @Override
    protected AABB damageBoundingBox() {
        return this.getBoundingBox().inflate(0.3);
    }

    public enum Type {

        SLEEP(b -> {
            b.magic().element(EnumElement.EARTH).withChangedAttribute(ModAttributes.RF_SLEEP.get(), 100);
        }, new Vector3f(207 / 255F, 13 / 255F, 38 / 255F)),
        MUSHROOM_POISON(b -> {
            b.magic().withChangedAttribute(ModAttributes.RF_POISON.get(), 50);
        }, new Vector3f(112 / 255F, 201 / 255F, 95 / 255F));

        public final Consumer<CustomDamage.Builder> damageMod;

        public final Vector3f particleColor;

        Type(Consumer<CustomDamage.Builder> damageMod, Vector3f particleColor) {
            this.damageMod = damageMod;
            this.particleColor = particleColor;
        }
    }
}
