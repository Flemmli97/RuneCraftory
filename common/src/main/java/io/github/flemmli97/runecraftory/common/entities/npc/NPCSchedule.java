package io.github.flemmli97.runecraftory.common.entities.npc;

import com.google.common.collect.ImmutableList;
import io.github.flemmli97.runecraftory.api.enums.EnumDay;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import io.github.flemmli97.runecraftory.common.world.WorldHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.schedule.Activity;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public class NPCSchedule {

    private final EntityNPCBase npc;

    private int wakeUpTime;

    private int workTime, breakTime, workTimeAfter, doneWorkTime;
    private int meetTime, meetTimeAfter;

    private int sleepTime;

    private final EnumSet<EnumDay> workDays;

    private List<Component> view;

    public NPCSchedule(EntityNPCBase npc, int wakeUpTime, int workTime, int breakTime, int workTimeAfter, int doneWorkTime, int sleepTime, int meetTimeOffday, int meetTimeAfterOffday, EnumSet<EnumDay> workDays) {
        this.npc = npc;
        this.wakeUpTime = wakeUpTime;
        this.workTime = Math.max(this.wakeUpTime, workTime);
        this.breakTime = Math.max(this.workTime, breakTime);
        this.workTimeAfter = Math.max(this.breakTime, workTimeAfter);
        this.doneWorkTime = Math.max(this.workTimeAfter, doneWorkTime);
        this.meetTime = Math.max(this.wakeUpTime, meetTimeOffday);
        this.meetTimeAfter = Math.max(this.meetTime, meetTimeAfterOffday);
        this.sleepTime = Math.max(this.meetTimeAfter, Math.max(this.doneWorkTime, sleepTime));
        this.workDays = workDays;
    }

    public NPCSchedule(EntityNPCBase npc, Random random) {
        this(npc, randomizedTime(random, 6, 10),

                randomizedTime(random, 8, 10),
                randomizedTime(random, 11, 13),
                randomizedTime(random, 12, 15),
                randomizedTime(random, 16, 18),

                randomizedTime(random, 20, 23),

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

    public Activity getActivity(ServerLevel level) {
        if (this.npc.getShop() == EnumShop.RANDOM)
            return Activity.IDLE;
        int dayTime = WorldUtils.dayTime(level);
        EnumDay day = WorldHandler.get(level.getServer()).currentDay();
        if (dayTime < this.wakeUpTime)
            return Activity.REST;
        if (this.workDays.contains(day) && this.npc.getShop() != EnumShop.NONE) {
            if (dayTime < this.workTime)
                return Activity.IDLE;
            if (dayTime < this.breakTime)
                return Activity.WORK;
            if (dayTime < this.workTimeAfter)
                return Activity.MEET;
            if (dayTime < this.doneWorkTime)
                return Activity.WORK;
        } else {
            if (dayTime < this.meetTime)
                return Activity.IDLE;
            if (dayTime < this.meetTimeAfter)
                return Activity.MEET;
        }
        if (dayTime < this.sleepTime)
            return Activity.IDLE;
        return Activity.REST;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("WakeTime", this.wakeUpTime);
        tag.putInt("WorkTime", this.workTime);
        tag.putInt("BreakTime", this.breakTime);
        tag.putInt("PostBreakWork", this.workTimeAfter);
        tag.putInt("DoneWork", this.doneWorkTime);
        tag.putInt("MeetOffDay", this.meetTime);
        tag.putInt("MeetAfter", this.meetTimeAfter);
        tag.putInt("SleepTime", this.sleepTime);
        ListTag days = new ListTag();
        this.workDays.forEach(d -> days.add(StringTag.valueOf(d.name())));
        tag.put("WorkDays", days);
        return tag;
    }

    public void load(CompoundTag tag) {
        this.wakeUpTime = tag.getInt("WakeTime");
        this.workTime = tag.getInt("WorkTime");
        this.breakTime = tag.getInt("BreakTime");
        this.workTimeAfter = tag.getInt("PostBreakWork");
        this.doneWorkTime = tag.getInt("DoneWork");
        this.meetTime = tag.getInt("MeetOffDay");
        this.meetTimeAfter = tag.getInt("MeetAfter");
        this.sleepTime = tag.getInt("SleepTime");
        ListTag days = tag.getList("WorkDays", Tag.TAG_STRING);
        this.workDays.clear();
        days.forEach(t -> {
            try {
                EnumDay day = EnumDay.valueOf(t.getAsString());
                this.workDays.add(day);
            } catch (IllegalArgumentException ignored) {
            }
        });
        this.view = null;
    }

    public List<Component> viewSchedule() {
        if (this.view == null) {
            if (this.npc.getShop() == EnumShop.NONE || this.npc.getShop() == EnumShop.RANDOM) {
                this.view = ImmutableList.of();
                return this.view;
            }
            List<Component> newList = new ArrayList<>();
            boolean noBreaks = this.breakTime == this.workTimeAfter;
            newList.add(new TranslatableComponent("npc.schedule.work", this.formatTime(this.workTime), noBreaks ? this.formatTime(this.doneWorkTime) : this.formatTime(this.breakTime)));
            if (!noBreaks)
                newList.add(new TranslatableComponent("npc.schedule.work.2", this.formatTime(this.workTimeAfter), this.formatTime(this.doneWorkTime)));
            newList.add(new TranslatableComponent("npc.schedule.days.header"));
            List<EnumDay> weekDayCounts = new ArrayList<>();
            for (EnumDay day : EnumDay.values()) {
                if (day == EnumDay.SATURDAY || day == EnumDay.SUNDAY)
                    continue;
                if (!this.workDays.contains(day))
                    weekDayCounts.add(day);
            }
            if (this.workDays.size() == EnumDay.values().length) {
                newList.add(new TranslatableComponent("npc.schedule.days.all"));
            } else {
                switch (weekDayCounts.size()) {
                    case 0 -> newList.add(new TranslatableComponent("npc.schedule.days.0"));
                    case 1 -> newList.add(new TranslatableComponent("npc.schedule.days.1", weekDayCounts.get(0)));
                    case 2 -> newList.add(new TranslatableComponent("npc.schedule.days.2", weekDayCounts.get(0), weekDayCounts.get(1)));
                    default -> newList.add(new TranslatableComponent("npc.schedule.days.with", this.workDays.stream().filter(day -> day != EnumDay.SATURDAY && day != EnumDay.SUNDAY).map(EnumDay::translation).toArray()));
                }
                if (this.workDays.contains(EnumDay.SATURDAY)) {
                    if (this.workDays.contains(EnumDay.SUNDAY))
                        newList.add(new TranslatableComponent("npc.schedule.days.weekend.2", EnumDay.SATURDAY.translation(), EnumDay.SUNDAY.translation()));
                    else
                        newList.add(new TranslatableComponent("npc.schedule.days.weekend.1", new TranslatableComponent(EnumDay.SATURDAY.translation())));
                } else if (this.workDays.contains(EnumDay.SUNDAY))
                    newList.add(new TranslatableComponent("npc.schedule.days.weekend.1", new TranslatableComponent(EnumDay.SUNDAY.translation())));
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
}
