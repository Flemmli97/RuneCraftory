package com.flemmli97.runecraftory.common.entity.monster;

import java.util.ArrayList;
import java.util.List;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.entity.ai.EntityAIMeleeBase;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

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

public class EntityWooly extends EntityMobBase implements IShearable {

    private static final DataParameter<Boolean> SHEARED = EntityDataManager.createKey(EntityWooly.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SPAWNSHEARED = EntityDataManager.createKey(EntityWooly.class, DataSerializers.BOOLEAN);

    private int shearTick;
    
    public static final AnimatedAction walk = new AnimatedAction(28,0, "walk");
    public static final AnimatedAction[] anims = new AnimatedAction[] {AnimatedAction.vanillaAttack, walk};

    public EntityAIMeleeBase<EntityWooly> attack = new EntityAIMeleeBase<EntityWooly>(this, 1);

    public EntityWooly(World world) {
        super(world);
        this.setSize(0.6f, 1.3f);
        this.tasks.addTask(2, this.attack);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(SPAWNSHEARED, this.getRNG().nextFloat()<0.1);
        this.dataManager.register(SHEARED, this.dataManager.get(SPAWNSHEARED));

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
        return this.dataManager.get(SPAWNSHEARED) || this.isTamed()? 0.7f : 0.01f;
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return anims;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        return type != AnimationType.MELEE || anim.getID().equals("vanilla");
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
        int i = 1 + this.rand.nextInt(3);
        List<ItemStack> ret = new ArrayList<ItemStack>();
        for (int j = 0; j < i; ++j) {
            ret.add(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 0));
        }
        this.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0f, 1.0f);
        return ret;
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
