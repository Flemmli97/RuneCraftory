package com.flemmli97.runecraftory.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.flemmli97.runecraftory.common.utils.schematic.NewBlockPos;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class Schematic {
	
	private final int width, height, length;
	private List<TileEntity> tiles;
	private Map<NewBlockPos, IBlockState> posBlockMapping;
	
	public Schematic(int width, int height, int length, Map<NewBlockPos, IBlockState> posBlockMapping, @Nullable ArrayList<TileEntity> tileentities)
	{
		this.width=width;
		this.height=height;
		this.length=length;
		this.posBlockMapping = posBlockMapping;
		this.tiles= tileentities;
	}
	
	public int getWidth()
	{
		return this.width;
	}
	
	public int getHeight()
	{
		return this.height;
	}
	
	public int getLength()
	{
		return this.length;
	}
	
	public List<TileEntity> getTileEntities() 
	{
		return this.tiles;
	}
	
	public IBlockState getBlockAt(BlockPos pos)
	{
		NewBlockPos pos1 = new NewBlockPos(pos.getX(), pos.getY(), pos.getZ());
		IBlockState state = this.posBlockMapping.get(pos1);
		return state;
	}
	
	@Override
	public String toString()
	{
		return "Schematic:{[Width:" + this.width + "],[Height:" + this.height + "],[Length:" + this.length+ "],[Tiles:" + this.tiles + "]}";
	}
}
