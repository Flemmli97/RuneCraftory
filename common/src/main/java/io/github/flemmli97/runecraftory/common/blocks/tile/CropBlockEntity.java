package io.github.flemmli97.runecraftory.common.blocks.tile;

import io.github.flemmli97.runecraftory.common.blocks.BlockCrop;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CropBlockEntity extends BlockEntity {

    private boolean isGiant = false;
    private float age;
    private long lastGrowth = 0;
    private float cropLvl;
    private boolean withered;
    private BlockPos[] nearbyGiant;

    public CropBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlocks.cropTile.get(), blockPos, blockState);
    }

    public int age() {
        return (int) this.age;
    }

    public void resetAge() {
        this.age = 0;
        this.setChanged();
    }

    public int level() {
        return (int) this.cropLvl;
    }

    public boolean isGiant() {
        return this.isGiant;
    }

    public void increaseLevel(float amount) {
        this.cropLvl += amount;
    }

    public boolean isFullyGrown(BlockCrop block) {
        return this.age >= block.properties().growth();
    }

    public boolean canGrow() {
        return Math.abs(this.level.getGameTime() / 24000 - this.lastGrowth / 24000) >= 1;
    }

    public void growCrop(Level level, BlockPos pos, BlockState state, float speed, float cropLvl, float seasonModifier) {
        Block block = state.getBlock();
        this.age += speed * seasonModifier;
        BlockState newState = state;
        if (block instanceof BlockCrop crop) {
            this.age = Math.min(((BlockCrop) block).properties().growth(), this.age);
            int stateAge = (int) (this.age * 3 / (float) crop.properties().growth());
            newState = state.setValue(BlockCrop.AGE, Mth.clamp(stateAge, 0, 3));
        }
        level.sendBlockUpdated(pos, state, newState, Block.UPDATE_ALL);
        this.setChanged();
    }

    public void setWithered() {
        this.withered = true;
        //this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).withMirror(mirrorIn))
    }

    public boolean isWithered() {
        return this.withered;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.age = nbt.getFloat("Age");
        this.isGiant = nbt.getBoolean("Giant");
        this.cropLvl = nbt.getFloat("Level");
        this.withered = nbt.getBoolean("Withered");
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putFloat("Age", this.age);
        nbt.putBoolean("Giant", this.isGiant);
        nbt.putFloat("Level", this.cropLvl);
        nbt.putBoolean("Withered", this.withered);
    }
}