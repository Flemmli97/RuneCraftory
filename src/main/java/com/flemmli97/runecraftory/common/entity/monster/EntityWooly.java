package com.flemmli97.runecraftory.common.entity.monster;

import com.flemmli97.runecraftory.common.entity.EntityChargingMobBase;
import com.flemmli97.runecraftory.common.entity.ai.EntityAIChargeAttackBase;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

import java.util.ArrayList;
import java.util.List;

public class EntityWooly extends EntityChargingMobBase implements IShearable {

    private static final DataParameter<Boolean> SHEARED = EntityDataManager.createKey(EntityWooly.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SPAWNSHEARED = EntityDataManager.createKey(EntityWooly.class, DataSerializers.BOOLEAN);

    private int shearTick;
    
    public static final AnimatedAction slap = new AnimatedAction(16,7, "slap");
    public static final AnimatedAction kick = new AnimatedAction(20,3, "kick");
    public static final AnimatedAction headbutt = new AnimatedAction(16,7, "headbutt");

    public static final AnimatedAction[] anims = new AnimatedAction[] {slap, kick, headbutt};

    public EntityAIChargeAttackBase<EntityWooly> attack = new EntityAIChargeAttackBase<EntityWooly>(this);

    public EntityWooly(World world) {
        super(world);
        this.setSize(0.7f, 1.55f);
        this.tasks.addTask(2, this.attack);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(SPAWNSHEARED, this.getRNG().nextFloat()<0.1);
        this.dataManager.register(SHEARED, this.dataManager.get(SPAWNSHEARED));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.19);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(!this.dataManager.get(SPAWNSHEARED)) {
            this.shearTick = Math.max(--this.shearTick, 0);
            if (this.shearTick == 1) {
                this.setSheared(false);
            }
        }
    }
    
    @Override
    protected void tameEntity(EntityPlayer owner) {
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
    public float attackChance() {
        return this.dataManager.get(SPAWNSHEARED) || this.isTamed()? 0.8f : 0.01f;
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return anims;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim){
        return 0.8;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if(type==AnimationType.CHARGE)
            return anim.getID().equals(kick.getID()) && this.getRNG().nextFloat() < 0.5;
        else if(type==AnimationType.MELEE)
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
    public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos) {
        return !this.isSheared();
    }

    @Override
    public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        this.setSheared(true);
        List<ItemStack> ret = new ArrayList<ItemStack>();
        if(!this.isTamed()){
            ret.add((new ItemStack(ModItems.furSmall)));
        }else {
            int i = 1 + this.rand.nextInt(3);
            for (int j = 0; j < i; ++j) {
                ret.add(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 0));
            }
        }
        this.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0f, 1.0f);
        return ret;
    }

    @Override
    public float chargingLength(){
        return 3;
    }
    
    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("Sheared", this.isSheared());
        compound.setBoolean("SpawnedSheared", this.dataManager.get(SPAWNSHEARED));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setSheared(compound.getBoolean("Sheared"));
        this.dataManager.set(SPAWNSHEARED, compound.getBoolean("SpawnedSheared"));
    }
}
