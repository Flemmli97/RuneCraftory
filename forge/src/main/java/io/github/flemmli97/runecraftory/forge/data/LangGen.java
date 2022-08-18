package io.github.flemmli97.runecraftory.forge.data;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumDay;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.blocks.BlockCrop;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolAxe;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolFishingRod;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolHammer;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolHoe;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolSickle;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolWateringCan;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.registry.ModEffects;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
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
import net.minecraftforge.common.data.ExistingFileHelper;
import org.apache.commons.lang3.text.translate.JavaUnicodeEscaper;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Same as LanguageProvider but with a linked hashmap and reading from old lang file
 */
public class LangGen implements DataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Comparator<String> order = Comparator.comparingInt(o -> LangType.get(o).ordinal());
    private final Map<String, String> data = new LinkedHashMap<>();
    private final DataGenerator gen;
    private final String modid;
    private final String locale;

    public LangGen(DataGenerator gen, ExistingFileHelper existing) {
        this.gen = gen;
        this.modid = RuneCraftory.MODID;
        this.locale = "en_us";
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
            } else if (sup == ModItems.fourLeafClover) {
                this.add(sup.get(), "4-Leaf Clover");
            } else if (sup == ModItems.fourLeafCloverGiant) {
                this.add(sup.get(), "Giant 4-Leaf Clover");
            } else if (sup == ModItems.fourLeafCloverSeeds) {
                this.add(sup.get(), "4-Leaf Clover Seed");
            } else if (sup.get() instanceof SpawnEgg)
                this.add(sup.get(), "%s" + " Spawn Egg");
            else
                this.add(sup.get(), this.simpleTranslation(sup.getID()));
        }

        for (RegistryEntrySupplier<Block> sup : ModBlocks.BLOCKS.getEntries()) {
            if (sup.get() instanceof BlockCrop)
                this.add(sup.get(), this.simpleTranslation(sup.getID()));
        }
        this.add(ModEntities.wooly.get(), "Wooly");
        this.add(ModEntities.gate.get(), "Gate");
        this.add(ModEntities.orc.get(), "Orc");
        this.add(ModEntities.orcArcher.get(), "Orc Archer");
        this.add(ModEntities.ant.get(), "Ant");
        this.add(ModEntities.big_muck.get(), "Big Muck");
        this.add(ModEntities.beetle.get(), "Beetle");
        this.add(ModEntities.ambrosia.get(), "Ambrosia");
        this.add(ModEntities.thunderbolt.get(), "Thunderbolt");
        this.add(ModEntities.pomme_pomme.get(), "Pomme Pomme");
        this.add(ModEntities.cluckadoodle.get(), "Cluckadoodle");
        this.add(ModEntities.buffamoo.get(), "Buffamoo");
        this.add(ModEntities.chipsqueek.get(), "Chipsqueek");
        this.add(ModEntities.tortas.get(), "Tortas");
        this.add(ModEntities.sky_fish.get(), "Sky Fish");
        this.add(ModEntities.weagle.get(), "Weagle");
        this.add(ModEntities.goblin.get(), "Goblin");
        this.add(ModEntities.goblinArcher.get(), "Goblin Archer");
        this.add(ModEntities.duck.get(), "Duck");
        this.add(ModEntities.fairy.get(), "Fairy");
        this.add(ModEntities.ghost.get(), "Ghost");
        this.add(ModEntities.spirit.get(), "Spirit");
        this.add(ModEntities.ghostRay.get(), "Ghost Ray");
        this.add(ModEntities.spider.get(), "Spider");
        this.add(ModEntities.shadowPanther.get(), "Shadow Panther");
        this.add(ModEntities.monsterBox.get(), "Monster Box");
        this.add(ModEntities.gobbleBox.get(), "Gobble Box");
        this.add(ModEntities.marionetta.get(), "Marionetta");

        this.add(ModEntities.treasureChest.get(), "Treasure Chest");

        this.add(ModAttributes.RF_DEFENCE.get().getDescriptionId(), "DEF");
        this.add(ModAttributes.RF_MAGIC.get().getDescriptionId(), "M. ATT");
        this.add(ModAttributes.RF_MAGIC_DEFENCE.get().getDescriptionId(), "M. DEF");

        this.add(ModAttributes.RFPARA.get().getDescriptionId(), "Para");
        this.add(ModAttributes.RFPOISON.get().getDescriptionId(), "Poison");
        this.add(ModAttributes.RFSEAL.get().getDescriptionId(), "Sealing");
        this.add(ModAttributes.RFSLEEP.get().getDescriptionId(), "Sleep");
        this.add(ModAttributes.RFFAT.get().getDescriptionId(), "Fatigue");
        this.add(ModAttributes.RFCOLD.get().getDescriptionId(), "Cold");
        this.add(ModAttributes.RFDIZ.get().getDescriptionId(), "Dizz");
        this.add(ModAttributes.RFCRIT.get().getDescriptionId(), "Crit");
        this.add(ModAttributes.RFSTUN.get().getDescriptionId(), "Stun");
        this.add(ModAttributes.RFFAINT.get().getDescriptionId(), "Faint");
        this.add(ModAttributes.RFDRAIN.get().getDescriptionId(), "Drain");
        this.add(ModAttributes.RFRESWATER.get().getDescriptionId(), "Water Res");
        this.add(ModAttributes.RFRESEARTH.get().getDescriptionId(), "Earth Res");
        this.add(ModAttributes.RFRESWIND.get().getDescriptionId(), "Wind Res");
        this.add(ModAttributes.RFRESFIRE.get().getDescriptionId(), "Fire Res");
        this.add(ModAttributes.RFRESDARK.get().getDescriptionId(), "Dark Res");
        this.add(ModAttributes.RFRESLIGHT.get().getDescriptionId(), "Light Res");
        this.add(ModAttributes.RFRESLOVE.get().getDescriptionId(), "Love Res");
        this.add(ModAttributes.RFRESPARA.get().getDescriptionId(), "Paralysis Res");
        this.add(ModAttributes.RFRESPOISON.get().getDescriptionId(), "Poison Res");
        this.add(ModAttributes.RFRESSEAL.get().getDescriptionId(), "Seal Res");
        this.add(ModAttributes.RFRESSLEEP.get().getDescriptionId(), "Sleep Res");
        this.add(ModAttributes.RFRESFAT.get().getDescriptionId(), "Fatigue Res");
        this.add(ModAttributes.RFRESCOLD.get().getDescriptionId(), "Cold Res");
        this.add(ModAttributes.RFRESDIZ.get().getDescriptionId(), "Diz Res");
        this.add(ModAttributes.RFRESCRIT.get().getDescriptionId(), "Crit Res");
        this.add(ModAttributes.RFRESSTUN.get().getDescriptionId(), "Stun Res");
        this.add(ModAttributes.RFRESFAINT.get().getDescriptionId(), "Faint Res");
        this.add(ModAttributes.RFRESDRAIN.get().getDescriptionId(), "Drain Res");

        this.add(ModEffects.cold.get(), "Cold");
        this.add(ModEffects.sleep.get(), "Sleeping");
        this.add(ModEffects.poison.get(), "Poison");
        this.add(ModEffects.paralysis.get(), "Paralysis");
        this.add(ModEffects.seal.get(), "Sealed");
        this.add(ModEffects.fatigue.get(), "Fatigue");

        for (EnumDay day : EnumDay.values())
            this.add(day.translation(), day.toString().substring(0, 3));

        this.add("recipe.eat.unlock", "Unlocked recipe for %s");
        this.add("recipe.eat.fail", "Didn't learn any recipe. Maybe your crafting level is too low");

        this.add("container.shipping_bin", "Shipping Bin");

        this.add("shipping.money", "Earning from shipped items: %s");

        this.add("tile.crafting.forge", "Forging");
        this.add("tile.crafting.armor", "Crafting");
        this.add("tile.crafting.chemistry", "Chemistry");
        this.add("tile.crafting.cooking", "Cooking");

        this.add("container.shippingbin", "Shipping Bin");
        this.add("element_water", "Attribute: Water");
        this.add("element_earth", "Attribute: Earth");
        this.add("element_wind", "Attribute: Wind");
        this.add("element_fire", "Attribute: Fire");
        this.add("element_light", "Attribute: Light");
        this.add("element_dark", "Attribute: Dark");
        this.add("element_love", "Attribute: Love");
        this.add("tooltip.item.level", "Level: %s");
        this.add("tooltip.item.buy", "Buy: %s$");
        this.add("tooltip.item.sell", "Sell: %s$");
        this.add("tooltip.item.equipped", "When equipped");
        this.add("tooltip.item.upgrade", "Upgrade");
        this.add("tooltip.item.eaten", "When eaten");
        this.add("tooltip.food.hp", "HP: %s");
        this.add("tooltip.food.hp.percent", "HP: %s%%");
        this.add("tooltip.food.rp", "RP: %s");
        this.add("tooltip.food.rp.percent", "RP: %s%%");
        this.add("tooltip.food.rpmax", "RP Max: %s");
        this.add("tooltip.food.rpmax.percent", "RP Max: %s%%");
        this.add("tooltip.item.spawn", "Rename to a number to set level");
        this.add("money", "Money");
        this.add("season", "Season");
        this.add("tooltip.growth", "Growth: %sd");
        this.add("tooltip.harvested", "Harvest Amount: %s");
        this.add("season.spring", "Spring");
        this.add("season.summer", "Summer");
        this.add("season.fall", "Fall");
        this.add("season.winter", "Winter");
        this.add("tooltip.season.best", "Good Season");
        this.add("tooltip.season.bad", "Bad Season");
        this.add("sleep", "Sleep");
        this.add("monster.interact.sit", "Entity is now staying");
        this.add("monster.interact.move", "Entity is now moving freely in this area");
        this.add("monster.interact.follow", "Entity is now following you");
        this.add("tooltip.item.treasure_chest", "Shift-right-click to cycle through loot tier");
        this.add("tooltip.item.treasure_level", "Chest tier lvl: %s");

        this.add("crafting.rpMax.missing", "Missing total rp");

        this.add("level", "Level");
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

        this.add("runecraftory.command.skill.lvl.add", "Added %3$s %1$s skill level to %2$s");
        this.add("runecraftory.command.skill.xp.add", "Added %3$s %1$s skill xp points to %2$s");
        this.add("runecraftory.command.skill.lvl.set", "Set level of skill %1$s to %3$s for %2$s");
        this.add("runecraftory.command.lvl.xp.add", "Added %1$s xp points to %2$s");
        this.add("runecraftory.command.lvl.set", "Set level of %1$s to %2$s");
        this.add("runecraftory.command.reset.all", "Reset all player data for %s");
        this.add("runecraftory.command.unlock.recipes", "Unlocked all crafting recipes for %s");
        this.add("runecraftory.command.reset.recipe", "Locked all crafting recipes for %s again");

        this.add("runecraftory.jei.forge", "Forging");
        this.add("runecraftory.jei.cooking", "Cooking");
        this.add("runecraftory.jei.chemistry", "Pharmacy");
        this.add("runecraftory.jei.armory", "Equipment");
        this.add("runecraftory.jei.locked", "Unknown Recipe");

        this.add("runecraftory_book", "Runepedia");
        this.add("runecraftory.patchouli.landing", "");
        this.add("runecraftory.patchouli.main.start", "General");
        this.add("runecraftory.patchouli.main.start.desc", "");
        this.add("runecraftory.patchouli.entry.crafting", "Crafting");
        this.add("runecraftory.patchouli.entry.crafting.1", "To use any of the crafting devices you need to (depending on the config) " +
                "$(li)unlocked the recipe $(li)enough rp to craft it $(br) " +
                "Then using the correct crafting device you can make the desired item. " +
                "Upon crafting an item the amount of rp shown will be used up. You can learn new recipes by eating recipe breads which (currently) " +
                "can be obtain only via treasure chests");
        this.add("runecraftory.patchouli.entry.crafting.forge", "The forge is used to craft all weapons and tools");
        this.add("runecraftory.patchouli.entry.crafting.armor", "Use the accessory table to make various armor pieces");
        this.add("runecraftory.patchouli.entry.crafting.cooking", "The cooking table is as the name implies used to make all kinds of food");
        this.add("runecraftory.patchouli.entry.crafting.chemistry", "A chemistry set allows you to create potions and other pharmacy items");
        this.add("runecraftory.patchouli.entry.minerals", "Minerals");
        this.add("runecraftory.patchouli.entry.minerals.1", "Cluster of minerals spawn all over the world. These mineral clusters can be mined with an iron pickaxe or above but its best mined with " +
                "the $(item)mining hammers$() from this mod. Better hammers and mining skill decrease the chance of the mineral breaking and increase the chance to get rarer materials from it.");
        this.add("runecraftory.patchouli.entry.minerals.2", "Some materials even are impossible to get unless you have a high enough mining skill. " +
                "$(br)Minerals regenerate after a day. $(br)If you want to completly get rid of a mineral mine a broken one while shifting.");
        this.add("runecraftory.patchouli.entry.shipping", "Shipping Items");
        this.add("runecraftory.patchouli.entry.shipping.1", "You can craft a shipping bin to sell items. Every morning all sellable items in the shipping bin will be sold. The shipping bin inventory is global for each player." +
                "$(br)Note: ATM money is useless");
        this.add("runecraftory.patchouli.entry.entities", "Monsters");
        this.add("runecraftory.patchouli.entry.entities.1", "The mobs in this mod don't spawn by themself but through gates that appear through the world. $(br)" +
                "The type of monster a gate spawns depends on the biome and a gate will continue to spawn monsters till it is destroyed. Gates can drop their corresponding crystals upon destruction. " +
                "$(br)Bosses spawn in their structures only and can be fought once every day.");
        this.add("runecraftory.patchouli.entry.entities.2", "Monsters are able to be tamed by simple holding any item and shift-right-click them. This will consume the item and after a while $(bold)heart$(reset) particles" +
                " appear if the taming was successful and $(bold)smoke$() particles appear if otherwise. Monsters might have one or more favorite items that doubles " +
                "the taming chance and bosses can $(#ff0000)only$() be tamed by giving them their favorite items.");
        this.add("runecraftory.patchouli.entry.entities.3", "You can interact with a tamed monster using the following actions: $(li)S$(thing)hift-right-clicking with an empty hand$() to change the behavior to follow, wander or stay mode." +
                "$(li)$(thing)Right-click with an empty$() hand to (if possible) ride it." +
                "$(li)While ridden press any of the following keys $(thing)$(k:runecraftory.key.spell_1), $(k:runecraftory.key.spell_2), $(k:runecraftory.key.spell_3), $(k:runecraftory.key.spell_4)$() to perform an attack." +
                "$(li)$(thing)Right-click$() while holding a food item to feed it. ");
        this.add("runecraftory.patchouli.entry.entities.4", "Food without additional benefits simply heals it while the other apply their benefits like they would to a player");
        this.add("runecraftory.patchouli.entry.equipment", "Equipment");
        this.add("runecraftory.patchouli.entry.equipment.1", "WIP");
        this.add("runecraftory.patchouli.entry.spellskills", "Spells and Skills");
        this.add("runecraftory.patchouli.entry.spellskills.1", "WIP");
        this.add("runecraftory.patchouli.entry.farming", "Farming");
        this.add("runecraftory.patchouli.entry.farming.1", "WIP");

        this.add("runecraftory.patchouli.entry.entities.desc", "List of all monsters");
        this.add(patchouliEntity(ModEntities.wooly.getID()), "Sheep like creature that is rather passive. Shearable.");
        this.add(patchouliEntity(ModEntities.orc.getID()), "");
        this.add(patchouliEntity(ModEntities.orcArcher.getID()), "An orc but with a bow");
        this.add(patchouliEntity(ModEntities.ant.getID()), "");
        this.add(patchouliEntity(ModEntities.beetle.getID()), "");
        this.add(patchouliEntity(ModEntities.big_muck.getID()), "Mushroom like create that attacks using spores");
        this.add(patchouliEntity(ModEntities.buffamoo.getID()), "");
        this.add(patchouliEntity(ModEntities.chipsqueek.getID()), "");
        this.add(patchouliEntity(ModEntities.cluckadoodle.getID()), "");
        this.add(patchouliEntity(ModEntities.pomme_pomme.getID()), "");
        this.add(patchouliEntity(ModEntities.tortas.getID()), "");
        this.add(patchouliEntity(ModEntities.sky_fish.getID()), "");
        this.add(patchouliEntity(ModEntities.weagle.getID()), "");
        this.add(patchouliEntity(ModEntities.goblin.getID()), "");
        this.add(patchouliEntity(ModEntities.goblinArcher.getID()), "");
        this.add(patchouliEntity(ModEntities.duck.getID()), "");
        this.add(patchouliEntity(ModEntities.fairy.getID()), "");
        this.add(patchouliEntity(ModEntities.ghost.getID()), "");
        this.add(patchouliEntity(ModEntities.spirit.getID()), "");
        this.add(patchouliEntity(ModEntities.ghostRay.getID()), "");
        this.add(patchouliEntity(ModEntities.spider.getID()), "");
        this.add(patchouliEntity(ModEntities.shadowPanther.getID()), "");
        this.add(patchouliEntity(ModEntities.gobbleBox.getID()), "");
        this.add(patchouliEntity(ModEntities.monsterBox.getID()), "");

        this.add(patchouliEntity(ModEntities.ambrosia.getID()), "Butterfly boss monster. Spawns in forest groves and can be fought once a day");
        this.add(patchouliEntity(ModEntities.thunderbolt.getID()), "Horse said to be as fast as lightning. Spawns in water ruins");
        this.add(patchouliEntity(ModEntities.marionetta.getID()), "Spooky old doll that spawns in theater ruins");

        for (EnumSkills s : EnumSkills.values())
            this.add(s.getTranslation(),
                    this.capitalize(s.getTranslation().replace("skill.", "").replace("_", " "),
                            Lists.newArrayList("and")));
    }

    private String simpleTranslation(ResourceLocation res) {
        String s = res.getPath();
        if (s.startsWith("ore_broken")) {
            s = s.replace("ore_broken", "");
            s = "broken_" + s + "_mineral";
        } else if (s.startsWith("ore_")) {
            s = s.replace("ore_", "");
            s = s + "_mineral";
        } else if (s.startsWith("crop_")) {
            s = s.replace("crop_", "");
            if (s.endsWith("_giant")) {
                s = s.replace("_giant", "");
                s = "giant_" + s;
            }
        } else if (s.startsWith("seed_")) {
            s = s.replace("seed_", "");
            s = s + "_seeds";
        } else if (s.startsWith("plant_")) {
            s = s.replace("plant_", "");
        }
        return Stream.of(s.trim().split("_"))
                .filter(word -> word.length() > 0)
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" ")).replace("Plus", "+");
    }

    private static String patchouliEntity(ResourceLocation res) {
        return "runecraftory.patchouli.entry.entity." + res;
    }

    private String capitalize(String s, List<String> dont) {
        return Stream.of(s.trim().split("\\s"))
                .filter(word -> word.length() > 0)
                .map(word -> dont.contains(word) ? word : word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));
    }

    @Override
    public void run(HashCache cache) throws IOException {
        this.addTranslations();
        Map<String, String> sort = this.data.entrySet().stream().sorted((e, e2) -> order.compare(e.getKey(), e2.getKey()))
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
        SKILL,
        TOOLTIP,
        DAY,
        SEASON,
        DEATH,
        ITEMGROUP,
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
            if (s.startsWith("element_"))
                return ELEMENT;
            if (s.startsWith("attribute.rf."))
                return ATTRIBUTE;
            if (s.startsWith("skill."))
                return SKILL;
            if (s.startsWith("tooltip."))
                return TOOLTIP;
            if (s.startsWith("day."))
                return DAY;
            if (s.startsWith("season."))
                return SEASON;
            if (s.startsWith("death."))
                return DEATH;
            if (s.startsWith("itemGroup."))
                return ITEMGROUP;
            return OTHER;
        }
    }
}
