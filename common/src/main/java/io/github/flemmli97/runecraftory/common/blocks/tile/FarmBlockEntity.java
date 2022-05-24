package io.github.flemmli97.runecraftory.common.blocks.tile;

import io.github.flemmli97.runecraftory.api.IDailyUpdate;
import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.api.enums.EnumWeather;
import io.github.flemmli97.runecraftory.common.blocks.BlockCrop;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import io.github.flemmli97.runecraftory.common.world.WorldHandler;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FarmBlockEntity extends BlockEntity implements IDailyUpdate {

    private float growthMultiplier = 1;
    private boolean growGiant;
    private int health = 255;
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

    public boolean growGiant() {
        return this.growGiant;
    }

    public void applyGrowthFertilizer(float amount) {
        this.growthMultiplier = Math.min(5, this.growthMultiplier + amount);
        this.setChanged();
    }

    public void applyLevelFertilizer(float amount) {
        this.farmLevel = Math.min(2, (this.farmLevel + amount));
        this.setChanged();
    }

    public boolean canUseBonemeal() {
        return this.growthMultiplier < 2.45;
    }

    public void applyBonemeal() {
        if (this.growthMultiplier < 2.45)
            this.applyGrowthFertilizer(0.05f);
    }

    public void applyHealth(int amount) {
        this.health = Math.min(255, this.health + amount);
        this.setChanged();
    }

    public void applySizeFertilizer(boolean growGiant) {
        this.growGiant = growGiant;
        this.setChanged();
    }

    @Override
    public void update(ServerLevel level) {
        boolean isWet = level.getBlockState(this.worldPosition).getValue(FarmBlock.MOISTURE) > 0;
        BlockPos cropPos = this.worldPosition.above();
        BlockState cropState = level.getBlockState(cropPos);
        boolean vanilla = cropState.getBlock() instanceof CropBlock;
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
                float season = crop.properties().map(p -> p.seasonMultiplier(WorldHandler.get(level.getServer()).currentSeason())).orElse(1f);
                float runeyBonus = WorldHandler.get(level.getServer()).currentWeather() == EnumWeather.RUNEY ? 7 : 1;
                if (!tile.isFullyGrown(crop))
                    tile.growCrop(level, cropPos, cropState, this.growthMultiplier * runeyBonus, this.farmLevel, season);
                didCropGrow = true;
                Platform.INSTANCE.cropGrowEvent(level, cropPos, level.getBlockState(cropPos));
            } else if (cropState.getBlock() instanceof CropBlock crop) {
                if (crop.isMaxAge(cropState)) {
                    this.age = 0;
                } else {
                    CropProperties props = DataPackHandler.getCropStat(crop.asItem());
                    if (props != null) {
                        float season = props.seasonMultiplier(WorldHandler.get(level.getServer()).currentSeason());
                        float runeyBonus = WorldHandler.get(level.getServer()).currentWeather() == EnumWeather.RUNEY ? 7 : 1;
                        float speed = this.growthMultiplier * season * runeyBonus;
                        this.age += Math.min(props.growth(), speed);
                        //The blockstates maxAge
                        int maxAge = crop.getMaxAge();

                        int stage = Math.round(this.age * maxAge) / props.growth();
                        //Update the blockstate according to the growth age from this tile
                        level.setBlock(cropPos, crop.getStateForAge(Math.min(stage, maxAge)), 3);
                    } else {
                        //if not increase block state age by one per day with reduced bonus from growth multiplier
                        int age = cropState.getValue(CropBlock.AGE);
                        level.setBlock(cropPos, crop.getStateForAge(Math.min((int) (age + Math.max(1, this.growthMultiplier / 2.4)), crop.getMaxAge())), 2);
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
            if (level.random.nextInt(2) == 0) {
                this.growthMultiplier = Math.max(this.growthMultiplier - 0.1F, 0.1F);
            }
            this.health = Math.max(0, --this.health);
            this.farmLevel = Math.max(0, this.farmLevel - 0.01F);
        } else {
            if (level.random.nextInt(4) == 0) {
                this.growthMultiplier = Math.min(this.growthMultiplier + 0.2F, 1F);
            }
        }
        this.setChanged();
    }

    @Override
    public boolean inValid() {
        return this.isRemoved();
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.health = nbt.getInt("Health");
        this.growthMultiplier = nbt.getFloat("Growth");
        this.farmLevel = nbt.getFloat("Level");
        this.age = nbt.getFloat("Age");
        this.growGiant = nbt.getBoolean("Giant");
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
        nbt.putBoolean("Giant", this.growGiant);
        nbt.putInt("LastUpdate", this.lastUpdateDay);
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        if (this.level instanceof ServerLevel serverLevel) {
            WorldHandler.get(serverLevel.getServer()).addToTracker(this);
            if (this.check && Math.abs(this.lastUpdateDay - WorldUtils.day(serverLevel)) > 0) {
                serverLevel.getServer().tell(new TickTask(1, () -> this.update(serverLevel)));
            } else {
                this.lastUpdateDay = WorldUtils.day(serverLevel);
                this.check = false;
                serverLevel.getServer().tell(new TickTask(1, this::setChanged));
            }
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (this.level instanceof ServerLevel serverLevel) {
            WorldHandler.get(serverLevel.getServer()).removeFromTracker(this);
        }
    }
}
