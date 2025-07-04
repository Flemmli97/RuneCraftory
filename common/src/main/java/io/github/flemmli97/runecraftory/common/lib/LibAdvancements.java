package io.github.flemmli97.runecraftory.common.lib;

import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.PlayerPredicate;
import net.minecraft.resources.ResourceLocation;

public class LibAdvancements {

    public static final ResourceLocation ROOT = mainLine("root");

    public static final ResourceLocation TAME_FIRST = mainLine("taming/first");
    public static final ResourceLocation TAME_TEN = mainLine("taming/ten");
    public static final ResourceLocation TAME_BOSS_FIRST = mainLine("taming/boss_first");
    public static final ResourceLocation TAME_BOSS_FIVE = mainLine("taming/boss_five");
    public static final ResourceLocation TAME_BOSS_ALL = mainLine("taming/boss_all");

    public static final ResourceLocation SHIP_FIRST = mainLine("shipping/first");
    public static final ResourceLocation SHIP_FIFTY = mainLine("shipping/fifty");

    public static final ResourceLocation SHOP = mainLine("shopping");
    public static final ResourceLocation MONEY_100K = mainLine("money/100k");
    public static final ResourceLocation MONEY_1M = mainLine("money/million");

    public static final ResourceLocation SKILL_5 = mainLine("skill/skill_weapon_5");
    public static final ResourceLocation SKILL_10 = mainLine("skill/skill_10");
    public static final ResourceLocation SKILL_25 = mainLine("skill/skill_25");
    public static final ResourceLocation SKILL_50 = mainLine("skill/skill_50");
    public static final ResourceLocation SKILL_100 = mainLine("skill/skill_100");

    public static final ResourceLocation LEVEL_10 = mainLine("level/level_10");
    public static final ResourceLocation LEVEL_25 = mainLine("level/level_25");
    public static final ResourceLocation LEVEL_50 = mainLine("level/level_50");
    public static final ResourceLocation LEVEL_100 = mainLine("level/level_100");

    public static final ResourceLocation FORGE_ITEM = mainLine("crafting/forge");
    public static final ResourceLocation CRAFT_ARMOR = mainLine("crafting/craft");
    public static final ResourceLocation MAKE_MEDICINE = mainLine("crafting/medicine");
    public static final ResourceLocation COOK = mainLine("crafting/cook");

    public static final ResourceLocation UPGRADE_ITEM = mainLine("upgrade");
    public static final ResourceLocation CHANGE_ELEMENT = mainLine("change_element");
    public static final ResourceLocation SPELL = mainLine("spell");
    public static final ResourceLocation CHANGE_SPELL = mainLine("change_spell");
    public static final ResourceLocation LIGHT_ORE = mainLine("light_ore");

    public static final ResourceLocation FERTILIZER = mainLine("fertilizer");
    public static final ResourceLocation GIANT_CROPS = mainLine("giant_crops");
    public static final ResourceLocation HELPER = mainLine("monster_help");
    public static final ResourceLocation HIGH_TIER_TOOL = mainLine("final_tool");

    public static final ResourceLocation ROOT_PROGRESSION = progression("root");
    public static final ResourceLocation GREATER_DEMON = progression("path_1/greater_demon");
    public static final ResourceLocation CHIMERA = progression("path_1/chimera");
    public static final ResourceLocation RAFFLESIA = progression("path_1/rafflesia");
    public static final ResourceLocation GRIMOIRE = progression("path_1/grimoire");
    public static final ResourceLocation DEAD_TREE = progression("path_2/dead_tree");
    public static final ResourceLocation RACCOON = progression("path_3/raccoon");
    public static final ResourceLocation SKELEFANG = progression("path_3/skelefang");
    public static final ResourceLocation AMBROSIA = progression("path_4/ambrosia");
    public static final ResourceLocation THUNDERBOLT = progression("path_4/thunderbolt");
    public static final ResourceLocation MARIONETTA = progression("path_4/marionetta");
    public static final ResourceLocation HANDONETTA = progression("path_4/handonetta");
    public static final ResourceLocation SANO_UNO = progression("path_4/sano_uno");
    public static final ResourceLocation SARCOPHAGUS = progression("path_4/sarcophagus");

    private static ResourceLocation mainLine(String id) {
        return RuneCraftory.modRes("main/" + id);
    }

    private static ResourceLocation progression(String id) {
        return RuneCraftory.modRes("progression/" + id);
    }

    public static EntityPredicate.Builder playerAdvancementCheck(ResourceLocation advancement) {
        return EntityPredicate.Builder.entity()
                .subPredicate(PlayerPredicate.Builder.player().checkAdvancementDone(advancement, true).build());
    }
}
