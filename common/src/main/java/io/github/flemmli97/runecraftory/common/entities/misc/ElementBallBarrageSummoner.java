package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class ElementBallBarrageSummoner extends ProjectileSummonHelperEntity {

    protected static final EntityDataAccessor<Integer> ELEMENT_DATA = SynchedEntityData.defineId(ElementBallBarrageSummoner.class, EntityDataSerializers.INT);

    private EnumElement element = EnumElement.NONE;

    public ElementBallBarrageSummoner(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public ElementBallBarrageSummoner(Level level, LivingEntity caster, EnumElement element) {
        super(ModEntities.ELEMENTAL_BARRAGE_SUMMONER.get(), level, caster);
        this.element = element;
        this.entityData.set(ELEMENT_DATA, this.element.ordinal());
        this.damageMultiplier = 0.8f;
        this.maxLivingTicks = 20;
    }

    public EnumElement getElement() {
        return this.element;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (key == ELEMENT_DATA) {
            int i = this.entityData.get(ELEMENT_DATA);
            if (i < EnumElement.values().length)
                this.element = EnumElement.values()[i];
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ELEMENT_DATA, 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(ELEMENT_DATA, compound.getInt("Element"));
        try {
            this.element = EnumElement.values()[this.entityData.get(ELEMENT_DATA)];
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Element", this.entityData.get(ELEMENT_DATA));
    }

    @Override
    protected void summonProjectiles() {
        EntityBaseSpellBall spellBall = new EntityBaseSpellBall(this.level, this.getOwner(), this.element);
        spellBall.withMaxLivingTicks(40);
        spellBall.setDamageMultiplier(this.damageMultiplier);
        spellBall.setPos(spellBall.getX() + this.random.nextFloat() * 1.5 - 0.75, spellBall.getY() + this.random.nextFloat() * 0.05 - 0.1, spellBall.getZ() + this.random.nextFloat() * 1.5 - 0.75);
        spellBall.shootAtPosition(this.targetX, this.targetY, this.targetZ, 0.3f, 8);
        this.level.addFreshEntity(spellBall);
        if (this.getOwner() != null) {
            switch (this.element) {
                case FIRE ->
                        this.getOwner().playSound(ModSounds.SPELL_GENERIC_POOF.get(), 1, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
                case WATER ->
                        this.getOwner().playSound(ModSounds.SPELL_GENERIC_WATERBUBBLE.get(), 1, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
            }
        }
    }
}
