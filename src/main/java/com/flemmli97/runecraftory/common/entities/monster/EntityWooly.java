package com.flemmli97.runecraftory.common.entities.monster;

import com.flemmli97.runecraftory.common.entities.AnimationType;
import com.flemmli97.runecraftory.common.entities.ChargingMonster;
import com.flemmli97.runecraftory.common.entities.monster.ai.ChargeAttackGoal;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IForgeShearable;

import java.util.ArrayList;
import java.util.List;

public class EntityWooly extends ChargingMonster implements IForgeShearable {

    private static final DataParameter<Boolean> SHEARED = EntityDataManager.createKey(EntityWooly.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SPAWNSHEARED = EntityDataManager.createKey(EntityWooly.class, DataSerializers.BOOLEAN);

    private int shearTick;

    public static final AnimatedAction slap = new AnimatedAction(16, 7, "slap");
    public static final AnimatedAction kick = new AnimatedAction(20, 3, "kick");
    public static final AnimatedAction headbutt = new AnimatedAction(16, 7, "headbutt");

    public static final AnimatedAction[] anims = new AnimatedAction[]{slap, kick, headbutt};

    public ChargeAttackGoal<EntityWooly> attack = new ChargeAttackGoal<>(this);

    public EntityWooly(EntityType<? extends EntityWooly> type, World world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(SPAWNSHEARED, this.getRNG().nextFloat() < 0.1);
        this.dataManager.register(SHEARED, this.dataManager.get(SPAWNSHEARED));
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.19);
        super.applyAttributes();
    }

    @Override
    public void livingTick() {
        super.livingTick();
        if (!this.dataManager.get(SPAWNSHEARED)) {
            this.shearTick = Math.max(--this.shearTick, 0);
            if (this.shearTick == 1) {
                this.setSheared(false);
            }
        }
    }

    @Override
    protected void tameEntity(PlayerEntity owner) {
        super.tameEntity(owner);
        this.dataManager.set(SPAWNSHEARED, false);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_SHEEP_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SHEEP_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.65f;
    }

    @Override
    protected float getSoundPitch() {
        return 1.7f;
    }

    @Override
    public float attackChance(AnimationType type) {
        return this.dataManager.get(SPAWNSHEARED) || this.isTamed() ? 0.8f : 0.03f;
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return anims;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 0.8;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.CHARGE)
            return anim.getID().equals(kick.getID()) && this.getRNG().nextFloat() < 0.5;
        else if (type == AnimationType.MELEE)
            return anim.getID().equals(slap.getID()) || anim.getID().equals(headbutt.getID());
        return false;
    }

    public boolean isSheared() {
        return this.dataManager.get(SHEARED);
    }

    public void setSheared(boolean flag) {
        this.shearTick = 24000;
        this.dataManager.set(SHEARED, flag);
    }

    @Override
    public boolean isShearable(ItemStack item, World world, BlockPos pos) {
        return !this.isSheared();
    }

    @Override
    public List<ItemStack> onSheared(PlayerEntity player, ItemStack item, World world, BlockPos pos, int fortune) {
        this.setSheared(true);
        List<ItemStack> ret = new ArrayList<>();
        //if(!this.isTamed()){
        //    ret.add((new ItemStack(ModItems.furSmall)));
        //}else {
        int i = 1 + this.rand.nextInt(3);
        for (int j = 0; j < i; ++j) {
            ret.add(new ItemStack(Blocks.WHITE_WOOL, 1));
        }
        //}
        this.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0f, 1.0f);
        return ret;
    }

    @Override
    public float chargingLength() {
        return 3;
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("Sheared", this.isSheared());
        compound.putBoolean("SpawnedSheared", this.dataManager.get(SPAWNSHEARED));
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setSheared(compound.getBoolean("Sheared"));
        this.dataManager.set(SPAWNSHEARED, compound.getBoolean("SpawnedSheared"));
    }

    @Override
    public void handleRidingCommand(int command) {
        if (this.getAnimation() == null) {
            if (command == 2)
                this.setAnimation(kick);
            else if (command == 1)
                this.setAnimation(headbutt);
            else
                this.setAnimation(slap);
        }
    }
}