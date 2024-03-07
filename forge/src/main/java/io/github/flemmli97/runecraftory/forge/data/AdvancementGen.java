package io.github.flemmli97.runecraftory.forge.data;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.advancements.LevelTrigger;
import io.github.flemmli97.runecraftory.common.advancements.MoneyTrigger;
import io.github.flemmli97.runecraftory.common.advancements.ShippingTrigger;
import io.github.flemmli97.runecraftory.common.advancements.ShopTrigger;
import io.github.flemmli97.runecraftory.common.advancements.SimpleTrigger;
import io.github.flemmli97.runecraftory.common.advancements.SkillLevelTrigger;
import io.github.flemmli97.runecraftory.common.advancements.TameMonsterTrigger;
import io.github.flemmli97.runecraftory.common.registry.ModCriteria;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

public class AdvancementGen implements DataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final DataGenerator generator;

    public AdvancementGen(DataGenerator dataGenerator) {
        this.generator = dataGenerator;
    }

    public List<Consumer<Consumer<Advancement>>> advancements() {
        Consumer<Consumer<Advancement>> c = cons -> {
            Advancement root = Advancement.Builder.advancement().display(ModItems.MEDICINAL_HERB.get(), new TranslatableComponent("runecraftory.advancements.root.title"), new TranslatableComponent("runecraftory.advancements.root.description"), new ResourceLocation("textures/block/dirt.png"), FrameType.TASK, false, false, false).addCriterion("crafting_table", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.CRAFTING_TABLE)).save(cons, id("root"));
            Advancement tameFirst = Advancement.Builder.advancement().parent(root).display(SpawnEgg.fromType(ModEntities.WOOLY.get()).get(), new TranslatableComponent("runecraftory.advancements.tame.first.title"), new TranslatableComponent("runecraftory.advancements.tame.first.description"), null, FrameType.GOAL, true, true, false).addCriterion("tame_monster", TameMonsterTrigger.TriggerInstance.of(1)).save(cons, id("taming/first"));
            Advancement tameTen = TameMonsterTrigger.TriggerInstance.amountOfSteps(Advancement.Builder.advancement().parent(tameFirst).display(SpawnEgg.fromType(ModEntities.WOOLY.get()).get(), new TranslatableComponent("runecraftory.advancements.tame.ten.title"), new TranslatableComponent("runecraftory.advancements.tame.ten.description"), null, FrameType.TASK, true, true, false), "tame_monster", 10).save(cons, id("taming/ten"));
            Advancement tameBoss = Advancement.Builder.advancement().parent(tameFirst).display(SpawnEgg.fromType(ModEntities.AMBROSIA.get()).get(), new TranslatableComponent("runecraftory.advancements.tame.boss.title"), new TranslatableComponent("runecraftory.advancements.tame.boss.description"), null, FrameType.CHALLENGE, true, true, false).addCriterion("tame_boss_monster", TameMonsterTrigger.TriggerInstance.bossOf(1)).save(cons, id("taming/boss_first"));

            Advancement shipping = Advancement.Builder.advancement().parent(root).display(ModItems.SHIPPING_BIN.get(), new TranslatableComponent("runecraftory.advancements.shipping.title"), new TranslatableComponent("runecraftory.advancements.shipping.description"), null, FrameType.GOAL, true, true, false).addCriterion("ship_item", ShippingTrigger.TriggerInstance.shipAny()).save(cons, id("shipping"));
            Advancement shop = Advancement.Builder.advancement().parent(shipping).display(Items.GOLD_INGOT, new TranslatableComponent("runecraftory.advancements.shop.title"), new TranslatableComponent("runecraftory.advancements.shop.description"), null, FrameType.GOAL, true, true, false).addCriterion("buy_item", ShopTrigger.TriggerInstance.buyAny()).save(cons, id("shop"));
            Advancement million = Advancement.Builder.advancement().parent(shop).display(ModItems.EMERY_FLOWER.get(), new TranslatableComponent("runecraftory.advancements.million.title"), new TranslatableComponent("runecraftory.advancements.million.description"), null, FrameType.TASK, true, true, false).addCriterion("have_money", MoneyTrigger.TriggerInstance.of(1000000)).save(cons, id("million"));

            Advancement skill = Advancement.Builder.advancement().parent(root).display(ModItems.SHORT_DAGGER.get(), new TranslatableComponent("runecraftory.advancements.skill.weapon.5.title"), new TranslatableComponent("runecraftory.advancements.skill.weapon.5.description"), null, FrameType.TASK, true, true, false).requirements(RequirementsStrategy.OR)
                    .addCriterion("short_sword", SkillLevelTrigger.TriggerInstance.of(5, EnumSkills.SHORTSWORD)).addCriterion("long_sword", SkillLevelTrigger.TriggerInstance.of(5, EnumSkills.LONGSWORD)).addCriterion("spear", SkillLevelTrigger.TriggerInstance.of(5, EnumSkills.SPEAR)).addCriterion("axes_hammer", SkillLevelTrigger.TriggerInstance.of(5, EnumSkills.HAMMERAXE)).addCriterion("fist", SkillLevelTrigger.TriggerInstance.of(5, EnumSkills.FIST)).save(cons, id("skill/skill_weapon_5"));
            Advancement skill10 = Advancement.Builder.advancement().parent(root).display(Items.EXPERIENCE_BOTTLE, new TranslatableComponent("runecraftory.advancements.skill.10.title"), new TranslatableComponent("runecraftory.advancements.skill.10.description"), null, FrameType.TASK, true, true, false).addCriterion("skill", SkillLevelTrigger.TriggerInstance.of(10)).save(cons, id("skill/skill_10"));
            Advancement skill25 = Advancement.Builder.advancement().parent(skill10).display(Items.EXPERIENCE_BOTTLE, new TranslatableComponent("runecraftory.advancements.skill.25.title"), new TranslatableComponent("runecraftory.advancements.skill.25.description"), null, FrameType.TASK, true, true, false).addCriterion("skill", SkillLevelTrigger.TriggerInstance.of(25)).save(cons, id("skill/skill_25"));
            Advancement skill50 = Advancement.Builder.advancement().parent(skill25).display(Items.EXPERIENCE_BOTTLE, new TranslatableComponent("runecraftory.advancements.skill.50.title"), new TranslatableComponent("runecraftory.advancements.skill.50.description"), null, FrameType.GOAL, true, true, false).addCriterion("skill", SkillLevelTrigger.TriggerInstance.of(50)).save(cons, id("skill/skill_50"));
            Advancement skill100 = Advancement.Builder.advancement().parent(skill50).display(Items.EXPERIENCE_BOTTLE, new TranslatableComponent("runecraftory.advancements.skill.100.title"), new TranslatableComponent("runecraftory.advancements.skill.100.description"), null, FrameType.CHALLENGE, true, true, false).addCriterion("skill", SkillLevelTrigger.TriggerInstance.of(100)).save(cons, id("skill/skill_100"));

            Advancement level10 = Advancement.Builder.advancement().parent(root).display(ModItems.LEVELISER.get(), new TranslatableComponent("runecraftory.advancements.level.10.title"), new TranslatableComponent("runecraftory.advancements.level.10.description"), null, FrameType.TASK, true, true, false).addCriterion("level", LevelTrigger.TriggerInstance.of(10)).save(cons, id("level/level_10"));
            Advancement level25 = Advancement.Builder.advancement().parent(level10).display(ModItems.LEVELISER.get(), new TranslatableComponent("runecraftory.advancements.level.25.title"), new TranslatableComponent("runecraftory.advancements.level.25.description"), null, FrameType.TASK, true, true, false).addCriterion("level", LevelTrigger.TriggerInstance.of(25)).save(cons, id("level/level_25"));
            Advancement level50 = Advancement.Builder.advancement().parent(level25).display(glowing(ModItems.LEVELISER.get()), new TranslatableComponent("runecraftory.advancements.level.50.title"), new TranslatableComponent("runecraftory.advancements.level.50.description"), null, FrameType.TASK, true, true, false).addCriterion("level", LevelTrigger.TriggerInstance.of(50)).save(cons, id("level/level_50"));
            Advancement level100 = Advancement.Builder.advancement().parent(level50).display(glowing(ModItems.LEVELISER.get()), new TranslatableComponent("runecraftory.advancements.level.100.title"), new TranslatableComponent("runecraftory.advancements.level.100.description"), null, FrameType.CHALLENGE, true, true, false).addCriterion("level", LevelTrigger.TriggerInstance.of(100)).save(cons, id("level/level_100"));

            Advancement upgradeItem = Advancement.Builder.advancement().parent(root).display(ModItems.CHEAP_BRACELET.get(), new TranslatableComponent("runecraftory.advancements.upgrade.title"), new TranslatableComponent("runecraftory.advancements.upgrade.description"), null, FrameType.TASK, true, true, false).addCriterion("upgrade", SimpleTrigger.TriggerInstance.of(ModCriteria.UPGRADE_ITEM)).save(cons, id("upgrade"));
            Advancement changeElement = Advancement.Builder.advancement().parent(upgradeItem).display(ModItems.CRYSTAL_LOVE.get(), new TranslatableComponent("runecraftory.advancements.change.element.title"), new TranslatableComponent("runecraftory.advancements.change.element.description"), null, FrameType.TASK, true, true, false).addCriterion("element", SimpleTrigger.TriggerInstance.of(ModCriteria.CHANGE_ELEMENT)).save(cons, id("change_element"));
            Advancement spell = Advancement.Builder.advancement().parent(upgradeItem).display(ModItems.FIRE_BALL_SMALL.get(), new TranslatableComponent("runecraftory.advancements.spell.title"), new TranslatableComponent("runecraftory.advancements.spell.description"), null, FrameType.TASK, true, true, false).addCriterion("spell", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ModTags.SPELLS).build())).save(cons, id("spell"));
            Advancement changeSpell = Advancement.Builder.advancement().parent(spell).display(ModItems.ROD.get(), new TranslatableComponent("runecraftory.advancements.change.spell.title"), new TranslatableComponent("runecraftory.advancements.change.spell.description"), null, FrameType.TASK, true, true, false).addCriterion("spell", SimpleTrigger.TriggerInstance.of(ModCriteria.CHANGE_SPELL)).save(cons, id("change_spell"));

            Advancement fertilizer = Advancement.Builder.advancement().parent(root).display(ModItems.FORMULAR_A.get(), new TranslatableComponent("runecraftory.advancements.fertilizer.title"), new TranslatableComponent("runecraftory.advancements.fertilizer.description"), null, FrameType.TASK, true, true, false).addCriterion("fertilizer", SimpleTrigger.TriggerInstance.of(ModCriteria.FERTILIZE_FARM)).save(cons, id("fertilizer"));
            Advancement helper = Advancement.Builder.advancement().parent(fertilizer).display(ModItems.MOB_STAFF.get(), new TranslatableComponent("runecraftory.advancements.monster.help.title"), new TranslatableComponent("runecraftory.advancements.monster.help.description"), null, FrameType.TASK, true, true, false).addCriterion("farming", SimpleTrigger.TriggerInstance.of(ModCriteria.COMMAND_FARMING)).save(cons, id("monster_help"));
            Advancement hightTierTool = Advancement.Builder.advancement().parent(helper).display(ModItems.WATERING_CAN_PLATINUM.get(), new TranslatableComponent("runecraftory.advancements.final.tool.title"), new TranslatableComponent("runecraftory.advancements.final.tool.description"), null, FrameType.CHALLENGE, true, true, false).addCriterion("final_tool", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ModTags.HIGH_TIER_TOOLS).build())).save(cons, id("final_tool"));
        };
        return List.of(c);
    }

    private static ItemStack glowing(ItemLike itemLike) {
        ItemStack stack = new ItemStack(itemLike.asItem());
        stack.enchant(Enchantments.UNBREAKING, 1);
        return stack;
    }

    public static RequirementsStrategy amount(int amount) {
        return collection -> {
            String[][] strings = new String[amount][];
            for (int i = 0; i < amount; i++)
                strings[i] = collection.toArray(new String[0]);
            return strings;
        };
    }

    @Override
    public void run(HashCache cache) {
        Path path = this.generator.getOutputFolder();
        HashSet<ResourceLocation> set = Sets.newHashSet();
        Consumer<Advancement> consumer = advancement -> {
            if (!set.add(advancement.getId())) {
                throw new IllegalStateException("Duplicate advancement " + advancement.getId());
            }
            Path path2 = createPath(path, advancement);
            try {
                DataProvider.save(GSON, cache, advancement.deconstruct().serializeToJson(), path2);
            } catch (IOException iOException) {
                RuneCraftory.LOGGER.error("Couldn't save advancement {}", path2, iOException);
            }
        };
        for (Consumer<Consumer<Advancement>> consumer2 : this.advancements()) {
            consumer2.accept(consumer);
        }
    }

    private static Path createPath(Path path, Advancement advancement) {
        return path.resolve("data/" + advancement.getId().getNamespace() + "/advancements/" + advancement.getId().getPath() + ".json");
    }

    private static String id(String id) {
        return RuneCraftory.MODID + ":main/" + id;
    }

    @Override
    public String getName() {
        return "Advancements";
    }
}
