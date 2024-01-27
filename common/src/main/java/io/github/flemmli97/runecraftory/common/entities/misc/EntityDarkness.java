package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class EntityDarkness extends BaseDamageCloud {

    public EntityDarkness(EntityType<? extends EntityDarkness> type, Level world) {
        super(type, world);
    }

    public EntityDarkness(Level world, LivingEntity shooter) {
        super(ModEntities.DARKNESS.get(), world, shooter);
        this.setPos(this.getX(), this.getY() + 0.1, this.getZ());
    }

    @Override
    public int livingTickMax() {
        return 20;
    }

    @Override
    public boolean canStartDamage() {
        return this.livingTicks == 4;
    }

    @Override
    public float getRadius() {
        return 2.9f;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected boolean damageEntity(LivingEntity livingEntity) {
        boolean flag = CombatUtils.damageWithFaintAndCrit(this.getOwner(), livingEntity, new CustomDamage.Builder(this, this.getOwner()).magic().noKnockback().element(EnumElement.DARK).hurtResistant(5), CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.MAGIC.get()) * this.damageMultiplier, null);
        if (flag) {
            Vec3 distVec = livingEntity.position().subtract(this.position()).normalize();
            CombatUtils.knockbackEntityIgnoreResistance(livingEntity, 0.8f, distVec.x(), distVec.z());
        }
        return flag;
    }

    @Override
    protected AABB damageBoundingBox() {
        float radius = this.getRadius();
        return this.getBoundingBox().inflate(radius, 0.4, radius);
    }
}
