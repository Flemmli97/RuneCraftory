package io.github.flemmli97.runecraftory.common.entities.npc;

import io.github.flemmli97.runecraftory.common.registry.ModPoiTypes;
import net.minecraft.world.entity.ai.village.poi.PoiType;

import javax.annotation.Nullable;
import java.util.function.Predicate;
import java.util.function.Supplier;

public enum EnumShop {

    NONE(() -> null, "shop_none"),
    GENERAL(() -> PoiType.FARMER, "shop_general"),
    FLOWER(() -> PoiType.FARMER, "shop_flower"),
    WEAPON(() -> PoiType.TOOLSMITH, "shop_weapon"),
    CLINIC(() -> PoiType.CLERIC, "shop_clinic"),
    FOOD(() -> PoiType.BUTCHER, "shop_food"),
    MAGIC(ModPoiTypes.CASH_REGISTER, "shop_magic"),
    RUNESKILL(ModPoiTypes.CASH_REGISTER, "shop_runeskill"),
    BATHHOUSE(() -> PoiType.FISHERMAN, "shop_bathhouse"),
    RANDOM(() -> null, "shop_random");

    public final Supplier<PoiType> poiType;

    @Nullable
    public final Predicate<PoiType> predicate;

    public final String translationKey;

    EnumShop(Supplier<PoiType> poiType, String translationKey) {
        this.poiType = poiType;
        this.translationKey = translationKey;
        if (poiType == ModPoiTypes.CASH_REGISTER)
            this.predicate = t -> this.poiType.get().getPredicate().test(t);
        else
            this.predicate = this.poiType == null ? null : t -> this.poiType.get().getPredicate().test(t) || ModPoiTypes.CASH_REGISTER.get().getPredicate().test(t);
    }
}
