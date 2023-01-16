package io.github.flemmli97.runecraftory.common.world;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.IDailyUpdate;
import io.github.flemmli97.runecraftory.api.enums.EnumDay;
import io.github.flemmli97.runecraftory.api.enums.EnumSeason;
import io.github.flemmli97.runecraftory.api.enums.EnumWeather;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.network.S2CCalendar;
import io.github.flemmli97.runecraftory.common.utils.CalendarImpl;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import io.github.flemmli97.runecraftory.platform.Platform;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class WorldHandler extends SavedData {

    private static final String id = "RCCalendar";

    private final CalendarImpl calendar = new CalendarImpl();

    /**
     * Non persistent tracker to update stuff daily
     */
    private final Set<IDailyUpdate> updateTracker = Sets.newConcurrentHashSet();
    private final Map<UUID, Set<BarnData>> playerBarns = new HashMap<>();
    private final Map<ResourceKey<Level>, Long2ObjectMap<BarnData>> positionBarnMap = new HashMap<>();

    private final Map<UUID, Set<Pair<UUID, GlobalPos>>> unloadedPartyMembers = new HashMap<>();
    private final Map<UUID, Set<UUID>> toRemovePartyMembers = new HashMap<>();

    private int updateDelay, lastUpdateDay;

    public WorldHandler() {
    }

    private WorldHandler(CompoundTag tag) {
        this.load(tag);
    }

    public static WorldHandler get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(WorldHandler::new, WorldHandler::new, id);
    }

    public static boolean canUpdateWeather(Level level) {
        return GeneralConfig.modifyWeather && level.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT);
    }

    public static boolean shouldUpdateWeather(Level level, EnumWeather currentWeather) {
        if (currentWeather == EnumWeather.RUNEY || currentWeather == EnumWeather.STORM)
            return WorldUtils.dayTime(level) == 1;
        long time = WorldUtils.dayTime(level);
        return (time % 3000) == 1;
    }

    public CalendarImpl getCalendar() {
        return this.calendar;
    }

    public void setDateDayAndSeason(MinecraftServer server, int date, EnumDay day, EnumSeason season) {
        this.calendar.setDateDayAndSeason(date, day, season);
        Platform.INSTANCE.sendToAll(new S2CCalendar(this.calendar), server);
        this.setDirty();
    }

    public void increaseDay(ServerLevel level) {
        int date = WorldUtils.day(level);
        EnumDay day = EnumDay.values()[Math.floorMod(date, EnumDay.values().length)];
        EnumSeason season = EnumSeason.values()[Math.floorMod(date / 30, EnumSeason.values().length)];
        this.calendar.setDateDayAndSeason(date % 30 + 1, day, season);
        Platform.INSTANCE.sendToAll(new S2CCalendar(this.calendar), level.getServer());
        this.setDirty();
    }

    public EnumSeason currentSeason() {
        return this.calendar.currentSeason();
    }

    public int date() {
        return this.calendar.date();
    }

    public EnumDay currentDay() {
        return this.calendar.currentDay();
    }

    public EnumWeather currentWeather() {
        return this.calendar.currentWeather();
    }

    public EnumWeather[] tomorrowsWeather() {
        return this.calendar.tomorrowsForecast();
    }

    public void update(ServerLevel level) {
        boolean doWeather = canUpdateWeather(level);
        if (WorldUtils.canUpdateDaily(level, this.lastUpdateDay)) {
            this.increaseDay(level);
            this.updateTracker.removeIf(IDailyUpdate::inValid);
            this.updateTracker.forEach(update -> update.update(level));
            this.createDailyWeather(level);
            if (doWeather)
                this.updateWeatherTo(level, this.calendar.getCurrentWeatherFor(level));
            this.lastUpdateDay = WorldUtils.day(level);
            return;
        }
        if (doWeather && shouldUpdateWeather(level, this.currentWeather())) {
            this.updateWeatherTo(level, this.calendar.getCurrentWeatherFor(level));
        }
        // Checks if current weather is correct and if not corrects it
        /*
        if (--this.updateDelay <= 0) {
            if (doWeather && !this.isCorrectWeather(level))
                this.setMCWeather(level);
            this.updateDelay = 40;
        }*/
    }

    public void updateWeatherTo(ServerLevel level, EnumWeather weather) {
        this.calendar.setWeather(level.getServer(), weather);
        this.setMCWeather(level);
        this.updateDelay = 100;
        this.setDirty();
    }

    private void createDailyWeather(ServerLevel level) {
        EnumWeather[] nextWeather = new EnumWeather[8];
        EnumSeason season = this.currentSeason();
        int rainCount = 0;
        for (int i = 0; i < nextWeather.length; i++) {
            float chance = level.random.nextFloat();
            if (i != 0) {
                if (nextWeather[0].wholeDay) {
                    nextWeather[i] = nextWeather[0];
                    return;
                }
            } else {
                float stormAdd = (season == EnumSeason.SUMMER || season == EnumSeason.WINTER) ? 0.04F : 0;
                if (chance < 0.03F)
                    nextWeather[i] = EnumWeather.RUNEY;
                else if (chance < 0.015F + stormAdd)
                    nextWeather[i] = EnumWeather.STORM;
                if (nextWeather[i] != null)
                    return;
            }
            float rainAdd = rainCount > 0 ? 0.5f - (rainCount - 1) * 0.2f : 0;
            if (i < 3)
                rainAdd += season == EnumSeason.SUMMER ? 0.1 : 0.05;
            if (chance < 0.1F + rainAdd) {
                nextWeather[i] = EnumWeather.RAIN;
                rainCount++;
            } else
                nextWeather[i] = EnumWeather.CLEAR;
        }
        this.calendar.updateWeathers(nextWeather);
    }

    private void setMCWeather(ServerLevel level) {
        this.currentWeather().setWeather.accept(level);
    }

    private boolean isCorrectWeather(ServerLevel level) {
        return switch (this.currentWeather()) {
            case RAIN -> level.isRaining();
            case CLEAR, RUNEY, CLOUDY -> !level.isRaining() && !level.isThundering();
            case STORM -> level.isRaining() && level.isThundering();
        };
    }

    public void addToTracker(IDailyUpdate update) {
        this.updateTracker.add(update);
    }

    public boolean removeFromTracker(IDailyUpdate update) {
        return this.updateTracker.remove(update);
    }

    //========BARNS
    public BarnData getOrCreateFor(UUID player, Level level, BlockPos pos) {
        BarnData data = this.positionBarnMap.computeIfAbsent(level.dimension(), k -> new Long2ObjectOpenHashMap<>())
                .computeIfAbsent(pos.asLong(), l -> new BarnData(GlobalPos.of(level.dimension(), pos)));
        this.playerBarns.computeIfAbsent(player, uuid -> new HashSet<>())
                .add(data);
        this.setDirty();
        return data;
    }

    public Set<BarnData> barnsOf(UUID player) {
        return ImmutableSet.copyOf(this.playerBarns.getOrDefault(player, Set.of()));
    }

    @Nullable
    public BarnData barnAt(GlobalPos pos) {
        Long2ObjectMap<BarnData> map = this.positionBarnMap.get(pos.dimension());
        if (map != null)
            return map.get(pos.pos().asLong());
        return null;
    }

    @Nullable
    public BarnData findFittingBarn(BaseMonster monster, UUID owner) {
        return this.barnsOf(owner)
                .stream().filter(b -> b.hasCapacityFor(monster.getProp().getSize(), monster.getProp().needsRoof()))
                .findFirst().orElse(null);
    }

    @Nullable
    public BarnData findFittingBarn(BaseMonster monster) {
        if (monster.getOwnerUUID() == null)
            return null;
        return this.findFittingBarn(monster, monster.getOwnerUUID());
    }

    public void removeMonsterFromPlayer(UUID player, BaseMonster monster) {
        this.playerBarns.getOrDefault(player, Set.of())
                .forEach(b -> b.removeMonster(monster.getUUID()));
    }

    public void removeBarn(UUID player, GlobalPos pos) {
        Long2ObjectMap<BarnData> map = this.positionBarnMap.get(pos.dimension());
        if (map != null) {
            BarnData old = map.remove(pos.pos().asLong());
            this.playerBarns.computeIfAbsent(player, uuid -> new HashSet<>())
                    .remove(old);
            old.remove();
            this.setDirty();
        }
    }

    //===========

    public void safeUnloadedPartyMembers(LivingEntity entity) {
        if (entity instanceof BaseMonster monster && monster.getOwnerUUID() != null)
            this.unloadedPartyMembers.computeIfAbsent(monster.getOwnerUUID(), o -> new HashSet<>())
                    .add(Pair.of(entity.getUUID(), GlobalPos.of(entity.level.dimension(), entity.blockPosition())));
        else if (entity instanceof EntityNPCBase npc && npc.getEntityToFollowUUID() != null)
            this.unloadedPartyMembers.computeIfAbsent(npc.getEntityToFollowUUID(), o -> new HashSet<>())
                    .add(Pair.of(entity.getUUID(), GlobalPos.of(entity.level.dimension(), entity.blockPosition())));
    }

    public Set<Pair<UUID, GlobalPos>> getUnloadedPartyMembersFor(Player player) {
        return this.unloadedPartyMembers.computeIfAbsent(player.getUUID(), o -> new HashSet<>());
    }

    public void toRemovePartyMember(LivingEntity entity) {
        if (entity instanceof BaseMonster monster && monster.getOwnerUUID() != null)
            this.toRemovePartyMembers.computeIfAbsent(monster.getOwnerUUID(), o -> new HashSet<>())
                    .add(entity.getUUID());
        else if (entity instanceof EntityNPCBase npc && npc.getEntityToFollowUUID() != null)
            this.toRemovePartyMembers.computeIfAbsent(npc.getEntityToFollowUUID(), o -> new HashSet<>())
                    .add(entity.getUUID());
    }

    public Set<UUID> removedPartyMembersFor(Player player) {
        return this.toRemovePartyMembers.computeIfAbsent(player.getUUID(), o -> new HashSet<>());
    }

    public void load(CompoundTag compoundNBT) {
        this.calendar.read(compoundNBT);
        this.lastUpdateDay = compoundNBT.getInt("LastUpdateDay");
        CompoundTag barns = compoundNBT.getCompound("PlayerBarns");
        barns.getAllKeys().forEach(key -> {
            UUID uuid = UUID.fromString(key);
            ListTag list = barns.getList(key, Tag.TAG_COMPOUND);
            Set<BarnData> map = this.playerBarns.computeIfAbsent(uuid, u -> new HashSet<>());
            list.forEach(t -> {
                BarnData data = BarnData.fromTag((CompoundTag) t);
                map.add(data);
                this.positionBarnMap.computeIfAbsent(data.pos.dimension(), k -> new Long2ObjectOpenHashMap<>()).put(data.pos.pos().asLong(), data);
            });
        });
        CompoundTag unloadedParties = compoundNBT.getCompound("UnloadedParties");
        unloadedParties.getAllKeys().forEach(key -> {
            UUID uuid = UUID.fromString(key);
            ListTag list = unloadedParties.getList(key, Tag.TAG_COMPOUND);
            Set<Pair<UUID, GlobalPos>> map = this.unloadedPartyMembers.computeIfAbsent(uuid, u -> new HashSet<>());
            list.forEach(t -> {
                CompoundTag cTag = (CompoundTag) t;
                map.add(Pair.of(UUID.fromString(cTag.getString("UUID")), GlobalPos.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, cTag.get("Pos")))
                        .getOrThrow(false, RuneCraftory.logger::error)));
            });
        });
        CompoundTag removedPartyMembers = compoundNBT.getCompound("RemovedPartyMembers");
        removedPartyMembers.getAllKeys().forEach(key -> {
            UUID uuid = UUID.fromString(key);
            ListTag list = removedPartyMembers.getList(key, Tag.TAG_INT_ARRAY);
            Set<UUID> uuids = this.toRemovePartyMembers.computeIfAbsent(uuid, u -> new HashSet<>());
            list.forEach(t -> uuids.add(NbtUtils.loadUUID(t)));
        });
    }

    @Override
    public CompoundTag save(CompoundTag compoundNBT) {
        this.calendar.write(compoundNBT);
        compoundNBT.putInt("LastUpdateDay", this.lastUpdateDay);
        CompoundTag barns = new CompoundTag();
        this.playerBarns.forEach((uuid, pB) -> {
            ListTag pBTag = new ListTag();
            pB.forEach(b -> {
                if (!b.isInvalid())
                    pBTag.add(b.save());
            });
            barns.put(uuid.toString(), pBTag);
        });
        compoundNBT.put("PlayerBarns", barns);
        CompoundTag unloadedParties = new CompoundTag();
        this.unloadedPartyMembers.forEach((uuid, pairs) -> {
            if (!pairs.isEmpty()) {
                ListTag pTags = new ListTag();
                pairs.forEach(p -> {
                    CompoundTag pTag = new CompoundTag();
                    pTag.putString("UUID", p.getFirst().toString());
                    GlobalPos.CODEC.encodeStart(NbtOps.INSTANCE, p.getSecond()).resultOrPartial(RuneCraftory.logger::error)
                            .ifPresent(t -> pTag.put("Pos", t));
                    pTags.add(pTag);
                });
                unloadedParties.put(uuid.toString(), pTags);
            }
        });
        compoundNBT.put("UnloadedParties", unloadedParties);
        CompoundTag removedPartyMembers = new CompoundTag();
        this.toRemovePartyMembers.forEach((uuid, uuids) -> {
            if (!uuids.isEmpty()) {
                ListTag pTags = new ListTag();
                uuids.forEach(member -> pTags.add(NbtUtils.createUUID(member)));
                removedPartyMembers.put(uuid.toString(), pTags);
            }
        });
        compoundNBT.put("RemovedPartyMembers", removedPartyMembers);
        return compoundNBT;
    }
}
