package io.github.flemmli97.runecraftory.common.blocks.tile;

import io.github.flemmli97.runecraftory.api.IDailyUpdate;
import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.api.enums.EnumWeather;
import io.github.flemmli97.runecraftory.common.blocks.BlockCrop;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.utils.GrassRegrowUtil;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import io.github.flemmli97.runecraftory.common.world.WorldHandler;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FarmBlockEntity extends BlockEntity implements IDailyUpdate {

    //Stats
    private float growthMultiplier = 1;
    private float giantBonus;
    private int health = 32;
    private float farmLevel;

    private int lastUpdateDay;
    //Used for other crops not from this mod
    private float age;
    //If the tile got updated during reload.
    private boolean check;

    public FarmBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlocks.farmTile.get(), blockPos, blockState);
    }

    public int health() {
        return this.health;
    }

    public float level() {
        return this.farmLevel;
    }

    public float growth() {
        return this.growthMultiplier;
    }

    public float giantBonus() {
        return this.giantBonus;
    }

    public void applyGrowthFertilizer(float amount) {
        this.applyGrowthFertilizer(amount, true);
    }

    private void applyGrowthFertilizer(float amount, boolean blockUpdate) {
        this.growthMultiplier = Mth.clamp(this.growthMultiplier + amount, 0.1f, 5);
        if (blockUpdate) {
            this.setChanged();
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public boolean canUseBonemeal() {
        return this.growthMultiplier < 2.35;
    }

    public void applyBonemeal() {
        if (this.growthMultiplier < 2.35)
            this.applyGrowthFertilizer(0.15f);
    }

    public void applyLevelFertilizer(float amount) {
        this.applyLevelFertilizer(amount, true);
    }

    private void applyLevelFertilizer(float amount, boolean blockUpdate) {
        this.farmLevel = Mth.clamp(this.farmLevel + amount, 0, 2);
        if (blockUpdate) {
            this.setChanged();
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public void applyHealth(int amount) {
        this.applyHealth(amount, true);
    }

    private void applyHealth(int amount, boolean blockUpdate) {
        this.health = Mth.clamp(this.health + amount, 0, 255);
        if (blockUpdate) {
            this.setChanged();
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public void applySizeFertilizer(boolean growGiant) {
        this.applySizeFertilizer((growGiant ? 1 : -1), true);
    }

    private void applySizeFertilizer(float giantMod, boolean blockUpdate) {
        this.giantBonus = Mth.clamp(this.giantBonus + giantMod, -2, 2);
        if (blockUpdate) {
            this.setChanged();
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public void onStorming() {
        this.health--;
        if (this.health <= 0) {
            BlockPos cropPos = this.getBlockPos().above();
            BlockState cropState = this.level.getBlockState(cropPos);
            boolean destroyChange = this.level.random.nextFloat() < 0.4f;
            if (destroyChange) {
                if (cropState.getBlock() instanceof CropBlock || cropState.getBlock() instanceof BlockCrop) {
                    if (this.level.random.nextFloat() < 0.6f) {
                        this.level.destroyBlock(cropPos, true);
                    } else
                        this.level.setBlock(cropPos, ModBlocks.witheredGrass.get().defaultBlockState(), Block.UPDATE_ALL);
                } else {
                    this.level.setBlock(this.getBlockPos(), Blocks.DIRT.defaultBlockState(), Block.UPDATE_ALL);
                }
            }
        }
        this.setChanged();
    }

    @Override
    public void update(ServerLevel level) {
        this.lastUpdateDay = WorldUtils.day(level);
        this.setChanged();
        boolean isWet = level.getBlockState(this.worldPosition).getValue(FarmBlock.MOISTURE) > 0;
        BlockPos cropPos = this.worldPosition.above();
        BlockState cropState = level.getBlockState(cropPos);
        boolean vanilla = cropState.getBlock() instanceof CropBlock;
        if (!vanilla && !(cropState.getBlock() instanceof BlockCrop)) {
            if (isWet)
                level.setBlockAndUpdate(this.worldPosition, this.getBlockState().setValue(FarmBlock.MOISTURE, 0));
            this.regenFarmlandStats();
            if (cropState.isAir() && level.random.nextFloat() < 0.02) {
                GrassRegrowUtil.tryGrowHerb(level, cropPos);
            }
            return;
        }
        if (!vanilla || ((CropBlock) cropState.getBlock()).isMaxAge(cropState)) {
            this.age = 0;
            if (vanilla)
                return;
        }
        boolean didCropGrow = false;
        if (isWet) {
            level.setBlockAndUpdate(this.worldPosition, this.getBlockState().setValue(FarmBlock.MOISTURE, 0));
            if (cropState.getBlock() instanceof BlockCrop crop) {
                //Let the crop tile entity handle growth
                CropBlockEntity tile = (CropBlockEntity) level.getBlockEntity(cropPos);
                if (!tile.isFullyGrown(crop)) {
                    float season = crop.properties().map(p -> p.seasonMultiplier(WorldHandler.get(level.getServer()).currentSeason())).orElse(1f);
                    float runeyBonus = WorldHandler.get(level.getServer()).currentWeather() == EnumWeather.RUNEY ? 5 : 1;
                    tile.growCrop(level, cropPos, cropState, this.growthMultiplier * runeyBonus, this.farmLevel, season);
                    didCropGrow = true;
                    Platform.INSTANCE.cropGrowEvent(level, cropPos, level.getBlockState(cropPos));
                }
            } else if (cropState.getBlock() instanceof CropBlock crop) {
                if (crop.isMaxAge(cropState)) {
                    this.age = 0;
                } else {
                    CropProperties props = DataPackHandler.cropManager().get(crop.asItem());
                    if (props != null) {
                        float season = props.seasonMultiplier(WorldHandler.get(level.getServer()).currentSeason());
                        float runeyBonus = WorldHandler.get(level.getServer()).currentWeather() == EnumWeather.RUNEY ? 5 : 1;
                        float speed = this.growthMultiplier * season * runeyBonus;
                        this.age += Math.min(props.growth(), speed);
                        //The blockstates maxAge
                        int maxAge = crop.getMaxAge();

                        int stage = Math.round(this.age * maxAge) / props.growth();
                        //Update the blockstate according to the growth age from this tile
                        BlockState newState = crop.getStateForAge(Math.min(stage, maxAge));
                        level.setBlock(cropPos, newState, 3);
                        if (crop.isMaxAge(newState)) {
                            this.age = 0;
                        }
                    }
                    didCropGrow = true;
                    Platform.INSTANCE.cropGrowEvent(level, cropPos, level.getBlockState(cropPos));
                }
            }
        } else if (cropState.getBlock() instanceof BlockCrop) {
            CropBlockEntity tile = (CropBlockEntity) level.getBlockEntity(cropPos);
            tile.tryWitherCrop(level.random);
        }

        if (didCropGrow) {
            if (level.random.nextInt(3) == 0) {
                this.applyGrowthFertilizer(-0.1f, false);
            }
            if (level.random.nextInt(5) == 0)
                this.applyHealth(-1, false);
            this.applyLevelFertilizer(-0.05f, false);
        } else {
            if (level.random.nextInt(4) == 0) {
                this.regenFarmlandStats();
            }
        }
    }

    private void regenFarmlandStats() {
        this.growthMultiplier = Math.min(this.growthMultiplier + 0.1F, 1);
        this.health = Math.min(32, ++this.health);
        this.applyLevelFertilizer(-0.1f, false);
        this.giantBonus = Mth.clamp(this.giantBonus - 0.05f, 0, 2);
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
                serverLevel.getServer().tell(new TickTask(1, () -> this.update(serverLevel)));
            } else {
                this.lastUpdateDay = day;
                this.check = false;
                serverLevel.getServer().tell(new TickTask(1, this::setChanged));
            }
        }
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.health = nbt.getInt("Health");
        this.growthMultiplier = nbt.getFloat("Growth");
        this.farmLevel = nbt.getFloat("Level");
        this.age = nbt.getFloat("Age");
        this.giantBonus = nbt.getFloat("Giant");
        this.lastUpdateDay = nbt.getInt("LastUpdate");
        this.check = true;
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("Health", this.health);
        nbt.putFloat("Level", this.farmLevel);
        nbt.putFloat("Growth", this.growthMultiplier);
        nbt.putFloat("Age", this.age);
        nbt.putFloat("Giant", this.giantBonus);
        nbt.putInt("LastUpdate", this.lastUpdateDay);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (this.level instanceof ServerLevel serverLevel) {
            WorldHandler.get(serverLevel.getServer()).removeFromTracker(this);
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("Health", this.health);
        nbt.putFloat("Level", this.farmLevel);
        nbt.putFloat("Growth", this.growthMultiplier);
        nbt.putFloat("Giant", this.giantBonus);
        return nbt;
    }
}
