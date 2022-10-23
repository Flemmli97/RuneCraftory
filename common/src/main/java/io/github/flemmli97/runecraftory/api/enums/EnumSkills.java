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
    FISHING("skill.fishing", GainType.MEDIUM),
    COOKING("skill.cooking", GainType.FAST),
    FORGING("skill.forging", GainType.FAST),
    CHEMISTRY("skill.chemistry", GainType.FAST),
    CRAFTING("skill.crafting", GainType.FAST),
    SLEEPING("skill.sleeping", GainType.FAST),
    SEARCHING("skill.searching", GainType.MEDIUM),
    WALKING("skill.walking", GainType.COMMON),
    EATING("skill.eating", GainType.MEDIUM),
    DEFENCE("skill.defence", GainType.COMMON),
    RESPOISON("skill.poison_res", GainType.FAST),
    RESSEAL("skill.seal_res", GainType.FAST),
    RESPARA("skill.paralysis_res", GainType.FAST),
    RESSLEEP("skill.sleep_res", GainType.FAST),
    RESFAT("skill.fatigue_res", GainType.FAST),
    RESCOLD("skill.cold_res", GainType.FAST),
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
        MEDIUM,
        FAST
    }
}
