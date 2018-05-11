package com.flemmli97.runecraftory.common.entity.monster;

import java.util.ArrayList;
import java.util.List;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.entity.ai.EntityAIGenericMelee;

import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

public class EntityWooly extends EntityMobBase implements IShearable{

    private static final DataParameter<Boolean> SHEARED = EntityDataManager.<Boolean>createKey(EntityWooly.class, DataSerializers.BOOLEAN);
    private int shearTick;
	public EntityAIGenericMelee attack = new EntityAIGenericMelee(this, 1, true, 1);
	public EntityWooly(World world)
	{
		super(world);
		this.setSize(0.6F, 1.3F);
		this.tasks.addTask(2, attack);
	}
	
	@Override
	protected void entityInit()
    {
		super.entityInit();
		this.dataManager.register(SHEARED, false);
    }
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		this.shearTick= Math.max(this.shearTick--, 0);
		if(this.shearTick==1)
			this.setSheared(false);
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
		return SoundEvents.ENTITY_SHEEP_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SHEEP_DEATH;
	}

	@Override
	protected float getSoundVolume() {
		return 0.65F;
	}

	@Override
	protected float getSoundPitch() {
		return 1.7F;
	}

	/*@Override
	public EnumElement entityElement() {
		return EnumElement.NONE;
	}*/

	@Override
	public float attackChance() {
		return this.dataManager.get(SHEARED) ? 0.7F : 0.01F;
	}

	@Override
	public int getAttackTimeFromPattern(byte pattern) {
		return 20;
	}

	@Override
	public int attackFromPattern() {
		return 15;
	}

	@Override
	public int maxAttackPatterns() {
		return 2;
	}
	
	public boolean isSheared()
	{
		return this.dataManager.get(SHEARED);
	}
	
	public void setSheared(boolean flag)
	{
		this.shearTick=24000;
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
        for (int j = 0; j < i; ++j)
            ret.add(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 0));

        this.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
        return ret;
	}	
}
