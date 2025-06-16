package io.github.flemmli97.runecraftory.forge.data.tags;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
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

    public ItemTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTagProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTagProvider, RuneCraftory.MODID, existingFileHelper);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        ModItems.DATAGENTAGS.forEach((key, supList) -> supList.forEach(sup -> this.tag(key).add(sup.get())));
        this.tag(RunecraftoryTags.Items.SHORTSWORDS)
                .add(ModItems.PLANT_SWORD.get());
        this.tag(RunecraftoryTags.Items.SHIELDS)
                .add(ModItems.PLANT_SHIELD.get());
        ModItems.DATAGENTAGS.keySet().stream().sorted(Comparator.comparing(TagKey::location)).forEach(key -> {
            if (key.location().getPath().startsWith("foods/")) {
                this.tag(RunecraftoryTags.Items.FOODS).addTag(key);
            }
        });

        this.tag(RunecraftoryTags.Items.SLIME)
                .add(ModItems.GLUE.get());

        this.tag(RunecraftoryTags.Items.RAW_MATERIALS_TIN)
                .add(ModItems.RAW_TIN.get());
        this.tag(RunecraftoryTags.Items.INGOTS_TIN)
                .add(ModItems.TIN_INGOT.get());
        this.tag(RunecraftoryTags.Items.DUSTS_BRONZE)
                .add(ModItems.BRONZE_DUST.get());
        this.tag(RunecraftoryTags.Items.INGOTS_BRONZE)
                .add(ModItems.BRONZE_INGOT.get());
        this.tag(RunecraftoryTags.Items.RAW_MATERIALS_SILVER)
                .add(ModItems.SILVER_INGOT.get());
        this.tag(RunecraftoryTags.Items.INGOTS_SILVER)
                .add(ModItems.SILVER_INGOT.get());
        this.tag(RunecraftoryTags.Items.RAW_MATERIALS_PLATINUM)
                .add(ModItems.RAW_PLATINUM.get());
        this.tag(RunecraftoryTags.Items.INGOTS_PLATINUM)
                .add(ModItems.PLATINUM_INGOT.get());

        this.tag(RunecraftoryTags.Items.GEMS_AMETHYST)
                .add(ModItems.AMETHYST.get());
        this.tag(RunecraftoryTags.Items.GEMS_AQUAMARINE)
                .add(ModItems.AQUAMARINE.get());
        this.tag(RunecraftoryTags.Items.GEMS_RUBY)
                .add(ModItems.RUBY.get());
        this.tag(RunecraftoryTags.Items.GEMS_SAPPHIRE)
                .add(ModItems.SAPPHIRE.get());

        this.tag(RunecraftoryTags.Items.ORICHALCUM)
                .add(ModItems.ORICHALCUM.get());
        this.tag(RunecraftoryTags.Items.DRAGONIC)
                .add(ModItems.DRAGONIC.get());

        this.tag(RunecraftoryTags.Items.GENERIC_TRASH)
                .add(ModItems.FAILED_DISH.get(), ModItems.DISASTROUS_DISH.get(),
                        ModItems.WEEDS.get(), ModItems.WITHERED_GRASS.get())
                .addTag(ItemTags.DIRT)
                .addTag(RunecraftoryTags.Items.COBBLESTONE);

        this.tag(RunecraftoryTags.Items.MINERALS)
                .add(ModItems.SCRAP_PLUS.get())
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
        this.tag(Tags.Items.GEMS).addTag(RunecraftoryTags.Items.JEWELS);
        this.tag(RunecraftoryTags.Items.CRYSTALS)
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
        this.tag(RunecraftoryTags.Items.STICKS)
                .add(Items.STICK,
                        ModItems.STICK_THICK.get(),
                        ModItems.HORN_INSECT.get(),
                        ModItems.HORN_RIGID.get(),
                        ModItems.HORN_DEVIL.get(),
                        ModItems.PLANT_STEM.get(),
                        ModItems.HORN_BULL.get(),
                        ModItems.MOVING_BRANCH.get())
                .addOptionalTag(Tags.Items.RODS_WOODEN.location());
        this.tag(RunecraftoryTags.Items.LIQUIDS)
                .add(ModItems.GLUE.get(),
                        ModItems.DEVIL_BLOOD.get(),
                        ModItems.PARA_POISON.get(),
                        ModItems.POISON_KING.get());
        this.tag(RunecraftoryTags.Items.FEATHERS)
                .add(Items.FEATHER,
                        ModItems.FEATHER_BLACK.get(),
                        ModItems.FEATHER_THUNDER.get(),
                        ModItems.FEATHER_YELLOW.get(),
                        ModItems.DRAGON_FIN.get())
                .addOptionalTag(Tags.Items.FEATHERS.location());
        this.tag(RunecraftoryTags.Items.SHELLS_BONES)
                .add(ModItems.TURTLE_SHELL.get(),
                        ModItems.FISH_FOSSIL.get(),
                        ModItems.SKULL.get(),
                        ModItems.DRAGON_BONES.get(),
                        ModItems.TORTOISE_SHELL.get(),
                        ModItems.AMMONITE.get());
        this.tag(RunecraftoryTags.Items.STONES)
                .add(ModItems.ROCK.get(),
                        ModItems.STONE_ROUND.get(),
                        ModItems.STONE_TINY.get(),
                        ModItems.STONE_GOLEM.get(),
                        ModItems.TABLET_GOLEM.get(),
                        ModItems.STONE_SPIRIT.get(),
                        ModItems.TABLET_TRUTH.get());
        this.tag(RunecraftoryTags.Items.STRINGS)
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
        this.tag(RunecraftoryTags.Items.SHARDS)
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
        this.tag(RunecraftoryTags.Items.FURS)
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
        this.tag(RunecraftoryTags.Items.POWDERS)
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
        this.tag(RunecraftoryTags.Items.CLOTHS)
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
        this.tag(RunecraftoryTags.Items.CLAWS_FANGS)
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
        this.tag(RunecraftoryTags.Items.SCALES)
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

        this.tag(RunecraftoryTags.Items.HIGH_TIER_TOOLS)
                .add(ModItems.HOE_PLATINUM.get())
                .add(ModItems.WATERING_CAN_PLATINUM.get())
                .add(ModItems.SICKLE_PLATINUM.get())
                .add(ModItems.HAMMER_PLATINUM.get())
                .add(ModItems.AXE_PLATINUM.get())
                .add(ModItems.FISHING_ROD_PLATINUM.get());

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
                .add(ModItems.EGG_S.get())
                .add(ModItems.EGG_M.get())
                .add(ModItems.EGG_L.get());

        this.tag(RunecraftoryTags.Items.MILKS)
                .add(ModItems.MILK_S.get())
                .add(ModItems.MILK_M.get())
                .add(ModItems.MILK_L.get())
                .addTag(Tags.Items.BUCKETS_MILK);

        // Taming tags
        this.tag(RunecraftoryTags.tamingTag(ModEntities.WOOLY.get()))
                .addTag(RunecraftoryTags.Items.SHEARS)
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
                .addTag(RunecraftoryTags.Items.MILKS)
                .add(Items.WHEAT);
        this.tag(RunecraftoryTags.tamingTag(ModEntities.CHIPSQUEEK.get()))
                .addTag(RunecraftoryTags.Items.FURS);
        this.tag(RunecraftoryTags.tamingTag(ModEntities.CLUCKADOODLE.get()))
                .addTag(RunecraftoryTags.Items.EGGS)
                .addTag(RunecraftoryTags.Items.SEEDS);
        this.tag(RunecraftoryTags.tamingTag(ModEntities.POMME_POMME.get()))
                .add(Items.APPLE);
        this.tag(RunecraftoryTags.tamingTag(ModEntities.TORTAS.get()))
                .add(Items.SEAGRASS)
                .add(ModItems.TORTOISE_SHELL.get());
        this.tag(RunecraftoryTags.tamingTag(ModEntities.SKY_FISH.get()))
                .add(Items.KELP);
        this.tag(RunecraftoryTags.tamingTag(ModEntities.WEAGLE.get()))
                .addTag(RunecraftoryTags.Items.FEATHERS);
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
                .addTag(tempKey("vegetables/tomato"));
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
                .addTag(RunecraftoryTags.Items.AXES);
        this.tag(RunecraftoryTags.tamingTag(ModEntities.MINOTAUR_KING.get()))
                .add(ModItems.HAMMER_PIECE.get())
                .addTag(RunecraftoryTags.Items.AXES);

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
        this.tag(RunecraftoryTags.Items.TURNIP)
                .add(ModItems.TURNIP.get())
                .add(ModItems.TURNIP_GIANT.get())
                .add(ModItems.TURNIP_PINK.get())
                .add(ModItems.TURNIP_PINK_GIANT.get())
                .add(ModItems.GOLDEN_TURNIP.get())
                .add(ModItems.GOLDEN_TURNIP_GIANT.get());
        this.tag(RunecraftoryTags.Items.ORANGE)
                .add(ModItems.ORANGE.get());
        this.tag(RunecraftoryTags.Items.GRAPES)
                .add(ModItems.GRAPES.get());

        for (RegistryEntrySupplier<Item, ?> sup : ModItems.SEEDS) {
            TagKey<Item> seedTag = tempKey(RunecraftoryTags.Items.SEEDS.location().getPath() + "/" + sup.getID().getPath().replace("seed_", ""));
            this.tag(seedTag).add(sup.get());
            this.tag(RunecraftoryTags.Items.SEEDS).addTag(seedTag);
        }

        for (Pair<String, RegistryEntrySupplier<Item, ?>> sup : ModItems.VEGGIES) {
            String name = sup.getFirst();
            TagKey<Item> veggTag = tempKey(RunecraftoryTags.Items.FOODS_VEGGETABLE.location().getPath() + "/" + name);
            this.tag(veggTag).add(sup.getSecond().get());
            this.tag(RunecraftoryTags.Items.FOODS_VEGGETABLE).addTag(veggTag);

            if (sup.getSecond() != ModItems.TURNIP && sup.getSecond() != ModItems.TURNIP_GIANT) { // Normal turnip handled already before
                TagKey<Item> cropsTag = tempKey(RunecraftoryTags.Items.CROPS.location().getPath() + "/" + name);
                this.tag(cropsTag).add(sup.getSecond().get());
                this.tag(RunecraftoryTags.Items.CROPS).addTag(cropsTag);
            }
        }

        for (Pair<String, RegistryEntrySupplier<Item, ?>> sup : ModItems.FRUITS) {
            String name = sup.getFirst();
            TagKey<Item> veggTag = tempKey(RunecraftoryTags.Items.FOODS_FRUIT.location().getPath() + "/" + name);
            this.tag(veggTag).add(sup.getSecond().get());
            this.tag(RunecraftoryTags.Items.FOODS_FRUIT).addTag(veggTag);

            TagKey<Item> cropsTag = tempKey(RunecraftoryTags.Items.CROPS.location().getPath() + "/" + name);
            this.tag(cropsTag).add(sup.getSecond().get());
            this.tag(RunecraftoryTags.Items.CROPS).addTag(cropsTag);
        }

        for (Pair<String, RegistryEntrySupplier<Item, ?>> sup : ModItems.FLOWERS) {
            String name = sup.getFirst();
            TagKey<Item> veggTag = tempKey(RunecraftoryTags.Items.FLOWERS.location().getPath() + "/" + name);
            this.tag(veggTag).add(sup.getSecond().get());
            this.tag(RunecraftoryTags.Items.FLOWERS).addTag(veggTag);

            TagKey<Item> cropsTag = tempKey(RunecraftoryTags.Items.CROPS.location().getPath() + "/" + name);
            this.tag(cropsTag).add(sup.getSecond().get());
            this.tag(RunecraftoryTags.Items.CROPS).addTag(cropsTag);
        }

        this.tag(RunecraftoryTags.Items.QUICKHARVEST_BYPASS)
                .add(ModItems.FORMULAR_A.get(), ModItems.FORMULAR_B.get(), ModItems.FORMULAR_C.get(), ModItems.MINIMIZER.get(),
                        ModItems.GIANTIZER.get(), ModItems.GREENIFIER.get(), ModItems.GREENIFIER_PLUS.get(), ModItems.WETTABLE_POWDER.get())
                .add(ModItems.WATERING_CAN_SCRAP.get(), ModItems.WATERING_CAN_IRON.get(),
                        ModItems.WATERING_CAN_SILVER.get(), ModItems.WATERING_CAN_GOLD.get(), ModItems.WATERING_CAN_PLATINUM.get());
    }

    protected static TagKey<Item> tempKey(String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", path));
    }

}
