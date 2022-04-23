package io.github.flemmli97.runecraftory.api.enums;

public enum EnumCrafting {

    FORGE("forge"),
    ARMOR("armor"),
    CHEM("chemistry"),
    COOKING("cooking");

    private final String id;

    EnumCrafting(String translation) {
        this.id = translation;
    }

    public String getId() {
        return this.id;
    }
}
