package com.flemmli97.runecraftory.common.blocks.tile;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.init.GateSpawning;
import com.flemmli97.runecraftory.common.utils.RFCalculations;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileBossSpawner extends TileEntity implements ITickable{

	private long time;
	private String savedEntity;
	public TileBossSpawner(World world) {
		this.time=world.getWorldTime();
	}
	
	@Override
	public void update() {
		if(this.savedEntity!=null && Math.abs(this.world.getWorldTime()/24000 - this.time/24000)>=1 && !this.world.isRemote)
		{
			this.time=this.world.getWorldTime();
			this.spawnEntity();
		}
	}
	
	private void spawnEntity()
	{
		EntityMobBase mob = GateSpawning.entityFromString(world, savedEntity);
        int k = world.getEntitiesWithinAABB(mob.getClass(), (new AxisAlignedBB(this.pos).grow(32))).size();
        if(k!=0)
        {        
        		return;
        }
        mob.setLevel(RFCalculations.levelFromDistSpawn(world, pos));
        mob.setLocationAndAngles(this.pos.getX()+0.5, this.pos.getY()+5, this.pos.getZ()+0.5, world.rand.nextFloat() * 360.0F, 0.0F);
		mob.setHomePosAndDistance(this.pos, 16);
        mob.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(mob)), (IEntityLivingData)null);
        this.world.spawnEntity(mob);
	}
	
	public void setEntity(String entity)
	{
		this.savedEntity=entity;
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
