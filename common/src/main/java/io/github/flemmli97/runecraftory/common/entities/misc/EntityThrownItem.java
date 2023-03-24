package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class EntityThrownItem extends BaseProjectile {

    private static final EntityDataAccessor<ItemStack> STACK = SynchedEntityData.defineId(EntityThrownItem.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Boolean> ROTATING = SynchedEntityData.defineId(EntityThrownItem.class, EntityDataSerializers.BOOLEAN);

    public EntityThrownItem(EntityType<? extends EntityThrownItem> type, Level world) {
        super(type, world);
    }

    public EntityThrownItem(Level world, LivingEntity shooter) {
        super(ModEntities.THROWN_ITEM.get(), world, shooter);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(STACK, ItemStack.EMPTY);
        this.getEntityData().define(ROTATING, false);
    }

    public void setItem(ItemStack stack) {
        this.entityData.set(STACK, Util.make(stack.copy(), itemStack -> itemStack.setCount(1)));
    }

    public ItemStack getItem() {
        return this.entityData.get(STACK);
    }

    public void setRotating(boolean rotating) {
        this.entityData.set(ROTATING, rotating);
    }

    public boolean isRotating() {
        return this.entityData.get(ROTATING);
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        boolean res = CombatUtils.damageWithFaintAndCrit(this.getOwner(), result.getEntity(), new CustomDamage.Builder(this, this.getOwner()).hurtResistant(3), CombatUtils.getAttributeValue(this.getOwner(), Attributes.ATTACK_DAMAGE) * this.damageMultiplier, null);
        if (res)
            this.discard();
        return res;
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {
        this.discard();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        ItemStack itemStack = this.getItem();
        if (!itemStack.isEmpty()) {
            compound.put("Item", itemStack.save(new CompoundTag()));
        }
        compound.putBoolean("Rotating", this.isRotating());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        ItemStack itemStack = ItemStack.of(compound.getCompound("Item"));
        this.setItem(itemStack);
        this.setRotating(compound.getBoolean("Rotating"));
    }
}
