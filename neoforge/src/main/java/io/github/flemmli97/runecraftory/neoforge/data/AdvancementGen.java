package io.github.flemmli97.runecraftory.neoforge.data;

import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.common.advancements.CropHarvestTrigger;
import io.github.flemmli97.runecraftory.common.advancements.LevelTrigger;
import io.github.flemmli97.runecraftory.common.advancements.MoneyTrigger;
import io.github.flemmli97.runecraftory.common.advancements.ShippingTrigger;
import io.github.flemmli97.runecraftory.common.advancements.ShopTrigger;
import io.github.flemmli97.runecraftory.common.advancements.SkillLevelTrigger;
import io.github.flemmli97.runecraftory.common.advancements.TameMonsterTrigger;
import io.github.flemmli97.runecraftory.common.lib.LibAdvancements;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryCriteria;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AdvancementGen extends AdvancementProvider {

    public AdvancementGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(new RunecraftoryAdvancements()));
    }

    public static class RunecraftoryAdvancements implements AdvancementGenerator {

        @Override
        public void generate(HolderLookup.Provider provider, Consumer<AdvancementHolder> cons, ExistingFileHelper fileHelper) {
            AdvancementHolder root = Advancement.Builder.advancement().display(RuneCraftoryItems.MEDICINAL_HERB.get(), Component.translatable("runecraftory.advancements.root.title"), Component.translatable("runecraftory.advancements.root.description"), ResourceLocation.withDefaultNamespace("textures/block/dirt.png"), AdvancementType.TASK, false, false, false).addCriterion("crafting_table", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.CRAFTING_TABLE)).save(cons, LibAdvancements.ROOT.toString());
            AdvancementHolder tameFirst = Advancement.Builder.advancement().parent(root).display(SpawnEgg.fromType(RuneCraftoryEntities.WOOLY.get()).get(), Component.translatable("runecraftory.advancements.tame.first.title"), Component.translatable("runecraftory.advancements.tame.first.description"), null, AdvancementType.TASK, true, true, false).addCriterion("tame_monster", TameMonsterTrigger.TriggerInstance.of(1)).save(cons, LibAdvancements.TAME_FIRST.toString());
            AdvancementHolder tameTen = TameMonsterTrigger.TriggerInstance.amountOfSteps(Advancement.Builder.advancement().parent(tameFirst).display(RuneCraftoryItems.MONSTER_BARN.get(), Component.translatable("runecraftory.advancements.tame.ten.title"), Component.translatable("runecraftory.advancements.tame.ten.description"), null, AdvancementType.GOAL, true, true, false), "tame_monster", 10, false).save(cons, LibAdvancements.TAME_TEN.toString());
            AdvancementHolder tameBoss = Advancement.Builder.advancement().parent(tameFirst).display(RuneCraftoryItems.AMBROSIAS_THORNS.get(), Component.translatable("runecraftory.advancements.tame.boss.title"), Component.translatable("runecraftory.advancements.tame.boss.description"), null, AdvancementType.TASK, true, true, false).addCriterion("tame_boss_monster", TameMonsterTrigger.TriggerInstance.bossOf(1)).save(cons, LibAdvancements.TAME_BOSS_FIRST.toString());
            AdvancementHolder tameBossFive = TameMonsterTrigger.TriggerInstance.amountOfSteps(Advancement.Builder.advancement().parent(tameBoss).display(glowing(SpawnEgg.fromType(RuneCraftoryEntities.AMBROSIA.get()).get()), Component.translatable("runecraftory.advancements.tame.boss.five.title"), Component.translatable("runecraftory.advancements.tame.boss.five.description"), null, AdvancementType.GOAL, true, true, false), "tame_bosses", 5, true).save(cons, LibAdvancements.TAME_BOSS_FIVE.toString());
            AdvancementHolder tameBossAll = allBosses(Advancement.Builder.advancement().parent(tameBossFive).display(RuneCraftoryItems.SCALE_LEGEND.get(), Component.translatable("runecraftory.advancements.tame.boss.all.title"), Component.translatable("runecraftory.advancements.tame.boss.all.description"), null, AdvancementType.CHALLENGE, true, true, false)).save(cons, LibAdvancements.TAME_BOSS_ALL.toString());

            AdvancementHolder shipping = Advancement.Builder.advancement().parent(root).display(RuneCraftoryItems.SHIPPING_BIN.get(), Component.translatable("runecraftory.advancements.shipping.title"), Component.translatable("runecraftory.advancements.shipping.description"), null, AdvancementType.TASK, true, true, false).addCriterion("ship_item", ShippingTrigger.TriggerInstance.shipAny(1)).save(cons, LibAdvancements.SHIP_FIRST.toString());
            AdvancementHolder shippingFifty = Advancement.Builder.advancement().parent(shipping).display(RuneCraftoryItems.SHIPPING_BIN.get(), Component.translatable("runecraftory.advancements.shipping.fifty.title"), Component.translatable("runecraftory.advancements.shipping.fifty.description"), null, AdvancementType.GOAL, true, true, false).addCriterion("ship_item", ShippingTrigger.TriggerInstance.shipAny(50)).save(cons, LibAdvancements.SHIP_FIFTY.toString());
            AdvancementHolder shop = Advancement.Builder.advancement().parent(shipping).display(Items.GOLD_INGOT, Component.translatable("runecraftory.advancements.shop.title"), Component.translatable("runecraftory.advancements.shop.description"), null, AdvancementType.TASK, true, true, false).addCriterion("buy_item", ShopTrigger.TriggerInstance.buyAny()).save(cons, LibAdvancements.SHOP.toString());
            AdvancementHolder hundred_k = Advancement.Builder.advancement().parent(shop).display(Items.GOLD_BLOCK, Component.translatable("runecraftory.advancements.100k.title"), Component.translatable("runecraftory.advancements.100k.description"), null, AdvancementType.GOAL, true, true, false).addCriterion("have_money", MoneyTrigger.TriggerInstance.of(100000)).save(cons, LibAdvancements.MONEY_100K.toString());
            AdvancementHolder million = Advancement.Builder.advancement().parent(hundred_k).display(RuneCraftoryItems.EMERY_FLOWER.get(), Component.translatable("runecraftory.advancements.million.title"), Component.translatable("runecraftory.advancements.million.description"), null, AdvancementType.CHALLENGE, true, true, false).addCriterion("have_money", MoneyTrigger.TriggerInstance.of(1000000)).save(cons, LibAdvancements.MONEY_1M.toString());

            AdvancementHolder skill = Advancement.Builder.advancement().parent(root).display(SpawnEgg.fromType(RuneCraftoryEntities.WOOLY.get()).get(), Component.translatable("runecraftory.advancements.skill.weapon.5.title"), Component.translatable("runecraftory.advancements.skill.weapon.5.description"), null, AdvancementType.TASK, true, true, false).requirements(AdvancementRequirements.Strategy.OR)
                    .addCriterion("short_sword", SkillLevelTrigger.TriggerInstance.of(Skills.SHORTSWORD, 5)).addCriterion("long_sword", SkillLevelTrigger.TriggerInstance.of(Skills.LONGSWORD, 5)).addCriterion("spear", SkillLevelTrigger.TriggerInstance.of(Skills.SPEAR, 5)).addCriterion("axes_hammer", SkillLevelTrigger.TriggerInstance.of(Skills.HAMMERAXE, 5)).addCriterion("first", SkillLevelTrigger.TriggerInstance.of(Skills.FIST, 5)).save(cons, LibAdvancements.SKILL_5.toString());
            AdvancementHolder skill10 = Advancement.Builder.advancement().parent(root).display(Items.EXPERIENCE_BOTTLE, Component.translatable("runecraftory.advancements.skill.10.title"), Component.translatable("runecraftory.advancements.skill.10.description"), null, AdvancementType.TASK, true, true, false).addCriterion("skill", SkillLevelTrigger.TriggerInstance.of(10)).save(cons, LibAdvancements.SKILL_10.toString());
            AdvancementHolder skill25 = Advancement.Builder.advancement().parent(skill10).display(Items.EXPERIENCE_BOTTLE, Component.translatable("runecraftory.advancements.skill.25.title"), Component.translatable("runecraftory.advancements.skill.25.description"), null, AdvancementType.TASK, true, true, false).addCriterion("skill", SkillLevelTrigger.TriggerInstance.of(25)).save(cons, LibAdvancements.SKILL_25.toString());
            AdvancementHolder skill50 = Advancement.Builder.advancement().parent(skill25).display(Items.EXPERIENCE_BOTTLE, Component.translatable("runecraftory.advancements.skill.50.title"), Component.translatable("runecraftory.advancements.skill.50.description"), null, AdvancementType.GOAL, true, true, false).addCriterion("skill", SkillLevelTrigger.TriggerInstance.of(50)).save(cons, LibAdvancements.SKILL_50.toString());
            AdvancementHolder skill100 = Advancement.Builder.advancement().parent(skill50).display(Items.EXPERIENCE_BOTTLE, Component.translatable("runecraftory.advancements.skill.100.title"), Component.translatable("runecraftory.advancements.skill.100.description"), null, AdvancementType.CHALLENGE, true, true, false).addCriterion("skill", SkillLevelTrigger.TriggerInstance.of(100)).save(cons, LibAdvancements.SKILL_100.toString());

            AdvancementHolder level10 = Advancement.Builder.advancement().parent(root).display(RuneCraftoryItems.LEVELISER.get(), Component.translatable("runecraftory.advancements.level.10.title"), Component.translatable("runecraftory.advancements.level.10.description"), null, AdvancementType.TASK, true, true, false).addCriterion("level", LevelTrigger.TriggerInstance.of(10)).save(cons, LibAdvancements.LEVEL_10.toString());
            AdvancementHolder level25 = Advancement.Builder.advancement().parent(level10).display(RuneCraftoryItems.LEVELISER.get(), Component.translatable("runecraftory.advancements.level.25.title"), Component.translatable("runecraftory.advancements.level.25.description"), null, AdvancementType.TASK, true, true, false).addCriterion("level", LevelTrigger.TriggerInstance.of(25)).save(cons, LibAdvancements.LEVEL_25.toString());
            AdvancementHolder level50 = Advancement.Builder.advancement().parent(level25).display(glowing(RuneCraftoryItems.LEVELISER.get()), Component.translatable("runecraftory.advancements.level.50.title"), Component.translatable("runecraftory.advancements.level.50.description"), null, AdvancementType.TASK, true, true, false).addCriterion("level", LevelTrigger.TriggerInstance.of(50)).save(cons, LibAdvancements.LEVEL_50.toString());
            AdvancementHolder level100 = Advancement.Builder.advancement().parent(level50).display(glowing(RuneCraftoryItems.LEVELISER.get()), Component.translatable("runecraftory.advancements.level.100.title"), Component.translatable("runecraftory.advancements.level.100.description"), null, AdvancementType.CHALLENGE, true, true, false).addCriterion("level", LevelTrigger.TriggerInstance.of(100)).save(cons, LibAdvancements.LEVEL_100.toString());

            AdvancementHolder forgingItem = Advancement.Builder.advancement().parent(root).display(RuneCraftoryItems.FORGE.get(), Component.translatable("runecraftory.advancements.crafting.forging.title"), Component.translatable("runecraftory.advancements.crafting.forging.description"), null, AdvancementType.TASK, true, true, false).addCriterion("craft", RuneCraftoryCriteria.FORGING.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()))).save(cons, LibAdvancements.FORGE_ITEM.toString());
            AdvancementHolder craftingItem = Advancement.Builder.advancement().parent(root).display(RuneCraftoryItems.ACCESSORY_WORKBENCH.get(), Component.translatable("runecraftory.advancements.crafting.armor.title"), Component.translatable("runecraftory.advancements.crafting.armor.description"), null, AdvancementType.TASK, true, true, false).addCriterion("craft", RuneCraftoryCriteria.CRAFTING.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()))).save(cons, LibAdvancements.CRAFT_ARMOR.toString());
            AdvancementHolder brewingItem = Advancement.Builder.advancement().parent(root).display(RuneCraftoryItems.CHEMISTRY_SET.get(), Component.translatable("runecraftory.advancements.crafting.chemistry.title"), Component.translatable("runecraftory.advancements.crafting.chemistry.description"), null, AdvancementType.TASK, true, true, false).addCriterion("craft", RuneCraftoryCriteria.MEDICINE.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()))).save(cons, LibAdvancements.MAKE_MEDICINE.toString());
            AdvancementHolder cookingItem = Advancement.Builder.advancement().parent(root).display(RuneCraftoryItems.COOKING_TABLE.get(), Component.translatable("runecraftory.advancements.crafting.cooking.title"), Component.translatable("runecraftory.advancements.crafting.cooking.description"), null, AdvancementType.TASK, true, true, false).addCriterion("craft", RuneCraftoryCriteria.COOKING.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()))).save(cons, LibAdvancements.COOK.toString());

            AdvancementHolder upgradeItem = Advancement.Builder.advancement().parent(craftingItem).display(RuneCraftoryItems.CHEAP_BRACELET.get(), Component.translatable("runecraftory.advancements.upgrade.title"), Component.translatable("runecraftory.advancements.upgrade.description"), null, AdvancementType.TASK, true, true, false).addCriterion("upgrade", RuneCraftoryCriteria.UPGRADE_ITEM.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()))).save(cons, LibAdvancements.UPGRADE_ITEM.toString());
            AdvancementHolder changeElement = Advancement.Builder.advancement().parent(forgingItem).display(RuneCraftoryItems.CRYSTAL_LOVE.get(), Component.translatable("runecraftory.advancements.change.element.title"), Component.translatable("runecraftory.advancements.change.element.description"), null, AdvancementType.TASK, true, true, false).addCriterion("element", RuneCraftoryCriteria.CHANGE_ELEMENT.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()))).save(cons, LibAdvancements.CHANGE_ELEMENT.toString());
            AdvancementHolder lightOre = Advancement.Builder.advancement().parent(changeElement).display(RuneCraftoryItems.LIGHT_ORE.get(), Component.translatable("runecraftory.advancements.lightore.title"), Component.translatable("runecraftory.advancements.lightore.description"), null, AdvancementType.GOAL, true, true, false).addCriterion("light_ore", RuneCraftoryCriteria.LIGHT_ORE.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()))).save(cons, LibAdvancements.LIGHT_ORE.toString());
            AdvancementHolder spell = Advancement.Builder.advancement().parent(forgingItem).display(RuneCraftoryItems.FIRE_BALL_SMALL.get(), Component.translatable("runecraftory.advancements.spell.title"), Component.translatable("runecraftory.advancements.spell.description"), null, AdvancementType.TASK, true, true, false).addCriterion("spell", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(RunecraftoryTags.Items.SPELLS).build())).save(cons, LibAdvancements.SPELL.toString());
            AdvancementHolder changeSpell = Advancement.Builder.advancement().parent(forgingItem).display(RuneCraftoryItems.ROD.get(), Component.translatable("runecraftory.advancements.change.spell.title"), Component.translatable("runecraftory.advancements.change.spell.description"), null, AdvancementType.TASK, true, true, false).addCriterion("spell", RuneCraftoryCriteria.CHANGE_SPELL.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()))).save(cons, LibAdvancements.CHANGE_SPELL.toString());

            AdvancementHolder fertilizer = Advancement.Builder.advancement().parent(root).display(RuneCraftoryItems.FORMULAR_A.get(), Component.translatable("runecraftory.advancements.fertilizer.title"), Component.translatable("runecraftory.advancements.fertilizer.description"), null, AdvancementType.TASK, true, true, false).addCriterion("fertilizer", RuneCraftoryCriteria.FERTILIZE_FARM.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()))).save(cons, LibAdvancements.FERTILIZER.toString());
            AdvancementHolder giantCrop = Advancement.Builder.advancement().parent(fertilizer).display(RuneCraftoryItems.TURNIP_GIANT.get(), Component.translatable("runecraftory.advancements.giant_crop.title"), Component.translatable("runecraftory.advancements.giant_crop.description"), null, AdvancementType.GOAL, true, true, false).addCriterion("giant_crop", CropHarvestTrigger.TriggerInstance.harvest(RunecraftoryTags.Blocks.GIANT_CROP_BLOCKS)).save(cons, LibAdvancements.GIANT_CROPS.toString());
            AdvancementHolder helper = Advancement.Builder.advancement().parent(fertilizer).display(RuneCraftoryItems.MOB_STAFF.get(), Component.translatable("runecraftory.advancements.monster.help.title"), Component.translatable("runecraftory.advancements.monster.help.description"), null, AdvancementType.TASK, true, true, false).addCriterion("farming", RuneCraftoryCriteria.COMMAND_FARMING.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()))).save(cons, LibAdvancements.HELPER.toString());
            AdvancementHolder hightTierTool = Advancement.Builder.advancement().parent(helper).display(RuneCraftoryItems.WATERING_CAN_PLATINUM.get(), Component.translatable("runecraftory.advancements.final.tool.title"), Component.translatable("runecraftory.advancements.final.tool.description"), null, AdvancementType.CHALLENGE, true, true, false).addCriterion("final_tool", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(RunecraftoryTags.Items.HIGH_TIER_TOOLS).build())).save(cons, LibAdvancements.HIGH_TIER_TOOL.toString());

            AdvancementHolder rootProgression = Advancement.Builder.advancement().display(RuneCraftoryItems.SHORT_DAGGER.get(), Component.translatable("runecraftory.advancements.progression.root.title"), Component.translatable("runecraftory.advancements.progression.root.description"), ResourceLocation.withDefaultNamespace("textures/block/dirt.png"), AdvancementType.TASK, false, false, false).addCriterion("crafting_table", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.CRAFTING_TABLE)).save(cons, LibAdvancements.ROOT_PROGRESSION.toString());
            Advancement.Builder builder = Advancement.Builder.advancement().display(SpawnEgg.fromType(RuneCraftoryEntities.WOOLY.get()).get(),
                            Component.translatable("runecraftory.advancements.progression.boss.greater_demon.title"),
                            Component.translatable("runecraftory.advancements.progression.boss.greater_demon.description"),
                            ResourceLocation.withDefaultNamespace("textures/block/dirt.png"), AdvancementType.TASK, true, true, false)
                    .addCriterion("dummy", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()));
            AdvancementHolder greater_demon = builder.save(cons, LibAdvancements.GREATER_DEMON.toString());
            AdvancementHolder chimera = bossProgression(RuneCraftoryEntities.CHIMERA, cons, LibAdvancements.CHIMERA, greater_demon);
            AdvancementHolder rafflesia = bossProgression(RuneCraftoryEntities.RAFFLESIA, cons, LibAdvancements.RAFFLESIA, chimera);
            AdvancementHolder grimoire = bossProgression(RuneCraftoryEntities.GRIMOIRE, cons, LibAdvancements.GRIMOIRE, rafflesia);
            AdvancementHolder deadTree = bossProgression(RuneCraftoryEntities.DEAD_TREE, cons, LibAdvancements.DEAD_TREE, rootProgression);
            AdvancementHolder raccoon = bossProgression(RuneCraftoryEntities.RACCOON, cons, LibAdvancements.RACCOON, rootProgression);
            AdvancementHolder skelefang = bossProgression(RuneCraftoryEntities.SKELEFANG, cons, LibAdvancements.SKELEFANG, raccoon);
            AdvancementHolder ambrosia = bossProgression(RuneCraftoryEntities.AMBROSIA, cons, LibAdvancements.AMBROSIA, rootProgression);
            AdvancementHolder thunderbolt = bossProgression(RuneCraftoryEntities.THUNDERBOLT, cons, LibAdvancements.THUNDERBOLT, ambrosia);
            AdvancementHolder marionetta = bossProgression(RuneCraftoryEntities.MARIONETTA, cons, LibAdvancements.MARIONETTA, thunderbolt);
            AdvancementHolder handonetta = bossProgression(RuneCraftoryEntities.HANDONETTA, cons, LibAdvancements.HANDONETTA, thunderbolt);
            builder = Advancement.Builder.advancement().display(SpawnEgg.fromType(RuneCraftoryEntities.SANO.get()).get(),
                            Component.translatable("runecraftory.advancements.progression.boss.sano_uno.title"),
                            Component.translatable("runecraftory.advancements.progression.boss.sano_uno.description"),
                            ResourceLocation.withDefaultNamespace("textures/block/dirt.png"), AdvancementType.TASK, true, true, false)
                    .addCriterion("boss1", KilledTrigger.TriggerInstance.playerKilledEntity(
                            EntityPredicate.Builder.entity().of(RuneCraftoryEntities.SANO.get()),
                            DamageSourcePredicate.Builder.damageType()
                                    .source(LibAdvancements.playerAdvancementCheck(marionetta.id()))))
                    .addCriterion("boss2", KilledTrigger.TriggerInstance.playerKilledEntity(
                            EntityPredicate.Builder.entity().of(RuneCraftoryEntities.UNO.get()),
                            DamageSourcePredicate.Builder.damageType()
                                    .source(LibAdvancements.playerAdvancementCheck(marionetta.id()))))
                    .parent(marionetta);
            AdvancementHolder sano_uno = builder.save(cons, LibAdvancements.SANO_UNO.toString());
            AdvancementHolder sarcophagus = bossProgression(RuneCraftoryEntities.SARCOPHAGUS, cons, LibAdvancements.SARCOPHAGUS, sano_uno);
        }

        private static ItemStack glowing(ItemLike itemLike) {
            ItemStack stack = new ItemStack(itemLike.asItem());
            stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
            return stack;
        }

        protected static Advancement.Builder allBosses(Advancement.Builder builder) {
            for (RegistryEntrySupplier<EntityType<?>, ?> sup : RuneCraftoryEntities.getBosses()) {
                builder.addCriterion(sup.getID().toString(), TameMonsterTrigger.TriggerInstance.of(1, new EntityPredicate.Builder().of(sup.get())));
            }
            return builder;
        }

        private static AdvancementHolder bossProgression(RegistryEntrySupplier<EntityType<?>, ?> entity, Consumer<AdvancementHolder> cons, ResourceLocation id, AdvancementHolder parent) {
            return bossProgression(entity, cons, id, parent, false);
        }

        private static AdvancementHolder bossProgression(RegistryEntrySupplier<EntityType<?>, ?> entity, Consumer<AdvancementHolder> cons, ResourceLocation id, AdvancementHolder parent, boolean hidden) {
            Criterion<KilledTrigger.TriggerInstance> trigger = parent != null ? KilledTrigger.TriggerInstance.playerKilledEntity(
                    EntityPredicate.Builder.entity().of(entity.get()),
                    DamageSourcePredicate.Builder.damageType()
                            .source(LibAdvancements.playerAdvancementCheck(parent.id()))) :
                    KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(entity.get()));
            Advancement.Builder builder = Advancement.Builder.advancement().display(SpawnEgg.fromType(entity.get()).get(),
                            Component.translatable(String.format("runecraftory.advancements.progression.boss.%s.title", entity.getID().getPath())),
                            Component.translatable(String.format("runecraftory.advancements.progression.boss.%s.description", entity.getID().getPath())),
                            ResourceLocation.withDefaultNamespace("textures/block/dirt.png"), AdvancementType.TASK, true, true, hidden)
                    .addCriterion("boss", trigger);
            if (parent != null)
                builder.parent(parent);
            return builder.save(cons, id.toString());
        }
    }
}
