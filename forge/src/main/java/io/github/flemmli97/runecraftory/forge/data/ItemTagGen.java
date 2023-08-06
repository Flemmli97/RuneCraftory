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
        this.tag(ModTags.CHEST_T1)
                .add(ModItems.forgingBread.get())
                .add(ModItems.armorBread.get())
                .add(ModItems.chemistryBread.get())
                .add(ModItems.cookingBread.get());
        this.tag(ModTags.CHEST_T2)
                .add(ModItems.forgingBread.get())
                .add(ModItems.armorBread.get())
                .add(ModItems.chemistryBread.get())
                .add(ModItems.cookingBread.get());

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

        this.forgeAndCommonTag(Tags.Items.SLIMEBALLS, ModTags.SLIME, ModItems.glue.get());
        this.tag(ModTags.SLIME)
                .add(Items.SLIME_BALL);
        this.forgeAndCommonTag(ModTags.BRONZE_F, ModTags.BRONZE, ModItems.bronze.get());
        this.forgeAndCommonTag(ModTags.SILVER_F, ModTags.SILVER, ModItems.silver.get());
        this.forgeAndCommonTag(ModTags.PLATINUM_F, ModTags.PLATINUM, ModItems.platinum.get());

        this.forgeAndCommonTag(ModTags.AMETHYST_F, ModTags.AMETHYSTS, ModItems.amethyst.get());
        this.tag(ModTags.AMETHYSTS)
                .add(Items.AMETHYST_SHARD);
        this.forgeAndCommonTag(ModTags.AQUAMARINE_F, ModTags.AQUAMARINES, ModItems.aquamarine.get());
        this.forgeAndCommonTag(ModTags.RUBY_F, ModTags.RUBIES, ModItems.ruby.get());
        this.forgeAndCommonTag(ModTags.SAPPHIRE_F, ModTags.SAPPHIRES, ModItems.sapphire.get());

        this.tag(ModTags.ORICHALCUM)
                .add(ModItems.orichalcum.get());
        this.tag(ModTags.DRAGONIC)
                .add(ModItems.dragonic.get());

        this.tag(ModTags.MINERALS)
                .add(ModItems.scrapPlus.get())
                .addTag(ModTags.IRON)
                .addTag(ModTags.GOLD)
                .addTag(ModTags.COPPER)
                .addTag(ModTags.BRONZE)
                .addTag(ModTags.SILVER)
                .addTag(ModTags.PLATINUM)
                .addTag(ModTags.ORICHALCUM)
                .addTag(ModTags.DRAGONIC);
        this.forgeAndCommonTag(Tags.Items.GEMS, ModTags.JEWELS,
                ModItems.amethyst.get(),
                ModItems.aquamarine.get(),
                ModItems.ruby.get(),
                ModItems.sapphire.get(),
                ModItems.coreRed.get(),
                ModItems.coreBlue.get(),
                ModItems.coreYellow.get(),
                ModItems.coreGreen.get(),
                ModItems.crystalSkull.get());
        this.tag(ModTags.JEWELS).add(Items.EMERALD, Items.DIAMOND);
        this.tag(ModTags.CRYSTALS)
                .add(ModItems.crystalWater.get())
                .add(ModItems.crystalEarth.get())
                .add(ModItems.crystalFire.get())
                .add(ModItems.crystalWind.get())
                .add(ModItems.crystalLight.get())
                .add(ModItems.crystalDark.get())
                .add(ModItems.crystalLove.get())
                .add(ModItems.crystalSmall.get())
                .add(ModItems.crystalBig.get())
                .add(ModItems.crystalMagic.get())
                .add(ModItems.crystalRune.get())
                .add(ModItems.crystalElectro.get());
        this.tag(ModTags.STICKS)
                .addOptionalTag(Tags.Items.RODS_WOODEN.location())
                .add(Items.STICK,
                        ModItems.stickThick.get(),
                        ModItems.hornInsect.get(),
                        ModItems.hornRigid.get(),
                        ModItems.hornDevil.get(),
                        ModItems.plantStem.get(),
                        ModItems.hornBull.get(),
                        ModItems.movingBranch.get());
        this.tag(ModTags.LIQUIDS)
                .add(ModItems.glue.get(),
                        ModItems.devilBlood.get(),
                        ModItems.paraPoison.get(),
                        ModItems.poisonKing.get());
        this.tag(ModTags.FEATHERS)
                .add(Items.FEATHER,
                        ModItems.featherBlack.get(),
                        ModItems.featherThunder.get(),
                        ModItems.featherYellow.get(),
                        ModItems.dragonFin.get())
                .addOptionalTag(Tags.Items.FEATHERS.location());
        this.tag(ModTags.SHELLS_BONES)
                .add(ModItems.turtleShell.get(),
                        ModItems.fishFossil.get(),
                        ModItems.skull.get(),
                        ModItems.dragonBones.get(),
                        ModItems.tortoiseShell.get());
        this.tag(ModTags.STONES)
                .add(ModItems.rock.get(),
                        ModItems.stoneRound.get(),
                        ModItems.stoneTiny.get(),
                        ModItems.stoneGolem.get(),
                        ModItems.tabletGolem.get(),
                        ModItems.stoneSpirit.get(),
                        ModItems.tabletTruth.get());
        this.tag(ModTags.STRINGS)
                .add(Items.STRING,
                        ModItems.yarn.get(),
                        ModItems.oldBandage.get(),
                        ModItems.ambrosiasThorns.get(),
                        ModItems.threadSpider.get(),
                        ModItems.puppetryStrings.get(),
                        ModItems.vine.get(),
                        ModItems.tailScorpion.get(),
                        ModItems.strongVine.get(),
                        ModItems.threadPretty.get(),
                        ModItems.tailChimera.get());
        this.tag(ModTags.SHARDS)
                .add(Items.FLINT,
                        ModItems.arrowHead.get(),
                        ModItems.bladeShard.get(),
                        ModItems.brokenHilt.get(),
                        ModItems.brokenBox.get(),
                        ModItems.bladeGlistening.get(),
                        ModItems.greatHammerShard.get(),
                        ModItems.hammerPiece.get(),
                        ModItems.shoulderPiece.get(),
                        ModItems.piratesArmor.get(),
                        ModItems.screwRusty.get(),
                        ModItems.screwShiny.get(),
                        ModItems.rockShardLeft.get(),
                        ModItems.rockShardRight.get(),
                        ModItems.MTGUPlate.get(),
                        ModItems.brokenIceWall.get());
        this.tag(ModTags.FURS)
                .add(ModItems.furSmall.get(),
                        ModItems.furMedium.get(),
                        ModItems.furLarge.get(),
                        ModItems.fur.get(),
                        ModItems.furball.get(),
                        ModItems.downYellow.get(),
                        ModItems.furQuality.get(),
                        ModItems.downPenguin.get(),
                        ModItems.lightningMane.get(),
                        ModItems.furRedLion.get(),
                        ModItems.furBlueLion.get(),
                        ModItems.chestHair.get());
        this.tag(ModTags.POWDERS)
                .add(ModItems.spore.get(),
                        ModItems.powderPoison.get(),
                        ModItems.sporeHoly.get(),
                        ModItems.fairyDust.get(),
                        ModItems.fairyElixir.get(),
                        ModItems.root.get(),
                        ModItems.powderMagic.get(),
                        ModItems.powderMysterious.get(),
                        ModItems.magic.get(),
                        ModItems.ashEarth.get(),
                        ModItems.ashFire.get(),
                        ModItems.ashWater.get(),
                        ModItems.turnipsMiracle.get(),
                        ModItems.melodyBottle.get());
        this.tag(ModTags.CLOTHS)
                .add(Items.LEATHER,
                        ModItems.clothCheap.get(),
                        ModItems.clothQuality.get(),
                        ModItems.clothQualityWorn.get(),
                        ModItems.clothSilk.get(),
                        ModItems.ghostHood.get(),
                        ModItems.gloveGiant.get(),
                        ModItems.gloveBlueGiant.get(),
                        ModItems.carapaceInsect.get(),
                        ModItems.carapacePretty.get(),
                        ModItems.clothAncientOrc.get());
        this.tag(ModTags.CLAWS_FANGS)
                .add(ModItems.jawInsect.get(),
                        ModItems.clawPanther.get(),
                        ModItems.clawMagic.get(),
                        ModItems.fangWolf.get(),
                        ModItems.fangGoldWolf.get(),
                        ModItems.clawPalm.get(),
                        ModItems.clawMalm.get(),
                        ModItems.giantsNail.get(),
                        ModItems.clawChimera.get(),
                        ModItems.tuskIvory.get(),
                        ModItems.tuskUnbrokenIvory.get(),
                        ModItems.scorpionPincer.get(),
                        ModItems.dangerousScissors.get(),
                        ModItems.propellorCheap.get(),
                        ModItems.propellorQuality.get(),
                        ModItems.fangDragon.get(),
                        ModItems.jawQueen.get(),
                        ModItems.fangDragon.get(),
                        ModItems.giantsNailBig.get());
        this.tag(ModTags.SCALES)
                .add(ModItems.scaleWet.get(),
                        ModItems.scaleGrimoire.get(),
                        ModItems.scaleDragon.get(),
                        ModItems.scaleCrimson.get(),
                        ModItems.scaleBlue.get(),
                        ModItems.scaleGlitter.get(),
                        ModItems.scaleLove.get(),
                        ModItems.scaleBlack.get(),
                        ModItems.scaleFire.get(),
                        ModItems.scaleEarth.get(),
                        ModItems.scaleLegend.get());

        this.tag(ModTags.HIGH_TIER_TOOLS)
                .add(ModItems.hoePlatinum.get())
                .add(ModItems.wateringCanPlatinum.get())
                .add(ModItems.sicklePlatinum.get())
                .add(ModItems.hammerPlatinum.get())
                .add(ModItems.axePlatinum.get())
                .add(ModItems.fishingRodPlatinum.get());

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

        this.forgeAndCommonTag(Tags.Items.EGGS, ModTags.EGGS, ModItems.eggS.get(),
                ModItems.eggM.get(), ModItems.eggL.get(), Items.EGG);
        this.tag(ModTags.MILKS)
                .add(ModItems.milkS.get())
                .add(ModItems.milkM.get())
                .add(ModItems.milkL.get())
                .add(Items.MILK_BUCKET);

        this.tag(ModTags.tamingTag(ModEntities.WOOLY.get()))
                .addTag(ModTags.SHEARS)
                .addTag(ItemTags.WOOL)
                .add(Items.WHEAT);
        this.tag(ModTags.tamingTag(ModEntities.ORC.get()))
                .add(ModItems.cheapBracelet.get())
                .add(ModItems.clothCheap.get())
                .add(ModItems.oldBandage.get());
        this.tag(ModTags.tamingTag(ModEntities.ORC_ARCHER.get()))
                .add(Items.GUNPOWDER)
                .add(Items.ARROW)
                .add(ModItems.arrowHead.get())
                .add(ModItems.recoveryPotion.get());
        this.tag(ModTags.tamingTag(ModEntities.ANT.get()))
                .add(ModItems.carapaceInsect.get())
                .add(ModItems.carapacePretty.get())
                .add(ModItems.jawInsect.get())
                .add(ModItems.jawQueen.get());
        this.tag(ModTags.tamingTag(ModEntities.BEETLE.get()))
                .add(ModItems.carapaceInsect.get())
                .add(ModItems.carapacePretty.get())
                .add(ModItems.hornInsect.get())
                .add(ModItems.hornRigid.get());
        this.tag(ModTags.tamingTag(ModEntities.BIG_MUCK.get()))
                .add(Items.BROWN_MUSHROOM)
                .add(Items.RED_MUSHROOM)
                .add(ModItems.mushroom.get())
                .add(ModItems.monarchMushroom.get())
                .add(ModItems.spore.get());
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
                .add(ModItems.tortoiseShell.get());
        this.tag(ModTags.tamingTag(ModEntities.SKY_FISH.get()))
                .add(Items.KELP);
        this.tag(ModTags.tamingTag(ModEntities.WEAGLE.get()))
                .addTag(ModTags.FEATHERS);
        this.tag(ModTags.tamingTag(ModEntities.GOBLIN.get()))
                .add(ModItems.oldBandage.get());
        this.tag(ModTags.tamingTag(ModEntities.GOBLIN_ARCHER.get()))
                .addTag(ModTags.tamingTag(ModEntities.GOBLIN.get()));
        this.tag(ModTags.tamingTag(ModEntities.DUCK.get()))
                .add(ModItems.fur.get())
                .add(Items.FEATHER);
        this.tag(ModTags.tamingTag(ModEntities.FAIRY.get()))
                .add(ModItems.fairyDust.get());
        this.tag(ModTags.tamingTag(ModEntities.GHOST.get()))
                .add(ModItems.ghostHood.get());
        this.tag(ModTags.tamingTag(ModEntities.SPIRIT.get()))
                .add(ModItems.crystalDark.get());
        this.tag(ModTags.tamingTag(ModEntities.GHOST_RAY.get()))
                .add(ModItems.ghostHood.get());
        this.tag(ModTags.tamingTag(ModEntities.SPIDER.get()))
                .add(Items.STRING)
                .add(ModItems.threadSpider.get());
        this.tag(ModTags.tamingTag(ModEntities.SHADOW_PANTHER.get()))
                .add(ModItems.fur.get())
                .add(ModItems.clawPanther.get());
        this.tag(ModTags.tamingTag(ModEntities.MONSTER_BOX.get()))
                .add(ModItems.failedDish.get())
                .add(ModItems.disastrousDish.get());
        this.tag(ModTags.tamingTag(ModEntities.GOBBLE_BOX.get()))
                .add(ModItems.failedDish.get())
                .add(ModItems.disastrousDish.get());
        this.tag(ModTags.tamingTag(ModEntities.KILLER_ANT.get()))
                .add(ModItems.carapacePretty.get());
        this.tag(ModTags.tamingTag(ModEntities.HIGH_ORC.get()))
                .addTag(ModTags.tamingTag(ModEntities.ORC.get()));
        this.tag(ModTags.tamingTag(ModEntities.ORC_HUNTER.get()))
                .addTag(ModTags.tamingTag(ModEntities.ORC_ARCHER.get()));
        this.tag(ModTags.tamingTag(ModEntities.HORNET.get()))
                .add(Items.HONEY_BOTTLE)
                .add(Items.HONEYCOMB);
        this.tag(ModTags.tamingTag(ModEntities.SILVER_WOLF.get()))
                .add(Items.BONE)
                .add(ModItems.fangWolf.get());
        this.tag(ModTags.tamingTag(ModEntities.LEAF_BALL.get()))
                .addTag(ItemTags.LEAVES);
        this.tag(ModTags.tamingTag(ModEntities.FURPY.get()))
                .addTag(ModTags.tamingTag(ModEntities.CHIPSQUEEK.get()));
        this.tag(ModTags.tamingTag(ModEntities.PALM_CAT.get()))
                .add(ModItems.fur.get())
                .add(Items.CAKE)
                .add(ModItems.cake.get());
        this.tag(ModTags.tamingTag(ModEntities.MINO.get()))
                .add(ModItems.grapes.get());
        this.tag(ModTags.tamingTag(ModEntities.TRICKY_MUCK.get()))
                .add(ModItems.powderPoison.get());
        this.tag(ModTags.tamingTag(ModEntities.FLOWER_LILY.get()))
                .addTag(ItemTags.FLOWERS);
        this.tag(ModTags.tamingTag(ModEntities.KING_WOOLY.get()))
                .addTag(ModTags.tamingTag(ModEntities.WOOLY.get()));
        this.tag(ModTags.tamingTag(ModEntities.BUFFALOO.get()))
                .add(ModItems.hornBull.get());
        this.tag(ModTags.tamingTag(ModEntities.GOBLIN_PIRATE.get()))
                .addTag(ModTags.tamingTag(ModEntities.GOBLIN.get()));
        this.tag(ModTags.tamingTag(ModEntities.GOBLIN_GANGSTER.get()))
                .addTag(ModTags.tamingTag(ModEntities.GOBLIN.get()));
        this.tag(ModTags.tamingTag(ModEntities.IGNIS.get()))
                .add(ModItems.crystalFire.get())
                .add(ModItems.crystalMagic.get());
        this.tag(ModTags.tamingTag(ModEntities.SCORPION.get()))
                .add(ModItems.tailScorpion.get());
        this.tag(ModTags.tamingTag(ModEntities.TROLL.get()))
                .add(ModItems.giantsNail.get())
                .add(ModItems.gloveGiant.get())
                .add(ModItems.hammerPiece.get());
        this.tag(ModTags.tamingTag(ModEntities.FLOWER_LION.get()))
                .addTag(ModTags.tamingTag(ModEntities.FLOWER_LILY.get()));
        this.tag(ModTags.tamingTag(ModEntities.TOMATO_GHOST.get()))
                .addTag(tempKeyFabric("vegetables/tomato"));

        this.tag(ModTags.tamingTag(ModEntities.AMBROSIA.get()))
                .add(ModItems.toyherbGiant.get());
        this.tag(ModTags.tamingTag(ModEntities.THUNDERBOLT.get()))
                .add(ModItems.carrotGiant.get());
        this.tag(ModTags.tamingTag(ModEntities.MARIONETTA.get()))
                .add(Items.CAKE)
                .add(ModItems.cheesecake.get())
                .add(ModItems.chocolateCake.get())
                .add(ModItems.applePie.get());
        this.tag(ModTags.tamingTag(ModEntities.DEAD_TREE.get()))
                .add(ModItems.greenifierPlus.get());
        this.tag(ModTags.tamingTag(ModEntities.CHIMERA.get()))
                .add(ModItems.royalCurry.get())
                .add(ModItems.ultimateCurry.get());
        this.tag(ModTags.tamingTag(ModEntities.RACCOON.get()))
                .add(ModItems.udon.get())
                .add(ModItems.tempuraUdon.get())
                .add(ModItems.curryUdon.get());
        this.tag(ModTags.tamingTag(ModEntities.SKELEFANG.get()))
                .add(Items.DRAGON_HEAD);

        TagKey<Item> temp = tempKeyForge("fruits/grapes");
        this.tag(ModTags.GRAPES)
                .add(ModItems.grapes.get())
                .addTag(temp);
        this.tag(temp)
                .add(ModItems.grapes.get());

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
        }
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
