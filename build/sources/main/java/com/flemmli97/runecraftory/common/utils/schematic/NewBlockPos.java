package com.flemmli97.runecraftory.common.utils.schematic;

import java.util.Arrays;

public class NewBlockPos {
	
	private int[] array = new int[3];
	public NewBlockPos(int x, int y, int z)
	{
		array[0] = x;
		array[1] = y;
		array[2] = z;
	}
	@Override
	public int hashCode() {
		return Arrays.hashCode(array);
	}
	
	public int getX()
	{
		return this.array[0];
	}
	
	public int getY()
	{
		return this.array[1];
	}
	
	public int getZ()
	{
		return this.array[2];
	}
	@Override
	public boolean equals(Object obj) {
		if(this==obj)
			return true;
		if(obj instanceof NewBlockPos)
		{
			return Arrays.equals(this.array, ((NewBlockPos) obj).array);
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		return "BlockPos:[x:" + this.array[0] + ",y:" + this.array[1]+ ",z:" + this.array[2] + "]";
	}
	
}