package io.github.flemmli97.runecraftory.common.entities.misc.summoners;

import io.github.flemmli97.runecraftory.common.entities.misc.ElementalTrailEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.ProjectileSummonHelperEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.WindBladeEntity;
import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class ElementalCircleSummoner extends ProjectileSummonHelperEntity {

    private ItemElement element = ItemElement.NONE;

    public ElementalCircleSummoner(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public ElementalCircleSummoner(Level level, LivingEntity caster, ItemElement element) {
        super(RuneCraftoryEntities.ELEMENTAL_CIRCLE_SUMMONER.get(), level, caster);
        this.damageMultiplier = 0.8f;
        this.maxLivingTicks = 380 / 10;
        this.element = element;
    }

    @Override
    protected void summonProjectiles() {
        float rot = this.getYRot() + (this.tickCount - 2) * 10;
        switch (this.element) {
            case WIND -> {
                WindBladeEntity proj = new WindBladeEntity(this.level(), this.getOwner());
                proj.setType(WindBladeEntity.Type.PLAIN);
                proj.setDamageMultiplier(this.damageMultiplier);
                proj.maxTicks(100);
                proj.shoot(this, 0, rot, 0, 0.32f, 0);
                proj.setPos(proj.getX(), this.getY(), proj.getZ());
                this.level().addFreshEntity(proj);
                this.playSound(RuneCraftorySounds.SPELL_GENERIC_WIND.get(), 1, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
            }
            case FIRE, DARK, WATER, EARTH -> {
                ElementalTrailEntity proj = new ElementalTrailEntity(this.level(), this.getOwner(), this.element);
                proj.setDamageMultiplier(this.damageMultiplier);
                proj.shoot(this, 0, rot, 0, 0.32f, 0);
                proj.withMaxLiving(40);
                double y = this.getY();
                if (this.element == ItemElement.WATER || this.element == ItemElement.EARTH)
                    y -= 0.5;
                proj.setPos(proj.getX(), y, proj.getZ());
                this.level().addFreshEntity(proj);
                this.playSound(RuneCraftorySounds.SPELL_GENERIC_FIRE_BALL.get(), 1, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.element = ItemElement.values()[compound.getInt("Element")];
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Element", this.element.ordinal());
    }
}
