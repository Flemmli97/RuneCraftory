package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.advancements.LevelTrigger;
import io.github.flemmli97.runecraftory.common.advancements.MoneyTrigger;
import io.github.flemmli97.runecraftory.common.advancements.ShippingTrigger;
import io.github.flemmli97.runecraftory.common.advancements.ShopTrigger;
import io.github.flemmli97.runecraftory.common.advancements.SimpleTrigger;
import io.github.flemmli97.runecraftory.common.advancements.SkillLevelTrigger;
import io.github.flemmli97.runecraftory.common.advancements.TameMonsterTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.resources.ResourceLocation;

public class ModCriteria {

    public static final LevelTrigger LEVEL_TRIGGER = register(new LevelTrigger());
    public static final MoneyTrigger MONEY_TRIGGER = register(new MoneyTrigger());
    public static final ShippingTrigger SHIPPING_TRIGGER = register(new ShippingTrigger());
    public static final ShopTrigger SHOP_TRIGGER = register(new ShopTrigger());
    public static final SkillLevelTrigger SKILL_LEVEL_TRIGGER = register(new SkillLevelTrigger());
    public static final TameMonsterTrigger TAME_MONSTER_TRIGGER = register(new TameMonsterTrigger());
    public static final SimpleTrigger UPGRADE_ITEM = register(new SimpleTrigger(new ResourceLocation(RuneCraftory.MODID, "upgrade_item")));
    public static final SimpleTrigger CHANGE_SPELL = register(new SimpleTrigger(new ResourceLocation(RuneCraftory.MODID, "change_spell")));
    public static final SimpleTrigger CHANGE_ELEMENT = register(new SimpleTrigger(new ResourceLocation(RuneCraftory.MODID, "change_element")));
    public static final SimpleTrigger FERTILIZE_FARM = register(new SimpleTrigger(new ResourceLocation(RuneCraftory.MODID, "fertilize_farm")));
    public static final SimpleTrigger COMMAND_FARMING = register(new SimpleTrigger(new ResourceLocation(RuneCraftory.MODID, "monster_farming")));

    public static void init() {
    }

    private static <T extends CriterionTrigger<?>> T register(T inst) {
        return CriteriaTriggers.register(inst);
    }
}
