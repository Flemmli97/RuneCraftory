package io.github.flemmli97.runecraftory.api.calendar;

public enum DayOfWeek {

    MONDAY("runecraftory.day.monday"),
    TUESDAY("runecraftory.day.tuesday"),
    WEDNESDAY("runecraftory.day.wednesday"),
    THURSDAY("runecraftory.day.thursday"),
    FRIDAY("runecraftory.day.friday"),
    SATURDAY("runecraftory.day.saturday"),
    SUNDAY("runecraftory.day.sunday");

    private final String translation;
    private final String translationFull;

    DayOfWeek(String id) {
        this.translation = id;
        this.translationFull = id + ".full";
    }

    public static DayOfWeek nextDay(DayOfWeek day) {
        return switch (day) {
            case FRIDAY -> SATURDAY;
            case MONDAY -> TUESDAY;
            case SATURDAY -> SUNDAY;
            case THURSDAY -> FRIDAY;
            case TUESDAY -> WEDNESDAY;
            case WEDNESDAY -> THURSDAY;
            default -> MONDAY;
        };
    }

    public String translation() {
        return this.translation;
    }

    public String translationFull() {
        return this.translationFull;
    }
}
