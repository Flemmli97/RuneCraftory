package com.flemmli97.runecraftory.common.blocks.tile;

import com.flemmli97.runecraftory.common.blocks.BlockCrop;
import com.flemmli97.runecraftory.common.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileCrop extends TileEntity {

    private boolean isGiant = false;
    private float age;
    private long lastGrowth = 0;
    private float level;
    private boolean withered;
    private BlockPos[] nearbyGiant;

    public TileCrop() {
        super(ModBlocks.cropTile.get());
    }

    public int age() {
        return (int) this.age;
    }

    public void resetAge() {
        this.age = 0;
        this.markDirty();
    }

    public int level() {
        return (int) this.level;
    }

    public boolean isGiant() {
        return this.isGiant;
    }

    public void increaseLevel(float amount) {
        this.level += amount;
    }

    public boolean isFullyGrown(BlockCrop block) {
        return this.age >= block.properties().growth();
    }

    public boolean canGrow() {
        return Math.abs(this.world.getGameTime() / 24000 - this.lastGrowth / 24000) >= 1;
    }

    public void growCrop(World world, BlockPos pos, BlockState state, float speed, float level, float seasonModifier) {
        Block block = state.getBlock();
        this.age += speed * seasonModifier;

        if (block instanceof BlockCrop) {
            this.age = Math.min(((BlockCrop) block).properties().growth(), this.age);
        }
        this.world.notifyBlockUpdate(pos, state, state, 2);
        this.markDirty();
    }

    public void setWithered() {
        this.withered = true;
        //this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).withMirror(mirrorIn))
    }

    public boolean isWithered() {
        return this.withered;
    }

    /*@Override
    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }*/

    public void postProcess() {

    }


    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.read(this.getBlockState(), pkt.getNbtCompound());
        this.getWorld().notifyBlockUpdate(this.pos, this.getWorld().getBlockState(this.pos), this.getWorld().getBlockState(this.pos), 3);
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        this.age = compound.getFloat("Age");
        this.isGiant = compound.getBoolean("Giant");
        this.level = compound.getFloat("Level");
        this.withered = compound.getBoolean("Withered");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putFloat("Age", this.age);
        compound.putBoolean("Giant", this.isGiant);
        compound.putFloat("Level", this.level);
        compound.putBoolean("Withered", this.withered);
        return compound;
    }
}