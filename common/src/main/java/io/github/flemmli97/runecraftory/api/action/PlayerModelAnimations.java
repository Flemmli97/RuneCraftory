package io.github.flemmli97.runecraftory.api.action;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.api.enums.EnumWeaponType;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * All relevant player model animations. Here since NPC also use them
 */
public class PlayerModelAnimations {

    private static final List<AnimatedAction> ALL = new ArrayList<>();

    public static final List<AnimatedAction> SHORT_SWORD = addTo(6, "short_sword", count -> {
                int defaultLength = EnumWeaponType.SHORTSWORD.defaultWeaponSpeed;
                int attack = 4;
                switch (count) {
                    case 1, 3 -> attack = (int) Math.ceil(0.32 * 20);
                    case 2 -> attack = (int) Math.ceil(0.24 * 20);
                    case 4 -> {
                        defaultLength = (int) Math.ceil(0.6 * 20);
                        attack = (int) Math.ceil(0.2 * 20);
                    }
                    case 5 -> attack = (int) Math.ceil(0.28 * 20);
                    case 6 -> defaultLength = (int) Math.ceil(1.36 * 20);
                }
                return Pair.of(defaultLength, attack);
            }
    );

    public static final List<AnimatedAction> LONG_SWORD = addTo(4, "long_sword", count -> {
                int defaultLength = EnumWeaponType.LONGSWORD.defaultWeaponSpeed;
                int attack = (int) Math.ceil(0.48 * 20);
                if (count == 3)
                    attack = (int) Math.ceil(0.56 * 20);
                if (count == 4)
                    defaultLength = (int) Math.ceil(1.4 * 20);
                return Pair.of(defaultLength, attack);
            }
    );

    public static final List<AnimatedAction> SPEAR = addTo(5, "spear", count -> {
                int defaultLength = EnumWeaponType.SPEAR.defaultWeaponSpeed;
                int attack = (int) Math.ceil(0.36 * 20);
                if (count == 2) {
                    defaultLength = (int) Math.ceil(0.52 * 20);
                    attack = (int) Math.ceil(0.24 * 20);
                } else if (count == 5) {
                    defaultLength = (int) Math.ceil(1.84 * 20);
                }
                return Pair.of(defaultLength, attack);
            }
    );

    public static final List<AnimatedAction> HAMMER_AXE = addTo(3, "hammer_axe", count -> {
                int defaultLength = EnumWeaponType.HAXE.defaultWeaponSpeed;
                int attack = (int) Math.ceil(0.6 * 20);
                if (count == 2)
                    attack = (int) Math.ceil(0.48 * 20);
                else if (count == 3)
                    defaultLength = (int) Math.ceil(1.48 * 20);
                return Pair.of(defaultLength, attack);
            }
    );

    public static final List<AnimatedAction> DUAL_BLADES = addTo(8, "dual_blades", count -> {
                int defaultLength = EnumWeaponType.DUAL.defaultWeaponSpeed;
                int attack = (int) Math.ceil(0.4 * 20);
                switch (count) {
                    case 1 -> attack = (int) Math.ceil(0.16 * 20);
                    case 2 -> attack = (int) Math.ceil(0.2 * 20);
                    case 3, 4, 5, 6 -> attack = (int) Math.ceil(0.24 * 20);
                    case 7 -> attack = (int) Math.ceil(0.4 * 20);
                    case 8 -> defaultLength = (int) Math.ceil(1.44 * 20);
                }
                return Pair.of(defaultLength, attack);
            }
    );

    public static final List<AnimatedAction> GLOVES = addTo(5, "glove", count -> {
                int defaultLength = EnumWeaponType.GLOVE.defaultWeaponSpeed;
                int attack = (int) Math.ceil(0.4 * 20);
                switch (count) {
                    case 1 -> attack = (int) Math.ceil(0.28 * 20);
                    case 2, 3 -> attack = (int) Math.ceil(0.24 * 20);
                    case 4 -> {
                        attack = (int) Math.ceil(0.4 * 20);
                        defaultLength = (int) Math.ceil(0.64 * 20);
                    }
                    case 5 -> defaultLength = (int) Math.ceil(1.32 * 20);
                }
                return Pair.of(defaultLength, attack);
            }
    );

    public static final AnimatedAction STAFF = addTo(AnimatedAction.builder(EnumWeaponType.STAFF.defaultWeaponSpeed + 1, "staff").marker(9).build());
    public static final AnimatedAction STAFF_USE = addTo(new AnimatedAction(16 + 1, 4, "staff_use"));

    public static List<AnimatedAction> getAll() {
        return ImmutableList.copyOf(ALL);
    }

    private static List<AnimatedAction> addTo(int amount, String baseName, DependendAnimationValue values) {
        ImmutableList.Builder<AnimatedAction> list = new ImmutableList.Builder<>();
        for (int i = 1; i <= amount; i++) {
            Pair<Integer, Integer> val = values.getValues(i);
            AnimatedAction anim = AnimatedAction.builder(val.getFirst() + 1, baseName + "_" + i).marker(val.getSecond()).build();
            ALL.add(anim);
            list.add(anim);
        }
        return list.build();
    }

    private static List<AnimatedAction> addTo(AnimatedAction... anims) {
        Collections.addAll(ALL, anims);
        return List.of(anims);
    }

    private static AnimatedAction addTo(AnimatedAction anim) {
        ALL.add(anim);
        return anim;
    }

    interface DependendAnimationValue {

        Pair<Integer, Integer> getValues(int count);

    }
}
