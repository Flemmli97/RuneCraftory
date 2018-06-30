package com.flemmli97.runecraftory.common.utils;

import java.util.Arrays;

public class Position
{
    private int[] array;
    
    public Position(int x, int y, int z) {
        (this.array = new int[3])[0] = x;
        this.array[1] = y;
        this.array[2] = z;
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.array);
    }
    
    public int getX() {
        return this.array[0];
    }
    
    public int getY() {
        return this.array[1];
    }
    
    public int getZ() {
        return this.array[2];
    }
    
    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof Position && Arrays.equals(this.array, ((Position)obj).array));
    }
    
    @Override
    public String toString() {
        return "BlockPos:[x:" + this.array[0] + ",y:" + this.array[1] + ",z:" + this.array[2] + "]";
    }
}
