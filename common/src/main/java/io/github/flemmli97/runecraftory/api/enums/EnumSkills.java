package io.github.flemmli97.runecraftory.api.enums;

public enum EnumSkills {

    SHORTSWORD("skill.short_sword", GainType.SLOW),
    LONGSWORD("skill.long_sword", GainType.SLOW),
    SPEAR("skill.spear", GainType.SLOW),
    HAMMERAXE("skill.hammer_and_axe", GainType.SLOW),
    DUAL("skill.dual_sword", GainType.SLOW),
    FIST("skill.fists", GainType.SLOW),

    FIRE("skill.fire", GainType.SLOW),
    WATER("skill.water", GainType.SLOW),
    EARTH("skill.earth", GainType.SLOW),
    WIND("skill.wind", GainType.SLOW),
    DARK("skill.dark", GainType.SLOW),
    LIGHT("skill.light", GainType.SLOW),
    LOVE("skill.love", GainType.SLOW),

    FARMING("skill.farming", GainType.COMMON),
    LOGGING("skill.logging", GainType.COMMON),
    MINING("skill.mining", GainType.COMMON),
    FISHING("skill.fishing", GainType.VERY_FAST),

    COOKING("skill.cooking", GainType.CRAFTING),
    FORGING("skill.forging", GainType.CRAFTING),
    CHEMISTRY("skill.chemistry", GainType.CRAFTING),
    CRAFTING("skill.crafting", GainType.CRAFTING),

    SLEEPING("skill.sleeping", GainType.FAST),
    SEARCHING("skill.searching", GainType.VERY_FAST),
    WALKING("skill.walking", GainType.COMMON),
    EATING("skill.eating", GainType.VERY_FAST),
    DEFENCE("skill.defence", GainType.COMMON),

    RES_POISON("skill.poison_res", GainType.FAST),
    RES_SEAL("skill.seal_res", GainType.FAST),
    RES_PARA("skill.paralysis_res", GainType.FAST),
    RES_SLEEP("skill.sleep_res", GainType.FAST),
    RES_FATIGUE("skill.fatigue_res", GainType.FAST),
    RES_COLD("skill.cold_res", GainType.FAST),

    BATH("skill.bathing", GainType.FAST),
    TAMING("skill.taming", GainType.FAST),
    LEADER("skill.leadership", GainType.FAST);

    private final String translation;

    public final GainType gainType;

    EnumSkills(String translation, GainType type) {
        this.translation = translation;
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
        CRAFTING;
    }
}
