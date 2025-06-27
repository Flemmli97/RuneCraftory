package io.github.flemmli97.runecraftory.common.lib;

import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;

import java.util.Map;

public class LibConstants {

    public static final int BASE_LEVEL = 1;
    public static final int MAX_MONSTER_LEVEL = 10000;

    public static final ResourceLocation PLAYER_CONFIG_MODIFIER = RuneCraftory.modRes("player_config_modifier");
    public static final ResourceLocation PLAYER_LEVEL_MODIFIER = RuneCraftory.modRes("player_level_modifier");
    public static final ResourceLocation PLAYER_SKILL_LEVEL_MODIFIER = RuneCraftory.modRes("player_skill_level_modifier");
    public static final ResourceLocation PLAYER_STAT_BOOST_ITEM_INCREASE = RuneCraftory.modRes("player_stat_boost_item_increase");

    public static final ResourceLocation FOOD_MODIFIER = RuneCraftory.modRes("food_modifier");
    public static final ResourceLocation FOOD_MODIFIER_MULTI = RuneCraftory.modRes("food_modifier_multiplier");

    public static final ResourceLocation MONSTER_LEVEL_MODIFIER = RuneCraftory.modRes("monster_level_modifier");
    public static final ResourceLocation MONSTER_BRUSH_MODIFIER = RuneCraftory.modRes("monster_brush_modifier");
    public static final ResourceLocation FRIENDSHIP_MODIFIER = RuneCraftory.modRes("friendship_modifier");
    public static final ResourceLocation MONSTER_GIFT_MODIFIER = RuneCraftory.modRes("monster_gift_modifier");

    public static final ResourceLocation STEP_UP_TEMP = RuneCraftory.modRes("step_up_temp");

    public static final Map<EquipmentSlot, ResourceLocation> EQUIPMENT_MODIFIERS = Map.of(
            EquipmentSlot.MAINHAND, RuneCraftory.modRes("equipment_mainhand_modifier"),
            EquipmentSlot.OFFHAND, RuneCraftory.modRes("equipment_offhand_modifier"),
            EquipmentSlot.HEAD, RuneCraftory.modRes("equipment_head_modifier"),
            EquipmentSlot.CHEST, RuneCraftory.modRes("equipment_chest_modifier"),
            EquipmentSlot.LEGS, RuneCraftory.modRes("equipment_legs_modifier"),
            EquipmentSlot.FEET, RuneCraftory.modRes("equipment_feet_modifier"));
    public static final ResourceLocation SHIELD_PENALTY = RuneCraftory.modRes("shield_penalty");
}
