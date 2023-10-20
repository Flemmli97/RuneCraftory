package io.github.flemmli97.runecraftory.common.entities.npc;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumDay;
import io.github.flemmli97.runecraftory.common.registry.ModActivities;
import io.github.flemmli97.runecraftory.common.utils.CodecHelper;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import io.github.flemmli97.runecraftory.common.world.WorldHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.schedule.Activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public class NPCSchedule {

    private final EntityNPCBase npc;

    private Schedule schedule;

    private List<Component> view;

    public NPCSchedule(EntityNPCBase npc, Schedule schedule) {
        this.npc = npc;
        this.schedule = schedule;
    }

    public NPCSchedule(EntityNPCBase npc, Random random) {
        this(npc, new Schedule(random));
    }

    public Activity getActivity(ServerLevel level) {
        if (!this.npc.getShop().hasSchedule)
            return Activity.IDLE;
        int dayTime = WorldUtils.dayTime(level);
        EnumDay day = WorldHandler.get(level.getServer()).currentDay();
        if (dayTime < this.schedule.wakeUpTime)
            return Activity.REST;
        if (this.schedule.workDays.contains(day) && this.npc.getShop().hasWorkSchedule) {
            if (dayTime < this.schedule.workTime)
                return ModActivities.EARLYIDLE.get();
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
        return (CompoundTag) Schedule.CODEC.encodeStart(NbtOps.INSTANCE, this.schedule).getOrThrow(true, RuneCraftory.logger::error);
    }

    public void load(CompoundTag tag) {
        this.schedule = Schedule.CODEC.parse(NbtOps.INSTANCE, tag)
                .resultOrPartial(s -> RuneCraftory.logger.error("Couldn't load schedule for " + this.npc + ". " + s)).orElse(new Schedule(this.npc.getRandom()));
        this.view = null;
    }

    public void with(Schedule schedule) {
        this.schedule = schedule;
        this.view = null;
    }

    public List<Component> viewSchedule() {
        if (this.view == null) {
            if (!this.npc.getShop().hasSchedule || !this.npc.getShop().hasWorkSchedule) {
                this.view = ImmutableList.of();
                return this.view;
            }
            List<Component> newList = new ArrayList<>();
            boolean noBreaks = this.schedule.breakTime == this.schedule.workTimeAfter;
            newList.add(new TranslatableComponent("runecraftory.npc.schedule.work", this.formatTime(this.schedule.workTime), noBreaks ? this.formatTime(this.schedule.doneWorkTime) : this.formatTime(this.schedule.breakTime)));
            if (!noBreaks)
                newList.add(new TranslatableComponent("npc.schedule.work.2", this.formatTime(this.schedule.workTimeAfter), this.formatTime(this.schedule.doneWorkTime)));
            newList.add(new TranslatableComponent("runecraftory.npc.schedule.days.header"));
            List<EnumDay> weekDayCounts = new ArrayList<>();
            for (EnumDay day : EnumDay.values()) {
                if (day == EnumDay.SATURDAY || day == EnumDay.SUNDAY)
                    continue;
                if (!this.schedule.workDays.contains(day))
                    weekDayCounts.add(day);
            }
            if (this.schedule.workDays.size() == EnumDay.values().length) {
                newList.add(new TranslatableComponent("runecraftory.npc.schedule.days.all"));
            } else {
                switch (weekDayCounts.size()) {
                    case 0 -> newList.add(new TranslatableComponent("npc.schedule.days.0"));
                    case 1 -> newList.add(new TranslatableComponent("npc.schedule.days.1", new TranslatableComponent(weekDayCounts.get(0).translationFull())));
                    case 2 -> newList.add(new TranslatableComponent("npc.schedule.days.2", new TranslatableComponent(weekDayCounts.get(0).translationFull(), weekDayCounts.get(1))));
                    default -> newList.add(new TranslatableComponent("runecraftory.npc.schedule.days.with", this.schedule.workDays.stream().filter(day -> day != EnumDay.SATURDAY && day != EnumDay.SUNDAY).map(e -> new TranslatableComponent(e.translationFull())).toArray()));
                }
                if (this.schedule.workDays.contains(EnumDay.SATURDAY)) {
                    if (this.schedule.workDays.contains(EnumDay.SUNDAY))
                        newList.add(new TranslatableComponent("npc.schedule.days.weekend.2", new TranslatableComponent(EnumDay.SATURDAY.translationFull()), new TranslatableComponent(EnumDay.SUNDAY.translationFull())));
                    else
                        newList.add(new TranslatableComponent("npc.schedule.days.weekend.1", new TranslatableComponent(EnumDay.SATURDAY.translationFull())));
                } else if (this.schedule.workDays.contains(EnumDay.SUNDAY))
                    newList.add(new TranslatableComponent("npc.schedule.days.weekend.1", new TranslatableComponent(EnumDay.SUNDAY.translationFull())));
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
                        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("WakeUpTime").forGetter(d -> d.wakeUpTime),
                        ExtraCodecs.POSITIVE_INT.fieldOf("WorkTime").forGetter(d -> d.workTime),
                        ExtraCodecs.POSITIVE_INT.fieldOf("BreakTime").forGetter(d -> d.breakTime),
                        ExtraCodecs.POSITIVE_INT.fieldOf("WorkTimeAfter").forGetter(d -> d.workTimeAfter),
                        ExtraCodecs.POSITIVE_INT.fieldOf("DoneWorkTime").forGetter(d -> d.doneWorkTime),
                        ExtraCodecs.POSITIVE_INT.fieldOf("MeetTime").forGetter(d -> d.meetTime),
                        ExtraCodecs.POSITIVE_INT.fieldOf("MeetTimeAfter").forGetter(d -> d.meetTimeAfter),
                        ExtraCodecs.POSITIVE_INT.fieldOf("SleepTime").forGetter(d -> d.sleepTime),
                        CodecHelper.enumCodec(EnumDay.class, null).listOf().fieldOf("WorkDays").forGetter(d -> d.workDays.stream().toList())
                ).apply(inst, Schedule::new)
        );

        public final int wakeUpTime;

        public final int workTime, breakTime, workTimeAfter, doneWorkTime;
        public final int meetTime, meetTimeAfter;

        public final int sleepTime;

        private final EnumSet<EnumDay> workDays;

        public Schedule(int wakeUpTime, int workTime, int breakTime, int workTimeAfter, int doneWorkTime, int sleepTime, int meetTimeOffday, int meetTimeAfterOffday, EnumSet<EnumDay> workDays) {
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

        private Schedule(int wakeUpTime, int workTime, int breakTime, int workTimeAfter, int doneWorkTime, int sleepTime, int meetTimeOffday, int meetTimeAfterOffday, List<EnumDay> workDays) {
            this(wakeUpTime, workTime, breakTime, workTimeAfter, doneWorkTime, sleepTime, meetTimeOffday, meetTimeAfterOffday, workDays.isEmpty() ? EnumSet.noneOf(EnumDay.class) : EnumSet.copyOf(workDays));
        }

        public Schedule(Random random) {
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

        private static int randomizedTime(Random random, int min, int max) {
            float hourAdd = random.nextInt((max - min) * 2) * 0.5f;
            float hour = (min + hourAdd - 6); //-6 cause 0 daytime = 6:00
            return (int) (hour * 10) * 100;
        }

        private static EnumSet<EnumDay> randomizedWorkDays(Random random) {
            EnumSet<EnumDay> set = EnumSet.noneOf(EnumDay.class);
            for (EnumDay day : EnumDay.values()) {
                if (day == EnumDay.SATURDAY) {
                    if (random.nextFloat() < 0.4f)
                        set.add(day);
                } else if (day == EnumDay.SUNDAY) {
                    if (random.nextFloat() < 0.15f)
                        set.add(day);
                } else if (random.nextFloat() < 0.85f)
                    set.add(day);
            }
            return set;
        }

        public Collection<EnumDay> getWorkDays() {
            return ImmutableSet.copyOf(this.workDays);
        }
    }
}
