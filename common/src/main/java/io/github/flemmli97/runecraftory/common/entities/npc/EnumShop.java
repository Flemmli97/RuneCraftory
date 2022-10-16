package io.github.flemmli97.runecraftory.common.entities.npc;

import io.github.flemmli97.runecraftory.common.registry.ModPoiTypes;
import net.minecraft.world.entity.ai.village.poi.PoiType;

import java.util.function.Supplier;

public enum EnumShop {

    NONE(() -> null, "shop_none"),
    GENERAL(() -> PoiType.FARMER, "shop_general"),
    FLOWER(() -> PoiType.FARMER, "shop_flower"),
    WEAPON(() -> PoiType.WEAPONSMITH, "shop_weapon"),
    CLINIC(() -> PoiType.CLERIC, "shop_clinic"),
    FOOD(() -> PoiType.BUTCHER, "shop_food"),
    MAGIC(ModPoiTypes.skills, "shop_magic"),
    RUNESKILL(ModPoiTypes.skills, "shop_runeskill"),
    RANDOM(() -> null, "shop_random");

    public final Supplier<PoiType> poiType;

    public final String translationKey;

    EnumShop(Supplier<PoiType> poiType, String translationKey) {
        this.poiType = poiType;
        this.translationKey = translationKey;
    }
}
