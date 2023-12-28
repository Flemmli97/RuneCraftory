package io.github.flemmli97.runecraftory.api.action;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
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
                int defaultLength = (int) Math.ceil(0.32 * 20);
                int attack = 4;
                switch (count) {
                    case 1, 3 -> attack = (int) Math.ceil(0.32 * 20);
                    case 2 -> attack = (int) Math.ceil(0.24 * 20);
                    case 4 -> attack = (int) Math.ceil(0.2 * 20);
                    case 5 -> attack = (int) Math.ceil(0.28 * 20);
                    case 6 -> defaultLength = (int) Math.ceil(1.36 * 20);
                }
                return Pair.of(defaultLength, attack);
            }
    );

    public static final List<AnimatedAction> LONG_SWORD = addTo(4, "long_sword", count -> {
                int defaultLength = (int) Math.ceil(0.56 * 20);
                int attack = (int) Math.ceil(0.48 * 20);
                if (count == 3)
                    attack = (int) Math.ceil(0.56 * 20);
                if (count == 4)
                    defaultLength = (int) Math.ceil(1.4 * 20);
                return Pair.of(defaultLength, attack);
            }
    );

    public static final List<AnimatedAction> SPEAR = addTo(5, "spear", count -> {
                int defaultLength = (int) Math.ceil(0.36 * 20);
                int attack = (int) Math.ceil(0.36 * 20);
                if (count == 2) {
                    attack = (int) Math.ceil(0.24 * 20);
                } else if (count == 5) {
                    defaultLength = (int) Math.ceil(1.84 * 20);
                }
                return Pair.of(defaultLength, attack);
            }
    );

    public static final List<AnimatedAction> HAMMER_AXE = addTo(3, "hammer_axe", count -> {
                int defaultLength = (int) Math.ceil(0.6 * 20);
                int attack = (int) Math.ceil(0.6 * 20);
                if (count == 2)
                    attack = (int) Math.ceil(0.48 * 20);
                else if (count == 3)
                    defaultLength = (int) Math.ceil(1.48 * 20);
                return Pair.of(defaultLength, attack);
            }
    );

    public static final List<AnimatedAction> DUAL_BLADES = addTo(8, "dual_blades", count -> {
                int defaultLength = (int) Math.ceil(0.28 * 20);
                int attack = (int) Math.ceil(0.4 * 20);
                switch (count) {
                    case 1, 2 -> attack = (int) Math.ceil(0.16 * 20);
                    case 3, 4, 5, 6, 7 -> attack = (int) Math.ceil(0.28 * 20);
                    case 8 -> defaultLength = (int) Math.ceil(1.44 * 20);
                }
                return Pair.of(defaultLength, attack);
            }
    );

    public static final List<AnimatedAction> GLOVES = addTo(5, "glove", count -> {
                int defaultLength = (int) Math.ceil(0.28 * 20);
                int attack = (int) Math.ceil(0.4 * 20);
                switch (count) {
                    case 1 -> attack = (int) Math.ceil(0.28 * 20);
                    case 2, 3 -> attack = (int) Math.ceil(0.24 * 20);
                    case 4 -> {
                        attack = (int) Math.ceil(0.4 * 20);
                        defaultLength = (int) Math.ceil(0.52 * 20);
                    }
                    case 5 -> defaultLength = (int) Math.ceil(1.32 * 20);
                }
                return Pair.of(defaultLength, attack);
            }
    );

    public static final AnimatedAction STAFF = addTo(AnimatedAction.builder((int) Math.ceil(0.6 * 20) + 1, "staff").marker(9).build());
    public static final AnimatedAction STAFF_USE = addTo(new AnimatedAction(16 + 1, 4, "staff_use"));

    public static final AnimatedAction POWER_WAVE = addTo(new AnimatedAction(length(0.64), 0.32, "power_wave"));
    public static final List<AnimatedAction> DASH_SLASH = addTo(2, "dash_slash", count -> count == 2 ? Pair.of((int) Math.ceil(0.64 * 20), (int) Math.ceil(0.48 * 20))
            : Pair.of((int) Math.ceil(0.84 * 20), (int) Math.ceil(0.2 * 20))
    );
    public static final List<AnimatedAction> RUSH_ATTACK = addTo(3, "rush_attack", count -> switch (count) {
        case 2 -> Pair.of((int) Math.ceil(0.6 * 20), (int) Math.ceil(0.32 * 20));
        default -> Pair.of((int) Math.ceil(1.72 * 20), (int) Math.ceil(0.36 * 20));
    });
    public static final AnimatedAction ROUND_BREAK = addTo(new AnimatedAction(length(0.8), 0.24, "round_break"));
    public static final AnimatedAction MIND_THRUST = addTo(new AnimatedAction(length(0.96), 0.72, "mind_thrust"));
    public static final AnimatedAction BUFF = addTo(new AnimatedAction(length(1.04), 0.68, "self_buff"));
    public static final AnimatedAction TWIN_ATTACK = addTo(new AnimatedAction(length(0.56), 0.28, "twin_attack"));
    public static final List<AnimatedAction> STORM = addTo(5, "storm", count -> switch (count) {
                case 4 -> Pair.of((int) Math.ceil(0.44 * 20), (int) Math.ceil(0.36 * 20));
                case 3 -> Pair.of((int) Math.ceil(0.44 * 20), (int) Math.ceil(0.24 * 20));
                case 5 -> Pair.of((int) Math.ceil(0.76 * 20), (int) Math.ceil(0.48 * 20));
                default -> Pair.of((int) Math.ceil(0.44 * 20), (int) Math.ceil(0.44 * 20));
            }
    );
    public static final AnimatedAction GUST = addTo(new AnimatedAction(length(0.84), 0.64, "gust"));
    public static final AnimatedAction RAIL_STRIKE = addTo(new AnimatedAction(length(1.4), 0.32, "rail_strike"));
    public static final List<AnimatedAction> WIND_SLASH = addTo(2, "wind_slash", count -> switch (count) {
        case 2 -> Pair.of((int) Math.ceil(1.2 * 20), 0);
        default -> Pair.of((int) Math.ceil(1.44 * 20), (int) Math.ceil(0.24 * 20));
    });
    public static final AnimatedAction FLASH_STRIKE = addTo(new AnimatedAction(length(1.36), 0.24, "flash_strike"));
    public static final AnimatedAction DELTA_STRIKE = addTo(new AnimatedAction(length(1.08), 0.28, "delta_strike"));
    public static final AnimatedAction NAIVE_BLADE = addTo(new AnimatedAction(length(0.72), 0.12, "naive_blade"));
    public static final AnimatedAction NAIVE_BLADE_SUCCESS = addTo(new AnimatedAction(length(1.08), 0.28, "naive_blade_success"));
    public static final AnimatedAction HURRICANE = addTo(new AnimatedAction(length(1.48), 0.32, "hurricane"));
    public static final AnimatedAction REAPER_SLASH = addTo(new AnimatedAction(length(0.84), 0.24, "reaper_slash"));
    public static final AnimatedAction MILLION_STRIKE = addTo(new AnimatedAction(length(0.72), 0.32, "million_strike"));
    public static final AnimatedAction AXEL_DISASTER = addTo(new AnimatedAction(length(1.2), 0.36, "axel_disaster"));
    public static final AnimatedAction STARDUST_UPPER = addTo(new AnimatedAction(length(1.76), 0.24, "stardust_upper"));
    public static final AnimatedAction GRAND_IMPACT = addTo(new AnimatedAction(length(3.08), 0.4, "grand_impact"));
    public static final AnimatedAction TORNADO_SWING = addTo(new AnimatedAction(length(1.28), 0.24, "tornade_swing"));
    public static final AnimatedAction GIGA_SWING = addTo(new AnimatedAction(length(1), 0.28, "giga_swing"));
    public static final AnimatedAction UPPER_CUT = addTo(new AnimatedAction(length(0.72), 0.36, "upper_cut"));
    public static final AnimatedAction DOUBLE_KICK = addTo(new AnimatedAction(length(0.76), 0.24, "double_kick"));
    public static final AnimatedAction STRAIGHT_PUNCH = addTo(new AnimatedAction(length(1.08), 0.84, "straight_punch"));
    public static final AnimatedAction NEKO_DAMASHI = addTo(new AnimatedAction(length(0.6), 0.36, "neko_damashi"));
    public static final AnimatedAction RUSH_PUNCH = addTo(new AnimatedAction(length(0.96), 0.2, "rush_punch"));
    public static final AnimatedAction CYCLONE = addTo(new AnimatedAction(length(1.44), 0.28, "cyclone"));

    public static List<AnimatedAction> getAll() {
        return ImmutableList.copyOf(ALL);
    }

    private static List<AnimatedAction> addTo(int amount, String baseName, DependendAnimationValue values) {
        ImmutableList.Builder<AnimatedAction> list = new ImmutableList.Builder<>();
        for (int i = 1; i <= amount; i++) {
            Pair<Integer, Integer> val = values.getValues(i);
            AnimatedAction anim = AnimatedAction.builder(val.getFirst(), baseName + "_" + i).marker(val.getSecond()).build();
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

    private static double length(double length) {
        return length;
    }

    interface DependendAnimationValue {
        Pair<Integer, Integer> getValues(int count);
    }
}
