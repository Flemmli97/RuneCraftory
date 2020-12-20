package com.flemmli97.runecraftory.common.blocks.tile;

import com.flemmli97.runecraftory.api.IDailyUpdate;
import com.flemmli97.runecraftory.api.datapack.CropProperties;
import com.flemmli97.runecraftory.api.enums.EnumWeather;
import com.flemmli97.runecraftory.common.blocks.BlockCrop;
import com.flemmli97.runecraftory.common.datapack.DataPackHandler;
import com.flemmli97.runecraftory.common.registry.ModBlocks;
import com.flemmli97.runecraftory.common.utils.WorldUtils;
import com.flemmli97.runecraftory.common.world.WorldHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.concurrent.TickDelayedTask;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;

public class TileFarm extends TileEntity implements IDailyUpdate {

    private float growthMultiplier = 1;
    private boolean growGiant;
    private int health = 255;
    private float level;
    private int lastUpdateDay;
    //Used for other crops not from this mod
    private float age;
    //If the tile got updated during reload.
    private boolean check;

    public TileFarm() {
        super(ModBlocks.farmTile.get());
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
        boolean isWet = world.getBlockState(this.pos).get(FarmlandBlock.MOISTURE) > 0;
        BlockPos cropPos = this.pos.up();
        BlockState cropState = world.getBlockState(cropPos);
        boolean didCropGrow = false;
        if (isWet) {
            world.setBlockState(this.pos, this.getBlockState().with(FarmlandBlock.MOISTURE, 0));
            if (cropState.getBlock() instanceof BlockCrop) {
                //Let the crop tile entity handle growth
                BlockCrop crop = (BlockCrop) cropState.getBlock();
                TileCrop tile = (TileCrop) world.getTileEntity(cropPos);
                float season = crop.properties().seasonMultiplier(WorldHandler.get(world).currentSeason());
                float runeyBonus = WorldHandler.get(world).currentWeather() == EnumWeather.RUNEY ? 7 : 1;
                if (!tile.isFullyGrown(crop))
                    tile.growCrop(world, cropPos, cropState, this.growthMultiplier * runeyBonus, this.level, season);
                didCropGrow = true;
                ForgeHooks.onCropsGrowPost(world, cropPos, world.getBlockState(cropPos));
            } else if (cropState.getBlock() instanceof CropsBlock) {
                CropsBlock crop = (CropsBlock) cropState.getBlock();
                CropProperties props = DataPackHandler.getCropStat(crop.asItem());
                if (props != null) {
                    float season = props.seasonMultiplier(WorldHandler.get(world).currentSeason());
                    float runeyBonus = WorldHandler.get(world).currentWeather() == EnumWeather.RUNEY ? 7 : 1;
                    float speed = this.growthMultiplier * season * runeyBonus;
                    this.age += Math.min(props.growth(), speed);
                    //The blockstates maxAge
                    int maxAge = crop.getMaxAge();

                    int stage = Math.round(this.age * maxAge) / props.growth();
                    //Update the blockstate according to the growth age from this tile
                    world.setBlockState(cropPos, crop.withAge(Math.min(stage, maxAge)), 3);
                } else {
                    //if not increase block state age by one per day with reduced bonus from growth multiplier
                    int age = cropState.get(CropsBlock.AGE);
                    world.setBlockState(cropPos, crop.withAge(Math.min((int) (age + Math.max(1, this.growthMultiplier / 2.4)), crop.getMaxAge())), 2);
                }
                didCropGrow = true;
                ForgeHooks.onCropsGrowPost(world, cropPos, world.getBlockState(cropPos));
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
        }
    }

    @Override
    public void fromTag(BlockState state, CompoundNBT compound) {
        super.fromTag(state, compound);
        this.health = compound.getInt("Health");
        this.growthMultiplier = compound.getFloat("Growth");
        this.level = compound.getFloat("Level");
        this.age = compound.getFloat("Age");
        this.growGiant = compound.getBoolean("Giant");
        this.lastUpdateDay = compound.getInt("LastUpdate");
        this.check = true;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("Health", this.health);
        compound.putFloat("Level", this.level);
        compound.putFloat("Growth", this.growthMultiplier);
        compound.putFloat("Age", this.age);
        compound.putBoolean("Giant", this.growGiant);
        compound.putInt("LastUpdate", this.lastUpdateDay);
        return compound;
    }

    @Override
    public void onLoad() {
        if (this.world != null && !this.world.isRemote) {
            WorldHandler.get((ServerWorld) this.world).addToTracker(this);
            if(this.check && Math.abs(this.lastUpdateDay - WorldUtils.day(this.world))>0) {
                this.world.getServer().enqueue(new TickDelayedTask(1, ()->this.update((ServerWorld) this.world)));
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
