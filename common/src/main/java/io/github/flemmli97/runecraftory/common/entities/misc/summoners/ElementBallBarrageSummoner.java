package io.github.flemmli97.runecraftory.common.entities.misc.summoners;

import io.github.flemmli97.runecraftory.common.entities.misc.ElementalBallEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.ProjectileSummonHelperEntity;
import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class ElementBallBarrageSummoner extends ProjectileSummonHelperEntity {

    protected static final EntityDataAccessor<Integer> ELEMENT_DATA = SynchedEntityData.defineId(ElementBallBarrageSummoner.class, EntityDataSerializers.INT);

    private ItemElement element = ItemElement.NONE;

    public ElementBallBarrageSummoner(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public ElementBallBarrageSummoner(Level level, LivingEntity caster, ItemElement element) {
        super(RuneCraftoryEntities.ELEMENTAL_BARRAGE_SUMMONER.get(), level, caster);
        this.element = element;
        this.entityData.set(ELEMENT_DATA, this.element.ordinal());
        this.damageMultiplier = 0.8f;
        this.maxLivingTicks = 20;
    }

    public ItemElement getElement() {
        return this.element;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (key == ELEMENT_DATA) {
            int i = this.entityData.get(ELEMENT_DATA);
            if (i < ItemElement.values().length)
                this.element = ItemElement.values()[i];
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ELEMENT_DATA, 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(ELEMENT_DATA, compound.getInt("Element"));
        try {
            this.element = ItemElement.values()[this.entityData.get(ELEMENT_DATA)];
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
        ElementalBallEntity spellBall = new ElementalBallEntity(this.level(), this.getOwner(), this.element);
        spellBall.withMaxLivingTicks(30);
        spellBall.setDamageMultiplier(this.damageMultiplier);
        spellBall.setPos(spellBall.getX() + this.random.nextFloat() * 1.5 - 0.75, spellBall.getY() + this.random.nextFloat() * 0.05 - 0.1, spellBall.getZ() + this.random.nextFloat() * 1.5 - 0.75);
        spellBall.shootAtPosition(this.targetX, this.targetY, this.targetZ, 0.3f, 8);
        this.level().addFreshEntity(spellBall);
        if (this.getOwner() != null) {
            switch (this.element) {
                case FIRE ->
                        this.getOwner().playSound(RuneCraftorySounds.SPELL_GENERIC_POOF.get(), 1, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
                case WATER ->
                        this.getOwner().playSound(RuneCraftorySounds.SPELL_GENERIC_WATERBUBBLE.get(), 1, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
            }
        }
    }
}
