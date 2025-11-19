package io.github.flemmli97.runecraftory.api.attachment;

import io.github.flemmli97.runecraftory.api.datapack.SkillProperties;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;

public enum Skills {

    SHORTSWORD("short_sword"),
    LONGSWORD("long_sword"),
    SPEAR("spear"),
    HAMMERAXE("hammer_and_axe"),
    DUAL("dual_sword"),
    FIST("fists"),

    FIRE("fire"),
    WATER("water"),
    EARTH("earth"),
    WIND("wind"),
    DARK("dark"),
    LIGHT("light"),
    LOVE("love"),

    FARMING("farming"),
    LOGGING("logging"),
    MINING("mining"),
    FISHING("fishing"),

    COOKING("cooking"),
    FORGING("forging"),
    CHEMISTRY("chemistry"),
    CRAFTING("crafting"),

    SLEEPING("sleeping"),
    SEARCHING("searching"),
    WALKING("walking"),
    EATING("eating"),
    DEFENCE("defence"),

    RES_POISON("poison_res"),
    RES_SEAL("seal_res"),
    RES_PARA("paralysis_res"),
    RES_SLEEP("sleep_res"),
    RES_FATIGUE("fatigue_res"),
    RES_COLD("cold_res"),

    BATH("bathing"),
    TAMING("taming"),
    LEADER("leadership");

    public static final String PREFIX = "runecraftory.skill.";
    private final String translation;

    Skills(String translation) {
        this.translation = PREFIX + translation;
    }

    public static Skills read(String s) {
        try {
            return Skills.valueOf(s);
        } catch (IllegalArgumentException ignored) {
        }
        return null;
    }

    public String getTranslation() {
        return this.translation;
    }

    public SkillProperties getProperties() {
        return DataPackHandler.INSTANCE.skillPropertiesManager().getPropertiesFor(this);
    }

    public enum GainType {
        COMMON,
        SLOW,
        FAST,
        VERY_FAST,
        CRAFTING
    }
}
