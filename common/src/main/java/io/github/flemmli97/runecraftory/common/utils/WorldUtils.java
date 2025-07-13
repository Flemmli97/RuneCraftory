package io.github.flemmli97.runecraftory.common.utils;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.calendar.Season;
import io.github.flemmli97.runecraftory.common.world.data.RunecraftorySavedData;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityInLevelCallback;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Supplier;

public class WorldUtils {

    public static final TicketType<ChunkPos> ENTITY_LOADER = TicketType.create(RuneCraftory.MODID + "_entity_loader", Comparator.comparingLong(ChunkPos::toLong), 10);

    public static final Codec<Pair<Season, Integer>> DATE = RecordCodecBuilder.create(inst ->
            inst.group(
                    CodecUtils.stringEnumCodec(Season.class, null).fieldOf("season").forGetter(Pair::getFirst),
                    dayRange().fieldOf("day").forGetter(Pair::getSecond)
            ).apply(inst, Pair::of));

    private static Codec<Integer> dayRange() {
        Function<Integer, DataResult<Integer>> function = i -> {
            if (i >= 1 && i <= 30) {
                return DataResult.success(i);
            }
            return DataResult.error(() -> "Date must be between 1 - 30 but is " + i);
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

    public static <T extends Mob> EntityInLevelCallback wrappedCallbackFor(T member, Supplier<Player> partyOwner, EntityInLevelCallback callback) {
        return new EntityInLevelCallback() {
            @Override
            public void onMove() {
                callback.onMove();
            }

            @Override
            public void onRemove(Entity.RemovalReason reason) {
                if (member.level() instanceof ServerLevel serverLevel) {
                    if (reason == Entity.RemovalReason.UNLOADED_TO_CHUNK) {
                        RunecraftorySavedData.get(serverLevel.getServer()).safeUnloadedPartyMembers(member);
                    } else if (reason == Entity.RemovalReason.DISCARDED || reason == Entity.RemovalReason.KILLED) {
                        Player owner = partyOwner.get();
                        if (owner instanceof ServerPlayer player) {
                            Platform.INSTANCE.getPlayerData(player).party.removePartyMember(member);
                        } else
                            RunecraftorySavedData.get(serverLevel.getServer()).toRemovePartyMember(member);
                    }
                }
                callback.onRemove(reason);
            }
        };
    }
}
