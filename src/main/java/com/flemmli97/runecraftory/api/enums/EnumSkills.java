package com.flemmli97.runecraftory.api.enums;

public enum EnumSkills {

    SHORTSWORD("skill.short_sword"),
    LONGSWORD("skill.long_sword"),
    SPEAR("skill.spear"),
    HAMMERAXE("skill.hammer_and_axe"),
    DUAL("skill.dual_sword"),
    FIST("skill.fists"),
    FIRE("skill.fire"),
    WATER("skill.water"),
    EARTH("skill.earth"),
    WIND("skill.wind"),
    DARK("skill.dark"),
    LIGHT("skill.light"),
    LOVE("skill.love"),
    FARMING("skill.farming"),
    LOGGING("skill.logging"),
    MINING("skill.mining"),
    FISHING("skill.fishing"),
    COOKING("skill.cooking"),
    FORGING("skill.forging"),
    CHEMISTRY("skill.chemistry"),
    CRAFTING("skill.crafting"),
    SLEEPING("skill.sleeping"),
    EATING("skill.eating"),
    DEFENCE("skill.defence"),
    RESPOISON("skill.poison_res"),
    RESSEAL("skill.seal_res"),
    RESPARA("skill.paralysis_res"),
    RESSLEEP("skill.sleep_res"),
    RESFAT("skill.fatigue_res"),
    RESCOLD("skill.cold_res"),
    BATH("skill.bathing"),
    TAMING("skill.taming"),
    LEADER("skill.leadership");

    private final String translation;

    EnumSkills(String translation){
        this.translation = translation;
    }

    public String getTranslation(){
        return this.translation;
    }
}
