package io.github.flemmli97.runecraftory.common.utils;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumSeason;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.world.WorldHandler;
import io.github.flemmli97.runecraftory.mixin.BiomeAccessor;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.entity.EntityInLevelCallback;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Supplier;

public class WorldUtils {

    public static final TicketType<ChunkPos> ENTITY_LOADER = TicketType.create(RuneCraftory.MODID + "_entity_loader", Comparator.comparingLong(ChunkPos::toLong), 100);

    public static final Codec<Pair<EnumSeason, Integer>> DATE = RecordCodecBuilder.create(inst ->
            inst.group(
                    CodecHelper.enumCodec(EnumSeason.class, null).fieldOf("season").forGetter(Pair::getFirst),
                    dayRange().fieldOf("day").forGetter(Pair::getSecond)
            ).apply(inst, Pair::of));

    private static Codec<Integer> dayRange() {
        Function<Integer, DataResult<Integer>> function = i -> {
            if (i >= 1 && i <= 30) {
                return DataResult.success(i);
            }
            return DataResult.error("Date must be between 1 - 30 but is " + i);
        };
        return Codec.INT.flatXmap(function, function);
    }

    public static boolean canUpdateDaily(Level level, int lastUpdate) {
        return level.getGameRules().getRule(GameRules.RULE_DAYLIGHT).get() && day(level) != lastUpdate;
    }

    public static int dayTime(Level level) {
        return (int) (level.getDayTime() % 24000);
    }

    public static int dayTimeTotal(Level level) {
        return (int) (level.getDayTime());
    }

    public static long totalTime(Level level) {
        return level.getGameTime();
    }

    public static int day(Level level) {
        return day(level, 0);
    }

    public static int day(Level world, int tickOffset) {
        return (int) ((world.getDayTime() + tickOffset) / 24000 % Integer.MAX_VALUE);
    }

    public static boolean canPlaceSnowAt(Level level, BlockPos pos) {
        return pos.getY() >= level.getMinBuildHeight() && pos.getY() < level.getMaxBuildHeight() && level.getBrightness(LightLayer.BLOCK, pos) < 10 && level.getBlockState(pos).isAir() && Blocks.SNOW.defaultBlockState().canSurvive(level, pos);
    }

    public static boolean coldEnoughForSnow(Level level, BlockPos pos, Biome biome) {
        if (biome.coldEnoughToSnow(pos)) {
            return false;
        }
        return seasonBasedTemp(level, pos, biome) < 0.15;
    }

    public static float seasonBasedTemp(Level level, BlockPos pos, Biome biome) {
        float temp = ((BiomeAccessor) (Object) biome).biomeTemp(pos);
        if (!GeneralConfig.seasonedSnow)
            return temp;
        EnumSeason season;
        if (level instanceof ServerLevel serverLevel)
            season = WorldHandler.get(serverLevel.getServer()).currentSeason();
        else
            season = ClientHandlers.clientCalendar.currentSeason();
        switch (season) {
            case SUMMER -> temp += 0.1f;
            case FALL -> temp -= 0.25f;
            case WINTER -> temp -= 0.8f;
        }
        return temp;
    }

    public static <T extends Mob> EntityInLevelCallback wrappedCallbackFor(T member, Supplier<Player> partyOwner, EntityInLevelCallback callback) {
        return new EntityInLevelCallback() {
            @Override
            public void onMove() {
                callback.onMove();
            }

            @Override
            public void onRemove(Entity.RemovalReason reason) {
                if (member.level instanceof ServerLevel serverLevel) {
                    if (reason == Entity.RemovalReason.UNLOADED_TO_CHUNK) {
                        WorldHandler.get(serverLevel.getServer()).safeUnloadedPartyMembers(member);
                    } else if (reason == Entity.RemovalReason.DISCARDED || reason == Entity.RemovalReason.KILLED) {
                        Player owner = partyOwner.get();
                        if (owner instanceof ServerPlayer player) {
                            Platform.INSTANCE.getPlayerData(player).ifPresent(d -> d.party.removePartyMember(member));
                        } else
                            WorldHandler.get(serverLevel.getServer()).toRemovePartyMember(member);
                    }
                }
                callback.onRemove(reason);
            }
        };
    }
}
