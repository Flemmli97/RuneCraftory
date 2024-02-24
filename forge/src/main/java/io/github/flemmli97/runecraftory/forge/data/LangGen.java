package io.github.flemmli97.runecraftory.forge.data;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumDay;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.api.enums.EnumSeason;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.blocks.BlockCrop;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.npc.job.BathhouseAttendant;
import io.github.flemmli97.runecraftory.common.entities.npc.job.Cook;
import io.github.flemmli97.runecraftory.common.entities.npc.job.Doctor;
import io.github.flemmli97.runecraftory.common.entities.npc.job.Smith;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolAxe;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolFishingRod;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolHammer;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolHoe;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolSickle;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolWateringCan;
import io.github.flemmli97.runecraftory.common.network.C2SNPCInteraction;
import io.github.flemmli97.runecraftory.common.network.C2SSetMonsterBehaviour;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.registry.ModEffects;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModNPCJobs;
import io.github.flemmli97.runecraftory.integration.simplequest.QuestTasks;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.text.translate.JavaUnicodeEscaper;

import javax.annotation.Nullable;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Same as LanguageProvider but with a linked hashmap
 */
public class LangGen implements DataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Comparator<String> ORDER = Comparator.comparingInt(o -> LangType.get(o).ordinal());
    private final Map<String, String> data = new LinkedHashMap<>();
    private final DataGenerator gen;
    private final String modid;
    private final String locale;
    private final NPCDataGen npcDataGen;

    public LangGen(DataGenerator gen, @Nullable NPCDataGen npcDataGen) {
        this.gen = gen;
        this.modid = RuneCraftory.MODID;
        this.locale = "en_us";
        this.npcDataGen = npcDataGen;
    }

    protected void addTranslations() {
        //Static order
        this.add(ModItems.hoeScrap.get(), "Cheap Hoe");
        this.add(ModItems.hoeIron.get(), "Sturdy Hoe");
        this.add(ModItems.hoeSilver.get(), "Seasoned Hoe");
        this.add(ModItems.hoeGold.get(), "Shiny Hoe");
        this.add(ModItems.hoePlatinum.get(), "Blessed Hoe");
        this.add(ModItems.wateringCanScrap.get(), "Cheap Waterpot");
        this.add(ModItems.wateringCanIron.get(), "Tin Waterpot");
        this.add(ModItems.wateringCanSilver.get(), "Lion Waterpot");
        this.add(ModItems.wateringCanGold.get(), "Rainbow Waterpot");
        this.add(ModItems.wateringCanPlatinum.get(), "Joy Waterpot");
        this.add(ModItems.sickleScrap.get(), "Cheap Sickle");
        this.add(ModItems.sickleIron.get(), "Iron Sickle");
        this.add(ModItems.sickleSilver.get(), "Quality Sickle");
        this.add(ModItems.sickleGold.get(), "Super Sickle");
        this.add(ModItems.sicklePlatinum.get(), "Legendary Sickle");
        this.add(ModItems.hammerScrap.get(), "Cheap Hammer");
        this.add(ModItems.hammerIron.get(), "Iron Hammer");
        this.add(ModItems.hammerSilver.get(), "Silver Hammer");
        this.add(ModItems.hammerGold.get(), "Golden Hammer");
        this.add(ModItems.hammerPlatinum.get(), "Platinum Hammer");
        this.add(ModItems.axeScrap.get(), "Cheap Axe");
        this.add(ModItems.axeIron.get(), "Chopping Axe");
        this.add(ModItems.axeSilver.get(), "Lumber Axe");
        this.add(ModItems.axeGold.get(), "Mountain Axe");
        this.add(ModItems.axePlatinum.get(), "Miracle Axe");
        this.add(ModItems.fishingRodScrap.get(), "Cheap Pole");
        this.add(ModItems.fishingRodIron.get(), "Beginner's Pole");
        this.add(ModItems.fishingRodSilver.get(), "Skilled Pole");
        this.add(ModItems.fishingRodGold.get(), "Famous Pole");
        this.add(ModItems.fishingRodPlatinum.get(), "Sacred Pole");
        for (RegistryEntrySupplier<Item> sup : ModItems.ITEMS.getEntries()) {
            if (sup.get() instanceof ItemToolAxe || sup.get() instanceof ItemToolHoe || sup.get() instanceof ItemToolWateringCan
                    || sup.get() instanceof ItemToolSickle || sup.get() instanceof ItemToolHammer || sup.get() instanceof ItemToolFishingRod)
                continue;
            if (sup == ModItems.steelTen) {
                this.add(sup.get(), "10-Fold Steel");
            } else if (sup == ModItems.MTGUPlate) {
                this.add(sup.get(), "MGTU Plate");
            } else if (sup == ModItems.furSmall) {
                this.add(sup.get(), "Fur (S)");
            } else if (sup == ModItems.furMedium) {
                this.add(sup.get(), "Fur (M)");
            } else if (sup == ModItems.furLarge) {
                this.add(sup.get(), "Fur (L)");
            } else if (sup == ModItems.eggS) {
                this.add(sup.get(), "Egg (S)");
            } else if (sup == ModItems.eggM) {
                this.add(sup.get(), "Egg (M)");
            } else if (sup == ModItems.eggL) {
                this.add(sup.get(), "Egg (L)");
            } else if (sup == ModItems.milkS) {
                this.add(sup.get(), "Milk (S)");
            } else if (sup == ModItems.milkM) {
                this.add(sup.get(), "Milk (M)");
            } else if (sup == ModItems.milkL) {
                this.add(sup.get(), "Milk (L)");
            } else if (sup == ModItems.grapeSapling) {
                this.add(sup.get(), "Grape Sapling?");
            } else if (sup.get() instanceof SpawnEgg)
                this.add(sup.get(), "%s" + " Spawn Egg");
            else
                this.add(sup.get(), this.simpleTranslation(sup.getID()));
        }

        this.add("runecraftory.item.creative.tooltip", "Debug item used for testing things");

        for (RegistryEntrySupplier<Block> sup : ModBlocks.BLOCKS.getEntries()) {
            if (sup.get() instanceof BlockCrop)
                this.add(sup.get(), this.simpleTranslation(sup.getID()));
        }
        this.add(ModBlocks.mushroom.get(), "Mushroom");
        this.add(ModBlocks.monarchMushroom.get(), "Monach Mushroom");
        this.add(ModBlocks.snow.get(), "Snow");

        for (RegistryEntrySupplier<EntityType<?>> sup : ModEntities.ENTITIES.getEntries()) {
            this.add(sup.get(), this.simpleTranslation(sup.getID()));
        }

        this.add(ModAttributes.HEALTHGAIN.get().getDescriptionId(), "HP");
        this.add(ModAttributes.RPGAIN.get().getDescriptionId(), "RP");
        this.add(ModAttributes.RPINCREASE.get().getDescriptionId(), "RP Max");

        this.add(ModAttributes.DEFENCE.get().getDescriptionId(), "DEF");
        this.add(ModAttributes.MAGIC.get().getDescriptionId(), "M. ATT");
        this.add(ModAttributes.MAGIC_DEFENCE.get().getDescriptionId(), "M. DEF");
        this.add(ModAttributes.PARA.get().getDescriptionId(), "Para");
        this.add(ModAttributes.POISON.get().getDescriptionId(), "Poison");
        this.add(ModAttributes.SEAL.get().getDescriptionId(), "Sealing");
        this.add(ModAttributes.SLEEP.get().getDescriptionId(), "Sleep");
        this.add(ModAttributes.FATIGUE.get().getDescriptionId(), "Fatigue");
        this.add(ModAttributes.COLD.get().getDescriptionId(), "Cold");
        this.add(ModAttributes.DIZZY.get().getDescriptionId(), "Dizz");
        this.add(ModAttributes.CRIT.get().getDescriptionId(), "Crit");
        this.add(ModAttributes.STUN.get().getDescriptionId(), "Stun");
        this.add(ModAttributes.FAINT.get().getDescriptionId(), "Faint");
        this.add(ModAttributes.DRAIN.get().getDescriptionId(), "Drain");
        this.add(ModAttributes.RES_WATER.get().getDescriptionId(), "Water Res");
        this.add(ModAttributes.RES_EARTH.get().getDescriptionId(), "Earth Res");
        this.add(ModAttributes.RES_WIND.get().getDescriptionId(), "Wind Res");
        this.add(ModAttributes.RES_FIRE.get().getDescriptionId(), "Fire Res");
        this.add(ModAttributes.RES_DARK.get().getDescriptionId(), "Dark Res");
        this.add(ModAttributes.RES_LIGHT.get().getDescriptionId(), "Light Res");
        this.add(ModAttributes.RES_LOVE.get().getDescriptionId(), "Love Res");
        this.add(ModAttributes.RES_PARA.get().getDescriptionId(), "Paralysis Res");
        this.add(ModAttributes.RES_POISON.get().getDescriptionId(), "Poison Res");
        this.add(ModAttributes.RES_SEAL.get().getDescriptionId(), "Seal Res");
        this.add(ModAttributes.RES_SLEEP.get().getDescriptionId(), "Sleep Res");
        this.add(ModAttributes.RES_FAT.get().getDescriptionId(), "Fatigue Res");
        this.add(ModAttributes.RES_COLD.get().getDescriptionId(), "Cold Res");
        this.add(ModAttributes.RES_DIZZY.get().getDescriptionId(), "Diz Res");
        this.add(ModAttributes.RES_CRIT.get().getDescriptionId(), "Crit Res");
        this.add(ModAttributes.RES_STUN.get().getDescriptionId(), "Stun Res");
        this.add(ModAttributes.RES_FAINT.get().getDescriptionId(), "Faint Res");
        this.add(ModAttributes.RES_DRAIN.get().getDescriptionId(), "Drain Res");

        this.add(ModAttributes.ATTACK_SPEED.get().getDescriptionId(), "Attack Speed");
        this.add(ModAttributes.ATTACK_RANGE.get().getDescriptionId(), "Attack Range");

        this.add(ModEffects.COLD.get(), "Cold");
        this.add(ModEffects.SLEEP.get(), "Sleeping");
        this.add(ModEffects.POISON.get(), "Poison");
        this.add(ModEffects.PARALYSIS.get(), "Paralysis");
        this.add(ModEffects.SEAL.get(), "Sealed");
        this.add(ModEffects.FATIGUE.get(), "Fatigue");
        this.add(ModEffects.BATH.get(), "Bath");
        this.add(ModEffects.TRUE_INVIS.get(), "True Invisibility");
        this.add(ModEffects.BLITZ.get(), "Blitz");
        this.add(ModEffects.STEEL_HEART.get(), "Steel Heart");

        for (EnumDay day : EnumDay.values()) {
            this.add(day.translation(), day.toString().substring(0, 3));
            String d = day.toString().toLowerCase(Locale.ROOT);
            this.add(day.translationFull(), d.substring(0, 1).toUpperCase(Locale.ROOT) + d.substring(1));
        }

        this.add("runecraftory.recipe.eat.fail", "Didn't learn any recipe. Maybe your crafting level is too low");

        this.add("runecraftory.container.shipping_bin", "Shipping Bin");

        this.add("runecraftory.shipping.money", "Earning from shipped items: %s");

        this.add("runecraftory.tile.crafting.forge", "Forging");
        this.add("runecraftory.tile.crafting.armor", "Crafting");
        this.add("runecraftory.tile.crafting.chemistry", "Chemistry");
        this.add("runecraftory.tile.crafting.cooking", "Cooking");

        this.add(EnumElement.WATER.getTranslation(), "Attribute: Water");
        this.add(EnumElement.EARTH.getTranslation(), "Attribute: Earth");
        this.add(EnumElement.WIND.getTranslation(), "Attribute: Wind");
        this.add(EnumElement.FIRE.getTranslation(), "Attribute: Fire");
        this.add(EnumElement.LIGHT.getTranslation(), "Attribute: Light");
        this.add(EnumElement.DARK.getTranslation(), "Attribute: Dark");
        this.add(EnumElement.LOVE.getTranslation(), "Attribute: Love");
        this.add("runecraftory.tooltip.item.level", "Level: %s");
        this.add("runecraftory.tooltip.item.buy", "Buy: %s$");
        this.add("runecraftory.tooltip.item.sell", "Sell: %s$");
        this.add("runecraftory.tooltip.item.difficulty", "Upgrade Difficulty: %s");
        this.add("runecraftory.tooltip.item.equipped", "When equipped");
        this.add("runecraftory.tooltip.item.upgrade", "Upgrade");
        this.add("runecraftory.tooltip.item.eaten", "When eaten");
        this.add("runecraftory.gui.level", "Level");
        this.add("runecraftory.gui.npc.id", "Npc data-id");
        this.add("runecraftory.gui.npc.profession", "Npc Profession");
        this.add("runecraftory.gui.save", "Save");

        this.add("runecraftory.tooltip.item.spawn", "Right click in air to change mob level");
        this.add("runecraftory.tooltip.item.prop", "A prop item. Takes on the items look when in another entities hand");

        this.add("runecraftory.tooltip.item.treasure_chest", "Shift-right-click to cycle through loot tier");
        this.add("runecraftory.tooltip.item.treasure_level", "Chest tier lvl: %s");
        this.add("runecraftory.tooltip.debug.stat", "Itemstat-ID: %s");
        this.add("runecraftory.tooltip.debug.crop", "Cropdata-ID: %s");
        this.add("runecraftory.tooltip.debug.food", "Fooddata-ID: %s");
        this.add("runecraftory.tooltip.item.npc", "Shift-right-click to change profession");
        this.add("runecraftory.tooltip.sapling", "Needs to be placed on farmland");

        this.add("runecraftory.tooltip.growth", "Growth: %sd");
        this.add("runecraftory.tooltip.harvested", "Harvest Amount: %s");
        this.add(EnumSeason.SPRING.translationKey(), "Spring");
        this.add(EnumSeason.SUMMER.translationKey(), "Summer");
        this.add(EnumSeason.FALL.translationKey(), "Fall");
        this.add(EnumSeason.WINTER.translationKey(), "Winter");
        this.add("runecraftory.tooltip.season.best", "Good Season");
        this.add("runecraftory.tooltip.season.bad", "Bad Season");

        this.add(BaseMonster.Behaviour.WANDER_HOME.interactKey, "You send %s home");
        this.add(BaseMonster.Behaviour.FOLLOW.interactKey, "%s is now following you");
        this.add(BaseMonster.Behaviour.FOLLOW_DISTANCE.interactKey, "%s is now following you with distance");
        this.add(BaseMonster.Behaviour.STAY.interactKey, "%s is now staying");
        this.add(BaseMonster.Behaviour.WANDER.interactKey, "%s is now wandering around in this area");
        this.add(BaseMonster.Behaviour.FARM.interactKey, "%s is now tending the crops");
        this.add("runecraftory.monster.interact.party.full", "Your party is full");
        this.add("runecraftory.monster.interact.ride.no", "You can't ride this monster");
        this.add("runecraftory.monster.interact.barn.no", "%s has no barn");
        this.add("runecraftory.monster.interact.barn.no.ext", "%1$s [%2$s] has no home to go to.");
        this.add("runecraftory.barn.interact.not.owner", "This barn belongs to %s.");
        this.add("runecraftory.barn.interact.block", "Barn with capacity %1$s (Free: %2$s).");
        this.add("runecraftory.barn.interact.block.roofed", "Roofed barn with capacity %1$s (Free: %2$s).");

        this.add("runecraftory.crafting.rpMax.missing", "Missing total rp");

        this.add("runecraftory.display.level", "Level: %s");
        this.add("death.attack.rfExhaust", "%1$s fainted");
        this.add("death.attack.rfAttack", "%1$s was knocked down by %2$s");
        this.add("itemGroup.runecraftory.weapons_tools", "Weapons and Tools");
        this.add("itemGroup.runecraftory.equipment", "Armor");
        this.add("itemGroup.runecraftory.upgrade", "Materials");
        this.add("itemGroup.runecraftory.blocks", "Rune Craftory Blocks");
        this.add("itemGroup.runecraftory.medicine", "Medicine");
        this.add("itemGroup.runecraftory.cast", "Spells and Rune-Skills");
        this.add("itemGroup.runecraftory.food", "Food");
        this.add("itemGroup.runecraftory.crops", "Farming");
        this.add("itemGroup.runecraftory.monsters", "Monsters");

        this.add("runecraftory.keycategory", "Runecraftory");
        this.add("runecraftory.key.spell_1", "Interaction Key 1");
        this.add("runecraftory.key.spell_2", "Interaction Key 2");
        this.add("runecraftory.key.spell_3", "Interaction Key 3");
        this.add("runecraftory.key.spell_4", "Interaction Key 4");

        this.add("runecraftory.generic.yes", "Yes");
        this.add("runecraftory.generic.no", "No");

        this.add("runecraftory.command.skill.lvl.add", "Added %3$s %1$s skill level to %2$s");
        this.add("runecraftory.command.skill.xp.add", "Added %3$s %1$s skill xp points to %2$s");
        this.add("runecraftory.command.skill.lvl.set", "Set level of skill %1$s to %3$s for %2$s");
        this.add("runecraftory.command.lvl.xp.add", "Added %1$s xp points to %2$s");
        this.add("runecraftory.command.lvl.set", "Set level of %1$s to %2$s");
        this.add("runecraftory.command.reset.all", "Reset all player data for %s");
        this.add("runecraftory.command.unlock.recipes", "Unlocked all crafting recipes for %s");
        this.add("runecraftory.command.reset.recipe", "Locked all crafting recipes for %s again");
        this.add("runecraftory.command.weather.no", "No such weather %s");
        this.add("runecraftory.command.set.weather", "Set current weather to %s");
        this.add("runecraftory.command.recalc.stats", "Recalculated level stats for %s entities");

        this.add("runecraftory.recipe_integration.locked", "Unknown Recipe");
        this.add("runecraftory.recipe_integration.crafting_level", "Lvl: %s");

        this.add(C2SSetMonsterBehaviour.Type.HOME.translation, "Send home");
        this.add(C2SSetMonsterBehaviour.Type.FOLLOW.translation, "Follow");
        this.add(C2SSetMonsterBehaviour.Type.FOLLOW_DISTANCE.translation, "Stay back");
        this.add(C2SSetMonsterBehaviour.Type.STAY.translation, "Stay");
        this.add(C2SSetMonsterBehaviour.Type.WANDER.translation, "Wander");
        this.add(C2SSetMonsterBehaviour.Type.FARM.translation, "Tend the crops");
        this.add(C2SSetMonsterBehaviour.Type.HARVESTINV.translation, "Crop Inventory");
        this.add(C2SSetMonsterBehaviour.Type.SEEDINV.translation, "Seed Inventory");
        this.add(C2SSetMonsterBehaviour.Type.RIDE.translation, "Ride");
        this.add(C2SSetMonsterBehaviour.Type.CENTER.translation, "Set center");

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

        this.add("runecraftory_book", "Runepedia");
        this.add("runecraftory.patchouli.subtitle", "");
        this.add("runecraftory.patchouli.landing", "WIP Guidebook for the mod");
        this.add("runecraftory.patchouli.category.main", "Introduction");
        this.add("runecraftory.patchouli.category.main.desc", "");
        this.add("runecraftory.patchouli.entry.crafting", "Crafting");
        this.add("runecraftory.patchouli.entry.crafting.1", "This mod adds 4 additional blocks that are used to craft various items. " +
                "Every crafting process requires a certain amount of rp. Recipes that you haven't unlocked yet will require more rp than those you have." +
                "You will not be able to craft an item if the required rp is higher than your total rp. " +
                "Crafting a locked recipe will unlock it. You can also learn new recipes by eating recipe breads.");
        this.add("runecraftory.patchouli.entry.crafting.2", "Weapon and tools can be upgraded in the forge while armor can be upgraded in the accessory table. " +
                "Open the upgrade gui by shift right clicking on the crafting device. An item can be upgraded till it reaches level 10. Holding shift while hovering over an item will tell you " +
                "what stat it gives. Using the same item multiple times causes a diminishing returns effect so try use different items");
        this.add("runecraftory.patchouli.entry.crafting.forge", "The forge is used to craft all weapons and tools. During crafting you can add up to 3 bonus items that will " +
                "act the same as if you upgraded the weapon/tool with it.");
        this.add("runecraftory.patchouli.entry.crafting.armor", "Use the accessory table to make various armor pieces. Same as the forge you can use up to 3 additional items here " +
                "to increase the stats.");
        this.add("runecraftory.patchouli.entry.crafting.cooking", "The cooking table is as the name implies used to make all kinds of food. Some items can also be used as additional items for a better end product.");
        this.add("runecraftory.patchouli.entry.crafting.chemistry", "A chemistry set allows you to create potions and other pharmacy items.");
        this.add("runecraftory.patchouli.entry.minerals", "Minerals");
        this.add("runecraftory.patchouli.entry.minerals.1", "Cluster of minerals spawn all over the world. These mineral clusters can be mined with an iron pickaxe or above but its best mined with " +
                "the $(item)mining hammers$() from this mod. Better hammers and mining skill decrease the chance of the mineral breaking and increase the chance to get rarer materials from it.");
        this.add("runecraftory.patchouli.entry.minerals.2", "Some materials even are impossible to get unless you have a high enough mining skill. " +
                "$(br)Minerals regenerate after a day. $(br)If you want to completly get rid of a mineral mine a broken one while shifting.");
        this.add("runecraftory.patchouli.entry.shipping", "Shipping Items");
        this.add("runecraftory.patchouli.entry.shipping.1", "You can craft a shipping bin to sell items. Every morning all sellable items in the shipping bin will be sold. The shipping bin inventory is global for each player.");
        this.add("runecraftory.patchouli.entry.entities", "Monsters");
        this.add("runecraftory.patchouli.entry.entities.1", "The mobs in this mod don't spawn by themself but through gates that appear through the world. $(br)" +
                "The type of monster a gate spawns depends on the biome and a gate will continue to spawn monsters till it is destroyed. Gates can drop their corresponding crystals upon destruction. " +
                "$(br)Bosses spawn in their structures only and can be fought once every day.");
        this.add("runecraftory.patchouli.entry.entities.2", "Placing a $(item)bell block$() (or other meeting POI type blocks) will prevent gates from spawning in a 48 block radius around it. " +
                "$(br)$(br)Monsters are able to be tamed by having a $(l:entry.entities#barn)barn$() with enough space and throwing an item at them. This will consume the item and after a while $(bold)heart$(reset) particles" +
                " appear if the taming was successful and $(bold)smoke$() particles appear if otherwise. Monsters might have one or more favorite items that ");
        this.add("runecraftory.patchouli.entry.entities.3", "doubles the taming chance and bosses can $(#ff0000)only$() be tamed by giving them their favorite items. " +
                "Other ways to increase taming chance include: Brushing, leveling the taming skill and hitting it with a love attribute weapon." +
                "$(br)$(br)Tamed monster cannot die. Instead they will simply play death when reaching critical damage and healing them through any means will bring them back up. " +
                "Shift right click a tamed monster with a vanilla stick will release them again.");
        this.add("runecraftory.patchouli.entry.entities.4", "You can interact with a tamed monster using the following actions: " +
                "$(li)$(thing)Shift-right-clicking opens up a menu for the monster." +
                "$(li)While ridden press any of the following keys $(thing)$(k:runecraftory.key.spell_1), $(k:runecraftory.key.spell_2), $(k:runecraftory.key.spell_3), $(k:runecraftory.key.spell_4)$() to perform an attack." +
                "$(li)$(thing)Drop$() an item to it will give it the item similar to the taming process.");
        this.add("runecraftory.patchouli.entry.entities.5", "Food without additional benefits simply heals it while the other apply their benefits like they would to a player." +
                "$(br)$(br)Tamed monster can also help you with farming crops. Upon setting them into farming mode they will tend the crops in a certain radius around the initial position. " +
                "The nearest inventory block will also be bound to their action and they will deposit harvested crops into that inventory and if seeds are in it they can also plant them.");
        this.add("runecraftory.patchouli.entry.entities.6", "While holding a $(item)monster command staff$() and opening the monster menu you can configure additional things like area of actions and inventory for the entity. " +
                "Right clicking a block while e.g. configuring the home position will set the home position to that place." +
                "$(br)Brushing and giving tamed monster items once a day increases your friendship with them.");
        this.add("runecraftory.patchouli.entry.entities.7", "Barn blocks are needed to tame monsters and can be bought by smiths.");
        this.add("runecraftory.patchouli.entry.entities.8", "To build a successful barn you would need at least a 5x5 area of hay bales. This area can increase up to 11x11. " +
                "Most monster also need a roof. All blocks (except blocks without collision) count as roofs and there needs to be at least 3 blocks of air between the ground and the roof. " +
                "Bigger sized barn also need more air blocks.");

        this.add("runecraftory.patchouli.entry.party", "Party");
        this.add("runecraftory.patchouli.entry.party.1", "You might have noticed that sometimes you can't make monsters or npc follow you. " +
                "This is due to the party system which allows only a max of 3 entities to follow you at the same time. Party members will follow you and teleport " +
                "if too far regardless of where you are.");

        this.add("runecraftory.patchouli.entry.quests", "Quests");
        this.add("runecraftory.patchouli.entry.quests.1", "This feature is WIP and there are currently no quests.");

        this.add("runecraftory.patchouli.category.farming", "Agriculture");
        this.add("runecraftory.patchouli.category.farming.desc", "An overview and guide about the agricultural aspects");
        this.add("runecraftory.patchouli.entry.farming", "Getting Started");
        this.add("runecraftory.patchouli.entry.farming.1", "To get started with growing crops you first need a $(item)hoe$(), a $(item)watering can$() and of course $(item)crop seeds$() to plant. " +
                "Then simply till the land to turn it into farmland. Using a $(item)magnifying glass$() you can see the stats of the farmland.");
        this.add("runecraftory.patchouli.entry.farming.2", "After that plant the crops on the farmland and water it with a watering can. Unlike vanilla farmland nearby water will not water it. " +
                "The crops will grow every day and you will also need to keep watering them each day till they are fully grown. Crops can wilt if you forget to water them and by not watering wilted crops they will turn into withered grass so make sure to keep them hydrated. " +
                "You can use items to increase the soil quality see $(l:entry.fertilizer#p1)here$().");
        this.add("runecraftory.patchouli.entry.farming.3", "Crops will get a growth bonus if they are planted in the correct season and if planted in the wrong season will grow slower. The cropsystem can affect crops not from this mod and if affected you will not be able to grow those the vanilla way. " +
                "You can see if they are affected by simply look if they have additional info attached to them.");
        this.add("runecraftory.patchouli.entry.fertilizer", "Fertilizer");
        this.add("runecraftory.patchouli.entry.fertilizer.1", "There are various items to improve your farming experience. You can buy them at shops. Vanilla bonemeal will not work like normal and grow the crops, " +
                "instead it will work as a very weak growth increaser for the soil");
        this.add("runecraftory.patchouli.entry.fertilizer.2", "$(li)Formular a, b and c acts increase the growth rate of the soil with a being the weakest and c the strongest." +
                "$(li)Wettable powder: Increases the soils defence. If the defence is 0 storms have a chance to destroy the crop." +
                "$(li)Giantizer/Minimizer are used to grow giant crops (Not implemented $(bold)ATM$())." +
                "$(li)Greenifier: Increases soil level and as such also crop level (Not implemented $(bold)ATM$()).");
        this.add("runecraftory.patchouli.entry.weather", "Weather");
        this.add("runecraftory.patchouli.entry.weather.1", "There are 4 types of weather conditions in this mod that only changes during certain times of the day: " +
                "$(li)$(a)Sunny$(): Normal sunny day without any special properties" +
                "$(li)$(9)Rain$(): Farmland will automatically get watered ");
        this.add("runecraftory.patchouli.entry.weather.2", "$(li)$(3)Stormy$(): Farmland will automatically get watered but also the defence gets reduced over time. If defence drops to 0 the crops can get destroyed so pay attention" +
                "$(li)$(6)Runey$(): Like sunny days but crops get a boost in growth" +
                "$(br)$(br)If it gets cold enough it might also snow in places where it normally wouldn't.");

        this.add("runecraftory.patchouli.category.equipment", "Equipments");
        this.add("runecraftory.patchouli.category.equipment.desc", "");
        this.add("runecraftory.patchouli.entry.weapon", "Weapons");
        this.add("runecraftory.patchouli.entry.weapon.1", "The mod adds a plethora of weapons and you will find a short explanation of each weapon type here. By reaching at least level 5 for a weapon type you are able to " +
                "use a charge attack. Simply hold right click and release after a while to use it. This will consume a bit of runepoints though. " +
                "They also have unlike vanilla swords an aoe effect. See it as sweeping but all mobs take equal damage.");
        this.add("runecraftory.patchouli.entry.weapon.2.title", "Short Swords");
        this.add("runecraftory.patchouli.entry.weapon.2", "Weapons with a shorter reach and attack power but quite fast with a small aoe effect. The closest to vanilla swords.");
        this.add("runecraftory.patchouli.entry.weapon.3.title", "Long Swords");
        this.add("runecraftory.patchouli.entry.weapon.3", "Bigger reach and more attack power but kinda slow. Has a decent aoe.");
        this.add("runecraftory.patchouli.entry.weapon.4.title", "Spears");
        this.add("runecraftory.patchouli.entry.weapon.4", "Big reach and fairly quick. The charge attack is special: After charging and releasing repeatedly right click to keep attacking");
        this.add("runecraftory.patchouli.entry.weapon.5.title", "Axe/Hammers");
        this.add("runecraftory.patchouli.entry.weapon.5", "Slow but strong and with bigger reach. Not to be confused with the axe and hammer tools though. Axes usually have a high crit rate while hammers a high stun chance. $(bold)ATM$() stunning will do nothing though");
        this.add("runecraftory.patchouli.entry.weapon.6.title", "Dual Blades");
        this.add("runecraftory.patchouli.entry.weapon.6", "Dual weapons. Fast but weaker than other weapons with a shorter reach. You will also not be able to use offhand items.");
        this.add("runecraftory.patchouli.entry.weapon.7.title", "Fists");
        this.add("runecraftory.patchouli.entry.weapon.7", "Dual weapons. Fast but with shorter reach and small aoe. Charge attack will push you into the direction you are looking and during that hitting any mobs in your way");
        this.add("runecraftory.patchouli.entry.weapon.8.title", "Staffs");
        this.add("runecraftory.patchouli.entry.weapon.8", "Magic weapon. Each staff has a base spell used per weapon swing. Additionally upgrading a staff with items can give it spells too. To use them simply hold right click. " +
                "A staff can have max 3 spells attached to it.");
        this.add("runecraftory.patchouli.entry.tools", "Tools");
        this.add("runecraftory.patchouli.entry.tools.1", "Here is an overview of the tools from this mod. You can use tools as weapons but they are noticeably weaker. The higher tier the tool is the more powerful the " +
                "charge ability will be.");
        this.add("runecraftory.patchouli.entry.tools.2.title", "Hoe");
        this.add("runecraftory.patchouli.entry.tools.2", "Used to till the earth turning it into farmland to grow crops.");
        this.add("runecraftory.patchouli.entry.tools.3.title", "Wateringcan");
        this.add("runecraftory.patchouli.entry.tools.3", "You need a watering can to water farmland. Right click on water blocks to fill it up.");
        this.add("runecraftory.patchouli.entry.tools.4.title", "Sickle");
        this.add("runecraftory.patchouli.entry.tools.4", "Can be used to clear out grass more easily");
        this.add("runecraftory.patchouli.entry.tools.5.title", "Hammer");
        this.add("runecraftory.patchouli.entry.tools.5", "Acts like a pickaxe but you can also use it to flatten farmland turning it back into dirt. It also gets additional benefits when breaking minerals.");
        this.add("runecraftory.patchouli.entry.tools.6.title", "Axe");
        this.add("runecraftory.patchouli.entry.tools.6", "For now acts just like vanilla axes");
        this.add("runecraftory.patchouli.entry.tools.7.title", "Fishing Rod");
        this.add("runecraftory.patchouli.entry.tools.7", "Similiar to vanilla fishing rod. Throw it into a body of water to start fishing. The body of water needs to be at least $(1)2 blocks deep and 3x3 wide$(). Higher tier fishing rods increases the speed to catch" +
                " a fish while the more charge it has the easier it is to catch a fish.");
        this.add("runecraftory.patchouli.entry.tools.8", "Useful to inspect the quality of the soil. If used as upgrade material will pass on its function to the upgraded tool.");
        this.add("runecraftory.patchouli.entry.spellskills", "Spells and Skills");
        this.add("runecraftory.patchouli.entry.spellskills.1", "There are several spells and skills that can be found all over the world. Every spell and skill will require runepoints to use them. " +
                "You can put both spell and skill items in the spell slots (in your inventory) and then you are able to simply press the corresponding key to cast them. " +
                "The only craftable spells are the following two:");
        this.add("runecraftory.patchouli.entry.spellskills.3", "Skills require you to hold a weapon in your hand and will drop in efficiency if you hold the wrong weapon. ");

        this.add("runecraftory.patchouli.category.npc", "Villagers");
        this.add("runecraftory.patchouli.category.npc.desc", "");
        this.add("runecraftory.patchouli.entry.npc", "Villagers");
        this.add("runecraftory.patchouli.entry.npc.1", "You might come across some villagers that are different from vanilla villagers. Some can operate shops where you can buy stuff. " +
                "For them to operate a shop they need a bed and workplace not too far away from eachother first. Right click allows you to interact with them and they will tell you if they are missing something.");
        this.add("runecraftory.patchouli.entry.npc.2", "Hover over the red text (if existent) to see that. Talking daily with a villager increases your friendship. You can also gift them item" +
                "by throwing it at them. If you give them equipment they will equip it too." +
                "$(br)$(br)Note: Villagers don't have skins yet.");

        this.add("runecraftory.patchouli.entry.npc.jobs", "Villager jobs");
        this.add("runecraftory.patchouli.entry.npc.jobs.1.title", "General Store");
        this.add("runecraftory.patchouli.entry.npc.jobs.1", "The general store sells mostly seeds, vegetables and fruits");
        this.add("runecraftory.patchouli.entry.npc.jobs.2.title", "Florist");
        this.add("runecraftory.patchouli.entry.npc.jobs.2", "Here you can buy flowers and seeds an also fertilizer for your crops");
        this.add("runecraftory.patchouli.entry.npc.jobs.3.title", "Smith");
        this.add("runecraftory.patchouli.entry.npc.jobs.3", "The smith sells weapons, tools and armor as well as monster barns");
        this.add("runecraftory.patchouli.entry.npc.jobs.4.title", "Doctor");
        this.add("runecraftory.patchouli.entry.npc.jobs.4", "You can buy medicine and herbs here");
        this.add("runecraftory.patchouli.entry.npc.jobs.5.title", "Cook");
        this.add("runecraftory.patchouli.entry.npc.jobs.5", "Buy food and recipe breads here. The cook only has a certain amount of recipe breads each day");
        this.add("runecraftory.patchouli.entry.npc.jobs.6.title", "Magic Store");
        this.add("runecraftory.patchouli.entry.npc.jobs.6", "Sells various spells here. WIP");
        this.add("runecraftory.patchouli.entry.npc.jobs.7.title", "Rune skill Store");
        this.add("runecraftory.patchouli.entry.npc.jobs.7", "Sells various rune skills here. WIP");
        this.add("runecraftory.patchouli.entry.npc.jobs.8.title", "Bath house");
        this.add("runecraftory.patchouli.entry.npc.jobs.8", "Bath houses offer a way to regen your hp and rp. By talking to the bath house attendant you can obtain an effect " +
                "which will regen hp+rp if you go into hot water (water with campfire below). Going out of the water will remove the effect immediately!");
        this.add("runecraftory.patchouli.entry.npc.jobs.9.title", "Travelling merchant");
        this.add("runecraftory.patchouli.entry.npc.jobs.9", "Sells misc items. WIP");

        this.add("runecraftory.patchouli.category.entities", "Monsters");
        this.add("runecraftory.patchouli.category.entities.desc", "List of all monsters");
        this.add(patchouliEntity(ModEntities.WOOLY.getID()), "Sheep like creature that is rather passive. Shearable.");
        this.add(patchouliEntity(ModEntities.ORC.getID()), "");
        this.add(patchouliEntity(ModEntities.ORC_ARCHER.getID()), "An orc but with a bow");
        this.add(patchouliEntity(ModEntities.ANT.getID()), "");
        this.add(patchouliEntity(ModEntities.BEETLE.getID()), "");
        this.add(patchouliEntity(ModEntities.BIG_MUCK.getID()), "Mushroom like create that attacks using spores");
        this.add(patchouliEntity(ModEntities.BUFFAMOO.getID()), "");
        this.add(patchouliEntity(ModEntities.CHIPSQUEEK.getID()), "");
        this.add(patchouliEntity(ModEntities.CLUCKADOODLE.getID()), "");
        this.add(patchouliEntity(ModEntities.POMME_POMME.getID()), "");
        this.add(patchouliEntity(ModEntities.TORTAS.getID()), "");
        this.add(patchouliEntity(ModEntities.SKY_FISH.getID()), "");
        this.add(patchouliEntity(ModEntities.WEAGLE.getID()), "");
        this.add(patchouliEntity(ModEntities.GOBLIN.getID()), "");
        this.add(patchouliEntity(ModEntities.GOBLIN_ARCHER.getID()), "");
        this.add(patchouliEntity(ModEntities.DUCK.getID()), "");
        this.add(patchouliEntity(ModEntities.FAIRY.getID()), "");
        this.add(patchouliEntity(ModEntities.GHOST.getID()), "");
        this.add(patchouliEntity(ModEntities.SPIRIT.getID()), "");
        this.add(patchouliEntity(ModEntities.GHOST_RAY.getID()), "");
        this.add(patchouliEntity(ModEntities.SPIDER.getID()), "");
        this.add(patchouliEntity(ModEntities.SHADOW_PANTHER.getID()), "");
        this.add(patchouliEntity(ModEntities.GOBBLE_BOX.getID()), "");
        this.add(patchouliEntity(ModEntities.MONSTER_BOX.getID()), "");

        this.add(patchouliEntity(ModEntities.AMBROSIA.getID()), "Butterfly boss monster. Spawns in forest groves and can be fought once a day");
        this.add(patchouliEntity(ModEntities.THUNDERBOLT.getID()), "Horse said to be as fast as lightning. Spawns in water ruins");
        this.add(patchouliEntity(ModEntities.MARIONETTA.getID()), "Spooky old doll that spawns in theater ruins");

        for (EnumSkills s : EnumSkills.values())
            this.add(s.getTranslation(),
                    this.capitalize(s.getTranslation().replace("skill.", "").replace("_", " "),
                            Lists.newArrayList("and")));

        this.add(C2SNPCInteraction.Type.TALK.translation, "Talk");
        this.add(C2SNPCInteraction.Type.FOLLOW.translation, "Follow me");
        this.add(C2SNPCInteraction.Type.FOLLOWDISTANCE.translation, "Stay back a bit");
        this.add(C2SNPCInteraction.Type.STAY.translation, "Stay here");
        this.add(C2SNPCInteraction.Type.STOPFOLLOW.translation, "Stop following");
        this.add(C2SNPCInteraction.Type.SHOP.translation, "I want to shop");
        this.add("runecraftory.gui.npc.shop.owner", "Owner of %s");
        this.add("runecraftory.gui.npc.bed.no", "I don't have a bed");
        this.add("runecraftory.gui.npc.workplace.no", "I don't have a work place. Valid workplaces are [%s]");

        this.add("runecraftory.gui.quests.accept", "Accept");
        this.add("runecraftory.gui.quests.reset", "Cancel");
        this.add("runecraftory.gui.quest.submit.button", "Submit");
        this.add("runecraftory.quest.gui.button", "Requests");
        this.add("runecraftory.quest.npc.none", "The NPC for this quest doesn't exist anymore!");
        this.add("runecraftory.quest.npc.header", "Requester: %1$s [%2$s,%3$s,%4$s]");

        this.add(ModNPCJobs.NONE.getSecond().getTranslationKey(), "None");
        this.add(ModNPCJobs.GENERAL.getSecond().getTranslationKey(), "General Store");
        this.add(ModNPCJobs.FLOWER.getSecond().getTranslationKey(), "Florist");
        this.add(ModNPCJobs.SMITH.getSecond().getTranslationKey(), "Smith");
        this.add(ModNPCJobs.DOCTOR.getSecond().getTranslationKey(), "Doctor");
        this.add(ModNPCJobs.COOK.getSecond().getTranslationKey(), "Cook");
        this.add(ModNPCJobs.MAGIC.getSecond().getTranslationKey(), "Magicskill merchant");
        this.add(ModNPCJobs.RUNE_SKILLS.getSecond().getTranslationKey(), "Runeskill merchant");
        this.add(ModNPCJobs.BATHHOUSE.getSecond().getTranslationKey(), "Bathhouse attendant");
        this.add(ModNPCJobs.RANDOM.getSecond().getTranslationKey(), "Travelling merchant");

        this.add(Smith.BARN_ACTION, "Monster barn");
        this.add(Smith.BARN_ACTION_DESCRIPTION, "You can buy a monster barn to house your tamed monsters. Each barn bought increases the costs of the next one");
        this.add(Smith.BARN_ACTION_SUCCESS, "Thank you for your purchase.");
        this.add(Smith.BARN_ACTION_FAIL, "You don't have enough materials for that.");
        this.add(Smith.BARN_COST, "A barn costs %1$s$ and following materials:");
        this.add(Smith.BARN_COST_MAT, "Logs x%1$s, Cobblestone x%2$s");
        this.add(Smith.BARN_COST_FAIL, "Error getting the cost of a barn");

        this.add(Cook.FORGE_BREAD_ACTION, "Weapon bread");
        this.add(Cook.ARMOR_BREAD_DESCRIPTION, "Accessory bread");
        this.add(Cook.CHEM_BREAD_SUCCESS, "Medicine bread");
        this.add(Cook.COOKING_BREAD_SUCCESS, "Cooking bread");
        this.add(Cook.BREAD_ACTION_SUCCESS, "Here you go");
        this.add(Cook.BREAD_ACTION_SUCCESS_GOOD, "Here you go. This one was made very well.");
        this.add(Cook.BREAD_ACTION_FAIL, "Seems you don't have enough money");
        this.add(Cook.BREAD_COST, "One loaf costs: %1$s. %2$s left");

        this.add(BathhouseAttendant.BATH_ACTION, "Take a bath");
        this.add(BathhouseAttendant.BATH_ACTION_SUCCESS, "Have a relaxing bath.");
        this.add(BathhouseAttendant.BATH_ACTION_FAIL, "You don't have enough money for that. You need %2$s.");
        this.add(BathhouseAttendant.BATH_COST, "Cost: %s$");

        this.add(Doctor.CURE_ACTION, "Cure");
        this.add(Doctor.CURE_ACTION_DESC, "Cure all negative status effects");
        this.add(Doctor.CURE_ACTION_SUCCESS, "There you go. Please take more care in the future.");
        this.add(Doctor.CURE_ACTION_FAIL, "It seems you don't have enough money.");
        this.add(Doctor.CURE_COST, "Cost: %s$");

        this.add("npc.schedule.work", "From: %1$s - %2$s");
        this.add("npc.schedule.work.2", "And: %1$s - %2$s");
        this.add("npc.schedule.days.header", "Open on:");
        this.add("npc.schedule.days.all", "Everyday");
        this.add("npc.schedule.days.0", "All weekdays");
        this.add("npc.schedule.days.1", "All weekdays except %s");
        this.add("npc.schedule.days.2", "All weekdays except %1$s and %2$s");
        this.add("npc.schedule.days.weekend.1", "%s");
        this.add("npc.schedule.days.weekend.2", "%1$s and %2$s");
        this.add("npc.schedule.days.with", "%1$s");

        this.add("npc.shop.inventory.full", "You don't have enough inventory space for it");
        this.add("npc.shop.money.no", "You don't have enough money");
        this.add("npc.shop.success", "Thank you for your purchase");

        this.add("npc.default.greeting.default", "Hello %s.");
        this.add("npc.default.talk.default", "Hi %s.");
        this.add("npc.default.follow.yes.default", "Sure %s. Where are we going?");
        this.add("npc.default.follow.no.default", "Sorry but im busy right now.");
        this.add("npc.default.follow.stop.default", "Ok.");
        this.add("npc.default.gift.neutral", "Thank you for your gift.");
        this.add("npc.default.quest.response.default", "Placeholder text for quest response");

        if (this.npcDataGen != null)
            this.npcDataGen.translations.forEach(this::add);

        this.add("runecraftory.advancements.root.title", "Runecraftory");
        this.add("runecraftory.advancements.root.description", "A minecrafty harvest moon");
        this.add("runecraftory.advancements.tame.first.title", "First buddy");
        this.add("runecraftory.advancements.tame.first.description", "Tame your first monster");
        this.add("runecraftory.advancements.tame.ten.title", "Monster tamer");
        this.add("runecraftory.advancements.tame.ten.description", "Tame 10 monster");
        this.add("runecraftory.advancements.tame.boss.title", "Strong buddy");
        this.add("runecraftory.advancements.tame.boss.description", "Tame a boss monster");

        this.add("runecraftory.advancements.shipping.title", "Money");
        this.add("runecraftory.advancements.shipping.description", "Ship your first item");
        this.add("runecraftory.advancements.shop.title", "Time for shopping");
        this.add("runecraftory.advancements.shop.description", "Buy your first item");
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

        this.add("runecraftory.advancements.level.10.title", "Level 10");
        this.add("runecraftory.advancements.level.10.description", "Get to level 10");
        this.add("runecraftory.advancements.level.25.title", "Level 25");
        this.add("runecraftory.advancements.level.25.description", "Get to level 25");
        this.add("runecraftory.advancements.level.50.title", "Level 50");
        this.add("runecraftory.advancements.level.50.description", "Get to level 50");
        this.add("runecraftory.advancements.level.100.title", "Level 100");
        this.add("runecraftory.advancements.level.100.description", "Get to level 100");

        this.add("runecraftory.advancements.upgrade.title", "Better equipment");
        this.add("runecraftory.advancements.upgrade.description", "Upgrade any equipment");
        this.add("runecraftory.advancements.change.element.title", "Its super effective");
        this.add("runecraftory.advancements.change.element.description", "Change an element of a weapon");
        this.add("runecraftory.advancements.spell.title", "Magick");
        this.add("runecraftory.advancements.spell.description", "Find or craft a spell");
        this.add("runecraftory.advancements.change.spell.title", "Staff power");
        this.add("runecraftory.advancements.change.spell.description", "Change or add a spell to a staff");

        this.add("runecraftory.advancements.fertilizer.title", "Increased production");
        this.add("runecraftory.advancements.fertilizer.description", "Use a fertilizer to improve your farmland");
        this.add("runecraftory.advancements.monster.help.title", "Useful helper");
        this.add("runecraftory.advancements.monster.help.description", "Command a monster to help you out with farming");
        this.add("runecraftory.advancements.final.tool.title", "The best");
        this.add("runecraftory.advancements.final.tool.description", "Obtain a final tier tool");

        this.add("runecraftory.dependency.simplequest.missing", "Simplequest is needed for quests");
        this.add(QuestTasks.ShippingEntry.ID + ".single", "Ship %1$s x%2$s");
        this.add(QuestTasks.ShippingEntry.ID + ".multi", "Ship any of the following x%2$s: %1$s");
        this.add(QuestTasks.ShippingEntry.ID + ".empty", "<Empty tag/items>");
        this.add(QuestTasks.LevelEntry.ID.toString(), "Reach level %s");
        this.add(QuestTasks.SkillLevelEntry.ID.toString(), "Reach level %1$s in %s");
        this.add(QuestTasks.TamingEntry.ID.toString(), "%s");

        this.add("runecraftory.dependency.tooltips.owner.none", "Unknown owner");
        this.add("runecraftory.dependency.tooltips.owner", "Owned by: %s");
        this.add("runecraftory.dependency.tooltips.friendpoints", "FP: %s");
        this.add("runecraftory.dependency.tooltips.barn", "Barn at: %s");
        this.add("runecraftory.dependency.tooltips.barn.no", "No Barn assigned!");
        this.add("runecraftory.dependency.tooltips.behaviour", "Behaviour: %s");
        this.add("runecraftory.dependency.tooltips.npc.follow", "Party: %s");
        this.add("runecraftory.dependency.tooltips.barn.1", "Roof: %1$s - Size: %2$s");
        this.add("runecraftory.dependency.tooltips.barn.2", "Capacity: %1$s / %2$s");
    }

    private String simpleTranslation(ResourceLocation res) {
        String s = res.getPath();
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
                .collect(Collectors.joining(" ")).replace("Plus", "+")
                .replace("Four Leaf", "4-Leaf")
                .replace("Pom Pom", "Pom-Pom");
    }

    private static String patchouliEntity(ResourceLocation res) {
        return "runecraftory.patchouli.entry.entity." + res;
    }

    private String capitalize(String s, List<String> dont) {
        return Stream.of(s.trim().split("\\s"))
                .filter(word -> !word.isEmpty())
                .map(word -> dont.contains(word) ? word : word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));
    }

    @Override
    public void run(HashCache cache) throws IOException {
        this.addTranslations();
        Map<String, String> sort = this.data.entrySet().stream().sorted((e, e2) -> ORDER.compare(e.getKey(), e2.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (old, v) -> old, LinkedHashMap::new));
        if (!this.data.isEmpty())
            this.save(cache, sort, this.gen.getOutputFolder().resolve("assets/" + this.modid + "/lang/" + this.locale + ".json"));
    }

    @Override
    public String getName() {
        return "Languages: " + this.locale;
    }

    @SuppressWarnings("deprecation")
    private void save(HashCache cache, Object object, Path target) throws IOException {
        String data = GSON.toJson(object);
        data = JavaUnicodeEscaper.outsideOf(0, 0x7f).translate(data); // Escape unicode after the fact so that it's not double escaped by GSON
        String hash = DataProvider.SHA1.hashUnencodedChars(data).toString();
        if (!Objects.equals(cache.getHash(target), hash) || !Files.exists(target)) {
            Files.createDirectories(target.getParent());

            try (BufferedWriter bufferedwriter = Files.newBufferedWriter(target)) {
                bufferedwriter.write(data);
            }
        }

        cache.putNew(target, hash);
    }

    public void addBlock(Supplier<? extends Block> key, String name) {
        this.add(key.get(), name);
    }

    public void add(Block key, String name) {
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

    public void addEnchantment(Supplier<? extends Enchantment> key, String name) {
        this.add(key.get(), name);
    }

    public void add(Enchantment key, String name) {
        this.add(key.getDescriptionId(), name);
    }

    /*
    public void addBiome(Supplier<? extends Biome> key, String name) {
        add(key.get(), name);
    }

    public void add(Biome key, String name) {
        add(key.getTranslationKey(), name);
    }
    */

    public void addEffect(Supplier<? extends MobEffect> key, String name) {
        this.add(key.get(), name);
    }

    public void add(MobEffect key, String name) {
        this.add(key.getDescriptionId(), name);
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

    enum LangType {
        ITEM,
        BLOCK,
        ENTITY,
        CONTAINER,
        ELEMENT,
        ATTRIBUTE,
        ADVANCEMENT,
        SKILL,
        TOOLTIP,
        DAY,
        SEASON,
        DEATH,
        ITEMGROUP,
        NPC,
        OTHER;

        public static LangType get(String s) {
            if (s.startsWith("item."))
                return ITEM;
            if (s.startsWith("block."))
                return BLOCK;
            if (s.startsWith("entity."))
                return ENTITY;
            if (s.startsWith("container."))
                return CONTAINER;
            if (s.startsWith("runecraftory.element."))
                return ELEMENT;
            if (s.startsWith("attribute.rf."))
                return ATTRIBUTE;
            if (s.startsWith("runecraftory.advancements"))
                return ADVANCEMENT;
            if (s.startsWith("skill."))
                return SKILL;
            if (s.startsWith("runecraftory.tooltip."))
                return TOOLTIP;
            if (s.startsWith("runecraftory.day."))
                return DAY;
            if (s.startsWith("runecraftory.season."))
                return SEASON;
            if (s.startsWith("death."))
                return DEATH;
            if (s.startsWith("itemGroup."))
                return ITEMGROUP;
            if (s.startsWith("runecraftory.npc."))
                return NPC;
            return OTHER;
        }
    }
}
