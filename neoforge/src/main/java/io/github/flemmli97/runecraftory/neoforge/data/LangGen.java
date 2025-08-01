package io.github.flemmli97.runecraftory.neoforge.data;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.api.calendar.DayOfWeek;
import io.github.flemmli97.runecraftory.api.calendar.Season;
import io.github.flemmli97.runecraftory.api.calendar.Weather;
import io.github.flemmli97.runecraftory.api.datapack.provider.AdditionalLanguages;
import io.github.flemmli97.runecraftory.api.registry.NPCProfession;
import io.github.flemmli97.runecraftory.common.blocks.ShippingBinBlock;
import io.github.flemmli97.runecraftory.common.blocks.entity.CraftingBlockEntity;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.npc.profession.BathhouseAttendant;
import io.github.flemmli97.runecraftory.common.entities.npc.profession.Blacksmith;
import io.github.flemmli97.runecraftory.common.entities.npc.profession.Chef;
import io.github.flemmli97.runecraftory.common.entities.npc.profession.Doctor;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerInfoScreen;
import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolAxe;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolFishingRod;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolHammer;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolHoe;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolSickle;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolWateringCan;
import io.github.flemmli97.runecraftory.common.network.C2SNPCInteraction;
import io.github.flemmli97.runecraftory.common.network.C2SSetMonsterBehaviour;
import io.github.flemmli97.runecraftory.common.quests.QuestData;
import io.github.flemmli97.runecraftory.common.quests.tasks.LevelTask;
import io.github.flemmli97.runecraftory.common.quests.tasks.NPCTalkTask;
import io.github.flemmli97.runecraftory.common.quests.tasks.ShippingTask;
import io.github.flemmli97.runecraftory.common.quests.tasks.SkillLevelTask;
import io.github.flemmli97.runecraftory.common.quests.tasks.TamingTask;
import io.github.flemmli97.runecraftory.common.recipes.CraftingType;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryBlocks;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryCreativeRuneCraftoryTabs;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryDamageType;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEffects;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCProfessions;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Same as LanguageProvider but with a linked hashmap
 */
public class LangGen implements DataProvider {

    private final Map<String, String> data = new HashMap<>();
    private final PackOutput packOutput;
    private final String modid, locale;

    private final AdditionalLanguages[] additionalLanguages;

    public LangGen(PackOutput packOutput, AdditionalLanguages... additionalLanguages) {
        this.packOutput = packOutput;
        this.modid = RuneCraftory.MODID;
        this.locale = "en_us";
        this.additionalLanguages = additionalLanguages;
    }

    protected void addTranslations() {
        //Static order
        this.add(RuneCraftoryItems.HOE_SCRAP.get(), "Cheap Hoe");
        this.add(RuneCraftoryItems.HOE_IRON.get(), "Sturdy Hoe");
        this.add(RuneCraftoryItems.HOE_SILVER.get(), "Seasoned Hoe");
        this.add(RuneCraftoryItems.HOE_GOLD.get(), "Shiny Hoe");
        this.add(RuneCraftoryItems.HOE_PLATINUM.get(), "Blessed Hoe");
        this.add(RuneCraftoryItems.WATERING_CAN_SCRAP.get(), "Cheap Waterpot");
        this.add(RuneCraftoryItems.WATERING_CAN_IRON.get(), "Tin Waterpot");
        this.add(RuneCraftoryItems.WATERING_CAN_SILVER.get(), "Lion Waterpot");
        this.add(RuneCraftoryItems.WATERING_CAN_GOLD.get(), "Rainbow Waterpot");
        this.add(RuneCraftoryItems.WATERING_CAN_PLATINUM.get(), "Joy Waterpot");
        this.add(RuneCraftoryItems.SICKLE_SCRAP.get(), "Cheap Sickle");
        this.add(RuneCraftoryItems.SICKLE_IRON.get(), "Iron Sickle");
        this.add(RuneCraftoryItems.SICKLE_SILVER.get(), "Quality Sickle");
        this.add(RuneCraftoryItems.SICKLE_GOLD.get(), "Super Sickle");
        this.add(RuneCraftoryItems.SICKLE_PLATINUM.get(), "Legendary Sickle");
        this.add(RuneCraftoryItems.HAMMER_SCRAP.get(), "Cheap Hammer");
        this.add(RuneCraftoryItems.HAMMER_IRON.get(), "Iron Hammer");
        this.add(RuneCraftoryItems.HAMMER_SILVER.get(), "Silver Hammer");
        this.add(RuneCraftoryItems.HAMMER_GOLD.get(), "Golden Hammer");
        this.add(RuneCraftoryItems.HAMMER_PLATINUM.get(), "Platinum Hammer");
        this.add(RuneCraftoryItems.AXE_SCRAP.get(), "Cheap Axe");
        this.add(RuneCraftoryItems.AXE_IRON.get(), "Chopping Axe");
        this.add(RuneCraftoryItems.AXE_SILVER.get(), "Lumber Axe");
        this.add(RuneCraftoryItems.AXE_GOLD.get(), "Mountain Axe");
        this.add(RuneCraftoryItems.AXE_PLATINUM.get(), "Miracle Axe");
        this.add(RuneCraftoryItems.FISHING_ROD_SCRAP.get(), "Cheap Pole");
        this.add(RuneCraftoryItems.FISHING_ROD_IRON.get(), "Beginner's Pole");
        this.add(RuneCraftoryItems.FISHING_ROD_SILVER.get(), "Skilled Pole");
        this.add(RuneCraftoryItems.FISHING_ROD_GOLD.get(), "Famous Pole");
        this.add(RuneCraftoryItems.FISHING_ROD_PLATINUM.get(), "Sacred Pole");
        for (RegistryEntrySupplier<Item, ?> sup : RuneCraftoryItems.ITEMS.getEntries()) {
            if (sup.get() instanceof ItemToolAxe || sup.get() instanceof ItemToolHoe || sup.get() instanceof ItemToolWateringCan
                    || sup.get() instanceof ItemToolSickle || sup.get() instanceof ItemToolHammer || sup.get() instanceof ItemToolFishingRod)
                continue;
            if (sup == RuneCraftoryItems.STEEL_TEN) {
                this.add(sup.get(), "10-Fold Steel");
            } else if (sup == RuneCraftoryItems.MTGU_PLATE) {
                this.add(sup.get(), "MGTU Plate");
            } else if (sup == RuneCraftoryItems.FUR_SMALL) {
                this.add(sup.get(), "Fur (S)");
            } else if (sup == RuneCraftoryItems.FUR_MEDIUM) {
                this.add(sup.get(), "Fur (M)");
            } else if (sup == RuneCraftoryItems.FUR_LARGE) {
                this.add(sup.get(), "Fur (L)");
            } else if (sup == RuneCraftoryItems.EGG_S) {
                this.add(sup.get(), "Egg (S)");
            } else if (sup == RuneCraftoryItems.EGG_M) {
                this.add(sup.get(), "Egg (M)");
            } else if (sup == RuneCraftoryItems.EGG_L) {
                this.add(sup.get(), "Egg (L)");
            } else if (sup == RuneCraftoryItems.MILK_S) {
                this.add(sup.get(), "Milk (S)");
            } else if (sup == RuneCraftoryItems.MILK_M) {
                this.add(sup.get(), "Milk (M)");
            } else if (sup == RuneCraftoryItems.MILK_L) {
                this.add(sup.get(), "Milk (L)");
            } else if (sup == RuneCraftoryItems.GRAPE_SAPLING) {
                this.add(sup.get(), "Grape Sapling?");
            } else if (sup.get() instanceof SpawnEgg)
                this.add(sup.get(), "%s" + " Spawn Egg");
//            else if (sup.get() instanceof RecordItem record) {
//                this.add(sup.get(), "Music Disc");
//                this.add(record.getDescriptionId() + ".desc", this.simpleTranslation(sup.getID().getPath()
//                        .replace("music_disc_", "").replace("-", "_")));
//            }
            else
                this.add(sup.get(), this.simpleTranslation(sup.getID()));
        }

        this.add("runecraftory.item.creative.tooltip", "Debug item used for testing things");
        this.add("runecraftory.item.creative.tooltip.mode", "Mode: %s");
        this.add("runecraftory.item.creative.tooltip.mode.default", "Default");
        this.add("runecraftory.item.creative.tooltip.mode.animation", "Entity Animations");

        for (RegistryEntrySupplier<Block, ?> sup : RuneCraftoryBlocks.BLOCKS.getEntries()) {
            this.add(sup.get(), this.simpleTranslation(sup.getID()));
        }

        this.add(CraftingBlockEntity.DISPLAY_PREFIX + CraftingType.FORGE.getId(), "Forging");
        this.add(CraftingBlockEntity.DISPLAY_PREFIX + CraftingType.ACCESSORY_WORKBENCH.getId(), "Crafting");
        this.add(CraftingBlockEntity.DISPLAY_PREFIX + CraftingType.CHEMISTRY_SET.getId(), "Chemistry");
        this.add(CraftingBlockEntity.DISPLAY_PREFIX + CraftingType.COOKING_TABLE.getId(), "Cooking");

        this.add(ShippingBinBlock.NAME, "Shipping Bin");
        this.add(ContainerInfoScreen.TITLE, "Info Screen");
        this.add(ContainerInfoScreen.TITLE_SUB, "Info Screen");

        for (RegistryEntrySupplier<EntityType<?>, ?> sup : RuneCraftoryEntities.ENTITIES.getEntries()) {
            if (sup.get() == RuneCraftoryEntities.SARCOPHAGUS_TELEPORTER.get()) {
                this.add(sup.get(), "Teleporter");
            } else {
                this.add(sup.get(), this.simpleTranslation(sup.getID()));
            }
        }

        for (RegistryEntrySupplier<Attribute, ? extends Attribute> sup : RuneCraftoryAttributes.ATTRIBUTES.getEntries()) {
            this.add(sup.get().getDescriptionId(), this.simpleTranslation(sup.getID()));
        }
        for (RegistryEntrySupplier<MobEffect, ?> reg : RuneCraftoryEffects.EFFECTS.getEntries()) {
            this.add(reg.get(), this.simpleTranslation(reg.getID()));
        }
        for (RegistryEntrySupplier<SoundEvent, ? extends SoundEvent> reg : RuneCraftorySounds.SOUND_EVENTS.getEntries()) {
            this.add(reg.asHolder());
        }

        for (DayOfWeek day : DayOfWeek.values()) {
            this.add(day.translation(), day.toString().substring(0, 3));
            String d = day.toString().toLowerCase(Locale.ROOT);
            this.add(day.translationFull(), d.substring(0, 1).toUpperCase(Locale.ROOT) + d.substring(1));
        }

        for (ItemElement element : ItemElement.values()) {
            this.add(element.getTranslation(), "Attribute: " + this.simpleTranslation(element.getTranslation().replace(ItemElement.PREFIX, "")));
        }

        for (Season season : Season.values()) {
            this.add(season.translationKey(), this.simpleTranslation(season.translationKey().replace(Season.PREFIX, "")));
        }

        for (Weather weather : Weather.values()) {
            this.add(weather.translation, this.simpleTranslation(weather.translation.replace(Weather.PREFIX, "")));
        }

        for (Skills s : Skills.values()) {
            this.add(s.getTranslation(),
                    this.capitalize(s.getTranslation().replace(Skills.PREFIX, "").replace("_", " "),
                            Lists.newArrayList("and")));
        }

        this.add("runecraftory.generic.yes", "Yes");
        this.add("runecraftory.generic.no", "No");

        this.add("runecraftory.tooltip.item.level", "Level: %s");
        this.add("runecraftory.tooltip.item.buy", "Buy: %sG");
        this.add("runecraftory.tooltip.item.sell", "Sell: %sG");
        this.add("runecraftory.tooltip.item.difficulty", "Upgrade Difficulty: %s");
        this.add("runecraftory.tooltip.item.upgrade", "Upgrade");
        this.add("runecraftory.tooltip.item.attribute", "%s: %s");
        this.add("runecraftory.tooltip.item.attribute.percentage", "%s: %s%%");

        this.add("runecraftory.tooltip.item.eaten", "When eaten");

        this.add("runecraftory.tooltip.item.spawn", "Right click in air to configure spawnegg");
        this.add("runecraftory.tooltip.item.prop", "A prop item. Takes on the items look when in another entities hand");

        this.add("runecraftory.tooltip.item.treasure_chest", "Shift-right-click to cycle through loot tier");
        this.add("runecraftory.tooltip.item.treasure_level", "Chest tier lvl: %s");
        this.add("runecraftory.tooltip.debug.stat", "Itemstat-ID: %s");
        this.add("runecraftory.tooltip.debug.crop", "Cropdata-ID: %s");
        this.add("runecraftory.tooltip.debug.food", "Fooddata-ID: %s");
        this.add("runecraftory.tooltip.sapling", "Needs to be placed on farmland");

        this.add("runecraftory.tooltip.crops.season.best", "Good Season: %s");
        this.add("runecraftory.tooltip.crops.season.bad", "Bad Season: %s");
        this.add("runecraftory.tooltip.crops.entry.2", "%s - %s");
        this.add("runecraftory.tooltip.crops.entry.3", "%s - %s - %s");
        this.add("runecraftory.tooltip.crops.growth", "Growth: %sd");
        this.add("runecraftory.tooltip.crops.harvested", "Harvest: %s");
        this.add("runecraftory.tooltip.crops.regrowable", "Regrowable");

        this.add("runecraftory.tooltip.baby.boy", "Boy");
        this.add("runecraftory.tooltip.baby.girl", "Girl");
        this.add("runecraftory.tooltip.baby.owner", "Parent: %s");

        for (ResourceKey<DamageType> types : RuneCraftoryDamageType.ATTACK_TYPES) {
            this.add("death.attack." + types.location().toLanguageKey(), "%1$s was knocked down by %2$s");
            this.add("death.attack." + types.location().toLanguageKey() + ".player", "%1$s was knocked down by %2$s");
            this.add("death.attack." + types.location().toLanguageKey() + ".item", "%1$s was knocked down by %2$s using %3$s");
        }
        this.add("death.attack." + RuneCraftoryDamageType.EXHAUST.location().toLanguageKey(), "%1$s fainted");
        this.add("death.attack." + RuneCraftoryDamageType.EXHAUST.location().toLanguageKey() + ".player", "%1$s fainted while fighting %2$s");
        this.add("death.attack." + RuneCraftoryDamageType.EXHAUST.location().toLanguageKey() + ".item", "%1$s fainted while fighting %2$s");
        this.add("death.attack." + RuneCraftoryDamageType.STRONG_POISON.location().toLanguageKey(), "%1$s was to weak and died of poison");
        this.add("death.attack." + RuneCraftoryDamageType.STRONG_POISON.location().toLanguageKey() + ".player", "%1$s was to weak and died of poison while fighting %2$s");
        this.add("death.attack." + RuneCraftoryDamageType.STRONG_POISON.location().toLanguageKey() + ".item", "%1$s was to weak and died of poison  while fighting %2$s using %3$s");

        this.add(BaseMonster.Behaviour.WANDER_HOME.interactKey, "You send %s home");
        this.add(BaseMonster.Behaviour.FOLLOW.interactKey, "%s is now following you");
        this.add(BaseMonster.Behaviour.FOLLOW_DISTANCE.interactKey, "%s is now following you with distance");
        this.add(BaseMonster.Behaviour.STAY.interactKey, "%s is now staying");
        this.add(BaseMonster.Behaviour.WANDER.interactKey, "%s is now wandering around in this area");
        this.add(BaseMonster.Behaviour.FARM.interactKey, "%s is now tending the crops");
        this.add("runecraftory.monster.interact.notowner", "This is not your monster!");
        this.add("runecraftory.monster.interact.party.full", "Your party is full");
        this.add("runecraftory.monster.interact.ride.no", "You can't ride this monster");
        this.add("runecraftory.monster.interact.barn.no", "%s has no barn");
        this.add("runecraftory.monster.interact.barn.no.ext", "%1$s [%2$s] has no home to go to.");
        this.add("runecraftory.barn.interact.not.owner", "This barn belongs to %s.");
        this.add("runecraftory.barn.interact.block", "Barn with capacity %1$s (Free: %2$s).");
        this.add("runecraftory.barn.interact.block.roofed", "Roofed barn with capacity %1$s (Free: %2$s).");

        for (RegistryEntrySupplier<CreativeModeTab, ? extends CreativeModeTab> tab : RuneCraftoryCreativeRuneCraftoryTabs.CREATIVE_MODE_TABS.getEntries()) {
            this.add("itemGroup." + tab.getID().getNamespace() + "." + tab.getID().getPath(), this.simpleTranslation(tab.getID().getPath()));
        }

        this.add("runecraftory.keycategory", "Runecraftory");
        this.add("runecraftory.key.spell_1", "Interaction Key 1");
        this.add("runecraftory.key.spell_2", "Interaction Key 2");
        this.add("runecraftory.key.spell_3", "Interaction Key 3");
        this.add("runecraftory.key.spell_4", "Interaction Key 4");

        this.add("runecraftory.command.skill.no", "No such skill %s!");
        this.add("runecraftory.command.skill.lvl.add", "Added %3$s %1$s skill level to %2$s");
        this.add("runecraftory.command.skill.xp.add", "Added %3$s %1$s skill xp points to %2$s");
        this.add("runecraftory.command.skill.lvl.set", "Set level of skill %1$s to %3$s for %2$s");
        this.add("runecraftory.command.lvl.xp.add", "Added %1$s xp points to %2$s");
        this.add("runecraftory.command.lvl.set", "Set level of %1$s to %2$s");
        this.add("runecraftory.command.reset.all", "Reset all player data for %s");
        this.add("runecraftory.command.unlock.recipes", "Unlocked all crafting recipes for %s");
        this.add("runecraftory.command.unlock.recipe", "Unlockeded crafting %2$s recipes for %1$s");
        this.add("runecraftory.command.reset.recipe", "Locked all crafting recipes for %s again");
        this.add("runecraftory.command.weather.no", "No such weather %s");
        this.add("runecraftory.command.set.weather", "Set current weather to %s");
        this.add("runecraftory.command.recalc.stats", "Recalculated level stats for %s entities");

        this.add("runecraftory.recipe_integration.locked", "Unknown Recipe");
        this.add("runecraftory.recipe_integration.crafting_level", "Lvl: %s");

        this.add(C2SSetMonsterBehaviour.Action.HOME.translation, "Send home");
        this.add(C2SSetMonsterBehaviour.Action.FOLLOW.translation, "Follow");
        this.add(C2SSetMonsterBehaviour.Action.FOLLOW_DISTANCE.translation, "Stay back");
        this.add(C2SSetMonsterBehaviour.Action.STAY.translation, "Stay");
        this.add(C2SSetMonsterBehaviour.Action.WANDER.translation, "Wander");
        this.add(C2SSetMonsterBehaviour.Action.FARM.translation, "Tend the crops");
        this.add(C2SSetMonsterBehaviour.Action.HARVESTINV.translation, "Crop Inventory");
        this.add(C2SSetMonsterBehaviour.Action.SEEDINV.translation, "Seed Inventory");
        this.add(C2SSetMonsterBehaviour.Action.RIDE.translation, "Ride");
        this.add(C2SSetMonsterBehaviour.Action.CENTER.translation, "Set center");

        this.add("runecraftory.behaviour.home.position", "Updated restriction center");
        this.add("runecraftory.behaviour.inventory.harvest", "Updated crop inventory position");
        this.add("runecraftory.behaviour.inventory.harvest.invalid", "Position is too far away");
        this.add("runecraftory.behaviour.inventory.seed", "Updated seed inventory position");
        this.add("runecraftory.behaviour.inventory.seed.invalid", "Position is too far away");

        this.add("runecraftory.tamed.monster.knockout.by", "%1$s got knocked out by %5$s at [%2$s, %3$s, %4$s]");
        this.add("runecraftory.tamed.monster.knockout", "%1$s got knocked out at [%2$s, %3$s, %4$s]");

        this.add("runecraftory.magnifying_glass.view.crop.growth", "Growth: %s");
        this.add("runecraftory.magnifying_glass.view.crop.level", "Level: %s");
        this.add("runecraftory.magnifying_glass.view.crop.giant", "Giant prog.: %s");
        this.add("runecraftory.magnifying_glass.view.speed", "Speed: %s");
        this.add("runecraftory.magnifying_glass.view.health", "Health: %s");
        this.add("runecraftory.magnifying_glass.view.level", "Quality: %s");
        this.add("runecraftory.magnifying_glass.view.giant", "Size: %s");
        this.add("runecraftory.magnifying_glass.view.defence", "Defence: %s");

        this.add("runecraftory.gui.date.format", "%s %s");

        this.add(C2SNPCInteraction.Action.TALK.translation, "Talk");
        this.add(C2SNPCInteraction.Action.FOLLOW.translation, "Follow me");
        this.add(C2SNPCInteraction.Action.FOLLOWDISTANCE.translation, "Stay back a bit");
        this.add(C2SNPCInteraction.Action.STAY.translation, "Stay here");
        this.add(C2SNPCInteraction.Action.STOPFOLLOW.translation, "Stop following");
        this.add(C2SNPCInteraction.Action.SHOP.translation, "I want to shop");
        this.add("runecraftory.gui.level", "Level");
        this.add("runecraftory.gui.npc.id", "Npc data-id");
        this.add("runecraftory.gui.npc.profession", "Npc Profession");
        this.add("runecraftory.gui.save", "Save");
        this.add("runecraftory.gui.crafting.rpMax.missing", "Missing total rp");
        this.add("runecraftory.gui.display.level", "Level: %s");
        this.add("runecraftory.gui.npc.shop.owner", "Owner of %s");
        this.add("runecraftory.gui.npc.bed.no", "I don't have a bed");
        this.add("runecraftory.gui.npc.workplace.no", "I don't have a work place. Valid workplaces are [%s]");
        this.add("runecraftory.gui.npc.parent", "Parent:");
        this.add("runecraftory.gui.npc.parents", "Parents:");
        this.add("runecraftory.gui.npc.relationship.dating", "Dating %s");
        this.add("runecraftory.gui.npc.relationship.married", "Married to %s");
        this.add("runecraftory.gui.npc.procreate", "Procreate");

        this.add("runecraftory.gui.quests.accept", "Accept");
        this.add("runecraftory.gui.quests.reset", "Cancel");
        this.add("runecraftory.gui.quest.submit.button", "Submit");
        this.add("runecraftory.gui.quest.button", "Requests");

        this.add("runecraftory.advancements.root.title", "Runecraftory");
        this.add("runecraftory.advancements.root.description", "A minecrafty harvest moon");
        this.add("runecraftory.advancements.tame.first.title", "First buddy");
        this.add("runecraftory.advancements.tame.first.description", "Tame your first monster");
        this.add("runecraftory.advancements.tame.ten.title", "Monster tamer");
        this.add("runecraftory.advancements.tame.ten.description", "Tame 10 monster");
        this.add("runecraftory.advancements.tame.boss.title", "Boss tamer");
        this.add("runecraftory.advancements.tame.boss.description", "Tame a boss monster");
        this.add("runecraftory.advancements.tame.boss.five.title", "More Bosses");
        this.add("runecraftory.advancements.tame.boss.five.description", "Tame 5 boss monster");
        this.add("runecraftory.advancements.tame.boss.all.title", "Legendary Hunter!");
        this.add("runecraftory.advancements.tame.boss.all.description", "Tame all the boss monsters");

        this.add("runecraftory.advancements.shipping.title", "First earnings");
        this.add("runecraftory.advancements.shipping.description", "Ship your first item");
        this.add("runecraftory.advancements.shipping.fifty.title", "Shipping Milestone");
        this.add("runecraftory.advancements.shipping.fifty.description", "Ship 50 different items");
        this.add("runecraftory.advancements.shop.title", "Time for shopping");
        this.add("runecraftory.advancements.shop.description", "Buy your first item");
        this.add("runecraftory.advancements.100k.title", "10 OK!");
        this.add("runecraftory.advancements.100k.description", "Reach 100k gold");
        this.add("runecraftory.advancements.million.title", "One Million!");
        this.add("runecraftory.advancements.million.description", "Have one million gold");

        this.add("runecraftory.advancements.skill.weapon.5.title", "Gotta start somewhere");
        this.add("runecraftory.advancements.skill.weapon.5.description", "Get to level 5 in any weapon skill");
        this.add("runecraftory.advancements.skill.10.title", "Skill Level 10");
        this.add("runecraftory.advancements.skill.10.description", "Get to level 10 in any skill");
        this.add("runecraftory.advancements.skill.25.title", "Skill Level 25");
        this.add("runecraftory.advancements.skill.25.description", "Get to level 25 in any skill");
        this.add("runecraftory.advancements.skill.50.title", "Skill Level 50");
        this.add("runecraftory.advancements.skill.50.description", "Get to level 50 in any skill");
        this.add("runecraftory.advancements.skill.100.title", "Skill Level 100");
        this.add("runecraftory.advancements.skill.100.description", "Get to level 100 in any skill");

        this.add("runecraftory.advancements.level.10.title", "Small steps");
        this.add("runecraftory.advancements.level.10.description", "Get to level 10");
        this.add("runecraftory.advancements.level.25.title", "Level 25");
        this.add("runecraftory.advancements.level.25.description", "Get to level 25");
        this.add("runecraftory.advancements.level.50.title", "Level 50");
        this.add("runecraftory.advancements.level.50.description", "Get to level 50");
        this.add("runecraftory.advancements.level.100.title", "Level 100");
        this.add("runecraftory.advancements.level.100.description", "Get to level 100");

        this.add("runecraftory.advancements.crafting.forging.title", "Art of Forging");
        this.add("runecraftory.advancements.crafting.forging.description", "Craft a weapon using the forge");
        this.add("runecraftory.advancements.crafting.armor.title", "Art of Protection");
        this.add("runecraftory.advancements.crafting.armor.description", "Craft an armor piece using the accessory workbench");
        this.add("runecraftory.advancements.crafting.chemistry.title", "Art of Alchemy");
        this.add("runecraftory.advancements.crafting.chemistry.description", "Make some medicine using the chemistry set");
        this.add("runecraftory.advancements.crafting.cooking.title", "Culinary Art");
        this.add("runecraftory.advancements.crafting.cooking.description", "Make some food using the cooking table");

        this.add("runecraftory.advancements.upgrade.title", "Better equipment");
        this.add("runecraftory.advancements.upgrade.description", "Upgrade any equipment");
        this.add("runecraftory.advancements.change.element.title", "Its super effective!");
        this.add("runecraftory.advancements.change.element.description", "Change an element of a weapon");
        this.add("runecraftory.advancements.spell.title", "Magick");
        this.add("runecraftory.advancements.spell.description", "Find or craft a spell");
        this.add("runecraftory.advancements.change.spell.title", "Staff power");
        this.add("runecraftory.advancements.change.spell.description", "Change or add a spell to a staff");
        this.add("runecraftory.advancements.lightore.title", "Faker");
        this.add("runecraftory.advancements.lightore.description", "Transfer the stats of a weapon to another using a light ore");

        this.add("runecraftory.advancements.fertilizer.title", "Increased production");
        this.add("runecraftory.advancements.fertilizer.description", "Use a fertilizer to improve your farmland");
        this.add("runecraftory.advancements.giant_crop.title", "Big Boy");
        this.add("runecraftory.advancements.giant_crop.description", "Grow and harvest a giant crop");
        this.add("runecraftory.advancements.monster.help.title", "Useful helper");
        this.add("runecraftory.advancements.monster.help.description", "Command a monster to help you out with farming");
        this.add("runecraftory.advancements.final.tool.title", "The best");
        this.add("runecraftory.advancements.final.tool.description", "Obtain a final tier tool");

        this.add("runecraftory.advancements.progression.root.title", "Runecraftory - Milestones");
        this.add("runecraftory.advancements.progression.root.description", "Defeating them will increase overall mob levels!");
        this.add("runecraftory.advancements.progression.boss.greater_demon.title", "Monke!");
        this.add("runecraftory.advancements.progression.boss.greater_demon.description", "Defeat the greater demon. But I don't think he exists yet!");
        this.add("runecraftory.advancements.progression.boss.chimera.title", "What is this creature!");
        this.add("runecraftory.advancements.progression.boss.chimera.description", "Defeat a chimera in a water ruin");
        this.add("runecraftory.advancements.progression.boss.rafflesia.title", "Status gallore");
        this.add("runecraftory.advancements.progression.boss.rafflesia.description", "Defeat rafflesia in the nether");
        this.add("runecraftory.advancements.progression.boss.grimoire.title", "Don't get blown away");
        this.add("runecraftory.advancements.progression.boss.grimoire.description", "Defeat a grimoire at a mountain top");
        this.add("runecraftory.advancements.progression.boss.dead_tree.title", "Whispies cousin");
        this.add("runecraftory.advancements.progression.boss.dead_tree.description", "Defeat the dead tree in a forest grove");
        this.add("runecraftory.advancements.progression.boss.raccoon.title", "Caught thief");
        this.add("runecraftory.advancements.progression.boss.raccoon.description", "Defeat a raccon somewhere in the plains");
        this.add("runecraftory.advancements.progression.boss.skelefang.title", "Ancient bone");
        this.add("runecraftory.advancements.progression.boss.skelefang.description", "Defeat skelefang found in the desert");
        this.add("runecraftory.advancements.progression.boss.ambrosia.title", "Butterfly?");
        this.add("runecraftory.advancements.progression.boss.ambrosia.description", "Defeat ambrosia in a forest grove");
        this.add("runecraftory.advancements.progression.boss.thunderbolt.title", "Demonic Horse");
        this.add("runecraftory.advancements.progression.boss.thunderbolt.description", "Defeat thunderbolt in the water ruins");
        this.add("runecraftory.advancements.progression.boss.marionetta.title", "It's not a ghost right?");
        this.add("runecraftory.advancements.progression.boss.marionetta.description", "Defeat marionetta in the theater ruins");
        this.add("runecraftory.advancements.progression.boss.sano_uno.title", "Are you worthy?");
        this.add("runecraftory.advancements.progression.boss.sano_uno.description", "Defeat sano and uno. Now where are they...");
        this.add("runecraftory.advancements.progression.boss.sarcophagus.title", "Don't get cursed!");
        this.add("runecraftory.advancements.progression.boss.sarcophagus.description", "Defeat sarcophagus after proving your worth to sano and uno");

        this.add("runecraftory.misc.recipe.eat.fail", "Didn't learn any recipe. Maybe your crafting level is too low");
        this.add("runecraftory.misc.sarcophagus.coming.soon", "This looks like a teleporter but it seems to not lead to anywhere yet...");
        this.add("runecraftory.misc.shipping.money", "Earning from shipped items: %s");
        this.add("runecraftory.misc.spawner.entry.deny", "A mystical force prevents you from entering!");

        // NPC stuff
        for (RegistryEntrySupplier<NPCProfession, ? extends NPCProfession> sup : RuneCraftoryNPCProfessions.PROFESSIONS.register().getEntries()) {
            this.add(sup.get().getTranslationKey(), this.simpleTranslation(sup.getID()));
        }
        this.add("npc.profession.general_store.owner", "General Store Owner");

        this.add(Blacksmith.BARN_ACTION, "Monster barn");
        this.add(Blacksmith.BARN_ACTION_DESCRIPTION, "You can buy a monster barn to house your tamed monsters. Each barn bought increases the costs of the next one");
        this.add(Blacksmith.BARN_ACTION_SUCCESS, "Thank you %s for your purchase.");
        this.add(Blacksmith.BARN_ACTION_FAIL, "You don't have enough materials for that.");
        this.add(Blacksmith.BARN_COST, "A barn costs %1$sG and following materials:");
        this.add(Blacksmith.BARN_COST_MAT, "Logs x%1$s, Cobblestone x%2$s");
        this.add(Blacksmith.BARN_COST_FAIL, "Error getting the cost of a barn");

        this.add(Chef.FORGE_BREAD_ACTION, "Weapon bread");
        this.add(Chef.ARMOR_BREAD_DESCRIPTION, "Accessory bread");
        this.add(Chef.CHEM_BREAD_SUCCESS, "Medicine bread");
        this.add(Chef.COOKING_BREAD_SUCCESS, "Cooking bread");
        this.add(Chef.BREAD_ACTION_SUCCESS, "Here you go");
        this.add(Chef.BREAD_ACTION_SUCCESS_GOOD, "Here you go. This one was made very well.");
        this.add(Chef.BREAD_ACTION_FAIL, "Seems you don't have enough money");
        this.add(Chef.BREAD_COST, "One loaf costs: %1$s. %2$s left");

        this.add(BathhouseAttendant.BATH_ACTION, "Take a bath");
        this.add(BathhouseAttendant.BATH_ACTION_SUCCESS, "Have a relaxing bath.");
        this.add(BathhouseAttendant.BATH_ACTION_FAIL, "You don't have enough money for that. You need %2$s.");
        this.add(BathhouseAttendant.BATH_COST, "Cost: %sG");

        this.add(Doctor.CURE_ACTION, "Cure");
        this.add(Doctor.CURE_ACTION_DESC, "Cure all negative status effects");
        this.add(Doctor.CURE_ACTION_SUCCESS, "There you go. Please take more care in the future.");
        this.add(Doctor.CURE_ACTION_FAIL, "It seems you don't have enough money.");
        this.add(Doctor.CURE_COST, "Cost: %sG");

        this.add("runecraftory.npc.schedule.work", "From: %1$s - %2$s");
        this.add("runecraftory.npc.schedule.work.2", "And: %1$s - %2$s");
        this.add("runecraftory.npc.schedule.days.header", "Open on:");
        this.add("runecraftory.npc.schedule.days.all", "Everyday");
        this.add("runecraftory.npc.schedule.days.0", "All weekdays");
        this.add("runecraftory.npc.schedule.days.1", "All weekdays except %s");
        this.add("runecraftory.npc.schedule.days.2", "All weekdays except %1$s and %2$s");
        this.add("runecraftory.npc.schedule.days.weekend.1", "%s");
        this.add("runecraftory.npc.schedule.days.weekend.2", "%1$s and %2$s");
        this.add("runecraftory.npc.schedule.days.with", "%1$s");

        this.add("runecraftory.npc.shop.inventory.full", "You don't have enough inventory space for it");
        this.add("runecraftory.npc.shop.money.no", "You don't have enough money");
        this.add("runecraftory.npc.shop.success", "Thank you for your purchase");

        this.add("runecraftory.npc.conversation.missing", "-Missing Conversation with id %s-");
        this.add("runecraftory.npc.conversation.context.missing", "-Missing Conversation for context %s-");
        this.add("runecraftory.npc.conversation.response.missing", "-Missing quest response for quest %s-");
        this.add("runecraftory.npc.default.gift.neutral", "Thank you for your gift.");

        this.add("runecraftory.npc.spawn.name.missing", "Missing name for baby!");

        this.add("runecraftory.quest.npc.header", "Requester: %1$s [%2$s,%3$s,%4$s]");

        this.add(LevelTask.ID.toString(), "Reach level %s");
        this.add(NPCTalkTask.ID.toString(), "Talk to %s");
        this.add(NPCTalkTask.ID + ".generic", "Could not find NPC to talk to");
        this.add(NPCTalkTask.ID + ".not_resolved", "Unresolved NPC talk task");
        this.add(ShippingTask.ID + ".single", "Ship %1$s x%2$s");
        this.add(ShippingTask.ID + ".multi", "Ship any of the following x%2$s: %1$s");
        this.add(ShippingTask.ID + ".empty", "<Empty tag/items>");
        this.add(SkillLevelTask.ID.toString(), "Reach level %1$s in %s");
        this.add(TamingTask.ID.toString(), "Tame %2$s %1$s");

        for (AdditionalLanguages langs : this.additionalLanguages) {
            if (langs != null) {
                langs.translations().forEach(this::add);
            }
        }

        this.add(QuestData.AcceptType.MISSING.langKey(), "Data is missing");
        this.add(QuestData.AcceptType.REQUIREMENTS.langKey(), "You do not meet the requirement for this quest");
        this.add(QuestData.AcceptType.ACCEPT.langKey(), "Quest accepted");
        this.add(QuestData.AcceptType.LIMIT.langKey(), "You reached your daily quest limit");
        this.add(QuestData.AcceptType.NONPC.langKey(), "NPC for this quest does not exist anymore!");

        this.add("runecraftory.dependency.tooltips.owner.none", "Unknown owner");
        this.add("runecraftory.dependency.tooltips.owner", "Owned by: %s");
        this.add("runecraftory.dependency.tooltips.friendpoints", "FP: %s");
        this.add("runecraftory.dependency.tooltips.barn", "Barn at: %s");
        this.add("runecraftory.dependency.tooltips.barn.no", "No Barn assigned!");
        this.add("runecraftory.dependency.tooltips.behaviour", "Behaviour: %s");
        this.add("runecraftory.dependency.tooltips.npc.follow", "Party: %s");
        this.add("runecraftory.dependency.tooltips.barn.1", "No Roof - Size: %1$s");
        this.add("runecraftory.dependency.tooltips.barn.1.alt", "Roof Height: %1$s - Size: %2$s");
        this.add("runecraftory.dependency.tooltips.barn.2", "Capacity: %1$s / %2$s");

        this.add("config.jade.plugin_runecraftory.jade_block_plugin", "Runecraftory Jade Block-Plugin");
        this.add("config.jade.plugin_runecraftory.jade_entity_plugin", "Runecraftory Jade Entity-Plugin");
        // Guidebook
        this.add("runecraftory.book.title", "Runepedia");
        this.add("runecraftory.book.landing", "Welcome to a wonderful world filled with adventures and runes. This book will help and guide you through your journey.");
    }

    private String simpleTranslation(ResourceLocation res) {
        String s = res.getPath();
        return this.simpleTranslation(s);
    }

    private String simpleTranslation(String s) {
        if (s.startsWith("ore_broken")) {
            s = s.replace("ore_broken", "");
            s = "broken_" + s + "_mineral";
        } else if (s.startsWith("ore_")) {
            s = s.replace("ore_", "");
            s = s + "_mineral";
        }
        return Stream.of(s.trim().split("_"))
                .filter(word -> !word.isEmpty())
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "))
                .replaceAll("Plus($| )", "+")
                .replace("Four Leaf", "4-Leaf")
                .replace("Pom Pom", "Pom-Pom")
                .replace("And", "&");
    }

    private String capitalize(String s, List<String> dont) {
        return Stream.of(s.trim().split("\\s"))
                .filter(word -> !word.isEmpty())
                .map(word -> dont.contains(word) ? word : word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        this.addTranslations();
        if (!this.data.isEmpty()) {
            Path path = this.packOutput.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(this.modid + "/lang/" + this.locale + ".json");
            JsonObject json = new JsonObject();
            this.data.forEach(json::addProperty);
            return DataProvider.saveStable(cache, json, path);
        }
        return CompletableFuture.allOf();
    }

    @Override
    public String getName() {
        return "Languages: " + this.locale;
    }

    public void addBlock(Supplier<? extends Block> key, String name) {
        this.add(key.get(), name);
    }

    public void add(Block key, String name) {
        if (!key.getDescriptionId().equals(key.asItem().getDescriptionId()))
            this.add(key.getDescriptionId(), name);
    }

    public void addItem(Supplier<? extends Item> key, String name) {
        this.add(key.get(), name);
    }

    public void add(Item key, String name) {
        this.add(key.getDescriptionId(), name);
    }

    public void addItemStack(Supplier<ItemStack> key, String name) {
        this.add(key.get(), name);
    }

    public void add(ItemStack key, String name) {
        this.add(key.getDescriptionId(), name);
    }

    public void addEffect(Supplier<? extends MobEffect> key, String name) {
        this.add(key.get(), name);
    }

    public void add(MobEffect key, String name) {
        this.add(key.getDescriptionId(), name);
    }

    public void add(Holder<SoundEvent> key) {
        String path = key.getKey().location().getPath();
        path.substring(path.indexOf(".")).replace(".", "_");
        this.add(key.getKey().location().toString(), this.simpleTranslation(path.substring(path.indexOf(".")).replace(".", "_")));
    }

    public void addEntityType(Supplier<? extends EntityType<?>> key, String name) {
        this.add(key.get(), name);
    }

    public void add(EntityType<?> key, String name) {
        this.add(key.getDescriptionId(), name);
    }

    public void add(String key, String value) {
        if (this.data.put(key, value) != null)
            throw new IllegalStateException("Duplicate translation key " + key);
    }
}
