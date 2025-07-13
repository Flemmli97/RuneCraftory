package io.github.flemmli97.runecraftory.api.calendar;

import io.github.flemmli97.runecraftory.common.world.data.Calendar;
import net.minecraft.world.level.Level;

public class CalendarAccess {

    public static Calendar.Date getDate(Level level) {
        return Calendar.get(level).date();
    }
}
