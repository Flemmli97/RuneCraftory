package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.ShopItemProperties;
import io.github.flemmli97.runecraftory.api.datapack.provider.ShopItemProvider;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModNPCJobs;
import net.minecraft.data.DataGenerator;

public class ShopItemGen extends ShopItemProvider {

    public ShopItemGen(DataGenerator gen) {
        super(gen, RuneCraftory.MODID);
    }

    @Override
    protected void add() {
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.turnipSeeds.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.turnipPinkSeeds.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.cabbageSeeds.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.pinkMelonSeeds.get(), ShopItemProperties.UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.hotHotSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.goldTurnipSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.goldPotatoSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.goldPumpkinSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.goldCabbageSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.bokChoySeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.leekSeeds.get(), UnlockType.DEFAULT);
        // this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.radishSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.greenPepperSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.spinachSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.yamSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.eggplantSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.pineappleSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.pumpkinSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.onionSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.cornSeeds.get(), UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.tomatoSeeds.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.cucumberSeeds.get(), ShopItemProperties.UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.fodderSeeds.get(), UnlockType.DEFAULT);

        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.turnip.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.turnipPink.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.cabbage.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.pinkMelon.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.pineapple.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.strawberry.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.goldenTurnip.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.goldenPotato.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.goldenPumpkin.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.goldenCabbage.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.hotHotFruit.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.bokChoy.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.leek.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.radish.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.spinach.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.greenPepper.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.yam.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.eggplant.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.tomato.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.corn.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.cucumber.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.pumpkin.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.onion.get());

        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.toyherbSeeds.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.moondropSeeds.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.pinkCatSeeds.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.charmBlueSeeds.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.lampGrassSeeds.get(), ShopItemProperties.UnlockType.AFTER_UNLOCK);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.cherryGrassSeeds.get(), ShopItemProperties.UnlockType.AFTER_UNLOCK);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.pomPomGrassSeeds.get(), ShopItemProperties.UnlockType.AFTER_UNLOCK);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.autumnGrassSeeds.get(), ShopItemProperties.UnlockType.AFTER_UNLOCK);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.noelGrassSeeds.get(), ShopItemProperties.UnlockType.AFTER_UNLOCK);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.fireflowerSeeds.get(), ShopItemProperties.UnlockType.AFTER_UNLOCK);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.fourLeafCloverSeeds.get(), ShopItemProperties.UnlockType.AFTER_UNLOCK);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.ironleafSeeds.get(), ShopItemProperties.UnlockType.AFTER_UNLOCK);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.whiteCrystalSeeds.get(), ShopItemProperties.UnlockType.AFTER_UNLOCK);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.redCrystalSeeds.get(), ShopItemProperties.UnlockType.AFTER_UNLOCK);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.greenCrystalSeeds.get(), ShopItemProperties.UnlockType.AFTER_UNLOCK);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.blueCrystalSeeds.get(), ShopItemProperties.UnlockType.AFTER_UNLOCK);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.emeryFlowerSeeds.get(), ShopItemProperties.UnlockType.AFTER_UNLOCK);

        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.toyherb.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.moondropFlower.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.pinkCat.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.charmBlue.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.lampGrass.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.cherryGrass.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.pomPomGrass.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.autumnGrass.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.noelGrass.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.fireflower.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.fourLeafClover.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.ironleaf.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.whiteCrystal.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.redCrystal.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.greenCrystal.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.blueCrystal.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.emeryFlower.get());

        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.formularA.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.formularB.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.formularC.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.minimizer.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.giantizer.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.greenifier.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.greenifierPlus.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.wettablePowder.get(), ShopItemProperties.UnlockType.DEFAULT);

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.hoeScrap.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.hoeIron.get());
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.hoeSilver.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.wateringCanScrap.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.wateringCanIron.get());
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.wateringCanSilver.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.sickleScrap.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.sickleIron.get());
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.sickleSilver.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.hammerScrap.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.hammerIron.get());
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.hammerSilver.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.axeScrap.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.axeIron.get());
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.axeSilver.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.fishingRodScrap.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.fishingRodIron.get());
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.fishingRodSilver.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.mobStaff.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.brush.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.glass.get(), ShopItemProperties.UnlockType.DEFAULT);

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.broadSword.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.cutlass.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.claymore.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.greatSword.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.spear.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.needleSpear.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.battleAxe.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.poleAxe.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.battleHammer.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.warHammer.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.shortDagger.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.ironEdge.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.leatherGlove.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.gloves.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.rod.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.aquamarineRod.get());

        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.recoveryPotion.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.healingPotion.get());
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.mysteryPotion.get());
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.magicalPotion.get());
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.roundoff.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.paraGone.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.coldMed.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.antidote.get(), ShopItemProperties.UnlockType.DEFAULT);

        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.elliLeaves.get());
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.whiteGrass.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.indigoGrass.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.purpleGrass.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.greenGrass.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.blueGrass.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.yellowGrass.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.redGrass.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.orangeGrass.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.blackGrass.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.antidoteGrass.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.medicinalHerb.get(), ShopItemProperties.UnlockType.DEFAULT);

        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.fireBallSmall.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.fireBallBig.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.explosion.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.waterLaser.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.parallelLaser.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.deltaLaser.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.screwRock.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.earthSpike.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.avengerRock.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.sonicWind.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.doubleSonic.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.penetrateSonic.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.lightBarrier.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.shine.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.prism.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.darkSnake.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.darkBall.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.darkness.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.cure.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.cureAll.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.cureMaster.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.mediPoison.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.mediPara.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.mediSeal.get());

        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.powerWave.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.dashSlash.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.rushAttack.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.roundBreak.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.mindThrust.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.gust.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.storm.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.blitz.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.twinAttack.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.railStrike.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.windSlash.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.flashStrike.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.naiveBlade.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.steelHeart.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.deltaStrike.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.hurricane.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.reaperSlash.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.millionStrike.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.axelDisaster.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.stardustUpper.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.tornadoSwing.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.grandImpact.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.gigaSwing.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.upperCut.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.doubleKick.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.straightPunch.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.nekoDamashi.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.rushPunch.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.cyclone.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.rapidMove.get());
    }
}
