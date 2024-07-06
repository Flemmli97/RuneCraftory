package io.github.flemmli97.runecraftory.common.lib;

import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.PlayerPredicate;
import net.minecraft.resources.ResourceLocation;

public class LibAdvancements {

    public static final ResourceLocation ROOT = main("root");

    public static final ResourceLocation TAME_FIRST = main("taming/first");
    public static final ResourceLocation TAME_TEN = main("taming/ten");
    public static final ResourceLocation TAME_BOSS_FIRST = main("taming/boss_first");
    public static final ResourceLocation TAME_BOSS_FIVE = main("taming/boss_five");
    public static final ResourceLocation TAME_BOSS_ALL = main("taming/boss_all");

    public static final ResourceLocation SHIP_FIRST = main("shipping/first");
    public static final ResourceLocation SHIP_FIFTY = main("shipping/fifty");

    public static final ResourceLocation SHOP = main("shopping");
    public static final ResourceLocation MONEY_100K = main("money/100k");
    public static final ResourceLocation MONEY_1M = main("money/million");

    public static final ResourceLocation SKILL_5 = main("skill/skill_weapon_5");
    public static final ResourceLocation SKILL_10 = main("skill/skill_10");
    public static final ResourceLocation SKILL_25 = main("skill/skill_25");
    public static final ResourceLocation SKILL_50 = main("skill/skill_50");
    public static final ResourceLocation SKILL_100 = main("skill/skill_100");

    public static final ResourceLocation LEVEL_10 = main("level/level_10");
    public static final ResourceLocation LEVEL_25 = main("level/level_25");
    public static final ResourceLocation LEVEL_50 = main("level/level_50");
    public static final ResourceLocation LEVEL_100 = main("level/level_100");

    public static final ResourceLocation FORGE_ITEM = main("crafting/forge");
    public static final ResourceLocation CRAFT_ARMOR = main("crafting/craft");
    public static final ResourceLocation MAKE_MEDICINE = main("crafting/medicine");
    public static final ResourceLocation COOK = main("crafting/cook");

    public static final ResourceLocation UPGRADE_ITEM = main("upgrade");
    public static final ResourceLocation CHANGE_ELEMENT = main("change_element");
    public static final ResourceLocation SPELL = main("spell");
    public static final ResourceLocation CHANGE_SPELL = main("change_spell");
    public static final ResourceLocation LIGHT_ORE = main("light_ore");

    public static final ResourceLocation FERTILIZER = main("fertilizer");
    public static final ResourceLocation GIANT_CROPS = main("giant_crops");
    public static final ResourceLocation HELPER = main("monster_help");
    public static final ResourceLocation HIGH_TIER_TOOL = main("final_tool");

    public static final ResourceLocation ROOT_PROGRESSION = progression("root");
    public static final ResourceLocation CHIMERA = progression("path_1/chimera");
    public static final ResourceLocation RAFFLESIA = progression("path_1/rafflesia");
    public static final ResourceLocation GRIMOIRE = progression("path_1/grimoire");
    public static final ResourceLocation DEAD_TREE = progression("path_2/dead_tree");
    public static final ResourceLocation RACCOON = progression("path_3/raccoon");
    public static final ResourceLocation SKELEFANG = progression("path_3/skelefang");
    public static final ResourceLocation AMBROSIA = progression("path_4/ambrosia");
    public static final ResourceLocation THUNDERBOLT = progression("path_4/thunderbolt");
    public static final ResourceLocation MARIONETTA = progression("path_4/marionetta");

    private static ResourceLocation main(String id) {
        return new ResourceLocation(RuneCraftory.MODID, "main/" + id);
    }

    private static ResourceLocation progression(String id) {
        return new ResourceLocation(RuneCraftory.MODID, "progression/" + id);
    }

    public static EntityPredicate.Builder playerAdvancementCheck(ResourceLocation advancement) {
        return EntityPredicate.Builder.entity().player(PlayerPredicate.Builder.player().checkAdvancementDone(advancement, true).build());
    }
}
