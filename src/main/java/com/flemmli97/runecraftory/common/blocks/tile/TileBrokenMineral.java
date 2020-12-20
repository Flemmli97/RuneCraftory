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
import net.minecraft.world.server.ServerWorld;

public class TileBrokenMineral extends TileEntity implements IDailyUpdate {

    private int lastUpdateDay = -1;

    public TileBrokenMineral() {
        super(ModBlocks.brokenMineralTile.get());
        System.out.println("tile " + this);
    }

    @Override
    public void update(ServerWorld world) {
        System.out.println("on update");
        BlockState state = this.world.getBlockState(this.pos);
        Block block = state.getBlock();
        if(block instanceof BlockBrokenMineral)
        {
            BlockBrokenMineral mineral = (BlockBrokenMineral) block;
            this.world.setBlockState(this.pos, mineral.getMineralState(state));
            this.lastUpdateDay = WorldUtils.day(world);
        }
    }

    @Override
    public void fromTag(BlockState state, CompoundNBT nbt) {
        super.fromTag(state, nbt);
        this.lastUpdateDay = nbt.getInt("LastUpdate");
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
            if(this.lastUpdateDay != -1 && Math.abs(this.lastUpdateDay - WorldUtils.day(this.world))>0)
                this.update((ServerWorld) this.world);
            WorldHandler.get((ServerWorld) this.world).addToTracker(this);
        }
    }

    @Override
    public void onChunkUnloaded() {
        if (this.world != null && !this.world.isRemote) {
            WorldHandler.get((ServerWorld) this.world).removeFromTracker(this);
        }
    }
}
