package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemTagGen extends ItemTagsProvider {

    public ItemTagGen(DataGenerator generator, BlockTagsProvider provider, ExistingFileHelper existingFileHelper) {
        super(generator, provider, RuneCraftory.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        ModItems.DATAGENTAGS.forEach((key, supList) -> supList.forEach(sup -> this.tag(key).add(sup.get())));
        this.tag(ModTags.SHORTSWORDS)
                .add(ModItems.PLANT_SWORD.get());
        this.tag(ModTags.SHIELDS)
                .add(ModItems.PLANT_SHIELD.get());

        //Forge copy tags
        this.tag(ModTags.IRON)
                .add(Items.IRON_INGOT)
                .addOptional(Tags.Items.INGOTS_IRON.location());
        this.tag(ModTags.GOLD)
                .add(Items.GOLD_INGOT)
                .addOptional(Tags.Items.INGOTS_GOLD.location());
        this.tag(ModTags.COPPER)
                .add(Items.COPPER_INGOT)
                .addOptional(Tags.Items.INGOTS_COPPER.location());
        this.tag(ModTags.COPPER_BLOCK)
                .add(Items.COPPER_BLOCK)
                .add(Items.CUT_COPPER)
                .addOptional(Tags.Items.STORAGE_BLOCKS_COPPER.location());
        this.tag(ModTags.EMERALDS)
                .add(Items.EMERALD)
                .addOptional(Tags.Items.GEMS_EMERALD.location());
        this.tag(ModTags.CHEST)
                .add(Items.ENDER_CHEST)
                .add(Items.TRAPPED_CHEST)
                .add(Items.CHEST)
                .add(Items.TRAPPED_CHEST)
                .addOptional(Tags.Items.CHESTS.location());
        this.tag(ModTags.SHEARS)
                .add(Items.SHEARS)
                .addOptional(Tags.Items.SHEARS.location());
        this.tag(ModTags.COBBLESTONE)
                .add(Items.COBBLESTONE)
                .add(Items.INFESTED_COBBLESTONE)
                .add(Items.MOSSY_COBBLESTONE)
                .add(Items.COBBLED_DEEPSLATE)
                .addOptional(Tags.Items.COBBLESTONE.location());
        this.tag(ModTags.WOOD_ROD)
                .add(Items.STICK)
                .addOptional(Tags.Items.RODS_WOODEN.location());
        this.tag(ModTags.SEEDS)
                .add(Items.WHEAT_SEEDS)
                .add(Items.BEETROOT_SEEDS)
                .add(Items.MELON_SEEDS)
                .add(Items.PUMPKIN_SEEDS)
                .addTag(Tags.Items.SEEDS);
        this.tag(ModTags.CROPS)
                .add(Items.BEETROOT)
                .add(Items.CARROT)
                .add(Items.NETHER_WART)
                .add(Items.POTATO)
                .add(Items.WHEAT);

        this.forgeAndCommonTag(Tags.Items.SLIMEBALLS, ModTags.SLIME, ModItems.GLUE.get());
        this.tag(ModTags.SLIME)
                .add(Items.SLIME_BALL);
        this.forgeAndCommonTag(ModTags.BRONZE_F, ModTags.BRONZE, ModItems.BRONZE.get());
        this.forgeAndCommonTag(ModTags.SILVER_F, ModTags.SILVER, ModItems.SILVER.get());
        this.forgeAndCommonTag(ModTags.PLATINUM_F, ModTags.PLATINUM, ModItems.PLATINUM.get());

        this.forgeAndCommonTag(ModTags.AMETHYST_F, ModTags.AMETHYSTS, ModItems.AMETHYST.get());
        this.tag(ModTags.AMETHYSTS)
                .add(Items.AMETHYST_SHARD);
        this.forgeAndCommonTag(ModTags.AQUAMARINE_F, ModTags.AQUAMARINES, ModItems.AQUAMARINE.get());
        this.forgeAndCommonTag(ModTags.RUBY_F, ModTags.RUBIES, ModItems.RUBY.get());
        this.forgeAndCommonTag(ModTags.SAPPHIRE_F, ModTags.SAPPHIRES, ModItems.SAPPHIRE.get());

        this.tag(ModTags.ORICHALCUM)
                .add(ModItems.ORICHALCUM.get());
        this.tag(ModTags.DRAGONIC)
                .add(ModItems.DRAGONIC.get());

        this.tag(ModTags.GENERIC_TRASH)
                .add(ModItems.FAILED_DISH.get(), ModItems.DISASTROUS_DISH.get(),
                        ModItems.WEEDS.get(), ModItems.WITHERED_GRASS.get())
                .addTag(ItemTags.DIRT)
                .addTag(ModTags.COBBLESTONE);
        this.tag(ModTags.SMITH_TRASH)
                .add(ModItems.SCRAP.get(), ModItems.SCRAP_PLUS.get())
                .addTag(ModTags.GENERIC_TRASH);

        this.tag(ModTags.MINERALS)
                .add(ModItems.SCRAP_PLUS.get())
                .addTag(ModTags.IRON)
                .addTag(ModTags.GOLD)
                .addTag(ModTags.COPPER)
                .addTag(ModTags.BRONZE)
                .addTag(ModTags.SILVER)
                .addTag(ModTags.PLATINUM)
                .addTag(ModTags.ORICHALCUM)
                .addTag(ModTags.DRAGONIC);
        this.forgeAndCommonTag(Tags.Items.GEMS, ModTags.JEWELS,
                ModItems.AMETHYST.get(),
                ModItems.AQUAMARINE.get(),
                ModItems.RUBY.get(),
                ModItems.SAPPHIRE.get(),
                ModItems.CORE_RED.get(),
                ModItems.CORE_BLUE.get(),
                ModItems.CORE_YELLOW.get(),
                ModItems.CORE_GREEN.get(),
                ModItems.CRYSTAL_SKULL.get());
        this.tag(ModTags.JEWELS).add(Items.EMERALD, Items.DIAMOND);
        this.tag(ModTags.CRYSTALS)
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
        this.tag(ModTags.STICKS)
                .addOptionalTag(Tags.Items.RODS_WOODEN.location())
                .add(Items.STICK,
                        ModItems.STICK_THICK.get(),
                        ModItems.HORN_INSECT.get(),
                        ModItems.HORN_RIGID.get(),
                        ModItems.HORN_DEVIL.get(),
                        ModItems.PLANT_STEM.get(),
                        ModItems.HORN_BULL.get(),
                        ModItems.MOVING_BRANCH.get());
        this.tag(ModTags.LIQUIDS)
                .add(ModItems.GLUE.get(),
                        ModItems.DEVIL_BLOOD.get(),
                        ModItems.PARA_POISON.get(),
                        ModItems.POISON_KING.get());
        this.tag(ModTags.FEATHERS)
                .add(Items.FEATHER,
                        ModItems.FEATHER_BLACK.get(),
                        ModItems.FEATHER_THUNDER.get(),
                        ModItems.FEATHER_YELLOW.get(),
                        ModItems.DRAGON_FIN.get())
                .addOptionalTag(Tags.Items.FEATHERS.location());
        this.tag(ModTags.SHELLS_BONES)
                .add(ModItems.TURTLE_SHELL.get(),
                        ModItems.FISH_FOSSIL.get(),
                        ModItems.SKULL.get(),
                        ModItems.DRAGON_BONES.get(),
                        ModItems.TORTOISE_SHELL.get());
        this.tag(ModTags.STONES)
                .add(ModItems.ROCK.get(),
                        ModItems.STONE_ROUND.get(),
                        ModItems.STONE_TINY.get(),
                        ModItems.STONE_GOLEM.get(),
                        ModItems.TABLET_GOLEM.get(),
                        ModItems.STONE_SPIRIT.get(),
                        ModItems.TABLET_TRUTH.get());
        this.tag(ModTags.STRINGS)
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
        this.tag(ModTags.SHARDS)
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
        this.tag(ModTags.FURS)
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
        this.tag(ModTags.POWDERS)
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
        this.tag(ModTags.CLOTHS)
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
        this.tag(ModTags.CLAWS_FANGS)
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
        this.tag(ModTags.SCALES)
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

        this.tag(ModTags.TURNIP)
                .add(ModItems.TURNIP.get(), ModItems.TURNIP_GIANT.get(), ModItems.TURNIP_PINK.get(), ModItems.TURNIP_PINK_GIANT.get(),
                        ModItems.GOLDEN_TURNIP.get(), ModItems.GOLDEN_TURNIP_GIANT.get());

        this.tag(ModTags.HIGH_TIER_TOOLS)
                .add(ModItems.HOE_PLATINUM.get())
                .add(ModItems.WATERING_CAN_PLATINUM.get())
                .add(ModItems.SICKLE_PLATINUM.get())
                .add(ModItems.HAMMER_PLATINUM.get())
                .add(ModItems.AXE_PLATINUM.get())
                .add(ModItems.FISHING_ROD_PLATINUM.get());

        this.tag(ModTags.UPGRADABLE_HELD).addTag(ModTags.TOOLS).addTag(ModTags.WEAPONS);
        this.tag(ModTags.TOOLS).addTag(ModTags.HOES).addTag(ModTags.WATERINGCANS)
                .addTag(ModTags.SICKLES).addTag(ModTags.HAMMER_TOOLS)
                .addTag(ModTags.AXE_TOOLS).addTag(ModTags.FISHING_RODS);

        this.tag(tempKeyFabric("hoes")).addTag(tempKeyForge("tools/hoes"));
        this.tag(tempKeyForge("tools/hoes")).addTag(ModTags.HOES);
        this.tag(tempKeyFabric("fishing_rods")).addTag(tempKeyForge("tools/fishing_rods"));
        this.tag(tempKeyForge("tools/fishing_rods")).addTag(ModTags.FISHING_RODS);

        this.tag(ModTags.WEAPONS).addTag(ModTags.SHORTSWORDS).addTag(ModTags.LONGSWORDS)
                .addTag(ModTags.SPEARS).addTag(ModTags.AXES)
                .addTag(ModTags.HAMMERS).addTag(ModTags.DUALBLADES)
                .addTag(ModTags.FISTS).addTag(ModTags.STAFFS);
        this.tag(ModTags.HAMMER_AXES)
                .addTag(ModTags.HAMMERS).addTag(ModTags.AXES);

        this.tag(tempKeyFabric("swords")).addTag(tempKeyForge("tools/swords"));
        this.tag(tempKeyForge("tools/swords"))
                .addTag(ModTags.SHORTSWORDS).addTag(ModTags.LONGSWORDS).addTag(ModTags.DUALBLADES);

        this.tag(ModTags.EQUIPMENT).addTag(ModTags.HELMET).addTag(ModTags.CHESTPLATE)
                .addTag(ModTags.ACCESSORIES).addTag(ModTags.BOOTS)
                .addTag(ModTags.SHIELDS);

        this.tag(tempKeyFabric("helmets")).addTag(tempKeyForge("armors/helmets"));
        this.tag(tempKeyForge("armors/helmets")).addTag(ModTags.HELMET);
        this.tag(tempKeyFabric("chestplates")).addTag(tempKeyForge("armors/chestplates"));
        this.tag(tempKeyForge("armors/chestplates")).addTag(ModTags.CHESTPLATE);
        this.tag(tempKeyFabric("boots")).addTag(tempKeyForge("armors/boots"));
        this.tag(tempKeyForge("armors/boots")).addTag(ModTags.BOOTS);
        this.tag(tempKeyFabric("shields")).addTag(tempKeyForge("tools/shields"));
        this.tag(tempKeyForge("tools/shields")).addTag(ModTags.SHIELDS);

        this.forgeAndCommonTag(Tags.Items.EGGS, ModTags.EGGS, ModItems.EGG_S.get(),
                ModItems.EGG_M.get(), ModItems.EGG_L.get(), Items.EGG);
        this.tag(ModTags.MILKS)
                .add(ModItems.MILK_S.get())
                .add(ModItems.MILK_M.get())
                .add(ModItems.MILK_L.get())
                .add(Items.MILK_BUCKET);

        this.tag(ModTags.tamingTag(ModEntities.WOOLY.get()))
                .addTag(ModTags.SHEARS)
                .addTag(ItemTags.WOOL)
                .add(Items.WHEAT);
        this.tag(ModTags.tamingTag(ModEntities.ORC.get()))
                .add(ModItems.CHEAP_BRACELET.get())
                .add(ModItems.CLOTH_CHEAP.get())
                .add(ModItems.OLD_BANDAGE.get());
        this.tag(ModTags.tamingTag(ModEntities.ORC_ARCHER.get()))
                .add(Items.GUNPOWDER)
                .add(Items.ARROW)
                .add(ModItems.ARROW_HEAD.get())
                .add(ModItems.RECOVERY_POTION.get());
        this.tag(ModTags.tamingTag(ModEntities.ANT.get()))
                .add(ModItems.CARAPACE_INSECT.get())
                .add(ModItems.CARAPACE_PRETTY.get())
                .add(ModItems.JAW_INSECT.get())
                .add(ModItems.JAW_QUEEN.get());
        this.tag(ModTags.tamingTag(ModEntities.BEETLE.get()))
                .add(ModItems.CARAPACE_INSECT.get())
                .add(ModItems.CARAPACE_PRETTY.get())
                .add(ModItems.HORN_INSECT.get())
                .add(ModItems.HORN_RIGID.get());
        this.tag(ModTags.tamingTag(ModEntities.BIG_MUCK.get()))
                .add(Items.BROWN_MUSHROOM)
                .add(Items.RED_MUSHROOM)
                .add(ModItems.MUSHROOM.get())
                .add(ModItems.MONARCH_MUSHROOM.get())
                .add(ModItems.SPORE.get());
        this.tag(ModTags.tamingTag(ModEntities.BUFFAMOO.get()))
                .addTag(ModTags.MILKS)
                .add(Items.WHEAT);
        this.tag(ModTags.tamingTag(ModEntities.CHIPSQUEEK.get()))
                .addTag(ModTags.FURS);
        this.tag(ModTags.tamingTag(ModEntities.CLUCKADOODLE.get()))
                .addTag(ModTags.EGGS)
                .addTag(ModTags.SEEDS);
        this.tag(ModTags.tamingTag(ModEntities.POMME_POMME.get()))
                .add(Items.APPLE);
        this.tag(ModTags.tamingTag(ModEntities.TORTAS.get()))
                .add(Items.SEAGRASS)
                .add(ModItems.TORTOISE_SHELL.get());
        this.tag(ModTags.tamingTag(ModEntities.SKY_FISH.get()))
                .add(Items.KELP);
        this.tag(ModTags.tamingTag(ModEntities.WEAGLE.get()))
                .addTag(ModTags.FEATHERS);
        this.tag(ModTags.tamingTag(ModEntities.GOBLIN.get()))
                .add(ModItems.OLD_BANDAGE.get());
        this.tag(ModTags.tamingTag(ModEntities.GOBLIN_ARCHER.get()))
                .addTag(ModTags.tamingTag(ModEntities.GOBLIN.get()));
        this.tag(ModTags.tamingTag(ModEntities.DUCK.get()))
                .add(ModItems.FUR.get())
                .add(Items.FEATHER);
        this.tag(ModTags.tamingTag(ModEntities.FAIRY.get()))
                .add(ModItems.FAIRY_DUST.get());
        this.tag(ModTags.tamingTag(ModEntities.GHOST.get()))
                .add(ModItems.GHOST_HOOD.get());
        this.tag(ModTags.tamingTag(ModEntities.SPIRIT.get()))
                .add(ModItems.CRYSTAL_DARK.get());
        this.tag(ModTags.tamingTag(ModEntities.GHOST_RAY.get()))
                .add(ModItems.GHOST_HOOD.get());
        this.tag(ModTags.tamingTag(ModEntities.SPIDER.get()))
                .add(Items.STRING)
                .add(ModItems.THREAD_SPIDER.get());
        this.tag(ModTags.tamingTag(ModEntities.SHADOW_PANTHER.get()))
                .add(ModItems.FUR.get())
                .add(ModItems.CLAW_PANTHER.get());
        this.tag(ModTags.tamingTag(ModEntities.MONSTER_BOX.get()))
                .add(ModItems.FAILED_DISH.get())
                .add(ModItems.DISASTROUS_DISH.get());
        this.tag(ModTags.tamingTag(ModEntities.GOBBLE_BOX.get()))
                .add(ModItems.FAILED_DISH.get())
                .add(ModItems.DISASTROUS_DISH.get());
        this.tag(ModTags.tamingTag(ModEntities.KILLER_ANT.get()))
                .add(ModItems.CARAPACE_PRETTY.get());
        this.tag(ModTags.tamingTag(ModEntities.HIGH_ORC.get()))
                .addTag(ModTags.tamingTag(ModEntities.ORC.get()));
        this.tag(ModTags.tamingTag(ModEntities.ORC_HUNTER.get()))
                .addTag(ModTags.tamingTag(ModEntities.ORC_ARCHER.get()));
        this.tag(ModTags.tamingTag(ModEntities.HORNET.get()))
                .add(Items.HONEY_BOTTLE)
                .add(Items.HONEYCOMB);
        this.tag(ModTags.tamingTag(ModEntities.SILVER_WOLF.get()))
                .add(Items.BONE)
                .add(ModItems.FANG_WOLF.get());
        this.tag(ModTags.tamingTag(ModEntities.LEAF_BALL.get()))
                .addTag(ItemTags.LEAVES);
        this.tag(ModTags.tamingTag(ModEntities.FURPY.get()))
                .addTag(ModTags.tamingTag(ModEntities.CHIPSQUEEK.get()));
        this.tag(ModTags.tamingTag(ModEntities.PALM_CAT.get()))
                .add(ModItems.FUR.get())
                .add(Items.CAKE)
                .add(ModItems.CAKE.get());
        this.tag(ModTags.tamingTag(ModEntities.MINO.get()))
                .add(ModItems.GRAPES.get());
        this.tag(ModTags.tamingTag(ModEntities.TRICKY_MUCK.get()))
                .add(ModItems.POWDER_POISON.get());
        this.tag(ModTags.tamingTag(ModEntities.FLOWER_LILY.get()))
                .addTag(ItemTags.FLOWERS);
        this.tag(ModTags.tamingTag(ModEntities.KING_WOOLY.get()))
                .addTag(ModTags.tamingTag(ModEntities.WOOLY.get()));
        this.tag(ModTags.tamingTag(ModEntities.BUFFALOO.get()))
                .add(ModItems.HORN_BULL.get());
        this.tag(ModTags.tamingTag(ModEntities.GOBLIN_PIRATE.get()))
                .addTag(ModTags.tamingTag(ModEntities.GOBLIN.get()));
        this.tag(ModTags.tamingTag(ModEntities.GOBLIN_GANGSTER.get()))
                .addTag(ModTags.tamingTag(ModEntities.GOBLIN.get()));
        this.tag(ModTags.tamingTag(ModEntities.IGNIS.get()))
                .add(ModItems.CRYSTAL_FIRE.get())
                .add(ModItems.CRYSTAL_MAGIC.get());
        this.tag(ModTags.tamingTag(ModEntities.SCORPION.get()))
                .add(ModItems.TAIL_SCORPION.get());
        this.tag(ModTags.tamingTag(ModEntities.TROLL.get()))
                .add(ModItems.GIANTS_NAIL.get())
                .add(ModItems.GLOVE_GIANT.get())
                .add(ModItems.HAMMER_PIECE.get());
        this.tag(ModTags.tamingTag(ModEntities.FLOWER_LION.get()))
                .addTag(ModTags.tamingTag(ModEntities.FLOWER_LILY.get()));
        this.tag(ModTags.tamingTag(ModEntities.TOMATO_GHOST.get()))
                .addTag(tempKeyFabric("vegetables/tomato"));

        this.tag(ModTags.tamingTag(ModEntities.AMBROSIA.get()))
                .add(ModItems.TOYHERB_GIANT.get());
        this.tag(ModTags.tamingTag(ModEntities.THUNDERBOLT.get()))
                .add(ModItems.CARROT_GIANT.get());
        this.tag(ModTags.tamingTag(ModEntities.MARIONETTA.get()))
                .add(Items.CAKE)
                .add(ModItems.CHEESECAKE.get())
                .add(ModItems.CHOCOLATE_CAKE.get())
                .add(ModItems.APPLE_PIE.get());
        this.tag(ModTags.tamingTag(ModEntities.DEAD_TREE.get()))
                .add(ModItems.GREENIFIER_PLUS.get());
        this.tag(ModTags.tamingTag(ModEntities.CHIMERA.get()))
                .add(ModItems.ROYAL_CURRY.get())
                .add(ModItems.ULTIMATE_CURRY.get());
        this.tag(ModTags.tamingTag(ModEntities.RACCOON.get()))
                .add(ModItems.UDON.get())
                .add(ModItems.TEMPURA_UDON.get())
                .add(ModItems.CURRY_UDON.get());
        this.tag(ModTags.tamingTag(ModEntities.SKELEFANG.get()))
                .add(Items.DRAGON_HEAD);

        TagKey<Item> temp = tempKeyForge("fruits/grapes");
        this.tag(ModTags.GRAPES)
                .add(ModItems.GRAPES.get())
                .addTag(temp);
        this.tag(temp)
                .add(ModItems.GRAPES.get());

        //Note: Add items to the matching forge tags and make fabric common tag include the corresponding forge tag
        TagKey<Item> forgeParentTag = tempKeyForge("seeds");
        //this.tag(Tags.Items.SEEDS).addTag(forgeParentTag); Already done above. Just here in case of confusion
        for (RegistryEntrySupplier<Item> sup : ModItems.SEEDS) {
            TagKey<Item> forgeTag = tempKeyForge("seeds/" + sup.getID().getPath().replace("seed_", ""));
            this.tag(forgeTag).add(sup.get());
            this.tag(forgeParentTag).addTag(forgeTag);

            TagKey<Item> commonTag = tempKeyFabric("seeds/" + sup.getID().getPath().replace("seed_", ""));
            this.tag(commonTag).addTag(forgeTag);
        }

        forgeParentTag = tempKeyForge("vegetables");
        TagKey<Item> forgeCropParentTag = tempKeyForge("crops");
        this.tag(ModTags.VEGGIES).addTag(forgeParentTag);
        this.tag(ModTags.CROPS).addTag(forgeCropParentTag);
        for (RegistryEntrySupplier<Item> sup : ModItems.VEGGIES) {
            String name = sup.getID().getPath().replace("crop_", "");
            TagKey<Item> forgeTag = tempKeyForge("vegetables/" + name);
            this.tag(forgeTag).add(sup.get());
            this.tag(forgeParentTag).addTag(forgeTag);

            TagKey<Item> commonTag = tempKeyFabric("vegetables/" + name);
            this.tag(commonTag).addTag(forgeTag);

            //Also add to crops tag
            forgeTag = tempKeyForge("crops/" + name);
            this.tag(forgeTag).add(sup.get());
            this.tag(forgeCropParentTag).addTag(forgeTag);

            commonTag = tempKeyFabric("crops/" + name);
            this.tag(commonTag).addTag(forgeTag);
        }

        forgeParentTag = tempKeyForge("fruits");
        this.tag(ModTags.FRUITS).addTag(forgeParentTag);
        for (RegistryEntrySupplier<Item> sup : ModItems.FRUITS) {
            String name = sup.getID().getPath().replace("crop_", "");
            TagKey<Item> forgeTag = tempKeyForge("fruits/" + name);
            this.tag(forgeTag).add(sup.get());
            this.tag(forgeParentTag).addTag(forgeTag);

            TagKey<Item> commonTag = tempKeyFabric("fruits/" + name);
            this.tag(commonTag).addTag(forgeTag);

            //Also add to crops tag
            forgeTag = tempKeyForge("crops/" + name);
            this.tag(forgeTag).add(sup.get());
            this.tag(forgeCropParentTag).addTag(forgeTag);

            commonTag = tempKeyFabric("crops/" + name);
            this.tag(commonTag).addTag(forgeTag);
        }

        forgeParentTag = tempKeyForge("flowers");
        this.tag(ModTags.FLOWERS).addTag(forgeParentTag);
        for (RegistryEntrySupplier<Item> sup : ModItems.FLOWERS) {
            this.tag(forgeParentTag).add(sup.get());
            //Also add to crops tag
            String name = sup.getID().getPath().replace("crop_", "");
            TagKey<Item> forgeTag = tempKeyForge("crops/" + name);
            this.tag(forgeTag).add(sup.get());
            this.tag(forgeCropParentTag).addTag(forgeTag);
        }

        this.tag(ModTags.QUICKHARVEST_BYPASS)
                .add(ModItems.FORMULAR_A.get(), ModItems.FORMULAR_B.get(), ModItems.FORMULAR_C.get(), ModItems.MINIMIZER.get(),
                        ModItems.GIANTIZER.get(), ModItems.GREENIFIER.get(), ModItems.GREENIFIER_PLUS.get(), ModItems.WETTABLE_POWDER.get())
                .add(ModItems.WATERING_CAN_SCRAP.get(), ModItems.WATERING_CAN_IRON.get(),
                        ModItems.WATERING_CAN_SILVER.get(), ModItems.WATERING_CAN_GOLD.get(), ModItems.WATERING_CAN_PLATINUM.get());
    }

    protected void forgeAndCommonTag(TagKey<Item> forge, TagKey<Item> common, Item... items) {
        TagAppender<Item> a = this.tag(forge);
        for (Item item : items)
            a.add(item);
        a = this.tag(common);
        a.addTag(forge);
    }

    protected static TagKey<Item> tempKeyForge(String path) {
        return PlatformUtils.INSTANCE.itemTag(new ResourceLocation("forge", path));
    }

    protected static TagKey<Item> tempKeyFabric(String path) {
        return PlatformUtils.INSTANCE.itemTag(new ResourceLocation("c", path));
    }
}
