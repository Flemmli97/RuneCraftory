package io.github.flemmli97.runecraftory.neoforge.data.tags;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Comparator;
import java.util.concurrent.CompletableFuture;

public class ItemTagGen extends ItemTagsProvider {

    public static final TagKey<Item> HIDDEN_FROM_RECIPE_VIEWERS = TagKey.create(Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath("c", "hidden_from_recipe_viewers"));

    public ItemTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTagProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTagProvider, RuneCraftory.MODID, existingFileHelper);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        IntrinsicTagAppender<Item> hidden = this.tag(HIDDEN_FROM_RECIPE_VIEWERS);
        RuneCraftoryItems.NOTEX.forEach(sup -> hidden.add(sup.get()));
        RuneCraftoryItems.DATAGENTAGS.forEach((key, supList) -> supList.forEach(sup -> this.tag(key).add(sup.get())));
        this.tag(RunecraftoryTags.Items.SHORTSWORDS)
                .add(RuneCraftoryItems.PLANT_SWORD.get());
        this.tag(RunecraftoryTags.Items.SHIELDS)
                .add(RuneCraftoryItems.PLANT_SHIELD.get());
        RuneCraftoryItems.DATAGENTAGS.keySet().stream().sorted(Comparator.comparing(TagKey::location)).forEach(key -> {
            if (key.location().getPath().startsWith("foods/")) {
                this.tag(RunecraftoryTags.Items.FOODS).addTag(key);
            }
        });

        this.tag(RunecraftoryTags.Items.ONE_TIME_UPGRADE)
                .add(RuneCraftoryItems.RACCOON_LEAF.get())
                .add(RuneCraftoryItems.GLITTA_AUGITE.get());

        this.tag(RunecraftoryTags.Items.SLIME)
                .add(RuneCraftoryItems.GLUE.get());

        this.tag(RunecraftoryTags.Items.RAW_MATERIALS_TIN)
                .add(RuneCraftoryItems.RAW_TIN.get());
        this.tag(RunecraftoryTags.Items.INGOTS_TIN)
                .add(RuneCraftoryItems.TIN_INGOT.get());
        this.tag(RunecraftoryTags.Items.DUSTS_BRONZE)
                .add(RuneCraftoryItems.BRONZE_DUST.get());
        this.tag(RunecraftoryTags.Items.INGOTS_BRONZE)
                .add(RuneCraftoryItems.BRONZE_INGOT.get());
        this.tag(RunecraftoryTags.Items.RAW_MATERIALS_SILVER)
                .add(RuneCraftoryItems.SILVER_INGOT.get());
        this.tag(RunecraftoryTags.Items.INGOTS_SILVER)
                .add(RuneCraftoryItems.SILVER_INGOT.get());
        this.tag(RunecraftoryTags.Items.RAW_MATERIALS_PLATINUM)
                .add(RuneCraftoryItems.RAW_PLATINUM.get());
        this.tag(RunecraftoryTags.Items.INGOTS_PLATINUM)
                .add(RuneCraftoryItems.PLATINUM_INGOT.get());

        this.tag(RunecraftoryTags.Items.GEMS_AMETHYST)
                .add(RuneCraftoryItems.AMETHYST.get());
        this.tag(RunecraftoryTags.Items.GEMS_AQUAMARINE)
                .add(RuneCraftoryItems.AQUAMARINE.get());
        this.tag(RunecraftoryTags.Items.GEMS_RUBY)
                .add(RuneCraftoryItems.RUBY.get());
        this.tag(RunecraftoryTags.Items.GEMS_SAPPHIRE)
                .add(RuneCraftoryItems.SAPPHIRE.get());

        this.tag(RunecraftoryTags.Items.ORICHALCUM)
                .add(RuneCraftoryItems.ORICHALCUM.get());
        this.tag(RunecraftoryTags.Items.DRAGONIC)
                .add(RuneCraftoryItems.DRAGONIC.get());

        this.tag(RunecraftoryTags.Items.GENERIC_TRASH)
                .add(RuneCraftoryItems.FAILED_DISH.get(), RuneCraftoryItems.DISASTROUS_DISH.get(),
                        RuneCraftoryItems.WEEDS.get(), RuneCraftoryItems.WITHERED_GRASS.get())
                .addTag(ItemTags.DIRT)
                .addTag(RunecraftoryTags.Items.COBBLESTONE);

        this.tag(RunecraftoryTags.Items.MINERALS)
                .add(RuneCraftoryItems.SCRAP_PLUS.get())
                .addTag(RunecraftoryTags.Items.IRON)
                .addTag(RunecraftoryTags.Items.GOLD)
                .addTag(RunecraftoryTags.Items.COPPER)
                .addTag(RunecraftoryTags.Items.COPPER)
                .addTag(RunecraftoryTags.Items.DUSTS_BRONZE)
                .addTag(RunecraftoryTags.Items.RAW_MATERIALS_SILVER)
                .addTag(RunecraftoryTags.Items.RAW_MATERIALS_PLATINUM)
                .addTag(RunecraftoryTags.Items.ORICHALCUM)
                .addTag(RunecraftoryTags.Items.DRAGONIC);
        this.tag(RunecraftoryTags.Items.JEWELS)
                .add(RuneCraftoryItems.AMETHYST.get())
                .add(RuneCraftoryItems.AQUAMARINE.get())
                .add(RuneCraftoryItems.RUBY.get())
                .add(RuneCraftoryItems.SAPPHIRE.get())
                .add(RuneCraftoryItems.CORE_RED.get())
                .add(RuneCraftoryItems.CORE_BLUE.get())
                .add(RuneCraftoryItems.CORE_YELLOW.get())
                .add(RuneCraftoryItems.CORE_GREEN.get())
                .add(RuneCraftoryItems.CRYSTAL_SKULL.get())
                .add(Items.EMERALD)
                .add(Items.DIAMOND);
        this.tag(Tags.Items.GEMS).addTag(RunecraftoryTags.Items.JEWELS);
        this.tag(RunecraftoryTags.Items.CRYSTALS)
                .add(RuneCraftoryItems.CRYSTAL_WATER.get())
                .add(RuneCraftoryItems.CRYSTAL_EARTH.get())
                .add(RuneCraftoryItems.CRYSTAL_FIRE.get())
                .add(RuneCraftoryItems.CRYSTAL_WIND.get())
                .add(RuneCraftoryItems.CRYSTAL_LIGHT.get())
                .add(RuneCraftoryItems.CRYSTAL_DARK.get())
                .add(RuneCraftoryItems.CRYSTAL_LOVE.get())
                .add(RuneCraftoryItems.CRYSTAL_SMALL.get())
                .add(RuneCraftoryItems.CRYSTAL_BIG.get())
                .add(RuneCraftoryItems.CRYSTAL_MAGIC.get())
                .add(RuneCraftoryItems.CRYSTAL_RUNE.get())
                .add(RuneCraftoryItems.CRYSTAL_ELECTRO.get());
        this.tag(RunecraftoryTags.Items.STICKS)
                .add(Items.STICK,
                        RuneCraftoryItems.STICK_THICK.get(),
                        RuneCraftoryItems.HORN_INSECT.get(),
                        RuneCraftoryItems.HORN_RIGID.get(),
                        RuneCraftoryItems.HORN_DEVIL.get(),
                        RuneCraftoryItems.PLANT_STEM.get(),
                        RuneCraftoryItems.HORN_BULL.get(),
                        RuneCraftoryItems.MOVING_BRANCH.get())
                .addOptionalTag(Tags.Items.RODS_WOODEN.location());
        this.tag(RunecraftoryTags.Items.LIQUIDS)
                .add(RuneCraftoryItems.GLUE.get(),
                        RuneCraftoryItems.DEVIL_BLOOD.get(),
                        RuneCraftoryItems.PARA_POISON.get(),
                        RuneCraftoryItems.POISON_KING.get());
        this.tag(RunecraftoryTags.Items.FEATHERS)
                .add(Items.FEATHER,
                        RuneCraftoryItems.FEATHER_BLACK.get(),
                        RuneCraftoryItems.FEATHER_THUNDER.get(),
                        RuneCraftoryItems.FEATHER_YELLOW.get(),
                        RuneCraftoryItems.DRAGON_FIN.get())
                .addOptionalTag(Tags.Items.FEATHERS.location());
        this.tag(RunecraftoryTags.Items.SHELLS_BONES)
                .add(RuneCraftoryItems.TURTLE_SHELL.get(),
                        RuneCraftoryItems.FISH_FOSSIL.get(),
                        RuneCraftoryItems.SKULL.get(),
                        RuneCraftoryItems.DRAGON_BONES.get(),
                        RuneCraftoryItems.TORTOISE_SHELL.get(),
                        RuneCraftoryItems.AMMONITE.get());
        this.tag(RunecraftoryTags.Items.STONES)
                .add(RuneCraftoryItems.ROCK.get(),
                        RuneCraftoryItems.STONE_ROUND.get(),
                        RuneCraftoryItems.STONE_TINY.get(),
                        RuneCraftoryItems.STONE_GOLEM.get(),
                        RuneCraftoryItems.TABLET_GOLEM.get(),
                        RuneCraftoryItems.STONE_SPIRIT.get(),
                        RuneCraftoryItems.TABLET_TRUTH.get());
        this.tag(RunecraftoryTags.Items.STRINGS)
                .add(Items.STRING,
                        RuneCraftoryItems.YARN.get(),
                        RuneCraftoryItems.OLD_BANDAGE.get(),
                        RuneCraftoryItems.AMBROSIAS_THORNS.get(),
                        RuneCraftoryItems.THREAD_SPIDER.get(),
                        RuneCraftoryItems.PUPPETRY_STRINGS.get(),
                        RuneCraftoryItems.VINE.get(),
                        RuneCraftoryItems.TAIL_SCORPION.get(),
                        RuneCraftoryItems.STRONG_VINE.get(),
                        RuneCraftoryItems.THREAD_PRETTY.get(),
                        RuneCraftoryItems.TAIL_CHIMERA.get());
        this.tag(RunecraftoryTags.Items.SHARDS)
                .add(Items.FLINT,
                        RuneCraftoryItems.ARROW_HEAD.get(),
                        RuneCraftoryItems.BLADE_SHARD.get(),
                        RuneCraftoryItems.BROKEN_HILT.get(),
                        RuneCraftoryItems.BROKEN_BOX.get(),
                        RuneCraftoryItems.BLADE_GLISTENING.get(),
                        RuneCraftoryItems.GREAT_HAMMER_SHARD.get(),
                        RuneCraftoryItems.HAMMER_PIECE.get(),
                        RuneCraftoryItems.SHOULDER_PIECE.get(),
                        RuneCraftoryItems.PIRATES_ARMOR.get(),
                        RuneCraftoryItems.SCREW_RUSTY.get(),
                        RuneCraftoryItems.SCREW_SHINY.get(),
                        RuneCraftoryItems.ROCK_SHARD_LEFT.get(),
                        RuneCraftoryItems.ROCK_SHARD_RIGHT.get(),
                        RuneCraftoryItems.MTGU_PLATE.get(),
                        RuneCraftoryItems.BROKEN_ICE_WALL.get());
        this.tag(RunecraftoryTags.Items.FURS)
                .add(RuneCraftoryItems.FUR_SMALL.get(),
                        RuneCraftoryItems.FUR_MEDIUM.get(),
                        RuneCraftoryItems.FUR_LARGE.get(),
                        RuneCraftoryItems.FUR.get(),
                        RuneCraftoryItems.FURBALL.get(),
                        RuneCraftoryItems.DOWN_YELLOW.get(),
                        RuneCraftoryItems.FUR_QUALITY.get(),
                        RuneCraftoryItems.DOWN_PENGUIN.get(),
                        RuneCraftoryItems.LIGHTNING_MANE.get(),
                        RuneCraftoryItems.FUR_RED_LION.get(),
                        RuneCraftoryItems.FUR_BLUE_LION.get(),
                        RuneCraftoryItems.CHEST_HAIR.get());
        this.tag(RunecraftoryTags.Items.POWDERS)
                .add(RuneCraftoryItems.SPORE.get(),
                        RuneCraftoryItems.POWDER_POISON.get(),
                        RuneCraftoryItems.SPORE_HOLY.get(),
                        RuneCraftoryItems.FAIRY_DUST.get(),
                        RuneCraftoryItems.FAIRY_ELIXIR.get(),
                        RuneCraftoryItems.ROOT.get(),
                        RuneCraftoryItems.POWDER_MAGIC.get(),
                        RuneCraftoryItems.POWDER_MYSTERIOUS.get(),
                        RuneCraftoryItems.MAGIC.get(),
                        RuneCraftoryItems.ASH_EARTH.get(),
                        RuneCraftoryItems.ASH_FIRE.get(),
                        RuneCraftoryItems.ASH_WATER.get(),
                        RuneCraftoryItems.TURNIPS_MIRACLE.get(),
                        RuneCraftoryItems.MELODY_BOTTLE.get());
        this.tag(RunecraftoryTags.Items.CLOTHS)
                .add(Items.LEATHER,
                        RuneCraftoryItems.CLOTH_CHEAP.get(),
                        RuneCraftoryItems.CLOTH_QUALITY.get(),
                        RuneCraftoryItems.CLOTH_QUALITY_WORN.get(),
                        RuneCraftoryItems.CLOTH_SILK.get(),
                        RuneCraftoryItems.GHOST_HOOD.get(),
                        RuneCraftoryItems.GLOVE_GIANT.get(),
                        RuneCraftoryItems.GLOVE_BLUE_GIANT.get(),
                        RuneCraftoryItems.CARAPACE_INSECT.get(),
                        RuneCraftoryItems.CARAPACE_PRETTY.get(),
                        RuneCraftoryItems.CLOTH_ANCIENT_ORC.get());
        this.tag(RunecraftoryTags.Items.CLAWS_FANGS)
                .add(RuneCraftoryItems.JAW_INSECT.get(),
                        RuneCraftoryItems.CLAW_PANTHER.get(),
                        RuneCraftoryItems.CLAW_MAGIC.get(),
                        RuneCraftoryItems.FANG_WOLF.get(),
                        RuneCraftoryItems.FANG_GOLD_WOLF.get(),
                        RuneCraftoryItems.CLAW_PALM.get(),
                        RuneCraftoryItems.CLAW_MALM.get(),
                        RuneCraftoryItems.GIANTS_NAIL.get(),
                        RuneCraftoryItems.CLAW_CHIMERA.get(),
                        RuneCraftoryItems.TUSK_IVORY.get(),
                        RuneCraftoryItems.TUSK_UNBROKEN_IVORY.get(),
                        RuneCraftoryItems.SCORPION_PINCER.get(),
                        RuneCraftoryItems.DANGEROUS_SCISSORS.get(),
                        RuneCraftoryItems.PROPELLOR_CHEAP.get(),
                        RuneCraftoryItems.PROPELLOR_QUALITY.get(),
                        RuneCraftoryItems.FANG_DRAGON.get(),
                        RuneCraftoryItems.JAW_QUEEN.get(),
                        RuneCraftoryItems.FANG_DRAGON.get(),
                        RuneCraftoryItems.GIANTS_NAIL_BIG.get());
        this.tag(RunecraftoryTags.Items.SCALES)
                .add(RuneCraftoryItems.SCALE_WET.get(),
                        RuneCraftoryItems.SCALE_GRIMOIRE.get(),
                        RuneCraftoryItems.SCALE_DRAGON.get(),
                        RuneCraftoryItems.SCALE_CRIMSON.get(),
                        RuneCraftoryItems.SCALE_BLUE.get(),
                        RuneCraftoryItems.SCALE_GLITTER.get(),
                        RuneCraftoryItems.SCALE_LOVE.get(),
                        RuneCraftoryItems.SCALE_BLACK.get(),
                        RuneCraftoryItems.SCALE_FIRE.get(),
                        RuneCraftoryItems.SCALE_EARTH.get(),
                        RuneCraftoryItems.SCALE_LEGEND.get());

        this.tag(RunecraftoryTags.Items.HIGH_TIER_TOOLS)
                .add(RuneCraftoryItems.HOE_PLATINUM.get())
                .add(RuneCraftoryItems.WATERING_CAN_PLATINUM.get())
                .add(RuneCraftoryItems.SICKLE_PLATINUM.get())
                .add(RuneCraftoryItems.HAMMER_PLATINUM.get())
                .add(RuneCraftoryItems.AXE_PLATINUM.get())
                .add(RuneCraftoryItems.FISHING_ROD_PLATINUM.get());

        this.tag(RunecraftoryTags.Items.UPGRADABLE_HELD).addTag(RunecraftoryTags.Items.TOOLS).addTag(RunecraftoryTags.Items.WEAPONS);
        this.tag(RunecraftoryTags.Items.TOOLS).addTag(RunecraftoryTags.Items.HOES).addTag(RunecraftoryTags.Items.WATERINGCANS)
                .addTag(RunecraftoryTags.Items.SICKLES).addTag(RunecraftoryTags.Items.HAMMER_TOOLS)
                .addTag(RunecraftoryTags.Items.AXE_TOOLS).addTag(RunecraftoryTags.Items.FISHING_RODS);

        this.tag(ItemTags.HOES).addTag(RunecraftoryTags.Items.HOES);
        this.tag(Tags.Items.TOOLS_FISHING_ROD).addTag(RunecraftoryTags.Items.FISHING_RODS);

        this.tag(RunecraftoryTags.Items.WEAPONS).addTag(RunecraftoryTags.Items.SHORTSWORDS).addTag(RunecraftoryTags.Items.LONGSWORDS)
                .addTag(RunecraftoryTags.Items.SPEARS).addTag(RunecraftoryTags.Items.AXES)
                .addTag(RunecraftoryTags.Items.HAMMERS).addTag(RunecraftoryTags.Items.DUALBLADES)
                .addTag(RunecraftoryTags.Items.FISTS).addTag(RunecraftoryTags.Items.STAFFS);
        this.tag(RunecraftoryTags.Items.HAMMER_AXES)
                .addTag(RunecraftoryTags.Items.HAMMERS).addTag(RunecraftoryTags.Items.AXES);

        this.tag(ItemTags.SWORDS)
                .addTag(RunecraftoryTags.Items.SHORTSWORDS)
                .addTag(RunecraftoryTags.Items.LONGSWORDS)
                .addTag(RunecraftoryTags.Items.DUALBLADES);
        this.tag(Tags.Items.TOOLS_SPEAR)
                .addTag(RunecraftoryTags.Items.SPEARS);

        this.tag(RunecraftoryTags.Items.EQUIPMENT).addTag(RunecraftoryTags.Items.HELMET).addTag(RunecraftoryTags.Items.CHESTPLATE)
                .addTag(RunecraftoryTags.Items.ACCESSORIES).addTag(RunecraftoryTags.Items.BOOTS)
                .addTag(RunecraftoryTags.Items.SHIELDS);

        this.tag(Tags.Items.ARMORS)
                .addTag(RunecraftoryTags.Items.HELMET)
                .addTag(RunecraftoryTags.Items.CHESTPLATE)
                .addTag(RunecraftoryTags.Items.BOOTS)
                .addTag(RunecraftoryTags.Items.ACCESSORIES);

        this.tag(RunecraftoryTags.Items.EGGS)
                .add(RuneCraftoryItems.EGG_S.get())
                .add(RuneCraftoryItems.EGG_M.get())
                .add(RuneCraftoryItems.EGG_L.get());

        this.tag(RunecraftoryTags.Items.MILKS)
                .add(RuneCraftoryItems.MILK_S.get())
                .add(RuneCraftoryItems.MILK_M.get())
                .add(RuneCraftoryItems.MILK_L.get())
                .addTag(Tags.Items.BUCKETS_MILK);

        // Taming tags
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.WOOLY.get()))
                .addTag(RunecraftoryTags.Items.SHEARS)
                .addTag(ItemTags.WOOL)
                .add(Items.WHEAT);
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.ORC.get()))
                .add(RuneCraftoryItems.CHEAP_BRACELET.get())
                .add(RuneCraftoryItems.CLOTH_CHEAP.get())
                .add(RuneCraftoryItems.OLD_BANDAGE.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.ORC_ARCHER.get()))
                .add(Items.GUNPOWDER)
                .add(Items.ARROW)
                .add(RuneCraftoryItems.ARROW_HEAD.get())
                .add(RuneCraftoryItems.RECOVERY_POTION.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.ANT.get()))
                .add(RuneCraftoryItems.CARAPACE_INSECT.get())
                .add(RuneCraftoryItems.CARAPACE_PRETTY.get())
                .add(RuneCraftoryItems.JAW_INSECT.get())
                .add(RuneCraftoryItems.JAW_QUEEN.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.BEETLE.get()))
                .add(RuneCraftoryItems.CARAPACE_INSECT.get())
                .add(RuneCraftoryItems.CARAPACE_PRETTY.get())
                .add(RuneCraftoryItems.HORN_INSECT.get())
                .add(RuneCraftoryItems.HORN_RIGID.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.BIG_MUCK.get()))
                .add(Items.BROWN_MUSHROOM)
                .add(Items.RED_MUSHROOM)
                .add(RuneCraftoryItems.MUSHROOM.get())
                .add(RuneCraftoryItems.MONARCH_MUSHROOM.get())
                .add(RuneCraftoryItems.SPORE.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.BUFFAMOO.get()))
                .addTag(RunecraftoryTags.Items.MILKS)
                .add(Items.WHEAT);
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.CHIPSQUEEK.get()))
                .addTag(RunecraftoryTags.Items.FURS);
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.CLUCKADOODLE.get()))
                .addTag(RunecraftoryTags.Items.EGGS)
                .addTag(RunecraftoryTags.Items.SEEDS);
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.POMME_POMME.get()))
                .add(Items.APPLE);
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.TORTAS.get()))
                .add(Items.SEAGRASS)
                .add(RuneCraftoryItems.TORTOISE_SHELL.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.SKY_FISH.get()))
                .add(Items.KELP);
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.WEAGLE.get()))
                .addTag(RunecraftoryTags.Items.FEATHERS);
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.GOBLIN.get()))
                .add(RuneCraftoryItems.OLD_BANDAGE.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.GOBLIN_ARCHER.get()))
                .addTag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.GOBLIN.get()));
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.DUCK.get()))
                .add(RuneCraftoryItems.FUR.get())
                .add(Items.FEATHER);
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.FAIRY.get()))
                .add(RuneCraftoryItems.FAIRY_DUST.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.GHOST.get()))
                .add(RuneCraftoryItems.GHOST_HOOD.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.SPIRIT.get()))
                .add(RuneCraftoryItems.CRYSTAL_DARK.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.GHOST_RAY.get()))
                .add(RuneCraftoryItems.GHOST_HOOD.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.SPIDER.get()))
                .add(Items.STRING)
                .add(RuneCraftoryItems.THREAD_SPIDER.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.SHADOW_PANTHER.get()))
                .add(RuneCraftoryItems.FUR.get())
                .add(RuneCraftoryItems.CLAW_PANTHER.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.MONSTER_BOX.get()))
                .add(RuneCraftoryItems.FAILED_DISH.get())
                .add(RuneCraftoryItems.DISASTROUS_DISH.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.GOBBLE_BOX.get()))
                .add(RuneCraftoryItems.FAILED_DISH.get())
                .add(RuneCraftoryItems.DISASTROUS_DISH.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.KILLER_ANT.get()))
                .add(RuneCraftoryItems.CARAPACE_PRETTY.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.HIGH_ORC.get()))
                .addTag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.ORC.get()));
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.ORC_HUNTER.get()))
                .addTag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.ORC_ARCHER.get()));
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.HORNET.get()))
                .add(Items.HONEY_BOTTLE)
                .add(Items.HONEYCOMB);
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.SILVER_WOLF.get()))
                .add(Items.BONE)
                .add(RuneCraftoryItems.FANG_WOLF.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.LEAF_BALL.get()))
                .addTag(ItemTags.LEAVES);
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.FURPY.get()))
                .addTag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.CHIPSQUEEK.get()));
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.PALM_CAT.get()))
                .add(RuneCraftoryItems.FUR.get())
                .add(Items.CAKE)
                .add(RuneCraftoryItems.CAKE.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.MINO.get()))
                .add(RuneCraftoryItems.GRAPES.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.TRICKY_MUCK.get()))
                .add(RuneCraftoryItems.POWDER_POISON.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.FLOWER_LILY.get()))
                .addTag(ItemTags.FLOWERS);
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.KING_WOOLY.get()))
                .addTag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.WOOLY.get()));
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.BUFFALOO.get()))
                .add(RuneCraftoryItems.HORN_BULL.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.GOBLIN_PIRATE.get()))
                .addTag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.GOBLIN.get()));
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.GOBLIN_GANGSTER.get()))
                .addTag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.GOBLIN.get()));
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.IGNIS.get()))
                .add(RuneCraftoryItems.CRYSTAL_FIRE.get())
                .add(RuneCraftoryItems.CRYSTAL_MAGIC.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.SCORPION.get()))
                .add(RuneCraftoryItems.TAIL_SCORPION.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.TROLL.get()))
                .add(RuneCraftoryItems.GIANTS_NAIL.get())
                .add(RuneCraftoryItems.GLOVE_GIANT.get())
                .add(RuneCraftoryItems.HAMMER_PIECE.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.FLOWER_LION.get()))
                .addTag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.FLOWER_LILY.get()));
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.TOMATO_GHOST.get()))
                .addOptionalTag(tempKey("vegetables/tomato"));
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.GOBLIN_CAPTAIN.get()))
                .addTag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.GOBLIN.get()));
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.GOBLIN_DON.get()))
                .addTag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.GOBLIN.get()));
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.MINERAL_SQUEEK.get()))
                .addTag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.CHIPSQUEEK.get()));
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.NAPPIE.get()))
                .add(RuneCraftoryItems.PINEAPPLE.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.MALM_TIGER.get()))
                .add(RuneCraftoryItems.FUR.get())
                .add(Items.CAKE)
                .add(RuneCraftoryItems.CAKE.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.LITTLE_EMPEROR.get()))
                .add(RuneCraftoryItems.CRYSTAL_LIGHT.get())
                .add(RuneCraftoryItems.CRYSTAL_DARK.get())
                .add(RuneCraftoryItems.CRYSTAL_SMALL.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.DEMON.get()))
                .add(RuneCraftoryItems.CRYSTAL_DARK.get())
                .add(RuneCraftoryItems.CRYSTAL_BIG.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.ARCH_DEMON.get()))
                .add(RuneCraftoryItems.CRYSTAL_DARK.get())
                .add(RuneCraftoryItems.CRYSTAL_BIG.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.MINOTAUR.get()))
                .add(RuneCraftoryItems.HAMMER_PIECE.get())
                .addTag(RunecraftoryTags.Items.AXES);
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.MINOTAUR_KING.get()))
                .add(RuneCraftoryItems.HAMMER_PIECE.get())
                .addTag(RunecraftoryTags.Items.AXES);

        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.AMBROSIA.get()))
                .add(RuneCraftoryItems.TOYHERB_GIANT.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.THUNDERBOLT.get()))
                .add(RuneCraftoryItems.CARROT_GIANT.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.MARIONETTA.get()))
                .add(Items.CAKE)
                .add(RuneCraftoryItems.CHEESECAKE.get())
                .add(RuneCraftoryItems.CHOCOLATE_CAKE.get())
                .add(RuneCraftoryItems.APPLE_PIE.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.HANDONETTA.get()))
                .add(Items.CAKE)
                .add(RuneCraftoryItems.CHEESECAKE.get())
                .add(RuneCraftoryItems.CHOCOLATE_CAKE.get())
                .add(RuneCraftoryItems.APPLE_PIE.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.DEAD_TREE.get()))
                .add(RuneCraftoryItems.GREENIFIER_PLUS.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.CHIMERA.get()))
                .add(RuneCraftoryItems.ROYAL_CURRY.get())
                .add(RuneCraftoryItems.ULTIMATE_CURRY.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.RACCOON.get()))
                .add(RuneCraftoryItems.UDON.get())
                .add(RuneCraftoryItems.TEMPURA_UDON.get())
                .add(RuneCraftoryItems.CURRY_UDON.get());
        this.tag(RunecraftoryTags.tamingTag(RuneCraftoryEntities.SKELEFANG.get()))
                .add(Items.DRAGON_HEAD);

        // Crops stuff
        this.tag(RunecraftoryTags.Items.TURNIP)
                .add(RuneCraftoryItems.TURNIP.get())
                .add(RuneCraftoryItems.TURNIP_GIANT.get())
                .add(RuneCraftoryItems.TURNIP_PINK.get())
                .add(RuneCraftoryItems.TURNIP_PINK_GIANT.get())
                .add(RuneCraftoryItems.GOLDEN_TURNIP.get())
                .add(RuneCraftoryItems.GOLDEN_TURNIP_GIANT.get());
        this.tag(RunecraftoryTags.Items.ORANGE)
                .add(RuneCraftoryItems.ORANGE.get());
        this.tag(RunecraftoryTags.Items.GRAPES)
                .add(RuneCraftoryItems.GRAPES.get());

        for (RegistryEntrySupplier<Item, ?> sup : RuneCraftoryItems.SEEDS) {
            TagKey<Item> seedTag = tempKey(RunecraftoryTags.Items.SEEDS.location().getPath() + "/" + sup.getID().getPath().replace("seed_", ""));
            this.tag(seedTag).add(sup.get());
            this.tag(RunecraftoryTags.Items.SEEDS).addTag(seedTag);
        }

        for (Pair<String, RegistryEntrySupplier<Item, ?>> sup : RuneCraftoryItems.VEGGIES) {
            String name = sup.getFirst();
            TagKey<Item> veggTag = tempKey(RunecraftoryTags.Items.FOODS_VEGGETABLE.location().getPath() + "/" + name);
            this.tag(veggTag).add(sup.getSecond().get());
            this.tag(RunecraftoryTags.Items.FOODS_VEGGETABLE).addTag(veggTag);

            if (sup.getSecond() != RuneCraftoryItems.TURNIP && sup.getSecond() != RuneCraftoryItems.TURNIP_GIANT) { // Normal turnip handled already before
                TagKey<Item> cropsTag = tempKey(RunecraftoryTags.Items.CROPS.location().getPath() + "/" + name);
                this.tag(cropsTag).add(sup.getSecond().get());
                this.tag(RunecraftoryTags.Items.CROPS).addTag(cropsTag);
            }
        }

        for (Pair<String, RegistryEntrySupplier<Item, ?>> sup : RuneCraftoryItems.FRUITS) {
            String name = sup.getFirst();
            TagKey<Item> veggTag = tempKey(RunecraftoryTags.Items.FOODS_FRUIT.location().getPath() + "/" + name);
            this.tag(veggTag).add(sup.getSecond().get());
            this.tag(RunecraftoryTags.Items.FOODS_FRUIT).addTag(veggTag);

            TagKey<Item> cropsTag = tempKey(RunecraftoryTags.Items.CROPS.location().getPath() + "/" + name);
            this.tag(cropsTag).add(sup.getSecond().get());
            this.tag(RunecraftoryTags.Items.CROPS).addTag(cropsTag);
        }

        for (Pair<String, RegistryEntrySupplier<Item, ?>> sup : RuneCraftoryItems.FLOWERS) {
            String name = sup.getFirst();
            TagKey<Item> veggTag = tempKey(RunecraftoryTags.Items.FLOWERS.location().getPath() + "/" + name);
            this.tag(veggTag).add(sup.getSecond().get());
            this.tag(RunecraftoryTags.Items.FLOWERS).addTag(veggTag);

            TagKey<Item> cropsTag = tempKey(RunecraftoryTags.Items.CROPS.location().getPath() + "/" + name);
            this.tag(cropsTag).add(sup.getSecond().get());
            this.tag(RunecraftoryTags.Items.CROPS).addTag(cropsTag);
        }

        this.tag(RunecraftoryTags.Items.QUICKHARVEST_BYPASS)
                .add(RuneCraftoryItems.FORMULAR_A.get(), RuneCraftoryItems.FORMULAR_B.get(), RuneCraftoryItems.FORMULAR_C.get(), RuneCraftoryItems.MINIMIZER.get(),
                        RuneCraftoryItems.GIANTIZER.get(), RuneCraftoryItems.GREENIFIER.get(), RuneCraftoryItems.GREENIFIER_PLUS.get(), RuneCraftoryItems.WETTABLE_POWDER.get())
                .add(RuneCraftoryItems.WATERING_CAN_SCRAP.get(), RuneCraftoryItems.WATERING_CAN_IRON.get(),
                        RuneCraftoryItems.WATERING_CAN_SILVER.get(), RuneCraftoryItems.WATERING_CAN_GOLD.get(), RuneCraftoryItems.WATERING_CAN_PLATINUM.get())
                .add(RuneCraftoryItems.SICKLE_SCRAP.get(), RuneCraftoryItems.SICKLE_IRON.get(),
                        RuneCraftoryItems.SICKLE_SILVER.get(), RuneCraftoryItems.SICKLE_GOLD.get(), RuneCraftoryItems.SICKLE_PLATINUM.get());
    }

    protected static TagKey<Item> tempKey(String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", path));
    }
}
