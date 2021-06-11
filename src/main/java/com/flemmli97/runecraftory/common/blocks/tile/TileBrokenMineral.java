package com.flemmli97.runecraftory.common.blocks.tile;

import com.flemmli97.runecraftory.api.IDailyUpdate;
import com.flemmli97.runecraftory.common.blocks.BlockBrokenMineral;
import com.flemmli97.runecraftory.common.registry.ModBlocks;
import com.flemmli97.runecraftory.common.utils.WorldUtils;
import com.flemmli97.runecraftory.common.world.WorldHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.concurrent.TickDelayedTask;
import net.minecraft.world.server.ServerWorld;

public class TileBrokenMineral extends TileEntity implements IDailyUpdate {

    private int lastUpdateDay;
    private boolean check;

    public TileBrokenMineral() {
        super(ModBlocks.brokenMineralTile.get());
    }

    @Override
    public void update(ServerWorld world) {
        BlockState state = this.getBlockState();
        Block block = state.getBlock();
        if (block instanceof BlockBrokenMineral) {
            BlockBrokenMineral mineral = (BlockBrokenMineral) block;
            this.world.setBlockState(this.pos, mineral.getMineralState(state));
        }
    }

    @Override
    public boolean inValid() {
        return this.isRemoved();
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.lastUpdateDay = nbt.getInt("LastUpdate");
        this.check = true;
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        super.write(nbt);
        nbt.putInt("LastUpdate", this.lastUpdateDay);
        return nbt;
    }

    @Override
    public void onLoad() {
        if (this.world != null && !this.world.isRemote) {
            WorldHandler.get((ServerWorld) this.world).addToTracker(this);
            if (this.check && Math.abs(this.lastUpdateDay - WorldUtils.day(this.world)) > 0) {
                this.world.getServer().enqueue(new TickDelayedTask(1, () -> this.update((ServerWorld) this.world)));
            } else {
                this.lastUpdateDay = WorldUtils.day(this.world);
                this.check = false;
                this.world.getServer().enqueue(new TickDelayedTask(1, this::markDirty));
            }
        }
    }

    @Override
    public void onChunkUnloaded() {
        if (this.world != null && !this.world.isRemote) {
            WorldHandler.get((ServerWorld) this.world).removeFromTracker(this);
        }
    }

    @Override
    public void remove() {
        super.remove();
        if (this.world != null && !this.world.isRemote) {
            WorldHandler.get((ServerWorld) this.world).removeFromTracker(this);
        }
    }
}
