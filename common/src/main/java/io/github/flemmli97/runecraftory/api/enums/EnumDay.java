package io.github.flemmli97.runecraftory.api.enums;

public enum EnumDay {

    MONDAY("day.monday"),
    TUESDAY("day.tuesday"),
    WEDNESDAY("day.wednesday"),
    THURSDAY("day.thursday"),
    FRIDAY("day.friday"),
    SATURDAY("day.saturday"),
    SUNDAY("day.sunday");

    private final String translation;
    private final String translationFull;

    EnumDay(String id) {
        this.translation = id;
        this.translationFull = id + ".full";
    }

    public static EnumDay nextDay(EnumDay day) {
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
