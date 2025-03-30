package io.github.flemmli97.runecraftory.api.action;

import com.google.common.collect.ImmutableList;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;

import java.util.ArrayList;
import java.util.List;

/**
 * All relevant player model animations. Here since NPC also use them
 */
public class PlayerModelAnimations {

    private static final List<AnimatedAction> ALL = new ArrayList<>();

    public static final List<AnimatedAction> SHORT_SWORD = addTo(6, "short_sword", (count, builder) -> switch (count) {
        case 2 -> builder.apply(0.52)
                .marker("attack", 0.32).marker("step", 0.24).marker("done", 0.4).build();
        case 3 -> builder.apply(0.52)
                .marker("attack", 0.4).marker("step", 0.24).marker("done", 0.4).build();
        case 4 -> builder.apply(0.48)
                .marker("attack", 0.28).marker("step", 0.28).marker("done", 0.36).build();
        case 5 -> builder.apply(0.48)
                .marker("attack", 0.36).marker("step", 0.16).marker("done", 0.36).build();
        case 6 -> builder.apply(1.68).marker("spin_start", 0.28).marker("spin_end", 1.04)
                .marker("reset", 0.48, 0.72).build();
        default -> builder.apply(0.52)
                .marker("attack", 0.36).marker("step", 0.28).marker("done", 0.4).build();
    });
    public static final AnimatedAction SHORT_SWORD_USE = addTo(AnimatedAction.builder(0.8, "short_sword_use")
            .marker("attack", 0.4).build());

    public static final List<AnimatedAction> LONG_SWORD = addTo(4, "long_sword", (count, builder) -> switch (count) {
        case 2 -> builder.apply(0.76)
                .marker("attack", 0.56).marker("step", 0.44).marker("done", 0.64).build();
        case 3 -> builder.apply(0.76)
                .marker("attack", 0.64).marker("step", 0.36).marker("done", 0.64).build();
        case 4 -> builder.apply(1.64).marker("spin_start", 0.2).marker("spin_end", 1.56)
                .marker("reset", 0.72).build();
        default -> builder.apply(0.76)
                .marker("attack", 0.52).marker("done", 0.64).build();
    });
    public static final AnimatedAction LONG_SWORD_USE = addTo(AnimatedAction.builder(0.72, "long_sword_use")
            .marker("attack", 0.44).build());

    public static final List<AnimatedAction> SPEAR = addTo(5, "spear", (count, builder) -> switch (count) {
        case 2 -> builder.apply(0.6)
                .marker("attack", 0.36).marker("step", 0.36).marker("done", 0.48).build();
        case 5 -> builder.apply(2.24).marker("spin_start", 0.2).marker("spin_end", 1.32)
                .marker("reset", 0.56, 0.92).marker("leap", 1.48).marker("leap_end", 1.84).marker("slam", 1.96).build();
        default -> builder.apply(0.6)
                .marker("attack", 0.44).marker("step", 0.48).marker("done", 0.48).build();
    });
    public static final AnimatedAction SPEAR_USE = addTo(AnimatedAction.builder(1.28, "spear_use")
            .marker("attack", 0.36).marker("end_continue", 0.52).marker("final", 1.04)
            .marker("chain_offset", 0.25).build());

    public static final List<AnimatedAction> HAMMER_AXE = addTo(3, "hammer_axe", (count, builder) ->
            count == 3 ? builder.apply(1.64).marker("spin_start", 0.44).marker("spin_middle", 0.96).marker("spin_end", 1.52)
                    .marker("reset", 0.96).marker("leap", 0.32).build()
                    : builder.apply(0.92)
                    .marker("attack", 0.8).marker("done", 0.8).build()
    );
    public static final AnimatedAction HAMME_AXE_USE = addTo(AnimatedAction.builder(1.12, "hammer_axe_use")
            .marker("attack", 0.76).marker("jump", 0.36).build());

    public static final List<AnimatedAction> DUAL_BLADES = addTo(8, "dual_blades", (count, builder) -> switch (count) {
        case 2, 3, 4 -> builder.apply(0.44)
                .marker("attack", 0.32).marker("step", 0.28).marker("done", 0.32).build();
        case 5 -> builder.apply(0.44)
                .marker("spin_start", 0.08).marker("spin_end", 0.32)
                .marker("step", 0.24).marker("done", 0.32).build();
        case 6 -> builder.apply(0.44)
                .marker("spin_start", 0.05).marker("spin_end", 0.32).marker("reset", 0.16)
                .marker("step", 0.24).marker("done", 0.32).build();
        case 7 -> builder.apply(0.52)
                .marker("leap", 0.12).marker("down", 0.24).marker("attack", 0.28).marker("done", 0.4).build();
        case 8 -> builder.apply(1.72).marker("spin_start", 0.28).marker("spin_end", 1.28)
                .marker("reset", 0.6, 0.84, 1.08).marker("last", 1.08).build();
        default -> builder.apply(0.44)
                .marker("attack", 0.32).marker("step", 0.24).marker("done", 0.32).build();
    });
    public static final AnimatedAction DUAL_BLADES_USE = addTo(AnimatedAction.builder(0.76, "dual_blades_use")
            .marker("attack", 0.36).build());

    public static final List<AnimatedAction> GLOVES = addTo(5, "glove", (count, builder) -> switch (count) {
        case 4 -> builder.apply(0.76)
                .marker("jump", 0.24).marker("down", 0.4).marker("attack", 0.56).build();
        case 5 -> builder.apply(1.64)
                .marker("move_start", 0.16).marker("move_end", 1.24)
                .marker("attack_start", 0.24).marker("attack_end", 1.16).build();
        default -> builder.apply(0.48)
                .marker("attack", 0.36).marker("step", 0.28).marker("done", 0.36).build();
    });
    public static final AnimatedAction GLOVES_USE = addTo(AnimatedAction.builder(1.68, "glove_use")
            .marker("attack_start", 0.24).marker("attack_end", 1.4)
            .marker("reset", 0.44, 0.6, 0.76, 0.92, 1.08, 1.24, 1.32).build());

    public static final List<AnimatedAction> STAFF = addTo(2, "staff", (count, builder) ->
            count == 2 ? builder.apply(0.72)
                    .marker("attack", 0.52).build()
                    : builder.apply(0.84)
                    .marker("attack", 0.72).marker("done", 0.72).build()
    );
    public static final AnimatedAction STAFF_USE = addTo(AnimatedAction.builder(0.88, "staff_use").marker("attack", 0.52).build());

    public static final AnimatedAction WATER_LASER_ONE = addTo(AnimatedAction.builder(2.92, "water_laser_one")
            .marker("attack", 0.52).marker("continue", 0.67).build());
    public static final AnimatedAction WATER_LASER_TWO = addTo(AnimatedAction.builder(1.72, "water_laser_two")
            .marker("attack", 0.52).marker("continue", 0.67).build());
    public static final AnimatedAction WATER_LASER_THREE = addTo(AnimatedAction.builder(1.12, "water_laser_three")
            .marker("attack", 0.52).marker("continue", 0.67).build());
    public static final AnimatedAction WATER_LASER_END = addTo(AnimatedAction.builder(0.28, "water_laser_end").build());

    public static final AnimatedAction POWER_WAVE = addTo(AnimatedAction.builder(0.72, "power_wave")
            .marker("attack", 0.32).marker("step", 0.32).build());
    public static final List<AnimatedAction> DASH_SLASH = addTo(2, "dash_slash", (count, builder) ->
            count == 2 ? builder.apply(0.64).marker("attack", 0.44).build()
                    : builder.apply(0.96)
                    .marker("move_start", 0.28).marker("sound", 0.44)
                    .marker("attack_start", 0.32).marker("attack_end", 0.8).build()
    );
    public static final List<AnimatedAction> RUSH_ATTACK = addTo(2, "rush_attack", (count, builder) ->
            count == 2 ? builder.apply(0.72)
                    .marker("attack_start", 0.2).marker("attack_end", 0.52)
                    .marker("leap", 0.16).build()
                    : builder.apply(1.96)
                    .marker("attack", 0.4, 0.56, 1.08)
                    .marker("chain_1_start", 0.4).marker("chain_1_end", 0.84)
                    .marker("chain_2_start", 1.32).marker("chain_2_end", 1.6)
                    .marker("step", 0.36, 0.56).marker("jump", 0.96).build()
    );
    public static final AnimatedAction ROUND_BREAK = addTo(AnimatedAction.builder(1.12, "round_break")
            .marker("attack_start", 0.28).marker("attack_end", 0.88).build());
    public static final AnimatedAction MIND_THRUST = addTo(AnimatedAction.builder(1.08, "mind_thrust")
            .marker("attack", 0.84).marker("step", 0.72).build());
    public static final AnimatedAction BUFF = addTo(AnimatedAction.builder(1.08, "self_buff").marker("attack", 0.64).build());
    public static final AnimatedAction TWIN_ATTACK = addTo(AnimatedAction.builder(0.68, "twin_attack").marker("attack", 0.4).build());
    public static final List<AnimatedAction> STORM = addTo(5, "storm", (count, builder) -> switch (count) {
                case 2 -> builder.apply(0.56)
                        .marker("attack", 0.44).marker("move", 0.32).marker("done", 0.44).build();
                case 3 -> builder.apply(0.52)
                        .marker("attack", 0.24).marker("move", 0.24).marker("done", 0.32).build();
                case 4 -> builder.apply(0.56)
                        .marker("attack", 0.44).marker("up", 0.08).marker("down", 0.24).marker("done", 0.44).build();
                case 5 -> builder.apply(0.72)
                        .marker("attack", 0.44).marker("up", 0.08).marker("down", 0.36).build();
                default -> builder.apply(0.56)
                        .marker("attack", 0.44).marker("move", 0.12).marker("done", 0.44).build();
            }
    );
    public static final AnimatedAction GUST = addTo(AnimatedAction.builder(0.92, "gust")
            .marker("attack", 0.6).marker("jump", 0.12).build());
    public static final AnimatedAction RAIL_STRIKE = addTo(AnimatedAction.builder(1.56, "rail_strike")
            .marker("attack_start", 0.4).marker("attack_end", 1.12)
            .marker("move_1", 0.2).marker("move_2", 0.72).marker("move_end", 1.24)
            .marker("reset", 0.56, 0.84, 1).build());
    public static final List<AnimatedAction> WIND_SLASH = addTo(2, "wind_slash", (count, builder) ->
            count == 2 ? builder.apply(1.44).marker("spin_start", 0).marker("spin_end", 1.08)
                    .marker("reset", 0.56).build()
                    : builder.apply(1.76).marker("spin_start", 0.36).marker("spin_end", 1.4)
                    .marker("reset", 0.88).marker("chain_start", 1.08).marker("leap", 0.28).build()
    );
    public static final AnimatedAction FLASH_STRIKE = addTo(AnimatedAction.builder(1.6, "flash_strike")
            .marker("attack_start", 0.32).marker("attack_end", 1.04).marker("reset", 0.56, 0.8)
            .marker("move_1", 0.36).marker("move_2", 0.48).marker("move_end", 1.16).build());
    public static final AnimatedAction DELTA_STRIKE = addTo(AnimatedAction.builder(1.32, "delta_strike")
            .marker("attack", 0.36, 0.6, 1.12).marker("step", 0.32, 1.04).build());
    public static final AnimatedAction NAIVE_BLADE = addTo(AnimatedAction.builder(0.84, "naive_blade").marker("prepared", 0.12).build());
    public static final AnimatedAction NAIVE_BLADE_SUCCESS = addTo(AnimatedAction.builder(1.36, "naive_blade_success")
            .marker("attack_1", 0.2).marker("attack_2", 1.04).marker("jump", 0.64).build());
    public static final AnimatedAction HURRICANE = addTo(AnimatedAction.builder(2.08, "hurricane")
            .marker("attack", 0.32)
            .marker("attack_start", 0.52).marker("attack_end", 1.64).marker("reset", 0.88, 1.24).build());
    public static final AnimatedAction REAPER_SLASH = addTo(AnimatedAction.builder(0.92, "reaper_slash")
            .marker("attack_start", 0.28).marker("attack_middle", 0.48).marker("attack_end", 0.68).build());
    public static final AnimatedAction MILLION_STRIKE = addTo(AnimatedAction.builder(1.04, "million_strike")
            .marker("attack", 0.48, 0.58, 0.68, 0.78).marker("attack_crit", 0.78).build());
    public static final AnimatedAction AXEL_DISASTER = addTo(AnimatedAction.builder(1.44, "axel_disaster")
            .marker("move_1", 0.28).marker("move_2", 0.44).marker("move_3", 1.04).marker("move_done", 1.24)
            .marker("attack_start", 0.44).marker("attack_end", 1.08)
            .marker("move_start", 0.32).marker("move_end", 1.2).build());
    public static final AnimatedAction STARDUST_UPPER = addTo(AnimatedAction.builder(1.76, "stardust_upper")
            .marker("attack_start_1", 0.16).marker("attack_end_1", 0.84)
            .marker("attack_start_2", 0.84).marker("attack_end_2", 1.52).build());
    public static final AnimatedAction GRAND_IMPACT = addTo(AnimatedAction.builder(3.32, "grand_impact")
            .marker("attack_1", 0.76).marker("attack_2", 1.52)
            .marker("combo_end", 1).build());
    public static final AnimatedAction TORNADO_SWING = addTo(AnimatedAction.builder(1.6, "tornado_swing")
            .marker("attack_start_1", 0.2).marker("attack_end_1", 1.04)
            .marker("attack_start_2", 1.04).marker("attack_end_2", 1.4)
            .marker("chain_offset", 0.28).build()
    );
    public static final AnimatedAction GIGA_SWING = addTo(AnimatedAction.builder(1.08, "giga_swing")
            .marker("attack_start", 0.28).marker("attack_end", 0.52).build());
    public static final AnimatedAction UPPER_CUT = addTo(AnimatedAction.builder(0.8, "upper_cut").marker("attack", 0.36).build());
    public static final AnimatedAction DOUBLE_KICK = addTo(AnimatedAction.builder(1.04, "double_kick")
            .marker("attack_start", 0.2).marker("attack_end", 0.76).marker("reset", 0.52)
            .marker("step", 0.44).build());
    public static final AnimatedAction STRAIGHT_PUNCH = addTo(AnimatedAction.builder(1.16, "straight_punch").marker("attack", 0.92).build());
    public static final AnimatedAction NEKO_DAMASHI = addTo(AnimatedAction.builder(0.76, "neko_damashi").marker("attack", 0.52).build());
    public static final AnimatedAction RUSH_PUNCH = addTo(AnimatedAction.builder(1.16, "rush_punch")
            .marker("attack", 0.52, 0.68, 0.84, 1).marker("attack_crit", 1.16).build());
    public static final AnimatedAction CYCLONE = addTo(AnimatedAction.builder(1.44, "cyclone")
            .marker("attack_start", 0.28).marker("attack_end", 1.24).marker("reset", 0.44, 0.64, 0.88, 1.08).build());
    public static final AnimatedAction RAPID_MOVE = addTo(AnimatedAction.builder(0.8, "rapid_move").marker("attack", 0.72).build());

    public static List<AnimatedAction> getAll() {
        return ImmutableList.copyOf(ALL);
    }

    private static List<AnimatedAction> addTo(int amount, String baseName, DependendAnimationValue values) {
        ImmutableList.Builder<AnimatedAction> list = new ImmutableList.Builder<>();
        for (int i = 1; i <= amount; i++) {
            int indx = i;
            AnimatedAction anim = values.apply(indx, length -> AnimatedAction.builder(length, baseName + "_" + indx));
            ALL.add(anim);
            list.add(anim);
        }
        return list.build();
    }

    private static AnimatedAction addTo(AnimatedAction anim) {
        ALL.add(anim);
        return anim;
    }

    interface DependendAnimationValue {
        AnimatedAction apply(int count, DependendAnimationBuilder builder);
    }

    interface DependendAnimationBuilder {
        AnimatedAction.Builder apply(double length);
    }
}
