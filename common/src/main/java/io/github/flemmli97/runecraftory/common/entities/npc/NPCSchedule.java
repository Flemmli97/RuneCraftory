package io.github.flemmli97.runecraftory.common.entities.npc;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.calendar.DayOfWeek;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryActivities;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import io.github.flemmli97.runecraftory.common.world.data.Calendar;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.schedule.Activity;
import net.tslat.smartbrainlib.api.core.schedule.SmartBrainSchedule;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Supplier;

public class NPCSchedule {

    private final NPCEntity npc;

    private Schedule schedule;

    private List<Component> view;

    private Activity currentActivity = Activity.IDLE;

    public NPCSchedule(NPCEntity npc, Schedule schedule) {
        this.npc = npc;
        this.schedule = schedule;
    }

    public NPCSchedule(NPCEntity npc, RandomSource random) {
        this(npc, new Schedule(random));
    }

    public static WrappedSchedule forBrain(Supplier<NPCSchedule> sup) {
        return new WrappedSchedule(sup);
    }

    public Activity getActivity(ServerLevel level) {
        Activity activity = this.getActivityNow(level);
        this.currentActivity = activity;
        return activity;
    }

    private Activity getActivityNow(ServerLevel level) {
        if (!this.npc.getProfession().hasSchedule)
            return Activity.IDLE;
        int dayTime = WorldUtils.dayTime(level);
        DayOfWeek day = Calendar.get(level).date().day();
        List<ActivityTime> activities;
        if (!this.npc.isBaby() && this.schedule.workDays.contains(day) && this.npc.getProfession().hasWorkSchedule) {
            activities = this.schedule.workdayActivities;
        } else {
            activities = this.schedule.nonWorkDaysActivities;
        }
        Activity activity = Activity.REST;
        for (ActivityTime time : activities) {
            if (time.start() > dayTime) {
                break;
            }
            activity = time.activity();
        }
        return activity;
    }

    public Activity currentActivity() {
        return this.currentActivity;
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

    public List<Component> viewSchedule() {
        if (!this.npc.getProfession().hasSchedule || !this.npc.getProfession().hasWorkSchedule) {
            return List.of();
        }
        if (this.view == null) {
            List<Component> newList = new ArrayList<>();
            if (!this.schedule.workTimes.isEmpty()) {
                newList.add(Component.translatable("runecraftory.npc.schedule.work", this.formatTime(this.schedule.workTimes.getFirst().start()), this.formatTime(this.schedule.workTimes.getFirst().end())));
                for (int i = 1; i < this.schedule.workTimes.size(); ++i) {
                    TimeSpan timeline = this.schedule.workTimes.get(i);
                    newList.add(Component.translatable("runecraftory.npc.schedule.work.2", this.formatTime(timeline.start()), this.formatTime(timeline.end())));
                }
            }
            newList.add(Component.translatable("runecraftory.npc.schedule.days.header"));
            List<DayOfWeek> weekDayCounts = new ArrayList<>();
            for (DayOfWeek day : DayOfWeek.values()) {
                if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY)
                    continue;
                if (!this.schedule.workDays.contains(day))
                    weekDayCounts.add(day);
            }
            if (this.schedule.workDays.size() == DayOfWeek.values().length) {
                newList.add(Component.translatable("runecraftory.npc.schedule.days.all"));
            } else {
                switch (weekDayCounts.size()) {
                    case 0 -> newList.add(Component.translatable("runecraftory.npc.schedule.days.0"));
                    case 1 ->
                            newList.add(Component.translatable("runecraftory.npc.schedule.days.1", Component.translatable(weekDayCounts.get(0).translationFull())));
                    case 2 ->
                            newList.add(Component.translatable("runecraftory.npc.schedule.days.2", Component.translatable(weekDayCounts.get(0).translationFull()), Component.translatable(weekDayCounts.get(1).translationFull())));
                    default ->
                            newList.add(Component.translatable("runecraftory.npc.schedule.days.with", this.schedule.workDays.stream().filter(day -> day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY).map(e -> Component.translatable(e.translationFull())).toArray()));
                }
                if (this.schedule.workDays.contains(DayOfWeek.SATURDAY)) {
                    if (this.schedule.workDays.contains(DayOfWeek.SUNDAY))
                        newList.add(Component.translatable("runecraftory.npc.schedule.days.weekend.2", Component.translatable(DayOfWeek.SATURDAY.translationFull()), Component.translatable(DayOfWeek.SUNDAY.translationFull())));
                    else
                        newList.add(Component.translatable("runecraftory.npc.schedule.days.weekend.1", Component.translatable(DayOfWeek.SATURDAY.translationFull())));
                } else if (this.schedule.workDays.contains(DayOfWeek.SUNDAY))
                    newList.add(Component.translatable("runecraftory.npc.schedule.days.weekend.1", Component.translatable(DayOfWeek.SUNDAY.translationFull())));
            }
            this.view = ImmutableList.copyOf(newList);
        }
        return this.view;
    }

    private String formatTime(int timeInTicks) {
        int hour = ((timeInTicks + 6000) % 24000) / 1000;
        int min = (int) (timeInTicks % 1000 * 0.06);
        String minS = (min < 10 ? "0" : "") + min;
        return String.format("%s:%s", hour, minS);
    }

    public static class Schedule {

        public static final Codec<Schedule> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(
                        ActivityTime.CODEC.listOf().fieldOf("work_timeline").forGetter(d -> d.workdayActivities),
                        ActivityTime.CODEC.listOf().fieldOf("offday_timeline").forGetter(d -> d.nonWorkDaysActivities),
                        CodecUtils.stringEnumCodec(DayOfWeek.class, null).listOf().fieldOf("work_days").forGetter(d -> d.workDays.stream().toList())
                ).apply(inst, Schedule::new)
        );

        private final List<ActivityTime> workdayActivities, nonWorkDaysActivities;
        private final List<TimeSpan> workTimes;

        private final EnumSet<DayOfWeek> workDays;

        private Schedule(List<ActivityTime> workdayActivities, List<ActivityTime> nonWorkDaysActivities, List<DayOfWeek> workDays) {
            this(workdayActivities, nonWorkDaysActivities, workDays.isEmpty() ? EnumSet.noneOf(DayOfWeek.class) : EnumSet.copyOf(workDays));
        }

        private Schedule(int wakeUpTime, int workTime, int breakTime, int workTimeAfter, int doneWorkTime, int sleepTime, int meetTimeOffday, int meetTimeAfterOffday, List<DayOfWeek> workDays) {
            this(List.of(new ActivityTime(wakeUpTime, RuneCraftoryActivities.EARLY_IDLE.get()),

                            new ActivityTime(Math.max(wakeUpTime + 500, workTime), Activity.WORK),
                            new ActivityTime(breakTime, Activity.MEET),
                            new ActivityTime(Math.max(breakTime, workTimeAfter), Activity.WORK),
                            new ActivityTime(doneWorkTime, Activity.IDLE),

                            new ActivityTime(sleepTime, Activity.REST)),
                    List.of(new ActivityTime(wakeUpTime, RuneCraftoryActivities.EARLY_IDLE.get()),

                            new ActivityTime(meetTimeOffday, Activity.MEET),
                            new ActivityTime(meetTimeAfterOffday, Activity.IDLE),

                            new ActivityTime(sleepTime, Activity.REST)),
                    workDays.isEmpty() ? EnumSet.noneOf(DayOfWeek.class) : EnumSet.copyOf(workDays));
        }

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

        public Schedule(List<ActivityTime> workdayActivities, List<ActivityTime> nonWorkDaysActivities, EnumSet<DayOfWeek> workDays) {
            List<TimeSpan> workTimes = new ArrayList<>();
            workdayActivities = sanitize(workdayActivities);
            for (int i = 0; i < workdayActivities.size(); i++) {
                ActivityTime current = workdayActivities.get(i);
                if (current.activity() == Activity.WORK) {
                    ActivityTime next = workdayActivities.get(++i);
                    while (i < workdayActivities.size() - 1) {
                        ActivityTime pointer = workdayActivities.get(i + 1);
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
            this.workdayActivities = workdayActivities;
            this.nonWorkDaysActivities = sanitize(nonWorkDaysActivities);
            this.workTimes = List.copyOf(workTimes);
            this.workDays = workDays;
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

    public record ActivityTime(int start, Activity activity) implements Comparable<ActivityTime> {

        public static final Codec<ActivityTime> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(ExtraCodecs.NON_NEGATIVE_INT.fieldOf("time").forGetter(ActivityTime::start),
                        BuiltInRegistries.ACTIVITY.byNameCodec().fieldOf("activity").forGetter(ActivityTime::activity)
                ).apply(inst, ActivityTime::new)
        );

        @Override
        public int compareTo(@NotNull NPCSchedule.ActivityTime o) {
            return Integer.compare(this.start, o.start);
        }
    }

    public record TimeSpan(int start, int end) {
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
}
