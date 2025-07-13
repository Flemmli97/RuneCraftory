package io.github.flemmli97.runecraftory.api.registry.action;

import com.google.common.collect.ImmutableList;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;

import java.util.List;

/**
 * All relevant player model animations. NPCs also use them
 */
public class PlayerModelAnimations {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();

    public static final List<String> SHORT_SWORD = addTo(6, "short_sword", count -> switch (count) {
        case 2 -> AnimationsBuilder.definition(0.52)
                .marker("attack", 0.32).marker("step", 0.24).marker("done", 0.4);
        case 3 -> AnimationsBuilder.definition(0.52)
                .marker("attack", 0.4).marker("step", 0.24).marker("done", 0.4);
        case 4 -> AnimationsBuilder.definition(0.48)
                .marker("attack", 0.28).marker("step", 0.28).marker("done", 0.36);
        case 5 -> AnimationsBuilder.definition(0.48)
                .marker("attack", 0.36).marker("step", 0.16).marker("done", 0.36);
        case 6 -> AnimationsBuilder.definition(1.68).marker("spin_start", 0.28).marker("spin_end", 1.04)
                .marker("reset", 0.48, 0.72);
        default -> AnimationsBuilder.definition(0.52)
                .marker("attack", 0.36).marker("step", 0.28).marker("done", 0.4);
    });
    public static final String SHORT_SWORD_USE = BUILDER.add("short_sword_use", AnimationsBuilder.definition(0.8)
            .marker("attack", 0.4));

    public static final List<String> LONG_SWORD = addTo(4, "long_sword", count -> switch (count) {
        case 2 -> AnimationsBuilder.definition(0.76)
                .marker("attack", 0.56).marker("step", 0.44).marker("done", 0.64);
        case 3 -> AnimationsBuilder.definition(0.76)
                .marker("attack", 0.64).marker("step", 0.36).marker("done", 0.64);
        case 4 -> AnimationsBuilder.definition(1.64).marker("spin_start", 0.2).marker("spin_end", 1.56)
                .marker("reset", 0.72);
        default -> AnimationsBuilder.definition(0.76)
                .marker("attack", 0.52).marker("done", 0.64);
    });
    public static final String LONG_SWORD_USE = BUILDER.add("long_sword_use", AnimationsBuilder.definition(0.72)
            .marker("attack", 0.44));

    public static final List<String> SPEAR = addTo(5, "spear", count -> switch (count) {
        case 2 -> AnimationsBuilder.definition(0.6)
                .marker("attack", 0.36).marker("step", 0.36).marker("done", 0.48);
        case 5 -> AnimationsBuilder.definition(2.24).marker("spin_start", 0.2).marker("spin_end", 1.32)
                .marker("reset", 0.56, 0.92).marker("leap", 1.48).marker("leap_end", 1.84).marker("slam", 1.96);
        default -> AnimationsBuilder.definition(0.6)
                .marker("attack", 0.44).marker("step", 0.48).marker("done", 0.48);
    });
    public static final String SPEAR_USE = BUILDER.add("spear_use", AnimationsBuilder.definition(1.28)
            .marker("attack", 0.36).marker("end_continue", 0.52).marker("final", 1.04)
            .marker("chain_offset", 0.25));

    public static final List<String> HAMMER_AXE = addTo(3, "hammer_axe", count ->
            count == 3 ? AnimationsBuilder.definition(1.64).marker("spin_start", 0.44).marker("spin_middle", 0.96).marker("spin_end", 1.52)
                    .marker("reset", 0.96).marker("leap", 0.32)
                    : AnimationsBuilder.definition(0.92)
                    .marker("attack", 0.8).marker("done", 0.8)
    );
    public static final String HAMME_AXE_USE = BUILDER.add("hammer_axe_use", AnimationsBuilder.definition(1.12)
            .marker("attack", 0.76).marker("jump", 0.36));

    public static final List<String> DUAL_BLADES = addTo(8, "dual_blades", count -> switch (count) {
        case 2, 3, 4 -> AnimationsBuilder.definition(0.44)
                .marker("attack", 0.32).marker("step", 0.28).marker("done", 0.32);
        case 5 -> AnimationsBuilder.definition(0.44)
                .marker("spin_start", 0.08).marker("spin_end", 0.32)
                .marker("step", 0.24).marker("done", 0.32);
        case 6 -> AnimationsBuilder.definition(0.44)
                .marker("spin_start", 0.05).marker("spin_end", 0.32).marker("reset", 0.16)
                .marker("step", 0.24).marker("done", 0.32);
        case 7 -> AnimationsBuilder.definition(0.52)
                .marker("leap", 0.12).marker("down", 0.24).marker("attack", 0.28).marker("done", 0.4);
        case 8 -> AnimationsBuilder.definition(1.72).marker("spin_start", 0.28).marker("spin_end", 1.28)
                .marker("reset", 0.6, 0.84, 1.08).marker("last", 1.08);
        default -> AnimationsBuilder.definition(0.44)
                .marker("attack", 0.32).marker("step", 0.24).marker("done", 0.32);
    });
    public static final String DUAL_BLADES_USE = BUILDER.add("dual_blades_use", AnimationsBuilder.definition(0.76)
            .marker("attack", 0.36));

    public static final List<String> GLOVES = addTo(5, "glove", count -> switch (count) {
        case 4 -> AnimationsBuilder.definition(0.76)
                .marker("jump", 0.24).marker("down", 0.4).marker("attack", 0.56);
        case 5 -> AnimationsBuilder.definition(1.32)
                .marker("leap", 0.16)
                .marker("attack_start", 0.24).marker("attack_end", 0.88);
        default -> AnimationsBuilder.definition(0.48)
                .marker("attack", 0.36).marker("step", 0.28).marker("done", 0.36);
    });
    public static final String GLOVES_USE = BUILDER.add("glove_use", AnimationsBuilder.definition(1.68)
            .marker("attack_start", 0.24).marker("attack_end", 1.4)
            .marker("reset", 0.44, 0.6, 0.76, 0.92, 1.08, 1.24, 1.32));

    public static final List<String> STAFF = addTo(2, "staff", count ->
            count == 2 ? AnimationsBuilder.definition(0.72)
                    .marker("attack", 0.52)
                    : AnimationsBuilder.definition(0.84)
                    .marker("attack", 0.72).marker("done", 0.72)
    );
    public static final String STAFF_USE = BUILDER.add("staff_use", AnimationsBuilder.definition(0.88).marker("attack", 0.52));

    public static final String WATER_LASER_ONE = BUILDER.add("water_laser_one", AnimationsBuilder.definition(2.92)
            .marker("attack", 0.52).marker("continue", 0.67));
    public static final String WATER_LASER_TWO = BUILDER.add("water_laser_two", AnimationsBuilder.definition(1.72)
            .marker("attack", 0.52).marker("continue", 0.67));
    public static final String WATER_LASER_THREE = BUILDER.add("water_laser_three", AnimationsBuilder.definition(1.12)
            .marker("attack", 0.52).marker("continue", 0.67));
    public static final String WATER_LASER_END = BUILDER.add("water_laser_end", AnimationsBuilder.definition(0.28));

    public static final String POWER_WAVE = BUILDER.add("power_wave", AnimationsBuilder.definition(0.72)
            .marker("attack", 0.32).marker("step", 0.32));
    public static final List<String> DASH_SLASH = addTo(2, "dash_slash", count ->
            count == 2 ? AnimationsBuilder.definition(0.64).marker("attack", 0.44)
                    : AnimationsBuilder.definition(0.96)
                    .marker("move_start", 0.28).marker("sound", 0.44)
                    .marker("attack_start", 0.32).marker("attack_end", 0.8)
    );
    public static final List<String> RUSH_ATTACK = addTo(2, "rush_attack", count ->
            count == 2 ? AnimationsBuilder.definition(0.72)
                    .marker("attack_start", 0.2).marker("attack_end", 0.52)
                    .marker("leap", 0.16)
                    : AnimationsBuilder.definition(1.96)
                    .marker("attack", 0.4, 0.56, 1.08)
                    .marker("chain_1_start", 0.4).marker("chain_1_end", 0.84)
                    .marker("chain_2_start", 1.32).marker("chain_2_end", 1.6)
                    .marker("step", 0.36, 0.56).marker("jump", 0.96)
    );
    public static final String ROUND_BREAK = BUILDER.add("round_break", AnimationsBuilder.definition(1.12)
            .marker("attack_start", 0.28).marker("attack_end", 0.88));
    public static final String MIND_THRUST = BUILDER.add("mind_thrust", AnimationsBuilder.definition(1.08)
            .marker("attack", 0.84).marker("step", 0.72));
    public static final String BUFF = BUILDER.add("self_buff", AnimationsBuilder.definition(1.08).marker("attack", 0.64));
    public static final String TWIN_ATTACK = BUILDER.add("twin_attack", AnimationsBuilder.definition(0.68).marker("attack", 0.4));
    public static final List<String> STORM = addTo(5, "storm", count -> switch (count) {
        case 2 -> AnimationsBuilder.definition(0.56)
                .marker("attack", 0.44).marker("move", 0.32).marker("done", 0.44);
        case 3 -> AnimationsBuilder.definition(0.52)
                .marker("attack", 0.24).marker("move", 0.24).marker("done", 0.32);
        case 4 -> AnimationsBuilder.definition(0.56)
                .marker("attack", 0.44).marker("up", 0.08).marker("down", 0.24).marker("done", 0.44);
        case 5 -> AnimationsBuilder.definition(0.72)
                .marker("attack", 0.44).marker("up", 0.08).marker("down", 0.36);
        default -> AnimationsBuilder.definition(0.56)
                .marker("attack", 0.44).marker("move", 0.12).marker("done", 0.44);
    });
    public static final String GUST = BUILDER.add("gust", AnimationsBuilder.definition(0.92)
            .marker("attack", 0.6).marker("jump", 0.12));
    public static final String RAIL_STRIKE = BUILDER.add("rail_strike", AnimationsBuilder.definition(1.56)
            .marker("attack_start", 0.4).marker("attack_end", 1.12)
            .marker("move_1", 0.2).marker("move_2", 0.72).marker("move_end", 1.24)
            .marker("reset", 0.56, 0.84, 1));
    public static final List<String> WIND_SLASH = addTo(2, "wind_slash", count ->
            count == 2 ? AnimationsBuilder.definition(1.44).marker("spin_start", 0).marker("spin_end", 1.08)
                    .marker("reset", 0.56)
                    : AnimationsBuilder.definition(1.76).marker("spin_start", 0.36).marker("spin_end", 1.4)
                    .marker("reset", 0.88).marker("chain_start", 1.08).marker("leap", 0.28)
    );
    public static final String FLASH_STRIKE = BUILDER.add("flash_strike", AnimationsBuilder.definition(1.6)
            .marker("attack_start", 0.32).marker("attack_end", 1.04).marker("reset", 0.56, 0.8)
            .marker("move_1", 0.36).marker("move_2", 0.48).marker("move_end", 1.16));
    public static final String DELTA_STRIKE = BUILDER.add("delta_strike", AnimationsBuilder.definition(1.32)
            .marker("attack", 0.36, 0.6, 1.12).marker("step", 0.32, 1.04));
    public static final String NAIVE_BLADE = BUILDER.add("naive_blade", AnimationsBuilder.definition(0.84).marker("prepared", 0.12));
    public static final String NAIVE_BLADE_SUCCESS = BUILDER.add("naive_blade_success", AnimationsBuilder.definition(1.36)
            .marker("attack_1", 0.2).marker("attack_2", 1.04).marker("jump", 0.64));
    public static final String HURRICANE = BUILDER.add("hurricane", AnimationsBuilder.definition(2.08)
            .marker("attack", 0.32)
            .marker("attack_start", 0.52).marker("attack_end", 1.64).marker("reset", 0.88, 1.24));
    public static final String REAPER_SLASH = BUILDER.add("reaper_slash", AnimationsBuilder.definition(0.92)
            .marker("attack_start", 0.28).marker("attack_middle", 0.48).marker("attack_end", 0.68));
    public static final String MILLION_STRIKE = BUILDER.add("million_strike", AnimationsBuilder.definition(1.04)
            .marker("attack", 0.48, 0.58, 0.68, 0.78).marker("attack_crit", 0.78));
    public static final String AXEL_DISASTER = BUILDER.add("axel_disaster", AnimationsBuilder.definition(1.44)
            .marker("move_1", 0.28).marker("move_2", 0.44).marker("move_3", 1.04).marker("move_done", 1.24)
            .marker("attack_start", 0.44).marker("attack_end", 1.08)
            .marker("move_start", 0.32).marker("move_end", 1.2));
    public static final String STARDUST_UPPER = BUILDER.add("stardust_upper", AnimationsBuilder.definition(1.76)
            .marker("attack_start_1", 0.16).marker("attack_end_1", 0.84)
            .marker("attack_start_2", 0.84).marker("attack_end_2", 1.52));
    public static final String GRAND_IMPACT = BUILDER.add("grand_impact", AnimationsBuilder.definition(3.32)
            .marker("attack_1", 0.76).marker("attack_2", 1.52)
            .marker("combo_end", 1));
    public static final String TORNADO_SWING = BUILDER.add("tornado_swing", AnimationsBuilder.definition(1.6)
            .marker("attack_start_1", 0.2).marker("attack_end_1", 1.04)
            .marker("attack_start_2", 1.04).marker("attack_end_2", 1.4)
            .marker("chain_offset", 0.28)
    );
    public static final String GIGA_SWING = BUILDER.add("giga_swing", AnimationsBuilder.definition(1.08)
            .marker("attack_start", 0.28).marker("attack_end", 0.52));
    public static final String UPPER_CUT = BUILDER.add("upper_cut", AnimationsBuilder.definition(0.8).marker("attack", 0.36));
    public static final String DOUBLE_KICK = BUILDER.add("double_kick", AnimationsBuilder.definition(1.04)
            .marker("attack_start", 0.2).marker("attack_end", 0.76).marker("reset", 0.52)
            .marker("step", 0.44));
    public static final String STRAIGHT_PUNCH = BUILDER.add("straight_punch", AnimationsBuilder.definition(1.16).marker("attack", 0.92));
    public static final String NEKO_DAMASHI = BUILDER.add("neko_damashi", AnimationsBuilder.definition(0.76).marker("attack", 0.52));
    public static final String RUSH_PUNCH = BUILDER.add("rush_punch", AnimationsBuilder.definition(1.16)
            .marker("attack", 0.52, 0.68, 0.84, 1).marker("attack_crit", 1.16));
    public static final String CYCLONE = BUILDER.add("cyclone", AnimationsBuilder.definition(1.44)
            .marker("attack_start", 0.28).marker("attack_end", 1.24).marker("reset", 0.44, 0.64, 0.88, 1.08));
    public static final String RAPID_MOVE = BUILDER.add("rapid_move", AnimationsBuilder.definition(0.8).marker("attack", 0.72));

    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private static List<String> addTo(int amount, String baseName, DependendAnimationBuilder values) {
        ImmutableList.Builder<String> list = new ImmutableList.Builder<>();
        for (int i = 1; i <= amount; i++) {
            list.add(BUILDER.add(baseName + "_" + i, values.apply(i)));
        }
        return list.build();
    }

    interface DependendAnimationBuilder {

        AnimationsBuilder.DefinitionBuilder apply(int count);
    }
}
