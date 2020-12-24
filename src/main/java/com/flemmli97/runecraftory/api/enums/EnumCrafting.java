package com.flemmli97.runecraftory.api.enums;

public enum EnumCrafting {

    FORGE("forge"),
    ARMOR("armor"),
    CHEM("chemistry"),
    COOKING("cooking");

    private final String translation;

    EnumCrafting(String translation){
        this.translation = translation;
    }

    public String getTranslation(){
        return this.translation;
    }
}
