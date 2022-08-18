package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
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
        ModItems.TREASURE.forEach((key, supList) -> supList.forEach(sup -> this.tag(key).add(sup.get())));

        //Forge copy tags
        this.tag(ModTags.iron)
                .add(Items.IRON_INGOT)
                .addOptional(Tags.Items.INGOTS_IRON.location());
        this.tag(ModTags.gold)
                .add(Items.GOLD_INGOT)
                .addOptional(Tags.Items.INGOTS_GOLD.location());
        this.tag(ModTags.copper)
                .add(Items.COPPER_INGOT)
                .addOptional(Tags.Items.INGOTS_COPPER.location());
        this.tag(ModTags.copperBlock)
                .add(Items.COPPER_BLOCK)
                .add(Items.CUT_COPPER)
                .addOptional(Tags.Items.STORAGE_BLOCKS_COPPER.location());
        this.tag(ModTags.emerald)
                .add(Items.EMERALD)
                .addOptional(Tags.Items.GEMS_EMERALD.location());
        this.tag(ModTags.chest)
                .add(Items.ENDER_CHEST)
                .add(Items.TRAPPED_CHEST)
                .add(Items.CHEST)
                .add(Items.TRAPPED_CHEST)
                .addOptional(Tags.Items.CHESTS.location());
        this.tag(ModTags.shears)
                .add(Items.SHEARS)
                .addOptional(Tags.Items.SHEARS.location());
        this.tag(ModTags.cobblestone)
                .add(Items.COBBLESTONE)
                .add(Items.INFESTED_COBBLESTONE)
                .add(Items.MOSSY_COBBLESTONE)
                .add(Items.COBBLED_DEEPSLATE)
                .addOptional(Tags.Items.COBBLESTONE.location());
        this.tag(ModTags.wood_rod)
                .add(Items.STICK)
                .addOptional(Tags.Items.RODS_WOODEN.location());
        this.tag(ModTags.slime)
                .add(Items.SLIME_BALL)
                .add(ModItems.glue.get())
                .addOptional(Tags.Items.SLIMEBALLS.location());

        this.forgeAndCommonTag(ModTags.bronzeF, ModTags.bronze, ModItems.bronze.get());
        this.forgeAndCommonTag(ModTags.silverF, ModTags.silver, ModItems.silver.get());
        this.forgeAndCommonTag(ModTags.platinumF, ModTags.platinum, ModItems.platinum.get());

        this.forgeAndCommonTag(ModTags.amethystF, ModTags.amethyst, ModItems.amethyst.get());
        this.forgeAndCommonTag(ModTags.aquamarineF, ModTags.aquamarine, ModItems.aquamarine.get());
        this.forgeAndCommonTag(ModTags.rubyF, ModTags.ruby, ModItems.ruby.get());
        this.forgeAndCommonTag(ModTags.sapphireF, ModTags.sapphire, ModItems.sapphire.get());

        this.tag(ModTags.orichalcum)
                .add(ModItems.orichalcum.get());
        this.tag(ModTags.dragonic)
                .add(ModItems.dragonic.get());

        this.tag(ModTags.minerals)
                .add(ModItems.scrapPlus.get())
                .addTag(ModTags.iron)
                .addTag(ModTags.gold)
                .addTag(ModTags.copper)
                .addTag(ModTags.bronze)
                .addTag(ModTags.silver)
                .addTag(ModTags.platinum)
                .addTag(ModTags.orichalcum)
                .addTag(ModTags.dragonic);
        this.forgeAndCommonTag(Tags.Items.GEMS, ModTags.jewels,
                ModItems.amethyst.get(),
                ModItems.aquamarine.get(),
                ModItems.ruby.get(),
                ModItems.sapphire.get(),
                ModItems.coreRed.get(),
                ModItems.coreBlue.get(),
                ModItems.coreYellow.get(),
                ModItems.coreGreen.get(),
                ModItems.crystalSkull.get());
        this.tag(ModTags.jewels).add(Items.EMERALD, Items.DIAMOND);
        this.tag(ModTags.crystals)
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
        this.tag(ModTags.sticks)
                .addOptionalTag(Tags.Items.RODS_WOODEN.location())
                .add(Items.STICK,
                        ModItems.stickThick.get(),
                        ModItems.hornInsect.get(),
                        ModItems.hornRigid.get(),
                        ModItems.hornDevil.get(),
                        ModItems.plantStem.get(),
                        ModItems.hornBull.get(),
                        ModItems.movingBranch.get());
        this.tag(ModTags.liquids)
                .add(ModItems.glue.get(),
                        ModItems.devilBlood.get(),
                        ModItems.paraPoison.get(),
                        ModItems.poisonKing.get());
        this.tag(ModTags.feathers)
                .add(Items.FEATHER,
                        ModItems.featherBlack.get(),
                        ModItems.featherThunder.get(),
                        ModItems.featherYellow.get(),
                        ModItems.dragonFin.get())
                .addOptionalTag(Tags.Items.FEATHERS.location());
        this.tag(ModTags.shellBone)
                .add(ModItems.turtleShell.get(),
                        ModItems.fishFossil.get(),
                        ModItems.skull.get(),
                        ModItems.dragonBones.get(),
                        ModItems.tortoiseShell.get());
        this.tag(ModTags.stones)
                .add(ModItems.rock.get(),
                        ModItems.stoneRound.get(),
                        ModItems.stoneTiny.get(),
                        ModItems.stoneGolem.get(),
                        ModItems.tabletGolem.get(),
                        ModItems.stoneSpirit.get(),
                        ModItems.tabletTruth.get());
        this.tag(ModTags.strings)
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
        this.tag(ModTags.shards)
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
        this.tag(ModTags.furs)
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
        this.tag(ModTags.powders)
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
        this.tag(ModTags.cloths)
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
        this.tag(ModTags.clawFangs)
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
        this.tag(ModTags.scales)
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

        this.forgeAndCommonTag(Tags.Items.EGGS, ModTags.eggs, ModItems.eggS.get(),
                ModItems.eggM.get(), ModItems.eggL.get(), Items.EGG);
        this.tag(ModTags.milk)
                .add(ModItems.milkS.get())
                .add(ModItems.milkM.get())
                .add(ModItems.milkL.get())
                .add(Items.MILK_BUCKET);

        this.tag(ModTags.woolyTamer)
                .addTag(ModTags.shears)
                .addTag(ItemTags.WOOL);
        this.tag(ModTags.marionettaTamer)
                .add(Items.CAKE)
                .add(ModItems.cheesecake.get())
                .add(ModItems.chocolateCake.get())
                .add(ModItems.applePie.get());
    }

    protected void forgeAndCommonTag(TagKey<Item> forge, TagKey<Item> common, Item... items) {
        TagAppender<Item> a = this.tag(forge);
        for (Item item : items)
            a.add(item);
        a = this.tag(common);
        for (Item item : items)
            a.add(item);
        a.addOptionalTag(forge.location());
    }
}
