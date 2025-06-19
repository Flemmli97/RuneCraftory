package io.github.flemmli97.runecraftory.common.lib;

import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.resources.ResourceLocation;

public class LibConstants {

    public static final int BASE_LEVEL = 1;
    public static final int MAX_MONSTER_LEVEL = 10000;

    public static final ResourceLocation MAX_HEALTH_MODIFIER = RuneCraftory.modRes("max_health_modifier");
    public static final ResourceLocation MAX_HEALTH_ITEM_INCREASE = RuneCraftory.modRes("max_health_item_modifier");

    public static final ResourceLocation FOOD_MODIFIER = RuneCraftory.modRes("player_food_modifier");

    public static final ResourceLocation ATTRIBUTE_LEVEL_MOD = RuneCraftory.modRes("level_increase_modifier");
    public static final ResourceLocation ATTRIBUTE_BRUSH_MOD = RuneCraftory.modRes("brush_attribute_modifier");
    public static final ResourceLocation ATTRIBUTE_FRIEND_MOD = RuneCraftory.modRes("friendship_modifier");
    public static final ResourceLocation MONSTER_ITEM_BONUS = RuneCraftory.modRes("monster_gift_modifier");
    public static final ResourceLocation FOOD_UUID = RuneCraftory.modRes("mob_food_modifier");
    public static final ResourceLocation FOOD_UUID_MULTI = RuneCraftory.modRes("mob_food_modifier_multiplier");

    public static final ResourceLocation STEP_UP_TEMP = RuneCraftory.modRes("step_buff_temp");

    public static final ResourceLocation[] EQUIPMENT_MODIFIERS = new ResourceLocation[]{RuneCraftory.modRes("a86e87f5-2f4a-4105-9d8b-1f29fed2f67e"),
            RuneCraftory.modRes("0f1dc59e-c82b-4765-a651-de69152440fc"),
            RuneCraftory.modRes("bb429d9c-ade1-4fe8-b914-5f1196ad9fa6"),
            RuneCraftory.modRes("88969886-531e-4141-9087-ef8340f8216c"),
            RuneCraftory.modRes("6fec8873-50b8-40d5-b8b9-94e06ef68c11"),
            RuneCraftory.modRes("fea154ab-3e7c-4d01-9e4e-18e515d37e57")};
}
