package com.flemmli97.runecraftory.common.blocks.tile;

import javax.annotation.Nullable;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.init.GateSpawning;
import com.flemmli97.runecraftory.common.utils.LevelCalc;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileBossSpawner extends TileEntity implements ITickable{

	private long time;
	private String savedEntity;
	public TileBossSpawner() {}
	public TileBossSpawner(World world) {
		this.time=world.getWorldTime();
	}
	
	@Override
	public void update() {
		if(this.savedEntity!=null && Math.abs(this.world.getWorldTime()/24000 - this.time/24000)>=1 && !this.world.isRemote)
		{
			this.spawnEntity();
		}
	}
	
	public void spawnEntity()
	{
		if(!this.world.isRemote)
		{
			EntityMobBase mob = GateSpawning.entityFromString(world, savedEntity);
			if(mob!=null)
			{
				this.time=this.world.getWorldTime();
		        int k = world.getEntitiesWithinAABB(mob.getClass(), (new AxisAlignedBB(this.pos).grow(32))).size();
		        if(k!=0)
		        {        
		        		return;
		        }
		        mob.setLevel(LevelCalc.levelFromDistSpawn(world, pos));
		        mob.setLocationAndAngles(this.pos.getX()+0.5, this.pos.getY()+5, this.pos.getZ()+0.5, world.rand.nextFloat() * 360.0F, 0.0F);
				mob.setHomePosAndDistance(this.pos, 16);
		        mob.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(mob)), (IEntityLivingData)null);
		        world.spawnEntity(mob);
			}
		}
	}
	
	public boolean setEntity(@Nullable EntityPlayer player, String entity)
	{
		if(player==null || player.canUseCommandBlock())
		{
			this.savedEntity=entity;
			this.spawnEntity();
			this.time=this.world.getWorldTime();
			return true;
		}
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if(compound.hasKey("Entity"))
			this.savedEntity= compound.getString("Entity");
		this.time = compound.getLong("LastSpawned");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		if(this.savedEntity!=null)
			compound.setString("Entity", this.savedEntity);
		compound.setLong("LastSpawned", time);
		return compound;
	}

	@Override
	public boolean onlyOpsCanSetNbt() {
		return true;
	}
}
