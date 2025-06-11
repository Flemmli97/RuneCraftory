package io.github.flemmli97.runecraftory.forge.data;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class ItemTagGen extends ItemTagsProvider {

    public ItemTagGen(PackOutput packOutput, BlockTagsProvider provider, ExistingFileHelper existingFileHelper) {
        super(generator, provider, RuneCraftory.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        ModItems.DATAGENTAGS.forEach((key, supList) -> supList.forEach(sup -> this.tag(key).add(sup.get())));
        this.tag(RunecraftoryTags.SHORTSWORDS)
                .add(ModItems.PLANT_SWORD.get());
        this.tag(RunecraftoryTags.SHIELDS)
                .add(ModItems.PLANT_SHIELD.get());
        ModItems.DATAGENTAGS.keySet().stream().sorted(Comparator.comparing(TagKey::location)).forEach(key -> {
            if (key.location().getPath().startsWith("foods/")) {
                this.tag(RunecraftoryTags.FOODS).addTag(key);
            }
        });

        this.tag(RunecraftoryTags.SUGAR).add(Items.SUGAR);
        this.tag(tempKeyFabric("crops/carrot")).add(Items.CARROT);
        this.tag(tempKeyFabric("crops/potato")).add(Items.POTATO);

        //Forge copy tags
        this.tag(RunecraftoryTags.IRON)
                .add(Items.IRON_INGOT)
                .addOptional(Tags.Items.INGOTS_IRON.location());
        this.tag(RunecraftoryTags.GOLD)
                .add(Items.GOLD_INGOT)
                .addOptional(Tags.Items.INGOTS_GOLD.location());
        this.tag(RunecraftoryTags.COPPER)
                .add(Items.COPPER_INGOT)
                .addOptional(Tags.Items.INGOTS_COPPER.location());
        this.tag(RunecraftoryTags.EMERALDS)
                .add(Items.EMERALD)
                .addOptional(Tags.Items.GEMS_EMERALD.location());
        this.tag(RunecraftoryTags.CHEST)
                .add(Items.ENDER_CHEST)
                .add(Items.TRAPPED_CHEST)
                .add(Items.CHEST)
                .add(Items.TRAPPED_CHEST)
                .addOptional(Tags.Items.CHESTS.location());
        this.tag(RunecraftoryTags.SHEARS)
                .add(Items.SHEARS)
                .addOptional(Tags.Items.SHEARS.location());
        this.tag(RunecraftoryTags.COBBLESTONE)
                .add(Items.COBBLESTONE)
                .add(Items.INFESTED_COBBLESTONE)
                .add(Items.MOSSY_COBBLESTONE)
                .add(Items.COBBLED_DEEPSLATE)
                .addOptional(Tags.Items.COBBLESTONE.location());
        this.tag(RunecraftoryTags.WOOD_ROD)
                .add(Items.STICK)
                .addOptional(Tags.Items.RODS_WOODEN.location());
        this.tag(RunecraftoryTags.SEEDS)
                .add(Items.WHEAT_SEEDS)
                .add(Items.BEETROOT_SEEDS)
                .add(Items.MELON_SEEDS)
                .add(Items.PUMPKIN_SEEDS)
                .addTag(Tags.Items.SEEDS);
        this.tag(RunecraftoryTags.CROPS)
                .add(Items.BEETROOT)
                .add(Items.CARROT)
                .add(Items.NETHER_WART)
                .add(Items.POTATO)
                .add(Items.WHEAT);

        handleWithForge(this::tag, Tags.Items.SLIMEBALLS, RunecraftoryTags.SLIME, ModItems.GLUE.get());
        this.tag(RunecraftoryTags.SLIME)
                .add(Items.SLIME_BALL);
        handleWithForge(Registry.ITEM_REGISTRY, this::tag, RunecraftoryTags.tagCommon("raw_materials/tin"), ModItems.RAW_TIN.get());
        handleWithForge(this::tag, RunecraftoryTags.TIN_F, RunecraftoryTags.TIN, ModItems.TIN_INGOT.get());
        handleWithForge(Registry.ITEM_REGISTRY, this::tag, RunecraftoryTags.tagCommon("dusts/bronze"), ModItems.BRONZE_DUST.get());
        handleWithForge(this::tag, RunecraftoryTags.BRONZE_F, RunecraftoryTags.BRONZE, ModItems.BRONZE_INGOT.get());
        handleWithForge(Registry.ITEM_REGISTRY, this::tag, RunecraftoryTags.tagCommon("raw_materials/silver"), ModItems.RAW_SILVER.get());
        handleWithForge(this::tag, RunecraftoryTags.SILVER_F, RunecraftoryTags.SILVER, ModItems.SILVER_INGOT.get());
        handleWithForge(Registry.ITEM_REGISTRY, this::tag, RunecraftoryTags.tagCommon("raw_materials/platinum"), ModItems.RAW_PLATINUM.get());
        handleWithForge(this::tag, RunecraftoryTags.PLATINUM_F, RunecraftoryTags.PLATINUM, ModItems.PLATINUM_INGOT.get());

        handleWithForge(this::tag, RunecraftoryTags.AMETHYST_F, RunecraftoryTags.AMETHYSTS, ModItems.AMETHYST.get());
        this.tag(RunecraftoryTags.AMETHYSTS)
                .add(Items.AMETHYST_SHARD);
        handleWithForge(this::tag, RunecraftoryTags.AQUAMARINE_F, RunecraftoryTags.AQUAMARINES, ModItems.AQUAMARINE.get());
        handleWithForge(this::tag, RunecraftoryTags.RUBY_F, RunecraftoryTags.RUBIES, ModItems.RUBY.get());
        handleWithForge(this::tag, RunecraftoryTags.SAPPHIRE_F, RunecraftoryTags.SAPPHIRES, ModItems.SAPPHIRE.get());

        this.tag(RunecraftoryTags.ORICHALCUM)
                .add(ModItems.ORICHALCUM.get());
        this.tag(RunecraftoryTags.DRAGONIC)
                .add(ModItems.DRAGONIC.get());

        this.tag(RunecraftoryTags.GENERIC_TRASH)
                .add(ModItems.FAILED_DISH.get(), ModItems.DISASTROUS_DISH.get(),
                        ModItems.WEEDS.get(), ModItems.WITHERED_GRASS.get())
                .addTag(ItemTags.DIRT)
                .addTag(RunecraftoryTags.COBBLESTONE);

        this.tag(RunecraftoryTags.MINERALS)
                .add(ModItems.SCRAP_PLUS.get())
                .addTag(RunecraftoryTags.IRON)
                .addTag(RunecraftoryTags.GOLD)
                .addTag(RunecraftoryTags.COPPER)
                .addTag(RunecraftoryTags.COPPER)
                .addTag(RunecraftoryTags.BRONZE)
                .addTag(RunecraftoryTags.SILVER)
                .addTag(RunecraftoryTags.PLATINUM)
                .addTag(RunecraftoryTags.ORICHALCUM)
                .addTag(RunecraftoryTags.DRAGONIC);
        this.tag(RunecraftoryTags.JEWELS)
                .add(ModItems.AMETHYST.get())
                .add(ModItems.AQUAMARINE.get())
                .add(ModItems.RUBY.get())
                .add(ModItems.SAPPHIRE.get())
                .add(ModItems.CORE_RED.get())
                .add(ModItems.CORE_BLUE.get())
                .add(ModItems.CORE_YELLOW.get())
                .add(ModItems.CORE_GREEN.get())
                .add(ModItems.CRYSTAL_SKULL.get())
                .add(Items.EMERALD)
                .add(Items.DIAMOND);
        handleWithForgeTags(this::tag, Tags.Items.GEMS, tempKeyFabric("gems"), RunecraftoryTags.JEWELS);
        this.tag(RunecraftoryTags.CRYSTALS)
                .add(ModItems.CRYSTAL_WATER.get())
                .add(ModItems.CRYSTAL_EARTH.get())
                .add(ModItems.CRYSTAL_FIRE.get())
                .add(ModItems.CRYSTAL_WIND.get())
                .add(ModItems.CRYSTAL_LIGHT.get())
                .add(ModItems.CRYSTAL_DARK.get())
                .add(ModItems.CRYSTAL_LOVE.get())
                .add(ModItems.CRYSTAL_SMALL.get())
                .add(ModItems.CRYSTAL_BIG.get())
                .add(ModItems.CRYSTAL_MAGIC.get())
                .add(ModItems.CRYSTAL_RUNE.get())
                .add(ModItems.CRYSTAL_ELECTRO.get());
        this.tag(RunecraftoryTags.STICKS)
                .addOptionalTag(Tags.Items.RODS_WOODEN.location())
                .add(Items.STICK,
                        ModItems.STICK_THICK.get(),
                        ModItems.HORN_INSECT.get(),
                        ModItems.HORN_RIGID.get(),
                        ModItems.HORN_DEVIL.get(),
                        ModItems.PLANT_STEM.get(),
                        ModItems.HORN_BULL.get(),
                        ModItems.MOVING_BRANCH.get());
        this.tag(RunecraftoryTags.LIQUIDS)
                .add(ModItems.GLUE.get(),
                        ModItems.DEVIL_BLOOD.get(),
                        ModItems.PARA_POISON.get(),
                        ModItems.POISON_KING.get());
        this.tag(RunecraftoryTags.FEATHERS)
                .add(Items.FEATHER,
                        ModItems.FEATHER_BLACK.get(),
                        ModItems.FEATHER_THUNDER.get(),
                        ModItems.FEATHER_YELLOW.get(),
                        ModItems.DRAGON_FIN.get())
                .addOptionalTag(Tags.Items.FEATHERS.location());
        this.tag(RunecraftoryTags.SHELLS_BONES)
                .add(ModItems.TURTLE_SHELL.get(),
                        ModItems.FISH_FOSSIL.get(),
                        ModItems.SKULL.get(),
                        ModItems.DRAGON_BONES.get(),
                        ModItems.TORTOISE_SHELL.get(),
                        ModItems.AMMONITE.get());
        this.tag(RunecraftoryTags.STONES)
                .add(ModItems.ROCK.get(),
                        ModItems.STONE_ROUND.get(),
                        ModItems.STONE_TINY.get(),
                        ModItems.STONE_GOLEM.get(),
                        ModItems.TABLET_GOLEM.get(),
                        ModItems.STONE_SPIRIT.get(),
                        ModItems.TABLET_TRUTH.get());
        this.tag(RunecraftoryTags.STRINGS)
                .add(Items.STRING,
                        ModItems.YARN.get(),
                        ModItems.OLD_BANDAGE.get(),
                        ModItems.AMBROSIAS_THORNS.get(),
                        ModItems.THREAD_SPIDER.get(),
                        ModItems.PUPPETRY_STRINGS.get(),
                        ModItems.VINE.get(),
                        ModItems.TAIL_SCORPION.get(),
                        ModItems.STRONG_VINE.get(),
                        ModItems.THREAD_PRETTY.get(),
                        ModItems.TAIL_CHIMERA.get());
        this.tag(RunecraftoryTags.SHARDS)
                .add(Items.FLINT,
                        ModItems.ARROW_HEAD.get(),
                        ModItems.BLADE_SHARD.get(),
                        ModItems.BROKEN_HILT.get(),
                        ModItems.BROKEN_BOX.get(),
                        ModItems.BLADE_GLISTENING.get(),
                        ModItems.GREAT_HAMMER_SHARD.get(),
                        ModItems.HAMMER_PIECE.get(),
                        ModItems.SHOULDER_PIECE.get(),
                        ModItems.PIRATES_ARMOR.get(),
                        ModItems.SCREW_RUSTY.get(),
                        ModItems.SCREW_SHINY.get(),
                        ModItems.ROCK_SHARD_LEFT.get(),
                        ModItems.ROCK_SHARD_RIGHT.get(),
                        ModItems.MTGU_PLATE.get(),
                        ModItems.BROKEN_ICE_WALL.get());
        this.tag(RunecraftoryTags.FURS)
                .add(ModItems.FUR_SMALL.get(),
                        ModItems.FUR_MEDIUM.get(),
                        ModItems.FUR_LARGE.get(),
                        ModItems.FUR.get(),
                        ModItems.FURBALL.get(),
                        ModItems.DOWN_YELLOW.get(),
                        ModItems.FUR_QUALITY.get(),
                        ModItems.DOWN_PENGUIN.get(),
                        ModItems.LIGHTNING_MANE.get(),
                        ModItems.FUR_RED_LION.get(),
                        ModItems.FUR_BLUE_LION.get(),
                        ModItems.CHEST_HAIR.get());
        this.tag(RunecraftoryTags.POWDERS)
                .add(ModItems.SPORE.get(),
                        ModItems.POWDER_POISON.get(),
                        ModItems.SPORE_HOLY.get(),
                        ModItems.FAIRY_DUST.get(),
                        ModItems.FAIRY_ELIXIR.get(),
                        ModItems.ROOT.get(),
                        ModItems.POWDER_MAGIC.get(),
                        ModItems.POWDER_MYSTERIOUS.get(),
                        ModItems.MAGIC.get(),
                        ModItems.ASH_EARTH.get(),
                        ModItems.ASH_FIRE.get(),
                        ModItems.ASH_WATER.get(),
                        ModItems.TURNIPS_MIRACLE.get(),
                        ModItems.MELODY_BOTTLE.get());
        this.tag(RunecraftoryTags.CLOTHS)
                .add(Items.LEATHER,
                        ModItems.CLOTH_CHEAP.get(),
                        ModItems.CLOTH_QUALITY.get(),
                        ModItems.CLOTH_QUALITY_WORN.get(),
                        ModItems.CLOTH_SILK.get(),
                        ModItems.GHOST_HOOD.get(),
                        ModItems.GLOVE_GIANT.get(),
                        ModItems.GLOVE_BLUE_GIANT.get(),
                        ModItems.CARAPACE_INSECT.get(),
                        ModItems.CARAPACE_PRETTY.get(),
                        ModItems.CLOTH_ANCIENT_ORC.get());
        this.tag(RunecraftoryTags.CLAWS_FANGS)
                .add(ModItems.JAW_INSECT.get(),
                        ModItems.CLAW_PANTHER.get(),
                        ModItems.CLAW_MAGIC.get(),
                        ModItems.FANG_WOLF.get(),
                        ModItems.FANG_GOLD_WOLF.get(),
                        ModItems.CLAW_PALM.get(),
                        ModItems.CLAW_MALM.get(),
                        ModItems.GIANTS_NAIL.get(),
                        ModItems.CLAW_CHIMERA.get(),
                        ModItems.TUSK_IVORY.get(),
                        ModItems.TUSK_UNBROKEN_IVORY.get(),
                        ModItems.SCORPION_PINCER.get(),
                        ModItems.DANGEROUS_SCISSORS.get(),
                        ModItems.PROPELLOR_CHEAP.get(),
                        ModItems.PROPELLOR_QUALITY.get(),
                        ModItems.FANG_DRAGON.get(),
                        ModItems.JAW_QUEEN.get(),
                        ModItems.FANG_DRAGON.get(),
                        ModItems.GIANTS_NAIL_BIG.get());
        this.tag(RunecraftoryTags.SCALES)
                .add(ModItems.SCALE_WET.get(),
                        ModItems.SCALE_GRIMOIRE.get(),
                        ModItems.SCALE_DRAGON.get(),
                        ModItems.SCALE_CRIMSON.get(),
                        ModItems.SCALE_BLUE.get(),
                        ModItems.SCALE_GLITTER.get(),
                        ModItems.SCALE_LOVE.get(),
                        ModItems.SCALE_BLACK.get(),
                        ModItems.SCALE_FIRE.get(),
                        ModItems.SCALE_EARTH.get(),
                        ModItems.SCALE_LEGEND.get());

        this.tag(RunecraftoryTags.HIGH_TIER_TOOLS)
                .add(ModItems.HOE_PLATINUM.get())
                .add(ModItems.WATERING_CAN_PLATINUM.get())
                .add(ModItems.SICKLE_PLATINUM.get())
                .add(ModItems.HAMMER_PLATINUM.get())
                .add(ModItems.AXE_PLATINUM.get())
                .add(ModItems.FISHING_ROD_PLATINUM.get());

        this.tag(RunecraftoryTags.UPGRADABLE_HELD).addTag(RunecraftoryTags.TOOLS).addTag(RunecraftoryTags.WEAPONS);
        this.tag(RunecraftoryTags.TOOLS).addTag(RunecraftoryTags.HOES).addTag(RunecraftoryTags.WATERINGCANS)
                .addTag(RunecraftoryTags.SICKLES).addTag(RunecraftoryTags.HAMMER_TOOLS)
                .addTag(RunecraftoryTags.AXE_TOOLS).addTag(RunecraftoryTags.FISHING_RODS);

        handleWithForgeTags(this::tag, tempKeyForge("tools/hoes"), tempKeyFabric("hoes"), RunecraftoryTags.HOES);
        handleWithForgeTags(this::tag, tempKeyForge("tools/fishing_rods"), tempKeyFabric("fishing_rods"), RunecraftoryTags.FISHING_RODS);

        this.tag(RunecraftoryTags.WEAPONS).addTag(RunecraftoryTags.SHORTSWORDS).addTag(RunecraftoryTags.LONGSWORDS)
                .addTag(RunecraftoryTags.SPEARS).addTag(RunecraftoryTags.AXES)
                .addTag(RunecraftoryTags.HAMMERS).addTag(RunecraftoryTags.DUALBLADES)
                .addTag(RunecraftoryTags.FISTS).addTag(RunecraftoryTags.STAFFS);
        this.tag(RunecraftoryTags.HAMMER_AXES)
                .addTag(RunecraftoryTags.HAMMERS).addTag(RunecraftoryTags.AXES);

        handleWithForgeTags(this::tag, tempKeyForge("tools/swords"), tempKeyFabric("swords"), RunecraftoryTags.SHORTSWORDS, RunecraftoryTags.LONGSWORDS, RunecraftoryTags.DUALBLADES);

        this.tag(RunecraftoryTags.EQUIPMENT).addTag(RunecraftoryTags.HELMET).addTag(RunecraftoryTags.CHESTPLATE)
                .addTag(RunecraftoryTags.ACCESSORIES).addTag(RunecraftoryTags.BOOTS)
                .addTag(RunecraftoryTags.SHIELDS);

        handleWithForgeTags(this::tag, tempKeyForge("armors/helmets"), tempKeyFabric("helmets"), RunecraftoryTags.HELMET);
        handleWithForgeTags(this::tag, tempKeyForge("armors/chestplates"), tempKeyFabric("chestplates"), RunecraftoryTags.CHESTPLATE);
        handleWithForgeTags(this::tag, tempKeyForge("armors/boots"), tempKeyFabric("boots"), RunecraftoryTags.BOOTS);
        handleWithForgeTags(this::tag, tempKeyForge("tools/shields"), tempKeyFabric("shields"), RunecraftoryTags.SHIELDS);

        handleWithForge(this::tag, Tags.Items.EGGS, RunecraftoryTags.EGGS, ModItems.EGG_S.get(),
                ModItems.EGG_M.get(), ModItems.EGG_L.get(), Items.EGG);
        this.tag(RunecraftoryTags.MILKS)
                .add(ModItems.MILK_S.get())
                .add(ModItems.MILK_M.get())
                .add(ModItems.MILK_L.get())
                .add(Items.MILK_BUCKET);

        // Taming tags
        this.tag(RunecraftoryTags.tamingTag(ModEntities.WOOLY.get()))
                .addTag(RunecraftoryTags.SHEARS)
                .addTag(ItemTags.WOOL)
                .add(Items.WHEAT);
        this.tag(RunecraftoryTags.tamingTag(ModEntities.ORC.get()))
                .add(ModItems.CHEAP_BRACELET.get())
                .add(ModItems.CLOTH_CHEAP.get())
                .add(ModItems.OLD_BANDAGE.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.ORC_ARCHER.get()))
                .add(Items.GUNPOWDER)
                .add(Items.ARROW)
                .add(ModItems.ARROW_HEAD.get())
                .add(ModItems.RECOVERY_POTION.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.ANT.get()))
                .add(ModItems.CARAPACE_INSECT.get())
                .add(ModItems.CARAPACE_PRETTY.get())
                .add(ModItems.JAW_INSECT.get())
                .add(ModItems.JAW_QUEEN.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.BEETLE.get()))
                .add(ModItems.CARAPACE_INSECT.get())
                .add(ModItems.CARAPACE_PRETTY.get())
                .add(ModItems.HORN_INSECT.get())
                .add(ModItems.HORN_RIGID.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.BIG_MUCK.get()))
                .add(Items.BROWN_MUSHROOM)
                .add(Items.RED_MUSHROOM)
                .add(ModItems.MUSHROOM.get())
                .add(ModItems.MONARCH_MUSHROOM.get())
                .add(ModItems.SPORE.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.BUFFAMOO.get()))
                .addTag(RunecraftoryTags.MILKS)
                .add(Items.WHEAT);
        this.tag(RunecraftoryTags.tamingTag(ModEntities.CHIPSQUEEK.get()))
                .addTag(RunecraftoryTags.FURS);
        this.tag(RunecraftoryTags.tamingTag(ModEntities.CLUCKADOODLE.get()))
                .addTag(RunecraftoryTags.EGGS)
                .addTag(RunecraftoryTags.SEEDS);
        this.tag(RunecraftoryTags.tamingTag(ModEntities.POMME_POMME.get()))
                .add(Items.APPLE);
        this.tag(RunecraftoryTags.tamingTag(ModEntities.TORTAS.get()))
                .add(Items.SEAGRASS)
                .add(ModItems.TORTOISE_SHELL.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.SKY_FISH.get()))
                .add(Items.KELP);
        this.tag(RunecraftoryTags.tamingTag(ModEntities.WEAGLE.get()))
                .addTag(RunecraftoryTags.FEATHERS);
        this.tag(RunecraftoryTags.tamingTag(ModEntities.GOBLIN.get()))
                .add(ModItems.OLD_BANDAGE.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.GOBLIN_ARCHER.get()))
                .addTag(RunecraftoryTags.tamingTag(ModEntities.GOBLIN.get()));
        this.tag(RunecraftoryTags.tamingTag(ModEntities.DUCK.get()))
                .add(ModItems.FUR.get())
                .add(Items.FEATHER);
        this.tag(RunecraftoryTags.tamingTag(ModEntities.FAIRY.get()))
                .add(ModItems.FAIRY_DUST.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.GHOST.get()))
                .add(ModItems.GHOST_HOOD.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.SPIRIT.get()))
                .add(ModItems.CRYSTAL_DARK.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.GHOST_RAY.get()))
                .add(ModItems.GHOST_HOOD.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.SPIDER.get()))
                .add(Items.STRING)
                .add(ModItems.THREAD_SPIDER.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.SHADOW_PANTHER.get()))
                .add(ModItems.FUR.get())
                .add(ModItems.CLAW_PANTHER.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.MONSTER_BOX.get()))
                .add(ModItems.FAILED_DISH.get())
                .add(ModItems.DISASTROUS_DISH.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.GOBBLE_BOX.get()))
                .add(ModItems.FAILED_DISH.get())
                .add(ModItems.DISASTROUS_DISH.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.KILLER_ANT.get()))
                .add(ModItems.CARAPACE_PRETTY.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.HIGH_ORC.get()))
                .addTag(RunecraftoryTags.tamingTag(ModEntities.ORC.get()));
        this.tag(RunecraftoryTags.tamingTag(ModEntities.ORC_HUNTER.get()))
                .addTag(RunecraftoryTags.tamingTag(ModEntities.ORC_ARCHER.get()));
        this.tag(RunecraftoryTags.tamingTag(ModEntities.HORNET.get()))
                .add(Items.HONEY_BOTTLE)
                .add(Items.HONEYCOMB);
        this.tag(RunecraftoryTags.tamingTag(ModEntities.SILVER_WOLF.get()))
                .add(Items.BONE)
                .add(ModItems.FANG_WOLF.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.LEAF_BALL.get()))
                .addTag(ItemTags.LEAVES);
        this.tag(RunecraftoryTags.tamingTag(ModEntities.FURPY.get()))
                .addTag(RunecraftoryTags.tamingTag(ModEntities.CHIPSQUEEK.get()));
        this.tag(RunecraftoryTags.tamingTag(ModEntities.PALM_CAT.get()))
                .add(ModItems.FUR.get())
                .add(Items.CAKE)
                .add(ModItems.CAKE.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.MINO.get()))
                .add(ModItems.GRAPES.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.TRICKY_MUCK.get()))
                .add(ModItems.POWDER_POISON.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.FLOWER_LILY.get()))
                .addTag(ItemTags.FLOWERS);
        this.tag(RunecraftoryTags.tamingTag(ModEntities.KING_WOOLY.get()))
                .addTag(RunecraftoryTags.tamingTag(ModEntities.WOOLY.get()));
        this.tag(RunecraftoryTags.tamingTag(ModEntities.BUFFALOO.get()))
                .add(ModItems.HORN_BULL.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.GOBLIN_PIRATE.get()))
                .addTag(RunecraftoryTags.tamingTag(ModEntities.GOBLIN.get()));
        this.tag(RunecraftoryTags.tamingTag(ModEntities.GOBLIN_GANGSTER.get()))
                .addTag(RunecraftoryTags.tamingTag(ModEntities.GOBLIN.get()));
        this.tag(RunecraftoryTags.tamingTag(ModEntities.IGNIS.get()))
                .add(ModItems.CRYSTAL_FIRE.get())
                .add(ModItems.CRYSTAL_MAGIC.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.SCORPION.get()))
                .add(ModItems.TAIL_SCORPION.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.TROLL.get()))
                .add(ModItems.GIANTS_NAIL.get())
                .add(ModItems.GLOVE_GIANT.get())
                .add(ModItems.HAMMER_PIECE.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.FLOWER_LION.get()))
                .addTag(RunecraftoryTags.tamingTag(ModEntities.FLOWER_LILY.get()));
        this.tag(RunecraftoryTags.tamingTag(ModEntities.TOMATO_GHOST.get()))
                .addTag(tempKeyFabric("vegetables/tomato"));
        this.tag(RunecraftoryTags.tamingTag(ModEntities.GOBLIN_CAPTAIN.get()))
                .addTag(RunecraftoryTags.tamingTag(ModEntities.GOBLIN.get()));
        this.tag(RunecraftoryTags.tamingTag(ModEntities.GOBLIN_DON.get()))
                .addTag(RunecraftoryTags.tamingTag(ModEntities.GOBLIN.get()));
        this.tag(RunecraftoryTags.tamingTag(ModEntities.MINERAL_SQUEEK.get()))
                .addTag(RunecraftoryTags.tamingTag(ModEntities.CHIPSQUEEK.get()));
        this.tag(RunecraftoryTags.tamingTag(ModEntities.NAPPIE.get()))
                .add(ModItems.PINEAPPLE.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.MALM_TIGER.get()))
                .add(ModItems.FUR.get())
                .add(Items.CAKE)
                .add(ModItems.CAKE.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.LITTLE_EMPEROR.get()))
                .add(ModItems.CRYSTAL_LIGHT.get())
                .add(ModItems.CRYSTAL_DARK.get())
                .add(ModItems.CRYSTAL_SMALL.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.DEMON.get()))
                .add(ModItems.CRYSTAL_DARK.get())
                .add(ModItems.CRYSTAL_BIG.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.ARCH_DEMON.get()))
                .add(ModItems.CRYSTAL_DARK.get())
                .add(ModItems.CRYSTAL_BIG.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.MINOTAUR.get()))
                .add(ModItems.HAMMER_PIECE.get())
                .addTag(RunecraftoryTags.AXES);
        this.tag(RunecraftoryTags.tamingTag(ModEntities.MINOTAUR_KING.get()))
                .add(ModItems.HAMMER_PIECE.get())
                .addTag(RunecraftoryTags.AXES);

        this.tag(RunecraftoryTags.tamingTag(ModEntities.AMBROSIA.get()))
                .add(ModItems.TOYHERB_GIANT.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.THUNDERBOLT.get()))
                .add(ModItems.CARROT_GIANT.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.MARIONETTA.get()))
                .add(Items.CAKE)
                .add(ModItems.CHEESECAKE.get())
                .add(ModItems.CHOCOLATE_CAKE.get())
                .add(ModItems.APPLE_PIE.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.HANDONETTA.get()))
                .add(Items.CAKE)
                .add(ModItems.CHEESECAKE.get())
                .add(ModItems.CHOCOLATE_CAKE.get())
                .add(ModItems.APPLE_PIE.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.DEAD_TREE.get()))
                .add(ModItems.GREENIFIER_PLUS.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.CHIMERA.get()))
                .add(ModItems.ROYAL_CURRY.get())
                .add(ModItems.ULTIMATE_CURRY.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.RACCOON.get()))
                .add(ModItems.UDON.get())
                .add(ModItems.TEMPURA_UDON.get())
                .add(ModItems.CURRY_UDON.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.SKELEFANG.get()))
                .add(Items.DRAGON_HEAD);

        // Crops stuff
        handleWithForge(Registry.ITEM_REGISTRY, this::tag, RunecraftoryTags.TURNIP, ModItems.TURNIP.get(), ModItems.TURNIP_GIANT.get(), ModItems.TURNIP_PINK.get(), ModItems.TURNIP_PINK_GIANT.get(),
                ModItems.GOLDEN_TURNIP.get(), ModItems.GOLDEN_TURNIP_GIANT.get());
        handleWithForge(Registry.ITEM_REGISTRY, this::tag, RunecraftoryTags.GRAPES, ModItems.GRAPES.get());
        handleWithForge(Registry.ITEM_REGISTRY, this::tag, RunecraftoryTags.ORANGE, ModItems.ORANGE.get());

        TagKey<Item> forgeParentTag = tempKeyForge("seeds");
        for (RegistryEntrySupplier<Item> sup : ModItems.SEEDS) {
            TagKey<Item> forgeTag = tempKeyForge("seeds/" + sup.getID().getPath().replace("seed_", ""));
            TagKey<Item> commonTag = tempKeyFabric("seeds/" + sup.getID().getPath().replace("seed_", ""));
            handleWithForge(this::tag, forgeTag, commonTag, sup.get());
            this.tag(forgeParentTag).addTag(forgeTag);
        }
        handleWithForge(this::tag, forgeParentTag, RunecraftoryTags.SEEDS);

        forgeParentTag = tempKeyForge("vegetables");
        TagKey<Item> forgeCropParentTag = tempKeyForge("crops");
        this.tag(RunecraftoryTags.VEGGIES).addTag(forgeParentTag);
        this.tag(RunecraftoryTags.CROPS).addTag(forgeCropParentTag);
        Set<String> done = new HashSet<>();
        for (Pair<String, RegistryEntrySupplier<Item>> sup : ModItems.VEGGIES) {
            String name = sup.getFirst();
            boolean none = done.add(name);
            TagKey<Item> commonTag = tempKeyFabric("vegetables/" + name);
            TagKey<Item> forgeTag = tempKeyForge("vegetables/" + name);
            handleWithForge(this::tag, forgeTag, commonTag, sup.getSecond().get());
            if (none)
                this.tag(forgeParentTag).addTag(forgeTag);

            //Also add to crops tag
            commonTag = tempKeyFabric("crops/" + name);
            forgeTag = tempKeyForge("crops/" + name);
            if (sup.getSecond() != ModItems.TURNIP && sup.getSecond() != ModItems.TURNIP_GIANT) { // Normal turnip handled already before
                handleWithForge(this::tag, forgeTag, commonTag, sup.getSecond().get());
            }
            if (none)
                this.tag(forgeCropParentTag).addTag(forgeTag);
        }

        forgeParentTag = tempKeyForge("fruits");
        this.tag(RunecraftoryTags.FRUITS).addTag(forgeParentTag);
        for (Pair<String, RegistryEntrySupplier<Item>> sup : ModItems.FRUITS) {
            String name = sup.getFirst();
            boolean none = done.add(name);
            TagKey<Item> commonTag = tempKeyFabric("fruits/" + name);
            TagKey<Item> forgeTag = tempKeyForge("fruits/" + name);
            handleWithForge(this::tag, forgeTag, commonTag, sup.getSecond().get());
            if (none)
                this.tag(forgeParentTag).addTag(forgeTag);

            //Also add to crops tag
            commonTag = tempKeyFabric("crops/" + name);
            forgeTag = tempKeyForge("crops/" + name);
            handleWithForge(this::tag, forgeTag, commonTag, sup.getSecond().get());
            if (none)
                this.tag(forgeCropParentTag).addTag(forgeTag);
        }

        forgeParentTag = tempKeyForge("flowers");
        this.tag(RunecraftoryTags.FLOWERS).addTag(forgeParentTag);
        for (Pair<String, RegistryEntrySupplier<Item>> sup : ModItems.FLOWERS) {
            this.tag(forgeParentTag).add(sup.getSecond().get());
            //Also add to crops tag
            String name = sup.getFirst();
            boolean none = done.add(name);
            TagKey<Item> commonTag = tempKeyFabric("crops/" + name);
            TagKey<Item> forgeTag = tempKeyForge("crops/" + name);
            handleWithForge(this::tag, forgeTag, commonTag, sup.getSecond().get());
            if (none)
                this.tag(forgeCropParentTag).addTag(forgeTag);
        }

        this.tag(RunecraftoryTags.QUICKHARVEST_BYPASS)
                .add(ModItems.FORMULAR_A.get(), ModItems.FORMULAR_B.get(), ModItems.FORMULAR_C.get(), ModItems.MINIMIZER.get(),
                        ModItems.GIANTIZER.get(), ModItems.GREENIFIER.get(), ModItems.GREENIFIER_PLUS.get(), ModItems.WETTABLE_POWDER.get())
                .add(ModItems.WATERING_CAN_SCRAP.get(), ModItems.WATERING_CAN_IRON.get(),
                        ModItems.WATERING_CAN_SILVER.get(), ModItems.WATERING_CAN_GOLD.get(), ModItems.WATERING_CAN_PLATINUM.get());
    }

    protected static TagKey<Item> tempKeyForge(String path) {
        return TagKey.create(Registry.ITEM_REGISTRY, ResourceLocation.fromNamespaceAndPath("forge", path));
    }

    protected static TagKey<Item> tempKeyFabric(String path) {
        return TagKey.create(Registry.ITEM_REGISTRY, ResourceLocation.fromNamespaceAndPath("c", path));
    }

    @SafeVarargs
    public static <T> void handleWithForge(ResourceKey<? extends Registry<T>> key,
                                           Function<TagKey<T>, TagsProvider.TagAppender<T>> provider, TagKey<T> fabricTag, T... elements) {
        TagKey<T> forgeTag = TagKey.create(key, ResourceLocation.fromNamespaceAndPath("forge", fabricTag.location().getPath()));
        handleWithForge(provider, forgeTag, fabricTag, elements);
    }

    @SafeVarargs
    public static <T> void handleWithForge(Function<TagKey<T>, TagsProvider.TagAppender<T>> provider, TagKey<T> forgeTag, TagKey<T> fabricTag, T... elements) {
        provider.apply(forgeTag).add(elements); // Use forge as base so add to forge
        TagsProvider.TagAppender<T> appender = provider.apply(fabricTag); // On fabric end simply refer to the forge tag
        Tag.Entry ent = new Tag.TagEntry(forgeTag.location());
        if (appender.getInternalBuilder().getEntries().noneMatch(e -> e.entry().equals(ent))) {
            appender.add(ent);
        }
    }

    @SafeVarargs
    public static <T> void handleWithForgeTags(ResourceKey<? extends Registry<T>> key,
                                               Function<TagKey<T>, TagsProvider.TagAppender<T>> provider, TagKey<T> fabricTag, TagKey<T>... elements) {
        TagKey<T> forgeTag = TagKey.create(key, ResourceLocation.fromNamespaceAndPath("forge", fabricTag.location().getPath()));
        handleWithForgeTags(provider, forgeTag, fabricTag, elements);
    }

    @SafeVarargs
    public static <T> void handleWithForgeTags(Function<TagKey<T>, TagsProvider.TagAppender<T>> provider, TagKey<T> forgeTag, TagKey<T> fabricTag, TagKey<T>... elements) {
        provider.apply(forgeTag).addTags(elements); // Use forge as base so add to forge
        TagsProvider.TagAppender<T> appender = provider.apply(fabricTag); // On fabric end simply refer to the forge tag
        Tag.Entry ent = new Tag.TagEntry(forgeTag.location());
        if (appender.getInternalBuilder().getEntries().noneMatch(e -> e.entry().equals(ent))) {
            appender.add(ent);
        }
    }
}
