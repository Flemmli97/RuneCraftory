package com.flemmli97.runecraftory.api.enums;

public enum EnumDay {

    MONDAY("day.monday"),
    TUESDAY("day.tuesday"),
    WEDNESDAY("day.wednesday"),
    THURSDAY("day.thursday"),
    FRIDAY("day.friday"),
    SATURDAY("day.saturday"),
    SUNDAY("day.sunday");

    private final String translation;

    EnumDay(String id) {
        this.translation = id;
    }

    public static EnumDay nextDay(EnumDay day) {
        switch (day) {
            case FRIDAY:
                return SATURDAY;
            case MONDAY:
                return TUESDAY;
            case SATURDAY:
                return SUNDAY;
            case THURSDAY:
                return FRIDAY;
            case TUESDAY:
                return WEDNESDAY;
            case WEDNESDAY:
                return THURSDAY;
            default:
                return MONDAY;
        }
    }

    public String translation() {
        return this.translation;
    }

}
