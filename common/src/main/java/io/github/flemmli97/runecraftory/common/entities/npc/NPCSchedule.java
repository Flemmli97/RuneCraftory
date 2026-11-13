package io.github.flemmli97.runecraftory.common.entities.npc;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.calendar.DayOfWeek;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryActivities;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import io.github.flemmli97.runecraftory.common.world.data.Calendar;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.schedule.Activity;
import net.tslat.smartbrainlib.api.core.schedule.SmartBrainSchedule;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

public class NPCSchedule {

    private final NPCEntity npc;

    private Schedule schedule;

    private List<Pair<Component, List<Component>>> view;

    private ActivityTime currentActivity = ActivityTime.IDLE;
    private final Random random = new Random();
    private long randomSeed;
    private boolean wantsToSocialize;

    public NPCSchedule(NPCEntity npc, Schedule schedule) {
        this.npc = npc;
        this.schedule = schedule;
    }

    private static List<TimeSpan> getWorkTime(List<ActivityTime> timeline) {
        List<TimeSpan> workTimes = new ArrayList<>();
        for (int i = 0; i < timeline.size(); i++) {
            ActivityTime current = timeline.get(i);
            if (current.activity() == Activity.WORK) {
                ActivityTime next = timeline.get(++i);
                while (i < timeline.size() - 1) {
                    ActivityTime pointer = timeline.get(i + 1);
                    if (pointer.start() == next.start() || next.activity() == Activity.WORK) {
                        i++;
                        next = pointer;
                    } else {
                        break;
                    }
                }
                int end = next.start();
                workTimes.add(new TimeSpan(current.start(), end));
            }
        }
        return workTimes;
    }

    public NPCSchedule(NPCEntity npc, RandomSource random) {
        this(npc, new Schedule(random));
    }

    public static WrappedSchedule forBrain(Supplier<NPCSchedule> sup) {
        return new WrappedSchedule(sup);
    }

    public Activity getActivity(ServerLevel level) {
        ActivityTime current = this.currentActivity;
        this.currentActivity = this.getActivityNow(level);
        if (current != this.currentActivity) {
            this.randomSeed = this.npc.getRandom().nextLong();
            float chance = this.currentActivity.socialChance().orElse(1f);
            this.wantsToSocialize = this.getScheduleRandom().nextFloat() < chance;
        }
        return this.currentActivity();
    }

    private ActivityTime getActivityNow(ServerLevel level) {
        if (!this.npc.getProfession().hasSchedule)
            return ActivityTime.IDLE;
        int dayTime = WorldUtils.dayTime(level);
        DayOfWeek day = Calendar.get(level).date().day();
        List<ActivityTime> activities = this.schedule.timelines.get(day);
        ActivityTime activity = ActivityTime.REST;
        for (ActivityTime time : activities) {
            if (time.start() > dayTime) {
                break;
            }
            activity = time;
        }
        if (this.npc.isBaby()) {
            if (activity.activity() == RuneCraftoryActivities.EARLY_IDLE.get()) {
                return ActivityTime.REST;
            }
            if (activity.activity() == Activity.WORK) {
                return ActivityTime.IDLE;
            }
        }
        return activity;
    }

    public Activity currentActivity() {
        return this.currentActivity.activity();
    }

    public ActivityTime getCurrentScheduleActivity() {
        return this.currentActivity;
    }

    public boolean poiMatchesActivity(Holder<PoiType> poi) {
        ActivityTime schedule = this.getCurrentScheduleActivity();
        if (schedule.activity() == Activity.MEET) {
            return schedule.poi().map(poi::is).orElseGet(() -> poi.is(PoiTypes.MEETING));
        }
        return false;
    }

    public long getScheduleRandomSeed() {
        return this.randomSeed;
    }

    public Random getScheduleRandom() {
        this.random.setSeed(this.getScheduleRandomSeed());
        return this.random;
    }

    public boolean wantsToSocialize() {
        return this.wantsToSocialize;
    }

    public CompoundTag save() {
        return (CompoundTag) Schedule.CODEC.encodeStart(NbtOps.INSTANCE, this.schedule).getOrThrow();
    }

    public void load(CompoundTag tag) {
        this.schedule = Schedule.CODEC.parse(NbtOps.INSTANCE, tag)
                .resultOrPartial(s -> RuneCraftory.LOGGER.error("Couldn't load schedule for {}. {}", this.npc, s))
                .orElseGet(() -> new Schedule(this.npc.getRandom()));
        this.view = null;
    }

    public void with(Schedule schedule) {
        this.schedule = schedule;
        this.view = null;
    }

    public List<Pair<Component, List<Component>>> viewSchedule() {
        if (!this.npc.getProfession().hasSchedule || !this.npc.getProfession().hasWorkSchedule) {
            return List.of();
        }
        if (this.view == null) {
            List<Pair<DayRange, List<TimeSpan>>> workTimes = new ArrayList<>();
            for (DayOfWeek day : DayOfWeek.values()) {
                List<TimeSpan> timeSpans = getWorkTime(this.schedule.timelines.get(day));
                Pair<DayRange, List<TimeSpan>> existing = workTimes.isEmpty() ? null : workTimes.getLast();
                if (existing != null && existing.getSecond().equals(timeSpans)) {
                    existing.getFirst().setEnd(day);
                } else {
                    workTimes.add(new Pair<>(new DayRange(day), timeSpans));
                }
            }
            List<Pair<Component, List<Component>>> display = new ArrayList<>();
            display.add(Pair.of(Component.translatable("runecraftory.npc.schedule.days.header"), List.of()));
            for (Pair<DayRange, List<TimeSpan>> entry : workTimes) {
                if (entry.getSecond().isEmpty())
                    continue;
                Component day;
                if (entry.getFirst().start() == DayOfWeek.MONDAY && entry.getFirst().end == DayOfWeek.SUNDAY) {
                    day = Component.translatable("runecraftory.npc.schedule.days.all");
                } else if (entry.getFirst().start() == DayOfWeek.MONDAY && entry.getFirst().end == DayOfWeek.FRIDAY) {
                    day = Component.translatable("runecraftory.npc.schedule.days.weekdays");
                } else if (entry.getFirst().start() == DayOfWeek.SATURDAY && entry.getFirst().end == DayOfWeek.SUNDAY) {
                    day = Component.translatable("runecraftory.npc.schedule.days.weekends");
                } else if (entry.getFirst().start() == entry.getFirst().end()) {
                    day = Component.translatable("runecraftory.npc.schedule.day", Component.translatable(entry.getFirst().start().translationFull()));
                } else {
                    day = Component.translatable("runecraftory.npc.schedule.days.span", Component.translatable(entry.getFirst().start().translationFull()), Component.translatable(entry.getFirst().end().translationFull()));
                }
                List<Component> times = new ArrayList<>();
                for (int i = 0; i < entry.getSecond().size(); ++i) {
                    TimeSpan timeSpan = entry.getSecond().get(i);
                    times.add(Component.translatable("runecraftory.npc.schedule.work", formatTime(timeSpan.start()), formatTime(timeSpan.end())));
                }
                display.add(new Pair<>(day, times));
            }
            this.view = ImmutableList.copyOf(display);
        }
        return this.view;
    }

    private static String formatTime(int timeInTicks) {
        int hour = ((timeInTicks + 6000) % 24000) / 1000;
        int min = (int) (timeInTicks % 1000 * 0.06);
        String minS = (min < 10 ? "0" : "") + min;
        return String.format("%s:%s", hour, minS);
    }

    public static class Schedule {

        public static final Codec<Schedule> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(Codec.unboundedMap(CodecUtils.stringEnumCodec(ScheduleDays.class, null), ActivityTime.CODEC.listOf())
                        .fieldOf("timelines").forGetter(d -> d.data)
                ).apply(inst, Schedule::new)
        );

        private final Map<ScheduleDays, List<ActivityTime>> data;
        private final EnumMap<DayOfWeek, List<ActivityTime>> timelines;

        public Schedule(RandomSource random) {
            this(randomizedTime(random, 6, 9),

                    randomizedTime(random, 8, 10),
                    randomizedTime(random, 11, 13),
                    randomizedTime(random, 12, 15),
                    randomizedTime(random, 16, 18),

                    randomizedTime(random, 20, 22),

                    randomizedTime(random, 9, 12),
                    randomizedTime(random, 13, 15),
                    randomizedWorkDays(random));
        }

        private Schedule(int wakeUpTime, int workTime, int breakTime, int workTimeAfter, int doneWorkTime, int sleepTime, int meetTimeOffday, int meetTimeAfterOffday, List<DayOfWeek> workDays) {
            this(of(List.of(new ActivityTime(wakeUpTime, RuneCraftoryActivities.EARLY_IDLE.get()),

                            new ActivityTime(Math.max(wakeUpTime + 500, workTime), Activity.WORK),
                            new ActivityTime(breakTime, Activity.MEET),
                            new ActivityTime(Math.max(breakTime, workTimeAfter), Activity.WORK),
                            new ActivityTime(doneWorkTime, Activity.IDLE),

                            new ActivityTime(sleepTime, Activity.REST)),
                    List.of(new ActivityTime(wakeUpTime, RuneCraftoryActivities.EARLY_IDLE.get()),

                            new ActivityTime(meetTimeOffday, Activity.MEET),
                            new ActivityTime(meetTimeAfterOffday, Activity.IDLE),

                            new ActivityTime(sleepTime, Activity.REST)),
                    workDays));
        }

        public Schedule(Map<ScheduleDays, List<ActivityTime>> data) {
            this.data = data;
            EnumMap<DayOfWeek, List<ActivityTime>> timelines = new EnumMap<>(DayOfWeek.class);
            for (Map.Entry<ScheduleDays, List<ActivityTime>> entry : data.entrySet()) {
                List<ActivityTime> timeline = sanitize(entry.getValue());
                for (DayOfWeek day : entry.getKey().days) {
                    timelines.put(day, timeline);
                }
            }
            List<DayOfWeek> missing = new ArrayList<>();
            for (DayOfWeek day : DayOfWeek.values()) {
                if (!timelines.containsKey(day)) {
                    missing.add(day);
                }
            }
            if (!missing.isEmpty()) {
                throw new IllegalStateException("Missing days in schedule" + missing);
            }
            this.timelines = timelines;
        }

        private static Map<ScheduleDays, List<ActivityTime>> of(List<ActivityTime> workdaysTime, List<ActivityTime> offdays, List<DayOfWeek> workdays) {
            Map<ScheduleDays, List<ActivityTime>> workTime = new LinkedHashMap<>();
            for (DayOfWeek day : DayOfWeek.values()) {
                if (workdays.contains(day)) {
                    workTime.put(ScheduleDays.of(day), workdaysTime);
                } else {
                    workTime.put(ScheduleDays.of(day), offdays);
                }
            }
            return workTime;
        }

        private static List<ActivityTime> sanitize(List<ActivityTime> times) {
            times = new ArrayList<>(times);
            times.sort(Comparator.naturalOrder());
            for (int i = 1; i < times.size(); i++) {
                ActivityTime prev = times.get(i - 1);
                ActivityTime current = times.get(i);
                times.set(i, new ActivityTime(Math.max(prev.start(), current.start()), current.activity()));
            }
            return List.copyOf(times);
        }

        private static int randomizedTime(RandomSource random, int min, int max) {
            float hourAdd = random.nextInt((max - min) * 2) * 0.5f;
            float hour = (min + hourAdd - 6); //-6 cause 0 daytime = 6:00
            return (int) (hour * 10) * 100;
        }

        private static List<DayOfWeek> randomizedWorkDays(RandomSource random) {
            List<DayOfWeek> days = new ArrayList<>();
            for (DayOfWeek day : DayOfWeek.values()) {
                if (day == DayOfWeek.SATURDAY) {
                    if (random.nextFloat() < 0.4f)
                        days.add(day);
                } else if (day == DayOfWeek.SUNDAY) {
                    if (random.nextFloat() < 0.15f)
                        days.add(day);
                } else if (random.nextFloat() < 0.85f)
                    days.add(day);
            }
            return days;
        }
    }

    public record ActivityTime(int start, Activity activity, Optional<Float> socialChance,
                               Optional<ResourceKey<PoiType>> poi) implements Comparable<ActivityTime> {

        public static final Codec<ActivityTime> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(ExtraCodecs.NON_NEGATIVE_INT.fieldOf("time").forGetter(ActivityTime::start),
                        BuiltInRegistries.ACTIVITY.byNameCodec().fieldOf("activity").forGetter(ActivityTime::activity),
                        Codec.FLOAT.optionalFieldOf("social_chance").forGetter(ActivityTime::socialChance),
                        ResourceKey.codec(Registries.POINT_OF_INTEREST_TYPE).optionalFieldOf("poi").forGetter(ActivityTime::poi)
                ).apply(inst, ActivityTime::new)
        );

        public static ActivityTime IDLE = new ActivityTime(-1, Activity.IDLE);
        public static ActivityTime REST = new ActivityTime(-1, Activity.REST);

        public ActivityTime(int start, Activity activity) {
            this(start, activity, Optional.empty(), Optional.empty());
        }

        public ActivityTime(int start, Activity activity, ResourceKey<PoiType> poi) {
            this(start, activity, Optional.empty(), Optional.of(poi));
        }

        @Override
        public int compareTo(@NotNull NPCSchedule.ActivityTime o) {
            return Integer.compare(this.start, o.start);
        }
    }

    private record TimeSpan(int start, int end) {
    }

    private static class DayRange {

        private final DayOfWeek start;
        private DayOfWeek end;

        private DayRange(DayOfWeek start) {
            this.start = start;
            this.end = start;
        }

        public void setEnd(DayOfWeek end) {
            this.end = end;
        }

        public DayOfWeek start() {
            return this.start;
        }

        public DayOfWeek end() {
            return this.end;
        }

        @Override
        public String toString() {
            return String.format("[%s, %s]", this.start, this.end);
        }
    }

    public static class WrappedSchedule extends SmartBrainSchedule {

        private final Supplier<NPCSchedule> schedule;

        public WrappedSchedule(Supplier<NPCSchedule> schedule) {
            this.schedule = schedule;
        }

        @Override
        public Activity tick(LivingEntity brainOwner) {
            return this.schedule.get().getActivity((ServerLevel) brainOwner.level());
        }
    }

    public enum ScheduleDays {

        MONDAY(DayOfWeek.MONDAY),
        TUESDAY(DayOfWeek.TUESDAY),
        WEDNESDAY(DayOfWeek.WEDNESDAY),
        THURSDAY(DayOfWeek.THURSDAY),
        FRIDAY(DayOfWeek.FRIDAY),
        SATURDAY(DayOfWeek.SATURDAY),
        SUNDAY(DayOfWeek.SUNDAY),
        WEEKDAYS(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY),
        WEEKEND(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

        private final DayOfWeek[] days;

        ScheduleDays(DayOfWeek... days) {
            this.days = days;
        }

        static ScheduleDays of(DayOfWeek day) {
            return switch (day) {
                case MONDAY -> MONDAY;
                case TUESDAY -> TUESDAY;
                case WEDNESDAY -> WEDNESDAY;
                case THURSDAY -> THURSDAY;
                case FRIDAY -> FRIDAY;
                case SATURDAY -> SATURDAY;
                case SUNDAY -> SUNDAY;
            };
        }
    }
}
