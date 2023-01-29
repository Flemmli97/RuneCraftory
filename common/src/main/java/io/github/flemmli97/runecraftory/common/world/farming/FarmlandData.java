package io.github.flemmli97.runecraftory.common.world.farming;

import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.api.enums.EnumSeason;
import io.github.flemmli97.runecraftory.api.enums.EnumWeather;
import io.github.flemmli97.runecraftory.common.blocks.BlockCrop;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.utils.GrassRegrowUtil;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import io.github.flemmli97.runecraftory.common.world.WorldHandler;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class FarmlandData {

    public static final float DEFAULT_SPEED = 1;
    public static final float DEFAULT_QUALITY = 0;
    public static final float DEFAULT_SIZE = 0;
    public static final int DEFAULT_DEFENCE = 0;
    public static final int DEFAULT_HEALTH = 32;

    public static final float MAX_SPEED = 5;
    public static final float MAX_QUALITY = 2;
    public static final float MAX_SIZE = 2;
    public static final int MAX_DEFENCE = 64;
    public static final int MAX_HEALTH = 255;

    public final BlockPos pos;

    //Soil Stats
    private float growth = 1;
    private float quality, size;
    private int health = 32;
    private int defence;

    //Crop Stats
    private float cropAge;
    private float cropSize;
    private float cropLevel = 1;

    private int lastUpdateDay;
    private int scheduledStormTicks, scheduledWatering;
    private final List<ExternalModifiers> scheduledData = new ArrayList<>();

    private boolean isLoaded, isFarmBlock;

    public FarmlandData(BlockPos pos) {
        this.pos = pos;
    }

    public static FarmlandData fromTag(CompoundTag tag, BlockPos pos) {
        FarmlandData data = new FarmlandData(pos);
        data.load(tag);
        return data;
    }

    public float getGrowth() {
        return this.growth;
    }

    public void applyGrowthFertilizer(float amount) {
        this.applyGrowthFertilizer(amount, true);
    }

    private void applyGrowthFertilizer(float amount, boolean sendToTracking) {
        this.growth = Mth.clamp(this.growth + amount, 0.1f, MAX_SPEED);
    }

    public boolean canUseBonemeal() {
        return this.growth < 2.35;
    }

    public void applyBonemeal() {
        if (this.growth < 2.35)
            this.applyGrowthFertilizer(0.15f);
    }

    public float getQuality() {
        return this.quality;
    }

    public void modifyQuality(float amount) {
        this.modifyQuality(amount, true);
    }

    private void modifyQuality(float amount, boolean sendToTracking) {
        this.quality = Mth.clamp(this.quality + amount, 0, MAX_QUALITY);
    }

    public float getSize() {
        return this.size;
    }

    public void applySizeFertilizer(boolean growGiant) {
        this.applySizeFertilizer((growGiant ? 1 : -1), true);
    }

    private void applySizeFertilizer(float giantMod, boolean sendToTracking) {
        this.size = Mth.clamp(this.size + giantMod, -2, MAX_SIZE);
    }

    public int getHealth() {
        return this.health;
    }

    public void modifyHealth(int amount) {
        this.modifyHealth(amount, true);
    }

    private void modifyHealth(int amount, boolean sendToTracking) {
        this.health = Mth.clamp(this.health + amount, 0, MAX_HEALTH);
    }

    public int getDefence() {
        return this.defence;
    }

    public void modifyDefence(int amount) {
        this.modifyDefence(amount, true);
    }

    private void modifyDefence(int amount, boolean sendToTracking) {
        this.defence = Mth.clamp(this.defence + amount, 0, MAX_DEFENCE);
    }

    public boolean hasDefaultStats() {
        return this.growth == DEFAULT_SPEED
                && this.quality == DEFAULT_QUALITY
                && this.size == DEFAULT_SIZE
                && this.health == DEFAULT_HEALTH;
    }

    protected void updateFarmBlock(boolean isFarmBlock) {
        this.isFarmBlock = isFarmBlock;
    }

    public boolean isFarmBlock() {
        return this.isFarmBlock;
    }

    public boolean shouldBeRemoved() {
        if (this.isFarmBlock)
            return false;
        return this.hasDefaultStats();
    }

    public void onRegrowableHarvest(ServerLevel level, BlockPos pos, BlockState state) {
        if (!(state.getBlock() instanceof CropBlock crop))
            return;
        CropProperties props = DataPackHandler.SERVER_PACK.cropManager().get(crop.asItem());
        if (props == null || !props.regrowable())
            return;
        float max = props.growth();
        this.cropAge = max * 0.5f;
        int maxAge = crop.getMaxAge();
        int stage = Math.round(this.cropAge * maxAge) / props.growth();
        //Update the blockstate according to the growth age
        BlockState newState = crop.getStateForAge(Math.min(stage, maxAge));
        level.setBlock(pos, newState, Block.UPDATE_ALL);
    }

    public void onCropUpdate(ServerLevel level, BlockPos pos, BlockState state) {
        if (!(state.getBlock() instanceof CropBlock crop)) {
            this.resetCrop();
        }
    }

    //===== Update stuff

    /**
     * Use when the farmland is not loaded and it gets watered from something (e.g. weather)
     */
    public void scheduleFarmlandWatering(ServerLevel level) {
        this.scheduledWatering++;
    }

    /**
     * Handles the farmland when its storming. Destroying crops etc.
     */
    public void onStorming(ServerLevel level) {
        if (!this.isFarmBlock)
            return;
        if (!this.isLoaded) {
            this.scheduledStormTicks++;
            this.scheduleFarmlandWatering(level);
            return;
        }
        this.doStormingLogic(level, 1);
    }

    private void doStormingLogic(ServerLevel level, int amount) {
        if (!this.isFarmBlock)
            return;
        BlockState state = null;
        BlockPos cropPos = this.pos.above();
        for (int i = 0; i < amount; i++) {
            this.defence--;
            if (this.defence <= 0) {
                state = state == null ? level.getBlockState(cropPos) : state;
                boolean destroyChance = level.random.nextFloat() < 0.4f;
                if (destroyChance) {
                    if (state.getBlock() instanceof CropBlock || state.getBlock() instanceof BlockCrop) {
                        if (level.random.nextFloat() < 0.6f) {
                            state = Blocks.AIR.defaultBlockState();
                            level.destroyBlock(cropPos, true);
                        } else
                            state = ModBlocks.witheredGrass.get().defaultBlockState();
                    } else {
                        state = Blocks.DIRT.defaultBlockState();
                        break;
                    }
                }
            }
        }
        if (state != null) {
            if (state.getBlock() == Blocks.DIRT)
                level.setBlock(this.pos, state, Block.UPDATE_ALL);
            else if (state.getBlock() == Blocks.AIR)
                level.destroyBlock(cropPos, true);
            else
                level.setBlock(cropPos, state, Block.UPDATE_ALL);
        }
    }

    public void tick(ServerLevel level, boolean onLoad) {
        this.lastUpdateDay = WorldUtils.day(level);
        //If its not a farm block can do stuff without it being loaded
        if (!this.isFarmBlock) {
            this.regenFarmlandStats();
            this.resetCrop();
            return;
        }
        WorldHandler handler = WorldHandler.get(level.getServer());
        ExternalModifiers ext = new ExternalModifiers(handler.currentSeason(), handler.currentWeather());
        if (!this.isLoaded) {
            this.scheduledData.add(ext);
            return;
        }
        if (!onLoad) {
            this.scheduledData.add(ext);
        } else //On load perform the missed storms
            this.doStormingLogic(level, this.scheduledStormTicks);
        BlockState farm = level.getBlockState(this.pos);
        boolean isWet = this.isFarmBlock && farm.getValue(FarmBlock.MOISTURE) > 0;
        BlockPos cropPos = this.pos.above();
        BlockState cropState = level.getBlockState(cropPos);

        //Do the stuff like block update etc. after the loop
        List<Runnable> run = new ArrayList<>();
        if (isWet)
            run.add(() -> level.setBlock(this.pos, farm.setValue(FarmBlock.MOISTURE, 0), Block.UPDATE_ALL));

        boolean growHerb = false;
        boolean cropRecalc = false;
        CropProperties props = cropState.getBlock() instanceof CropBlock crop ? DataPackHandler.SERVER_PACK.cropManager().get(crop.asItem()) : null;

        int wiltStage = 0;
        for (ExternalModifiers modifiers : this.scheduledData) {
            //Regen stats if unused
            if (!(cropState.getBlock() instanceof CropBlock crop)) {
                this.regenFarmlandStats();
                this.resetCrop();
                if (!growHerb) {
                    if (cropState.isAir())
                        if (level.random.nextFloat() < 0.02) {
                            run.add(() -> GrassRegrowUtil.tryGrowHerb(level, cropPos));
                            growHerb = true;
                        } else
                            growHerb = true;
                }
                //Nothing more to do
                if (growHerb && this.hasDefaultStats())
                    break;
                continue;
            }
            //Dont do stuff if crop is fully grown.
            //No withering unlike game (for e.g. building purposes)
            if (crop.isMaxAge(cropState)) {
                this.resetCrop();
                break;
            }
            //Handle crop growth
            boolean didCropGrow = false;
            boolean maxAgeStop = false;
            if (isWet) {
                if (props != null) {
                    if (!cropRecalc) {
                        cropRecalc = true;
                        run.add(() -> {
                            int maxAge = crop.getMaxAge();
                            int stage = Math.round(this.cropAge * maxAge) / props.growth();
                            //Update the blockstate according to the growth age
                            BlockState newState = crop.getStateForAge(Math.min(stage, maxAge));
                            level.setBlock(cropPos, newState, Block.UPDATE_ALL);
                            if (crop.isMaxAge(newState)) {
                                this.resetCrop();
                            }
                            Platform.INSTANCE.cropGrowEvent(level, cropPos, level.getBlockState(cropPos));
                        });
                    }
                    float season = props.seasonMultiplier(modifiers.season);
                    float runeyBonus = modifiers.weather == EnumWeather.RUNEY ? 5 : 1;
                    float speed = this.growth * season * runeyBonus;
                    this.cropAge += Math.min(props.growth(), speed);
                    this.cropLevel += this.quality * (level.getRandom().nextFloat() * 0.5 + 0.5);
                    this.cropSize += this.size * (level.getRandom().nextFloat() * 0.5 + 0.5);
                    didCropGrow = true;
                    if (this.cropAge >= props.growth())
                        maxAgeStop = true;
                }
                isWet = this.scheduledWatering > 0;
                this.scheduledWatering = Math.max(0, --this.scheduledWatering);
            } else if (cropState.getBlock() instanceof BlockCrop) {
                if (level.random.nextFloat() < GeneralConfig.witherChance) {
                    wiltStage++;
                }
            }

            if (didCropGrow) {
                this.modifyHealth(-1, false);
                this.applyGrowthFertilizer(-0.1f, false);
                this.modifyQuality(-0.05f, false);
            } else {
                this.regenFarmlandStats();
            }
            if (maxAgeStop)
                break;
        }
        this.scheduledData.clear();

        //Finalize the tick run
        run.forEach(Runnable::run);
        if (wiltStage > 0) {
            if (wiltStage > 1 || cropState.getValue(BlockCrop.WILTED)) {
                level.setBlock(cropPos, ModBlocks.witheredGrass.get().defaultBlockState(), Block.UPDATE_ALL);
            } else {
                level.setBlock(cropPos, cropState.setValue(BlockCrop.WILTED, true), Block.UPDATE_ALL);
            }
        }
    }

    private void regenFarmlandStats() {
        this.growth = Math.min(this.growth + 0.1F, DEFAULT_SPEED);
        this.modifyQuality(-0.1f, false);
        this.size = Mth.clamp(this.size - 0.05f, 0, 2);
        this.health = Math.min(DEFAULT_HEALTH, ++this.health);
    }

    private void resetCrop() {
        this.cropAge = 0;
        this.cropLevel = 1;
        this.cropSize = 0;
    }

    public void onLoad(ServerLevel level) {
        int day = WorldUtils.day(level);
        if (this.lastUpdateDay != day) {
            level.getServer().tell(new TickTask(1, () -> this.tick(level, true)));
        }
        this.isLoaded = true;
    }

    public void onUnload(ServerLevel level) {
        this.isLoaded = false;
    }

    public void load(CompoundTag nbt) {
        this.growth = nbt.getFloat("Growth");
        this.quality = nbt.getFloat("Quality");
        this.size = nbt.getFloat("Size");
        this.defence = nbt.getInt("Defence");
        this.health = nbt.getInt("Health");

        this.cropAge = nbt.getFloat("CropAge");
        this.cropLevel = nbt.getFloat("CropLevel");
        this.cropSize = nbt.getFloat("CropSize");

        this.lastUpdateDay = nbt.getInt("LastUpdate");
    }

    public CompoundTag save() {
        CompoundTag nbt = new CompoundTag();
        nbt.putFloat("Growth", this.growth);
        nbt.putFloat("Quality", this.quality);
        nbt.putFloat("Size", this.size);
        nbt.putInt("Health", this.health);
        nbt.putInt("Defence", this.defence);

        nbt.putFloat("CropAge", this.cropAge);
        nbt.putFloat("CropLevel", this.cropLevel);
        nbt.putFloat("CropSize", this.cropSize);

        nbt.putInt("LastUpdate", this.lastUpdateDay);
        return nbt;
    }

    protected record ExternalModifiers(EnumSeason season, EnumWeather weather) {
    }
}
