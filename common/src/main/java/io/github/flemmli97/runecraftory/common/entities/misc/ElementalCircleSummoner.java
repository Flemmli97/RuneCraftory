package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class ElementalCircleSummoner extends ProjectileSummonHelperEntity {

    private EnumElement element = EnumElement.NONE;

    public ElementalCircleSummoner(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public ElementalCircleSummoner(Level level, LivingEntity caster, EnumElement element) {
        super(ModEntities.ELEMENTAL_CIRCLE_SUMMONER.get(), level, caster);
        this.damageMultiplier = 0.8f;
        this.maxLivingTicks = 380 / 10;
        this.element = element;
    }

    @Override
    protected void summonProjectiles() {
        float rot = this.getYRot() + (this.tickCount - 2) * 10;
        switch (this.element) {
            case WIND -> {
                EntityWindBlade proj = new EntityWindBlade(this.level, this.getOwner());
                proj.setType(EntityWindBlade.Type.PLAIN);
                proj.setDamageMultiplier(this.damageMultiplier);
                proj.shoot(this, 0, rot, 0, 0.32f, 0);
                proj.setPos(proj.getX(), this.getY(), proj.getZ());
                this.level.addFreshEntity(proj);
                this.playSound(ModSounds.SPELL_GENERIC_WIND.get(), 1, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
            }
            case FIRE, DARK, WATER, EARTH -> {
                EntityElementalTrail proj = new EntityElementalTrail(this.level, this.getOwner(), this.element);
                proj.setDamageMultiplier(this.damageMultiplier);
                proj.shoot(this, 0, rot, 0, 0.32f, 0);
                proj.withMaxLiving(45);
                double y = this.getY();
                if (this.element == EnumElement.WATER || this.element == EnumElement.EARTH)
                    y -= 0.5;
                proj.setPos(proj.getX(), y, proj.getZ());
                this.level.addFreshEntity(proj);
                this.playSound(ModSounds.SPELL_GENERIC_FIRE_BALL.get(), 1, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
            }
        }

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.element = EnumElement.values()[compound.getInt("Element")];
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Element", this.element.ordinal());
    }
}
