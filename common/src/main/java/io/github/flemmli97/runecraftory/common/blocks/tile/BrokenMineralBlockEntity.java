package io.github.flemmli97.runecraftory.common.blocks.tile;

import io.github.flemmli97.runecraftory.api.IDailyUpdate;
import io.github.flemmli97.runecraftory.common.blocks.BlockBrokenMineral;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import io.github.flemmli97.runecraftory.common.world.WorldHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BrokenMineralBlockEntity extends BlockEntity implements IDailyUpdate {

    private int lastUpdateDay;
    private boolean check;

    public BrokenMineralBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlocks.brokenMineralTile.get(), blockPos, blockState);
    }

    @Override
    public void update(ServerLevel level) {
        BlockState state = this.getBlockState();
        Block block = state.getBlock();
        if (block instanceof BlockBrokenMineral mineral) {
            this.level.setBlockAndUpdate(this.worldPosition, mineral.getMineralState(state));
        }
    }

    @Override
    public boolean inValid() {
        return this.isRemoved();
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        if (this.level instanceof ServerLevel serverLevel) {
            WorldHandler.get(serverLevel.getServer()).addToTracker(this);
            int day = WorldUtils.day(this.level);
            if (this.check && this.lastUpdateDay != day) {
                this.level.getServer().tell(new TickTask(1, () -> this.update(serverLevel)));
            } else {
                this.lastUpdateDay = day;
                this.check = false;
                this.level.getServer().tell(new TickTask(1, this::setChanged));
            }
        }
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.lastUpdateDay = nbt.getInt("LastUpdate");
        this.check = true;
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("LastUpdate", this.lastUpdateDay);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (this.level instanceof ServerLevel serverLevel) {
            WorldHandler.get(serverLevel.getServer()).removeFromTracker(this);
        }
    }
}
