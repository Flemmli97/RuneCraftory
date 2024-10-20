package io.github.flemmli97.runecraftory.common.world.farming;

import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.api.enums.EnumSeason;
import io.github.flemmli97.runecraftory.api.enums.EnumWeather;
import io.github.flemmli97.runecraftory.common.blocks.BlockCrop;
import io.github.flemmli97.runecraftory.common.blocks.Growable;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.utils.CropUtils;
import io.github.flemmli97.runecraftory.common.utils.GrassRegrowUtil;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import io.github.flemmli97.runecraftory.common.world.WorldHandler;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Chunk independent farmland data
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
    private int cropProgress;

    private int lastUpdateDay;
    private int scheduledStormTicks, scheduledWatering, lastWeatherDay;
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

    public void applyGrowthFertilizer(@Nullable ServerLevel level, float amount) {
        this.growth = Mth.clamp(this.growth + amount, 0.1f, MAX_SPEED);
        if (level != null)
            FarmlandHandler.get(level.getServer()).scheduleUpdate(level, this);
    }

    public boolean canUseBonemeal() {
        return this.growth < 2.25;
    }

    public void applyBonemeal(@Nullable ServerLevel level) {
        if (this.growth < 2.25)
            this.applyGrowthFertilizer(level, Math.min(2.25f - this.growth, 0.15f));
    }

    public float getQuality() {
        return this.quality;
    }

    public void modifyQuality(@Nullable ServerLevel level, float amount) {
        this.quality = Mth.clamp(this.quality + amount, 0, MAX_QUALITY);
        if (level != null)
            FarmlandHandler.get(level.getServer()).scheduleUpdate(level, this);
    }

    public float getSize() {
        return this.size;
    }

    public void applySizeFertilizer(@Nullable ServerLevel level, boolean growGiant) {
        float mod = (growGiant ? 1 : -1);
        this.size = Mth.clamp(this.size + mod, -2, MAX_SIZE);
        if (level != null)
            FarmlandHandler.get(level.getServer()).scheduleUpdate(level, this);
    }

    public int getHealth() {
        return this.health;
    }

    public void modifyHealth(@Nullable ServerLevel level, int amount) {
        this.health = Mth.clamp(this.health + amount, 0, MAX_HEALTH);
        if (level != null)
            FarmlandHandler.get(level.getServer()).scheduleUpdate(level, this);
    }

    public int getDefence() {
        return this.defence;
    }

    public void modifyDefence(@Nullable ServerLevel level, int amount) {
        this.defence = Mth.clamp(this.defence + amount, 0, MAX_DEFENCE);
        if (level != null)
            FarmlandHandler.get(level.getServer()).scheduleUpdate(level, this);
    }

    public float getCropAge() {
        return (int) this.cropAge;
    }

    private int growthPercent(ServerLevel level, BlockState crop) {
        if (!(crop.getBlock() instanceof Growable))
            return 0;
        CropProperties props = DataPackHandler.INSTANCE.cropManager().get(crop.getBlock().getCloneItemStack(level, this.pos, crop).getItem());
        if (props != null) {
            return Math.min((int) (this.cropAge / props.growth() * 100), 100);
        }
        return 0;
    }

    public float getCropSize() {
        return this.cropSize;
    }

    public int getCropLevel() {
        return (int) this.cropLevel;
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
        if (!(state.getBlock() instanceof Growable crop)) {
            this.resetCrop();
            return;
        }
        CropProperties props = DataPackHandler.INSTANCE.cropManager().get(state.getBlock().getCloneItemStack(level, pos, state).getItem());
        if (props == null || !props.regrowable()) {
            this.resetCrop();
            return;
        }
        float max = props.growth();
        this.cropAge = max * 0.5f;
        int maxAge = crop.getGrowableMaxAge();
        int stage = Math.round(this.cropAge * maxAge) / props.growth();
        //Update the blockstate according to the growth age
        BlockState newState = crop.getGrowableStateForAge(state, Math.min(stage, maxAge));
        level.getServer().tell(new TickTask(1, () -> level.setBlock(pos, newState, Block.UPDATE_ALL)));
        this.cropProgress = this.growthPercent(level, state);
        FarmlandHandler.get(level.getServer()).scheduleUpdate(level, this);
    }

    public void onCropRemove(ServerLevel level, BlockPos pos, BlockState state) {
        this.resetCrop();
        FarmlandHandler.get(level.getServer()).scheduleUpdate(level, this);
    }

    //===== Update stuff

    /**
     * Use when the farmland gets watered from something
     */
    public void onWatering(ServerLevel level, int currentDay) {
        if (!this.isFarmBlock || this.lastWeatherDay == currentDay)
            return;
        this.lastWeatherDay = currentDay;
        if (!this.isLoaded) {
            this.scheduledWatering++;
            return;
        }
        BlockState farm = level.getBlockState(this.pos);
        if (farm.getValue(FarmBlock.MOISTURE) < 7)
            level.setBlock(this.pos, farm.setValue(FarmBlock.MOISTURE, 7), Block.UPDATE_ALL);
    }

    /**
     * Handles the farmland when its storming. Destroying crops etc.
     */
    public void onStorming(ServerLevel level, int currentDay) {
        if (!this.isFarmBlock || this.lastWeatherDay == currentDay)
            return;
        if (!this.isLoaded) {
            this.onWatering(level, currentDay);
            this.scheduledStormTicks++;
            return;
        }
        this.onWatering(level, currentDay);
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
                    if (state.getBlock() instanceof Growable) {
                        if (level.random.nextFloat() < 0.6f) {
                            state = Blocks.AIR.defaultBlockState();
                            level.destroyBlock(cropPos, true);
                        } else
                            state = ModBlocks.WITHERED_GRASS.get().defaultBlockState();
                    } else {
                        state = Blocks.DIRT.defaultBlockState();
                        break;
                    }
                }
            }
        }
        FarmlandHandler.get(level.getServer()).scheduleUpdate(level, this);
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
        if (!this.isLoaded && !GeneralConfig.tickUnloadedFarmland)
            return;
        //If its not a farm block can do stuff without it being loaded
        if (!this.isFarmBlock) {
            this.lastUpdateDay = WorldUtils.day(level);
            this.handleNotFarmblock(level);
            return;
        }
        WorldHandler handler = WorldHandler.get(level.getServer());
        ExternalModifiers ext = new ExternalModifiers(handler.currentSeason(), handler.currentWeather());
        if (!this.isLoaded) {
            this.scheduledData.add(ext);
            return;
        }
        this.lastUpdateDay = WorldUtils.day(level);
        BlockState farm = level.getBlockState(this.pos);
        this.isFarmBlock = FarmlandHandler.isFarmBlock(farm);
        boolean isWet = this.isFarmBlock && farm.getValue(FarmBlock.MOISTURE) > 0;
        boolean canRainAt = FarmlandHandler.canRainingAt(level, this.pos);
        if (!onLoad) {
            this.scheduledData.add(ext);
        } else {//On load perform the missed storms
            this.doStormingLogic(level, this.scheduledStormTicks);
            if (!this.isFarmBlock) { //If storm turned it into dirt
                this.handleNotFarmblock(level);
                return;
            }
            if (!isWet && canRainAt && this.scheduledWatering > 0 && farm.getBlock() instanceof FarmBlock) {
                this.scheduledWatering--;
                isWet = true;
            }
        }
        boolean ignoreWater = FarmlandHandler.get(level.getServer()).hasWater(level, this.pos);
        if (ignoreWater)
            isWet = true;
        BlockPos cropPos = this.pos.above();
        BlockState cropState = level.getBlockState(cropPos);

        //Do the stuff like block update etc. after the loop
        List<Runnable> run = new ArrayList<>();
        if (isWet)
            run.add(() -> level.setBlock(this.pos, farm.setValue(FarmBlock.MOISTURE, 0), Block.UPDATE_ALL));

        boolean growHerb = false;
        boolean cropRecalc = false;
        CropProperties props = cropState.getBlock() instanceof Growable ? DataPackHandler.INSTANCE.cropManager().get(cropState.getBlock().getCloneItemStack(level, this.pos, cropState).getItem()) : null;

        int wiltStage = 0;
        for (ExternalModifiers modifiers : this.scheduledData) {
            //Regen stats if unused
            if (!(cropState.getBlock() instanceof Growable crop)) {
                this.normalizeLand();
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
            boolean hasGiantVersion = props != null && props.getGiantVersion() != Blocks.AIR && !cropState.is(props.getGiantVersion());

            //Dont do stuff if crop is fully grown.
            //No withering unlike game (for e.g. building purposes)
            if (crop.isAtMaxAge(cropState) && (!hasGiantVersion || this.size == 0 || (this.size < 0 && this.cropSize <= 0))) {
                break;
            }
            //Handle crop growth
            boolean didCropGrow = false;
            boolean maxAgeStop = false;
            if (props != null) {
                if (!cropRecalc) {
                    cropRecalc = true;
                    if (crop.canGrow(level, cropPos, cropState)) {
                        run.add(() -> {
                            int maxAge = crop.getGrowableMaxAge();
                            int stage = Mth.floor(this.cropAge * maxAge) / props.growth();
                            //Update the blockstate according to the growth age
                            BlockState newState = crop.getGrowableStateForAge(cropState, Math.min(stage, maxAge));
                            if (newState.getBlock() instanceof Growable newGrowable)
                                newGrowable.onGrow(level, cropPos, newState, cropState);
                            else
                                level.setBlock(cropPos, newState, Block.UPDATE_ALL);
                            Platform.INSTANCE.cropGrowEvent(level, cropPos, level.getBlockState(cropPos));
                        });
                    } else {
                        run.add(() -> CropUtils.attemptGiantize(level, cropPos, crop, cropState, this.cropSize, props));
                    }
                }
                if (!isWet) {
                    if (level.random.nextFloat() < GeneralConfig.witherChance) {
                        wiltStage++;
                        // If crop cannot wilt we simply stop once we reach >= 2 (normally wilted)
                        // E.g. in case of vanilla crops etc.
                        if (!cropState.hasProperty(BlockCrop.WILTED) && wiltStage > 1)
                            break;
                    }
                }
                float season = props.seasonMultiplier(modifiers.season);
                float runeyBonus = modifiers.weather == EnumWeather.RUNEY ? 5 : 1;
                float speed = this.growth * season * runeyBonus;
                if (!isWet)
                    speed *= 0.5f;
                this.cropAge += Math.min(props.growth(), speed);
                this.cropLevel += this.quality * (level.getRandom().nextFloat() * 0.5 + 0.5);
                if (crop.isAtMaxAge(cropState) && hasGiantVersion) {
                    if (this.size != 0) {
                        this.cropSize += this.size * (level.getRandom().nextFloat() * 0.2 + 0.1);
                        didCropGrow = this.size > 0 ? this.cropSize < 1 : this.cropSize > 0;
                    }
                } else {
                    didCropGrow = true;
                }
                if (!didCropGrow)
                    maxAgeStop = true;
            }
            if (!ignoreWater && canRainAt)
                isWet = this.scheduledWatering > 0;
            this.scheduledWatering = Math.max(0, --this.scheduledWatering);
            if (didCropGrow) {
                if (level.random.nextInt(3) != 0)
                    this.modifyHealth(null, -1);
                if (level.random.nextBoolean())
                    this.applyGrowthFertilizer(null, this.growth > 1 ? -0.1f : -0.05f);
                if (level.random.nextBoolean())
                    this.modifyQuality(null, -0.05f);
            } else {
                this.normalizeLand();
            }
            if (maxAgeStop)
                break;
        }
        this.resetScheduledData();

        // Finalize the tick run
        run.forEach(Runnable::run);
        if (wiltStage > 0 && cropState.getBlock() instanceof BlockCrop blockCrop) {
            blockCrop.onWither(wiltStage, level, cropState, cropPos);
        }
        this.cropProgress = this.growthPercent(level, cropState);
        FarmlandHandler.get(level.getServer()).scheduleUpdate(level, this);
    }

    private void handleNotFarmblock(ServerLevel level) {
        this.normalizeLand();
        this.resetCrop();
        FarmlandHandler.get(level.getServer()).scheduleUpdate(level, this);
        this.resetScheduledData();
    }

    private void resetScheduledData() {
        this.scheduledData.clear();
        this.scheduledWatering = 0;
        this.scheduledStormTicks = 0;
    }

    private void normalizeLand() {
        this.growth = this.growth > DEFAULT_SPEED ? Math.max(this.growth - 0.1F, DEFAULT_SPEED) : Math.min(this.growth + 0.1F, DEFAULT_SPEED);
        this.modifyQuality(null, -0.1f);
        this.size = Mth.clamp(this.size - 0.05f, 0, 2);
        this.health = this.health > DEFAULT_HEALTH ? Math.max(this.health - 1, DEFAULT_HEALTH) : Math.min(this.health + 1, DEFAULT_HEALTH);
    }

    private void resetCrop() {
        this.cropAge = 0;
        this.cropLevel = 1;
        this.cropSize = 0;
        this.cropProgress = 0;
    }

    public void onLoad(ServerLevel level, boolean tick) {
        if (tick && this.lastUpdateDay != WorldUtils.day(level)) {
            level.getServer().tell(new TickTask(1, () -> this.tick(level, true)));
        }
        this.isLoaded = true;
    }

    public void onUnload(ServerLevel level) {
        this.isLoaded = false;
        FarmlandHandler.get(level.getServer()).scheduleUpdate(level, this);
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
        this.cropProgress = nbt.getInt("CropProgress");

        this.lastUpdateDay = nbt.getInt("LastUpdate");
        this.lastWeatherDay = nbt.getInt("LastWeatherDay");

        this.scheduledStormTicks = nbt.getInt("ScheduledStormTicks");
        this.scheduledWatering = nbt.getInt("ScheduledWaterAmount");
        ListTag scheduledDataTag = nbt.getList("ScheduledData", Tag.TAG_COMPOUND);
        scheduledDataTag.forEach(t -> {
            CompoundTag lT = (CompoundTag) t;
            this.scheduledData.add(new ExternalModifiers(EnumSeason.valueOf(lT.getString("Season")), EnumWeather.valueOf(lT.getString("Weather"))));
        });

        this.isLoaded = nbt.getBoolean("IsLoaded");
        this.isFarmBlock = nbt.getBoolean("IsFarmBlock");
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
        nbt.putInt("CropProgress", this.cropProgress);

        nbt.putInt("LastUpdate", this.lastUpdateDay);
        nbt.putInt("LastWeatherDay", this.lastWeatherDay);

        nbt.putInt("ScheduledStormTicks", this.scheduledStormTicks);
        nbt.putInt("ScheduledWaterAmount", this.scheduledWatering);
        ListTag scheduledDataTag = new ListTag();
        this.scheduledData.forEach(mod -> {
            CompoundTag lT = new CompoundTag();
            lT.putString("Season", mod.season.name());
            lT.putString("Weather", mod.weather.name());
            scheduledDataTag.add(lT);
        });
        nbt.put("ScheduledData", scheduledDataTag);

        nbt.putBoolean("IsLoaded", this.isLoaded);
        nbt.putBoolean("IsFarmBlock", this.isFarmBlock);
        return nbt;
    }

    /**
     * Needs to match {@link FarmlandDataContainer#fromBuffer}
     */
    public void writeToBuffer(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeFloat(this.growth);
        buf.writeFloat(this.quality);
        buf.writeFloat(this.size);
        buf.writeInt(this.health);
        buf.writeInt(this.defence);
        buf.writeInt(this.cropProgress);
        buf.writeInt(Math.min(100, (int) this.cropSize * 100));
        buf.writeFloat(this.cropLevel);
    }

    @Override
    public String toString() {
        return String.format("Farmland[%s, Loaded:%s, IsFarm:%s]",
                this.pos, this.isLoaded, this.isFarmBlock);
    }

    public String toStringFull() {
        return String.format("Farmland[%s, Loaded:%s, IsFarm:%s]: Growth:%s;Quality:%s;Size:%s;Health:%s;Defence:%s - Schedules:[StormTicks:%s;Watering:%s], Data: %s",
                this.pos, this.isLoaded, this.isFarmBlock, this.growth, this.quality, this.size, this.health, this.defence,
                this.scheduledStormTicks, this.scheduledWatering, this.scheduledData);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj instanceof FarmlandData data)
            return data.pos.equals(this.pos);
        return false;
    }

    @Override
    public int hashCode() {
        return this.pos.hashCode();
    }

    protected record ExternalModifiers(EnumSeason season, EnumWeather weather) {
    }
}