package io.github.flemmli97.runecraftory.api.enums;

public enum EnumSkills {

    SHORTSWORD("short_sword", GainType.SLOW),
    LONGSWORD("long_sword", GainType.SLOW),
    SPEAR("spear", GainType.SLOW),
    HAMMERAXE("hammer_and_axe", GainType.SLOW),
    DUAL("dual_sword", GainType.SLOW),
    FIST("fists", GainType.SLOW),

    FIRE("fire", GainType.SLOW),
    WATER("water", GainType.SLOW),
    EARTH("earth", GainType.SLOW),
    WIND("wind", GainType.SLOW),
    DARK("dark", GainType.SLOW),
    LIGHT("light", GainType.SLOW),
    LOVE("love", GainType.SLOW),

    FARMING("farming", GainType.COMMON),
    LOGGING("logging", GainType.COMMON),
    MINING("mining", GainType.COMMON),
    FISHING("fishing", GainType.VERY_FAST),

    COOKING("cooking", GainType.CRAFTING),
    FORGING("forging", GainType.CRAFTING),
    CHEMISTRY("chemistry", GainType.CRAFTING),
    CRAFTING("crafting", GainType.CRAFTING),

    SLEEPING("sleeping", GainType.FAST),
    SEARCHING("searching", GainType.VERY_FAST),
    WALKING("walking", GainType.COMMON),
    EATING("eating", GainType.VERY_FAST),
    DEFENCE("defence", GainType.COMMON),

    RES_POISON("poison_res", GainType.FAST),
    RES_SEAL("seal_res", GainType.FAST),
    RES_PARA("paralysis_res", GainType.FAST),
    RES_SLEEP("sleep_res", GainType.FAST),
    RES_FATIGUE("fatigue_res", GainType.FAST),
    RES_COLD("cold_res", GainType.FAST),

    BATH("bathing", GainType.FAST),
    TAMING("taming", GainType.FAST),
    LEADER("leadership", GainType.FAST);

    public static final String PREFIX = "runecraftory.skill.";
    public final GainType gainType;
    private final String translation;

    EnumSkills(String translation, GainType type) {
        this.translation = PREFIX + translation;
        this.gainType = type;
    }

    public static EnumSkills read(String s) {
        try {
            return EnumSkills.valueOf(s);
        } catch (IllegalArgumentException ignored) {
        }
        return null;
    }

    public String getTranslation() {
        return this.translation;
    }

    public enum GainType {
        COMMON,
        SLOW,
        FAST,
        VERY_FAST,
        CRAFTING
    }
}
