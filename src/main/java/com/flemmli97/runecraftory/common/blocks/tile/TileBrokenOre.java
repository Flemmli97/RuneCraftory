package com.flemmli97.runecraftory.common.blocks.tile;

import com.flemmli97.runecraftory.common.blocks.BlockBrokenMineral;
import com.flemmli97.runecraftory.common.blocks.BlockMineral;
import com.flemmli97.runecraftory.common.init.ModBlocks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;

public class TileBrokenOre extends TileEntity implements ITickable{

	private long time;
	public TileBrokenOre(){}
	
	public TileBrokenOre(World world) {
		this.time=world.getWorldTime();
	}

	@Override
	public void update() {
		if(Math.abs(this.world.getWorldTime()/24000 - this.time/24000)>=1 || this.world.getWorldTime()%24000==1)
		{
			this.world.setBlockState(pos, ModBlocks.mineral.getDefaultState().withProperty(BlockMineral.TIER, this.world.getBlockState(this.pos).getValue(BlockBrokenMineral.TIER)));
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.time=compound.getLong("BrokenTime");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setLong("BrokenTime", time);
		return compound;
	}

	
}
