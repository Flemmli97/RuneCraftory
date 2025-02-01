package io.github.flemmli97.runecraftory.api.enums;

import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.common.registry.ModAttackActions;

import java.util.function.Supplier;

public enum EnumWeaponType {

    FARM(1, null),
    SHORTSWORD(1, ModAttackActions.SHORT_SWORD),
    LONGSWORD(0.5f, ModAttackActions.LONG_SWORD),
    SPEAR(0.5f, ModAttackActions.SPEAR),
    HAXE(0.5f, ModAttackActions.HAMMER_AXE),
    DUAL(0, ModAttackActions.DUAL_BLADES),
    GLOVE(0, ModAttackActions.GLOVES),
    STAFF(0.5f, ModAttackActions.STAFF);

    public final float shieldEfficiency;
    private final Supplier<AttackAction> action;

    EnumWeaponType(float shieldEfficiency, Supplier<AttackAction> action) {
        this.shieldEfficiency = shieldEfficiency;
        this.action = action;
    }

    public AttackAction getAction() {
        return this.action != null ? this.action.get() : null;
    }
}
