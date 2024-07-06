package io.github.flemmli97.runecraftory.forge.data;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.advancements.CropHarvestTrigger;
import io.github.flemmli97.runecraftory.common.advancements.LevelTrigger;
import io.github.flemmli97.runecraftory.common.advancements.MoneyTrigger;
import io.github.flemmli97.runecraftory.common.advancements.ShippingTrigger;
import io.github.flemmli97.runecraftory.common.advancements.ShopTrigger;
import io.github.flemmli97.runecraftory.common.advancements.SimpleTrigger;
import io.github.flemmli97.runecraftory.common.advancements.SkillLevelTrigger;
import io.github.flemmli97.runecraftory.common.advancements.TameMonsterTrigger;
import io.github.flemmli97.runecraftory.common.lib.LibAdvancements;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.ModCriteria;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.function.Consumer;

public class AdvancementGen implements DataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final DataGenerator generator;

    public AdvancementGen(DataGenerator dataGenerator) {
        this.generator = dataGenerator;
    }

    public void advancements(Consumer<Advancement> cons) {
        Advancement root = Advancement.Builder.advancement().display(ModItems.MEDICINAL_HERB.get(), new TranslatableComponent("runecraftory.advancements.root.title"), new TranslatableComponent("runecraftory.advancements.root.description"), new ResourceLocation("textures/block/dirt.png"), FrameType.TASK, false, false, false).addCriterion("crafting_table", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.CRAFTING_TABLE)).save(cons, LibAdvancements.ROOT.toString());
        Advancement tameFirst = Advancement.Builder.advancement().parent(root).display(SpawnEgg.fromType(ModEntities.WOOLY.get()).get(), new TranslatableComponent("runecraftory.advancements.tame.first.title"), new TranslatableComponent("runecraftory.advancements.tame.first.description"), null, FrameType.TASK, true, true, false).addCriterion("tame_monster", TameMonsterTrigger.TriggerInstance.of(1)).save(cons, LibAdvancements.TAME_FIRST.toString());
        Advancement tameTen = TameMonsterTrigger.TriggerInstance.amountOfSteps(Advancement.Builder.advancement().parent(tameFirst).display(ModItems.MONSTER_BARN.get(), new TranslatableComponent("runecraftory.advancements.tame.ten.title"), new TranslatableComponent("runecraftory.advancements.tame.ten.description"), null, FrameType.GOAL, true, true, false), "tame_monster", 10, false).save(cons, LibAdvancements.TAME_TEN.toString());
        Advancement tameBoss = Advancement.Builder.advancement().parent(tameFirst).display(ModItems.AMBROSIAS_THORNS.get(), new TranslatableComponent("runecraftory.advancements.tame.boss.title"), new TranslatableComponent("runecraftory.advancements.tame.boss.description"), null, FrameType.TASK, true, true, false).addCriterion("tame_boss_monster", TameMonsterTrigger.TriggerInstance.bossOf(1)).save(cons, LibAdvancements.TAME_BOSS_FIRST.toString());
        Advancement tameBossFive = TameMonsterTrigger.TriggerInstance.amountOfSteps(Advancement.Builder.advancement().parent(tameBoss).display(glowing(SpawnEgg.fromType(ModEntities.AMBROSIA.get()).get()), new TranslatableComponent("runecraftory.advancements.tame.boss.five.title"), new TranslatableComponent("runecraftory.advancements.tame.boss.five.description"), null, FrameType.GOAL, true, true, false), "tame_bosses", 5, true).save(cons, LibAdvancements.TAME_BOSS_FIVE.toString());
        Advancement tameBossAll = allBosses(Advancement.Builder.advancement().parent(tameBossFive).display(ModItems.SCALE_LEGEND.get(), new TranslatableComponent("runecraftory.advancements.tame.boss.all.title"), new TranslatableComponent("runecraftory.advancements.tame.boss.all.description"), null, FrameType.CHALLENGE, true, true, false)).save(cons, LibAdvancements.TAME_BOSS_ALL.toString());

        Advancement shipping = Advancement.Builder.advancement().parent(root).display(ModItems.SHIPPING_BIN.get(), new TranslatableComponent("runecraftory.advancements.shipping.title"), new TranslatableComponent("runecraftory.advancements.shipping.description"), null, FrameType.TASK, true, true, false).addCriterion("ship_item", ShippingTrigger.TriggerInstance.shipAny(1)).save(cons, LibAdvancements.SHIP_FIRST.toString());
        Advancement shippingFifty = Advancement.Builder.advancement().parent(shipping).display(ModItems.SHIPPING_BIN.get(), new TranslatableComponent("runecraftory.advancements.shipping.fifty.title"), new TranslatableComponent("runecraftory.advancements.shipping.fifty.description"), null, FrameType.GOAL, true, true, false).addCriterion("ship_item", ShippingTrigger.TriggerInstance.shipAny(50)).save(cons, LibAdvancements.SHIP_FIFTY.toString());
        Advancement shop = Advancement.Builder.advancement().parent(shipping).display(Items.GOLD_INGOT, new TranslatableComponent("runecraftory.advancements.shop.title"), new TranslatableComponent("runecraftory.advancements.shop.description"), null, FrameType.TASK, true, true, false).addCriterion("buy_item", ShopTrigger.TriggerInstance.buyAny()).save(cons, LibAdvancements.SHOP.toString());
        Advancement hundred_k = Advancement.Builder.advancement().parent(shop).display(Items.GOLD_BLOCK, new TranslatableComponent("runecraftory.advancements.100k.title"), new TranslatableComponent("runecraftory.advancements.100k.description"), null, FrameType.GOAL, true, true, false).addCriterion("have_money", MoneyTrigger.TriggerInstance.of(100000)).save(cons, LibAdvancements.MONEY_100K.toString());
        Advancement million = Advancement.Builder.advancement().parent(hundred_k).display(ModItems.EMERY_FLOWER.get(), new TranslatableComponent("runecraftory.advancements.million.title"), new TranslatableComponent("runecraftory.advancements.million.description"), null, FrameType.CHALLENGE, true, true, false).addCriterion("have_money", MoneyTrigger.TriggerInstance.of(1000000)).save(cons, LibAdvancements.MONEY_1M.toString());

        Advancement skill = Advancement.Builder.advancement().parent(root).display(SpawnEgg.fromType(ModEntities.WOOLY.get()).get(), new TranslatableComponent("runecraftory.advancements.skill.weapon.5.title"), new TranslatableComponent("runecraftory.advancements.skill.weapon.5.description"), null, FrameType.TASK, true, true, false).requirements(RequirementsStrategy.OR)
                .addCriterion("short_sword", SkillLevelTrigger.TriggerInstance.of(5, EnumSkills.SHORTSWORD)).addCriterion("long_sword", SkillLevelTrigger.TriggerInstance.of(5, EnumSkills.LONGSWORD)).addCriterion("spear", SkillLevelTrigger.TriggerInstance.of(5, EnumSkills.SPEAR)).addCriterion("axes_hammer", SkillLevelTrigger.TriggerInstance.of(5, EnumSkills.HAMMERAXE)).addCriterion("first", SkillLevelTrigger.TriggerInstance.of(5, EnumSkills.FIST)).save(cons, LibAdvancements.SKILL_5.toString());
        Advancement skill10 = Advancement.Builder.advancement().parent(root).display(Items.EXPERIENCE_BOTTLE, new TranslatableComponent("runecraftory.advancements.skill.10.title"), new TranslatableComponent("runecraftory.advancements.skill.10.description"), null, FrameType.TASK, true, true, false).addCriterion("skill", SkillLevelTrigger.TriggerInstance.of(10)).save(cons, LibAdvancements.SKILL_10.toString());
        Advancement skill25 = Advancement.Builder.advancement().parent(skill10).display(Items.EXPERIENCE_BOTTLE, new TranslatableComponent("runecraftory.advancements.skill.25.title"), new TranslatableComponent("runecraftory.advancements.skill.25.description"), null, FrameType.TASK, true, true, false).addCriterion("skill", SkillLevelTrigger.TriggerInstance.of(25)).save(cons, LibAdvancements.SKILL_25.toString());
        Advancement skill50 = Advancement.Builder.advancement().parent(skill25).display(Items.EXPERIENCE_BOTTLE, new TranslatableComponent("runecraftory.advancements.skill.50.title"), new TranslatableComponent("runecraftory.advancements.skill.50.description"), null, FrameType.GOAL, true, true, false).addCriterion("skill", SkillLevelTrigger.TriggerInstance.of(50)).save(cons, LibAdvancements.SKILL_50.toString());
        Advancement skill100 = Advancement.Builder.advancement().parent(skill50).display(Items.EXPERIENCE_BOTTLE, new TranslatableComponent("runecraftory.advancements.skill.100.title"), new TranslatableComponent("runecraftory.advancements.skill.100.description"), null, FrameType.CHALLENGE, true, true, false).addCriterion("skill", SkillLevelTrigger.TriggerInstance.of(100)).save(cons, LibAdvancements.SKILL_100.toString());

        Advancement level10 = Advancement.Builder.advancement().parent(root).display(ModItems.LEVELISER.get(), new TranslatableComponent("runecraftory.advancements.level.10.title"), new TranslatableComponent("runecraftory.advancements.level.10.description"), null, FrameType.TASK, true, true, false).addCriterion("level", LevelTrigger.TriggerInstance.of(10)).save(cons, LibAdvancements.LEVEL_10.toString());
        Advancement level25 = Advancement.Builder.advancement().parent(level10).display(ModItems.LEVELISER.get(), new TranslatableComponent("runecraftory.advancements.level.25.title"), new TranslatableComponent("runecraftory.advancements.level.25.description"), null, FrameType.TASK, true, true, false).addCriterion("level", LevelTrigger.TriggerInstance.of(25)).save(cons, LibAdvancements.LEVEL_25.toString());
        Advancement level50 = Advancement.Builder.advancement().parent(level25).display(glowing(ModItems.LEVELISER.get()), new TranslatableComponent("runecraftory.advancements.level.50.title"), new TranslatableComponent("runecraftory.advancements.level.50.description"), null, FrameType.TASK, true, true, false).addCriterion("level", LevelTrigger.TriggerInstance.of(50)).save(cons, LibAdvancements.LEVEL_50.toString());
        Advancement level100 = Advancement.Builder.advancement().parent(level50).display(glowing(ModItems.LEVELISER.get()), new TranslatableComponent("runecraftory.advancements.level.100.title"), new TranslatableComponent("runecraftory.advancements.level.100.description"), null, FrameType.CHALLENGE, true, true, false).addCriterion("level", LevelTrigger.TriggerInstance.of(100)).save(cons, LibAdvancements.LEVEL_100.toString());

        Advancement forgingItem = Advancement.Builder.advancement().parent(root).display(ModItems.ITEM_BLOCK_FORGE.get(), new TranslatableComponent("runecraftory.advancements.crafting.forging.title"), new TranslatableComponent("runecraftory.advancements.crafting.forging.description"), null, FrameType.TASK, true, true, false).addCriterion("craft", SimpleTrigger.TriggerInstance.of(ModCriteria.FORGING)).save(cons, LibAdvancements.FORGE_ITEM.toString());
        Advancement craftingItem = Advancement.Builder.advancement().parent(root).display(ModItems.ITEM_BLOCK_ACCESS.get(), new TranslatableComponent("runecraftory.advancements.crafting.armor.title"), new TranslatableComponent("runecraftory.advancements.crafting.armor.description"), null, FrameType.TASK, true, true, false).addCriterion("craft", SimpleTrigger.TriggerInstance.of(ModCriteria.CRAFTING)).save(cons, LibAdvancements.CRAFT_ARMOR.toString());
        Advancement brewingItem = Advancement.Builder.advancement().parent(root).display(ModItems.ITEM_BLOCK_CHEM.get(), new TranslatableComponent("runecraftory.advancements.crafting.chemistry.title"), new TranslatableComponent("runecraftory.advancements.crafting.chemistry.description"), null, FrameType.TASK, true, true, false).addCriterion("craft", SimpleTrigger.TriggerInstance.of(ModCriteria.MEDICINE)).save(cons, LibAdvancements.MAKE_MEDICINE.toString());
        Advancement cookingItem = Advancement.Builder.advancement().parent(root).display(ModItems.ITEM_BLOCK_COOKING.get(), new TranslatableComponent("runecraftory.advancements.crafting.cooking.title"), new TranslatableComponent("runecraftory.advancements.crafting.cooking.description"), null, FrameType.TASK, true, true, false).addCriterion("craft", SimpleTrigger.TriggerInstance.of(ModCriteria.COOKING)).save(cons, LibAdvancements.COOK.toString());

        Advancement upgradeItem = Advancement.Builder.advancement().parent(craftingItem).display(ModItems.CHEAP_BRACELET.get(), new TranslatableComponent("runecraftory.advancements.upgrade.title"), new TranslatableComponent("runecraftory.advancements.upgrade.description"), null, FrameType.TASK, true, true, false).addCriterion("upgrade", SimpleTrigger.TriggerInstance.of(ModCriteria.UPGRADE_ITEM)).save(cons, LibAdvancements.UPGRADE_ITEM.toString());
        Advancement changeElement = Advancement.Builder.advancement().parent(forgingItem).display(ModItems.CRYSTAL_LOVE.get(), new TranslatableComponent("runecraftory.advancements.change.element.title"), new TranslatableComponent("runecraftory.advancements.change.element.description"), null, FrameType.TASK, true, true, false).addCriterion("element", SimpleTrigger.TriggerInstance.of(ModCriteria.CHANGE_ELEMENT)).save(cons, LibAdvancements.CHANGE_ELEMENT.toString());
        Advancement lightOre = Advancement.Builder.advancement().parent(changeElement).display(ModItems.LIGHT_ORE.get(), new TranslatableComponent("runecraftory.advancements.lightore.title"), new TranslatableComponent("runecraftory.advancements.lightore.description"), null, FrameType.GOAL, true, true, false).addCriterion("light_ore", SimpleTrigger.TriggerInstance.of(ModCriteria.LIGHT_ORE)).save(cons, LibAdvancements.LIGHT_ORE.toString());
        Advancement spell = Advancement.Builder.advancement().parent(forgingItem).display(ModItems.FIRE_BALL_SMALL.get(), new TranslatableComponent("runecraftory.advancements.spell.title"), new TranslatableComponent("runecraftory.advancements.spell.description"), null, FrameType.TASK, true, true, false).addCriterion("spell", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(RunecraftoryTags.SPELLS).build())).save(cons, LibAdvancements.SPELL.toString());
        Advancement changeSpell = Advancement.Builder.advancement().parent(forgingItem).display(ModItems.ROD.get(), new TranslatableComponent("runecraftory.advancements.change.spell.title"), new TranslatableComponent("runecraftory.advancements.change.spell.description"), null, FrameType.TASK, true, true, false).addCriterion("spell", SimpleTrigger.TriggerInstance.of(ModCriteria.CHANGE_SPELL)).save(cons, LibAdvancements.CHANGE_SPELL.toString());

        Advancement fertilizer = Advancement.Builder.advancement().parent(root).display(ModItems.FORMULAR_A.get(), new TranslatableComponent("runecraftory.advancements.fertilizer.title"), new TranslatableComponent("runecraftory.advancements.fertilizer.description"), null, FrameType.TASK, true, true, false).addCriterion("fertilizer", SimpleTrigger.TriggerInstance.of(ModCriteria.FERTILIZE_FARM)).save(cons, LibAdvancements.FERTILIZER.toString());
        Advancement giantCrop = Advancement.Builder.advancement().parent(fertilizer).display(ModItems.TURNIP_GIANT.get(), new TranslatableComponent("runecraftory.advancements.giant_crop.title"), new TranslatableComponent("runecraftory.advancements.giant_crop.description"), null, FrameType.GOAL, true, true, false).addCriterion("giant_crop", CropHarvestTrigger.TriggerInstance.harvest(RunecraftoryTags.GIANT_CROP_BLOCKS)).save(cons, LibAdvancements.GIANT_CROPS.toString());
        Advancement helper = Advancement.Builder.advancement().parent(fertilizer).display(ModItems.MOB_STAFF.get(), new TranslatableComponent("runecraftory.advancements.monster.help.title"), new TranslatableComponent("runecraftory.advancements.monster.help.description"), null, FrameType.TASK, true, true, false).addCriterion("farming", SimpleTrigger.TriggerInstance.of(ModCriteria.COMMAND_FARMING)).save(cons, LibAdvancements.HELPER.toString());
        Advancement hightTierTool = Advancement.Builder.advancement().parent(helper).display(ModItems.WATERING_CAN_PLATINUM.get(), new TranslatableComponent("runecraftory.advancements.final.tool.title"), new TranslatableComponent("runecraftory.advancements.final.tool.description"), null, FrameType.CHALLENGE, true, true, false).addCriterion("final_tool", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(RunecraftoryTags.HIGH_TIER_TOOLS).build())).save(cons, LibAdvancements.HIGH_TIER_TOOL.toString());

        Advancement rootProgression = Advancement.Builder.advancement().display(ModItems.SHORT_DAGGER.get(), new TranslatableComponent("runecraftory.advancements.progression.root.title"), new TranslatableComponent("runecraftory.advancements.progression.root.description"), new ResourceLocation("textures/block/dirt.png"), FrameType.TASK, false, false, false).addCriterion("crafting_table", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.CRAFTING_TABLE)).save(cons, LibAdvancements.ROOT_PROGRESSION.toString());
        Advancement chimera = bossProgression(ModEntities.CHIMERA, cons, LibAdvancements.CHIMERA, rootProgression);
        Advancement rafflesia = bossProgression(ModEntities.RAFFLESIA, cons, LibAdvancements.RAFFLESIA, chimera);
        Advancement grimoire = bossProgression(ModEntities.GRIMOIRE, cons, LibAdvancements.GRIMOIRE, rafflesia);
        Advancement deadTree = bossProgression(ModEntities.DEAD_TREE, cons, LibAdvancements.DEAD_TREE, rootProgression);
        Advancement raccoon = bossProgression(ModEntities.RACCOON, cons, LibAdvancements.RACCOON, rootProgression);
        Advancement skelefang = bossProgression(ModEntities.SKELEFANG, cons, LibAdvancements.SKELEFANG, raccoon);
        Advancement ambrosia = bossProgression(ModEntities.AMBROSIA, cons, LibAdvancements.AMBROSIA, rootProgression);
        Advancement thunderbolt = bossProgression(ModEntities.THUNDERBOLT, cons, LibAdvancements.THUNDERBOLT, ambrosia);
        Advancement marionetta = bossProgression(ModEntities.MARIONETTA, cons, LibAdvancements.MARIONETTA, thunderbolt);
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

    private static Path createPath(Path path, Advancement advancement) {
        return path.resolve("data/" + advancement.getId().getNamespace() + "/advancements/" + advancement.getId().getPath() + ".json");
    }

    protected static Advancement.Builder allBosses(Advancement.Builder builder) {
        for (RegistryEntrySupplier<EntityType<?>> sup : ModEntities.getBosses()) {
            builder.addCriterion(sup.getID().toString(), new TameMonsterTrigger.TriggerInstance(EntityPredicate.Composite.ANY, new EntityPredicate.Builder()
                    .of(sup.get()).build(), 1, false));
        }
        return builder;
    }

    private static <T extends Entity> Advancement bossProgression(RegistryEntrySupplier<EntityType<T>> entity, Consumer<Advancement> cons, ResourceLocation id, Advancement parent) {
        CriterionTriggerInstance trigger = parent != null ? new KilledTrigger.TriggerInstance(
                CriteriaTriggers.PLAYER_KILLED_ENTITY.getId(),
                EntityPredicate.Composite.wrap(LibAdvancements.playerAdvancementCheck(parent.getId()).build()),
                EntityPredicate.Composite.wrap(EntityPredicate.Builder.entity().of(entity.get()).build()),
                DamageSourcePredicate.ANY) :
                KilledTrigger.TriggerInstance.entityKilledPlayer(
                        EntityPredicate.Builder.entity().of(entity.get()).build());
        Advancement.Builder builder = Advancement.Builder.advancement().display(SpawnEgg.fromType(entity.get()).get(),
                        new TranslatableComponent(String.format("runecraftory.advancements.progression.boss.%s.title", entity.getID().getPath())),
                        new TranslatableComponent(String.format("runecraftory.advancements.progression.boss.%s.description", entity.getID().getPath())),
                        new ResourceLocation("textures/block/dirt.png"), FrameType.TASK, true, true, false)
                .addCriterion("boss", trigger);
        if (parent != null)
            builder.parent(parent);
        return builder.save(cons, id.toString());
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
        this.advancements(consumer);
    }

    @Override
    public String getName() {
        return "Advancements";
    }
}
