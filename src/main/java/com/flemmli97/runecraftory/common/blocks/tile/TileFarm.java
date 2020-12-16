package com.flemmli97.runecraftory.common.blocks.tile;

import com.flemmli97.runecraftory.api.IDailyUpdate;
import com.flemmli97.runecraftory.common.world.WorldHandler;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.server.ServerWorld;

public class TileFarm extends TileEntity implements IDailyUpdate {

    private float growthMultiplier = 1;
    private boolean growGiant;
    private int health = 255;
    private float level;
    private long lastUpdate;
    //Used for other crops not from this mod
    private float age;
    //If the tile got updated during reload.
    private boolean updated;

    public TileFarm(TileEntityType<?> p_i48289_1_) {
        super(p_i48289_1_);
    }

    public int health() {
        return this.health;
    }

    public float level() {
        return this.level;
    }

    public float growth() {
        return this.growthMultiplier;
    }

    public boolean growGiant() {
        return this.growGiant;
    }

    public void applyGrowthFertilizer(float amount) {
        this.growthMultiplier = Math.min(5, this.growthMultiplier + amount);
        this.markDirty();
    }

    public void applyLevelFertilizer(float amount) {
        this.level = Math.min(2, (this.level + amount));
        this.markDirty();
    }

    public void applyHealth(int amount) {
        this.health = Math.min(255, this.health + amount);
        this.markDirty();
    }

    public void applySizeFertilizer(boolean growGiant) {
        this.growGiant = growGiant;
        this.markDirty();
    }

    @Override
    public void update(ServerWorld world) {
        /*boolean isWet = world.getBlockState(pos).getValue(BlockFarmland.MOISTURE) > 0;
        BlockPos cropPos = pos.up();
        IBlockState cropState = world.getBlockState(cropPos);
        boolean didCropGrow = false;
        if (isWet) {
            world.setBlockState(pos, state.withProperty(BlockFarmland.MOISTURE, 0));
            if (cropState.getBlock() instanceof BlockCropBase) {
                //Let the crop tile entity handle growth
                BlockCropBase crop = (BlockCropBase) cropState.getBlock();
                TileCrop tile = (TileCrop) world.getTileEntity(cropPos);
                float season = crop.properties().seasonMultiplier(CalendarHandler.get(world).currentSeason());
                float runeyBonus = WeatherData.get(world).currentWeather() == EnumWeather.RUNEY ? 7 : 1;
                if (!tile.isFullyGrown(crop))
                    tile.growCrop(world, cropPos, cropState, this.growthMultiplier * runeyBonus, this.level, season);
                didCropGrow = true;
                net.minecraftforge.common.ForgeHooks.onCropsGrowPost(world, cropPos, state, world.getBlockState(cropPos));
            } else if (cropState.getBlock() instanceof BlockCrops) {
                BlockCrops crop = (BlockCrops) cropState.getBlock();
                CropProperties props = CropMap.getProperties(crop.getItem(this.world, cropPos, cropState));
                if (props != null) {
                    float season = props.seasonMultiplier(CalendarHandler.get(world).currentSeason());
                    float runeyBonus = WeatherData.get(world).currentWeather() == EnumWeather.RUNEY ? 7 : 1;
                    float speed = this.growthMultiplier * season * runeyBonus;
                    this.age += Math.min(props.growth(), speed);
                    //The blockstates maxAge
                    int maxAge = crop.getMaxAge();

                    int stage = Math.round(this.age * maxAge) / props.growth();
                    //Update the blockstate according to the growth age from this tile
                    world.setBlockState(cropPos, crop.withAge(Math.min(stage, maxAge)), 2);
                } else {
                    //if not increase block state age by one per day with reduced bonus from growth multiplier
                    int age = cropState.getValue(BlockCrops.AGE);
                    world.setBlockState(cropPos, crop.withAge(Math.min((int) (age + Math.max(1, this.growthMultiplier / 2.4)), crop.getMaxAge())), 2);
                }
                didCropGrow = true;
                net.minecraftforge.common.ForgeHooks.onCropsGrowPost(world, cropPos, state, world.getBlockState(cropPos));
            }
        }

        if (didCropGrow) {
            if (world.rand.nextInt(2) == 0) {
                this.growthMultiplier = Math.max(this.growthMultiplier - 0.1F, 0.1F);
            }
            this.health = Math.max(0, --this.health);
            this.level = Math.max(0, this.level - 0.01F);
        } else {
            if (world.rand.nextInt(4) == 0) {
                this.growthMultiplier = Math.min(this.growthMultiplier + 0.2F, 1F);
            }
        }*/
    }

    @Override
    public void fromTag(BlockState state, CompoundNBT compound) {
        super.fromTag(state, compound);
        this.health = compound.getInt("Health");
        this.growthMultiplier = compound.getFloat("Growth");
        this.level = compound.getFloat("Level");
        this.age = compound.getFloat("Age");
        this.growGiant = compound.getBoolean("Giant");
        this.lastUpdate = compound.getLong("LastUpdate");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("Health", this.health);
        compound.putFloat("Level", this.level);
        compound.putFloat("Growth", this.growthMultiplier);
        compound.putFloat("Age", this.age);
        compound.putBoolean("Giant", this.growGiant);
        compound.putLong("LastUpdate", this.lastUpdate);
        return compound;
    }

    @Override
    public void onLoad() {
        if (this.world != null && !this.world.isRemote)
            WorldHandler.get((ServerWorld) this.world).addToTracker(this);
    }

    @Override
    public void onChunkUnloaded() {
        if (this.world != null && !this.world.isRemote)
            WorldHandler.get((ServerWorld) this.world).removeFromTracker(this);
    }
}
