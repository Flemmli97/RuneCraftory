package io.github.flemmli97.runecraftory.common.entities.npc;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.calendar.DayOfWeek;
import io.github.flemmli97.runecraftory.common.registry.ModActivities;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import io.github.flemmli97.runecraftory.common.world.data.Calendar;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.schedule.Activity;
import net.tslat.smartbrainlib.api.core.schedule.SmartBrainSchedule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Supplier;

public class NPCSchedule {

    private final EntityNPCBase npc;

    private Schedule schedule;

    private List<Component> view;

    public NPCSchedule(EntityNPCBase npc, Schedule schedule) {
        this.npc = npc;
        this.schedule = schedule;
    }

    public NPCSchedule(EntityNPCBase npc, RandomSource random) {
        this(npc, new Schedule(random));
    }

    public static WrappedSchedule forBrain(Supplier<NPCSchedule> sup) {
        return new WrappedSchedule(sup);
    }

    public Activity getActivity(ServerLevel level) {
        if (!this.npc.getProfession().hasSchedule)
            return Activity.IDLE;
        int dayTime = WorldUtils.dayTime(level);
        DayOfWeek day = Calendar.get(level).date().day();
        if (dayTime < this.schedule.wakeUpTime)
            return Activity.REST;
        if (!this.npc.isBaby() && this.schedule.workDays.contains(day) && this.npc.getProfession().hasWorkSchedule) {
            if (dayTime < this.schedule.workTime)
                return ModActivities.EARLY_IDLE.get();
            if (dayTime < this.schedule.breakTime)
                return Activity.WORK;
            if (dayTime < this.schedule.workTimeAfter)
                return Activity.MEET;
            if (dayTime < this.schedule.doneWorkTime)
                return Activity.WORK;
        } else {
            if (dayTime < this.schedule.meetTime)
                return Activity.IDLE;
            if (dayTime < this.schedule.meetTimeAfter)
                return Activity.MEET;
        }
        if (dayTime < this.schedule.sleepTime)
            return Activity.IDLE;
        return Activity.REST;
    }

    public CompoundTag save() {
        return (CompoundTag) Schedule.CODEC.encodeStart(NbtOps.INSTANCE, this.schedule).getOrThrow();
    }

    public void load(CompoundTag tag) {
        this.schedule = Schedule.CODEC.parse(NbtOps.INSTANCE, tag)
                .resultOrPartial(s -> RuneCraftory.LOGGER.error("Couldn't load schedule for {}. {}", this.npc, s))
                .orElse(new Schedule(this.npc.getRandom()));
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
            boolean noBreaks = this.schedule.breakTime == this.schedule.workTimeAfter;
            newList.add(Component.translatable("runecraftory.npc.schedule.work", this.formatTime(this.schedule.workTime), noBreaks ? this.formatTime(this.schedule.doneWorkTime) : this.formatTime(this.schedule.breakTime)));
            if (!noBreaks)
                newList.add(Component.translatable("runecraftory.npc.schedule.work.2", this.formatTime(this.schedule.workTimeAfter), this.formatTime(this.schedule.doneWorkTime)));
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
                        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("wake_up_time").forGetter(d -> d.wakeUpTime),
                        ExtraCodecs.POSITIVE_INT.fieldOf("work_time").forGetter(d -> d.workTime),
                        ExtraCodecs.POSITIVE_INT.fieldOf("break_time").forGetter(d -> d.breakTime),
                        ExtraCodecs.POSITIVE_INT.fieldOf("work_time_after").forGetter(d -> d.workTimeAfter),
                        ExtraCodecs.POSITIVE_INT.fieldOf("done_work_time").forGetter(d -> d.doneWorkTime),
                        ExtraCodecs.POSITIVE_INT.fieldOf("meet_time").forGetter(d -> d.meetTime),
                        ExtraCodecs.POSITIVE_INT.fieldOf("meet_time_after").forGetter(d -> d.meetTimeAfter),
                        ExtraCodecs.POSITIVE_INT.fieldOf("sleep_time").forGetter(d -> d.sleepTime),
                        CodecUtils.stringEnumCodec(DayOfWeek.class, null).listOf().fieldOf("work_days").forGetter(d -> d.workDays.stream().toList())
                ).apply(inst, Schedule::new)
        );

        public final int wakeUpTime;

        public final int workTime, breakTime, workTimeAfter, doneWorkTime;
        public final int meetTime, meetTimeAfter;

        public final int sleepTime;

        private final EnumSet<DayOfWeek> workDays;

        public Schedule(int wakeUpTime, int workTime, int breakTime, int workTimeAfter, int doneWorkTime, int sleepTime, int meetTimeOffday, int meetTimeAfterOffday, EnumSet<DayOfWeek> workDays) {
            this.wakeUpTime = wakeUpTime;
            this.workTime = Math.max(this.wakeUpTime + 500, workTime);
            this.breakTime = Math.max(this.workTime, breakTime);
            this.workTimeAfter = Math.max(this.breakTime, workTimeAfter);
            this.doneWorkTime = Math.max(this.workTimeAfter, doneWorkTime);
            this.meetTime = Math.max(this.wakeUpTime, meetTimeOffday);
            this.meetTimeAfter = Math.max(this.meetTime, meetTimeAfterOffday);
            this.sleepTime = Math.max(this.meetTimeAfter, Math.max(this.doneWorkTime, sleepTime));
            this.workDays = workDays;
        }

        private Schedule(int wakeUpTime, int workTime, int breakTime, int workTimeAfter, int doneWorkTime, int sleepTime, int meetTimeOffday, int meetTimeAfterOffday, List<DayOfWeek> workDays) {
            this(wakeUpTime, workTime, breakTime, workTimeAfter, doneWorkTime, sleepTime, meetTimeOffday, meetTimeAfterOffday, workDays.isEmpty() ? EnumSet.noneOf(DayOfWeek.class) : EnumSet.copyOf(workDays));
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

        private static int randomizedTime(RandomSource random, int min, int max) {
            float hourAdd = random.nextInt((max - min) * 2) * 0.5f;
            float hour = (min + hourAdd - 6); //-6 cause 0 daytime = 6:00
            return (int) (hour * 10) * 100;
        }

        private static EnumSet<DayOfWeek> randomizedWorkDays(RandomSource random) {
            EnumSet<DayOfWeek> set = EnumSet.noneOf(DayOfWeek.class);
            for (DayOfWeek day : DayOfWeek.values()) {
                if (day == DayOfWeek.SATURDAY) {
                    if (random.nextFloat() < 0.4f)
                        set.add(day);
                } else if (day == DayOfWeek.SUNDAY) {
                    if (random.nextFloat() < 0.15f)
                        set.add(day);
                } else if (random.nextFloat() < 0.85f)
                    set.add(day);
            }
            return set;
        }

        public Collection<DayOfWeek> getWorkDays() {
            return ImmutableSet.copyOf(this.workDays);
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
}
