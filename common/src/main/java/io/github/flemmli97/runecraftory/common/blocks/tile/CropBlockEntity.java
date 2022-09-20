package io.github.flemmli97.runecraftory.common.blocks.tile;

import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.common.blocks.BlockCrop;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class CropBlockEntity extends BlockEntity {

    private boolean isGiant = false;
    private float age;
    private long lastGrowth = 0;
    private float cropLvl;
    private boolean wilted;
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

    public void onRegrowableHarvest(BlockCrop crop) {
        float max = crop.properties().map(CropProperties::growth).orElse(0);
        this.age = max * 0.5f;
        this.updateState();
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
        return block.properties().map(p -> this.age >= p.growth()).orElse(false);
    }

    public boolean canGrow() {
        return Math.abs(this.level.getGameTime() / 24000 - this.lastGrowth / 24000) >= 1;
    }

    public void growCrop(Level level, BlockPos pos, BlockState state, float speed, float cropLvl, float seasonModifier) {
        if (this.wilted)
            this.wilted = false;
        Block block = state.getBlock();
        this.age += speed * seasonModifier;
        if (block instanceof BlockCrop crop) {
            float max = crop.properties().map(CropProperties::growth).orElse(0);
            this.age = Math.min(max, this.age);
            int stateAge = (int) (this.age * 3 / max);
            state = state.setValue(BlockCrop.AGE, Mth.clamp(stateAge, 0, 3)).setValue(BlockCrop.WILTED, this.wilted);
            level.setBlock(pos, state, Block.UPDATE_ALL);
        }
        this.setChanged();
    }

    public void tryWitherCrop(Random rand) {
        if (this.getBlockState().getBlock() instanceof BlockCrop crop && crop.isMaxAge(this.getBlockState(), this.level, this.getBlockPos()))
            return;
        if (rand.nextFloat() < GeneralConfig.witherChance) {
            if (this.isWilted()) {
                if (this.level != null)
                    this.level.setBlock(this.getBlockPos(), ModBlocks.witheredGrass.get().defaultBlockState(), Block.UPDATE_ALL);
            } else {
                this.setWilted(true);
            }
        }
    }

    private void updateState() {
        if (!(this.level instanceof ServerLevel))
            return;
        BlockState newState = this.getBlockState();
        if (newState.getBlock() instanceof BlockCrop crop) {
            float max = crop.properties().map(CropProperties::growth).orElse(0);
            this.age = Math.min(crop.properties().map(CropProperties::growth).orElse(0), this.age);
            int stateAge = (int) (this.age * 3 / max);
            newState = newState.setValue(BlockCrop.AGE, Mth.clamp(stateAge, 0, 3))
                    .setValue(BlockCrop.WILTED, this.wilted);
        }
        this.level.setBlock(this.getBlockPos(), newState, Block.UPDATE_ALL);
    }

    public boolean isWilted() {
        return this.wilted;
    }

    public void setWilted(boolean flag) {
        this.wilted = flag;
        if (this.level != null)
            this.level.setBlock(this.getBlockPos(), this.level.getBlockState(this.getBlockPos()).setValue(BlockCrop.WILTED, flag), Block.UPDATE_ALL);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.age = nbt.getFloat("Age");
        this.isGiant = nbt.getBoolean("Giant");
        this.cropLvl = nbt.getFloat("Level");
        this.wilted = nbt.getBoolean("Wilted");
        this.updateState();
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putFloat("Age", this.age);
        nbt.putBoolean("Giant", this.isGiant);
        nbt.putFloat("Level", this.cropLvl);
        nbt.putBoolean("Wilted", this.wilted);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }
}