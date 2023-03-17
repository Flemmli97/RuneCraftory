package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.entity.EntityDamageCloud;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class EntityDarkness extends EntityDamageCloud {

    private float damageMultiplier = 1;

    public EntityDarkness(EntityType<? extends EntityDarkness> type, Level world) {
        super(type, world);
    }

    public EntityDarkness(Level world, LivingEntity shooter) {
        super(ModEntities.DARKNESS.get(), world, shooter);
        this.setPos(this.getX(), this.getY() + 0.1, this.getZ());
    }

    public void setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    @Override
    public int livingTickMax() {
        return 20;
    }

    @Override
    public boolean canStartDamage() {
        return this.livingTicks % 15 == 0;
    }

    @Override
    public float getRadius() {
        return 2;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected boolean damageEntity(LivingEntity livingEntity) {
        return CombatUtils.damage(this.getOwner(), livingEntity, new CustomDamage.Builder(this, this.getOwner()).element(EnumElement.DARK).hurtResistant(5), true, false, CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.MAGIC.get()) * this.damageMultiplier, null);
    }

    @Override
    protected AABB damageBoundingBox() {
        float radius = this.getRadius();
        return this.getBoundingBox().inflate(radius, 0.4, radius);
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
